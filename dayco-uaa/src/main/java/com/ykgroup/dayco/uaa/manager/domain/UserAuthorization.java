package com.ykgroup.dayco.uaa.manager.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ykgroup.dayco.uaa.user.domain.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(exclude = "user")
@NoArgsConstructor
@Table(name = "uaa_user_authorization")
public class UserAuthorization {
    @Id
    @GeneratedValue
    private Integer id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String roleName;

    public UserAuthorization(User user, String roleName) {
        this.user = user;
        this.roleName = roleName;
    }
}
