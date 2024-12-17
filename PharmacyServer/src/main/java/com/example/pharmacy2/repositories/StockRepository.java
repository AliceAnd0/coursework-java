package com.example.pharmacy2.repositories;

import com.example.pharmacy2.models.Medicine2;
import com.example.pharmacy2.models.Pharmacy;
import com.example.pharmacy2.models.Stock;
import com.example.pharmacy2.models.StockPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, StockPK>, JpaSpecificationExecutor<Stock> {
    /*@Query("SELECT p from Stock p where concat(p.medicine.id, ' ', p.pharmacy.id) like %?1%")
    List<Stock> search(String keyword);*/

    /*@Query("SELECT DISTINCT s.pharmacy FROM Stock s")
    List<Pharmacy> findDistinctPharmacies();

    @Query("SELECT s.medicine FROM Stock s WHERE s.pharmacy.id = :pharmacyId")
    List<Medicine2> findMedicinesByPharmacy(int pharmacyId);*/
}