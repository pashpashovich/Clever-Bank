package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class Customer extends User {
    private String FIO;
    private List<Account> accounts;
    private boolean hasAccess;

    @Override
    public String toString() {
        return "Customer{" +
                "FIO='" + FIO + '\'' +
                ", accounts=" + accounts +
                ", hasAccess=" + hasAccess +
                ", user_id=" + user_id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Customer(int user_id, String login, String password, String FIO, List<Account> accounts, boolean hasAccess) {
        super(user_id, login, password);
        this.FIO = FIO;
        this.accounts = accounts;
        this.hasAccess = hasAccess;
    }
}
