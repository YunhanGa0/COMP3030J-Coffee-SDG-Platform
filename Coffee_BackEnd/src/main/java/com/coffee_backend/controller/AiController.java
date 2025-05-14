package com.coffee_backend.controller;

import com.coffee_backend.dto.AiQueryRequest;
import com.coffee_backend.dto.AiQueryResponse;
import com.coffee_backend.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    @Autowired
    private OpenAiService openAiService;

    @PostMapping("/query")
    public Mono<AiQueryResponse> query(@RequestBody AiQueryRequest req) {
        return openAiService.query(req);
    }
}
