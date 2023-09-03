package org.example;

import lombok.Data;

/**
 * The class of User of the app with fields user_id,login and password
 */
@Data
public class User {
    /** This is the unique number of the user */
    int user_id;
    /** This is the unique name of the user*/
    String login;
    /** This is the hashed password with salt of the user to get into the app*/
    String password;

    /**
     * This is the constructor with all fields
     * @param user_id the unique number of the user
     * @param login the unique name of the user
     * @param password the hashed password with salt of the user to get into the app
     */
    public User(int user_id, String login, String password) {
        this.user_id = user_id;
        this.login = login;
        this.password = password;
    }
}
