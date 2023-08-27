package org.example;

import java.security.NoSuchAlgorithmException;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

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
                                                            System.out.print("Введите баланс счёта (при дробном значении используйте ',': ");
                                                            balance = scanner.nextDouble();
                                                            break;
                                                        } catch (InputMismatchException e) {
                                                            System.out.println("Неккоректный ввод... Повторите попытку");
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
                                            String customerMenu="Выберите действие:\n1.Просмотреть свои счета\n2.Выйти из аккаунта\n3.Выйти из приложения";
                                            choice=chooseAction(customerMenu,1,3);
                                        switch (choice) {
                                            case 1 -> {
                                                System.out.println(customer.getAccounts());
                                            }
                                            case 2 -> {
                                                flag=false;
                                            }
                                            case 3 -> exit();

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
}