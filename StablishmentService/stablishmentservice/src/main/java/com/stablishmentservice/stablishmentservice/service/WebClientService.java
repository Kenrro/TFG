package com.stablishmentservice.stablishmentservice.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.stablishmentservice.stablishmentservice.dto.errors.ErrorDto;
import com.stablishmentservice.stablishmentservice.exception.StablishmentGeneralException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebClientService {
    @Qualifier("securedWebClient") private final WebClient securedWebClient;

    public <T> T secureGetMethod(String uri, Class<T> responseType, Object... uriVariables) {
        return securedWebClient.get()
            .uri(uri, uriVariables) // <-- aquí pasamos los uriVariables
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> {
                    return response.bodyToMono(ErrorDto.class).flatMap((errorBody -> Mono.error(new StablishmentGeneralException(errorBody))));
                })
            .bodyToMono(responseType)
            .block();
    }
    public <T, R> R securePostMethod(String uri, T requestBody, Class<R> responseType, Object... uriVariables) {
        return securedWebClient.post()
            .uri(uri)
            .bodyValue(requestBody)
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> {
                    return response.bodyToMono(ErrorDto.class).flatMap((errorBody -> Mono.error(new StablishmentGeneralException(errorBody))));
                })
            .bodyToMono(responseType)
            .block();
    }
    public <T> T secureDeleteMethod(String uri, Class<T> responseType, Object... uriVariables) {
        return securedWebClient.delete()
            .uri(uri, uriVariables) 
            .retrieve()
            .onStatus(HttpStatusCode::isError, response -> {
                    return response.bodyToMono(ErrorDto.class).flatMap((errorBody -> Mono.error(new StablishmentGeneralException(errorBody))));
                })
            .bodyToMono(responseType)
            .block();
    }
}
