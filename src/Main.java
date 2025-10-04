import java.util.*;
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

    public Tax(double initialCapital, double taxRate) {
        super(initialCapital);
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


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Account Creation
        System.out.print("Create Account - Enter ID: ");
        String id = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();

        Seller seller = new Seller(id, name, pass);

        // Login
        System.out.println("\nLogin to your account:");
        System.out.print("Enter ID: ");
        String loginId = sc.nextLine();
        System.out.print("Enter Name: ");
        String loginName = sc.nextLine();
        System.out.print("Enter Password: ");
        String loginPass = sc.nextLine();


        if (seller.login(loginId, loginName, loginPass)) {

            System.out.println("Welcome back, Dear CEO");
            while (true) {
                System.out.println("1.Add products\n 2.Sell products \n 3.Give credit \n 4.View Credits \n 5.View daily profit \n 6.Excepted Yearly Profit \n 7.Tax on today profit \n 8.Exit ");
                Scanner input = new Scanner(System.in);
                int option = input.nextInt();

                switch (option) {
                    case 1:
                        System.out.print("Enter product name:");
                        String productName = input.nextLine();
                        System.out.println("Enter cost price and selling price:");
                        int costPrice = input.nextInt();
                        int sellingPrice = input.nextInt();
                        seller.addProduct(productName, costPrice, sellingPrice);
                        break;
                    case 2:
                        Payment cash = new CashPayment();
                        Payment credit = new CreditPayment();
                        System.out.print("Enter product name:");
                        String productNametobeSold = input.nextLine();
                        System.out.println("Quantity");
                        int quantity = input.nextInt();
                        System.out.print("Form of payment either cash or credit:");
                        String payment = input.nextLine();
                        Payment chosenPayment;
                        if (payment.equalsIgnoreCase("cash")) {
                            chosenPayment = cash;
                        }else {
                            chosenPayment = credit;

                }
                seller.sellProduct(productNametobeSold,quantity,chosenPayment);
                        break;
                    case 3:
                        System.out.println("Who do you wanna give credit to:");
                        String creditors=input.nextLine();
                        System.out.println("Amount");
                        int amount=input.nextInt();
                        seller.giveCredit(creditors,amount);
                        break;
                    case 4:
                        seller.viewCredits();
                    case 5:
                        double dailyProfit = seller.getTotalProfit();
                        System.out.println("Total Profit Today: " + dailyProfit);
                    case 6:
                        double dailyProfit1 = seller.getTotalProfit();
                        Profit profit = new ProfitOverYear(dailyProfit1, 365);
                        System.out.println("Expected Yearly Profit: " + profit.calculateProfit());

                    case 7:
                        double dailyProfit2=seller.getTotalProfit();
                        Tax taxSystem = new Tax(10000, 0.18); // 10,000 initial capital, 18% tax
                        double tax = taxSystem.calculateTax(dailyProfit2);
                        System.out.println("Tax over today's profit: " + tax);
                        taxSystem.addCapital(dailyProfit2 - tax);
                        System.out.println("Remaining Capital after adding profit & paying tax: " + taxSystem.getCapital());

                    case 8:
                        break;






                }
        }








            // Inheritance: Capital & Tax

        } else {
            System.out.println("Login failed!");
        }

        sc.close();
    }
}
