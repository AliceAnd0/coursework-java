package com.example.pharmacy2.services;

import com.example.pharmacy2.models.Stock;
import com.example.pharmacy2.models.StockPK;
import com.example.pharmacy2.repositories.StockRepository;
import com.example.pharmacy2.specifications.StockSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    @Autowired
    private StockRepository repoStock;


    public List<Stock> listAll() {
        return repoStock.findAll();
    }

    public void save(Stock stock, int medId, int pharmId) {
        StockPK pk = new StockPK(pharmId, medId);
        stock.setPk(pk);
        repoStock.save(stock);
    }

    public void saveEdittedStock(Stock stock) {
        repoStock.save(stock);
    }

    public Stock get(int medId, int pharmId) {
        StockPK stockPK = new StockPK(medId, pharmId); // Создаем составной ключ
        return repoStock.findById(stockPK).orElse(null); // Получаем объект Stock
    }

    public void delete(int id1, int id2) {
        StockPK stockPK = new StockPK(id1, id2); // Создаем составной ключ
        repoStock.deleteById(stockPK);
    }

    public List<Stock> filter(Integer medicineId, Integer pharmacyId, Integer amount) {
        Specification<Stock> specification = Specification.where(null);

        if (medicineId != null) {
            specification = specification.and(StockSpecifications.isLikeMedicineId(medicineId));
        }
        if (pharmacyId != null) {
            specification = specification.and(StockSpecifications.isLikePharmacyId(pharmacyId));
        }
        if (amount != null) {
            specification = specification.and(StockSpecifications.isLikeAmount(amount));
        }
        return repoStock.findAll(specification);
    }

    /*public List<Pharmacy> findDistinctPharmacies() {
        return repoStock.findDistinctPharmacies();
    }

    public List<Medicine2> findMedicinesByPharmacy(int pharmacyId) {
        return repoStock.findMedicinesByPharmacy(pharmacyId);
    }*/

}
