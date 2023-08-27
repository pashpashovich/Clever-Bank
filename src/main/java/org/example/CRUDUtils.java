package org.example;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CRUDUtils {
    private static final String insertAccount = "INSERT INTO \"public\".\"Accounts\" (accountnumber,balance,account_id,bank_id) VALUES (?,?,?,?)";
    private static final String insertCustomer = "INSERT INTO \"public\".\"Customers\" (customer_id,fio,has_access) VALUES (?,?,?)";
    private static final String insertTransaction = "INSERT INTO \"public\".\"Transactions\" (transaction_id,type_tr,fromaccount_id,toaccount_id,amount,date,time) VALUES (?,?,?,?,?,?,?)";

    private static final String insertUser = "INSERT INTO \"public\".\"Users\" (login, password,salt) VALUES (?, ?, ?)";
    private static final String toGetAllUsers="SELECT * FROM \"public\".\"Users\"";
    private static final String toGetAllCustomers="SELECT * FROM \"public\".\"Customers\"";

    private static final String toGetAllAccounts="SELECT * FROM \"public\".\"Accounts\"";

    private static final String toGetAllAccountsofBank="SELECT * FROM \"public\".\"Accounts\" WHERE bank_id=?";
    private static final String toGetUser="SELECT * FROM \"public\".\"Users\" WHERE login=?";
    private static final String toGetUserByID="SELECT * FROM \"public\".\"Users\" WHERE user_id=?";
    private static final String toGetAccountByNum="SELECT * FROM \"public\".\"Accounts\" WHERE accountnumber=?";


    private static final String isThereAdmin="SELECT * FROM \"public\".\"Admins\" WHERE admin_id=?";
    private static final String isThereCustomer="SELECT * FROM \"public\".\"Customers\" WHERE customer_id=?";
    private static final String toUpdateCustomer="UPDATE \"public\".\"Customers\" SET has_access=true WHERE customer_id=?";

    private static final String toUpdateAccount="UPDATE \"public\".\"Accounts\" SET balance=? WHERE accountnumber=?";


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

    public static Account getAccount(int accountNum) {
        Account account=null;
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetAccountByNum)) {
            preparedStatement.setInt(1,accountNum);
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                double balance= rs.getDouble("balance");
                int bank_id=rs.getInt("bank_id");
                account=new Account(accountNum,balance,BankName.values()[bank_id-1],getTransactionsOfAccount(accountNum));
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return account;
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

    public static List<Account> getAllAccounts() {
        List<Account> accounts=new ArrayList<>();
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toGetAllAccounts)) {
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                int account_number=rs.getInt("accountnumber");
                double balance=rs.getDouble("balance");
                int account_id=rs.getInt("account_id");
                int bank_id=rs.getInt("bank_id");
                List<Transaction> transactionsofAccount=getTransactionsOfAccount(account_number);
                accounts.add(new Account(account_number,balance,BankName.values()[bank_id-1],transactionsofAccount));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
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
                boolean hasAccess=rs.getBoolean("has_access");
                customer=new Customer(user.getUser_id(),user.getLogin(),user.getPassword(),fio,getAccountsOfCustomer(user.getUser_id()),hasAccess);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return customer;
    }

    public static void updateCustomer(Customer customer) {
        List <Transaction> transactions=new ArrayList<>();
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toUpdateCustomer)) {
            preparedStatement.setInt(1,customer.getUser_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

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

    public static void saveAccount(Account account, int customer_id) {
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(insertAccount)) {
            preparedStatement.setInt(1,account.getAccountNumber());
            preparedStatement.setDouble(2,account.getBalance());
            preparedStatement.setInt(3,customer_id);
            preparedStatement.setInt(4,account.getBank_name().ordinal()+1);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

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

//

   /*

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

