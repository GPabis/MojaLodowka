package com.company;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import org.json.*;



public class Lodowka extends MyHandler implements IConnectable{

    private List<String> produktWLodowce;
    private List<String> markaWLodowce;
    private List<Produkty.Kategoria> kategoriaWLodowce;
    private List<Integer> iloscWLodowce;
    private List<Double> cenaWLodowce;
    private List<LocalDate> dataWaznosciWLodowce;
    private List<Integer> IDproduktuWLodowce;

    public Lodowka(){
        this.produktWLodowce = new ArrayList<>();
        this.markaWLodowce = new ArrayList<>();
        this.kategoriaWLodowce = new ArrayList<>();
        this.iloscWLodowce = new ArrayList<>();
        this.cenaWLodowce = new ArrayList<>();
        this.dataWaznosciWLodowce = new ArrayList<>();
        this.IDproduktuWLodowce = new ArrayList<>();;
    }

    public void PobieraniePordoktowWLodowce(){
        Connection con = IConnectable.connectDefault();
        Statement stmt = null;
        ResultSet res = null;
        try{
            stmt = con.createStatement();
            res = stmt.executeQuery("SELECT ZakupioneProdukty.IDproduktu, Produkty.Produkt, Produkty.Marka, Produkty.Kategoria, Produkty.Cena, Ilosc, DataWaznosci FROM ZakupioneProdukty INNER JOIN Produkty ON ZakupioneProdukty.IDproduktu = Produkty.IDproduktu");
            if (this.IDproduktuWLodowce!=null){
                this.produktWLodowce.clear();
                this.markaWLodowce.clear();
                this.kategoriaWLodowce.clear();
                this.iloscWLodowce.clear();
                this.cenaWLodowce.clear();
                this.dataWaznosciWLodowce.clear();
                this.IDproduktuWLodowce.clear();
            }
            while (res.next()){
                if (!this.IDproduktuWLodowce.contains(res.getInt(1))){
                    this.IDproduktuWLodowce.add(res.getInt(1));
                    this.produktWLodowce.add(res.getString(2));
                    this.markaWLodowce.add(res.getString(3));
                    this.kategoriaWLodowce.add(Produkty.Kategoria.valueOf(res.getString(4)));
                    this.iloscWLodowce.add(res.getInt(6));
                    this.cenaWLodowce.add(res.getDouble(5)*res.getInt(6));
                    this.dataWaznosciWLodowce.add(res.getDate(7).toLocalDate());
                }
                else{
                    int i = this.IDproduktuWLodowce.indexOf(res.getInt(1));
                    int powiekszonaIlosc = this.iloscWLodowce.get(i) + res.getInt(6);
                    this.iloscWLodowce.set(i, powiekszonaIlosc);
                    double powiekszonaCena = res.getDouble(5) * this.iloscWLodowce.get(i);
                    this.cenaWLodowce.set(i, powiekszonaCena);
                }
            }
            System.out.println("Lista Pobrana");
        }
        catch (Exception e){System.out.println(e);}finally {
            IConnectable.ClosingConnection(con,null, stmt, res);
        }
    }


    public String toString(){
        StringBuilder sb = new StringBuilder();
        String cenaZaokraglona;
        sb.append("Produkty dostępne w twojej lodówce:\n");
        for (int i = 0; i < this.produktWLodowce.size(); i++){
            cenaZaokraglona = String.format("%.2f PLN", this.cenaWLodowce.get(i));
            sb.append(this.produktWLodowce.get(i) + "  |  " + this.markaWLodowce.get(i)+ "  |  "+this.kategoriaWLodowce.get(i)+"  |  "+ cenaZaokraglona+"  |  " + this.iloscWLodowce.get(i) + "  |  " + this.dataWaznosciWLodowce.get(i) + " |\n");
        }
        return sb.toString();
    }

