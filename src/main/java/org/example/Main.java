package org.example;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        CRUDUtils.getTransactionsData("SELECT * FROM \"public\".\"TransactionBank\"");
    }
}