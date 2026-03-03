package com.productservice.productsservice.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import com.productservice.productsservice.dto.errors.ErrorDto;
import com.productservice.productsservice.enums.ProductError;
import com.productservice.productsservice.exception.GeneralException;

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
                    .switchIfEmpty(
                            Mono.error(
                                new GeneralException(
                                    ErrorDto.builder()
                                        .from("remote service")
                                        .status(String.valueOf(response.statusCode().value()))
                                        .message("Remote service returned error without body")
                                        .build()
                                )
                            )
                        )
                            .flatMap(errorBody -> Mono.error(new GeneralException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (GeneralException e) {
            throw e;
        } 
        catch (WebClientRequestException  ex) {
            throw new GeneralException(
                ProductError.SERVICE_COMMUNICATION_FAILED, ex);
        } catch (Exception ex) {
            throw new GeneralException(
                ProductError.UNEXPECTED_ERROR, ex
            );
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
                    .switchIfEmpty(
                            Mono.error(
                                new GeneralException(
                                    ErrorDto.builder()
                                        .from("remote service")
                                        .status(String.valueOf(response.statusCode().value()))
                                        .message("Remote service returned error without body")
                                        .build()
                                )
                            )
                        )
                            .flatMap(errorBody -> Mono.error(new GeneralException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (GeneralException e) {
            throw e;
        } 
        catch (WebClientRequestException  ex) {
            throw new GeneralException(
                ProductError.SERVICE_COMMUNICATION_FAILED, ex);
        } catch (Exception ex) {
            throw new GeneralException(
                ProductError.UNEXPECTED_ERROR, ex
            );
        }
    }

    public <T> T secureDeleteMethod(String uri, Class<T> responseType, Object... uriVariables) {
        try {

            return securedWebClient.delete()
                .uri(uri, uriVariables)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(ErrorDto.class)
                    .switchIfEmpty(
                            Mono.error(
                                new GeneralException(
                                    ErrorDto.builder()
                                        .from("remote service")
                                        .status(String.valueOf(response.statusCode().value()))
                                        .message("Remote service returned error without body")
                                        .build()
                                )
                            )
                        )
                            .flatMap(errorBody -> Mono.error(new GeneralException(errorBody)))
                )
                .bodyToMono(responseType)
                .block();

        } catch (GeneralException e) {
            throw e;
        } 
        catch (WebClientRequestException  ex) {
            throw new GeneralException(
                ProductError.SERVICE_COMMUNICATION_FAILED, ex);
        } catch (Exception ex) {
            throw new GeneralException(ProductError.UNEXPECTED_ERROR, ex);
        }
    }
}

