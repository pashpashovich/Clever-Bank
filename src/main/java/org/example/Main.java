package org.example;

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

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
//        Server server = new Server(8080);
//        ServletContextHandler handler = new ServletContextHandler();
//        handler.setContextPath("/");
//        UserServlet userServlet=new UserServlet();
//        handler.addServlet(new ServletHolder(userServlet), "/users");
//        server.setHandler(handler);
//        try {
//            server.start();
//        } catch (Exception e) {
//            System.out.println("Что-то пошло не так...");;
//        }
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            LocalDate currentDate = LocalDate.now();
            if (isEndOfMonth(currentDate)) {
                YML.writeBank();
                YML.updateAccounts();
            }
        }, 0, 30, TimeUnit.SECONDS);
        System.out.println("----Добро пожаловать в приложение Clever-Bank----");
        int choice; // переменная для выбора действия
        Scanner scanner;
        while (true) {
            User user;// текущий пользователь системы
            Admin admin;
            Customer customer;
            String startMenu="Выберите действие:\n1.Авторизация\n2.Регистрация\n3.Выход из программы";
            choice=chooseAction(startMenu,1,3);
            switch (choice) {
                case 1 -> {
                    while (true) {
                        System.out.print("Введите Ваш логин: ");
                        scanner=new Scanner(System.in);
                        String login;
                        login = scanner.nextLine();
                        System.out.print("Введите Ваш пароль: ");
                        String password;
                        password = scanner.nextLine();
                        user = CRUDUtils.getUser(login, password);
                        if (user == null) {
                            System.out.println("Неверный логин или пароль...");
                        System.out.println("1.Повторить попытку\n2.Выйти в главное меню");
                        scanner=new Scanner(System.in);
                        choice=scanner.nextInt();
                        if (choice==2) break;
                        }
                        else {
                            admin=CRUDUtils.getAdmin(user);
                            if (admin!=null) {
                                System.out.println("Вы вошли в приложение как администратор "+admin.getLogin());
                                boolean flag=true;
                                while (flag) {
                                        String adminMenu="Выберите действие:\n1.Рассмотреть заявки на регистрацию\n2.Выйти из аккаунта\n3.Выйти из приложения";
                                        choice=chooseAction(adminMenu,1,3);
                                    switch (choice) {
                                        case 1 -> {
                                            List<Customer> customers;
                                            customers=CRUDUtils.getAllCustomers();
                                            System.out.println("---Неподтверждённые заявки---");
                                            int i=1;
                                            System.out.println("  Логин                       ФИО");
                                            Iterator<Customer> iterator = customers.iterator();
                                            while (iterator.hasNext()) {
                                                Customer registred_customer = iterator.next();
                                                if (!registred_customer.isHasAccess()) {
                                                    System.out.println(i + ". " + registred_customer.getLogin() + " " + registred_customer.getFIO());
                                                    i++;
                                                } else {
                                                    iterator.remove(); // Удаление элемента из коллекции
                                                }
                                            }
                                            if(i==1) System.out.println("Заявок нет");
                                            else {
                                                String adminAppliances = "Выберите заявку для подтверждения: ";
                                                choice = chooseAction(adminAppliances, 1, i);
                                                System.out.println(customers.get(choice-1));
                                                Customer newCustomer=customers.get(choice - 1);
                                                CRUDUtils.updateCustomer(newCustomer);
                                                System.out.println("Заявка подтверждена!\nДавайте теперь заполним данные этого клиента");
                                                System.out.println("Введите количество счетов для добавления:");
                                                int numberofAccounts;
                                                while (true) {
                                                   try {
                                                       scanner = new Scanner(System.in);
                                                       numberofAccounts = scanner.nextInt();
                                                       break;
                                                   } catch (InputMismatchException e) {
                                                       System.out.println("Неверный ввод... Повторите попытку");
                                                       scanner.nextLine();
                                                   }
                                               }
                                                int numberAccount;
                                                double balance;
                                                for (int k=0;k<numberofAccounts;k++) {
                                                    while (true)
                                                    {
                                                        scanner=new Scanner(System.in);
                                                        System.out.print("Введите номер счёта: ");
                                                    numberAccount = scanner.nextInt();
                                                    if (isNumAccountUnique(numberAccount)) break;
                                                    else System.out.println("Такой номер счёта уже существует в базе данных\nПовторите попытку");
                                                }
                                                    while (true) {
                                                        try {
                                                            scanner=new Scanner(System.in);
                                                            System.out.print("Введите баланс счёта (при дробном значении используйте ',' , а не '.'): ");
                                                            balance = scanner.nextDouble();
                                                            break;
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Неккоректный ввод... Повторите попытку");
                                                            scanner.nextLine();
                                                        }
                                                    }
                                                    String banks="Выберите банк, в котором будет обслуживаться счёт\n1.Клевер-банк\n2.Белинвестбанк\n3.БСББанк\n4.БеларусБанк\n5.СтатусБанк";
                                                    int bankNum=chooseAction(banks,1,5);
                                                    CRUDUtils.saveAccount(new Account(numberAccount,balance,BankName.values()[bankNum-1],null),newCustomer.getUser_id());
                                               }
                                            }
                                        }
                                        case 2 -> {
                                            flag=false;
                                        }
                                        case 3 -> {
                                            exit();
                                        }
                                    }
                                }
                            }
                            else {
                                customer=CRUDUtils.getCustomer(user);
                                if (customer!=null)
                                {
                                    System.out.println("Вы вошли в приложение как клиент "+customer.getFIO());
                                    boolean flag=true;
                                    while (flag) {
                                            String customerMenu="Выберите действие:\n1.Просмотреть свои счета\n2.Пополнение средств на счету\n3.Снятие средств со счёта \n4.Перевод денежных средств\n5.Выйти из аккаунта\n6.Выйти из приложения";
                                            choice=chooseAction(customerMenu,1,6);
                                        switch (choice) {
                                            case 1 -> {
                                               getAccountsOfTheUser(customer);
                                            }
                                            case 2 -> {
                                                List<Account> accounts=getAccountsOfTheUser(customer);
                                                double amount;
                                                String chooseAccount="Выберите счёт, который вы хотите пополнить: ";
                                                int choice2=chooseAction(chooseAccount,1,accounts.size());
                                                while (true) {
                                                    try {
                                                        System.out.print("Введите сумму, на которую Вы пополняете свой счёт(при дробном значении используйте ',' , а не '.'): ");
                                                        amount = scanner.nextDouble();
                                                        break;
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Неккоректный ввод... Повторите попытку");
                                                        scanner.nextLine();
                                                    }
                                                }
                                                Account account=accounts.get(choice2-1);
                                                long currentTimeMillis = System.currentTimeMillis();
                                                Date currentDate = new Date(currentTimeMillis);
                                                Time currentTime = new Time(currentTimeMillis);
                                                Transaction newTransaction=new Transaction(Transaction.generateId(currentDate,currentTime),account.getAccountNumber(),account.getAccountNumber(),amount,TransactionType.DEPOSIT, currentDate,currentTime);
                                                CRUDUtils.saveTransaction(newTransaction);
                                                account.deposit(amount);
                                                CRUDUtils.updateAccount(account);
                                                System.out.println("Пополнение счёта прошло успешно)");
                                                Check check=new Check( System.currentTimeMillis(),currentDate,currentTime,"Пополнение счёта", account.getBank_name().toString(),account.getBank_name().toString(),account.getAccountNumber(),account.getAccountNumber(),amount);
                                                check.saveCheckToFile();
                                                System.out.println("Чек успешно сохранён в папку check");
                                            }
                                            case 3 -> {
                                                List<Account> accounts=getAccountsOfTheUser(customer);
                                                double amount;
                                                String chooseAccount="Выберите счёт, с которого вы хотите снять средства: ";
                                                int choice2=chooseAction(chooseAccount,1,accounts.size());
                                                Account account=accounts.get(choice2-1);
                                                boolean isEnough=true;
                                                while (true) {
                                                    try {
                                                        scanner=new Scanner(System.in);
                                                        System.out.print("Введите сумму, которую Вы хотите снять со своего счёта(при дробном значении используйте ',' , а не '.'): ");
                                                        amount = scanner.nextDouble();
                                                        if(amount>account.getBalance()) {
                                                            System.out.println("Недостаточно средств на счету(((");
                                                            isEnough=false;
                                                        }
                                                        break;
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Неккоректный ввод... Повторите попытку");
                                                        scanner.nextLine();
                                                    }
                                                }
                                                if (isEnough) {
                                                    long currentTimeMillis = System.currentTimeMillis();
                                                    Date currentDate = new Date(currentTimeMillis);
                                                    Time currentTime = new Time(currentTimeMillis);
                                                    Transaction newTransaction = new Transaction(Transaction.generateId(currentDate,currentTime),account.getAccountNumber(), account.getAccountNumber(), amount, TransactionType.WITHDRAWAL, currentDate, currentTime);
                                                    CRUDUtils.saveTransaction(newTransaction);
                                                    account.withdraw(amount);
                                                    CRUDUtils.updateAccount(account);
                                                    System.out.println("Снятие средств со счёта прошло успешно)");
                                                    Check check=new Check( System.currentTimeMillis(),currentDate,currentTime,"Снятие средств", account.getBank_name().toString(),account.getBank_name().toString(),account.getAccountNumber(),account.getAccountNumber(),amount);
                                                    check.saveCheckToFile();
                                                    System.out.println("Чек успешно сохранён в папку check");
                                                }
                                            }
                                            case 4 -> { // поменять тип на BigDecimal
                                                List<Account> accounts = getAccountsOfTheUser(customer);
                                                String chooseAccount = "Выберите счёт, с которого вы хотите снять средства: ";
                                                int choice2 = chooseAction(chooseAccount, 1, accounts.size());
                                                Account myAccount = accounts.get(choice2 - 1);
                                                int toAccountNum;
                                                double sumOfTransfer;
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
                                                Account toAccount=CRUDUtils.getAccount(toAccountNum);
                                                boolean isEnoughMoney = true;
                                                while (true) {
                                                    try {
                                                        scanner = new Scanner(System.in);
                                                        System.out.print("Введите сумму перевода: ");
                                                        sumOfTransfer = scanner.nextDouble();
                                                        if (myAccount.getBalance() < sumOfTransfer) {
                                                            System.out.println("Недостаточно средств на счету для проведения транзакции");
                                                            isEnoughMoney = false;
                                                        }
                                                        break;
                                                    } catch (InputMismatchException e) {
                                                        System.out.println("Неккоректный ввод... Повторите попытку");
                                                        scanner.nextLine();
                                                    }
                                                }
                                                final double sum=sumOfTransfer;
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
                                                    CRUDUtils.saveTransaction(new Transaction(Transaction.generateId(currentDate,currentTime),myAccount.getAccountNumber(),toAccount.getAccountNumber(),sum,TransactionType.ACCOUNT_TRANSFER,currentDate,currentTime));
                                                    System.out.println("Транзакция выполнена успешно");
                                                    Check check=new Check( System.currentTimeMillis(),currentDate,currentTime,"Перевод", myAccount.getBank_name().toString(),toAccount.getBank_name().toString(),myAccount.getAccountNumber(),toAccount.getAccountNumber(),sum);
                                                    check.saveCheckToFile();
                                                    System.out.println("Чек успешно сохранён в папку check");
                                                }
                                            }
                                            case 5 -> flag=false;
                                            case 6 -> exit();

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
                    System.out.println("----Регистрация----\nПридумайте себе логин: ");
                    String login;
                    while (true) {
                        scanner=new Scanner(System.in);
                        login=scanner.nextLine();
                        if (!Admin.isUniqueLogin(login)) System.out.println("Такой логин уже существует...\nПовторите попытку: ");
                        else break;
                    }
                    System.out.println("Придумайте себе пароль: ");
                    String password=scanner.nextLine(),salt=PasswordHashing.generateSalt(),hashedPassword=PasswordHashing.hashPassword(password,salt);
                    CRUDUtils.saveUser(login,hashedPassword,salt);
                    User user2=CRUDUtils.getUser(login,password);
                    System.out.println("Введите свои фамилию, имя, отчество (латиницей): ");
                    String fio=scanner.nextLine();
                    assert user2 != null;
                    CRUDUtils.saveCustomer(user2,fio);
                    System.out.println("Спасибо за регистрацию! Далее администратор подтвердит Ваши данные и добавит Ваши счета...\nОжидайте,пожалуйста");
                }
                case 3 -> exit();
            }
//        CRUDUtils.getTransactionsData("SELECT * FROM \"public\".\"TransactionBank\"");
//        System.out.println(CRUDUtils.getUsersData("SELECT * FROM \"public\".\"User\""));
//        System.out.println(CRUDUtils.getAccountsData("SELECT * FROM \"public\".\"Account\""));

        }
    }


    public static int chooseAction(String menu,int start, int end) { // метод выбора действия
       int choice=0;
       Scanner scanner=new Scanner(System.in);
        while (true) {
            System.out.println(menu);
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
    public static void exit() {
        System.out.print("Спасибо за использование приложения)\nЖдём Вашего возвращения)");
        System.exit(0);
    }

    public static boolean isNumAccountUnique(int accountNumber) {
        List<Account> accounts=CRUDUtils.getAllAccounts();
        for (Account account: accounts) {
            if(account.getAccountNumber()==accountNumber) {
                return false;
            }
        }
        return true;
    }
    public static List<Account> getAccountsOfTheUser(Customer customer) {
        List<Account> myAccounts=customer.getAccounts();
        System.out.println("Номер счёта  Баланс на счету      Банк ");
        int i=1;
        for (Account account: myAccounts) {
            System.out.println(i+". "+account);
            i++;
        }
        return myAccounts;
    }
    public static boolean isEndOfMonth(LocalDate date) {
        int dayOfMonth = date.getDayOfMonth();
        int lastDayOfMonth = date.lengthOfMonth();
        return dayOfMonth == lastDayOfMonth;
//        return date.getMonth().equals(Month.AUGUST);
    }
}