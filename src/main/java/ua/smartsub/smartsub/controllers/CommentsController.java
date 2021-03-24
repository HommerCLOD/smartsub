package ua.smartsub.smartsub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.smartsub.smartsub.model.entity.Comments;
import ua.smartsub.smartsub.services.implentation.CommentsService;

import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @PostMapping(value = "/{user_id}/{subscribe_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Comments register(@RequestBody @Valid Comments comment, @PathVariable(name = "user_id") int user_id, @PathVariable(name = "subscribe_id") int subscribe_id) {

        return commentsService.saveComment(comment, user_id, subscribe_id);
    }
}
