package com.authservice.auth.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.authservice.auth.dto.errors.ErrorDto;
import com.authservice.auth.enums.AuthError;
import com.authservice.auth.exception.AuthException;

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
                            .flatMap(errorBody -> Mono.error(new AuthException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (AuthException ex) {
            throw ex;
        } catch (WebClientResponseException ex) {
            throw new AuthException(
                AuthError.SERVICE_COMMUNICATION_FAILED);
        } catch (Exception ex) {
            throw new AuthException(
                AuthError.UNEXPECTED_ERROR);
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
                            .flatMap(errorBody -> Mono.error(new AuthException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (AuthException ex) {
            throw ex;
        } catch (WebClientResponseException ex) {
            throw new AuthException(
                AuthError.SERVICE_COMMUNICATION_FAILED);
        } catch (Exception ex) {
            throw new AuthException(
                AuthError.UNEXPECTED_ERROR);
        }
    }

    public <T> T secureDeleteMethod(String uri, Class<T> responseType, Object... uriVariables) {
        try {

            return securedWebClient.delete()
                .uri(uri, uriVariables)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(ErrorDto.class)
                            .flatMap(errorBody -> Mono.error(new AuthException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (AuthException ex) {
            throw ex;
        } catch (WebClientResponseException ex) {
            throw new AuthException(
                AuthError.SERVICE_COMMUNICATION_FAILED);
        } catch (Exception ex) {
            throw new AuthException(
                AuthError.UNEXPECTED_ERROR );
        }
    }


}
