package com.company;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;

public class Server {
    public void LodowkaServerRun() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/lodowka", new MyHandler());
        server.createContext("/zjedz", new ZjedzHandler());
        server.createContext("/produkty", new ListaProduktowHandler());
        server.createContext("/kup", new SklepHandler());
        server.createContext("/nowy-produkt", new ProduktyHandler());
        server.setExecutor(null);
        server.start();
    }

}

