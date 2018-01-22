package fr.ptut.treflerie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Created by benja on 22/12/2017.
 */

public class MessageManager {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "treflerie.db";

    private static final String TABLE_MESSAGE = "table_message";
    private static final String COL_TAG = "tag";
    private static final int NUM_COL_TAG = 0;
    private static final String COL_LIBELLE = "libelle";
    private static final int NUM_COL_LIBELLE = 1;

    private SQLiteDatabase bdd;

    private Context context;
    private MaBaseSQLite maBaseSQLite;

    public MessageManager(Context context){
        //On créer la BDD et sa table
        this.context = context;
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

    public long insertMessage(Message message){

        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_TAG, message.getTag());
        values.put(COL_LIBELLE, message.getLibelle());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_MESSAGE, null, values);
    }

    public int updateMessage(String tag, Message message){

        //La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simple préciser quelle livre on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_TAG, message.getTag());
        values.put(COL_LIBELLE, message.getLibelle());
        return bdd.update(TABLE_MESSAGE, values, COL_TAG + " LIKE \"" + tag +"\"", null);
    }


    public Message getMessageByTag(String tag){

        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_MESSAGE, new String[] {COL_TAG, COL_LIBELLE}, COL_TAG + " LIKE \"" + tag +"\"", null, null, null, null);
        if (c != null)
            c.moveToFirst();
        Message msg = new Message(
                c.getString(NUM_COL_TAG),
                c.getString(NUM_COL_LIBELLE));
        return msg;
    }

    //Cette méthode permet de convertir un cursor en un livre
    private Message cursorToMessage(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un livre
        Message message = new Message();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        message.setTag(c.getString(NUM_COL_TAG));
        message.setLibelle(c.getString(NUM_COL_LIBELLE));
        //On ferme le cursor
        c.close();

        //On retourne le livre
        return message;
    }

    public int nombreDeLigne(){
        //sélectionner toutes les lignes de la table commandes
        Cursor curseur = bdd.query(TABLE_MESSAGE, null, null, null, null, null, null);
        //Renvoyer le nombre de lignes
        return curseur.getCount();
    }

    public int removeParametreByTag(String tag){
        if(!bdd.isOpen()) {
            this.open();
        }
        //Suppression d'un livre de la BDD grâce à l'ID
        return bdd.delete(TABLE_MESSAGE, COL_TAG + " = " +tag, null);
    }



}
