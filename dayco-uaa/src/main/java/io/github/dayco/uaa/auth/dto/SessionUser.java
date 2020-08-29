package io.github.dayco.uaa.auth.dto;

import java.io.Serializable;

import io.github.dayco.uaa.user.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SessionUser implements Serializable {
    private String userId;
    private String email;
    private String password;
    private String picture;
    private String registrationId;

    public SessionUser(User user) {
        this.userId = user.getUserId();
        this.email = (user.getEmail().isPresent()) ? user.getEmail().get().getEmail() : "";;
        this.picture = (user.getPicture().isPresent()) ? user.getPicture().get() : "";
    }

    public SessionUser(User user, String registrationId) {
        this(user);
        this.registrationId =  registrationId;
    }

    public SessionUser(String userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }
}
