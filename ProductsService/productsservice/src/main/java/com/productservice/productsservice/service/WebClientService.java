package com.productservice.productsservice.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.productservice.productsservice.dto.errors.ErrorDto;
import com.productservice.productsservice.enums.ProductError;
import com.productservice.productsservice.exception.ProductGeneralException;

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
                            .flatMap(errorBody -> Mono.error(new ProductGeneralException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (ProductGeneralException ex) {
            throw ex;
        } catch (WebClientResponseException ex) {
            throw new ProductGeneralException(
                ProductError.SERVICE_COMMUNICATION_FAILED);
        } catch (Exception ex) {
            throw new ProductGeneralException(
                ProductError.UNEXPECTED_ERROR);
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
                            .flatMap(errorBody -> Mono.error(new ProductGeneralException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (ProductGeneralException ex) {
            throw ex;
        } catch (WebClientResponseException ex) {
            throw new ProductGeneralException(
                ProductError.SERVICE_COMMUNICATION_FAILED);
        } catch (Exception ex) {
            throw new ProductGeneralException(
                ProductError.UNEXPECTED_ERROR);
        }
    }

    public <T> T secureDeleteMethod(String uri, Class<T> responseType, Object... uriVariables) {
        try {

            return securedWebClient.delete()
                .uri(uri, uriVariables)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(ErrorDto.class)
                            .flatMap(errorBody -> Mono.error(new ProductGeneralException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (ProductGeneralException ex) {
            throw ex;
        } catch (WebClientResponseException ex) {
            throw new ProductGeneralException(
                ProductError.SERVICE_COMMUNICATION_FAILED);
        } catch (Exception ex) {
            throw new ProductGeneralException(
                ProductError.UNEXPECTED_ERROR );
        }
    }
}
