package fr.ptut.treflerie.database;

/**
 * Created by benja on 22/12/2017.
 */

public class Message {

    private String tag;
    private String libelle;

    public Message(){}

    public Message(String tag, String libelle){
        this.tag = tag;
        this.libelle = libelle;
    }

    public String getTag() {
        return this.tag;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString() {
        return "Message{" +
                "tag='" + tag + '\'' +
                ", libelle='" + libelle + '\'' +
                '}';
    }
}
