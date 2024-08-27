package com.example.onlinebanking.repository;

import com.example.onlinebanking.model.Transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountFrom_Id(Long accountId);
    List<Transaction> findByAccountTo_Id(Long accountId);
}
