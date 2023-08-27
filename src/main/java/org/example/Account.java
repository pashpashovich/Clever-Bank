package org.example;

import lombok.Data;

import java.util.List;

@Data
public class Account {
    private int accountNumber;
    private double balance;
    private BankName bank_name;
    private List<Transaction> transactions;

    public Account(int accountNumber, double balance, BankName bank_name, List<Transaction> transactions) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.bank_name = bank_name;
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return accountNumber + "          " + balance +  "              "+ bank_name;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

//    public void transfer(Account recipient, double amount) {
//        synchronized (this) {
//            synchronized (recipient) {
//                if (balance >= amount) {
//                    withdraw(amount);
//                    recipient.deposit(amount);
//                }
//            }
//        }
//    }
}
