import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Wallet implements Serializable {
    private Map<String, Double> categoryBudgets;
    private Map<String, Double> categoryIncome;
    private Map<String, Double> categoryExpenses;

    public Wallet() {
        categoryBudgets = new HashMap<>();
        categoryIncome = new HashMap<>();
        categoryExpenses = new HashMap<>();
    }

    public void addIncome(double amount, String category) {
        categoryIncome.put(category, categoryIncome.getOrDefault(category, 0.0) + amount);
    }

    public void addExpense(double amount, String category) {
        categoryExpenses.put(category, categoryExpenses.getOrDefault(category, 0.0) + amount);
    }

    public void setCategoryBudget(String category, double amount) {
        categoryBudgets.put(category, amount);
    }

    public void saveWalletData(String username) {
        File userDir = new File("users_data", username);
        if (!userDir.exists()) {
            boolean dirsCreated = userDir.mkdirs();
            if (!dirsCreated) {
                System.out.println("Не удалось создать директорию для кошелька пользователя: " + username);
                return;
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(userDir, "wallet.ser")))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных кошелька: " + e.getMessage());
        }
    }

    public static Wallet loadWalletData(String username) {
        File userDir = new File("users_data", username);
        File file = new File(userDir, "wallet.ser");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (Wallet) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Ошибка при загрузке данных кошелька: " + e.getMessage());
            }
        }
        return new Wallet();
    }

    public double getCategoryIncome(String category) {
        return categoryIncome.getOrDefault(category, 0.0);
    }

    public double getCategoryExpenses(String category) {
        return categoryExpenses.getOrDefault(category, 0.0);
    }

    public double getTotalIncome() {
        return categoryIncome.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public double getTotalExpenses() {
        return categoryExpenses.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public void displayCategoryInfo(StringBuilder report) {
        for (String category : categoryBudgets.keySet()) {
            double budget = categoryBudgets.get(category);
            double expenses = getCategoryExpenses(category);
            double remainingBudget = budget - expenses;

            String categoryInfo = category + ": " + budget + ", Оставшийся бюджет: " + remainingBudget + "\n";
            System.out.print(categoryInfo);
            report.append(categoryInfo);
        }
    }
}
