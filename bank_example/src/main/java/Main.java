
import java.math.BigDecimal;
import java.util.Random;
import java.util.Scanner;

public class Main {

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {

     User user = new User ("n1", "s1", "female", 12345566L, "EUR", new BigDecimal("1000.50"));
     Bank bank = new Bank();
     bank.getUsersOfBank().add(user);

     Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the bank. Please choose the ticket:");
        printAMenu();

        while (true) {
            char choice = scanner.next().charAt(0);
        switch(choice) {

            case '1':
                System.out.println("please enter your name");
                scanner.nextLine();
                String name = scanner.nextLine();
                System.out.println("please enter your surname");
                String surname = scanner.nextLine();
                System.out.println("please enter your gender");
                String gender = scanner.nextLine();
                System.out.println("please select your main currency: 1-EUR, 2-USD, 3-JPY, 4-CHF");
                String currency="EUR";

                char option = scanner.next().charAt(0);
                while (!(option == '1'||option == '2'||option == '3'||option == '4')) {
                    System.out.println("please choose a number from 1 to 4!");

                    option = scanner.next().charAt(0);
                    switch (option) {
                        case '1' -> currency = "EUR";
                        case '2' -> currency = "USD";
                        case '3' -> currency = "JPY";
                        case '4' -> currency = "CHF";
                    }
                }

                long min = 1000000000L;
                long max = 9999999999L;
                Random random = new Random();
                long accountNumber = random.nextLong() % (max - min) + max;
                User registeredUser = new User(name, surname, gender, accountNumber, currency, BigDecimal.ZERO);

                user = bank.createNewUser(registeredUser);
                System.out.println(user);



            case '2':
                if (user == null) {
                    System.out.println("no users registered to a bank");

                } else {
                    bank.showUserBalance(user);
                }
                printAMenu();
                break;
            case '3':

                if (user == null) {
                    System.out.println("no users registered to a bank");
                } else {
                    BigDecimal number;
                    do {
                        System.out.println("Please enter amount you want to withdraw (positive number): ");
                        while (!scanner.hasNextBigDecimal()) {
                            System.out.println("Please enter a positive number!");
                            System.out.println("That's not a number!");
                            scanner.next();
                        }
                        number = scanner.nextBigDecimal();
                    } while (number.compareTo(BigDecimal.ZERO) !=1);
                    bank.userWithdrawMoney(user, number);
                    printAMenu();
                }
                break;

            case '4':
                if (user == null) {
                    System.out.println("no users registered to a bank");
                } else {
                    BigDecimal number;
                    do {
                        System.out.println("Please enter amount you want to deposit (positive number): ");
                        while (!scanner.hasNextBigDecimal()) {
                            System.out.println("Please enter a positive number!");
                            System.out.println("That's not a number!");
                            scanner.next();
                        }
                        number = scanner.nextBigDecimal();
                    } while (number.compareTo(BigDecimal.ZERO) !=1);
                    bank.userDepositMoney(user, number);
                    printAMenu();
                }
                break;

            case 'x':
                System.out.println("Thank you for using our bank");
                break;
        }
        }
    }

    private static void printAMenu() {
        System.out.println("Bank menu: ");
        System.out.println("1 - to register as a new user ");
        System.out.println("2 - to see your balance ");
        System.out.println("3 - to withdraw money");
        System.out.println("4 - to deposit money");
        System.out.println("x - to end a program ");
    }
}
