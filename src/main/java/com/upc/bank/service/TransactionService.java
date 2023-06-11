package com.upc.bank.service;

import com.upc.bank.model.Transaction;

public interface TransactionService {
    public abstract Transaction createTransaction(Transaction transaction);
}
