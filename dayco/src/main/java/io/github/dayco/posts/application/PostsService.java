package io.github.dayco.posts.application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.dayco.external.ui.UserClient;
import io.github.dayco.external.ui.vo.User;
import io.github.dayco.posts.domain.Posts;
import io.github.dayco.posts.domain.PostsComment;
import io.github.dayco.posts.infra.PostsJpaRepository;
import io.github.dayco.posts.ui.dto.PostsCommentDto;
import io.github.dayco.posts.ui.dto.PostsDto;

import io.github.dayco.posts.ui.dto.PostsLikeDto;
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
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final UserClient userClient;
    private final PostsJpaRepository postsJpaRepository;
    private final PostsLikeService postsLikeService;
    private final PostsCommentService postsCommentService;

    private static final int MAX_SHOW_COMMENT_COUNT = 5;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${file.upload.url}")
    private String FILE_UPLOAD_URL;
    @Value("${file.upload.accessKey}")
    private String FILE_UPLOAD_ACCESSKEY;
    @Value("${file.upload.secretKey}")
    private String FILE_UPLOAD_SECRETKEY;

    private final static String FILE_BUCKET = "posts";

    private MinioClient minioClient;

    @PostConstruct
    private void initialize() {
        // Create a minioClient with the MinIO server playground, its access key and secret key.
        minioClient =
                MinioClient.builder()
                           .endpoint(FILE_UPLOAD_URL)
                           .credentials(FILE_UPLOAD_ACCESSKEY, FILE_UPLOAD_SECRETKEY)
                           .build();
    }

    /**
     * 파일 이미지를 저장한다.
     * @param fileNameOpt
     * @param fileBase64
     * @return 파일 저장 경로
     */
    private Optional<String> saveFileImage(Optional<String> fileNameOpt, Optional<String> fileBase64)
            throws Exception {
        String fileSavedPath = null;
        Optional<String> fileSavedUrl = null;
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
        if( fileNameOpt.isPresent()) {
            String fileName = fileNameOpt.get();
            fileSavedPath = FileSystemView
                                    .getFileSystemView()
                                    .getHomeDirectory()
                            + File.separator + fileName;
            File file = new File(fileSavedPath);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                // Base64 를 일반 파일로 변환하고 저장
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] decodedBytes = decoder.decode(fileBase64.get().getBytes());

                fileOutputStream.write(decodedBytes);
            }

            // Make 'posts' bucket if not exist.
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(FILE_BUCKET).build());
            if (!found) {
                // Make a new bucket called 'posts'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(FILE_BUCKET).build());
            } else {
                logger.info("Bucket 'posts' already exists.");
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
            fileSavedUrl = Optional.ofNullable(
                    minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                                     .method(Method.GET)
                                                     .bucket(FILE_BUCKET)
                                                     .object(fileName)
                                                     .build()));
        }
        return fileSavedUrl;
    }

    @Transactional
    public Posts save(PostsDto postsDto) throws Exception {
        Optional<String> fileSavedUrl = saveFileImage(postsDto.getFileName(), postsDto.getFileBase64());
        return postsJpaRepository.save(
                new Posts(postsDto.getTitle(),
                          postsDto.getContent(),
                          postsDto.getAuthor(),
                          postsDto.getFileName(),
                          fileSavedUrl)
        );
    }

    @Transactional
    public Posts save(String title, String content, String author,
                      Optional<String> fileName, Optional<String> fileBase64) throws Exception {
        Optional<String> fileSavedUrl = saveFileImage(fileName, fileBase64);
        return postsJpaRepository.save(
                new Posts(title, content, author,
                          fileName,
                          fileSavedUrl));
    }

    @Transactional
    public Posts update(Long id, String title, String content,
                        Optional<String> fileName, Optional<String> fileBase64) throws Exception {
        Posts posts = postsJpaRepository.findById(id)
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                "There are not posts. id = " + id));
        Optional<String> fileSavedUrl = saveFileImage(fileName, fileBase64);
        posts.update(title, content, fileName, fileSavedUrl);
        return posts;
    }

    public PostsDto findWithCommentsAndLikeCnt(Long id) {
        Posts posts = postsJpaRepository.findById(id)
                                         .orElseThrow(() -> new IllegalArgumentException(
                                                 "There are not posts. id = " + id));

        PostsDto postsDto = new PostsDto(posts);
        postsDto.setPostsLike(new PostsLikeDto(posts.getId(),
                                               postsLikeService.getLikes(posts.getId())));
        postsDto.setPostsComments(
                postsCommentService.getPostsCommentsByPaging(posts.getId(),
                                                             0,
                                                             MAX_SHOW_COMMENT_COUNT)
                                   .get().map(postsComment -> {
                    return new PostsCommentDto(postsComment,
                                               userClient.getUser(postsComment.getAuthor()).get());
                }).collect(Collectors.toList()));
        return postsDto;
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsJpaRepository.findById(id)
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                "There are not posts. id = " + id));
        postsJpaRepository.delete(posts);
    }

    @Transactional(readOnly = true)
    public List<PostsDto> findAll() throws Exception {
        return postsJpaRepository.findAllDesc()
                .stream().map(posts -> {
                    /**
                     * Posts 이미지 정보를 얻어 올 때, Expire 시간이 7 일이므로, URL 을 갱신시킨다.
                     * @todo 추후에 Redis 로 Expire 지나기 전에 변경시키는 형태로 변경
                     */
                    if(StringUtils.isNotEmpty(posts.getFileName())) {
                        try {
                            String filePathUrl =
                                    minioClient.getPresignedObjectUrl(
                                            GetPresignedObjectUrlArgs.builder()
                                                                     .method(Method.GET)
                                                                     .bucket(FILE_BUCKET)
                                                                     .object(posts.getFileName())
                                                                     .build());
                            postsJpaRepository.save(posts.updateFileSavedUrl(filePathUrl));
                        } catch (Exception e) {
                            logger.error("posts filename ::" + posts.getFileName());
                        }
                    }

                    PostsDto postsDto = new PostsDto(posts);
                    postsDto.setPostsLike(new PostsLikeDto(posts.getId(),
                                                           postsLikeService.getLikes(posts.getId())));
                    postsDto.setPostsComments(
                            postsCommentService.getPostsCommentsByPaging(posts.getId(),
                                                                         0,
                                                                         MAX_SHOW_COMMENT_COUNT)
                            .get().map(postsComment -> {
                                return new PostsCommentDto(postsComment,
                                                    userClient.getUser(postsComment.getAuthor()).get());
                            }).collect(Collectors.toList()));
                    return postsDto;
                }).collect(Collectors.toList());
    }

    public long increaseLike(Long id, String userId) {
        return postsLikeService.increaseLike(id, userId);
    }

    public long decreaseLike(Long id, String userId) {
        return postsLikeService.decreaseLike(id, userId);
    }

    public long getLikes(Long id) {
        return postsLikeService.getLikes(id);
    }

    public List<PostsCommentDto> getPostsComments(List<Long> postsIds, int page, int rowNum) {
        List<PostsComment> postsComments = postsCommentService.getPostsComments(postsIds, page, rowNum);
        List<PostsCommentDto> postsCommentDtos = postsComments.stream().map(postsComment -> {
            return new PostsCommentDto(
                    postsComment.getPosts().getId(),
                    postsComment.getId(),
                    userClient.getUser(postsComment.getAuthor()).get(),
                    postsComment.getComment(),
                    postsComment.getCreatedDate(),
                    postsComment.getModifiedDate());
       }).collect(Collectors.toList());
        return postsCommentDtos;
    }

    public Page<PostsCommentDto> getPageOfComments(Long postsId, int page, int rowNum) {
        Page<PostsComment> pageOfComments = postsCommentService.getPostsCommentsByPaging(postsId, page, rowNum);
        Page<PostsCommentDto> postsCommentDtos = pageOfComments.map(postsComment -> {
            return new PostsCommentDto(
                    postsComment.getPosts().getId(),
                    postsComment.getId(),
                    userClient.getUser(postsComment.getAuthor()).get(),
                    postsComment.getComment(),
                    postsComment.getCreatedDate(),
                    postsComment.getModifiedDate());
        });
        return postsCommentDtos;
    }

    /**
     * 게시글 댓글 생성
     * @param postsId Posts ID
     * @param author 등록자
     * @param comment 댓글
     * @return 등록되어진 댓글
     */
    public PostsCommentDto createComment(Long postsId, User author, String comment) {
        return postsCommentService.createComment(postsId, author, comment);
    }

    /**
     * 게시글 댓글 수정
     * @param commentId 댓글 ID
     * @param author 등록자
     * @param comment 댓글
     * @return 수정되어진 댓글
     */
    public PostsCommentDto editComment(Long commentId, User author, String comment) {
        PostsComment postsComment = postsCommentService.updateComment(commentId, comment);
        return new PostsCommentDto(
                postsComment.getPosts().getId(),
                postsComment.getId(),
                author,
                postsComment.getComment(),
                postsComment.getCreatedDate(),
                postsComment.getModifiedDate()
        );
    }

    /**
     * 게시글 댓글 삭제
     * @param commentId 댓글 ID
     * @return 삭제된 댓글 ID
     */
    public PostsComment deleteComment(Long commentId) {
        Optional<PostsComment> postsComment =  postsCommentService.getPostsComment(commentId);
        if(postsComment.isEmpty()) {
            throw new IllegalStateException("Posts Comment empty");
        }
        postsCommentService.deleteComment(commentId);
        return postsComment.get();
    }

}
