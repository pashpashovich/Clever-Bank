package org.example;

import lombok.Data;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

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

    public static int generateId(Date date,Time time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(time);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        return (year * 100000000 + month * 1000000 + day * 10000 + hours * 100 + minutes);
    }
}
