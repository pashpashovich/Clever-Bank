package org.example;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class of working with file "bank.yml"
 */
public class YML {
    /**
     * This method writes values to yml file
     */
    public static void writeBank() {
        Map<String, BigDecimal> bankMap = new HashMap<>();
        List<Account> accounts = CRUDUtils.getAllAccounts();
        for (Account account : accounts) {
            String s = Integer.toString(account.getAccountNumber());
            BigDecimal multiplier = new BigDecimal("0.01");
            bankMap.put(s, account.getBalance().multiply(multiplier));
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

    /**
     * This method reads data from yml file and updates the records of accounts in database
     */
    public static void updateAccounts() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream("D:/Java/cleverBank/src/main/java/org/example/bank.yml")) {
            Map<String, BigDecimal> data = yaml.load(inputStream);
            for (Map.Entry<String, BigDecimal> entry : data.entrySet()) {
                int key = Integer.parseInt(entry.getKey());
                BigDecimal toAddValue = entry.getValue();
                Account account=CRUDUtils.getAccount(key);
                BigDecimal newBalance=toAddValue.add(account.getBalance());
                account.setBalance(newBalance);
                CRUDUtils.updateAccount(account);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
