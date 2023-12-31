import java.math.BigDecimal;

public class User {
    private final String name;
    private final String surname;
    private final String gender;
    private final long accountNumber;
    private final String mainCurrency;
    private BigDecimal balance;

    public User(String name, String surname, String gender, long accountNumber, String mainCurrency, BigDecimal balance) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.accountNumber = accountNumber;
        this.mainCurrency = mainCurrency;
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getMainCurrency() {
        return mainCurrency;
    }

    @Override
    public String toString() {
        return "User " +
                "name: '" + name + '\'' +
                ", surname: '" + surname + '\'' +
                ", gender: '" + gender + '\'' +
                ", accountNumber: " + accountNumber +
                ", mainCurrency: " + mainCurrency + '\'' +
                ", balance: " + balance;
    }
}
