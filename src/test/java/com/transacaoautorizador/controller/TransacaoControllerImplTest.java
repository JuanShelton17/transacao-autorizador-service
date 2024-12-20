package com.transacaoautorizador.controller;

import com.transacaoautorizador.model.TransacaoDTO;
import com.transacaoautorizador.service.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransacaoControllerImplTest {

    @InjectMocks
    private TransacaoControllerImpl transacaoController;

    @Mock
    private TransacaoService transacaoService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(transacaoController).build();

    }

    @Test
    void testAutorizarTransacaoOk() throws Exception {
        TransacaoDTO transacaoDTO = new TransacaoDTO();
        when(transacaoService.autorizarTransacao(transacaoDTO)).thenReturn("OK");

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content("{ \"campo1\": \"valor1\", \"campo2\": \"valor2\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().string("OK"));

        verify(transacaoService, times(1)).autorizarTransacao(transacaoDTO);
    }

    @Test
    void testAutorizarTransacaoUnprocessableEntity() throws Exception {
        TransacaoDTO transacaoDTO = new TransacaoDTO();
        when(transacaoService.autorizarTransacao(transacaoDTO)).thenReturn("CARTAO_INEXISTENTE");

        mockMvc.perform(post("/transacoes")
                        .contentType("application/json")
                        .content("{ \"campo1\": \"valor1\", \"campo2\": \"valor2\" }"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("CARTAO_INEXISTENTE"));

        verify(transacaoService, times(1)).autorizarTransacao(transacaoDTO);
    }
}