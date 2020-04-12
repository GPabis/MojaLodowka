package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


public class ListaIstniejacychProduktow extends Sklep implements IConnectable{
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
        Connection con = IConnectable.connectDefault();
        Statement stmt = null;
        ResultSet res = null;
        Produkty pobranyProdukt = null;
        try{
            stmt = con.createStatement();
            res = stmt.executeQuery(zapytanieSelectProdukt);
            if (res.next()){
                Produkty.Kategoria kategoria = Produkty.Kategoria.valueOf(res.getString(4));
                pobranyProdukt = new Produkty(res.getString(2),res.getString(3), kategoria,res.getDouble(5), res.getInt(6));
                pobranyProdukt.produktID = res.getInt(1);
            }
        }
        catch (Exception e){System.out.println(e);}finally {
            IConnectable.ClosingConnection(con, null, stmt, res);
        }
        return pobranyProdukt;
    }

    public int PobieranieRozmiaruTabeli(){

        /**
         * Method is using for getting numbers of rows from Produkty table
         *
         * @return              int - number of rows it table Produkty
         */
        Connection con = IConnectable.connectDefault();
        Statement stmt = null;
        ResultSet res = null;
        int rozmiarTabeli = 0;
        try {
            stmt = con.createStatement();
            res = stmt.executeQuery("SELECT COUNT(1) FROM Produkty");
            if (res.next()){
                rozmiarTabeli = res.getInt(1);
            }
            System.out.println("Tabela Produkty Jest Pusta");
        }
        catch (Exception e){System.out.println(e);}finally {
            IConnectable.ClosingConnection(con, null, stmt, res);
        }
        if(rozmiarTabeli==0)System.out.println("Tabela Produkty Jest Pusta");
        return rozmiarTabeli;
    }

    public int PobieranieIDPierwszegoProduktu(){
        Connection con = IConnectable.connectDefault();
        Statement stmt = null;
        ResultSet res = null;
        int numerPierwszegoIndexu = 0;
        try {
            stmt = con.createStatement();
            res = stmt.executeQuery("SELECT IDproduktu FROM Produkty");
            if (res.first()){
                numerPierwszegoIndexu = res.getInt(1);
            }
        }
        catch (Exception e){System.out.println(e);}finally {
            IConnectable.ClosingConnection(con, null, stmt, res);
        }
        if (numerPierwszegoIndexu==0) System.out.println("Tabela Produkty Jest Pusta");
        return numerPierwszegoIndexu;
    }

    public int PobieranieIDOstatniegoProduktu(){
        Connection con = IConnectable.connectDefault();
        Statement stmt = null;
        ResultSet res = null;
        int numerOstatniegoIndexu = 0;
        try {
            stmt = con.createStatement();
            res = stmt.executeQuery("SELECT IDproduktu FROM Produkty");
            if (res.last()){
                numerOstatniegoIndexu = res.getInt(1);
            }
            System.out.println("Tabela Produkty Jest Pusta");
        }
        catch (Exception e){System.out.println(e);}finally {
            IConnectable.ClosingConnection(con, null,stmt,res);
        }
        if (numerOstatniegoIndexu ==0)System.out.println("Tabela Produkty Jest Pusta");
        return numerOstatniegoIndexu;
    }
}
