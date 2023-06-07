package com.upc.bank.service.Impl;

import com.upc.bank.model.Account;
import com.upc.bank.repository.AccountRepository;
import com.upc.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public Account createAccount(Account account){
        return accountRepository.save(account);
    }
}
