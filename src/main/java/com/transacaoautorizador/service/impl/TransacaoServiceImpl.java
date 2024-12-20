package com.transacaoautorizador.service.impl;

import com.cartaoAutorizador.model.client.NovoCartao;
import com.transacaoautorizador.model.Transacao;
import com.transacaoautorizador.model.TransacaoDTO;
import com.transacaoautorizador.repository.TransacaoRepository;
import com.transacaoautorizador.service.CartaoService;
import com.transacaoautorizador.service.TransacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static com.transacaoautorizador.constants.MessageConstants.*;


@Service
@RequiredArgsConstructor
public class TransacaoServiceImpl implements TransacaoService {

    private final CartaoService cartaoService;
    private final TransacaoRepository transacaoRepository;
    private final MessageSource messageSource;


    @Override
    public String autorizarTransacao(TransacaoDTO transacao) {
        NovoCartao cartao = cartaoService.obterDadosCartao(transacao.getNumeroCartao());

        if(cartao == null) {
            return messageSource.getMessage(ERROR_CARTAO_INEXISTENTE, null, Locale.getDefault());
        }

        if (!cartao.getSenha().equals(transacao.getSenhaCartao())) {
           return messageSource.getMessage(ERROR_CARTAO_SENHA_INVALIDA, null, Locale.getDefault());
        }

        if (cartao.getSaldo() < transacao.getValor()) {
            return messageSource.getMessage(ERROR_CARTAO_SALDO_INSUFICIENTE, null, Locale.getDefault());
        }

        cartao.setSaldo(cartao.getSaldo() - transacao.getValor());
        cartaoService.atualizarDadosCartao(cartao);

        var t = toTransacao(transacao);
        transacaoRepository.save(t);

        return messageSource.getMessage(CARTAO_OK, null, Locale.getDefault());
    }

    private Transacao toTransacao(TransacaoDTO transacaoDTO){
        return Transacao.builder()
                .senhaCartao(transacaoDTO.getSenhaCartao())
                .numeroCartao(transacaoDTO.getNumeroCartao())
                .valor(transacaoDTO.getValor())
                .build();
    }
}
