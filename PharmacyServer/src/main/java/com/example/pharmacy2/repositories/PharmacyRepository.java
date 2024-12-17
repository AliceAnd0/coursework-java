package com.example.pharmacy2.repositories;

import java.util.List;

import com.example.pharmacy2.models.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Integer> {
    @Query("SELECT p from Pharmacy p where concat(p.id, '', p.address, '', p.phoneNumber) like %?1%")
    List<Pharmacy> search(String keyword);
}
