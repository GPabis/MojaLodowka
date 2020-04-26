package com.company;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.*;
import java.net.URLDecoder;

public class ProduktyHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        try {
            StringBuilder sb = new StringBuilder();
            InputStreamReader ios = new InputStreamReader(t.getRequestBody(), "utf-8");
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            String encoding = "UTF-8";
            t.getResponseHeaders().set("Content-Type", "text/plain; charset=" + encoding);
            int i;
            while ((i = ios.read()) != -1) {
                sb.append((char) i);
            }
            JSONObject jsonObj = new JSONObject(sb.toString());
            Produkty produkt = new Produkty(
                    jsonObj.getString("produkt"),
                    jsonObj.getString("marka"),
                    Produkty.Kategoria.valueOf(jsonObj.getString("kategoria")),
                    jsonObj.getDouble("cena"),
                    jsonObj.getInt("iloscDniWaznosci")
            );
            System.out.println(produkt.toString());
            produkt.DodajProduktDoBazyProduktow();
            String response = "Koniec";
            t.sendResponseHeaders(200,response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
