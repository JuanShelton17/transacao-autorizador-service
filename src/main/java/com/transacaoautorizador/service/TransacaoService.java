package com.transacaoautorizador.service;

import com.transacaoautorizador.model.Transacao;
import com.transacaoautorizador.model.TransacaoDTO;

public interface TransacaoService {
    String autorizarTransacao(TransacaoDTO transacao);
}
