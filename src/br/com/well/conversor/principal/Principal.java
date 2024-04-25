package br.com.well.conversor.principal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        String endereco = String.format("https://v6.exchangerate-api.com/v6/(your_api_key_here)/latest/USD");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        String json = response.body();

        // Parse the JSON response using Gson
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);

        // Access the conversion_rates object
        JsonObject conversionRates = jsonObject.getAsJsonObject("conversion_rates");

        // Define the 10 most used currencies
        List<String> currencies = Arrays.asList("USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD");

        // Display the currencies to the user
        System.out.println("Select two currencies and an amount to convert:");
        for (int i = 0; i < currencies.size(); i++) {
            System.out.println((i + 1) + ". " + currencies.get(i));
        }

        // Get the user's choices
        System.out.println("Enter the number of the first currency:");
        int firstCurrencyIndex = scanner.nextInt() - 1;
        System.out.println("Enter the number of the second currency:");
        int secondCurrencyIndex = scanner.nextInt() - 1;
        System.out.println("Enter the amount to convert:");
        double amount = scanner.nextDouble();

        // Get the conversion rates
        double firstCurrencyToUsdRate = conversionRates.get(currencies.get(firstCurrencyIndex)).getAsDouble();
        double usdToSecondCurrencyRate = conversionRates.get(currencies.get(secondCurrencyIndex)).getAsDouble();

        // Convert the amount to the second currency
        double amountInSecondCurrency = (amount / firstCurrencyToUsdRate) * usdToSecondCurrencyRate;

        System.out.println(amount + " " + currencies.get(firstCurrencyIndex) + " is equivalent to " + amountInSecondCurrency + " " + currencies.get(secondCurrencyIndex));
    }
}