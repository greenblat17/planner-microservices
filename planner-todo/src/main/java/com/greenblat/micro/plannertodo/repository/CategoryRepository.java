package com.greenblat.micro.plannertodo.repository;

import com.greenblat.micro.plannerentity.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserIdOrderByTitleAsc(Long id);

    @Query(
            "SELECT c FROM Category c WHERE" +
                    "(:title IS NULL OR " +
                    ":title='' OR " +
                    "lower(:title) like lower(concat('%', :title, '%') ) )" +
                    "AND c.userId=:userId " +
                    "ORDER BY c.title ASC"
    )
    List<Category> findByTitle(@Param("title") String title, @Param("userId") Long userId);
}
