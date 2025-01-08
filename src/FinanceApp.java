import java.io.*;
import java.util.Scanner;

public class FinanceApp {
    private static UserManager userManager = new UserManager();
    private static User currentUser;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Регистрация");
            System.out.println("2. Вход");
            System.out.println("3. Выйти");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 3:
                    System.out.println("До свидания!");
                    return;
            }
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        userManager.addUser(username, password);
        System.out.println("Пользователь зарегистрирован!");
    }

    private static void loginUser(Scanner scanner) {
        System.out.print("Введите логин: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        currentUser = userManager.authenticateUser(username, password);
        if (currentUser != null) {
            System.out.println("Авторизация успешна!");
            userMenu(scanner);
        } else {
            System.out.println("Неверный логин или пароль.");
        }
    }

    private static void userMenu(Scanner scanner) {
        while (true) {
            System.out.println("\n1. Добавить доход");
            System.out.println("2. Добавить расход");
            System.out.println("3. Установить бюджет");
            System.out.println("4. Показать отчет");
            System.out.println("5. Выход");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addIncome(scanner);
                    break;
                case 2:
                    addExpense(scanner);
                    break;
                case 3:
                    setCategoryBudget(scanner);
                    break;
                case 4:
                    saveFinancialReport();
                    break;
                case 5:
                    return;
            }
        }
    }

    private static void addIncome(Scanner scanner) {
        System.out.print("Введите категорию дохода: ");
        String category = scanner.nextLine();
        double amount = readPositiveAmount(scanner);
        currentUser.getWallet().addIncome(amount, category);
        currentUser.getWallet().saveWalletData(currentUser.getUsername());
    }

    private static void addExpense(Scanner scanner) {
        System.out.print("Введите категорию расхода: ");
        String category = scanner.nextLine();
        double amount = readPositiveAmount(scanner);
        currentUser.getWallet().addExpense(amount, category);
        currentUser.getWallet().saveWalletData(currentUser.getUsername());
    }

    private static void setCategoryBudget(Scanner scanner) {
        System.out.print("Введите категорию для бюджета: ");
        String category = scanner.nextLine();
        double budget = readPositiveAmount(scanner);
        currentUser.getWallet().setCategoryBudget(category, budget);
        currentUser.getWallet().saveWalletData(currentUser.getUsername());
    }

    private static double readPositiveAmount(Scanner scanner) {
        double amount;
        while (true) {
            try {
                System.out.print("Введите сумму: ");
                amount = Double.parseDouble(scanner.nextLine());
                if (amount < 0) {
                    System.out.println("Сумма должна быть положительной.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат суммы. Попробуйте снова.");
            }
        }
        return amount;
    }

    private static void saveFinancialReport() {
        StringBuilder report = new StringBuilder();
        report.append("Общий доход: ").append(currentUser.getWallet().getTotalIncome()).append("\n");
        report.append("Общие расходы: ").append(currentUser.getWallet().getTotalExpenses()).append("\n");
        report.append("Бюджет по категориям: \n");
        currentUser.getWallet().displayCategoryInfo(report);

        System.out.println(report);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("financial_report.txt"))) {
            writer.write(report.toString());
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении отчета: " + e.getMessage());
        }
    }
}
