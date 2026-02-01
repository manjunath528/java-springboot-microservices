package com.pm.aiagent.store;

import com.pm.aiagent.model.UserContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserContextStore {

    private final Map<String, UserContext> store = new ConcurrentHashMap<>();

    public void put(UserContext ctx) {
        store.put(ctx.userId(), ctx);
    }

    public Optional<UserContext> get(String userId) {
        return Optional.ofNullable(store.get(userId));
    }
}
