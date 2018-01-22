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
    private static final String COL_INTERLOCUTEUR = "interlocuteur";
    private static final int NUM_COL_INTERLOCUTEUR = 2;
    private static final String COL_RECECPTION = "reception";
    private static final int NUM_COL_RECEPTION = 3;
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
        values.put(COL_INTERLOCUTEUR, transaction.getInterlocuteur());
        values.put(COL_RECECPTION, transaction.getReception());
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
        values.put(COL_INTERLOCUTEUR, transaction.getInterlocuteur());
        values.put(COL_RECECPTION, transaction.getReception());
        values.put(COL_DATE, transaction.getDate());
        return bdd.update(TABLE_TRANSACTION, values, COL_IDTRANS + " = " +idTrans, null);
    }


    public Transaction getTransactionById(int id){

        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_TRANSACTION, new String[] {COL_IDTRANS, COL_MONTANT, COL_INTERLOCUTEUR, COL_RECECPTION, COL_DATE}, COL_IDTRANS + " LIKE \"" + id +"\"", null, null, null, null);
        return cursorToTransaction(c);
    }

    //Cette méthode permet de convertir un cursor en un livre
    private Transaction cursorToTransaction(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un livre
        Transaction transaction = new Transaction();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        transaction.setIdTransaction(c.getInt(NUM_COL_IDTRANS));
        transaction.setMontant(c.getDouble(NUM_COL_MONTANT));
        transaction.setInterlocuteur(c.getString(NUM_COL_INTERLOCUTEUR));
        transaction.setReception(c.getInt(NUM_COL_RECEPTION));
        transaction.setDate(c.getString(NUM_COL_DATE));
        //On ferme le cursor
        c.close();

        //On retourne le livre
        return transaction;
    }

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
