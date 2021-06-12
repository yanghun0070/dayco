package io.github.dayco.uaa.user.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@Table(name = "uaa_profile")
@Entity
public class Profile {
    @Id
    @Column(name = "user_id")
    private String userId;
    private String fileName;
    @Column(length = 1000)
    private String imageUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime expireTime;

    public Profile(String userId, String imageUrl) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.updateTime = LocalDateTime.now();
        this.expireTime = LocalDateTime.now().plusDays(7);
    }

    public Profile(String userId, String fileName, String imageUrl) {
        this(userId, imageUrl);
        this.fileName = fileName;
        this.createTime = LocalDateTime.now();
    }

}
