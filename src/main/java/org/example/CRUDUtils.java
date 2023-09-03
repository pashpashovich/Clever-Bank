package org.example;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class of static methods to create, read, delete and update records in the tables of database
 */
public class CRUDUtils {
    /** This is a SQL request to save a record of account */
    private static final String insertAccount = "INSERT INTO \"public\".\"Accounts\" (accountnumber,balance,account_id,bank_id,date_of_open,currency_t) VALUES (?,?,?,?,?,?)";
   /**This is a SQL request to save a record of customer */
    private static final String insertCustomer = "INSERT INTO \"public\".\"Customers\" (customer_id,fio,has_access) VALUES (?,?,?)";
    /**This is a SQL request to save a record of transaction */
    private static final String insertTransaction = "INSERT INTO \"public\".\"Transactions\" (transaction_id,type_tr,fromaccount_id,toaccount_id,amount,date,time) VALUES (?,?,?,?,?,?,?)";
    /**This is a SQL request to save a record of user */
    private static final String insertUser = "INSERT INTO \"public\".\"Users\" (login, password,salt) VALUES (?, ?, ?)";
    /** This is a SQL request to get all users from table "Users"*/
    private static final String toGetAllUsers="SELECT * FROM \"public\".\"Users\"";
    /** This is a SQL request to get all customers from table "Customers" */
    private static final String toGetAllCustomers="SELECT * FROM \"public\".\"Customers\"";
    /** This is a SQL request to get all accounts from table "Accounts"*/
    private static final String toGetAllAccounts="SELECT * FROM \"public\".\"Accounts\"";
    /** This is a SQL request to get a record of user with exact login */
    private static final String toGetUser="SELECT * FROM \"public\".\"Users\" WHERE login=?";
    /** This is a SQL request to get a record of user with exact user_id */
    private static final String toGetUserByID="SELECT * FROM \"public\".\"Users\" WHERE user_id=?";
    /**  This is a SQL request to get a record of account with exact account number */
    private static final String toGetAccountByNum="SELECT * FROM \"public\".\"Accounts\" WHERE accountnumber=?";
    /** This is a SQL request to get a record of admin with exact admin_id */
    private static final String isThereAdmin="SELECT * FROM \"public\".\"Admins\" WHERE admin_id=?";
    /**This is a SQL request to get a record of customer with exact customer_id */
    private static final String isThereCustomer="SELECT * FROM \"public\".\"Customers\" WHERE customer_id=?";
    /** This is a SQL request to update access of the customer with exact id */
    private static final String toUpdateCustomer="UPDATE \"public\".\"Customers\" SET has_access=true WHERE customer_id=?";
    /** This is a SQL request to update balance af account */
    private static final String toUpdateAccount="UPDATE \"public\".\"Accounts\" SET balance=? WHERE accountnumber=?";
    /** This is a SQL request to get all accounts of the exact user*/
    private static final String toGetAccountsOfTheUser="SELECT * FROM \"public\".\"Accounts\" WHERE account_id=?";
    /** This is a SQL request to get all transactions of the exact account */
    private static final String toGetTransactionsOfAccount="SELECT * FROM \"public\".\"Transactions\" WHERE (fromaccount_id=? OR toaccount_id=?)";
    /** This is a SQL request to delete a record of the exact user*/
    private static final String toDeleteUser="DELETE FROM \"public\".\"Users\" WHERE user_id=?";


