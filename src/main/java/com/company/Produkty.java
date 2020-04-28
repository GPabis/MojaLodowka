package com.company;
import java.sql.*;

public class Produkty extends ListaIstniejacychProduktow implements IConnectable{
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
            Connection con = IConnectable.connectDefault();
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
        Connection con = IConnectable.connectDefault();
        Statement stmt = null;
        ResultSet res = null;
        PreparedStatement ps = null;
        try{
            stmt = con.createStatement();
            res = stmt.executeQuery("SELECT * FROM Produkty");
            while (res.next()){
                if (res.getString("Produkt").equalsIgnoreCase(this.produkt) && res.getString("Marka").equalsIgnoreCase(this.marka)){
                    throw new SQLException("Jest juz "+this.produkt + " Marki " + this.marka);
                }
            }
            ps = con.prepareStatement("insert into Produkty values(default ,?,?,?,?,?)");
            ps.setString(1, this.produkt);
            ps.setString(2, this.marka);
            ps.setString(3, this.przypisanaKategoria.name());
            ps.setDouble(4, this.cena);
            ps.setInt(5, this.iloscDniNimSiePrzeterminuje);
            ps.executeUpdate();
        }
        catch (Exception e){System.out.println(e);}finally {
            IConnectable.ClosingConnection(con, ps, stmt, res);
        }
    }

    public void ZmienProduktIDodajDoBazyProdoktow(String produkt, String marka, Kategoria kategoria, double cena, int iloscDniNimSiePrzeterminuje){
        ZmienProdukt(produkt, marka, kategoria, cena, iloscDniNimSiePrzeterminuje);
        DodajProduktDoBazyProduktow();
    }

}
