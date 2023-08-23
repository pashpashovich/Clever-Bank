package org.example;

import java.sql.*;

public class CRUDUtils {
    private static String toSaveTransaction="INSERT INTO CleverBank(cleverbank_id,numFrom,numTo,sum) VALUES(?,?,?,?);";
    private static String toUpdateTransaction="UPDATE CleverBank SET sum=? WHERE cleverbank_id=?";
    private static String toDeleteTransaction="DELETE FROM CleverBank WHERE id=?";


    public static void getTransactionsData(String query) {
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(query)) {
            ResultSet rs=preparedStatement.executeQuery();
            while (rs.next()) {
                int id=rs.getInt("transaction_id");
                int numFrom=rs.getInt("fromAccount_id");
                int numTo=rs.getInt("toAccount_id");
                double sum =rs.getDouble("amount");
                Date data=rs.getDate("data");
                System.out.println(id+" "+numFrom+" "+numTo+" "+sum+" "+data);
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
    }

    /*public static List<Transaction> saveTransaction(Transaction transaction) {
        List <Transaction> transactions=new ArrayList<>();
        try(Connection connection=DBUtils.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(toSaveTransaction)) {
            preparedStatement.setInt(1,transaction.getId());
            preparedStatement.setString(2,transaction.getSourceAccount().getAccountNumber());
            preparedStatement.setString(3,transaction.getDestinationAccount().getAccountNumber());
            preparedStatement.setDouble(4,transaction.getAmount());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    public static List<Transaction> updateTransaction(int id, double sum) {
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

