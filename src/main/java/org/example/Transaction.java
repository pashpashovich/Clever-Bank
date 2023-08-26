package org.example;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
public class Transaction {

    private int id;
    private int sourceAccount_id;
    private int destinationAccount_id;
    private double amount;
    private TransactionType type;
    private Date date;
    private Time time;

    public Transaction(int id, int sourceAccount_id, int destinationAccount_id, double amount, TransactionType type, Date date, Time time) {
        this.id = id;
        this.sourceAccount_id = sourceAccount_id;
        this.destinationAccount_id = destinationAccount_id;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.time = time;
    }
}
