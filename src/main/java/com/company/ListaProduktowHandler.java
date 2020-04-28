package com.company;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
public class ListaProduktowHandler implements HttpHandler{

    @Override
    public void handle(HttpExchange t) throws IOException {
        ListaIstniejacychProduktow lista = new ListaIstniejacychProduktow();
        lista.PobieranieProduktuZBazyDanych();
        String produktyJSON = lista.ProduktyJSON().toString();
        String encoding = "UTF-8";
        t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        t.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        t.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        t.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");
        t.getResponseHeaders().add("Access-Control-Allow-Credentials-Header", "*");
        t.getResponseHeaders().set("Content-Type", "text/html; charset=" + encoding);
        t.sendResponseHeaders(200, produktyJSON.getBytes().length);
        OutputStream os = t.getResponseBody();
        os.write(produktyJSON.getBytes());
        os.close();
    }
}
