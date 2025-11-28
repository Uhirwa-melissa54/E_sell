import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
abstract class Profit {
    protected double dailyProfit;
    public abstract double calculateProfit();
}


class ProfitOverYear extends Profit {
    private int days;

    public ProfitOverYear(double dailyProfit, int days) {
        this.dailyProfit = dailyProfit;
        this.days = days;
    }


    public double calculateProfit() {
        return dailyProfit * days;
    }
}
class Capital {
    protected double initialCapital;
    public Capital() {}

    public Capital(double initialCapital) {
        this.initialCapital = initialCapital;
    }

    public void addCapital(double amount) {
        initialCapital += amount;
        System.out.println("Added capital: " + amount + " | New Total: " + initialCapital);
    }

    public double getCapital() {
        return initialCapital;
    }
}

class Tax extends Capital {
    private double taxRate;

    public Tax(double taxRate) {

        this.taxRate = taxRate;
    }

    public double calculateTax(double profit) {
        return profit * taxRate;
    }
}


class Payment {
    public void processPayment(double amount) {
        System.out.println("Processing generic payment of " + amount);
    }
}

class CashPayment extends Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Cash payment received: " + amount);
    }
}

class CreditPayment extends Payment {
    @Override
    public void processPayment(double amount) {
        System.out.println("Credit payment recorded: " + amount);
    }
}

class Seller {
    private String accountId; // private
    private String name;
    private String password;
    private List<Product> products = new ArrayList<>();
    private double totalProfit = 0.0;
    private List<String> creditsGiven = new ArrayList<>();

    public Seller(String accountId, String name, String password) {
        this.accountId = accountId;
        this.name = name;
        this.password = password;
    }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public String getName() { return name; }

    public boolean login(String accountId, String name, String password) {
        return this.accountId.equals(accountId) && this.name.equals(name) && this.password.equals(password);
    }

    public void addProduct(String productName, double costPrice, double sellingPrice) {
        products.add(new Product(productName, costPrice, sellingPrice));
    }

    public void sellProduct(String productName, int quantity, Payment paymentMethod) {
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(productName)) {
                double profit = (p.getSellingPrice() - p.getCostPrice()) * quantity;
                totalProfit += profit;
                double totalAmount = p.getSellingPrice() * quantity;
                paymentMethod.processPayment(totalAmount); // Polymorphism
                System.out.println("Sold " + quantity + " " + productName +
                        " with profit: " + profit);
                return;
            }
        }
        System.out.println("Product not found!");
    }

    public double getTotalProfit() { return totalProfit; }

    public void giveCredit(String customerName, double amount) {
        creditsGiven.add(customerName + " received credit of " + amount);
    }

    public void viewCredits() {
        System.out.println("Credits Given:");
        for (String credit : creditsGiven) {
            System.out.println(credit);
        }
    }
}




class Product {
    private String name;
    private double costPrice;
    private double sellingPrice;

    public Product(String name, double costPrice, double sellingPrice) {
        this.name = name;
        this.costPrice = costPrice;
        this.sellingPrice = sellingPrice;
    }

    public String getName() { return name; }
    public double getCostPrice() { return costPrice; }
    public double getSellingPrice() { return sellingPrice; }
}
 class ConnectDB{
    public Connection connectDB(){
        String url=System.getenv("db_url");
        String user=System.getenv("db_user");
        String password=System.getenv("db_pass");
        try{
            Connection conn= DriverManager.getConnection(url,user,password);
            return conn;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }

    }

}


public class Main {
    private static final String BASE_PATH = "C:\\Users\\HP\\IdeaProjects\\"; // folder for storing user files

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Seller Management System");
        System.out.println("1. Sign Up");
        System.out.println("2. Log In");
        System.out.print("Choose option: ");
        int option = sc.nextInt();
        sc.nextLine(); // clear buffer

        Seller seller = null;

        if (option == 1) {
            // SIGN UP
            System.out.print("Enter ID: ");
            String id = sc.nextLine();
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Password: ");
            String pass = sc.nextLine();

            // Create account file
            String filename = BASE_PATH + name + "_" + pass + ".txt";
            File file = new File(filename);

            try {
                if (file.createNewFile()) {
                    FileWriter writer = new FileWriter(file);
                    writer.write("Account ID: " + id + "\n");
                    writer.write("Name: " + name + "\n");
                    writer.write("Password: " + pass + "\n");
                    writer.close();
                    System.out.println("Account created successfully!");
                    seller = new Seller(id, name, pass);
                } else {
                    System.out.println("Account already exists. Please log in instead.");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

        } else if (option == 2) {
            // LOGIN
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Password: ");
            String pass = sc.nextLine();

            String filename = BASE_PATH + name + "_" + pass + ".txt";
            File file = new File(filename);

            if (file.exists()) {
                System.out.println("Login successful!");
                seller = new Seller("001", name, pass); // ID is not stored; using dummy
            } else {
                System.out.println("Login failed! Account not found.");
                return;
            }
        } else {
            System.out.println("Invalid option!");
            return;
        }

        // ----------------------------
        // AFTER LOGIN — main menu
        // ----------------------------
        while (true) {
            System.out.println("\n1. Add product");
            System.out.println("2. Sell product");
            System.out.println("3. Give credit");
            System.out.println("4. View credits");
            System.out.println("5. View daily profit");
            System.out.println("6. Expected yearly profit");
            System.out.println("7. Tax on today’s profit");
            System.out.println("8. Exit");

            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {
                case 1:
                    System.out.print("Enter product name: ");
                    String productName = sc.nextLine();
                    System.out.print("Enter cost price: ");
                    double costPrice = sc.nextDouble();
                    System.out.print("Enter selling price: ");
                    double sellingPrice = sc.nextDouble();
                    sc.nextLine();
                    seller.addProduct(productName, costPrice, sellingPrice);
                    break;

                case 2:
                    System.out.print("Enter product name to sell: ");
                    String productToSell = sc.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantity = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter payment method (cash/credit): ");
                    String method = sc.nextLine();

                    Payment payment = method.equalsIgnoreCase("cash") ? new CashPayment() : new CreditPayment();
                    seller.sellProduct(productToSell, quantity, payment);
                    break;

                case 3:
                    System.out.print("Enter customer name: ");
                    String customer = sc.nextLine();
                    System.out.print("Enter amount: ");
                    double amount = sc.nextDouble();
                    sc.nextLine();
                    seller.giveCredit(customer, amount);
                    break;

                case 4:
                    seller.viewCredits();
                    break;

                case 5:
                    System.out.println("Today's total profit: " + seller.getTotalProfit());
                    break;

                case 6:
                    Profit profit = new ProfitOverYear(seller.getTotalProfit(), 365);
                    System.out.println("Expected yearly profit: " + profit.calculateProfit());
                    break;

                case 7:
                    Tax taxSystem = new Tax(10000, 0.18);
                    double tax = taxSystem.calculateTax(seller.getTotalProfit());
                    System.out.println("Tax on today’s profit: " + tax);
                    taxSystem.addCapital(seller.getTotalProfit() - tax);
                    System.out.println("Capital after tax: " + taxSystem.getCapital());
                    break;

                case 8:
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}


