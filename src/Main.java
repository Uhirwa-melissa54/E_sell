import java.sql.*;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

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

    @Override
    public double calculateProfit() {
        return dailyProfit * days;
    }
}

class Capital {
    protected double initialCapital;
    public Capital() {}
    public Capital(double initialCapital) { this.initialCapital = initialCapital; }

    public void addCapital(double amount) {
        initialCapital += amount;
        System.out.println("Added capital: " + amount + " | New Total: " + initialCapital);
    }

    public double getCapital() { return initialCapital; }
}

class Tax extends Capital {
    private double taxRate;
    public Tax(double taxRate) { this.taxRate = taxRate; }

    public double calculateTax(double profit) { return profit * taxRate; }
}

class Payment {
    public void processPayment(double amount) { System.out.println("Processing generic payment of " + amount); }
}

class CashPayment extends Payment {
    @Override
    public void processPayment(double amount) { System.out.println("Cash payment received: " + amount); }
}

class CreditPayment extends Payment {
    @Override
    public void processPayment(double amount) { System.out.println("Credit payment recorded: " + amount); }
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

class Seller {
    private String accountId;
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
    public String getName() { return name; }
    public String getPassword() { return password; }

    public void addProduct(String productName, double costPrice, double sellingPrice) {
        products.add(new Product(productName, costPrice, sellingPrice));
    }

    public void sellProduct(String productName, int quantity, Payment paymentMethod) {
        for (Product p : products) {
            if (p.getName().equalsIgnoreCase(productName)) {
                double profit = (p.getSellingPrice() - p.getCostPrice()) * quantity;
                totalProfit += profit;
                double totalAmount = p.getSellingPrice() * quantity;
                paymentMethod.processPayment(totalAmount);
                System.out.println("Sold " + quantity + " " + productName + " with profit: " + profit);
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

public class Main {
    private static final String BASE_PATH = "C:\\Users\\HP\\IdeaProjects\\";

    public static void logAction(String filename, String action) {
        try (FileWriter writer = new FileWriter(filename, true)) {
            writer.write(LocalDate.now() + " - " + action + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection conn = null;
        Scanner sc = new Scanner(System.in);
        Seller seller = null;

        try {
            ConnectDB task = new ConnectDB();
            Thread t = new Thread(task);
            t.start();
            t.join();
            conn = task.getConnection();

            boolean loggedIn = false;

            while (!loggedIn) {
                System.out.println("\nWelcome to Sell_Safely system");
                System.out.println("1. Sign Up");
                System.out.println("2. Log In");
                System.out.print("Your choice: ");
                int option = sc.nextInt();
                sc.nextLine();

                if (option == 1) {
                    // Sign Up
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt(); sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String pass = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter Location: ");
                    String location = sc.nextLine();

                    String sql = "INSERT INTO clients (name, email, location, password, _id) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, name);
                    pstmt.setString(2, email);
                    pstmt.setString(3, location);
                    pstmt.setString(4, pass);
                    pstmt.setInt(5, id);

                    int rowsInserted = pstmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Sign Up successful!");
                        String filename = BASE_PATH + name + "_" + pass + ".txt";
                        File file = new File(filename);
                        if (!file.exists()) file.createNewFile();
                        seller = new Seller(String.valueOf(id), name, pass);
                        loggedIn = true;
                    } else {
                        System.out.println("Sign Up failed. Try again.");
                    }

                } else if (option == 2) {
                    // Login
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String pass = sc.nextLine();

                    String sql = "SELECT * FROM clients WHERE name = ? AND password = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, name);
                    pstmt.setString(2, pass);

                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        int id = rs.getInt("_id");
                        seller = new Seller(String.valueOf(id), name, pass);
                        System.out.println("Login successful!");
                        loggedIn = true;
                    } else {
                        System.out.println("Invalid credentials.");
                        System.out.print("Do you want to Sign Up (1) or try Login again (2)? ");
                        int nextChoice = sc.nextInt(); sc.nextLine();
                        if (nextChoice == 1) option = 1; // force signup in next loop
                    }
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            }

            // Main Menu
            String filename = BASE_PATH + seller.getName() + "_" + seller.getPassword() + ".txt";

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
                int choice = sc.nextInt(); sc.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter product name: ");
                        String productName = sc.nextLine();
                        System.out.print("Enter cost price: ");
                        double costPrice = sc.nextDouble();
                        System.out.print("Enter selling price: ");
                        double sellingPrice = sc.nextDouble(); sc.nextLine();

                        seller.addProduct(productName, costPrice, sellingPrice);
                        logAction(filename, "Added product: " + productName + " | Cost: " + costPrice + " | Sell: " + sellingPrice);
                        break;

                    case 2:
                        System.out.print("Enter product name to sell: ");
                        String productToSell = sc.nextLine();
                        System.out.print("Enter quantity: ");
                        int quantity = sc.nextInt(); sc.nextLine();
                        System.out.print("Enter payment method (cash/credit): ");
                        String method = sc.nextLine();

                        Payment payment = method.equalsIgnoreCase("cash") ? new CashPayment() : new CreditPayment();
                        seller.sellProduct(productToSell, quantity, payment);
                        logAction(filename, "Sold product: " + productToSell + " | Quantity: " + quantity + " | Payment: " + method);
                        break;

                    case 3:
                        System.out.print("Enter customer name: ");
                        String customer = sc.nextLine();
                        System.out.print("Enter amount: ");
                        double amount = sc.nextDouble(); sc.nextLine();

                        seller.giveCredit(customer, amount);
                        logAction(filename, "Gave credit to: " + customer + " | Amount: " + amount);
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
                        Tax taxSystem = new Tax(0.18);
                        double tax = taxSystem.calculateTax(seller.getTotalProfit());
                        System.out.println("Tax on today’s profit: " + tax);
                        taxSystem.addCapital(seller.getTotalProfit() - tax);
                        System.out.println("Capital after tax: " + taxSystem.getCapital());
                        break;

                    case 8:
                        System.out.println("Goodbye!");
                        conn.close();
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }
            }

        } catch (InterruptedException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
