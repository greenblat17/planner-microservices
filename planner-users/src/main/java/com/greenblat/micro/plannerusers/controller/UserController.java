package com.greenblat.micro.plannerusers.controller;

import com.greenblat.micro.plannerentity.entity.Task;
import com.greenblat.micro.plannerentity.entity.User;
import com.greenblat.micro.plannerusers.search.UserSearchValues;
import com.greenblat.micro.plannerusers.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    public static final String ID_COLUMN = "id";
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> showUser(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/all")
    public ResponseEntity<User> showAllUsersById(@RequestParam(value = "user_id", required = false) Long userId, @RequestParam(value = "email", required = false) String email) {
        if (userId == null && email == null) {
            return new ResponseEntity("missed param: email OR id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (email != null) {
            return ResponseEntity.ok(userService.getUserByEmail(email));
        }

        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<User> add(@RequestBody User user) {

        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
        }

        return ResponseEntity.ok(userService.addUser(user));

    }


    @PutMapping("/update")
    public ResponseEntity<Task> update(@RequestBody User user) {

        if (user.getId() == null || user.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            return new ResponseEntity("missed param: email", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getUsername() == null || user.getUsername().trim().length() == 0) {
            return new ResponseEntity("missed param: username", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getPassword() == null || user.getPassword().trim().length() == 0) {
            return new ResponseEntity("missed param: password", HttpStatus.NOT_ACCEPTABLE);
        }

        userService.updateUser(user);

        return new ResponseEntity(HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Task> deleteByUserId(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Task> deleteByUserEmail(@RequestParam("email") String email) {
        userService.deleteUser(email);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<User>> search(@RequestBody UserSearchValues userSearchValues) {

        String email = userSearchValues.getEmail() != null ? userSearchValues.getEmail() : null;
        String username = userSearchValues.getUsername() != null ? userSearchValues.getUsername() : null;

        String sortColumn = userSearchValues.getSortColumn() != null ? userSearchValues.getSortColumn() : null;
        String sortDirection = userSearchValues.getSortDirection() != null ? userSearchValues.getSortDirection() : null;

        Integer pageNumber = userSearchValues.getPageNumber() != null ? userSearchValues.getPageNumber() : null;
        Integer pageSize = userSearchValues.getPageSize() != null ? userSearchValues.getPageSize() : null;

        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> result = userService.getByParams(username, email, pageRequest);

        return ResponseEntity.ok(result);
    }

}
