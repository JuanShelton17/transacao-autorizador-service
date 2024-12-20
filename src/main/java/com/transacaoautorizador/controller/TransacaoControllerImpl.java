package com.transacaoautorizador.controller;

import com.transacaoautorizador.TransacoesApi;
import com.transacaoautorizador.model.TransacaoDTO;
import com.transacaoautorizador.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransacaoControllerImpl implements TransacoesApi {

    private final TransacaoService service;

    @Override
    public ResponseEntity<String> _autorizarTransacao(TransacaoDTO transacaoDTO) {

        String resultado = service.autorizarTransacao(transacaoDTO);

        return "OK".equals(resultado) ? ResponseEntity.status(201).body(resultado) : ResponseEntity.unprocessableEntity().body(resultado);
    }
}
