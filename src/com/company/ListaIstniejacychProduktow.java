package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class ListaIstniejacychProduktow extends Sklep{
    private List<Produkty> listaIstniejacychProduktow;

    public ListaIstniejacychProduktow(){
        /**
         * Constructior of Class ListaIstniejacychProduktow containing list of all existing products
         */
        this.listaIstniejacychProduktow = new ArrayList<>();
    }


    public void DodajDoListyIstniejacychProduktow(){
        /**
         * DodajDoListyIstniejacychProduktow is adding specific product to listaIstniejacychProduktow list.
         * @param produkt       adding product object
         */
        for (int i = PobieranieIDPierwszegoProduktu(); i <= PobieranieIDOstatniegoProduktu(); i++){
            Produkty dodawanyProdukt = PobieranieProduktuZBazyDanych(i);
            if (dodawanyProdukt != null) this.listaIstniejacychProduktow.add(dodawanyProdukt);
        }
    }

    public String toString(){
        /**
         * toString method that shows to users list of already created products form Database and Produkt objects.
         * @return          String of list of existing products.
         */
        StringBuilder sb = new StringBuilder();
        sb.append("Lista Produktow: \n");
        for (int i = 0; i<listaIstniejacychProduktow.size(); i++){
            sb.append(listaIstniejacychProduktow.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public Produkty WybierzProdukt(int idProduktu){
        /**
         * Method that select specific product form a list of exisiting products,
         * based on @param productID of specific product.
         * If @param productID don't match, then appears IndexOutOfBoundsException
         * @param idProduktu        it should contain @param produktID
         * @return                  Object of a specific product
         */
        for (int i = 0; i<listaIstniejacychProduktow.size(); i++){
            if (listaIstniejacychProduktow.get(i).produktID==idProduktu) return listaIstniejacychProduktow.get(i);
        }
        throw new IndexOutOfBoundsException("Nie ma produktu od takim ID!");
    }


    public Produkty PobieranieProduktuZBazyDanych(int IDproduktu){

        /**
         * Method is using for geting specific product from database.
         * @param IDproduktu        id of product that we wanna get.
         * @return                  returning Product object.
         */

        String zapytanieSelectProdukt = "SELECT * FROM Produkty WHERE IDproduktu = " + IDproduktu;
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://85.194.242.107:3306/m11794_BazaLodowka", "m11794_GPabis", "PgDrawrof97");
            Statement stmt = con.createStatement();
            ResultSet query = stmt.executeQuery(zapytanieSelectProdukt);
            if (query.next()){
                Produkty.Kategoria kategoria = Produkty.Kategoria.valueOf(query.getString(4));
                Produkty pobranyProdukt = new Produkty(query.getString(2),query.getString(3), kategoria,query.getDouble(5), query.getInt(6));
                pobranyProdukt.produktID = query.getInt(1);
                return pobranyProdukt;
            }
            return null;
        }
        catch (Exception e){System.out.println(e);}
        return null;
    }

    public int PobieranieRozmiaruTabeli(){

        /**
         * Method is using for getting numbers of rows from Produkty table
         *
         * @return              int - number of rows it table Produkty
         */

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://85.194.242.107:3306/m11794_BazaLodowka", "m11794_GPabis", "PgDrawrof97");
            Statement stmt = con.createStatement();
            ResultSet query = stmt.executeQuery("SELECT COUNT(1) FROM Produkty");
            if (query.next()){
                int rozmiarTabeli = query.getInt(1);
                return rozmiarTabeli;
            }
            System.out.println("Tabela Produkty Jest Pusta");
            return 0;
        }
        catch (Exception e){System.out.println(e);}
        throw new IndexOutOfBoundsException();
    }

    public int PobieranieIDPierwszegoProduktu(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://85.194.242.107:3306/m11794_BazaLodowka", "m11794_GPabis", "PgDrawrof97");
            Statement stmt = con.createStatement();
            ResultSet query = stmt.executeQuery("SELECT IDproduktu FROM Produkty");
            if (query.first()){
                int numerPierwszegoIndexu = query.getInt(1);
                return numerPierwszegoIndexu;
            }
            System.out.println("Tabela Produkty Jest Pusta");
            return 0;
        }
        catch (Exception e){System.out.println(e);}
        throw new IndexOutOfBoundsException();
    }

    public int PobieranieIDOstatniegoProduktu(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://85.194.242.107:3306/m11794_BazaLodowka", "m11794_GPabis", "PgDrawrof97");
            Statement stmt = con.createStatement();
            ResultSet query = stmt.executeQuery("SELECT IDproduktu FROM Produkty");
            if (query.last()){
                int numerOstatniegoIndexu = query.getInt(1);
                return numerOstatniegoIndexu;
            }
            System.out.println("Tabela Produkty Jest Pusta");
            return 0;
        }
        catch (Exception e){System.out.println(e);}
        throw new IndexOutOfBoundsException();
    }
}
