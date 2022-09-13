package com.greenblat.micro.plannertodo.service;

import com.greenblat.micro.plannerentity.entity.Statistic;
import com.greenblat.micro.plannertodo.repository.StatisticRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StatisticService {

    private final StatisticRepository statisticRepository;

    public StatisticService(StatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    public Statistic findAll(Long userId) {
        return statisticRepository.findByUserId(userId);
    }
}
