package com.example.pharmacy2.services;

import com.example.pharmacy2.models.*;
import com.example.pharmacy2.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class PharmacyService {

    @Autowired
    private PharmacyRepository repo;

    public List<Pharmacy> listAll(String keyword) {
        if (keyword != null) {
            return repo.search(keyword);
        }
        return repo.findAll();
    }

    public void save(Pharmacy pharmacy) {
        repo.save(pharmacy);
    }

    public Pharmacy get(Integer id) {
        return repo.findById(id).get();
    }

    public void delete(int id) {
        repo.deleteById(id);
    }

}
