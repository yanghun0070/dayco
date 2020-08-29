package io.github.dayco.external.ui.vo;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    private String userId;
    private Optional<String> email;
    private String password;
    private Optional<Integer> age;
    private Optional<Integer> gender;
    private Optional<String> picture;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;


}
