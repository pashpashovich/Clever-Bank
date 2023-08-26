package org.example;

import lombok.Getter;

import java.util.List;

@Getter
public class Customer extends User {
    private String FIO;
    private List<Account> accounts;
    private boolean hasAccess;

    public Customer(int user_id, String login, String password, String FIO, List<Account> accounts, boolean hasAccess) {
        super(user_id, login, password);
        this.FIO = FIO;
        this.accounts = accounts;
        this.hasAccess = hasAccess;
    }
}
