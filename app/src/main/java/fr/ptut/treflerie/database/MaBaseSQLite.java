package fr.ptut.treflerie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by benja on 12/12/2017.
 */

public class MaBaseSQLite extends SQLiteOpenHelper {

    private static final String TABLE_PARAMETRES = "table_parametres";
    private static final String COL_NUM_COMPTE = "numCompte";
    private static final String COL_TEL_SERVEUR = "telServeur";
    private static final String COL_MONTANT_MAX = "montantMax";
    private static final String COL_NOM = "nom";
    private static final String COL_SOLDE = "solde";

    private static final String CREATE_PARAMETRE = "CREATE TABLE " + TABLE_PARAMETRES + " ("
            + COL_NUM_COMPTE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_TEL_SERVEUR + " TEXT NOT NULL, "
            + COL_MONTANT_MAX + " DOUBLE NOT NULL, "
            + COL_NOM + " TEXT NOT NULL, "
            + COL_SOLDE + " DOUBLE );";



    private static final String TABLE_MESSAGE = "table_message";
    private static final String COL_TAG = "tag";
    private static final String COL_LIBELLE = "libelle";

    private static final String CREATE_MESSAGE = "CREATE TABLE " + TABLE_MESSAGE + " ("
            + COL_TAG + " TEXT, "
            + COL_LIBELLE + " TEXT );";

    public MaBaseSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //on créé la table à partir de la requête écrite dans la variable CREATE_PARAMETRE
        db.execSQL(CREATE_PARAMETRE);
        db.execSQL(CREATE_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
        //comme ça lorsque je change la version les id repartent de 0
        db.execSQL("DROP TABLE " + TABLE_PARAMETRES + ";");
        db.execSQL("DROP TABLE " + TABLE_MESSAGE + ";");
        onCreate(db);
    }

}