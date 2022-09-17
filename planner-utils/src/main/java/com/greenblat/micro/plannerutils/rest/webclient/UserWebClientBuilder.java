package com.greenblat.micro.plannerutils.rest.webclient;

import com.greenblat.micro.plannerentity.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public class UserWebClientBuilder {

    private static final String BASE_URL = "http://localhost:8765/planner-users/user";

    public boolean userExists(Long userId) {

        try {
            User user = WebClient.create(BASE_URL)
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
        Flux<User> userFlux = WebClient.create(BASE_URL)
                .get()
                .uri(uriBuilder -> uriBuilder.path("/show")
                        .queryParam("user_id", userId)
                        .build())
                .retrieve()
                .bodyToFlux(User.class);

        return userFlux;
    }
}
