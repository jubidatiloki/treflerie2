package fr.ptut.treflerie.database;

import java.util.Date;

/**
 * Created by benja on 08/12/2017.
 */

public class Transaction {

    private int idTransaction;
    private double montant;
    private int expediteur;
    private int destinataire;
    private String date;

    //un tel 0 signifiera son propre tél

    public Transaction(){}

    public Transaction(double montant, int expediteur, int destinataire, String date) {
        this.montant = montant;
        this.expediteur = expediteur;
        this.destinataire = destinataire;
        this.date = date;
    }

    public int getIdTransaction() {
        return this.idTransaction;
    }

    public double getMontant() {
        return this.montant;
    }

    public int getExpediteur() {
        return this.expediteur;
    }

    public int getDestinataire() {
        return this.destinataire;
    }

    public String getDate() {
        return this.date;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setExpediteur(int expediteur) {
        this.expediteur = expediteur;
    }

    public void setDestinataire(int destinataire) {
        this.destinataire = destinataire;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "idTransaction=" + idTransaction +
                ", montant=" + montant +
                ", expediteur=" + expediteur +
                ", destinataire=" + destinataire +
                ", date='" + date + '\'' +
                '}';
    }
}
