package com.upc.bank.controller;

import com.upc.bank.exception.ValidationException;
import com.upc.bank.model.Account;
import com.upc.bank.repository.AccountRepository;
import com.upc.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank/v1")
public class AccountController {
    @Autowired
    private AccountService accountService;
    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return new ResponseEntity<List<Account>>(accountRepository.findAll(), HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        validateAccount(account);
        return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.CREATED);
    }

    private void validateAccount(Account account) {
        if (accountRepository.existsByNameCustomerAndNumberAccount(account.getNameCustomer(), account.getNumberAccount())){
            throw new ValidationException("No se puede registrar la cuenta porque ya existe uno con estos datos");
        }
        if (account.getNameCustomer() == null || account.getNameCustomer().trim().isEmpty()) {
            throw new ValidationException("El nombre del cliente debe ser obligatorio");
        }
        if (account.getNameCustomer().length() > 30) {
            throw new ValidationException("El nombre del cliente no debe exceder los 30 caracteres");
        }
        if (account.getNumberAccount() == null || account.getNumberAccount().trim().isEmpty()) {
            throw new ValidationException("El número de cuenta debe ser obligatorio”");
        }
        if (account.getNumberAccount().length() != 13) {
            throw new ValidationException("El número de cuenta debe tener una longitud de 13 caracteres");
        }
    }

    private void existsByTitleAndEditorial(Account account){
        if (accountRepository.existsByNameCustomerAndNumberAccount(account.getNameCustomer(), account.getNumberAccount())){
            throw new ValidationException("No se puede registrar la cuenta porque ya existe uno con estos datos");
        }
    }
}