//package org.activetrader;
//
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Scanner;
//
//public class CoinbaseApiClientTrading {
//
//    private static final String COINBASE_API_URL = "https://api.coinbase.com/v2/prices/spot?currency=USD";
//    private static final int INTERVAL_MS = 10000; // 10 seconds
//    private static double funds = 10000.0; // Initial funds in USD
//    private static double bitcoinAmount = 0.0; // Amount of Bitcoin owned
//    private static double buyPrice = 0.0; // Price at which Bitcoin was bought
//    private static boolean isRunning = true;
//
//    public static void main(String[] args) {
//        Thread priceUpdater = new Thread(() -> {
//            while (isRunning) {
//                try {
//                    String response = getBitcoinPrice();
//                    double currentPrice = Double.parseDouble(response);
//                    clearConsole();
//                    displayCurrentState(currentPrice);
//                    Thread.sleep(INTERVAL_MS);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        priceUpdater.start();
//
//        Scanner scanner = new Scanner(System.in);
//        while (isRunning) {
//            try {
//                String response = getBitcoinPrice();
//                double currentPrice = Double.parseDouble(response);
//
//                displayMenu();
//                int choice = scanner.nextInt();
//
//                switch (choice) {
//                    case 1:
//                        buyBitcoin(currentPrice);
//                        break;
//                    case 2:
//                        sellBitcoin(currentPrice);
//                        break;
//                    case 3:
//                        isRunning = false;
//                        System.out.println("Exiting...");
//                        priceUpdater.interrupt();
//                        return;
//                    default:
//                        System.out.println("Invalid choice. Please select again.");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private static String getBitcoinPrice() throws Exception {
//        URL url = new URL(COINBASE_API_URL);
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Accept", "application/json");
//
//        if (conn.getResponseCode() != 200) {
//            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
//        }
//
//        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//        StringBuilder response = new StringBuilder();
//        String output;
//        while ((output = br.readLine()) != null) {
//            response.append(output);
//        }
//        conn.disconnect();
//
//        return parsePriceFromResponse(response.toString());
//    }
//
//    private static String parsePriceFromResponse(String jsonResponse) {
//        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
//        JsonObject jsonObject = jsonElement.getAsJsonObject();
//        JsonObject dataObject = jsonObject.getAsJsonObject("data");
//        return dataObject.get("amount").getAsString();
//    }
//
//    private static void displayMenu() {
//        System.out.println("1. Buy Bitcoin");
//        System.out.println("2. Sell Bitcoin");
//        System.out.println("3. Exit");
//        System.out.print("Enter your choice: ");
//    }
//
//    private static void buyBitcoin(double currentPrice) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter amount to invest in USD: ");
//        double investment = scanner.nextDouble();
//
//        if (investment > funds) {
//            System.out.println("Insufficient funds.");
//            return;
//        }
//
//        bitcoinAmount += investment / currentPrice;
//        funds -= investment;
//        buyPrice = currentPrice;
//
//        System.out.println("Bought Bitcoin at " + currentPrice + " USD.");
//    }
//
//    private static void sellBitcoin(double currentPrice) {
//        if (bitcoinAmount == 0) {
//            System.out.println("No Bitcoin to sell.");
//            return;
//        }
//
//        double sellValue = bitcoinAmount * currentPrice;
//        double profitOrLoss = sellValue - (bitcoinAmount * buyPrice);
//
//        funds += sellValue;
//        bitcoinAmount = 0;
//
//        System.out.println("Sold Bitcoin at " + currentPrice + " USD.");
//        System.out.println("Profit/Loss: " + profitOrLoss + " USD.");
//    }
//
//    private static void displayCurrentState(double currentPrice) {
//        System.out.println("Bitcoin Price (USD): " + currentPrice);
//        displayCurrentPosition(currentPrice);
//    }
//
//    private static void displayCurrentPosition(double currentPrice) {
//        double currentValue = bitcoinAmount * currentPrice;
//        double profitOrLoss = currentValue - (bitcoinAmount * buyPrice);
//
//        System.out.println("\n--- Current Position ---");
//        System.out.println("Bitcoin amount: " + bitcoinAmount);
//        System.out.println("Current value: " + currentValue + " USD");
//        System.out.println("Profit/Loss: " + profitOrLoss + " USD");
//        System.out.println("Available funds: " + funds + " USD");
//        System.out.println("------------------------\n");
//    }
//
//    private static void clearConsole() {
//        try {
//            if (System.getProperty("os.name").contains("Windows")) {
//                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//            } else {
//                System.out.print("\033[H\033[2J");
//                System.out.flush();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
