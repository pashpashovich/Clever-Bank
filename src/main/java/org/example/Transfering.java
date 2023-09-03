package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class represents transfer of money
 */
public class Transfering {
    private final Lock transferLock = new ReentrantLock();

    /**
     *  This method withdraws
     * @param fromAccount - the object of class Account
     * @param amount - amount of withdraw
     */
    public void transferFundsFrom(Account fromAccount, double amount) {
        try {
            transferLock.lock();
            fromAccount.withdraw(amount);
        } finally {
            transferLock.unlock();
        }
    }

    /**
     * This method deposits
     * @param toAccount - the object of class Account
     * @param amount - amount of deposit
     */
    public void transferFundsTo(Account toAccount, double amount) {
        try {
            transferLock.lock();
            toAccount.deposit(amount);
        } finally {
            transferLock.unlock();
        }
    }
}
