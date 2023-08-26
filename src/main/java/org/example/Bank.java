package org.example;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Bank {
    private BankName name;
    private List<Account> accounts;
    private List<Transaction> transactions;

}
