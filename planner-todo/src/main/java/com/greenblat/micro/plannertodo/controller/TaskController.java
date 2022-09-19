package com.greenblat.micro.plannertodo.controller;

import com.greenblat.micro.plannerentity.entity.Task;
import com.greenblat.micro.plannerentity.entity.User;
import com.greenblat.micro.plannertodo.feign.UserFeignClient;
import com.greenblat.micro.plannertodo.search.TaskSearchValues;
import com.greenblat.micro.plannertodo.service.TaskService;
import com.greenblat.micro.plannerutils.rest.webclient.UserWebClientBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    public static final String ID_COLUMN = "id";

    private final TaskService taskService;
    private final UserFeignClient userFeignClient;


    public TaskController(TaskService taskService,  UserFeignClient userFeignClient) {
        this.taskService = taskService;
        this.userFeignClient = userFeignClient;
    }


    @GetMapping("/all")
    public ResponseEntity<List<Task>> findAll(@RequestParam("user_id") Long userId) {
        return ResponseEntity.ok(taskService.findAll(userId ));
    }

    @PostMapping("/add")
    public ResponseEntity<Task> add(@RequestBody Task task) {

        if (task.getId() != null && task.getId() != 0) {
            return new ResponseEntity("redundant param: id MUST be null", HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }

        ResponseEntity<User> result =  userFeignClient.findUserById(task.getUserId());
        if (result == null){
            return new ResponseEntity("система пользователей недоступна, попробуйте позже", HttpStatus.NOT_FOUND);
        }
        if (result.getBody() != null){
            return ResponseEntity.ok(taskService.add(task));
        }

        return new ResponseEntity("user id=" + task.getUserId() + " not found", HttpStatus.NOT_ACCEPTABLE);

    }


    @PutMapping("/update")
    public ResponseEntity<Task> update(@RequestBody Task task) {

        if (task.getId() == null || task.getId() == 0) {
            return new ResponseEntity("missed param: id", HttpStatus.NOT_ACCEPTABLE);
        }

        if (task.getTitle() == null || task.getTitle().trim().length() == 0) {
            return new ResponseEntity("missed param: title", HttpStatus.NOT_ACCEPTABLE);
        }


        taskService.update(task);

        return new ResponseEntity(HttpStatus.OK);

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Task> delete(@PathVariable("id") Long id) {
        taskService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    // получение объекта по id
    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable("id") Long id) {
        Task task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }


    @PostMapping("/search")
    public ResponseEntity<Page<Task>> search(@RequestBody TaskSearchValues taskSearchValues) throws ParseException {

        String title = taskSearchValues.getTitle() != null ? taskSearchValues.getTitle() : null;

        // конвертируем Boolean в Integer
        Integer completed = taskSearchValues.getCompleted() != null ? taskSearchValues.getCompleted() : null;

        Long priorityId = taskSearchValues.getPriorityId() != null ? taskSearchValues.getPriorityId() : null;
        Long categoryId = taskSearchValues.getCategoryId() != null ? taskSearchValues.getCategoryId() : null;

        String sortColumn = taskSearchValues.getSortColumn() != null ? taskSearchValues.getSortColumn() : null;
        String sortDirection = taskSearchValues.getSortDirection() != null ? taskSearchValues.getSortDirection() : null;

        Integer pageNumber = taskSearchValues.getPageNumber() != null ? taskSearchValues.getPageNumber() : null;
        Integer pageSize = taskSearchValues.getPageSize() != null ? taskSearchValues.getPageSize() : null;

        Long userId = taskSearchValues.getUserId() != null ? taskSearchValues.getUserId() : null;

        if (userId == null || userId == 0) {
            return new ResponseEntity("missed param: user_id", HttpStatus.NOT_ACCEPTABLE);
        }


        // чтобы захватить в выборке все задачи по датам, независимо от времени - можно выставить время с 00:00 до 23:59

        Date dateFrom = null;
        Date dateTo = null;


        // выставить 00:00 для начальной даты (если она указана)
        if (taskSearchValues.getDateFrom() != null) {
            Calendar calendarFrom = Calendar.getInstance();
            calendarFrom.setTime(taskSearchValues.getDateFrom());
            calendarFrom.set(Calendar.HOUR_OF_DAY, 0);
            calendarFrom.set(Calendar.MINUTE, 0);
            calendarFrom.set(Calendar.SECOND, 0);
            calendarFrom.set(Calendar.MILLISECOND, 0);

            dateFrom = calendarFrom.getTime(); // записываем начальную дату с 00:00

        }


        // выставить 23:59 для конечной даты (если она указана)
        if (taskSearchValues.getDateTo() != null) {

            Calendar calendarTo = Calendar.getInstance();
            calendarTo.setTime(taskSearchValues.getDateTo());
            calendarTo.set(Calendar.HOUR_OF_DAY, 23);
            calendarTo.set(Calendar.MINUTE, 59);
            calendarTo.set(Calendar.SECOND, 59);
            calendarTo.set(Calendar.MILLISECOND, 999);

            dateTo = calendarTo.getTime(); // записываем конечную дату с 23:59

        }


        Sort.Direction direction = sortDirection == null || sortDirection.trim().length() == 0 || sortDirection.trim().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(direction, sortColumn, ID_COLUMN);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Task> result = taskService.findByParams(title, completed, priorityId, categoryId, userId, dateFrom, dateTo, pageRequest);

        return ResponseEntity.ok(result);

    }


}
