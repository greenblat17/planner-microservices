package com.greenblat.micro.plannertodo.controller;

import com.greenblat.micro.plannerentity.entity.Priority;
import com.greenblat.micro.plannerentity.entity.User;
import com.greenblat.micro.plannertodo.feign.UserFeignClient;
import com.greenblat.micro.plannertodo.search.PrioritySearchValues;
import com.greenblat.micro.plannertodo.service.PriorityService;
import com.greenblat.micro.plannerutils.rest.webclient.UserWebClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private final PriorityService priorityService;
    private final UserFeignClient userFeignClient;

    public PriorityController(PriorityService priorityService, UserFeignClient userFeignClient) {
        this.priorityService = priorityService;
        this.userFeignClient = userFeignClient;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Priority> findById(@PathVariable("id") Long id) {
        Priority priority = priorityService.getById(id);
        return ResponseEntity.ok(priority);
    }

    @GetMapping("/all")
    public List<Priority> showAllPriority(@RequestParam("user_id") Long userId) {
        return priorityService.findAll(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<Priority> add(@RequestBody Priority priority) {
        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("misses param: title must bu null", HttpStatus.NOT_ACCEPTABLE);
        }

        ResponseEntity<User> result =  userFeignClient.findUserById(priority.getUserId());
        if (result == null){
            return new ResponseEntity("система пользователей недоступна, попробуйте позже", HttpStatus.NOT_FOUND);
        }
        if (result.getBody() != null){
            return ResponseEntity.ok(priorityService.addPriority(priority));
        }

        return new ResponseEntity("user id=" + priority.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);
    }

    @PutMapping("/update")
    public ResponseEntity<Priority> update(@RequestBody Priority priority) {

        if (priority.getId() == null || priority.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (priority.getTitle() == null || priority.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        priorityService.updatePriority(priority);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Priority> delete(@PathVariable("id") Long id) {
        priorityService.deletePriority(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Priority>> search(@RequestBody PrioritySearchValues prioritySearchValues) {

        if (prioritySearchValues.getUserId() == null || prioritySearchValues.getUserId() == 0) {
            return new ResponseEntity("missed param: ID", HttpStatus.NOT_ACCEPTABLE);
        }

        List<Priority> priorityByTitle = priorityService.findAll(
                prioritySearchValues.getTitle(), prioritySearchValues.getUserId());

        return  ResponseEntity.ok(priorityByTitle);

    }
}
