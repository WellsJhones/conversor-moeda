package br.com.well.conversor.principal;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Principal {
    public static void main(String[] args) throws IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);

        String endereco = String.format("https://v6.exchangerate-api.com/v6/(your-api-key)/latest/USD");

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

        System.out.println("Please select an option:");
        System.out.println("1 - Retrieve from history");
        System.out.println("2 - Make a new simulation");

        int option = scanner.nextInt();

//        int firstCurrencyIndex = 0;
//        int secondCurrencyIndex = 0;
        double amount = 0;

        int firstCurrencyIndex = 0;
        int secondCurrencyIndex = 0;
        double amountInSecondCurrency = 0;
        String finalResult = null;
        switch (option) {
            case 1:
                // Call the method to retrieve from history
                List<String> history = getHistory();
                for (String record : history) {
                    System.out.println(record);
                }
                break;
            case 2:
                // Existing code for new simulation...
                for (int i = 0; i < 25; i++) {
                    System.out.println(String.format("%d - %s", i + 1, conversionRates.keySet().toArray()[i]));
                    currencies.add(conversionRates.keySet().toArray()[i].toString());

                }

                System.out.println("Enter the number of the first currency:");
                firstCurrencyIndex = scanner.nextInt() - 1;
                if (firstCurrencyIndex < 0 || firstCurrencyIndex >= currencies.size()) {
                    System.out.println("Invalid currency index");
                    return;
                }

                System.out.println("Enter the number of the second currency:");
                secondCurrencyIndex = scanner.nextInt() - 1;
                if (secondCurrencyIndex < 0 || secondCurrencyIndex >= currencies.size()) {
                    System.out.println("Invalid currency index");
                    return;
                }

                System.out.println("Enter the amount to convert:");
                amount = scanner.nextDouble();
                if (amount < 0) {
                    System.out.println("Invalid amount");
                    return;
                }
                // Display the currencies to the user

//         Get the conversion rates


                double firstCurrencyToUsdRate = conversionRates.get(currencies.get(firstCurrencyIndex)).getAsDouble();
                double usdToSecondCurrencyRate = conversionRates.get(currencies.get(secondCurrencyIndex)).getAsDouble();
                                // Convert the amount to the second currency
                amountInSecondCurrency = (amount / firstCurrencyToUsdRate) * usdToSecondCurrencyRate;
                finalResult = String.format("amount: %.2f %s is equivalent to %.2f %s", amount, currencies.get(firstCurrencyIndex),
                        amountInSecondCurrency, currencies.get(secondCurrencyIndex));


                break;
            default:
                System.out.println("Invalid option");
                break;
        }
        if (currencies.isEmpty()) {
            System.out.println("Have a good day!");
            return;
        }

        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Create the log entry
        String logEntry = String.format("%s: Converted %.2f %s to %.2f %s",
                now.format(formatter),
                amount,
                currencies.get(firstCurrencyIndex),
                amountInSecondCurrency,
                currencies.get(secondCurrencyIndex)
        );

        // Write the log entry to the "History.txt" file
        FileWriter file = new FileWriter("History.txt", true); // Append to the file
        file.write(logEntry + "\n");
        file.close();
        System.out.println(finalResult);


    }

    private static List<String> getHistory() {
        List<String> history = new ArrayList<>();
        java.io.File historyFile = new java.io.File("History.txt");

        if (!historyFile.exists()) {
            System.out.println("No history available.");
            return history;
        }

        try {
            Scanner file = new Scanner(historyFile);
            while (file.hasNextLine()) {
                history.add(file.nextLine());
            }
            file.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return history;
    }
}