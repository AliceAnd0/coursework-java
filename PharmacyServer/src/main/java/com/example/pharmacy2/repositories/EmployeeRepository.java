package com.example.pharmacy2.repositories;

import java.util.List;
import java.util.Optional;

import com.example.pharmacy2.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<AppUser, Long> {
    @Query("SELECT p from AppUser p where concat(p.username, '', p.role) like %?1%")
    List<AppUser> search(String keyword);

    Optional<AppUser> findByUsername(String username);
}