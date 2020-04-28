package com.company;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SklepHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        try {
            StringBuilder sb = new StringBuilder();
            InputStream ios = t.getRequestBody();
            int i;
            while ((i = ios.read()) != -1) {
                sb.append((char) i);
            }
            JSONObject jsonObj = new JSONObject(sb.toString());
            JSONArray jsonArr = jsonObj.getJSONArray("koszyk");
            System.out.println(jsonArr.length());
            Sklep sklep = new Sklep();
            ListaIstniejacychProduktow lista = new ListaIstniejacychProduktow();
            lista.PobieranieProduktuZBazyDanych();
            for (i = 0; i<jsonArr.length(); i++){
                JSONObject produktWKoszykuJson = jsonArr.getJSONObject(i);
                for (int j = 0; j < produktWKoszykuJson.getInt("ilosc"); j++){
                    sklep.Zakupy(lista, produktWKoszykuJson.getInt("id"));
                }
            }
            sklep.DodajZakupyDoBazyDanych();
            String response = "Koniec";
            t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            t.sendResponseHeaders(200,response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
