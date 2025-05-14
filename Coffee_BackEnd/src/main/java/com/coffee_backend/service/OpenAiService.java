package com.coffee_backend.service;

import com.coffee_backend.dto.AiQueryRequest;
import com.coffee_backend.dto.AiQueryResponse;
import com.coffee_backend.exception.BadRequestException;
import com.coffee_backend.repo.ArticleRepository;
import com.coffee_backend.repo.CourseRepository;
import com.coffee_backend.repo.FarmRepository;
import com.coffee_backend.util.PromptFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class OpenAiService {
    @Autowired
    private WebClient webClient;

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private CourseRepository courseRepository;

    public Mono<String> chat(String prompt) {
        Map<String, Object> body = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", prompt)
                )
        );
        return webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .map(resp -> ((Map<?, ?>) ((List<?>) resp.get("choices")).get(0)).get("message"))
                .map(m -> ((Map<?, ?>) m).get("content").toString());
    }

    public Mono<AiQueryResponse> query(AiQueryRequest req) {
        Mono<List> dataMono;
        switch(req.getResource()) {
            case "article":
                dataMono = load(req, articleRepository);
                break;
            case "farm":
                dataMono = load(req, farmRepository);
                break;
            case "course":
                dataMono = load(req, courseRepository);
                break;
            default:
                return Mono.error(new BadRequestException("Error"));
        }

        return dataMono.flatMap(list -> {
            String sys = PromptFactory.getSystemPrompt(req.getResource(), req.getMode());
            String prompt = PromptFactory.buildPrompt(sys, list, req.getQuery());
            return chat(prompt);
        }).map(answer -> {
            AiQueryResponse r = new AiQueryResponse();
            r.setAnswer(answer);
            return r;
        });
    }

    private Mono<List> load(AiQueryRequest req, JpaRepository repo) {
        if ("single".equals(req.getMode())) {
            return Mono.just(
                    List.of(repo.findById(req.getTargetId()).orElseThrow())
            );
        } else {
            return Mono.just(repo.findAll());
        }
    }
}
