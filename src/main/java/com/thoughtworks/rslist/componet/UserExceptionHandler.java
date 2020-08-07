package com.thoughtworks.rslist.componet;
import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.exception.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(assignableTypes  = UserController.class)
public class UserExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity userNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Error error = new Error("invalid user");
        log.error(error.getError());
        return ResponseEntity.badRequest().body(error);
    }
}
