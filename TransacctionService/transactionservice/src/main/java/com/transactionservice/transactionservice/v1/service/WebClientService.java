package com.transactionservice.transactionservice.v1.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.transactionservice.transactionservice.v1.dto.errors.ErrorDto;
import com.transactionservice.transactionservice.v1.enums.TransactionError;
import com.transactionservice.transactionservice.v1.exception.GeneralException;

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
                            .flatMap(errorBody -> Mono.error(new GeneralException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (GeneralException ex) {
            throw ex;
        } catch (WebClientResponseException ex) {
            throw new GeneralException(
                TransactionError.SERVICE_COMMUNICATION_FAILED);
        } catch (Exception ex) {
            throw new GeneralException(
                TransactionError.UNEXPECTED_ERROR);
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
                            .flatMap(errorBody -> Mono.error(new GeneralException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (GeneralException ex) {
            throw ex;
        } catch (WebClientResponseException ex) {
            throw new GeneralException(
                TransactionError.SERVICE_COMMUNICATION_FAILED);
        } catch (Exception ex) {
            throw new GeneralException(
                TransactionError.UNEXPECTED_ERROR);
        }
    }

    public <T> T secureDeleteMethod(String uri, Class<T> responseType, Object... uriVariables) {
        try {

            return securedWebClient.delete()
                .uri(uri, uriVariables)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(ErrorDto.class)
                            .flatMap(errorBody -> Mono.error(new GeneralException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (GeneralException ex) {
            throw ex;
        } catch (WebClientResponseException ex) {
            throw new GeneralException(
                TransactionError.SERVICE_COMMUNICATION_FAILED);
        } catch (Exception ex) {
            throw new GeneralException(
                TransactionError.UNEXPECTED_ERROR );
        }
    }
    public <T, R> R securePutMethod(
        String uri, T requestBody, Class<R> responseType, Object... uriVariables
    ) {
        try {
            return securedWebClient.put()
                .uri(uri, uriVariables)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                    HttpStatusCode::isError,
                    response -> response
                        .bodyToMono(ErrorDto.class)
                        .flatMap(error ->
                            Mono.error(new GeneralException(error))
                        )
                )
                .bodyToMono(responseType)
                .block();

        } catch (GeneralException e) {
            throw e;

        } catch (WebClientResponseException e) {
            throw new GeneralException(
                TransactionError.SERVICE_COMMUNICATION_FAILED
            );

        } catch (Exception e) {
            throw new GeneralException(
                TransactionError.UNEXPECTED_ERROR
            );
        }
    }

}
