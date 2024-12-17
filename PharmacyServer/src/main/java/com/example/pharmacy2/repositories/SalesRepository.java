package com.example.pharmacy2.repositories;
import java.util.List;

import com.example.pharmacy2.models.Medicine2;
import com.example.pharmacy2.models.Pharmacy;
import com.example.pharmacy2.models.Sales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface SalesRepository extends JpaRepository<Sales, Integer>, JpaSpecificationExecutor<Sales> {
    /*@Query("SELECT p from Sales p where concat(p.id, '', p.date, '', p.medicineName) like %?1%")
    List<Sales> search(String keyword);*/
}