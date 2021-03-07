package io.github.dayco.uaa.user.application;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.dayco.uaa.user.domain.User;
import io.github.dayco.uaa.user.infra.UserJpaRepository;
import io.github.dayco.uaa.user.ui.dto.ProfileDto;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.http.Method;

@Service
public class ProfileService {

    private final UserJpaRepository userJpaRepository;

    public ProfileService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private MinioClient minioClient;

    @Value("${file.upload.url}")
    private String FILE_UPLOAD_URL;
    @Value("${file.upload.accessKey}")
    private String FILE_UPLOAD_ACCESSKEY;
    @Value("${file.upload.secretKey}")
    private String FILE_UPLOAD_SECRETKEY;
    private final static String FILE_BUCKET = "profile";

    @PostConstruct
    private void initialize() {
        // Create a minioClient with the MinIO server playground, its access key and secret key.
        minioClient =
                MinioClient.builder()
                           .endpoint(FILE_UPLOAD_URL)
                           .credentials(FILE_UPLOAD_ACCESSKEY, FILE_UPLOAD_SECRETKEY)
                           .build();
    }

    public User change(ProfileDto profileDto, User user) throws Exception {
        String fileSavedPath = null;
        String profileUrl = null;
        /**
         * 브라우저는 보안상 파일을 조작할 수 없다. 때문에 모든 값은 읽기 전용 이다.
         * name : 파일 이름
         * lastModified : 파일을 마지막으로 수정한 Unix Time
         * lastModifiedDate : 파일을 마지막으로 수정한 Date 객체
         * size : 파일의 크기 (Byte 값)
         * type : MIME 유형
         *
         * 그러므로, 위와 같은 File 내용이 넘어오고, 파일 경로를 알 수가 없으므로,(fakepath)
         * 해당 서버에 파일 경로에 파일 이미지를 생성한 후에 별도 서버로 넘긴다.
         */
        if( profileDto.getFileName().isPresent()) {
            String fileName = profileDto.getFileName().get();
            fileSavedPath = FileSystemView
                                    .getFileSystemView()
                                    .getHomeDirectory()
                            + File.separator + fileName;
            File file = new File(fileSavedPath);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                // Base64 를 일반 파일로 변환하고 저장
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] decodedBytes = decoder.decode(profileDto.getFileBase64().get().getBytes());
                fileOutputStream.write(decodedBytes);
            }

            // Make 'profile' bucket if not exist.
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(FILE_BUCKET).build());
            if (!found) {
                // Make a new bucket called 'profile'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(FILE_BUCKET).build());
            } else {
                logger.info("Bucket 'profile' already exists.");
            }

            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                                    .bucket(FILE_BUCKET)
                                    .object(fileName)
                                    .filename(fileSavedPath)
                                    .build());

            /**
             * expire 시간이 7 일 이므로, 다시 접속 시에 url 을 갱신시킨다.
             */
            profileUrl =
                    minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                                     .method(Method.GET)
                                                     .bucket(FILE_BUCKET)
                                                     .object(fileName)
                                                     .build());
        }
        return change(user.getUserId(),
                      profileDto.getEmail(),
                      profileDto.getPassword(),
                      profileDto.getFileName(),
                      (StringUtils.isNotEmpty(profileUrl)) ? Optional.of(profileUrl) : Optional.empty());
    }

    @Transactional
    public User change(String userId,
                       Optional<String> email,
                       Optional<String> password,
                       Optional<String> fileName,
                       Optional<String> profileUrl) throws Exception {
        User user = userJpaRepository.findByUserId(userId)
                         .orElseThrow(() -> new IllegalArgumentException(
                                               "There are not userId = " + userId));
        return userJpaRepository.save(
                user.update(userId, email, password, fileName, profileUrl));
    }



    @Transactional
    public void changeProfileUrl(Optional<User> userOpt, String userId) throws Exception {
        if(userOpt.isPresent()) {
            User user = userOpt.get();
            if(StringUtils.isNotEmpty(user.getProfileFileName())) {
                Optional<String> profileUrl = Optional.ofNullable(
                        minioClient.getPresignedObjectUrl(
                                GetPresignedObjectUrlArgs.builder()
                                                         .method(Method.GET)
                                                         .bucket("profile")
                                                         .object(user.getProfileFileName())
                                                         .build()));
                userJpaRepository.save(
                        user.profileUpdate(userId, profileUrl));
            }
        }
    }

    @Transactional
    public void changeProfileUrl(String userId) throws Exception {
        changeProfileUrl(userJpaRepository.findByUserId(userId), userId);
    }
}
