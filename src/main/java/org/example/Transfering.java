package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Transfering {
    private final Lock transferLock = new ReentrantLock();

    public void transferFundsFrom(Account fromAccount, double amount) {
        try {
            transferLock.lock();
            fromAccount.withdraw(amount);
        } finally {
            transferLock.unlock();
        }
    }
    public void transferFundsTo(Account toAccount, double amount) {
        try {
            transferLock.lock();
            toAccount.deposit(amount);
        } finally {
            transferLock.unlock();
        }
    }
}
