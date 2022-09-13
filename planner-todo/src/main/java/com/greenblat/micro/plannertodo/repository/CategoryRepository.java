package com.greenblat.backend.todo.repository;

import com.greenblat.backend.todo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserEmailOrderByTitleAsc(String email);

    @Query(
            "SELECT c FROM Category c WHERE" +
                    " c.user.email=:email AND " +
                    "(:title IS NULL OR " +
                    ":title='' OR " +
                    "lower(:title) like lower(concat('%', :title, '%') ) )" +
                    "ORDER BY c.title ASC"
    )
    List<Category> findByTitle(@Param("email") String email, @Param("title") String title);
}
