package com.transacaoautorizador.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestsConstants {

    public static final String URL_CARTAO_AUTORIZADOR_DADOS_CARTAO = "http://localhost:8080/cartoes/dados";
    public static final String URL_CARTAO_AUTORIZADOR = "http://localhost:8080/cartoes";

}
