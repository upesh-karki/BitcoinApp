Bitcoin Trading Application Documentation
Video navigating how to use it: https://www.youtube.com/watch?v=z-97F7_w6sE

Overview
This Bitcoin trading application allows users to buy, sell, and automatically trade Bitcoin based on current market
prices. The application provides real-time price updates, maintains trade history, and manages the user's funds.

Features
Real-Time Price Update: Fetches the current Bitcoin price from Coinbase.

Manual Trading:
Buy Bitcoin: Allows users to buy Bitcoin with specified USD amounts.
Sell Bitcoin: Allows users to sell all Bitcoin holdings at the current price.
Auto Trading: Automatically executes buy or sell orders based on price changes.
Trade History: Displays a history of all buy and sell transactions.
Status Dashboard: Shows the current amount of Bitcoin, total funds, current Bitcoin value, and profit or loss.

How to Use

User Interface
The web interface includes the following elements:

Current Bitcoin Price: Displays the latest Bitcoin price in USD.
Bitcoin Amount: Shows the amount of Bitcoin currently owned.
Current Value: Displays the current value of Bitcoin holdings.
Profit or Loss: Shows the profit or loss based on the average buy price.
Funds: Displays the remaining USD funds.
Investment Amount: Input field for the amount of USD to invest in Bitcoin.
Buy Button: Executes a buy order with the specified investment amount.
Sell Button: Sells all current Bitcoin holdings.
Auto Trade Button: Starts the auto-trading process.
Trade History: Displays the list of all executed buy and sell orders.

Steps to Operate
Fetching the Current Price:

The current price of Bitcoin is displayed and updated automatically.
Buying Bitcoin:

Enter the amount in USD in the "Investment Amount" field.
Click the "Buy" button to purchase Bitcoin with the specified amount.
Selling Bitcoin:

Click the "Sell" button to sell all Bitcoin holdings at the current price.
Auto Trading:

Click the "Auto Trade" button to enable automatic trading based on price changes.
The system will buy Bitcoin if the price drops and sell Bitcoin if the price rises above the average buy price.
Viewing Trade History:

The trade history is updated with each buy or sell order and is displayed in the "Trade History" section.


Flow Diagram
Here is a textual flow representation of the application's processes:


Start
|
|-- Fetch Current Price
|       |
|       V
|-- Display Price on Dashboard
|       |
|       V
|-- User Action: Buy Bitcoin
|       |
|       |-- Input Investment Amount
|       |-- Click Buy Button
|       |
|       V
|-- Buy Bitcoin at Current Price
|       |
|       V
|-- Update Bitcoin Amount and Funds
|       |
|       V
|-- Record Trade in History
|
|-- User Action: Sell Bitcoin
|       |
|       |-- Click Sell Button
|       |
|       V
|-- Sell All Bitcoin at Current Price
|       |
|       V
|-- Update Bitcoin Amount and Funds
|       |
|       V
|-- Record Trade in History
|
|-- User Action: Enable Auto Trade
|       |
|       |-- Click Auto Trade Button
|       |
|       V
|-- Start Auto Trade Loop
|
|-- Fetch Current Price
|       |
|       V
|-- Compare with Previous Price
|       |
|       |-- If Current Price > Previous Price and > Average Buy Price
|       |       |
|       |       V
|       |-- Sell All Bitcoin
|       |
|       |-- If Current Price < Previous Price
|       |       |
|       |       V
|       |-- Buy Bitcoin with Trade Amount USD
|       |
|       V
|-- Update Previous Price
|
V
Repeat Fetch Current Price


Technical Details
Endpoints
GET /price: Fetches the current Bitcoin price from Coinbase.
POST /buy: Buys Bitcoin with a specified amount in USD.
POST /sell: Sells all current Bitcoin holdings.
POST /autoTrade: Enables the auto-trading function.
GET /status: Returns the current status, including Bitcoin amount, funds, current value, and profit/loss.
GET /tradeHistory: Retrieves the history of all trades.


Code Snippets

Fetching Bitcoin Price:

@GetMapping("/price")
public String getBitcoinPrice() {
RestTemplate restTemplate = new RestTemplate();
String result = restTemplate.getForObject(COINBASE_API_URL, String.class);
double currentPrice = Double.parseDouble(parsePriceFromResponse(result));
if (previousPrice == 0.0) {
previousPrice = currentPrice;
}
return String.valueOf(currentPrice);
}


Buying Bitcoin:

@PostMapping("/buy")
public String buyBitcoin(@RequestBody Investment investment) {
double currentPrice = Double.parseDouble(getBitcoinPrice());
if (investment.getAmount() > funds) {
return "Insufficient funds.";
}
double amountBought = investment.getAmount() / currentPrice;
bitcoinAmount += amountBought;
funds -= investment.getAmount();
averageBuyPrice = ((averageBuyPrice * (bitcoinAmount - amountBought)) + (currentPrice * amountBought)) / bitcoinAmount;
tradeHistory.add("Bought Bitcoin at " + currentPrice + " USD.");
return "Bought Bitcoin at " + currentPrice + " USD.";
}

Selling Bitcoin:

@PostMapping("/sell")
public String sellBitcoin() {
double currentPrice = Double.parseDouble(getBitcoinPrice());
if (bitcoinAmount == 0) {
return "No Bitcoin to sell.";
}
if (currentPrice <= averageBuyPrice) {
return "Current price is not profitable.";
}
double sellValue = bitcoinAmount * currentPrice;
double profitOrLoss = sellValue - (bitcoinAmount * averageBuyPrice);
funds += sellValue;
bitcoinAmount = 0;
averageBuyPrice = 0.0;
tradeHistory.add("Sold Bitcoin at " + currentPrice + " USD. Profit/Loss: " + profitOrLoss + " USD.");
return "Sold Bitcoin at " + currentPrice + " USD. Profit/Loss: " + profitOrLoss + " USD.";
}

Auto Trading:

@PostMapping("/autoTrade")
public String autoTrade() {
double currentPrice = Double.parseDouble(getBitcoinPrice());
String action = "No action";
if (currentPrice > previousPrice && currentPrice > averageBuyPrice) {
if (bitcoinAmount > 0) {
double sellValue = bitcoinAmount * currentPrice;
double profitOrLoss = sellValue - (bitcoinAmount * averageBuyPrice);
funds += sellValue;
bitcoinAmount = 0;
averageBuyPrice = 0.0;
action = "Auto Trade: Sold Bitcoin at " + currentPrice + " USD. Profit/Loss: " + profitOrLoss + " USD.";
tradeHistory.add(action);
}
} else if (currentPrice < previousPrice && funds >= tradeAmountUsd) {
double amountToBuy = tradeAmountUsd / currentPrice;
bitcoinAmount += amountToBuy;
funds -= tradeAmountUsd;
averageBuyPrice = ((averageBuyPrice * (bitcoinAmount - amountToBuy)) + (currentPrice * amountToBuy)) / bitcoinAmount;
action = "Auto Trade: Bought Bitcoin at " + currentPrice + " USD.";
tradeHistory.add(action);
}
previousPrice = currentPrice;
return action;
}


By following this documentation, users should be able to effectively operate the Bitcoin trading application,
understand its features, and leverage its auto-trading capabilities to manage their Bitcoin investments.






