package io.github.dayco.uaa.user.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dayco.uaa.auth.config.CurrentUser;
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
    public User change(@RequestBody ProfileDto profileDto, @CurrentUser User user) throws Exception {
        return profileService.change(profileDto, user);
    }

}
