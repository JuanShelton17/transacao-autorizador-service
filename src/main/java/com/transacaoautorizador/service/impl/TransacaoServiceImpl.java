package com.transacaoautorizador.service.impl;

import com.cartaoAutorizador.model.client.NovoCartao;
import com.transacaoautorizador.model.Transacao;
import com.transacaoautorizador.model.TransacaoDTO;
import com.transacaoautorizador.repository.TransacaoRepository;
import com.transacaoautorizador.service.CartaoService;
import com.transacaoautorizador.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TransacaoServiceImpl implements TransacaoService {

    private final CartaoService cartaoService;
    private final TransacaoRepository transacaoRepository;

    @Override
    public String autorizarTransacao(TransacaoDTO transacao) {
        NovoCartao cartao = cartaoService.obterDadosCartao(transacao.getNumeroCartao());

        if(cartao == null) {
            return "CARTAO_INEXISTENTE";
        }

        if (!cartao.getSenha().equals(transacao.getSenhaCartao())) {
            return "SENHA_INVALIDA";
        }

        if (cartao.getSaldo() < transacao.getValor()) {
            return "SALDO_INSUFICIENTE";
        }

        // Atualizar saldo do cartão
        cartao.setSaldo(cartao.getSaldo() - transacao.getValor());
        cartaoService.atualizarDadosCartao(cartao);

        var t = toTransacao(transacao);
        transacaoRepository.save(t);

        return "OK";
    }

    private Transacao toTransacao(TransacaoDTO transacaoDTO){
        return Transacao.builder()
                .senhaCartao(transacaoDTO.getSenhaCartao())
                .numeroCartao(transacaoDTO.getNumeroCartao())
                .valor(transacaoDTO.getValor())
                .build();
    }
}
