package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.exception.*;
import com.thoughtworks.rslist.exception.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice(assignableTypes  = RsController.class)
public class RsEventExceptionHandler {

    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsEventNotValidExceptionHandler(Exception e) {
        Error error = new Error(e.getMessage());
        if (e instanceof MethodArgumentNotValidException) {
            error.setError("invalid param");
        }
        log.error(error.getError());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler({UserNotExistsException.class, UserIdNotMatchException.class})
    public ResponseEntity userErrorExceptionHandler(Exception e) {
        Error error = new Error(e.getMessage());
        log.error(error.getError());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler({RsEventNotExistsException.class})
    public ResponseEntity rsEventNotExistsExceptionHandler(Exception e) {
        Error error = new Error(e.getMessage());
        log.error(error.getError());
        return ResponseEntity.badRequest().body(error);
    }

}
