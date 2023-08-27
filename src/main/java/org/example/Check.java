package org.example;

import lombok.Data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;

@Data
public class Check {
    private long check_id;
    private Date date;
    private Time time;
    private String tr_type;
    private String FromBankName;
    private String ToBankName;
    private int fromAccountNum;
    private int toAccountNum;
    private double sum;

    public Check(long check_id, Date date, Time time, String tr_type, String fromBankName, String toBankName, int fromAccountNum, int toAccountNum, double sum) {
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

    public void saveCheckToFile() {
        String fileName = "check_" + System.currentTimeMillis() + ".txt"; // генерируем уникальное имя файла
        File file = new File("/D:/Java/cleverBank/src/check/" + fileName);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("----------------------------------\n");
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
