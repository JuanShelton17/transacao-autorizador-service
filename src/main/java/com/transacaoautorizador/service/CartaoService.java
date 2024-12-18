package com.transacaoautorizador.service;

import com.cartaoAutorizador.model.client.CartaoDTO;
import com.cartaoAutorizador.model.client.NovoCartao;

public interface CartaoService {
    NovoCartao obterDadosCartao(String numeroCartao);

    CartaoDTO atualizarDadosCartao(NovoCartao novoCartao);

}
