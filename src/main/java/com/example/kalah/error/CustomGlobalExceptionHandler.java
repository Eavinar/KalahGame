package com.example.kalah.error;

import com.example.kalah.entity.Alert;
import com.example.kalah.exceptions.ConnectedUserOutOfAllowanceException;
import com.example.kalah.exceptions.IllegalMoveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ConnectedUserOutOfAllowanceException.class)
    public ResponseEntity<Alert> handleMoreThanTwoUsersException(Exception exception) {
        Alert alert = new Alert();
        alert.setMessage(exception.getMessage());
        alert.setStatus("FAIL");
        return new ResponseEntity<>(alert, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalMoveException.class)
    public ResponseEntity<Alert> illegalMove(Exception exception) {
        Alert alert = new Alert();
        alert.setMessage(exception.getMessage());
        alert.setStatus("FAIL");
        return new ResponseEntity<>(alert, HttpStatus.BAD_REQUEST);
    }
}
