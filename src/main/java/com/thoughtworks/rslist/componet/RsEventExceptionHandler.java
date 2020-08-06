package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.domain.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RsEventExceptionHandler {

    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsEventNotValidExceptionHandler(Exception e) {
        Error error = new Error(e.getMessage());
        System.out.println(e.getLocalizedMessage());
        if (e instanceof MethodArgumentNotValidException) {
            error.setError("invalid param");
        }
        return ResponseEntity.badRequest().body(error);
    }

}
