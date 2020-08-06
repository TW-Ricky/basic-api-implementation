package com.thoughtworks.rslist.componet;
import com.thoughtworks.rslist.api.UserController;
import com.thoughtworks.rslist.domain.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = {UserController.class})
public class UserExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity userNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Error error = new Error("invalid user");
        return ResponseEntity.badRequest().body(error);
    }
}
