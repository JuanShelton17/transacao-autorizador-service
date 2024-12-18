package com.transacaoautorizador.repository;

import com.transacaoautorizador.model.Transacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends MongoRepository<Transacao, String> {
    List<Transacao> findByNumeroCartao(String numeroCartao);
}
