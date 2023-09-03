package org.example;

import lombok.Data;
import java.sql.Date;
import java.util.List;
import java.math.BigDecimal;

/**
 * This is a class account that represents the account of the exact customer
 */
@Data
public class Account {
    /** Number of the account*/
    private int accountNumber;
    /** The balance of the account*/
    private BigDecimal balance;
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
    public Account(int accountNumber, BigDecimal balance, BankName bank_name, Date dateOfCreating, Currency currency, List<Transaction> transactions) {
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
        return String.format("%-15d %-10f %-8s %-15s %-10s%n",accountNumber,balance,currency,bank_name,dateOfCreating);
    }

    /**
     * The method of the deposit
     * @param amount - to add to the existing balance
     */
    public void deposit(BigDecimal amount) {
        amount=translation(amount,currency);
        balance=balance.add(amount);
    }

    /**
     * The method of the withdrawal
     * @param amount - to take from the existing balance
     */
    public void withdraw(BigDecimal amount) {
        amount=translation(amount,currency);
        balance=balance.subtract(amount);
    }

    /**
     * This method translates from not BYN currency to BYN currency
     * @param amount - the sum transaction
     * @param currency - name of currency as enum
     * @return returns the amount in BYN
     */
    public BigDecimal translation (BigDecimal amount, Currency currency) {
        BigDecimal rus=new BigDecimal("38.02");
        BigDecimal usd=new BigDecimal("0.39");
        BigDecimal euro=new BigDecimal("0.37");
        BigDecimal yuan=new BigDecimal("2.82");
        switch (currency) {
            case RUB -> amount=amount.multiply(rus);
            case USD -> amount=amount.multiply(usd);
            case XEU -> amount=amount.multiply(euro);
            case CNY ->  amount=amount.multiply(yuan);
        }
        return amount;
    }

}
