package com.pm.aiagent.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LlmClient {

    @Value("${ai.openai.api-key:}")  // default empty
    private String apiKey;

    @Value("${ai.openai.model:gpt-4o-mini}") // default model
    private String model;

    private final OkHttpClient client;

    public LlmClient(OkHttpClient client) {
        this.client = client;
    }

    public String generateResponse(String prompt) {
        // If API key is missing, return a dummy response
        if (apiKey == null || apiKey.isBlank()) {
            return "AI suggestions are temporarily unavailable.";
        }

        String body = """
        {
          "model": "%s",
          "messages": [
            { "role": "system", "content": "You are a professional fitness AI coach." },
            { "role": "user", "content": "%s" }
          ],
          "temperature": 0.7
        }
        """.formatted(model, prompt);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(RequestBody.create(body, MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            return "Error communicating with AI: " + e.getMessage();
        }
    }
}
