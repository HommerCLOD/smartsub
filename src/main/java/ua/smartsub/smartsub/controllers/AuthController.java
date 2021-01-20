package ua.smartsub.smartsub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.smartsub.smartsub.DTO.LoginDTO;
import ua.smartsub.smartsub.entity.User;
import ua.smartsub.smartsub.security.jwt.JwtProvider;
import ua.smartsub.smartsub.services.IAuthService;

import javax.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    private IAuthService authService;
    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody @Valid User user) {
        return authService.saveUser(user);
    }


    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse login(@RequestBody @Valid LoginDTO loginDTO) {
        User user = authService.findByLoginAndPassword(loginDTO.getUsername(),loginDTO.getPassword());
        String token = jwtProvider.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    @GetMapping(value = "/hello")
    public String Hello() {
        return "Hello";
    }


}
