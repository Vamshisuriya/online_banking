package com.example.onlinebanking.service;

import com.example.onlinebanking.model.Transaction;
import com.example.onlinebanking.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getTransactionsByAccountFrom(Long accountId) {
        return transactionRepository.findByAccountFrom_Id(accountId);
    }

    public List<Transaction> getTransactionsByAccountTo(Long accountId) {
        return transactionRepository.findByAccountTo_Id(accountId);
    }
}
