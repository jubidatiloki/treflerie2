package fr.ptut.treflerie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;


/**
 * Created by benja on 11/12/2017.
 */

public class TransactionManager {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "treflerie.db";

    private static final String TABLE_TRANSACTION = "table_transactions";
    private static final String COL_IDTRANS = "id_trans";
    private static final int NUM_COL_IDTRANS = 0;
    private static final String COL_MONTANT = "montant";
    private static final int NUM_COL_MONTANT = 1;
    private static final String COL_EXP = "expediteur";
    private static final int NUM_COL_EXP = 2;
    private static final String COL_DEST = "destinataire";
    private static final int NUM_COL_DEST = 3;
    private static final String COL_DATE = "date";
    private static final int NUM_COL_DATE = 4;


    private SQLiteDatabase bdd;

    private MaBaseSQLite maBaseSQLite;

    public TransactionManager(Context context){
        //On créer la BDD et sa table
        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }


    public SQLiteDatabase getBDD(){
        return bdd;
    }



    public long insertTransaction(Transaction transaction){

        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_IDTRANS, transaction.getIdTransaction());
        values.put(COL_MONTANT, transaction.getMontant());
        values.put(COL_EXP, transaction.getExpediteur());
        values.put(COL_DEST, transaction.getDestinataire());
        values.put(COL_DATE, transaction.getDate());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_TRANSACTION, null, values);
    }

    public int updateTransaction(int idTrans, Transaction transaction){

        //La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simple préciser quelle livre on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_IDTRANS, transaction.getIdTransaction());
        values.put(COL_MONTANT, transaction.getMontant());
        values.put(COL_EXP, transaction.getExpediteur());
        values.put(COL_DEST, transaction.getDestinataire());
        values.put(COL_DATE, transaction.getDate());
        return bdd.update(TABLE_TRANSACTION, values, COL_IDTRANS + " = " +idTrans, null);
    }


    public Transaction getTransaction(){

        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_TRANSACTION, new String[] {COL_IDTRANS, COL_MONTANT, COL_EXP, COL_DEST, COL_DATE}, null, null, null, null, null);
        Transaction trans = new Transaction(
                Double.parseDouble(c.getString(NUM_COL_MONTANT)),
                Integer.parseInt(c.getString(NUM_COL_EXP)),
                Integer.parseInt(c.getString(NUM_COL_DEST)),
                c.getString(NUM_COL_DATE));
        return trans;
    }
    /*
    //Cette méthode permet de convertir un cursor en un livre
    private Parametre cursorToParametre(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un livre
        Parametre parametre = new Parametre();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        parametre.setNumCompte(c.getInt(NUM_COL_NUM_COMPTE));
        parametre.setTelServeur(c.getString(NUM_COL_TEL_SERVEUR));
        parametre.setMontantMax(c.getDouble(NUM_COL_MONTANT_MAX));
        parametre.setNom(c.getString(NUM_COL_NOM));
        parametre.setSolde(c.getDouble(NUM_COL_SOLDE));
        //On ferme le cursor
        c.close();

        //On retourne le livre
        return parametre;
    } */

    public int nombreDeLigne(){
        //sélectionner toutes les lignes de la table commandes
        Cursor curseur = bdd.query(TABLE_TRANSACTION, null, null, null, null, null, null);
        //Renvoyer le nombre de lignes
        return curseur.getCount();
    }

    public int removeTransactionById(int idTrans){
        if(!bdd.isOpen()) {
            this.open();
        }
        //Suppression d'un livre de la BDD grâce à l'ID
        return bdd.delete(TABLE_TRANSACTION, COL_IDTRANS + " = " +idTrans, null);
    }
}
