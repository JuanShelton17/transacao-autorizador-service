package com.transacaoautorizador.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "transacoes")
public class Transacao {

    @Id
    private String id;
    private String numeroCartao;
    private String senhaCartao;
    private Double valor;

}
