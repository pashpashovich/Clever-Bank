package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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
        int choice = 0; // переменная для выбора действия
        Scanner scanner = new Scanner(System.in);
        while (true) {
            User user = null;// текущий пользователь системы
            Admin admin=null;
            Customer customer=null;
            while (true) {
                System.out.println("Выберите действие:\n1.Авторизация\n2.Регистрация\n3.Выход из программы");
                try {
                    choice = scanner.nextInt();
                    if (choice > 0 && choice < 4) break;
                    else System.out.println("Неверный выбор...Повторите попытку");
                } catch (InputMismatchException e) {
                    System.out.println("Неверный ввод... Повторите попытку");
                    scanner.nextLine();

                }
            }
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
                                System.out.println("Вы вошли в приложение как администратор ");
                            }
                            else {
                                customer=CRUDUtils.getCustomer(user);
                                if (customer!=null)
                                {
                                    System.out.println("Вы вошли в приложение как клиент "+customer.getFIO());
                                    boolean flag=true;
                                    while (flag) {
                                        while (true) {
                                            System.out.println("Выберите действие:\n1.Просмотреть свои счета\n2.Выйти из аккаунта\n3.Выйти из приложения");
                                            try {
                                                choice = scanner.nextInt();
                                                if (choice > 0 && choice < 3) break;
                                                else System.out.println("Неверный выбор...Повторите попытку");
                                            } catch (InputMismatchException e) {
                                                System.out.println("Неверный ввод... Повторите попытку");
                                                scanner.nextLine();

                                            }
                                        }
                                        switch (choice) {
                                            case 1 -> {
                                                System.out.println(customer.getAccounts());
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
                                    System.out.println("Что-то пошло не так...");
                                    break;
                                }

                            }



                            break;
                        }
                    }

                }
                case 2 -> {

                }

                case 3 -> {
                    exit();

                }
            }
//        CRUDUtils.getTransactionsData("SELECT * FROM \"public\".\"TransactionBank\"");
//        System.out.println(CRUDUtils.getUsersData("SELECT * FROM \"public\".\"User\""));
//        System.out.println(CRUDUtils.getAccountsData("SELECT * FROM \"public\".\"Account\""));

        }
    }

    public static void exit() {
        System.out.print("Спасибо за использование приложения)\nЖдём Вашего возвращения)");
        System.exit(0);
    }
}