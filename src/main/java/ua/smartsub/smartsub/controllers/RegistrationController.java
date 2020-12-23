package ua.smartsub.smartsub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.smartsub.smartsub.dao.UserDao;
import ua.smartsub.smartsub.entity.User;

@RequestMapping(value = "/registration")
@RestController
public class RegistrationController {

    @Autowired
    private UserDao userDao;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User register(@RequestBody User userRequest) {

        userDao.save(userRequest);
        return userRequest;
    }
    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String test() {

        return "REst";
    }
}
