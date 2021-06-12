package io.github.dayco.uaa.manager.ui;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.dayco.uaa.manager.service.ManagerService;
import io.github.dayco.uaa.manager.domain.Authorization;
import io.github.dayco.uaa.manager.domain.Resource;
import io.github.dayco.uaa.manager.domain.UserAuthorization;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @PostMapping("/user/authorization/create")
    public void createUserAuthorization(@RequestBody UserAuthorization userAuthorization) {
        managerService.createUserAuthorization(userAuthorization);
    }

    @GetMapping("/user/authorizations")
    public void getUserAuthorizations() {
        managerService.selectUserAuthorizations();
    }

    @PostMapping("/authorization/create")
    public void createAuthorization(@RequestBody Authorization authorization) {
        managerService.createAuthorization(authorization);
    }

    @PostMapping("/authorizations/create")
    public void createAuthorizations(@RequestBody List<Authorization> authorizations) {
        managerService.createAuthorizations(authorizations);
    }

    @PostMapping("/resources/create")
    public void create(@RequestBody List<Resource> resources)  {
        managerService.createResources(resources);
    }

    @PostMapping("/resource/create")
    public void create(@RequestBody Resource resource) {
        managerService.createResource(resource);
    }
}
