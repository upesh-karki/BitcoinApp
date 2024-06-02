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
//
//public class CoinbaseApiClient {
//
//    private static final String COINBASE_API_URL = "https://api.coinbase.com/v2/prices/spot?currency=USD";
//    private static final int INTERVAL_MS = 1; // 60 seconds
//
//    public static void main(String[] args) {
//        while (true) {
//            try {
//                String response = getBitcoinPrice();
//                System.out.println("Bitcoin Price (USD): " + response);
//                Thread.sleep(INTERVAL_MS);
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
//}
