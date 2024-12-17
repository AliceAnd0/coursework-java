package com.example.pharmacy2.repositories;

import java.util.List;

import com.example.pharmacy2.models.Medicine2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface MedicineRepository extends JpaRepository<Medicine2, Integer>, JpaSpecificationExecutor<Medicine2> {
    @Query("SELECT p from Medicine2 p where concat(p.name, ' ', ' ', p.price) like %?1%")
    List<Medicine2> search(String keyword);
}
