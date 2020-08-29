package io.github.dayco.uaa.user.domain;

import java.util.regex.Pattern;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Email {
    private String email;

    private void validate(String email) {
        boolean isEmailPattern = Pattern.matches("^[a-zA-z0-9]+@[a-zA-Z0-9]+.+[a-zA-Z]$", email);
        if(!isEmailPattern) throw new IllegalArgumentException("email don't match");
    }

    public Email(String email) {
        validate(email);
        this.email = email;
    }
}
