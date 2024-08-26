package com.example.service;


import com.example.domain.Transaction;
import com.example.util.TransactionLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
@Component
public class TransactionScheduler {
    
    private static final String CSV_FILE = "transactions.csv";
    private static final Iterator<Transaction> TRANSACTIONS = TransactionLoader.loadTransactions(CSV_FILE).iterator();

    private final TransactionService transactionService;

    @Scheduled(fixedRateString = "${transaction-producer.fixed-rate}")
    public void scheduleFixedRateTask() {
        if (TRANSACTIONS.hasNext()) {
            Transaction transaction = TRANSACTIONS.next();
            transactionService.sendTransactionEvent(transaction);
            log.info("Transaction sent: {}", transaction);
        }
    }
}
