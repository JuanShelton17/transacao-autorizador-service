package com.transacaoautorizador.service.impl;

import com.cartaoAutorizador.model.client.CartaoDTO;
import com.cartaoAutorizador.model.client.NovoCartao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.transacaoautorizador.constants.HttpStatusConstants;
import com.transacaoautorizador.exception.AulasException;
import com.transacaoautorizador.service.CartaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

import static com.transacaoautorizador.constants.AppConstants.ERROR_OBTER_DADOS_CARTAO;
import static com.transacaoautorizador.constants.RequestsConstants.URL_CARTAO_AUTORIZADOR;
import static com.transacaoautorizador.constants.RequestsConstants.URL_CARTAO_AUTORIZADOR_DADOS_CARTAO;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class CartaoServiceImpl implements CartaoService {

    private final HttpClient httpClient;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public NovoCartao obterDadosCartao(String numeroCartao) {
        try {
            var urlBuilder = getUri(numeroCartao);
            var request = getRequest(urlBuilder);
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()){

                case HttpStatusConstants.OK:
                    Type tipoLista = new TypeToken<NovoCartao>() {}.getType();
                    Gson g = new Gson();
                    return g.fromJson(response.body(), tipoLista);

                default:
                    throw new AulasException(HttpStatus.INTERNAL_SERVER_ERROR, messageSource.getMessage(ERROR_OBTER_DADOS_CARTAO, null, Locale.getDefault()));
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CartaoDTO atualizarDadosCartao(NovoCartao novoCartao) {
        try {
            var urlBuilder = getUriUpdate();
            var request = getRequestUpdate(urlBuilder, novoCartao);
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()){

                case HttpStatusConstants.OK:
                    Type tipoLista = new TypeToken<CartaoDTO>() {}.getType();
                    Gson g = new Gson();
                    return g.fromJson(response.body(), tipoLista);

                default:
                    throw new AulasException(HttpStatus.INTERNAL_SERVER_ERROR, messageSource.getMessage(ERROR_OBTER_DADOS_CARTAO, null, Locale.getDefault()));
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private URI getUri(String pathParam) {
        var urlComPath = String.format("%s/%s", URL_CARTAO_AUTORIZADOR_DADOS_CARTAO, pathParam);
        return  URI.create(urlComPath);
    }
    private URI getUriUpdate() {
        return  URI.create(URL_CARTAO_AUTORIZADOR);
    }

    private HttpRequest getRequest(URI url) {

        return HttpRequest.newBuilder()
                .GET()
                .uri(url)
                .build();
    }

    private HttpRequest getRequestUpdate(URI url, NovoCartao novoCartao) throws JsonProcessingException {
        String jsonBody = objectMapper.writeValueAsString(novoCartao);

        return HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
    }

}
