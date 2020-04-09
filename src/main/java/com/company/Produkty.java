package com.company;

import java.sql.*;

public class Produkty extends ListaIstniejacychProduktow{
    public String produkt;
    public String marka;
    public static int produktIDnumeracja = 100;
    public enum Kategoria {
        MiesoWedliny,
        RybyOwoceMorza,
        Pieczywo,
        SeryJogurtyMleczne,
        DaniaGotoweISosy,
        SłodyczeIPrzekaski,
        Przetwory,
        Napoje,
        Sypkie,
        Przyprawy,
        KawaIHerbata,
        WarzywaIOwoce,
        Alkohole
    }
    public Kategoria przypisanaKategoria;
    public double cena;
    public int produktID;
    public int iloscDniNimSiePrzeterminuje;

    /**
     * Auto increment of ID of each created product
     */
    {
        produktIDnumeracja +=1;
    }

    static {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://85.194.242.107:3306/m11794_BazaLodowka", "m11794_GPabis", "HaslO2020");
            Statement stmt = con.createStatement();
            String tworzenieTabeliProdukty = "CREATE TABLE IF NOT EXISTS Produkty" +
                    "(IDproduktu INT(3) NOT NULL AUTO_INCREMENT," +
                    "Produkt VARCHAR(35) NOT NULL," +
                    "Marka VARCHAR(35) NOT NULL," +
                    "Kategoria ENUM('MiesoWedliny','RybyOwoceMorza', 'Pieczywo', 'SeryJogurtyMleczne','DaniaGotoweISosy','SłodyczeIPrzekaski','Przetwory','Napoje','Sypkie','Przyprawy','KawaIHerbata','WarzywaIOwoce','Alkohole') NOT NULL," +
                    "Cena DOUBLE(5, 2) NOT NULL," +
                    "IloscDniWaznosci int(4) NOT NULL," +
                    "PRIMARY KEY(IDproduktu))";
            stmt.executeUpdate(tworzenieTabeliProdukty);
            System.out.println("Tabela Produkty Gotowa");
        }
        catch (Exception e){System.out.println(e);}
    }

    public Produkty(){}

    public Produkty(String produkt, String marka, Kategoria kategoria, double cena, int iloscDniNimSiePrzeterminuje){
        /**
         * Product Class creates and contains specific informations about product
         * on which we will be operate.
         * @param produkt       Name of product
         * @param marka         Brand of specific product
         * @param produktIDnumeracja        iteration of products
         * @param przypisanaKategoria       product category
         * @param cena                      price of product
         * @param produktID                 ID of product
         * @param iloscDniNimSiePrzeterminuje       amount of day after product will expire
         */
        this.produkt = produkt;
        this.marka = marka;
        this.przypisanaKategoria = kategoria;
        this.cena = cena;
        this.produktID = this.produktIDnumeracja;
        this.iloscDniNimSiePrzeterminuje = iloscDniNimSiePrzeterminuje;
    }

    /**
     * toString method that returns all information of specific product
     * @return
     */

    @Override
    public String toString(){
        return "ID: "+ produktID + " Produkt: " + produkt + " Marka: " + marka + " Kategoria: " + przypisanaKategoria + " Cena: " + cena + "ZŁ";
    }

    public void ZmienProdukt(String produkt, String marka, Kategoria kategoria, double cena, int iloscDniNimSiePrzeterminuje){
        this.produkt = produkt;
        this.marka = marka;
        this.przypisanaKategoria = kategoria;
        this.cena = cena;
        this.produktID = this.produktIDnumeracja;
        this.iloscDniNimSiePrzeterminuje = iloscDniNimSiePrzeterminuje;
    }

    public void DodajProduktDoBazyProduktow(){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://85.194.242.107:3306/m11794_BazaLodowka", "m11794_GPabis", "HaslO2020");
            Statement stm = con.createStatement();
            ResultSet res = stm.executeQuery("SELECT * FROM Produkty");
            while (res.next()){
                if (res.getString("Produkt").equalsIgnoreCase(this.produkt) && res.getString("Marka").equalsIgnoreCase(this.marka)){
                    throw new SQLException("Jest juz "+this.produkt + " Marki " + this.marka);
                }
            }
            PreparedStatement stmt = con.prepareStatement("insert into Produkty values(default ,?,?,?,?,?)");
            stmt.setString(1, this.produkt);
            stmt.setString(2, this.marka);
            stmt.setString(3, this.przypisanaKategoria.name());
            stmt.setDouble(4, this.cena);
            stmt.setInt(5, this.iloscDniNimSiePrzeterminuje);
            stmt.executeUpdate();
        }
        catch (Exception e){System.out.println(e);}
    }

    public void ZmienProduktIDodajDoBazyProdoktow(String produkt, String marka, Kategoria kategoria, double cena, int iloscDniNimSiePrzeterminuje){
        ZmienProdukt(produkt, marka, kategoria, cena, iloscDniNimSiePrzeterminuje);
        DodajProduktDoBazyProduktow();
    }
}
