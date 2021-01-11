package ua.smartsub.smartsub.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.smartsub.smartsub.exception.UniqueUserException;

@RestControllerAdvice
public class ErrorController {

    Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = "Object" +
                fieldError.getObjectName() +
                ", field " +
                fieldError.getField() +
                "-" +
                fieldError.getDefaultMessage();
        logger.warn("Handling MethodArgumentNotValidException" + message);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),"Invalid input data" , message);
    }

    @ExceptionHandler(value = {UniqueUserException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerUniqueUserExeption(UniqueUserException ex){
        logger.warn("Handling UniqueUserException   " + ex.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),"Invalid input data" , ex.getMessage());
    }

}
