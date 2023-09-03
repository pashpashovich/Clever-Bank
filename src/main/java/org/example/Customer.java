package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * The class that represents the customer of the bank
 */
@Getter
@Setter
public class Customer extends User {
    /** Surname, name, patronymic surname of the customer*/
    private String FIO;
    /** List of accounts of the customer*/
    private List<Account> accounts;
    /** Has access to the app or not*/
    private boolean hasAccess;

    /**
     * The constructor of the class Customer
     * @param user_id - the unique number of the user
     * @param login - the unique name of the user
     * @param password - the hashed password with salt of the user to get into the app
     * @param FIO - Surname, name, patronymic surname of the customer
     * @param accounts - List of accounts of the customer
     * @param hasAccess - Has access to the app or not
     */
    public Customer(int user_id, String login, String password, String FIO, List<Account> accounts, boolean hasAccess) {
        super(user_id, login, password);
        this.FIO = FIO;
        this.accounts = accounts;
        this.hasAccess = hasAccess;
    }
}
