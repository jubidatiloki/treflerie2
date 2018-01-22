package fr.ptut.treflerie.controller;

import android.content.Context;
import android.telephony.SmsManager;

import fr.ptut.treflerie.database.Parametre;
import fr.ptut.treflerie.database.ParametreManager;

/**
 * Created by benja on 14/12/2017.
 */

public class SmsSender {
    /**
     * Constructeur de la classe SmsSender permettant d'envoyer un sms au numéro par défaut du serveur
     * @param msg : message à envoyer
     */
    private ParametreManager parametreManager;

    public SmsSender(String msg, Context context){
        parametreManager = new ParametreManager(context);
        parametreManager.open();

        if(parametreManager.getParametre().getTelServeur().length()>= 4 && msg.length() > 0){
            SmsManager.getDefault().sendTextMessage(parametreManager.getParametre().getTelServeur(), null, msg, null, null);
        }
    }

}
