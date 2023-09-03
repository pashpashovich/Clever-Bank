package org.example;

import lombok.Data;
import java.sql.Date;
import java.util.List;

/**
 * This is a class account that represents the account of the exact customer
 */
@Data
public class Account {
    /** Number of the account*/
    private int accountNumber;
    /** The balance of the account*/
    private double balance;
    /** The name of the bank*/
    private BankName bank_name;
    /** The date of creating the account*/
    private Date dateOfCreating;
    /** The type of the currency*/
    private Currency currency;
    /** All the transactions of this account */
    private List<Transaction> transactions;


    /**
     * This is a constructor of the class Account
     * @param accountNumber - number of the account
     * @param balance - the balance of the account
     * @param bank_name - the name of the bank
     * @param dateOfCreating - the date of creating the account
     * @param currency - the type of the currency
     * @param transactions - all the transactions of this account
     */
    public Account(int accountNumber, double balance, BankName bank_name, Date dateOfCreating, Currency currency, List<Transaction> transactions) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.bank_name = bank_name;
        this.dateOfCreating = dateOfCreating;
        this.currency = currency;
        this.transactions = transactions;
    }

    /**
     * This method gives the description of the object Account
     * @return returns the string that includes the description of the object
     */
    @Override
    public String toString() {
        return accountNumber + "          " + balance +  "              "+ bank_name;
    }

    /**
     * The method of the deposit
     * @param amount - to add to the existing balance
     */
    public void deposit(double amount) {
        balance += amount;
    }

    /**
     * The method of the withdrawal
     * @param amount - to take from the existing balance
     */
    public void withdraw(double amount) {
        balance -= amount;
    }

}
