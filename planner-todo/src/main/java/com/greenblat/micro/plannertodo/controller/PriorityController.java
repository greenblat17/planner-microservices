package com.greenblat.micro.plannertodo.controller;

import com.greenblat.micro.plannerentity.entity.Priority;
import com.greenblat.micro.plannertodo.search.PrioritySearchValues;
import com.greenblat.micro.plannertodo.service.PriorityService;
import com.greenblat.micro.plannerutils.rest_template.UserRestBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    private final PriorityService priorityService;
    private final UserRestBuilder userRestBuilder;

    public PriorityController(PriorityService priorityService, UserRestBuilder userRestBuilder) {
        this.priorityService = priorityService;
        this.userRestBuilder = userRestBuilder;
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

        if (userRestBuilder.userExists(priority.getUserId())) {
            return ResponseEntity.ok(priorityService.addPriority(priority));
        }

        return new ResponseEntity("user with id=" + priority.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);
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
