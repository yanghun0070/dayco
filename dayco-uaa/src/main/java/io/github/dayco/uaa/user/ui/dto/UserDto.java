package io.github.dayco.uaa.user.ui.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    private String userId;
    private String email;
    private Integer age;
    private Integer gender;
    private String picture;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;

    @Builder
    public UserDto(String userId, String email,
                                 Integer age,
                                 Integer gender,
                                 String picture,
                                 LocalDateTime createTime,
                                 LocalDateTime modifyTime) {
        this.userId = userId;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.picture = picture;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }
}
