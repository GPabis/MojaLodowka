package com.company;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProduktyWLodowce {
    private int produktWLodowceID;
    private String produktWLodowce;
    private String markaProduktuWLodowce;
    private List<Integer> iloscProduktowWLodowce;
    private LocalDate dataZakupuNajstarszegoProduktu;
    private LocalDate najkrotszaDataWaznosci;
    private double cenaProduktuWLodowce;
    private List<Integer> IDZakupow;
    private List<LocalDate> DatyZakopow;

    public ProduktyWLodowce(){
        this.produktWLodowceID = -1;
        this.produktWLodowce = "";
        this.markaProduktuWLodowce ="";
        this.iloscProduktowWLodowce = new ArrayList<>();
        this.dataZakupuNajstarszegoProduktu = null;
        this.najkrotszaDataWaznosci = null;
        this.cenaProduktuWLodowce = 0;
        this.IDZakupow = new ArrayList<>();
        this.DatyZakopow = new ArrayList<>();
    }


}
