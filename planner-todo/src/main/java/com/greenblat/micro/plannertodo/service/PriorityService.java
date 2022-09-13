package com.greenblat.micro.plannertodo.service;

import com.greenblat.micro.plannerentity.entity.Priority;
import com.greenblat.micro.plannertodo.repository.PriorityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PriorityService {

    private final PriorityRepository priorityRepository;

    public PriorityService(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    public Priority getById(Long id) {
        return priorityRepository.findById(id).get();
    }

    public List<Priority> findAll(Long userId) {
        return priorityRepository.findByUserIdOrderByIdAsc(userId);
    }

    public List<Priority> findAll(String title, Long userId) {
        return priorityRepository.findByTitle(title, userId);
    }

    @Transactional
    public Priority addPriority(Priority priority) {
        return priorityRepository.save(priority);
    }

    @Transactional
    public Priority updatePriority(Priority priority) {
        return priorityRepository.save(priority);
    }

    @Transactional
    public void deletePriority(Long id) {
        priorityRepository.deleteById(id);
    }


}
