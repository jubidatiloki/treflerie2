package fr.ptut.treflerie.database;

import fr.ptut.treflerie.controller.Configuration;

/**
 * Created by benja on 12/12/2017.
 */

public class Parametre {

    private int numCompte;
    private String telServeur;
    private double montantMax;
    private String nom;
    private double solde;



    public Parametre(){
        this.telServeur = Configuration.TEL_SERVEUR_DEFAUT;
        this.montantMax = Configuration.MONTANT_MAX_DEFAUT;
        this.numCompte = 33;
        this.nom = Configuration.NOM_DEFAUT;
        this.solde = Configuration.SOLDE_DEFAUT;
    }

    public Parametre(String telServeur, double montantMax){
        this.numCompte = 33;
        this.telServeur = telServeur;
        this.montantMax = montantMax;
        this.nom = Configuration.NOM_DEFAUT;
        this.solde = Configuration.SOLDE_DEFAUT;
    }

    public Parametre(int numCompte, String telServeur, double montantMax){
        this.numCompte = numCompte;
        this.telServeur = telServeur;
        this.montantMax = montantMax;
        this.nom = Configuration.NOM_DEFAUT;
        this.solde = Configuration.SOLDE_DEFAUT;
    }

    public Parametre(int numCompte, String telServeur, double montantMax, String nom, double solde){
        this.numCompte = numCompte;
        this.telServeur = telServeur;
        this.montantMax = montantMax;
        this.nom = nom;
        this.solde = solde;
    }

    public int getNumCompte(){
        return this.numCompte;
    }

    public String getTelServeur() {
        return this.telServeur;
    }

    public double getMontantMax() {
        return this.montantMax;
    }

    public String getNom(){ return this.nom; }

    public double getSolde(){ return this.solde; }

    public void setNumCompte(int numCompte){
        this.numCompte = numCompte;
    }

    public void setTelServeur(String telServeur) {
        this.telServeur = telServeur;
    }

    public void setMontantMax(double montantMax) {
        this.montantMax = montantMax;
    }

    public void setNom(String nom){ this.nom = nom; }

    public void setSolde(double solde){ this.solde = solde; }

    @Override
    public String toString() {
        return "Parametre{" +
                "numCompte=" + numCompte +
                ", telServeur='" + telServeur + '\'' +
                ", montantMax=" + montantMax +
                ", nom='" + nom + '\'' +
                ", solde=" + solde +
                '}';
    }
}
