package com.company;

import java.util.ArrayList;
import java.util.List;

public class Main extends Produkty {

    public static void main(String[] args) {
        ListaIstniejacychProduktow lista1 = new ListaIstniejacychProduktow();
        lista1.DodajDoListyIstniejacychProduktow();
        Sklep sklep1 = new Sklep();
        Lodowka lodowka1 = new Lodowka();
        lodowka1.PobieraniePordoktowWLodowce();
        System.out.println(lodowka1);
        lodowka1.Zjedz(12,5);
        lodowka1.PobieraniePordoktowWLodowce();
        System.out.println(lodowka1);
    }
}
