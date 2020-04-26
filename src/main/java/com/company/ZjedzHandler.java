package com.company;
import java.io.*;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;


public class ZjedzHandler implements HttpHandler{
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
            Lodowka lodowka = new Lodowka();
            lodowka.Zjedz(jsonObj.getInt("id"), jsonObj.getInt("ilosc"));
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