    public void Zjedz(int IDproduktuWLodowce, int iloscProduktowKtoreZjadles){
        Connection con = IConnectable.connectDefault();
        Statement stmt = null;
        ResultSet res = null;
        try{
            List<Integer> IDzakupow = new ArrayList<>();
            int ileDoOdjęcia = 0, iloscDoOdjeciaZOstatnichZakupow =0;
            stmt = con.createStatement();
            String query = "SELECT * FROM ZakupioneProdukty WHERE IDproduktu = "+IDproduktuWLodowce;
            res = stmt.executeQuery(query);
            if (res.wasNull())throw new IndexOutOfBoundsException();
            while (res.next()){
                IDzakupow.add(res.getInt(1));
                ileDoOdjęcia += res.getInt(3);
                if (ileDoOdjęcia>iloscProduktowKtoreZjadles) break;
                iloscDoOdjeciaZOstatnichZakupow += res.getInt(3);
                if (ileDoOdjęcia>=iloscProduktowKtoreZjadles) break;
            }
            iloscDoOdjeciaZOstatnichZakupow = iloscProduktowKtoreZjadles - iloscDoOdjeciaZOstatnichZakupow;
            if (ileDoOdjęcia<iloscProduktowKtoreZjadles) throw new IndexOutOfBoundsException();
            if (iloscDoOdjeciaZOstatnichZakupow!=0 && IDzakupow.size()>1){
                stmt.executeUpdate(OdejmowanieIlosciProduktow(IDzakupow, IDproduktuWLodowce, iloscDoOdjeciaZOstatnichZakupow));
                IDzakupow.remove(IDzakupow.size()-1);
                stmt.executeUpdate(UsuwanieZakopow(IDzakupow, IDproduktuWLodowce));
            }
            else {
                if (IDzakupow.size()>1){
                    stmt.executeUpdate(UsuwanieZakopow(IDzakupow, IDproduktuWLodowce));
                }
                else {
                    stmt.executeUpdate(OdejmowanieIlosciProduktow(IDzakupow, IDproduktuWLodowce, iloscProduktowKtoreZjadles));
                }
            }
        }
        catch (Exception e){System.out.println(e);}finally {
            IConnectable.ClosingConnection(con,null, stmt, res);
        }
    }

    public String UsuwanieZakopow(List<Integer> IDzakupow, int idProduktu){
        String idZakopowString = IDzakupow.toString();
        idZakopowString = idZakopowString.replace("[","('").replace("]","')").replace(",","',").replace(" ", "'");
        String queryDelete = "DELETE FROM ZakupioneProdukty WHERE IDproduktu =" + idProduktu +" AND IDzakupow IN"+idZakopowString;
        return queryDelete;
    }

    public String OdejmowanieIlosciProduktow(List<Integer> IDzakupow, int idProduktu, int iloscDoOdjecia){
        String queryUpdate = "UPDATE ZakupioneProdukty SET Ilosc=Ilosc-" +iloscDoOdjecia +" WHERE IDzakupow= " +IDzakupow.get(IDzakupow.size()-1) + " AND IDproduktu =" + idProduktu;
        return queryUpdate;
    }

    public JSONObject ProduktyJSON(){
        JSONObject produktyJson = new JSONObject();
        JSONArray produkty = new JSONArray ();
        for (int i = 0; i < this.produktWLodowce.size(); i++){
            JSONObject produkt = new JSONObject();
            produkt.put("produkt", this.produktWLodowce.get(i));
            produkt.put("marka", this.markaWLodowce.get(i));
            produkt.put("kategoria", this.kategoriaWLodowce.get(i));
            produkt.put("ilosc", this.iloscWLodowce.get(i));
            produkt.put("wartosc", this.cenaWLodowce.get(i));
            produkt.put("dataWaznosci", this.dataWaznosciWLodowce.get(i));
            produkt.put("id", this.IDproduktuWLodowce.get(i));
            produkty.put(produkt);
        }
        produktyJson.put("produkty", produkty);
        return produktyJson;
    }

}
