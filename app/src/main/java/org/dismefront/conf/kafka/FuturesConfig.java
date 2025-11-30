package org.dismefront.conf.kafka;

import org.dismefront.futures.CompletableFutureObject;
import org.springframework.context.annotation.Bean;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FuturesConfig<T extends CompletableFutureObject> {
    private final Map<String, CompletableFuture<T>> futures = new ConcurrentHashMap<>();

    public CompletableFuture<T> createFuture(String futureId) {
        CompletableFuture<T> future = new CompletableFuture<>();
        futures.put(futureId, future);
        return future;
    }

    public void completeFuture(T event) {
        CompletableFuture<T> future = futures.remove(event.getId());
        if (future != null) {
            future.complete(event);
        } else {
            System.err.println("No future found for ID: " + event.getId());
        }
    }

    @Bean
    public Map<String, CompletableFuture<T>> responseMap() {
        return futures;
    }

}
