package io.github.dayco.uaa.user.ui.dto;

import java.util.Optional;

import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class ProfileDto {
    private Optional<String> email; //이메일
    private Optional<String> password; //비밀번호
    private Optional<String> fileBase64; //File Base64
    private Optional<String> fileName; //파일명

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public Optional<String> getPassword() {
        return password;
    }

    public void setPassword(Optional<String> password) {
        this.password = password;
    }

    public Optional<String> getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(Optional<String> fileBase64) {
        this.fileBase64 = fileBase64;
    }

    public Optional<String> getFileName() {
        return fileName;
    }

    public void setFileName(Optional<String> fileName) {
        this.fileName = fileName;
    }

}
