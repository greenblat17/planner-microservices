package com.greenblat.micro.plannerusers.repository;

import com.greenblat.micro.plannerentity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    void deleteByEmail(String email);

    @Query(
            "SELECT u FROM User u WHERE " +
                    "(:email IS NULL OR :email='' OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%') ) ) OR " +
                    "(:username IS NULL OR :email='' OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%') ) )"
    )
    Page<User> findByParams(@Param("email") String email, @Param("username") String username, Pageable pageable);
}
