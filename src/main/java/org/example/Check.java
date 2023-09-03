package org.example;

import lombok.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

/**
 * The class of creating the check of the transaction
 */
@Data
public class Check {
    /** the unique number of the check*/
    private long check_id;
    /** the date of creating the check*/
    private Date date;
    /** time of creating the check*/
    private Time time;
    /** the type of transaction as enum*/
    private String tr_type;
    /** the name of the bank from which transaction was made*/
    private String FromBankName;
    /**  the name of the bank to which transaction was made*/
    private String ToBankName;
    /** the number of the account from which transaction was made*/
    private int fromAccountNum;
    /**  the number of the account to which transaction was made*/
    private int toAccountNum;
    /** the sum of the transaction*/
    private BigDecimal sum;

    /**
     *
     * @param check_id - the unique number of the check
     * @param date - the date of creating the check
     * @param time - time of creating the check
     * @param tr_type - the type of transaction as enum
     * @param fromBankName - the name of the bank from which transaction was made
     * @param toBankName - the name of the bank to which transaction was made
     * @param fromAccountNum - the number of the account from which transaction was made
     * @param toAccountNum - the number of the account to which transaction was made
     * @param sum - the sum of the transaction
     */
    public Check(long check_id, Date date, Time time, String tr_type, String fromBankName, String toBankName, int fromAccountNum, int toAccountNum, BigDecimal sum) {
        this.check_id = check_id;
        this.date = date;
        this.time = time;
        this.tr_type = tr_type;
        FromBankName = fromBankName;
        ToBankName = toBankName;
        this.fromAccountNum = fromAccountNum;
        this.toAccountNum = toAccountNum;
        this.sum = sum;
    }

    /** The method of creating the check*/
    public void saveCheckToFile() {
        String fileName = "check_" + System.currentTimeMillis() + ".txt"; // генерируем уникальное имя файла
        File file = new File("/D:/Java/cleverBank/src/check/" + fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("\tБанковский чек\n");
            writer.write("Чек: "+check_id+"\n");
            writer.write(date+"\t"+time+"\n");
            writer.write("Тип транзакции: "+tr_type+"\n");
            writer.write("Банк отправителя: "+FromBankName+"\n");
            writer.write("Банк получателя: "+ToBankName+"\n");
            writer.write("Счёт отправителя: "+fromAccountNum+"\n");
            writer.write("Счёт получателя: "+toAccountNum+"\n");
            writer.write("Сумма: "+sum+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
