package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.domain.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RsEventExceptionHandler {

    @ExceptionHandler(RsEventNotValidException.class)
    public ResponseEntity RsEventNotValidExceptionHandler(RsEventNotValidException e) {
        Error error = new Error(e.getMessage());

        return ResponseEntity.badRequest().body(error);
    }

}
