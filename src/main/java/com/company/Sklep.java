package com.company;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class Sklep extends ProduktyWLodowce implements IConnectable{

    /**
     * Sklep Class is used to adding new product to our store (Lodówka),
     * user operate on methods form this class. It's imports specific product,
     * form the ListaIstniejacychProduktow class and add it to new object that
     * will give it to store object and next to the database.
     * Beside values of specific Product it contains also information of amounts
     * of specific product, sum of price of produsts, date of operation and product expiry date
     *
     * @author Grzegorz Pabiś
     */

    public List<Produkty> listaProduktowWKoszyku;
    private double cenaProduktow;
    private List<Integer> iloscDanegoProduktu;
    private LocalDate dataZakupow;
    private List<LocalDate> dataWaznosci;
    private int numerZakupow;
    public static int numerZakupowNumeracja = 0;

    static {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = IConnectable.connectDefault();
            Statement stmt = con.createStatement();
            String tworzenieTabeliZakupy = "CREATE TABLE IF NOT EXISTS Zakupy" +
                    "(IDzakupow INT(3) NOT NULL AUTO_INCREMENT," +
                    "DataZakupow DATE NOT NULL," +
                    "PRIMARY KEY (IDzakupow))";
            String tworzenieTabeliZakupioneProdukty = "CREATE TABLE IF NOT EXISTS ZakupioneProdukty" +
                    "(IDzakupow INT(3) NOT NULL," +
                    "IDproduktu INT(3) NOT NULL, " +
                    "Ilosc int(3) NOT NULL, "+
                    "DataWaznosci DATE NOT NULL, " +
                    "PRIMARY KEY (IDzakupow, IDproduktu)," +
                    "FOREIGN KEY (IDproduktu) REFERENCES Produkty(IDproduktu)," +
                    "FOREIGN KEY (IDzakupow) REFERENCES Zakupy(IDzakupow))";
            stmt.executeUpdate(tworzenieTabeliZakupy);
            stmt.executeUpdate(tworzenieTabeliZakupioneProdukty);
            System.out.println("Tabela Zakupy Gotowa");
            stmt.close();
            con.close();
        }
        catch (Exception e){System.out.println(e);}
    }

    public Sklep(){
        /**
         * Constructor of class. At first it is empty.
         *
         * @param listaProduktowWKoszyku        list of product added to
         * @param caneProduktow                  sum of prices of specific products
         * @param iloscDanegoProduktu            collection of amount of specific products
         * @param dataZakupow                    date of adding product to the store
         * @param dataWaznosci                   date of product expiry
         */
        this.listaProduktowWKoszyku = new ArrayList<>();
        this.cenaProduktow = 0;
        this.iloscDanegoProduktu = new ArrayList<>();
        this.dataZakupow = LocalDate.now();
        this.dataWaznosci = new ArrayList<>();
        this.numerZakupow = numerZakupowNumeracja + 1;
    }

    /**
     * Method that print for user list of all products form ListaIstniejacychProduktow class.
     * @param lista         ListaIstniejacychProduktow list of product on which user can operate
     */

    public void WypiszListeDostepnychProduktow(ListaIstniejacychProduktow lista) {
        System.out.println(lista);
    }

    /**
     * It is main method for adding products to the Sklep object and then to the database,
     * User will be asked to choose ID of specific product form the list of products (showed
     * by @method WypiszListeDostepnychProduktow). After entering specific ID, inherited method form
     * ListaIstniajacychProduktow class @method WybierzProdukt choose specific product form the list.
     *
     * If the product was selected for the first time it will add it to the @param listaProduktowWKoszyku
     * and it will add 1 amount of that product to the @param iloscDanegoProduktu and it will calculate date of
     * expiry for this product. Also new price will be added.
     *
     * If the product was selected for the second time it will only increment @param iloscDanegoProduktu and @param
     * cenaProduktow
     *
     * If user decide to end "shopping" then will only needed to put "0" instead of ID.
     * @param listaWszystkichProduktow          list of existing product
     */

    public void Zakupy(ListaIstniejacychProduktow listaWszystkichProduktow, int nrId){
            Produkty wybranyProdukt = listaWszystkichProduktow.WybierzProdukt(nrId);
            if (!listaProduktowWKoszyku.contains(wybranyProdukt)) {
                listaProduktowWKoszyku.add(wybranyProdukt);
                iloscDanegoProduktu.add(1);
                dataWaznosci.add(LocalDate.now().plusDays(wybranyProdukt.iloscDniNimSiePrzeterminuje));
            } else {
                Integer iloscProduktu = iloscDanegoProduktu.get(listaProduktowWKoszyku.indexOf(wybranyProdukt)) + 1;
                iloscDanegoProduktu.set(listaProduktowWKoszyku.indexOf(wybranyProdukt), iloscProduktu);
            }
            this.cenaProduktow += wybranyProdukt.cena;
    }

    /**
     * toString method create string for printing list of buyed product that will be
     * added to the store.
     * @return                  string contains all data gathered by @method Zakupy
     */

    public String toString(){
        if (listaProduktowWKoszyku.size()==0) return "Nie było zakupów!";
        StringBuilder sb = new StringBuilder();
        sb.append("Dnia ");
        sb.append(dataZakupow);
        sb.append(" zakupiono następujące produkty: \n");
        for (int i = 0; i<listaProduktowWKoszyku.size(); i++){
            sb.append(listaProduktowWKoszyku.get(i).toString());
            sb.append(" W ilości [");
            sb.append(iloscDanegoProduktu.get(i));
            sb.append("], Produkt Zepsuje Się: ");
            sb.append(dataWaznosci.get(i));
            sb.append("\n");
        }
        sb.append("Łącznie Wydano: ");
        sb.append(cenaProduktow);
        sb.append("ZŁ");
        return sb.toString();
    }

    /**
     * Method that shows current list of object in @param listaProduktowWKoszyku, @param iloscProduktoWKoszyku,
     * , @param dataWaznosci and @param cenaProduktow
     * @return              Returning string of information
     */

    public String PokazZawartoscKoszyka(){

        StringBuilder sb = new StringBuilder();
        sb.append("W twoim koszyku znajdują się: \n");
        for (int i = 0; i<listaProduktowWKoszyku.size(); i++){
            sb.append(listaProduktowWKoszyku.get(i).toString());
            sb.append(" W ilości [");
            sb.append(iloscDanegoProduktu.get(i));
            sb.append("], Produkt Zepsuje Się: ");
            sb.append(dataWaznosci.get(i));
            sb.append("\n");
        }
        sb.append("Łączna Cena: ");
        sb.append(cenaProduktow);
        sb.append("ZŁ");
        return sb.toString();
    }

    /**
     * Method that remove selected items form @param listaProdutkowWKoszyku
     */

    public void UsunZKoszyka(){
        int nrId = -1;
        while (nrId !=0) {
            Scanner sr = new Scanner(System.in);
            System.out.println(PokazZawartoscKoszyka() + "\n Który produkt chcesz usunąć? Napisz nr. ID. Jeżeli nie chcesz wyrzucić żadnego produktu napisz \"0\"");
            nrId = sr.nextInt();
            if (nrId == 0) continue;
            Produkty wybranyProdukt = WybierzProduktZKoszyka(nrId);
            this.cenaProduktow -= wybranyProdukt.cena;
            if (iloscDanegoProduktu.get(listaProduktowWKoszyku.indexOf(wybranyProdukt)) <= 1) {
                iloscDanegoProduktu.remove(listaProduktowWKoszyku.indexOf(wybranyProdukt));
                this.dataWaznosci.remove(listaProduktowWKoszyku.indexOf(wybranyProdukt));
                listaProduktowWKoszyku.remove(wybranyProdukt);
            } else {
                Integer iloscProduktu = iloscDanegoProduktu.get(listaProduktowWKoszyku.indexOf(wybranyProdukt)) - 1;
                iloscDanegoProduktu.set(listaProduktowWKoszyku.indexOf(wybranyProdukt), iloscProduktu);
            }
            if (listaProduktowWKoszyku.size() == 0) {
                System.out.println("W koszyku nic nie ma!");
                nrId = 0;
            }
            else System.out.println(PokazZawartoscKoszyka());
        }
    }

    /**
     * Method that select specific product form list @param listaProduktowWKoszyku
     * @param idProduktu            ID of selected product
     * @return                      object Produkt form list @param listaProduktowWKoszyku
     */

    public Produkty WybierzProduktZKoszyka(int idProduktu){
        for (int i = 0; i<listaProduktowWKoszyku.size(); i++){
            System.out.println(listaProduktowWKoszyku.get(i).produktID==idProduktu);
            System.out.println(listaProduktowWKoszyku.get(i).produktID);
            if (listaProduktowWKoszyku.get(i).produktID==idProduktu) return listaProduktowWKoszyku.get(i);
        }
        System.out.println("Błędny nr. ID");
        return null;
    }

    /**
     * Clear all items form Object.
     */

    public void OproznijKoszyk(){
        this.listaProduktowWKoszyku.clear();
        this.cenaProduktow = 0;
        this.iloscDanegoProduktu.clear();
        this.dataZakupow = LocalDate.now();
        this.dataWaznosci.clear();
    }

    public void DodajZakupyDoBazyDanych(){
        Connection con = IConnectable.connectDefault();
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement zakupyPrepStmt = null;
        try {
            if (this.listaProduktowWKoszyku.size()==0) throw new IndexOutOfBoundsException();
            stmt = con.createStatement();
            zakupyPrepStmt = con.prepareStatement("INSERT into Zakupy values (default,?)");
            zakupyPrepStmt.setDate(1,java.sql.Date.valueOf(this.dataZakupow));
            zakupyPrepStmt.executeUpdate();
            rs = stmt.executeQuery("SELECT IDzakupow FROM Zakupy");
            if (rs.last()) this.numerZakupow = rs.getInt(1);
            PreparedStatement prepStmt = con.prepareStatement("insert into ZakupioneProdukty values(?,?,?,?)");
            for (int i = 0; i<this.listaProduktowWKoszyku.size(); i++){
                prepStmt.setInt(1,this.numerZakupow);
                prepStmt.setInt(2, this.listaProduktowWKoszyku.get(i).produktID);
                prepStmt.setInt(3, this.iloscDanegoProduktu.get(i));
                prepStmt.setDate(4, java.sql.Date.valueOf(this.dataWaznosci.get(i)));
                prepStmt.executeUpdate();
            }
            prepStmt.close();
        }
        catch (Exception e){System.out.println(e);}finally {
            IConnectable.ClosingConnection(con, zakupyPrepStmt, stmt, rs);
        }
    }


}
