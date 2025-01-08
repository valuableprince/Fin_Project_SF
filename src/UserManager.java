import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private static final String USERS_DIRECTORY = "users_data";
    private Map<String, User> users;

    public UserManager() {
        users = new HashMap<>();
        loadUsers();
    }

    public void addUser(String username, String password) {
        users.put(username, new User(username, password));
        saveUser(username);
    }

    public User authenticateUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            user.setWallet(Wallet.loadWalletData(username));
            return user;
        }
        return null;
    }

    private void saveUser(String username) {
        User user = users.get(username);

        File userDir = new File(USERS_DIRECTORY, username);
        if (!userDir.exists()) {
            boolean dirsCreated = userDir.mkdirs();
            if (!dirsCreated) {
                System.out.println("Не удалось создать директорию для пользователя: " + username);
                return;
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(userDir, "user.ser")))) {
            oos.writeObject(user);
            user.getWallet().saveWalletData(username);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных пользователя: " + e.getMessage());
        }
    }


    public void loadUsers() {
        File usersDir = new File(USERS_DIRECTORY);
        if (!usersDir.exists()) {
            boolean dirsCreated = usersDir.mkdirs();
            if (!dirsCreated) {
                System.out.println("Не удалось создать директорию для пользователей.");
                return;
            }
        }

        File[] userDirs = usersDir.listFiles();
        if (userDirs != null) {
            for (File userDir : userDirs) {
                if (userDir.isDirectory()) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(userDir, "user.ser")))) {
                        User user = (User) ois.readObject();
                        users.put(user.getUsername(), user);
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Ошибка при загрузке данных пользователя: " + e.getMessage());
                    }
                }
            }
        } else {
            System.out.println("Не удалось прочитать директории пользователей.");
        }
    }
}
