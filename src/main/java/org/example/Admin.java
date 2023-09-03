package org.example;

import java.util.List;

/**
 * The class Admin that extends class User and realizes the Singleton pattern. Also,except User's fields, has a static field users
 */
public class Admin extends User{

    /** the field instance with the type Admin for realization Singleton */
    private static Admin instance = null;
    /** the field users for storing all the users of the app */
    private static List<User> users;

    /**
     * This static method creates a new object of the class Admin if it wasn't created yet
     * @param user the object of the class User
     * @param users the list of all users of the app
     * @return returns the static field instance
     */
    public static Admin getInstance(User user,List<User> users) {
        if (instance == null) {
            instance = new Admin(user.getUser_id(),user.getLogin(),user.getPassword(),users);
        }
        return instance;
    }

    /**
     * The constructor of the class for the method getInstance()
     * @param user_id the unique number of the user
     * @param login the unique name of the user
     * @param password the hashed password with salt of the user to get into the app
     * @param users the list of all users of the app
     */
    public Admin(int user_id, String login, String password, List<User> users) {
        super(user_id, login, password);
        Admin.users = users;
    }

    /**
     * This is a static method that checks the uniqueness of the login that user has entered
     * @param login the unique name of the user
     * @return true if the login is unique and false if not
     */
    public static boolean isUniqueLogin(String login) {
        users=CRUDUtils.getAllUsers();
        for (User user: users) {
            if (login.equals(user.getLogin()))
                return false;
        }
        return true;
    }
}
