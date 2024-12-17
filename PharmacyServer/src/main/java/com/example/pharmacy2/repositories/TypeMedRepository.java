package com.example.pharmacy2.repositories;

import com.example.pharmacy2.models.Pharmacy;
import com.example.pharmacy2.models.TypeMed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeMedRepository extends JpaRepository<TypeMed, Integer> {
    @Query("SELECT p from TypeMed p where concat(p.id, '', p.name) like %?1%")
    List<TypeMed> search(String keyword);
}
