package ua.smartsub.smartsub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.smartsub.smartsub.entity.User;

@RestController
public class AuthController {

//    @Autowired
//    private UserDao userDao;
//
//    @PostMapping(value = "/registration")
//    @ResponseStatus(HttpStatus.CREATED)
//    public String register(@RequestBody User userRequest) {
//
//        userDao.save(userRequest);
//        return "Ok";
//    }


    @GetMapping(value = "/login")
    @ResponseStatus(HttpStatus.CREATED)
    public User login(@RequestBody User userRequest) {
        return userRequest;
    }

}
