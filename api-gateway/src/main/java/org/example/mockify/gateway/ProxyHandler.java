package org.example.mockify.gateway;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Set;

@Component
public class ProxyHandler {

    private static final Set<String> HOP_BY_HOP = Set.of(
            HttpHeaders.CONNECTION,
            HttpHeaders.TRANSFER_ENCODING,
            HttpHeaders.HOST,
            "keep-alive",
            "te",
            "trailers",
            "upgrade"
    );

    private final WebClient webClient = WebClient.create();

    public Mono<ServerResponse> proxy(ServerRequest request, String targetBaseUrl) {
        String path = request.uri().getRawPath();
        String query = request.uri().getRawQuery();
        URI targetUri = URI.create(targetBaseUrl + path + (query != null ? "?" + query : ""));

        return request.bodyToMono(byte[].class)
                .defaultIfEmpty(new byte[0])
                .flatMap(body ->
                        webClient.method(request.method())
                                .uri(targetUri)
                                .headers(h -> request.headers().asHttpHeaders().forEach((key, values) -> {
                                    if (!HOP_BY_HOP.contains(key.toLowerCase())) {
                                        h.addAll(key, values);
                                    }
                                }))
                                .bodyValue(body)
                                .exchangeToMono(response ->
                                        response.bodyToMono(byte[].class)
                                                .defaultIfEmpty(new byte[0])
                                                .flatMap(respBody -> {
                                                    var builder = ServerResponse.status(response.statusCode())
                                                            .headers(h -> response.headers().asHttpHeaders().forEach((key, values) -> {
                                                                if (!HOP_BY_HOP.contains(key.toLowerCase())) {
                                                                    h.addAll(key, values);
                                                                }
                                                            }));
                                                    return respBody.length > 0
                                                            ? builder.bodyValue(respBody)
                                                            : builder.build();
                                                })
                                )
                );
    }
}
