package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.api.RsController;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
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
        System.out.println(e.getLocalizedMessage());
        if (e instanceof MethodArgumentNotValidException) {
            error.setError("invalid param");
        }
        log.error(error.getError());
        return ResponseEntity.badRequest().body(error);
    }

}
