package com.transacaoautorizador.config.handler;

import com.transacaoautorizador.exception.BaseException;
import com.transacaoautorizador.exception.CartaoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerAdvisor {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> handleException(BaseException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CartaoException.class)
    public ResponseEntity<String> handleCartaoException(CartaoException ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
