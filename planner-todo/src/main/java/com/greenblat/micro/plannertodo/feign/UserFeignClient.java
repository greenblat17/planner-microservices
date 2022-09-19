package com.greenblat.micro.plannertodo.feign;

import com.greenblat.micro.plannerentity.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "planner-users")
public interface UserFeignClient {

    @GetMapping("/user/show")
    ResponseEntity<User> findUserById(@RequestParam("user_id") Long id);
}

//@Component
//class UserFeignClientFallback implements UserFeignClient{
//
//    @Override
//    public ResponseEntity<User> findUserById(Long id) {
//        return null;
//    }
//
//}
