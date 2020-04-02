package com.ykgroup.dayco.uaa.auth.domain;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class AuthenticationRequest implements Serializable {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
