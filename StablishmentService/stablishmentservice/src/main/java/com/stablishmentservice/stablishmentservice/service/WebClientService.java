package com.stablishmentservice.stablishmentservice.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.stablishmentservice.stablishmentservice.dto.errors.ErrorDto;
import com.stablishmentservice.stablishmentservice.enums.StablishmentError;
import com.stablishmentservice.stablishmentservice.exception.StablishmentGeneralException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebClientService {
    @Qualifier("securedWebClient") private final WebClient securedWebClient;

    public <T> T secureGetMethod(String uri, Class<T> responseType, Object... uriVariables) {
        try {

            return securedWebClient.get()
                .uri(uri, uriVariables)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(ErrorDto.class)
                            .flatMap(errorBody -> Mono.error(new StablishmentGeneralException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (StablishmentGeneralException ex) {
            throw ex;
        } catch (WebClientResponseException ex) {
            throw new StablishmentGeneralException(
                StablishmentError.SERVICE_COMMUNICATION_FAILED);
        } catch (Exception ex) {
            throw new StablishmentGeneralException(
                StablishmentError.UNEXPECTED_ERROR);
        }
    }

    public <T, R> R securePostMethod(String uri, T requestBody, Class<R> responseType, Object... uriVariables) {
        try {

            return securedWebClient.post()
                .uri(uri, uriVariables)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(ErrorDto.class)
                            .flatMap(errorBody -> Mono.error(new StablishmentGeneralException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (StablishmentGeneralException ex) {
            throw ex;
        } catch (WebClientResponseException ex) {
            ex.printStackTrace();
            throw new StablishmentGeneralException(
                StablishmentError.SERVICE_COMMUNICATION_FAILED);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new StablishmentGeneralException(
                StablishmentError.UNEXPECTED_ERROR);
        }
    }

    public <T> T secureDeleteMethod(String uri, Class<T> responseType, Object... uriVariables) {
        try {

            return securedWebClient.delete()
                .uri(uri, uriVariables)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(ErrorDto.class)
                            .flatMap(errorBody -> Mono.error(new StablishmentGeneralException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (StablishmentGeneralException ex) {
            throw ex;
        } catch (WebClientResponseException ex) {
            throw new StablishmentGeneralException(
                StablishmentError.SERVICE_COMMUNICATION_FAILED);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new StablishmentGeneralException(
                StablishmentError.UNEXPECTED_ERROR );
        }
    }

}
