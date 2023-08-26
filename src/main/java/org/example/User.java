package org.example;

import lombok.Data;

@Data
public class User {

    int user_id;
    String login;
    String password;

    public User(int user_id, String login, String password) {
        this.user_id = user_id;
        this.login = login;
        this.password = password;
    }
}
