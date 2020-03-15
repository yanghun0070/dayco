package com.ykgroup.dayco.uaa.auth.domain;

import java.io.Serializable;

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
@ToString
public class AuthenticationRequest implements Serializable {
    private String username;
    private String password;
}
