package com.transacaoautorizador.service.impl;

import com.cartaoAutorizador.model.client.NovoCartao;
import com.transacaoautorizador.model.Transacao;
import com.transacaoautorizador.model.TransacaoDTO;
import com.transacaoautorizador.repository.TransacaoRepository;
import com.transacaoautorizador.service.CartaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class TransacaoServiceImplTest {

    @InjectMocks
    private TransacaoServiceImpl transacaoService;

    @Mock
    private CartaoService cartaoService;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private MessageSource messageSource;

    private TransacaoDTO transacaoDTO;

    private NovoCartao cartao;

    @BeforeEach
    void setUp() {
        openMocks(this);

        transacaoDTO = new TransacaoDTO();
        transacaoDTO.setNumeroCartao("1234567890123456");
        transacaoDTO.setSenhaCartao("1234");
        transacaoDTO.setValor(100.0);

        cartao = new NovoCartao();
        cartao.setNumeroCartao("1234567890123456");
        cartao.setSenha("1234");
        cartao.setSaldo(500.0);
    }

    @Test
    void testAutorizarTransacaoSucesso() {
        when(cartaoService.obterDadosCartao(transacaoDTO.getNumeroCartao())).thenReturn(cartao);
        when(messageSource.getMessage("cartao.ok", null, Locale.getDefault())).thenReturn("OK");

        String resultado = transacaoService.autorizarTransacao(transacaoDTO);

        assertAll(
                () -> assertEquals("OK", resultado),
                () -> verify(cartaoService, times(1)).obterDadosCartao(transacaoDTO.getNumeroCartao()),
                () -> verify(cartaoService, times(1)).atualizarDadosCartao(cartao),
                () -> verify(transacaoRepository, times(1)).save(any(Transacao.class))

        );
    }

    @Test
    void testAutorizarTransacaoCartaoInexistente() {

        when(messageSource.getMessage("error.cartao.inexistente", null, Locale.getDefault()))
                .thenReturn("Cartão inexistente");
        when(cartaoService.obterDadosCartao(transacaoDTO.getNumeroCartao())).thenReturn(null);


        String resultado = transacaoService.autorizarTransacao(transacaoDTO);

        assertAll(
                () -> assertEquals("Cartão inexistente", resultado),
                () -> verify(cartaoService, times(1)).obterDadosCartao(transacaoDTO.getNumeroCartao()),
                () -> verifyNoInteractions(transacaoRepository)
        );
    }

    @Test
    void testAutorizarTransacaoSenhaInvalida() {
        cartao.setSenha("9999");
        when(cartaoService.obterDadosCartao(transacaoDTO.getNumeroCartao())).thenReturn(cartao);
        when(messageSource.getMessage("error.cartao.senha.invalida", null, Locale.getDefault()))
                .thenReturn("Senha inválida");

        String resultado = transacaoService.autorizarTransacao(transacaoDTO);
        assertAll(
                () -> assertEquals("Senha inválida", resultado),
                () -> verify(cartaoService, times(1)).obterDadosCartao(transacaoDTO.getNumeroCartao()),
                () -> verifyNoInteractions(transacaoRepository)
        );
    }


    @Test
    void testAutorizarTransacaoSaldoInsuficiente() {
        cartao.setSaldo(50.0);
        when(cartaoService.obterDadosCartao(transacaoDTO.getNumeroCartao())).thenReturn(cartao);
        when(messageSource.getMessage("error.cartao.saldo.insuficiente", null, Locale.getDefault()))
                .thenReturn("Saldo insuficiente");

        String resultado = transacaoService.autorizarTransacao(transacaoDTO);

        assertAll(
                () -> assertEquals("Saldo insuficiente", resultado),
                () -> verify(cartaoService, times(1)).obterDadosCartao(transacaoDTO.getNumeroCartao()),
                () -> verifyNoInteractions(transacaoRepository)
        );
    }

}