package org.example;

import java.time.LocalDateTime;

public class Transaction {
    private Account sourceAccount;
    private Account destinationAccount;
    private double amount;
    private LocalDateTime timestamp;

    public Transaction(Account sourceAccount, Account destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

}
