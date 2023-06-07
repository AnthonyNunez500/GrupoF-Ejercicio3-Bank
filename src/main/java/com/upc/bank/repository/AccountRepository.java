package com.upc.bank.repository;


import com.upc.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository {
    List<Account>  findById(Long id);
    List<Account>  filterByName(String nameCustomer);
    boolean existsByName(String nameCustomer);
}
