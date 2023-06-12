package com.upc.bank.controller;

import com.upc.bank.exception.ResourceNotFoundException;
import com.upc.bank.exception.ValidationException;
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

@RestController
@RequestMapping("/api/bank/v1")
public class TransactionController {
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    public TransactionController(TransactionRepository transactionRepository, AccountRepository accountRepository){
        this.transactionRepository=transactionRepository;
        this.accountRepository=accountRepository;
    }

    @Transactional(readOnly = true)
    @RequestMapping("/transactions/filterByNameCustomer")
    public ResponseEntity<List<Transaction>> getAllTransactionsByNameCustomer(@RequestParam(value = "nameCustomer") String nameCustomer){
        return new ResponseEntity<List<Transaction>>(
                transactionRepository.findByAccount(accountRepository.findByNameCustomer(nameCustomer)),
                HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @RequestMapping("/transactions/filterByCreateDateRange")
    public ResponseEntity<List<Transaction>> getAllTransactionsByCreateDateRange(@RequestParam(value = "startDate") LocalDate startDate,
                                                                                 @RequestParam(value = "endDate") LocalDate endDate){
        return new ResponseEntity<>(transactionRepository.findByCreateDateBetween(startDate,endDate),HttpStatus.OK);
    }

    @Transactional
    @RequestMapping("/accounts/{id}/transactions")
    public ResponseEntity<Transaction> createTransaction( @PathVariable(value = "id") long accountId,
                                                            @RequestBody Transaction transaction){
        Account account=accountRepository.findById(accountId)
                .orElseThrow(()->new ResourceNotFoundException("No se encuentra cuenta con el id ingresado"));
        validateTransaction(transaction);
        transaction.setCreateDate(LocalDate.now());
        return new ResponseEntity<Transaction>(transactionRepository.save(transaction),HttpStatus.CREATED);
    }

    private void validateTransaction(Transaction transaction){
        if (transaction.getType()==null || transaction.getType().trim().isEmpty()){
            throw new ValidationException("El tipo de transacción bancaria debe ser obligatorio");
        }
        if (transaction.getAmount()<=0 || transaction.getAmount().equals(0)){
            throw new ValidationException("El monto en una transacción bancaria debe ser mayor de S/.0.0");
        }
        if (transaction.getAmount()>transaction.getBalance()){
            throw new ValidationException("En una transacción bancaria tipo retiro el monto no puede ser mayor al saldo");
        }
        if (transaction.getType()=="Deposito"){
            transaction.setBalance(transaction.getBalance()+transaction.getAmount());
        }
        if (transaction.getType()=="Retiro" && transaction.getAmount()<transaction.getBalance()){
            transaction.setBalance(transaction.getBalance()-transaction.getAmount());
        }
    }
}
