package io.github.dayco.uaa.user.ui.dto;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {
    private String userId;
    private Optional<String> email;
    private Optional<Integer> age;
    private Optional<Integer> gender;
    private Optional<String> profileImageUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Builder
    public UserDto(String userId,
                   Optional<String> email,
                   Optional<Integer> age,
                   Optional<Integer> gender,
                   Optional<String> profileImageUrl,
                   LocalDateTime createTime,
                   LocalDateTime updateTime) {
        this.userId = userId;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
