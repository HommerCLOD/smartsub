package ua.smartsub.smartsub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.smartsub.smartsub.model.entity.Comments;
import ua.smartsub.smartsub.services.ICommentsService;
import ua.smartsub.smartsub.services.implentation.CommentsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private ICommentsService commentsService;

    @PostMapping(value = "/{user_id}/{subscribe_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Comments insertComment(@RequestBody @Valid Comments comment, @PathVariable(name = "user_id") int user_id, @PathVariable(name = "subscribe_id") int subscribe_id) {
        return commentsService.saveComment(comment, user_id, subscribe_id);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long id) {
        commentsService.deleteCommentById(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Comments editComment(@PathVariable Long id, @RequestBody @Valid Comments comment) {
        return commentsService.editComment(id, comment);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Comments> allComments() {
    return commentsService.allComments();
    }
}
