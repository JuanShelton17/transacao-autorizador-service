package com.transacaoautorizador.exception;

import org.springframework.http.HttpStatus;

public class AulasException extends BaseException{

    public AulasException(HttpStatus status, String message) {
        super(status, message);
    }
}
