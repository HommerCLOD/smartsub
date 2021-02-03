package ua.smartsub.smartsub.exception;

public class TokenRefreshException extends RuntimeException  {

    private  String token;
    private  String message;

    public TokenRefreshException(String token, String message) {
        super(String.format("Couldn't refresh token for [%s]: [%s])", token, message));
        this.token = token;
        this.message = message;
    }
}
