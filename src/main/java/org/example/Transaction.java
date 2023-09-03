package org.example;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

/**
 * This class represents transaction
 */
@Data
public class Transaction {

    /** unique number of transaction*/
    private int id;
    /** the id of account from which transaction is made*/
    private int sourceAccount_id;
    /** the id of account to which transaction is made*/
    private int destinationAccount_id;
    /** the amount of transaction in BYN*/
    private BigDecimal amount;
    /** the type of transaction as enum*/
    private TransactionType type;
    /** the date of transaction*/
    private Date date;
    /** time of transaction*/
    private Time time;

    /**
     * The constructor of the class
     * @param id - unique number of transaction
     * @param sourceAccount_id - the id of account from which transaction is made
     * @param destinationAccount_id - the id of account to which transaction is made
     * @param amount - the amount of transaction
     * @param type - the type of transaction as enum
     * @param date - the date of transaction
     * @param time - time of transaction
     */
    public Transaction(int id, int sourceAccount_id, int destinationAccount_id, BigDecimal amount, TransactionType type, Date date, Time time) {
        this.id = id;
        this.sourceAccount_id = sourceAccount_id;
        this.destinationAccount_id = destinationAccount_id;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.time = time;
    }

    /**
     * This method generates the unique id of transaction
     * @param date - current date
     * @param time - current time
     * @return returns generated id
     */
    public static int generateId(Date date,Time time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(time);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds=calendar.get(Calendar.SECOND);
        return (year * 100000000 + month * 1000000 + day * 10000 + hours * 100 + minutes*10+seconds);
    }
}
