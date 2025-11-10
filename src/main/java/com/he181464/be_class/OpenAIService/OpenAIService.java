package com.he181464.be_class.OpenAIService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class OpenAIService {
    @Value("${openai.api-key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/chat/completions")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public String ask(String question) {
        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", new Object[]{
                        Map.of("role", "user", "content", question)
                }
        );

        try {
            Map<String, Object> response = webClient.post()
                    .header("Authorization", "Bearer " + apiKey)
                    .body(Mono.just(body), Map.class)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            var choices = (java.util.List<Map<String, Object>>) response.get("choices");
            var message = (Map<String, Object>) choices.get(0).get("message");
            return message.get("content").toString();
        } catch (Exception e) {
            return "Lỗi khi gọi OpenAI API: " + e.getMessage();
        }
    }
}
