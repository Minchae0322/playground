package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.exception.TokenNotValidException;
import com.example.playgroundmanage.login.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TokenNotValidException.class)
    public ErrorResponse isWrongIdAndPassword(TokenNotValidException e) {
        return ErrorResponse.builder()
                .code("403")
                .message(e.getMessage())
                .build();
    }
}
