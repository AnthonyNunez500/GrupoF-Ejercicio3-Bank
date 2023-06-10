package com.upc.bank.repository;


import com.upc.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long>{
    //List<Account>  findById(Long id);
    List<Account> findByNameCustomer(String nameCustomer);
    boolean existsByNameCustomerAndNumberAccount(String nameCustomer,String numberAccount);
}
