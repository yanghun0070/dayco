package io.github.dayco.posts.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "postsComments")
@NoArgsConstructor
@Entity
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "posts_id")
    private Long id;
    @Column(length = 500, nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    private String author;
    private LocalDateTime modifiedDate;
    private LocalDateTime createdDate;
    @Column(length = 1000)
    private String fileSavedUrl;
    private String fileName;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL)
    private List<PostsComment> postsComments = new ArrayList<>();

    @Builder
    public Posts(String title, String content, String author,
                 Optional<String> fileName,
                 Optional<String> fileSavedUrl) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.modifiedDate = LocalDateTime.now();
        this.createdDate = LocalDateTime.now();
        if(fileName.isPresent()) {
            this.fileName = fileName.get();
        }
        if(fileSavedUrl.isPresent()) {
            this.fileSavedUrl =  fileSavedUrl.get();
        }
    }

    public void update(String title, String content,
                       Optional<String> fileName,
                       Optional<String> fileSavedUrl) {
        this.title = title;
        this.content = content;
        this.modifiedDate = LocalDateTime.now();
        if(fileName.isPresent()) {
            this.fileName = fileName.get();
        }
        if(fileSavedUrl.isPresent()) {
            this.fileSavedUrl =  fileSavedUrl.get();
        }
    }

    public Posts updateFileSavedUrl(String fileSavedUrl) {
        this.fileSavedUrl =  fileSavedUrl;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
