package io.github.dayco.uaa.user.ui.dto;

import java.util.Optional;

import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
public class ProfileDto {
    private String userId; //유저 ID
    private Optional<String> fileBase64; //File Base64
    private Optional<String> fileName; //파일명

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
