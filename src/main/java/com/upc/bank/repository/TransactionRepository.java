package com.upc.bank.repository;
import com.upc.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
public interface TransactionRepository {
    List<Transaction> findById(Long id);
    List<Transaction> filterByNameCustomer(String nameCustomer);
    List<Transaction> filterByCreateDateRange(LocalDate initialDate,LocalDate finalDate);
}
