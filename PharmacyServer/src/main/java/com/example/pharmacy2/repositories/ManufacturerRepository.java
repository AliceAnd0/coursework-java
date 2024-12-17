package com.example.pharmacy2.repositories;

import java.util.List;

import com.example.pharmacy2.models.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {
    @Query("SELECT p from Manufacturer p where concat(p.name, '', p.country, '', p.address) like %?1%")
    List<Manufacturer> search(String keyword);
}