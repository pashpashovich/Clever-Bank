package org.example;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDUtils {
    private static final String insertUser = "INSERT INTO \"public\".\"Users\" (user_id, login, password) VALUES (?, ?, ?)";
    private static final String toGetAllUsers="SELECT * FROM \"public\".\"Users\"";
    private static final String toGetAllAccountsofBank="SELECT * FROM \"public\".\"Accounts\" WHERE bank_id=?";
    private static final String toGetUser="SELECT * FROM \"public\".\"Users\" WHERE login=?";
    private static final String isThereAdmin="SELECT * FROM \"public\".\"Admins\" WHERE admin_id=?";
    private static final String isThereCustomer="SELECT * FROM \"public\".\"Customers\" WHERE customer_id=?";


    private static final String toGetAccountsOfTheUser="SELECT * FROM \"public\".\"Accounts\" WHERE account_id=?";
    private static final String toGetTransactionsOfAccount="SELECT * FROM \"public\".\"Transactions\" WHERE (fromaccount_id=? OR toaccount_id=?)";
    private static final String toGetCustomer="SELECT * FROM \"public\".\"Customers\" WHERE customer_id=?";
    private static final String toSaveTransaction="INSERT INTO CleverBank(cleverbank_id,numFrom,numTo,sum) VALUES(?,?,?,?);";
    private static final String toUpdateTransaction="UPDATE CleverBank SET sum=? WHERE cleverbank_id=?";
    private static final String toDeleteTransaction="DELETE FROM CleverBank WHERE id=?";


    public static User getUser(String login,String password) {
        User user=null;
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetUser)) {
            preparedStatement.setString(1,login);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                String salt=rs.getString("salt");
                String result=PasswordHashing.hashPassword(password,salt);
                String hashedPassword=rs.getString("password");
                if (!result.equals(hashedPassword)) return null;
                else {
                    int user_id=rs.getInt("user_id");
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

    public static Customer getCustomer(User user) {
        Customer customer=null;
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(isThereCustomer)) {
            preparedStatement.setInt(1,user.getUser_id());
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                String fio=rs.getString("fio");
                boolean hasAccess=rs.getBoolean("hasAccess");
                customer=new Customer(user.getUser_id(),user.getLogin(),user.getPassword(),fio,getAccountsOfCustomer(user.getUser_id()),hasAccess);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customer;
    }


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
                accounts.add(new Account(numberAccount,balance,BankName.values()[bank_id-1],getTransactionsOfAccount(numberAccount)));


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }


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



//    public static List<Account> getAllAccountsofBank(int bank_id) {
//        List<Account> accounts=new ArrayList<>();
//        try(Connection connection=DBUtils.getConnection();
//            PreparedStatement preparedStatement=connection.prepareStatement(toGetAllAccountsofBank)) {
//            preparedStatement.setInt(1,bank_id);
//            ResultSet rs=preparedStatement.executeQuery();
//            while (rs.next()) {
//                int account_number=rs.getInt("accountNumber");
//                double balance=rs.getDouble("balance");
//                String hashedPassword=rs.getString("password");
//                accounts.add(new Account(account_number,balance,new Bank() ));
//            }
//        } catch (SQLException e) {
//            System.out.println("SQL Exception: " + e.getMessage());
//        }
//        return accounts;
//    }
//
//
//    public static List<Account> getAllAccounts() {
//        List<Account> accounts=new ArrayList<>();
//        try(Connection connection=DBUtils.getConnection();
//            PreparedStatement preparedStatement=connection.prepareStatement(toGetAllAccounts)) {
//            ResultSet rs=preparedStatement.executeQuery();
//            while (rs.next()) {
//                int account_number=rs.getInt("accountNumber");
//                double balance=rs.getDouble("balance");
//                String hashedPassword=rs.getString("password");
//                users.add(new User(user_id,login,hashedPassword));
//            }
//        } catch (SQLException e) {
//            System.out.println("SQL Exception: " + e.getMessage());
//        }
//        return accounts;
//    }
//    public static List<Transaction> getTransactionsData(String query) {
//        List <Transaction> transactions=new ArrayList<>();
//        try(Connection connection=DBUtils.getConnection();
//            PreparedStatement preparedStatement=connection.prepareStatement(query)) {
//            ResultSet rs=preparedStatement.executeQuery();
//            while (rs.next()) {
//                int id = rs.getInt("transaction_id");
//                int numFrom = rs.getInt("fromAccount_id");
//                int numTo = rs.getInt("toAccount_id");
//                double sum = rs.getDouble("amount");
//                Date data = rs.getDate("data");
//                Time time=rs.getTime("data");
//                System.out.println(id + " " + numFrom + " " + numTo + " " + sum + " " + data+" "+time);
//
//            }
//        } catch (SQLException e) {
//            System.out.println("SQL Exception: " + e.getMessage());
//        }
//        return transactions;
//    }

//    public static List<Customer> getCustomersData(String query) {
//        List<Customer> users=new ArrayList<>();
//        try(Connection connection=DBUtils.getConnection();
//            PreparedStatement preparedStatement=connection.prepareStatement(query)) {
//            ResultSet rs=preparedStatement.executeQuery();
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String name=rs.getString("name");
//                users.add(new Customer(id,name));
//            }
//        } catch (SQLException e) {
//            System.out.println("SQL Exception: " + e.getMessage());
//        }
//        return users;
//
//    }

//    public static List<Account> getAccountsData(String query) {
//        List <Account> accounts=new ArrayList<>();
//        List<User> users=getUsersData("SELECT * FROM \"public\".\"User\"");
//        User thisUser=null;
//        try(Connection connection=DBUtils.getConnection();
//            PreparedStatement preparedStatement=connection.prepareStatement(query)) {
//            ResultSet rs=preparedStatement.executeQuery();
//            while (rs.next()) {
//                int account_id = rs.getInt("account_id");
//                int user_id = rs.getInt("user_id");
//                for (User user:users) {
//                    if(user.getId()==user_id) thisUser=user;
//                }
//                double balance=rs.getDouble("balance");
//                accounts.add(new Account(account_id,thisUser,balance));
//            }
//        } catch (SQLException e) {
//            System.out.println("SQL Exception: " + e.getMessage());
//        }
//        return accounts;
//    }

//    public static void saveUser(User user) {
//        try(Connection connection=DBUtils.getConnection();
//            PreparedStatement preparedStatement=connection.prepareStatement(insertUser)) {
//            preparedStatement.setInt(1,user.getUser_id());
//            preparedStatement.setString(2,user.getLogin());
//            preparedStatement.setString(3,user.getPassword());
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

   /* public static List<Transaction> updateTransaction(int id, double sum) {
        List <Transaction> transactions=new ArrayList<>();
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toUpdateTransaction)) {
            preparedStatement.setDouble(1,sum);
            preparedStatement.setInt(2,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    public static void deleteTransaction(int id) {
        List <Transaction> students=new ArrayList<>();
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toDeleteTransaction)) {
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
}

