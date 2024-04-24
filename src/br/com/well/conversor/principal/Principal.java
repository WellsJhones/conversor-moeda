package br.com.well.conversor.principal;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitura = new Scanner(System.in);

//        System.out.println("Digite o Cep : ");
//        var cep = leitura.nextLine();

        String  endereco = String.format("https://v6.exchangerate-api.com/v6/c3fcff854bb89b5eeaf7fd02/latest/USD");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();

        HttpResponse<String> response= HttpClient.newHttpClient().send(request,HttpResponse.BodyHandlers.ofString());
        String json = response.body();
        System.out.println(json);
    }
}
