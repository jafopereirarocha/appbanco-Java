import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// ---------------------- ACCOUNT BASE CLASS ----------------------
abstract class Account {
    protected String accountNumber;
    protected double balance;

    public Account(double initialBalance) {
        this.accountNumber = generateAccountNumber();
        this.balance = initialBalance;
    }

    private String generateAccountNumber() {
        Random rand = new Random();
        int number = 10000000 + rand.nextInt(90000000); // 8-digit number
        return String.valueOf(number);
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.printf("Deposited $%.2f. New balance: $%.2f%n", amount, balance);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.printf("Withdrew $%.2f. New balance: $%.2f%n", amount, balance);
            return true;
        } else {
            System.out.println("Error: Insufficient funds!");
            return false;
        }
    }
}

// ---------------------- SAVINGS ACCOUNT ----------------------
class SavingsAccount extends Account {
    public SavingsAccount(double initialBalance) {
        super(initialBalance);
    }
}

// ---------------------- CHECKING ACCOUNT ----------------------
class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(double initialBalance, double overdraftLimit) {
        super(initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= balance + overdraftLimit) {
            balance -= amount;
            System.out.printf("Withdrew $%.2f. New balance: $%.2f%n", amount, balance);
            return true;
        } else {
            System.out.println("Error: Withdrawal exceeds overdraft limit!");
            return false;
        }
    }
}

// ---------------------- CUSTOMER ----------------------
class Customer {
    private String name;
    private String id;
    private ArrayList<Account> accounts;

    public Customer(String name, String id) {
        this.name = name;
        this.id = id;
        accounts = new ArrayList<>();
    }

    public String getName() { return name; }
    public String getId() { return id; }

    public void addAccount(Account account) { accounts.add(account); }

    public Account getAccount(String accNumber) {
        for (Account a : accounts) {
            if (a.getAccountNumber().equals(accNumber)) return a;
        }
        return null;
    }

    public void showAccounts() {
        System.out.println("Accounts for " + name + ":");
        for (Account a : accounts) {
            System.out.printf("Account Number: %s, Balance: $%.2f%n", a.getAccountNumber(), a.getBalance());
        }
    }
}

// ---------------------- BANK ----------------------
class Bank {
    private ArrayList<Customer> customers;

    public Bank() { customers = new ArrayList<>(); }

    public void addCustomer(Customer customer) { customers.add(customer); }

    public Customer findCustomer(String id) {
        for (Customer c : customers) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    public void listCustomers() {
        System.out.println("Bank Customers:");
        for (Customer c : customers) {
            System.out.printf("Customer Name: %s, ID: %s%n", c.getName(), c.getId());
        }
    }
}

// ---------------------- MAIN CLASS ----------------------
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();
        String id;

        System.out.print("Enter customer name: ");
        String name = sc.nextLine();

        // Validate 8-digit customer ID
        while (true) {
            System.out.print("Enter 8-digit customer ID: ");
            id = sc.nextLine();
            if (id.matches("\\d{8}")) break;
            System.out.println("Error: Customer ID must be exactly 8 digits!");
        }

        Customer customer = new Customer(name, id);
        bank.addCustomer(customer);

        boolean exit = false;
        while (!exit) {
            System.out.println("\n--- Banking Menu ---");
            System.out.println("1. Create Savings Account");
            System.out.println("2. Create Checking Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Show Accounts");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter initial balance: ");
                    double saBal = Double.parseDouble(sc.nextLine().replace(',', '.'));
                    SavingsAccount sa = new SavingsAccount(saBal);
                    customer.addAccount(sa);
                    System.out.println("Savings account created! Account Number: " + sa.getAccountNumber());
                    break;

                case 2:
                    System.out.print("Enter initial balance: ");
                    double caBal = Double.parseDouble(sc.nextLine().replace(',', '.'));
                    System.out.print("Enter overdraft limit: ");
                    double overdraft = Double.parseDouble(sc.nextLine().replace(',', '.'));
                    CheckingAccount ca = new CheckingAccount(caBal, overdraft);
                    customer.addAccount(ca);
                    System.out.println("Checking account created! Account Number: " + ca.getAccountNumber());
                    break;

                case 3:
                    System.out.print("Enter account number: ");
                    String depAcc = sc.nextLine();
                    Account depAccount = customer.getAccount(depAcc);
                    if (depAccount != null) {
                        System.out.print("Enter deposit amount: ");
                        double depAmount = Double.parseDouble(sc.nextLine().replace(',', '.'));
                        depAccount.deposit(depAmount);
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                case 4:
                    System.out.print("Enter account number: ");
                    String wdAcc = sc.nextLine();
                    Account wdAccount = customer.getAccount(wdAcc);
                    if (wdAccount != null) {
                        System.out.print("Enter withdrawal amount: ");
                        double wdAmount = Double.parseDouble(sc.nextLine().replace(',', '.'));
                        wdAccount.withdraw(wdAmount);
                    } else {
                        System.out.println("Account not found!");
                    }
                    break;

                case 5:
                    customer.showAccounts();
                    break;

                case 6:
                    exit = true;
                    System.out.println("Exiting... Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Try again!");
            }
        }

        sc.close();
    }
}
