package com.example.pharmacy2.services;

import com.example.pharmacy2.models.TypeMed;
import com.example.pharmacy2.repositories.TypeMedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeMedService {
    @Autowired
    private TypeMedRepository repo;

    public List<TypeMed> listAll(String keyword) {
        if (keyword != null) {
            return repo.search(keyword);
        }
        return repo.findAll();
    }


    //@PreAuthorize("hasRole('ADMIN')")
    public void save(TypeMed typeMed) {
        repo.save(typeMed);
    }

    public TypeMed get(Integer id) {
        return repo.findById(id).get();
    }

    public void delete(Integer id) {
        repo.deleteById(id);
    }
}
