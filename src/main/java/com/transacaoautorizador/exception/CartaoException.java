package com.transacaoautorizador.exception;

import org.springframework.http.HttpStatus;

public class CartaoException extends BaseException{

    public CartaoException(HttpStatus status, String message) {
        super(status, message);
    }
}
