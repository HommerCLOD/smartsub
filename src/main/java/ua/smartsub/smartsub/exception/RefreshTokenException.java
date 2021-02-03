package ua.smartsub.smartsub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class RefreshTokenException  extends RuntimeException  {

    private  String token;
    private  String message;

    public RefreshTokenException(String token, String message) {
        super(String.format("Couldn't refresh token for [%s]: [%s])", token, message));
        this.token = token;
        this.message = message;
    }
}
