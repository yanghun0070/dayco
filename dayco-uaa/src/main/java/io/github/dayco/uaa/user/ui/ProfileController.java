package io.github.dayco.uaa.user.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import javax.swing.filechooser.FileSystemView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dayco.uaa.user.application.ProfileService;
import io.github.dayco.uaa.user.domain.User;
import io.github.dayco.uaa.user.ui.dto.ProfileDto;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("change")
    public User change(@RequestBody ProfileDto profileDto) throws Exception {
        String fileSavedPath = null;

        if( profileDto.getFileName().isPresent()) {
            String fileName = profileDto.getFileName().get();
            //
            /**
             *  저장할 파일 경로를 지정
             *  @TODO 저장될 이미지 URL 전체를 저장하는 방식으로 구현 필요
             */
            fileSavedPath = FileSystemView
                                    .getFileSystemView()
                                    .getHomeDirectory()
                            + "/app/resources/" + fileName;

            File file = new File(fileSavedPath);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                // Base64 를 일반 파일로 변환하고 저장
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] decodedBytes = decoder.decode(profileDto.getFileBase64().get().getBytes());

                fileOutputStream.write(decodedBytes);
            }
        }
        return profileService.change(profileDto.getEmail(), profileDto.getPassword(), Optional.of(fileSavedPath));
    }
}
