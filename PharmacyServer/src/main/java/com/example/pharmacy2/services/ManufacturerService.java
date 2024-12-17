package com.example.pharmacy2.services;

import com.example.pharmacy2.models.Manufacturer;
import com.example.pharmacy2.repositories.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService {
    @Autowired
    private ManufacturerRepository repo;

    public List<Manufacturer> listAll(String keyword) {
        if (keyword != null) {
            return repo.search(keyword);
        }
        return repo.findAll();
    }

    public void save(Manufacturer manufacturer) {
        repo.save(manufacturer);
    }

    public Manufacturer get(Integer id) {
        return repo.findById(id).get();
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }

}
