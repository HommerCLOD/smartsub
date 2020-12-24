package ua.smartsub.smartsub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.smartsub.smartsub.dao.UserDao;
import ua.smartsub.smartsub.entity.User;
import ua.smartsub.smartsub.services.implentation.IAuthService;

import javax.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping(value = "/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody @Valid User user) {

        return authService.registrationUser(user);
    }


    @GetMapping(value = "/login")
    @ResponseStatus(HttpStatus.CREATED)
    public User login(@RequestBody @Valid User userRequest) {
        return userRequest;
    }

}
