package io.github.dayco.uaa.user.domain;

import javax.persistence.Embeddable;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Password {
    private String password;

    public Password(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
