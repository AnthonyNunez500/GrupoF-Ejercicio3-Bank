package com.upc.bank.controller;

import com.upc.bank.exception.ResourceNotFoundException;
import com.upc.bank.model.Account;
import com.upc.bank.model.Transaction;
import com.upc.bank.repository.AccountRepository;
import com.upc.bank.repository.TransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/bank/v1")
public class TransactionController {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    public TransactionController(TransactionRepository transactionRepository, AccountRepository accountRepository){
        this.transactionRepository=transactionRepository;
        this.accountRepository=accountRepository;
    }

    @Transactional(readOnly = true)
    @GetMapping("/transactions/filterByNameCustomer")
    public ResponseEntity<List<Transaction>> getAllTransactionsByNameCustomer(@RequestParam(value = "nameCustomer") String nameCustomer){
        return new ResponseEntity<List<Transaction>>(transactionRepository.findByAccount_NameCustomer(nameCustomer), HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @GetMapping("/transactions/filterByCreateDateRange")
    public ResponseEntity<List<Transaction>> getAllTransactionsByCreateDateRange(@RequestParam(value = "from") LocalDate from, @RequestParam(value = "to") LocalDate to){
        return new ResponseEntity<>(transactionRepository.findByCreateDateRange(from,to),HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/accounts/{id}/transactions")
    public ResponseEntity<Transaction> registerTransaction(@PathVariable(name = "id") Long AccountId, @RequestBody Transaction transaction){
        Account account=accountRepository.findById(AccountId)
                .orElseThrow(()->new ResourceNotFoundException("No se encuentra cuenta con el id ingresado"));
        return new ResponseEntity<Transaction>(transactionRepository.save(transaction),HttpStatus.CREATED);
    }
}
