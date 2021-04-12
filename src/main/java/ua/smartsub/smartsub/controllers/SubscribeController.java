package ua.smartsub.smartsub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.smartsub.smartsub.model.DTO.ApiResponse;
import ua.smartsub.smartsub.exception.SubscribeCreateException;
import ua.smartsub.smartsub.exception.SubscribeFindException;
import ua.smartsub.smartsub.model.entity.Subscribe;
import ua.smartsub.smartsub.services.ISubscribeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController {

    @Autowired
    private ISubscribeService subscribeService;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createSubscribe(@RequestBody @Valid Subscribe subscribeDTO) {
        return subscribeService.saveSubscribe(subscribeDTO).map((subscribe) ->
        ResponseEntity.ok(new ApiResponse("Create new Subscribe successful", true))
        ).orElseThrow(() -> new SubscribeCreateException(subscribeDTO.getName(), "Error create Subscribe"));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public Subscribe findSubscribeById(@PathVariable long id) {
        return subscribeService.findSubscribeById(id).orElseThrow(() -> new SubscribeFindException(id,"Error find Subscribe by id"));
    }

    @GetMapping(value = "/all")
    @ResponseStatus(HttpStatus.FOUND)
    public List<Subscribe> findAllSubscribes() {
        return subscribeService.findALl();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public List<Subscribe> findSubscribeByAll(@RequestParam(required=false) String keyword ) {
        return subscribeService.findSubscribeByAll(keyword);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> removeById(@PathVariable long id) {
        subscribeService.removeById(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity updateById(@PathVariable Subscribe subscribeDTO, @PathVariable long id) {

        return subscribeService.updateSubscribe(subscribeDTO,id).map((subscribe) ->
                ResponseEntity.ok(new ApiResponse("Create new Subscribe successful", true))
        ).orElseThrow(() -> new SubscribeCreateException(subscribeDTO.getName(), "Error create Subscribe"));
    }
}
