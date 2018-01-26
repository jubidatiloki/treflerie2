package fr.ptut.treflerie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by benja on 12/12/2017.
 */

public class ParametreManager {

    private static final int VERSION_BDD = 2;
    private static final String NOM_BDD = "treflerie.db";

    private static final String TABLE_PARAMETRES = "table_parametres";
    private static final String COL_NUM_COMPTE = "numCompte";
    private static final int NUM_COL_NUM_COMPTE  = 0;
    private static final String COL_TEL_SERVEUR = "telServeur";
    private static final int NUM_COL_TEL_SERVEUR = 1;
    private static final String COL_MONTANT_MAX = "montantMax";
    private static final int NUM_COL_MONTANT_MAX = 2;
    private static final String COL_NOM = "nom";
    private static final int NUM_COL_NOM = 3;
    private static final String COL_SOLDE = "solde";
    private static final int NUM_COL_SOLDE = 4;


    private SQLiteDatabase bdd;

    private MaBaseSQLite maBaseSQLite;

    public ParametreManager(Context context){
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

    public long insertParametre(Parametre parametre){

        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
        values.put(COL_NUM_COMPTE, parametre.getNumCompte());
        values.put(COL_TEL_SERVEUR, parametre.getTelServeur());
        values.put(COL_MONTANT_MAX, parametre.getMontantMax());
        values.put(COL_NOM, parametre.getNom());
        values.put(COL_SOLDE, parametre.getSolde());
        //on insère l'objet dans la BDD via le ContentValues
        return bdd.insert(TABLE_PARAMETRES, null, values);
    }

    public int updateParametre(Parametre parametre){

        //La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
        //il faut simple préciser quelle livre on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_NUM_COMPTE, parametre.getNumCompte());
        values.put(COL_TEL_SERVEUR, parametre.getTelServeur());
        values.put(COL_MONTANT_MAX, parametre.getMontantMax());
        values.put(COL_NOM, parametre.getNom());
        values.put(COL_SOLDE, parametre.getSolde());
        return bdd.update(TABLE_PARAMETRES, values, null, null);
    }


    public Parametre getParametre(){

        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_PARAMETRES, new String[] {COL_NUM_COMPTE, COL_TEL_SERVEUR, COL_MONTANT_MAX, COL_NOM, COL_SOLDE}, null, null, null, null, null);
        if (c != null)
            c.moveToFirst();
        Parametre param = new Parametre(Integer.parseInt(c.getString(NUM_COL_NUM_COMPTE)),
                                        c.getString(NUM_COL_TEL_SERVEUR),
                                        Double.parseDouble(c.getString(NUM_COL_MONTANT_MAX)),
                                        c.getString(NUM_COL_NOM),
                                        Double.parseDouble(c.getString(NUM_COL_SOLDE)));
        return param;
    }

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
    }

    public int nombreDeLigne(){
        //sélectionner toutes les lignes de la table commandes
        Cursor curseur = bdd.query(TABLE_PARAMETRES, null, null, null, null, null, null);
        //Renvoyer le nombre de lignes
        return curseur.getCount();
    }

    public int removeParametreByNum(int numCompte){
        if(!bdd.isOpen()) {
            this.open();
        }
        //Suppression d'un livre de la BDD grâce à l'ID
        return bdd.delete(TABLE_PARAMETRES, COL_NUM_COMPTE + " = " +numCompte, null);
    }
}