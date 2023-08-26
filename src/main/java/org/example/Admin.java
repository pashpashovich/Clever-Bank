package org.example;

import java.util.List;

public class Admin extends User{

    private List<User> users;

    public Admin(int user_id, String login, String password, List<User> users) {
        super(user_id, login, password);
        this.users = users;
    }
}
