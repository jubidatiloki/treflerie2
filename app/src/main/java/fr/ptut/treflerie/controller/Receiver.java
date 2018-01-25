package fr.ptut.treflerie.controller;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import fr.ptut.treflerie.MainActivity;
import fr.ptut.treflerie.database.Message;
import fr.ptut.treflerie.database.MessageManager;
import fr.ptut.treflerie.database.Parametre;
import fr.ptut.treflerie.database.ParametreManager;
import fr.ptut.treflerie.model.FragmentNouvelle;


/**
 * Created by pierrick on 1/22/18.
 */

public class Receiver {

    private String[] liste;
    private MessageManager messageManager;
    private ParametreManager parametreManager;

    public void onReceive(Context context, Intent intent, String messageBody) {
        liste = messageBody.split(" ");

        messageManager = new MessageManager(context);
        parametreManager = new ParametreManager(context);
        messageManager.open();
        parametreManager.open();

        //consulter son solde
        // Le solde de votre compte 33 est de 151 T
        if (estUnSolde()) {
            Message msg = new Message("solde", liste[8]);
            messageManager.updateMessage("solde", msg);
            Parametre param = parametreManager.getParametre();
            param.setSolde(Double.parseDouble(liste[8].replace(",", ".")));
            parametreManager.updateParametre(param);
        }

        // derniere transaction
        // Votre derniere transaction est le 19.12.17 : recu de 35 Reveillere Pierrick 50 T     (taille 16 )
        //Votre derniere transaction est le 19.12.17 : pour 35 Reveillere Pierrick 50 T         (taille 15 )
        else if(estUneDerniere()){
            Message msg;
            if(liste.length == 16){
                msg = new Message("derniere", liste[5] + " " + liste[10]+ " " + liste[12] + " " + liste[13] + " " + liste[14] + " "+ "recu");
            }else{
                msg = new Message("derniere", liste[5] + " " + liste[9]+ " " + liste[11] + " " + liste[12] + " " + liste[13] + " "+ "pour");
            }
            //Toast.makeText(context, msg.toString(), Toast.LENGTH_SHORT).show();
            messageManager.updateMessage("derniere", msg);
        }
        // recap des 2 derniers mois
        // Volume 2 derniers mois: Recettes=101,04 Dépenses =0,04
        else if (estUnMois()) {
            String r = liste[6].split("=")[1];
            String d = liste[9].replace("=","" );
            Message msg = new Message("mois", r + " " + d);
            messageManager.updateMessage("mois", msg);
        }
        // recap de l'envoi qui vient d'etre effectué
        // Donné a Reveillere Pierrick (35):1,33 T
        else if(estUnEnvoi()){
            FragmentNouvelle fragmentNouvelle = new FragmentNouvelle();
            fragmentNouvelle.getTimer().onFinish();
            Toast.makeText(new MainActivity(),"La transaction a bien été effectuée.", Toast.LENGTH_LONG).show();
        }

        //msg type quand on recoit des sous:
        //   Recu de jean paul (99):15,12T
        else if (estUnRecu()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
            Toast.makeText(context, "date: " + currentDateandTime, Toast.LENGTH_SHORT).show();
            //Transaction transaction = new Transaction(Double.parseDouble(liste[5]), Integer.parseInt(phoneNumber), 0, currentTime.toString());
            //Toast.makeText(context, transaction.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean estUnRecu() {
        return liste[0].equals("Recu") && liste[1].equals("de");
    }
    public boolean estUnSolde() { return liste[0].equals("Le") && liste[1].equals("solde"); }
    public boolean estUneDerniere() { return liste[0].equals("Votre") && liste[1].equals("derniere"); }
    public boolean estUnMois(){ return liste[0].equals("Volume") && liste[1].equals("2"); }
    public boolean estUnEnvoi(){ return liste[0].equals("Donné") && liste[1].equals("a");}
}
