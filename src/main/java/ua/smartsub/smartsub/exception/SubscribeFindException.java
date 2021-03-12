package ua.smartsub.smartsub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class SubscribeFindException extends RuntimeException {
    private  long id;
    private  String message;

    public SubscribeFindException(long id, String message) {
        super(String.format("Error searching for subscription by ID [%s] : '%s'", id, message));
        this.id = id;
        this.message = message;
    }

}
