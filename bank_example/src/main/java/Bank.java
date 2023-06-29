import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Bank {
    private List<User> usersOfBank = new ArrayList<>();
    public List<User> getUsersOfBank() {
        return usersOfBank;
    }

    public void showUserBalance (User user){

        System.out.println("Your balance is: " + user.getBalance() +" " + user.getMainCurrency());

    }
    public void userWithdrawMoney (User user, BigDecimal amountToWithdraw){

        if (user.getBalance().compareTo(amountToWithdraw)!=-1) {
        BigDecimal newBalance = user.getBalance().subtract(amountToWithdraw);
        user.setBalance(newBalance);
            System.out.println("Operation successful");
        }
        else {
            System.out.println("Not enough money in your account");
        }
        System.out.println("Your balance is: " + user.getBalance() +" " + user.getMainCurrency());
    }
    public void userDepositMoney (User user, BigDecimal amountToDeposit){
        BigDecimal newBalance = user.getBalance().add(amountToDeposit);
        user.setBalance(newBalance);
        System.out.println("Operation successful");
        System.out.println("Your balance is: " + user.getBalance() +" " + user.getMainCurrency());
    }

    public User createNewUser(User registeredUser){

        replaceUser(registeredUser);
        System.out.println("User successfully created.");
        return registeredUser;
    }

    private void replaceUser(User registeredUser){
        usersOfBank.clear();
        usersOfBank.add(registeredUser);

    }

}
