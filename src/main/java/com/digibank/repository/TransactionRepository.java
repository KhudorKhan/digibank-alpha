package com.digibank.repository;

import com.digibank.model.Transaction;
import com.digibank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByStatus(Transaction.TransactionStatus status);
}


