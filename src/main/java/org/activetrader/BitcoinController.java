package org.activetrader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BitcoinController {
    private static final String COINBASE_API_URL = "https://api.coinbase.com/v2/prices/spot?currency=USD";
    private double funds = 100000.0; // Initial funds in USD
    private double bitcoinAmount = 0.0; // Amount of Bitcoin owned
    private double buyPrice = 0.0; // Price at which Bitcoin was bought
    private double averageBuyPrice = 0.0; // Average purchase price of Bitcoin
    private double previousPrice = 0.0;
    private List<String> tradeHistory = new ArrayList<>();
    private double tradeAmountUsd = 100.0;

    @GetMapping("/price")
    public String getBitcoinPrice() {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(COINBASE_API_URL, String.class);
        double currentPrice = Double.parseDouble(parsePriceFromResponse(result));

        // Only set previousPrice if it's not been set yet (first run)
        if (previousPrice == 0.0) {
            previousPrice = currentPrice;
        }

        return String.valueOf(currentPrice);
    }

    @PostMapping("/buy")
    public String buyBitcoin(@RequestBody Investment investment) {
        double currentPrice = Double.parseDouble(getBitcoinPrice());
        if (investment.getAmount() > funds) {
            return "Insufficient funds.";
        }

        double amountBought = investment.getAmount() / currentPrice;
        bitcoinAmount += amountBought;
        funds -= investment.getAmount();

        // Update average buy price
        averageBuyPrice = ((averageBuyPrice * (bitcoinAmount - amountBought)) + (currentPrice * amountBought)) / bitcoinAmount;

        tradeHistory.add("Bought Bitcoin at " + currentPrice + " USD.");
        return "Bought Bitcoin at " + currentPrice + " USD.";
    }

    @PostMapping("/sell")
    public String sellBitcoin() {
        double currentPrice = Double.parseDouble(getBitcoinPrice());
        if (bitcoinAmount == 0) {
            return "No Bitcoin to sell.";
        }

        // Only sell if current price is greater than the average buy price
        if (currentPrice <= averageBuyPrice) {
            return "Current price is not profitable.";
        }

        double sellValue = bitcoinAmount * currentPrice;
        double profitOrLoss = sellValue - (bitcoinAmount * averageBuyPrice);

        funds += sellValue;
        bitcoinAmount = 0;
        averageBuyPrice = 0.0; // Reset average buy price since no Bitcoin is held

        tradeHistory.add("Sold Bitcoin at " + currentPrice + " USD. Profit/Loss: " + profitOrLoss + " USD.");
        return "Sold Bitcoin at " + currentPrice + " USD. Profit/Loss: " + profitOrLoss + " USD.";
    }

    @GetMapping("/status")
    public String getCurrentStatus() {
        double currentPrice = Double.parseDouble(getBitcoinPrice());
        double currentValue = bitcoinAmount * currentPrice;
        double profitOrLoss = currentValue - (bitcoinAmount * averageBuyPrice);

        JsonObject status = new JsonObject();
        status.addProperty("bitcoinAmount", bitcoinAmount);
        status.addProperty("currentValue", currentValue);
        status.addProperty("profitOrLoss", profitOrLoss);
        status.addProperty("funds", funds);
        status.addProperty("currentPrice", currentPrice);

        return status.toString();
    }

    @PostMapping("/autoTrade")
    public String autoTrade() {
        double currentPrice = Double.parseDouble(getBitcoinPrice());
        String action = "No action";

        if (currentPrice > previousPrice && currentPrice > averageBuyPrice) {
            // Sell all holdings
            if (bitcoinAmount > 0) {
                double sellValue = bitcoinAmount * currentPrice;
                double profitOrLoss = sellValue - (bitcoinAmount * averageBuyPrice);
                funds += sellValue;
                bitcoinAmount = 0;
                averageBuyPrice = 0.0; // Reset average buy price since no Bitcoin is held
                action = Instant.now()+ " Auto Trade: Sold Bitcoin at " + currentPrice + " USD. Profit/Loss: " + profitOrLoss + " USD.";
                tradeHistory.add(action);
            }
        } else if (currentPrice < previousPrice && funds >= tradeAmountUsd) {
            // Buy a portion
            double amountToBuy = tradeAmountUsd / currentPrice;
            bitcoinAmount += amountToBuy;
            funds -= tradeAmountUsd;

            // Update average buy price
            averageBuyPrice = ((averageBuyPrice * (bitcoinAmount - amountToBuy)) + (currentPrice * amountToBuy)) / bitcoinAmount;

            action = Instant.now()+ " Auto Trade: Bought"+ "worth of $"+ tradeAmountUsd + " Bitcoin at " + currentPrice + " USD.";
            tradeHistory.add(action);
        }

        previousPrice = currentPrice; // Update previous price
        return action;
    }

    @GetMapping("/tradeHistory")
    public List<String> getTradeHistory() {
        return tradeHistory;
    }

    private String parsePriceFromResponse(String jsonResponse) {
        JsonElement jsonElement = JsonParser.parseString(jsonResponse);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject dataObject = jsonObject.getAsJsonObject("data");
        return dataObject.get("amount").getAsString();
    }
}

class Investment {
    private double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