    /**
     * This is a method that returns the object of the class User from the database with these values of login and password
     * @param login This is a unique name that was entered by user to get into the app
     * @param password This is a not hashed password that was entered by user to get into the app
     * @return the object of the class User
     */
    public static User getUser(String login,String password) {
        User user=null;
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetUser)) {
            preparedStatement.setString(1,login);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                String salt=rs.getString("salt"); // забираем соль из бд, которая использовалась для хеширования пароля данного аккаунта
                String result=PasswordHashing.hashPassword(password,salt); // хешируем пароль, который ввел пользователь
                String hashedPassword=rs.getString("password"); // получаем хешированный пароль, который хранится в бд
                if (!result.equals(hashedPassword)) return null; // если не совпадают
                else { // если совпадают
                    int user_id=rs.getInt("user_id"); // получаем user_id
                    user = new User(user_id,login,password);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    /**
     * This is a method that returns the object of the class User from the database with this value of user_id
     * @param user_id the unique number of user in database
     * @return returns the object of the class User
     */
    public static User getUser(int user_id) {
        User user=null;
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetUserByID)) {
            preparedStatement.setInt(1,user_id);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                String login=rs.getString("login");
                String hashedPassword=rs.getString("password");
                user=new User(user_id,login,hashedPassword);
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return user;
    }

    /**
     * This is a method that returns the object of the class Account from the database table with this value of accountNum
     * @param accountNum unique number of account
     * @return returns the object of the class Account
     */
    public static Account getAccount(int accountNum) {
        Account account=null;
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetAccountByNum)) {
            preparedStatement.setInt(1,accountNum);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                double balance= rs.getDouble("balance");
                int bank_id=rs.getInt("bank_id");
                Date date=rs.getDate("date_of_open");
                String type_cur=rs.getString("currency_t");
                Currency type_currency=Currency.valueOf(type_cur);
                account= new Account(accountNum,balance,BankName.values()[bank_id-1],date,type_currency,getTransactionsOfAccount(accountNum));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return account;
    }

    /**
     * This method takes all the users from database
     * @return - the list of all users of the app
     */
    public static List<User> getAllUsers() {
        List<User> users=new ArrayList<>();
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetAllUsers)) {
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                int user_id=rs.getInt("user_id");
                String login=rs.getString("login");
                String hashedPassword=rs.getString("password");
                users.add(new User(user_id,login,hashedPassword));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

    /**
     * This method gets all the customers from table "Customers"
     * @return returns the array list of Customers
     */

    public static List<Customer> getAllCustomers() {
        List<Customer> customers=new ArrayList<>();
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetAllCustomers)) {
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                int customer_id=rs.getInt("customer_id");
                String fio=rs.getString("fio");
                boolean hasAccess=rs.getBoolean("has_access");
                User user=getUser(customer_id);
                customers.add(new Customer(customer_id,user.getLogin(),user.getPassword(),fio,getAccountsOfCustomer(customer_id),hasAccess));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customers;
    }

    /**
     * This method takes all the accounts from the database
     * @return - the list of all accounts of the app
     */
    public static List<Account> getAllAccounts() {
        List<Account> accounts=new ArrayList<>();
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetAllAccounts)) {
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                int account_number=rs.getInt("accountnumber");
                double balance=rs.getDouble("balance");
                int bank_id=rs.getInt("bank_id");
                List<Transaction> transactionsofAccount=getTransactionsOfAccount(account_number);
                Date date=rs.getDate("date_of_open");
                String type_cur=rs.getString("currency_t");
                Currency type_currency=Currency.valueOf(type_cur);
                accounts.add(new Account(account_number,balance,BankName.values()[bank_id-1],date,type_currency,transactionsofAccount));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    /**
     * This method checks whether user is admin
     * @param user the object of class User
     * @return returns the object of class Admin
     */
    public static Admin getAdmin(User user) {
        Admin admin=null;
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(isThereAdmin)) {
            preparedStatement.setInt(1,user.getUser_id());
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                admin=new Admin(user.getUser_id(),user.getLogin(),user.getPassword(),getAllUsers());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return admin;
    }

    /**
     * This method takes the object of the class Customer of the particular user
     * @param user - the exact user
     * @return - the object of the class Customer
     */
    public static Customer getCustomer(User user) {
        Customer customer=null;
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(isThereCustomer)) {
            preparedStatement.setInt(1,user.getUser_id());
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                String fio=rs.getString("fio");
                boolean hasAccess=rs.getBoolean("has_access");
                customer=new Customer(user.getUser_id(),user.getLogin(),user.getPassword(),fio,getAccountsOfCustomer(user.getUser_id()),hasAccess);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customer;
    }

    /**
     * This method updates customer's access with exact user_id
     * @param customer - the object which should be updated
     */
    public static void updateCustomer(Customer customer) {
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toUpdateCustomer)) {
            preparedStatement.setInt(1,customer.getUser_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method updates the account's balance
     * @param account - the object of the class balance
     */
    public static void updateAccount(Account account) {
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toUpdateAccount)) {
            preparedStatement.setDouble(1,account.getBalance());
            preparedStatement.setInt(2,account.getAccountNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * This method gets all the accounts of particular customer
     * @param account_id - the unique number of account
     * @return returns the list of accounts
     */
    public static List<Account> getAccountsOfCustomer(int account_id) {
        List<Account> accounts=new ArrayList<>();
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetAccountsOfTheUser)) {
            preparedStatement.setInt(1,account_id);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                int numberAccount=rs.getInt("accountnumber");
                double balance=rs.getDouble("balance");
                int bank_id=rs.getInt("bank_id");
                Date date=rs.getDate("date_of_open");
                String type_cur=rs.getString("currency_t");
                Currency type_currency=Currency.valueOf(type_cur);
                accounts.add(new Account(numberAccount,balance,BankName.values()[bank_id-1],date,type_currency,getTransactionsOfAccount(account_id)));


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }


    /**
     * This method gets all the transactions of the account
     * @param numberAccount - the unique number of the account
     * @return returns list of transactions
     */
    public static List<Transaction> getTransactionsOfAccount(int numberAccount) {
        List<Transaction> transactions=new ArrayList<>();
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetTransactionsOfAccount)) {
            preparedStatement.setInt(1,numberAccount);
            preparedStatement.setInt(2,numberAccount);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                int transaction_id=rs.getInt("transaction_id");
                int fromAccount_id=rs.getInt("fromaccount_id");
                int toAccount_id=rs.getInt("toaccount_id");
                String type_trs=rs.getString("type_tr");
                TransactionType type_tr=TransactionType.valueOf(type_trs);
                double amount=rs.getDouble("amount");
                Date date=rs.getDate("date");
                Time time=rs.getTime("time");
                transactions.add(new Transaction(transaction_id,fromAccount_id,toAccount_id,amount,type_tr,date,time));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return transactions;
    }

    /**
     * This method saves the object of the class User to the database
     * @param login - unique name of the user
     * @param password - hashed password
     * @param salt - "salt" for hashing
     */
    public static void saveUser(String login, String password,String salt) {
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(insertUser)) {
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,salt);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method saves the object of class Customer to the database table "Customers"
     * @param user the object of class User
     * @param fio surname and name of customer
     */
    public static void saveCustomer(User user,String fio) {
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(insertCustomer)) {
            preparedStatement.setInt(1,user.getUser_id());
            preparedStatement.setString(2,fio);
            preparedStatement.setBoolean(3,false);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method saves the object of class Account to the database table "Accounts"
     * @param account - the object of class Account
     * @param customer_id - the unique number of customer
     */
    public static void saveAccount(Account account, int customer_id) {
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(insertAccount)) {
            preparedStatement.setInt(1,account.getAccountNumber());
            preparedStatement.setDouble(2,account.getBalance());
            preparedStatement.setInt(3,customer_id);
            preparedStatement.setInt(4,account.getBank_name().ordinal()+1);
            preparedStatement.setDate(5,account.getDateOfCreating());
            preparedStatement.setObject(6, account.getCurrency(),Types.OTHER);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method saves the object of class Transaction to the database table "Transactions"
     * @param transaction - the object of class Transaction
     */
    public static void saveTransaction(Transaction transaction) {
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(insertTransaction)) {
            preparedStatement.setInt(1,transaction.getId());
            preparedStatement.setObject(2, transaction.getType(),Types.OTHER);
            preparedStatement.setInt(3,transaction.getSourceAccount_id());
            preparedStatement.setInt(4,transaction.getDestinationAccount_id());
            preparedStatement.setDouble(5,transaction.getAmount());
            preparedStatement.setDate(6,transaction.getDate());
            preparedStatement.setTime(7,transaction.getTime());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This method deletes the object of the user with exact user_id
     * @param user_id - the unique number of user
     */
    public static void deleteUser(int user_id) {
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toDeleteUser)) {
            preparedStatement.setInt(1,user_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}

