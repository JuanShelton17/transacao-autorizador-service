package com.transacaoautorizador.service.impl;

import com.cartaoAutorizador.model.client.CartaoDTO;
import com.cartaoAutorizador.model.client.NovoCartao;
import com.google.gson.Gson;
import com.transacaoautorizador.exception.CartaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CartaoServiceImplTest {

    @InjectMocks
    private CartaoServiceImpl cartaoService;

    @Mock
    private HttpClient httpClient;

    @Mock
    private MessageSource messageSource;

    private NovoCartao novoCartao;

    @BeforeEach
    void setUp() {
        openMocks(this);
        novoCartao = new NovoCartao();
        novoCartao.setNumeroCartao("1234567890123456");
        novoCartao.setSenha("1234");
        novoCartao.setSaldo(1000.0);
    }

    @Test
    void testObterDadosCartaoComSucesso() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(new Gson().toJson(novoCartao));

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        var resultado = cartaoService.obterDadosCartao("1234567890123456");

        assertAll(
                () -> assertEquals(novoCartao.getNumeroCartao(), resultado.getNumeroCartao()),
                () -> assertEquals(novoCartao.getSenha(), resultado.getSenha()),
                () -> assertEquals(novoCartao.getSaldo(), resultado.getSaldo()),
                () -> verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))

        );
    }

    @Test
    void testObterDadosCartaoErro() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(500);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);
        when(messageSource.getMessage("error.obter.dados.cartao", null, Locale.getDefault()))
                .thenReturn("Erro ao obter dados do cartão");

        RuntimeException exception =
                org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                        () -> cartaoService.obterDadosCartao("1234567890123456")
                );

        assertAll(
                () -> assertEquals("Erro ao obter dados do cartão", exception.getMessage()),
                () -> verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))
        );
    }

    @Test
    void testAtualizarDadosCartaoComSucesso() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);

        CartaoDTO cartaoDTO = new CartaoDTO();
        cartaoDTO.setNumeroCartao("1234567890123456");
        cartaoDTO.setSaldo(500.0);

        when(response.statusCode()).thenReturn(200);
        when(response.body()).thenReturn(new Gson().toJson(cartaoDTO));
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);

        var resultado = cartaoService.atualizarDadosCartao(novoCartao);

        assertAll(
                () -> assertEquals(cartaoDTO.getNumeroCartao(), resultado.getNumeroCartao()),
                () -> assertEquals(cartaoDTO.getSaldo(), resultado.getSaldo()),
                () -> verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))
        );
    }

    @Test
    void testAtualizarDadosCartaoErro() throws Exception {
        HttpResponse<String> response = mock(HttpResponse.class);

        when(response.statusCode()).thenReturn(500); // Simula o status 500 - Erro do servidor
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(response);
        when(messageSource.getMessage("error.atualizar.dados.cartao", null, Locale.getDefault()))
                .thenReturn("Erro ao atualizar dados do cartão");

        RuntimeException exception =
                org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                        () -> cartaoService.atualizarDadosCartao(novoCartao)
                );

        assertAll(
                () -> assertEquals("Erro ao atualizar dados do cartão", exception.getMessage()),
                () -> verify(httpClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))
        );
    }

}