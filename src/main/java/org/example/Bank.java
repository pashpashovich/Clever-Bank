package org.example;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * This class represents the banks in which customers are served
 */
@Data
@RequiredArgsConstructor
public class Bank {
    /** The name of the bank as ENUM*/
    private BankName name;
    /** The list of the accounts of the bank*/
    private List<Account> accounts;
    /** The list of all the transactions of accounts of this bank*/
    private List<Transaction> transactions;
}
