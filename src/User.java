import java.io.*;

public class User implements Serializable {
    private String username;
    private String password;
    private Wallet wallet;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.wallet = new Wallet();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
