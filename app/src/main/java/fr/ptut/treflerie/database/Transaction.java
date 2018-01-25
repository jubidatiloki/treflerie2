package fr.ptut.treflerie.database;

/**
 * Created by benja on 08/12/2017.
 */

public class Transaction {

    private int idTransaction;
    private double montant;
    private String interlocuteur;      //échangiste pour logan
    private int reception;          // 1 pour oui, 0 pour non
    private String date;

    public Transaction(){};

    public Transaction(int idTransaction, double montant, String interlocuteur, int reception, String date) {
        this.idTransaction = idTransaction;
        this.montant = montant;
        this.interlocuteur = interlocuteur;
        this.reception = reception;
        this.date = date;
    }

    public int getIdTransaction() {
        return idTransaction;
    }

    public double getMontant() {
        return montant;
    }

    public String getInterlocuteur() {
        return interlocuteur;
    }

    public int getReception() {
        return reception;
    }

    public String getDate() {
        return date;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setInterlocuteur(String interlocuteur) {
        this.interlocuteur = interlocuteur;
    }

    public void setReception(int recu) {
        this.reception = recu;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "idTransaction=" + idTransaction +
                ", montant=" + montant +
                ", interlocuteur='" + interlocuteur + '\'' +
                ", reception='" + reception + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
