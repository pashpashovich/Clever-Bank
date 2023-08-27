package org.example;

import java.util.List;

public class Admin extends User{

    private static List<User> users;

    public Admin(int user_id, String login, String password, List<User> users) {
        super(user_id, login, password);
        this.users = users;
    }

    public static boolean isUniqueLogin(String login) {
        users=CRUDUtils.getAllUsers();
        for (User user: users) {
            if (login.equals(user.getLogin()))
                return false;
        }
        return true;
    }
}
