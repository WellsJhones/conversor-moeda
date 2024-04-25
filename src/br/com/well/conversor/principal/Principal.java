package br.com.well.conversor.principal;

import br.com.well.conversor.principal.module.WriteFiles;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Principal {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        String endereco = String.format("https://v6.exchangerate-api.com/v6/(your-key-here)/latest/USD");

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
        List<String> currencies = new ArrayList();

        // Display the currencies to the user
        for (int i = 0;  i < 25; i++) {
            System.out.println(String.format("%d - %s",i+1, conversionRates.keySet().toArray()[i]));
            currencies.add(conversionRates.keySet().toArray()[i].toString());

        }

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

        System.out.println(String.format("amount: %.2f %s is equivalent to %.2f %s", amount, currencies.get(firstCurrencyIndex),
                amountInSecondCurrency, currencies.get(secondCurrencyIndex)));

    }

}