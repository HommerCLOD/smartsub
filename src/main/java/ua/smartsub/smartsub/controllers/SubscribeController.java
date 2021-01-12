package ua.smartsub.smartsub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.smartsub.smartsub.entity.Subscribe;
import ua.smartsub.smartsub.services.implentation.SubscribeService;

import javax.validation.Valid;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController {

    @Autowired
    private SubscribeService subscribeService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Subscribe register(@RequestBody @Valid Subscribe subscribe) {
        return subscribeService.saveSubscribe(subscribe);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Subscribe findSubscribeById(@PathVariable int id) {
        return subscribeService.findSubscribeById(id);
    }
}
