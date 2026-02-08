package com.pm.aiagent.service;

import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class LlmClientTest {

    @Test
    void isEnabledReturnsFalseWhenApiKeyBlank() {
        LlmClient client = new LlmClient(new OkHttpClient());
        ReflectionTestUtils.setField(client, "apiKey", "");

        assertThat(client.isEnabled()).isFalse();
    }

    @Test
    void isEnabledReturnsTrueWhenApiKeyProvided() {
        LlmClient client = new LlmClient(new OkHttpClient());
        ReflectionTestUtils.setField(client, "apiKey", "test-key");

        assertThat(client.isEnabled()).isTrue();
    }

    @Test
    void generateCoachMessageReturnsFallbackWhenDisabled() throws Exception {
        LlmClient client = new LlmClient(new OkHttpClient());
        ReflectionTestUtils.setField(client, "apiKey", "");

        String message = client.generateCoachMessage("Nutrition calories summary");

        assertThat(message).startsWith("[LLM_DISABLED]");
        assertThat(message).contains("balance your meal");
    }
}
