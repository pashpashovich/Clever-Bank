package org.example;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Data;

import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;

/**
 * This class makes the account statement in formats of TXT and PDF
 */
@Data
public class AccountStatement {
    /** The object of the class Customer*/
    private Customer customer;
    /** The object of the class Account*/
    private Account account;

    /**
     *  The constructor of the class
     * @param customer - the object of the class Customer
     * @param account - the object of the class Account
     */
    public AccountStatement(Customer customer, Account account) {
        this.customer = customer;
        this.account = account;
    }

    /**
     *  The method of saving account statement
     * @param ch1 - choice of the user to save in PDF or TXt
     * @param ch2 - choice of the user of the period of the statement (month,year,all period)
     */

    public void saveAccountStatement(int ch1,int ch2) {
        String fileName = "AccountStamentOf" + account.getAccountNumber() + System.currentTimeMillis()+".txt"; // генерируем уникальное имя файла
        String inputTxt="/D:/Java/cleverBank/src/statements/" + fileName;
            Date currentDate = new Date(System.currentTimeMillis());
            Time currentTime = new Time(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            Date fromDate = null;
            switch (ch2) {
                case 1 -> {
                    calendar.add(Calendar.MONTH, -1);
                    fromDate = new Date(calendar.getTimeInMillis());
                }
                case 2 -> {
                    calendar.add(Calendar.YEAR, -1);
                    fromDate = new Date(calendar.getTimeInMillis());
                }
                case 3 -> fromDate = currentDate;
            }
            File file = new File(inputTxt);
            try (FileWriter writer = new FileWriter(file)) {
                String str1 = String.format("%70s%n","Выписка");
                writer.write(str1);
                String str2 = String.format("%70s%n","Clever-Bank");
                writer.write(str2);
                String str3 = String.format("%-30s | %-30s%n","Клиент",customer.getFIO());
                writer.write(str3);
                String str4 = String.format("%-30s | %-30d%n","Счёт",account.getAccountNumber());
                writer.write(str4);
                String str5 = String.format("%-30s | %-30s%n","Валюта",account.getCurrency());
                writer.write(str5);
                String str6 = String.format("%-30s | %-30s%n","Дата открытия",account.getDateOfCreating());
                writer.write(str6);
                String str7 = String.format("%-30s | %-10s - %-13s%n","Период",account.getDateOfCreating(),currentDate);
                writer.write(str7);
                String str8 = String.format("%-30s | %-10s, %s%n","Дата и время формирования",currentDate,currentTime);
                writer.write(str8);
                String str9 = String.format("%-30s | %f %s%n","Остаток ",account.getBalance(),account.getCurrency());
                writer.write(str9);
                String str10 = String.format("%5s       | %15s                 | %8s%n","Дата ","Примечание","Сумма");
                writer.write(str10);
                writer.write("------------------------------------------------------------------------------------------------------\n");
                List<Transaction> transactionsOfPeriod = CRUDUtils.getTransactionsOfAccount(account.getAccountNumber());
                for (Transaction transaction : transactionsOfPeriod) {
                    if (transaction.getDate().compareTo(fromDate) >= 0) {
                        String str11 = String.format("%-11s | %-30s | %-15s%n",transaction.getDate() ,transaction.getType(),account.getCurrency());
                        writer.write(str11);
                    }
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (ch1==1) {
                try {
                    String outputPdfFile = "D:/Java/cleverBank/src/statements/Statements.pdf";
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(outputPdfFile));
                    document.open();
                    BaseFont baseFont = BaseFont.createFont("D:/Java/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    Font font = new Font(baseFont);
                    BufferedReader br = new BufferedReader(new FileReader(inputTxt));
                    String line;
                    while ((line = br.readLine()) != null) {
                        Paragraph paragraph = new Paragraph(line,font);
                        document.add(paragraph);
                    }
                    br.close();
                    document.close();
                   file.delete(); // удаляем текстовый файл
                    System.out.println("PDF файл успешно создан.");
                } catch (DocumentException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
