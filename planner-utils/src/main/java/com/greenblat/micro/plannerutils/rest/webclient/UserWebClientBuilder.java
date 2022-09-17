package com.greenblat.micro.plannerutils.rest.webclient;

import com.greenblat.micro.plannerentity.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.concurrent.Flow;

@Component
public class UserWebClientBuilder {

    private static final String BASE_URL_USER = "http://localhost:8765/planner-users/user";
    private static final String BASE_URL_DATA = "http://localhost:8765/planner-todo/data";

    public boolean userExists(Long userId) {

        try {
            User user = WebClient.create(BASE_URL_USER)
                    .get()
                    .uri(uriBuilder -> uriBuilder.path("/show")
                            .queryParam("user_id", userId)
                            .build())
                    .retrieve()
                    .bodyToFlux(User.class)
                    .blockFirst();

            if (user != null)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Flux<User> userExistAsync(Long userId) {

        return WebClient.create(BASE_URL_USER)
                .get()
                .uri(uriBuilder -> uriBuilder.path("/show")
                        .queryParam("user_id", userId)
                        .build())
                .retrieve()
                .bodyToFlux(User.class);
    }

    public Flux<Boolean> initUserData(Long userId) {
        return WebClient.create(BASE_URL_DATA)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/init/{id}")
                        .build(userId))
                .retrieve()
                .bodyToFlux(Boolean.class);
    }
}
