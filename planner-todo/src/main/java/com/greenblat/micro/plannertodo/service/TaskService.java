package com.greenblat.micro.plannertodo.service;

import com.greenblat.micro.plannerentity.entity.Task;
import com.greenblat.micro.plannertodo.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service

@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository repository) {
        this.taskRepository = repository;
    }


    public List<Task> findAll(Long userId) {
        return taskRepository.findByUserIdOrderByTitleAsc(userId);
    }

    public Task add(Task task) {
        return taskRepository.save(task);
    }

    public Task update(Task task) {
        return taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }


    public Page<Task> findByParams(String text, Integer completed, Long priorityId, Long categoryId, Long userId, Date dateFrom, Date dateTo, PageRequest paging) {
        return taskRepository.findByParams(text, completed, priorityId, categoryId, userId, dateFrom, dateTo, paging);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).get();
    }


}
