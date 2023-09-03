package org.example;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * This is the main class which contains the main method and other static methods
 * @author Kosovich Pavel
 */
public class Main {
    /**
     * With this method our app starts
     * @param args standard argument for main method
     * @throws NoSuchAlgorithmException the exception which is thrown if there is no algorithm for hashing password
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1); // это планировщик, который повторяет действия каждые 30 секунд работы приложения
        scheduler.scheduleAtFixedRate(() -> {
            if (isEndOfMonth()) {
                YML.writeBank();
                YML.updateAccounts();
            }
        }, 0, 30, TimeUnit.SECONDS);
        System.out.println("----Добро пожаловать в приложение Clever-Bank----");
        int choice; // переменная для выбора действия
        Scanner scanner;
        while (true) {
            User user;// текущий пользователь приложения
            Admin admin; // текущий администратор приложения
            Customer customer; // текущий клиент приложения
            String startMenu="Выберите действие:\n1.Авторизация\n2.Регистрация\n3.Выход из программы\n";
            choice=chooseAction(startMenu,1,3);
            switch (choice) {
                case 1 -> {
                    while (true) {
                        System.out.print("Введите Ваш логин: ");
                        scanner=new Scanner(System.in);
                        String login;
                        login = scanner.nextLine(); // пользователь вводит свой логин
                        System.out.print("Введите Ваш пароль: ");
                        String password;
                        password = scanner.nextLine(); // и пароль
                        user = CRUDUtils.getUser(login, password); // пытаемся получить пользователя по такому логину и паролю
                        if (user == null) {
                            System.out.println("Неверный логин или пароль...");
                        String act="Выберите действие:\n1.Повторить попытку\n2.Выйти в главное меню\n";
                        choice=chooseAction(act,1,2);
                        if (choice==2) break;
                        }
                        else { // если получили такого пользователя
                            if (CRUDUtils.getAdmin(user)!=null) {
                                admin=Admin.getInstance(user,CRUDUtils.getAllUsers());
                                System.out.println("Вы вошли в приложение как администратор "+admin.getLogin());
                                boolean flag=true;
                                while (flag) {
                                        String adminMenu="Выберите действие:\n1.Рассмотреть заявки на регистрацию\n2.Просмотреть всех пользователей\n3.Удалить пользователя\n4.Выйти из аккаунта\n5.Выйти из приложения\n";
                                        choice=chooseAction(adminMenu,1,5);
                                    switch (choice) {
                                        case 1 -> {
                                            List<Customer> applications;
                                            applications=CRUDUtils.getAllCustomers();
                                            System.out.println("---Неподтверждённые заявки---");
                                            int i=1;
                                            System.out.println("№   Логин          ФIО");
                                            Iterator<Customer> iterator = applications.iterator(); // используем итератор
                                            while (iterator.hasNext()) {
                                                Customer registred_customer = iterator.next();
                                                if (!registred_customer.isHasAccess()) {
                                                    System.out.println(i + ". " + registred_customer.getLogin() + " " + registred_customer.getFIO());
                                                    i++;
                                                } else {
                                                    iterator.remove(); // удаление элемента из коллекции
                                                }
                                            }
                                            if(i==1) System.out.println("Заявок нет");
                                            else {
                                                String adminAppliances = "Выберите заявку для подтверждения: ";
                                                choice = chooseAction(adminAppliances, 1, i);
                                                Customer newCustomer=applications.get(choice - 1);
                                                CRUDUtils.updateCustomer(newCustomer);
                                                System.out.println("Заявка подтверждена!\nДавайте теперь заполним данные этого клиента");
                                                int numberofAccounts;
                                                while (true) {
                                                   try {
                                                       System.out.print("Введите количество счетов для добавления:");
                                                       scanner = new Scanner(System.in);
                                                       numberofAccounts = scanner.nextInt();
                                                       if (numberofAccounts<0) throw new SignException(numberofAccounts,"количество счетов");
                                                       break;
                                                   } catch (InputMismatchException e) {
                                                       System.out.println("Неверный ввод... Повторите попытку");
                                                       scanner.nextLine();
                                                   } catch (SignException e) {
                                                       System.out.println(e);
                                                   }
                                                }
                                                int numberAccount;
                                                BigDecimal balance;
                                                for (int k=0;k<numberofAccounts;k++) {
                                                    while (true)
                                                    {
                                                        try {
                                                            scanner = new Scanner(System.in);
                                                            System.out.print("Введите номер счёта " + (k + 1) + ": ");
                                                            numberAccount = scanner.nextInt();
                                                            if (isNumAccountUnique(numberAccount)) break;
                                                            else System.out.println("Такой номер счёта уже существует в базе данных\nПовторите попытку");
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Неккоректный ввод... Повторите попытку");
                                                        }
                                                }
                                                    while (true) {
                                                        try {
                                                            scanner=new Scanner(System.in);
                                                            System.out.print("Введите баланс счёта (при дробном значении используйте ',' , а не '.'): ");
                                                            double balance_d = scanner.nextDouble();
                                                            balance=new BigDecimal(balance_d);
                                                            break;
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Неккоректный ввод... Повторите попытку");
                                                            scanner.nextLine();
                                                        }
                                                    }
                                                    String banks="Выберите банк, в котором будет обслуживаться счёт\n1.Клевер-банк\n2.Белинвестбанк\n3.БСББанк\n4.БеларусБанк\n5.СтатусБанк\n";
                                                    int bankNum=chooseAction(banks,1,5);
                                                    long currentTimeMillis = System.currentTimeMillis();
                                                    Date currentDate = new Date(currentTimeMillis);
                                                    System.out.println(currentDate);
                                                    String currencies="Выберите валюту, в которой будут храниться средства на счету:\n1.Белорусские рубли\n2.Доллары США\n3.Российские рубли\n4.Евро\n5.Китайские юани\n";
                                                    int choiceofCurrency=chooseAction(currencies,1,5);
                                                    CRUDUtils.saveAccount(new Account(numberAccount,balance,BankName.values()[bankNum-1],currentDate,Currency.values()[choiceofCurrency-1],null),newCustomer.getUser_id());
                                               }
                                            }
                                        }
                                        case 2 -> outCustomers();
                                        case 3 -> {
                                            List<Customer> customers=outCustomers();
                                            String s="Введите номер клиента для удаления(будет удалена их учётная запись, а также все счета и транзакции, связанные с этими счетами): ";
                                            int choice3=chooseAction(s,1,customers.size());
                                            Customer delCustomer=customers.get(choice3-1);
                                            CRUDUtils.deleteUser(delCustomer.getUser_id());
                                            System.out.println("Удаление прошло успешно)");
                                        }
                                        case 4 -> {
                                                flag=false;
                                            System.out.println("Выход из аккаунта администратора "+admin.getLogin()+" выполнен успешно");
                                        }
                                        case 5 -> exit();

                                    }
                                }
                            }
                            else {
                                customer=CRUDUtils.getCustomer(user);
                                if (customer.isHasAccess())
                                {
                                    System.out.println("Вы вошли в приложение как клиент "+customer.getFIO());
                                    boolean flag=true;
                                    while (flag) {
                                            String customerMenu="Выберите действие:\n1.Просмотреть свои счета\n2.Пополнение средств на счет\n3.Снятие средств со счёта \n4.Перевод денежных средств\n5.Создать выписку по транзакциям\n6.Выйти из аккаунта\n7.Выйти из приложения\n";
                                            choice=chooseAction(customerMenu,1,7);
                                        switch (choice) {
                                            case 1 -> getAccountsOfTheUser(customer);
                                            case 2 -> {
                                                List<Account> accounts=getAccountsOfTheUser(customer);
                                                if (!accounts.isEmpty()) {
                                                    BigDecimal amount;
                                                    String chooseAccount = "Выберите порядковый номер счёта, который вы хотите пополнить: ";
                                                    int choice2 = chooseAction(chooseAccount, 1, accounts.size());
                                                    while (true) {
                                                        try {
                                                            System.out.print("Введите сумму, на которую Вы пополняете свой счёт(при дробном значении используйте ',' , а не '.'): ");
                                                            double amount_d = scanner.nextDouble();
                                                            if (amount_d < 0)
                                                                throw new SignException(amount_d, "средств");
                                                            amount = new BigDecimal(amount_d);
                                                            break;
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Неккоректный ввод... Повторите попытку");
                                                            scanner.nextLine();
                                                        } catch (SignException e) {
                                                            System.out.println(e);
                                                        }
                                                    }
                                                    Account account = accounts.get(choice2 - 1);
                                                    long currentTimeMillis = System.currentTimeMillis();
                                                    Date currentDate = new Date(currentTimeMillis);
                                                    Time currentTime = new Time(currentTimeMillis);
                                                    Transaction newTransaction = new Transaction(Transaction.generateId(currentDate, currentTime), account.getAccountNumber(), account.getAccountNumber(), amount, TransactionType.DEPOSIT, currentDate, currentTime);
                                                    CRUDUtils.saveTransaction(newTransaction);
                                                    account.deposit(amount);
                                                    CRUDUtils.updateAccount(account);
                                                    System.out.println("Пополнение счёта прошло успешно)");
                                                    Check check = new Check(System.currentTimeMillis(), currentDate, currentTime, "Пополнение счёта", account.getBank_name().toString(), account.getBank_name().toString(), account.getAccountNumber(), account.getAccountNumber(), amount);
                                                    check.saveCheckToFile();
                                                    System.out.println("Чек успешно сохранён в папку check");
                                                }
                                            }
                                            case 3 -> {
                                                List<Account> accounts=getAccountsOfTheUser(customer);
                                                if (!accounts.isEmpty()) {
                                                    BigDecimal amount;
                                                    String chooseAccount = "Выберите порядковый номер счёта, с которого вы хотите снять средства: ";
                                                    int choice2 = chooseAction(chooseAccount, 1, accounts.size());
                                                    Account account = accounts.get(choice2 - 1);
                                                    boolean isEnough = true;
                                                    while (true) {
                                                        try {
                                                            scanner = new Scanner(System.in);
                                                            System.out.print("Введите сумму, которую Вы хотите снять со своего счёта(при дробном значении используйте ',' , а не '.'): ");
                                                            double amount_d = scanner.nextDouble();
                                                            if (amount_d < 0)
                                                                throw new SignException(amount_d, "средств");
                                                            amount = new BigDecimal(amount_d);
                                                            if (amount.compareTo(account.getBalance()) > 0) {
                                                                System.out.println("Недостаточно средств на счету(((");
                                                                isEnough = false;
                                                            }
                                                            break;
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Неккоректный ввод... Повторите попытку");
                                                            scanner.nextLine();
                                                        } catch (SignException e) {
                                                            System.out.println(e);
                                                        }
                                                    }
                                                    if (isEnough) {
                                                        long currentTimeMillis = System.currentTimeMillis();
                                                        Date currentDate = new Date(currentTimeMillis);
                                                        Time currentTime = new Time(currentTimeMillis);
                                                        Transaction newTransaction = new Transaction(Transaction.generateId(currentDate, currentTime), account.getAccountNumber(), account.getAccountNumber(), amount, TransactionType.WITHDRAWAL, currentDate, currentTime);
                                                        CRUDUtils.saveTransaction(newTransaction);
                                                        account.withdraw(amount);
                                                        CRUDUtils.updateAccount(account);
                                                        System.out.println("Снятие средств со счёта прошло успешно)");
                                                        Check check = new Check(System.currentTimeMillis(), currentDate, currentTime, "Снятие средств", account.getBank_name().toString(), account.getBank_name().toString(), account.getAccountNumber(), account.getAccountNumber(), amount);
                                                        check.saveCheckToFile();
                                                        System.out.println("Чек успешно сохранён в папку check");
                                                    }
                                                }
                                            }
                                            case 4 -> {
                                                List<Account> accounts = getAccountsOfTheUser(customer);
                                                if(!accounts.isEmpty()) {
                                                    String chooseAccount = "Выберите порядковый номер счёта, с которого вы хотите снять средства: ";
                                                    int choice2 = chooseAction(chooseAccount, 1, accounts.size());
                                                    Account myAccount = accounts.get(choice2 - 1);
                                                    int toAccountNum;
                                                    BigDecimal sumOfTransfer;
                                                    while (true) {
                                                        try {
                                                            scanner = new Scanner(System.in);
                                                            System.out.print("Введите номер счёта, на который Вы хотите сделать перевод: ");
                                                            toAccountNum = scanner.nextInt();
                                                            if (isNumAccountUnique(toAccountNum)) {
                                                                System.out.println("Такого номера счёта не существует( Повторите попытку");
                                                            } else break;
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Неккоректный ввод... Повторите попытку");
                                                            scanner.nextLine();
                                                        }
                                                    }
                                                    Account toAccount = CRUDUtils.getAccount(toAccountNum);
                                                    boolean isEnoughMoney = true;
                                                    while (true) {
                                                        try {
                                                            scanner = new Scanner(System.in);
                                                            System.out.print("Введите сумму перевода в BYN (при дробном значении используйте ',' , а не '.'): ");
                                                            double sumOfTransfer_d = scanner.nextDouble();
                                                            if (sumOfTransfer_d < 0)
                                                                throw new SignException(sumOfTransfer_d, "средств");
                                                            sumOfTransfer = new BigDecimal(sumOfTransfer_d);
                                                            if (sumOfTransfer.compareTo(myAccount.getBalance()) > 0) {
                                                                System.out.println("Недостаточно средств на счету для проведения транзакции");
                                                                isEnoughMoney = false;
                                                            }
                                                            break;
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Неккоректный ввод... Повторите попытку");
                                                            scanner.nextLine();
                                                        } catch (SignException e) {
                                                            System.out.println(e);
                                                        }
                                                    }
                                                    final BigDecimal sum = sumOfTransfer;
                                                    if (isEnoughMoney) {
                                                        Transfering transferService = new Transfering();
                                                        Thread thread1 = new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                transferService.transferFundsFrom(myAccount, sum);
                                                            }
                                                        });
                                                        Thread thread2 = new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                transferService.transferFundsTo(toAccount, sum);
                                                            }
                                                        });
                                                        thread1.start();
                                                        thread2.start();
                                                        try {
                                                            thread1.join();
                                                            thread2.join();
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                        CRUDUtils.updateAccount(myAccount);
                                                        CRUDUtils.updateAccount(toAccount);
                                                        long currentTimeMillis = System.currentTimeMillis();
                                                        Date currentDate = new Date(currentTimeMillis);
                                                        Time currentTime = new Time(currentTimeMillis);
                                                        CRUDUtils.saveTransaction(new Transaction(Transaction.generateId(currentDate, currentTime), myAccount.getAccountNumber(), toAccount.getAccountNumber(), sum, TransactionType.ACCOUNT_TRANSFER, currentDate, currentTime));
                                                        System.out.println("Транзакция выполнена успешно");
                                                        Check check = new Check(System.currentTimeMillis(), currentDate, currentTime, "Перевод", myAccount.getBank_name().toString(), toAccount.getBank_name().toString(), myAccount.getAccountNumber(), toAccount.getAccountNumber(), sum);
                                                        check.saveCheckToFile();
                                                        System.out.println("Чек успешно сохранён в папку check");
                                                    }
                                                }
                                            }
                                            case 5 -> {
                                                List<Account> accounts = getAccountsOfTheUser(customer);
                                                if(!accounts.isEmpty()) {
                                                    String chooseAccount = "Выберите счёт, по которому хотите получить выписку: ";
                                                    int choice2 = chooseAction(chooseAccount, 1, accounts.size());
                                                    Account myAccount = accounts.get(choice2 - 1);
                                                    String choosePeriod = "Выберите период выписки:\n1.За месяц\n2.За год\n3.За весь период обслуживания\n";
                                                    int choice3 = chooseAction(choosePeriod, 1, 3);
                                                    String chooseFormat = "Выберите формат выписки:\n1.PDF\n2.TXT\n";
                                                    int choice4 = chooseAction(chooseFormat, 1, 2);
                                                    AccountStatement accountStatement = new AccountStatement(customer, myAccount);
                                                    accountStatement.saveAccountStatement(choice4, choice3);
                                                }
                                            }
                                            case 6 -> {
                                                flag=false;
                                                System.out.println("Выход из аккаунта клиента "+customer.getLogin()+" выполнен успешно");
                                            }
                                            case 7 -> exit();
                                        }
                                    }
                                }
                                else {
                                    System.out.println("Администратор еще не зарегистрировал Вас. Дождитесь, пожалуйста)");
                                    break;
                                }

                            }
                            break;
                        }
                    }

                }
                case 2 -> {
                    System.out.print("----Регистрация----\nПридумайте себе логин: ");
                    String login;
                    while (true) { // проверяем введённый пользователем логин на уникальность
                        scanner=new Scanner(System.in);
                        login=scanner.nextLine();
                        if (!Admin.isUniqueLogin(login)) System.out.print("Такой логин уже существует...\nПовторите попытку: ");
                        else break;
                    }
                    System.out.print("Придумайте себе пароль: ");
                    String password=scanner.nextLine(),salt=PasswordHashing.generateSalt(),hashedPassword=PasswordHashing.hashPassword(password,salt); // пользователь вводит пароль, генерируется соль и этот пароль хешируется
                    CRUDUtils.saveUser(login,hashedPassword,salt); // сохраняем пользователя в бд
                    User user2=CRUDUtils.getUser(login,password); // получаем пользователя из бд, так как не знаем его user_id
                    System.out.print("Введите свои фамилию, имя, отчество: ");
                    String fio=scanner.nextLine();
                    assert user2 != null;
                    CRUDUtils.saveCustomer(user2,fio); // сохраняем клиента в бд
                    System.out.println("Спасибо за регистрацию! Далее администратор подтвердит Ваши данные и добавит Ваши счета...\nВозвращайтесь позже,пожалуйста");
                }
                case 3 -> exit();
            }
        }
    }

    /**
     * This is a static method which checks the user's choice
     * @param menu this is a string which contains the options which user can choose
     * @param start this is the smallest number which user can choose
     * @param end this is the biggest number which user can choose
     * @return the serial number of the user's choice
     */
    public static int chooseAction(String menu,int start, int end) { // метод выбора действия
       int choice;
       Scanner scanner=new Scanner(System.in);
        while (true) {
            System.out.print(menu);
            try {
                choice = scanner.nextInt();
                if (choice >= start && choice <= end) break;
                else System.out.println("Неверный выбор...Повторите попытку");
            } catch (InputMismatchException e) {
                System.out.println("Неверный ввод... Повторите попытку");
                scanner.nextLine();
            }
        }
        return choice;
    }

