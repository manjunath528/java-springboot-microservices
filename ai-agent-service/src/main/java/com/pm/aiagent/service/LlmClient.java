package com.pm.aiagent.service;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LlmClient {

    private final OkHttpClient client;

    @Value("${ai.openai.api-key:}")
    private String apiKey;

    @Value("${ai.openai.model:gpt-4o-mini}")
    private String model;

    public LlmClient(OkHttpClient client) {
        this.client = client;
    }

    public boolean isEnabled() {
        return apiKey != null && !apiKey.isBlank();
    }

    /**
     * Returns a plain-text recommendation message.
     * If API key is missing, returns a safe fallback message.
     */
    public String generateCoachMessage(String prompt) throws IOException {
        if (!isEnabled()) {
            return "[LLM_DISABLED] " + fallback(prompt);
        }

        String json = """
        {
          "model": "%s",
          "messages": [
            { "role": "system", "content": "You are a professional fitness AI coach. Respond with short, actionable advice." },
            { "role": "user", "content": %s }
          ],
          "temperature": 0.7
        }
        """.formatted(model, toJsonString(prompt));

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) return "No response body from LLM.";
            String body = response.body().string();

            // Minimal extraction without adding json libs:
            // looks for: "content":"...."
            String content = extractContent(body);
            return content != null ? content : body;
        }
    }

    private static String toJsonString(String s) {
        // minimal JSON escaping
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }

    private static String extractContent(String json) {
        // very small heuristic parser to avoid extra deps
        String needle = "\"content\":";
        int idx = json.indexOf(needle);
        if (idx < 0) return null;

        int start = json.indexOf('"', idx + needle.length());
        if (start < 0) return null;
        start++;

        int end = json.indexOf('"', start);
        if (end < 0) return null;

        return json.substring(start, end).replace("\\n", "\n");
    }

    private static String fallback(String prompt) {
        // Simple fallback logic
        if (prompt.toLowerCase().contains("calories") && prompt.toLowerCase().contains("nutrition")) {
            return "Try to balance your meal with more protein and fiber.";
        }
        return "Keep consistency today: a short workout + hydration goes a long way.";
    }
}
