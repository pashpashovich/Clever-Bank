package org.example;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YML {

    public static void writeBank() {
        Map<String, Double> bankMap = new HashMap<>();
        List<Account> accounts = CRUDUtils.getAllAccounts();
        for (Account account : accounts) {
            String s = Integer.toString(account.getAccountNumber());
            bankMap.put(s, account.getBalance()*0.01);
        }
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // Настройки форматирования
        Yaml yaml = new Yaml(options);
        try (FileWriter writer = new FileWriter("D:/Java/cleverBank/src/main/java/org/example/bank.yml")) {
            yaml.dump(bankMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateAccounts() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream("D:/Java/cleverBank/src/main/java/org/example/bank.yml")) {
            Map<String, Double> data = yaml.load(inputStream);
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                int key = Integer.parseInt(entry.getKey());
                Double toAddValue = entry.getValue();
                Account account=CRUDUtils.getAccount(key);
                double newBalance=toAddValue+account.getBalance();
                account.setBalance(newBalance);
                CRUDUtils.updateAccount(account);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}
