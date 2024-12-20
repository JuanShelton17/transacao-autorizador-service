package com.transacaoautorizador.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageConstants {
    public static final String ERROR_OBTER_DADOS_CARTAO = "error.obter.dados.cartao";
    public static final String ERROR_ATUALIZAR_DADOS_CARTAO = "error.atualizar.dados.cartao";
    public static final String ERROR_CARTAO_INEXISTENTE = "error.cartao.inexistente";
    public static final String ERROR_CARTAO_SENHA_INVALIDA = "error.cartao.senha.invalida";
    public static final String ERROR_CARTAO_SALDO_INSUFICIENTE = "error.cartao.saldo.insuficiente";
    public static final String CARTAO_OK = "cartao.ok";
}
