package ua.smartsub.smartsub.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class SubscribeCreateException extends RuntimeException{
    private  String botname;
    private  String message;

    public SubscribeCreateException(String botname, String message) {
        super(String.format("Failed to create Subscribe by [%s] : '%s'", botname, message));
        this.botname = botname;
        this.message = message;
    }


}
