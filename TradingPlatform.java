// TradingPlatform.java
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Stock.java
class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}


class Portfolio {
    private Map<String, Integer> stocks; // Symbol and quantity

    public Portfolio() {
        stocks = new HashMap<>();
    }

    public void buyStock(String symbol, int quantity) {
        stocks.put(symbol, stocks.getOrDefault(symbol, 0) + quantity);
    }

    public void sellStock(String symbol, int quantity) {
        int currentQuantity = stocks.getOrDefault(symbol, 0);
        if (currentQuantity >= quantity) {
            stocks.put(symbol, currentQuantity - quantity);
        } else {
            throw new IllegalArgumentException("Not enough shares to sell.");
        }
    }

    public Map<String, Integer> getStocks() {
        return stocks;
    }
}


class Market {
    private Map<String, Stock> stocks;
    private Random random;

    public Market() {
        stocks = new HashMap<>();
        random = new Random();
        // Initialize with some stocks
        stocks.put("AAPL", new Stock("AAPL", 150.00));
        stocks.put("GOOGL", new Stock("GOOGL", 2800.00));
        stocks.put("TSLA", new Stock("TSLA", 700.00));
    }

    public void updateStockPrices() {
        for (Stock stock : stocks.values()) {
            double change = (random.nextDouble() - 0.5) * 2; // Random price change between -1 and 1
            double newPrice = stock.getPrice() + change;
            if (newPrice > 0) {
                stock.setPrice(newPrice);
            }
        }
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public Map<String, Stock> getAllStocks() {
        return stocks;
    }
}









public class TradingPlatform {
    private Market market;
    private Portfolio portfolio;
    private Scanner scanner;

    public TradingPlatform() {
        market = new Market();
        portfolio = new Portfolio();
        scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println();
            System.out.println("1. Buy Stock");
            System.out.println("2. Sell Stock");
            System.out.println("3. View Portfolio");
            System.out.println("4. View Market Data");
            System.out.println("5. Update Market");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    buyStock();
                    break;
                case 2:
                    sellStock();
                    break;
                case 3:
                    viewPortfolio();
                    break;
                case 4:
                    viewMarketData();
                    break;
                case 5:
                    market.updateStockPrices();
                    System.out.println("Market updated.");
                    break;
                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void buyStock() {
        System.out.print("Enter stock symbol: ");
        String symbol = scanner.next();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();

        Stock stock = market.getStock(symbol);
        if (stock != null) {
            portfolio.buyStock(symbol, quantity);
            System.out.println("Bought " + quantity + " shares of " + symbol);
        } else {
            System.out.println("Stock not found.");
        }
    }

    private void sellStock() {
        System.out.print("Enter stock symbol: ");
        String symbol = scanner.next();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();

        try {
            portfolio.sellStock(symbol, quantity);
            System.out.println("Sold " + quantity + " shares of " + symbol);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewPortfolio() {
        System.out.println("Portfolio:");
        for (Map.Entry<String, Integer> entry : portfolio.getStocks().entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();
            double price = market.getStock(symbol).getPrice();
            System.out.println(symbol + ": " + quantity + " shares at $" + price + " each");
        }
    }

    private void viewMarketData() {
        System.out.println("Market Data:");
        for (Stock stock : market.getAllStocks().values()) {
            System.out.println(stock.getSymbol() + ": $" + stock.getPrice());
        }
    }

    public static void main(String[] args) {
        TradingPlatform platform = new TradingPlatform();
        platform.start();
    }
}
