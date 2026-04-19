package com.example.verify_backend.Config;

import com.example.verify_backend.Exception.BusinessLogicException;
import com.example.verify_backend.Exception.NoAffectException;
import com.example.verify_backend.Exception.ValidationLinkException;
import com.example.verify_backend.Exception.ValidationXmlException;
import com.example.verify_backend.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleException(MethodArgumentNotValidException exception) {
        List<String> messageList = exception.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        Result result = new Result()
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setMessage(String.valueOf(messageList));

        return ResponseEntity
                .badRequest()
                .body(result);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<Result> handleException(BusinessLogicException exception) {
        Result result = new Result()
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setMessage(exception.getMessage());

        return ResponseEntity
                .badRequest()
                .body(result);
    }

    @ExceptionHandler(ValidationXmlException.class)
    public ResponseEntity<Result> handleException(ValidationXmlException exception) {
        Result result = new Result()
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setMessage(exception.getMessage());

        return ResponseEntity
                .badRequest()
                .body(result);
    }

    @ExceptionHandler(ValidationLinkException.class)
    public ResponseEntity<Result> handleException(ValidationLinkException exception) {
        Result result = new Result()
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setMessage(exception.getMessage());

        return ResponseEntity
                .badRequest()
                .body(result);
    }

    @ExceptionHandler(NoAffectException.class)
    public ResponseEntity<Result> handleException(NoAffectException exception) {
        Result result = new Result()
                .setCode(HttpStatus.CONFLICT.value())
                .setMessage(exception.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(result);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleException(Exception exception) {
        log.error("Внутренняя ошибка сервиса", exception);

        Result result = new Result()
                .setCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setMessage("Внутренняя ошибка сервиса");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(result);
    }
}
