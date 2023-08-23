package org.example;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private String name;
    private List<Account> accounts;
     private List<Transaction> transactions;

    public Bank(String name, List<Account> accounts, List<Transaction> transactions) {
        this.name = name;
        this.accounts = accounts;
        this.transactions = transactions;
    }

    public String getName() {
        return name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