    /**
     * This is a static method that ends the work of the app
     */
    public static void exit() {
        System.out.print("Спасибо за использование приложения)\nЖдём Вашего возвращения)");
        System.exit(0);
    }

    /**
     * This static method checks the uniqueness of the account number across all accounts which are in the database
     * @param accountNumber just the account number
     * @return returns true if the number is unique and false if it is not
     */
    public static boolean isNumAccountUnique(int accountNumber) {
        List<Account> accounts=CRUDUtils.getAllAccounts();
        for (Account account: accounts) {
            if(account.getAccountNumber()==accountNumber) {
                return false;
            }
        }
        return true;
    }

    /**
     * This static method is called when it is needed to get all accounts of the exact customer
     * @param customer this is the object of the class Customer
     * @return returns the list of accounts of the customer
     */
    public static List<Account> getAccountsOfTheUser(Customer customer) {
        List<Account> myAccounts=customer.getAccounts();
        System.out.println("№ Номер счёта     Баланс на счету         Банк       Дата открытия");
        int i=1;
        for (Account account: myAccounts) {
            System.out.print(i+". "+account);
            i++;
        }
        if(i==1) System.out.println("Счетов нет");
        return myAccounts;
    }

    /**
     * This static method checks if today is the end of the current month or not
     * @return true if the end and false if not
     */
    public static boolean isEndOfMonth() {
        LocalDate currentDate = LocalDate.now();
        int dayOfMonth = currentDate.getDayOfMonth();
        int lastDayOfMonth = currentDate.lengthOfMonth();
        return dayOfMonth == lastDayOfMonth;
    }

    /**
     * This static method prints all the customers
     * @return returns the list of all customers
     */
    public static List<Customer> outCustomers() {
        List<Customer> customers=CRUDUtils.getAllCustomers();
        System.out.println("№ Логин                ФIО");
        int i=1;
        for (Customer customer1: customers) {
            System.out.println(i+" "+customer1.getLogin() + " " + customer1.getFIO());
            i++;
        }
        return customers;
    }
}