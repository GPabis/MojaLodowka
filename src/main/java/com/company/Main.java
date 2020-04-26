package com.company;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Main extends Produkty{

    public static void main(String[] args) throws Exception{
        Server server = new Server();
//        ListaIstniejacychProduktow lista = new ListaIstniejacychProduktow();
//        lista.DodajDoListyIstniejacychProduktow();
//        Sklep sklep = new Sklep();
//        sklep.Zakupy(lista);
//        sklep.DodajZakupyDoBazyDanych();

        Lodowka lodowka = new Lodowka();
        lodowka.PobieraniePordoktowWLodowce();
        server.LodowkaServerRun();




    }
}
