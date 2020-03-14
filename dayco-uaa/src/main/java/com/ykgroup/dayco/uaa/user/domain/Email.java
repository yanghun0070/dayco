package com.ykgroup.dayco.uaa.user.domain;

import java.util.regex.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class Email {
    private String name;

    private void validate(String name) {
        boolean isEmailPattern = Pattern.matches("^[a-zA-z0-9]+@[a-zA-Z0-9]+.+[a-zA-Z]$", name);
        if(!isEmailPattern) throw new IllegalArgumentException("email don't match");
    }

    public Email(String name) {
        validate(name);
        this.name = name;
    }
}
