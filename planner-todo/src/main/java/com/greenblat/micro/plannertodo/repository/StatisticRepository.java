package com.greenblat.micro.plannertodo.repository;

import com.greenblat.micro.plannerentity.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    Statistic findByUserId(Long id);
}
