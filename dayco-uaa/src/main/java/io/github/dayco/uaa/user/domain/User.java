package io.github.dayco.uaa.user.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.dayco.uaa.manager.domain.UserAuthorization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "userAuthorizations")
@Table(name = "uaa_user")
public class User implements UserDetails {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Column
    private Integer age;

    @Column
    private Integer gender;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Profile profile;

    @Column
    private LocalDateTime createTime;

    @Column
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAuthorization> userAuthorizations = new ArrayList<>();

    public User(String userId, String password) {
        this.userId = userId;
        this.password = new Password(password);
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public User(String userId, String password, String imageUrl) {
        this(userId, password);
        this.profile = new Profile(userId, imageUrl);
    }

    public User(String userId, String password, String email, String imageUrl) {
        this(userId, password, imageUrl);
        this.email = new Email(email);
        this.profile = new Profile(userId, imageUrl);
    }

    public User update(String userId, String imageUrl) {
        this.userId = userId;
        this.profile = new Profile(userId, imageUrl);
        this.updateTime = LocalDateTime.now();
        return this;
    }

    /**
     * @todo 추후에 나이, 성 관련하여 추가할 수 있는 기능 추가
     */
    public User update(String userId, String password, Integer age, Integer gender) {
        this.userId = userId;
        this.password = new Password(password);
        this.age = age;
        this.gender = gender;
        this.updateTime = LocalDateTime.now();
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userAuthorizations
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    public Optional<Integer> getAge() {
        return Optional.ofNullable(age);
    }

    public Optional<Integer> getGender() {
        return Optional.ofNullable(gender);
    }

    public Optional<Email> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<Profile> getProfile() {
        return Optional.ofNullable(profile);
    }

    public void setEmail(String email) {
        this.email = new Email(email);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addUserAuthorization(UserAuthorization userAuthorization) {
        // To avoid falling into an endless loop
        if(userAuthorization == null) {
            userAuthorizations = new ArrayList<>();
        }
        userAuthorizations.add(userAuthorization);
    }
}
