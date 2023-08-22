package org.example;

public class Account {
    private String accountNumber;
    private User user;
    private double balance;

    public Account(String accountNumber, User user, double initialBalance) {
        this.accountNumber = accountNumber;
        this.user = user;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public User getUser() {
        return user;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public void transfer(Account recipient, double amount) {
        synchronized (this) {
            synchronized (recipient) {
                if (balance >= amount) {
                    withdraw(amount);
                    recipient.deposit(amount);
                }
            }
        }
    }
}
