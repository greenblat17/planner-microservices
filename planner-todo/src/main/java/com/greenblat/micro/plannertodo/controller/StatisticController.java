package com.greenblat.micro.plannertodo.controller;

import com.greenblat.micro.plannerentity.entity.Statistic;
import com.greenblat.micro.plannertodo.service.StatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistic")
public class StatisticController {

    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/all")
    public ResponseEntity<Statistic> showAllStatistic(@RequestParam("user_id") Long userId) {
        return ResponseEntity.ok(statisticService.findAll(userId));
    }
}
