package io.github.dayco.uaa.user.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.dayco.uaa.user.domain.Profile;
import io.github.dayco.uaa.user.domain.User;
import io.github.dayco.uaa.user.repository.ProfileJpaRepository;
import io.github.dayco.uaa.user.repository.UserJpaRepository;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;

@Service
public class ProfileService {

    private final UserJpaRepository userJpaRepository;
    private final ProfileJpaRepository profileJpaRepository;

    public ProfileService(UserJpaRepository userJpaRepository,
                          ProfileJpaRepository profileJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.profileJpaRepository = profileJpaRepository;
    }

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private MinioClient minioClient;

    @Value("${file.upload.url}")
    private String fileUploadUrl;
    @Value("${file.upload.accessKey}")
    private String fileUploadAccessKey;
    @Value("${file.upload.secretKey}")
    private String fileUploadSecretKey;
    @Value("${file.upload.savedDirectory}")
    private String fileUploadSavedDirectory;

    private final static String FILE_BUCKET = "profile";

    @PostConstruct
    private void initialize() {
        // Create a minioClient with the MinIO server playground, its access key and secret key.
        minioClient =
                MinioClient.builder()
                           .endpoint(fileUploadUrl)
                           .credentials(fileUploadAccessKey, fileUploadSecretKey)
                           .build();
    }

    /**
     * 파일 Base64 Decoding 시킨 후에 파일 저장 경로에 저장시킨다.
     * @param fileBase64
     * @param localSavedFile
     * @throws IOException
     */
    private void writeFile(String fileBase64, File localSavedFile) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(localSavedFile)) {
            // Base64 를 일반 파일로 변환하고 저장
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decodedBytes = decoder.decode(fileBase64.getBytes());
            fileOutputStream.write(decodedBytes);
        }
    }

    private void uploadProfile(String fileName, String localSavedFileStr)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException,
                   NoSuchAlgorithmException, ServerException, InternalException, XmlParserException,
                   ErrorResponseException {
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
                                .filename(localSavedFileStr)
                                .build());
    }

    public Profile upload(String fileBase64, String fileName, User user) throws Exception {

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
        String localSavedFileStr = fileUploadSavedDirectory + File.separator + fileName;
        File localSavedFile = new File(localSavedFileStr);
        writeFile(fileBase64, localSavedFile);
        uploadProfile(fileName, localSavedFileStr);

        /**
         * expire 시간이 7 일 이므로, 다시 접속 시에 url 을 갱신시킨다.
         */
        String imageUrl =
                minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                                 .method(Method.GET)
                                                 .bucket(FILE_BUCKET)
                                                 .object(fileName)
                                                 .build());

        logger.debug("User Id : {}, File Name : {}, ImageUrl : {}",
                     user.getUserId(), fileName, imageUrl);
        return profileJpaRepository.save(new Profile(user.getUserId(), fileName, imageUrl));
    }

    @Transactional
    public void changeImageUrl(Optional<Profile> profileOpt, String userId) throws Exception {
        if(profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            if(StringUtils.isNotEmpty(profile.getFileName())) {
                String imageUrl = minioClient.getPresignedObjectUrl(
                                GetPresignedObjectUrlArgs.builder()
                                                         .method(Method.GET)
                                                         .bucket("profile")
                                                         .object(profile.getFileName())
                                                         .build());

                profileJpaRepository.save(new Profile(userId, imageUrl));
            }
        }
    }

}
