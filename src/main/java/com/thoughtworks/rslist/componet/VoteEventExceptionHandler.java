package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.api.VoteEventController;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.VoteNumberOverUsersOwnException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = VoteEventController.class)
public class VoteEventExceptionHandler {

    @ExceptionHandler({VoteNumberOverUsersOwnException.class})
    public ResponseEntity voteNumberNotValidExceptionHandler(Exception e) {
        Error error = new Error();
        error.setError(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}
