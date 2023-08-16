package com.bookshop.repository;

import com.bookshop.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query(value = "FROM User u JOIN FETCH u.roles r "
            + "WHERE u.email = :email AND u.isDeleted = false AND r.isDeleted = false")
    Optional<User> findByEmail(String email);
}
