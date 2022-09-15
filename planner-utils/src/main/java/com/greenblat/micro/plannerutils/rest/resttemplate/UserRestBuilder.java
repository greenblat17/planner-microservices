package com.greenblat.micro.plannerutils.rest.resttemplate;

import com.greenblat.micro.plannerentity.entity.User;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserRestBuilder {

    private static final String BASE_URL = "http://localhost:8765/planner-users/user";

    public boolean userExists(Long userId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity request = new HttpEntity(headers);


        ResponseEntity<User> response = null;

        try {
            response = restTemplate.exchange(BASE_URL + "/show?user_id={userId}", HttpMethod.GET, request, User.class, userId);

            if (response.getStatusCode() == HttpStatus.OK)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
