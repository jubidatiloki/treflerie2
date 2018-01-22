package fr.ptut.treflerie.controller;

import android.telephony.SmsManager;

/**
 * Created by benja on 14/12/2017.
 */

public class SmsSender {
    /**
     * Constructeur de la classe SmsSender permettant d'envoyer un sms au numéro par défaut du serveur
     * @param msg : message à envoyer
     */
    public SmsSender(String msg){
        if(Configuration.TEL_SERVEUR_DEFAUT.length()>= 4 && msg.length() > 0){
            SmsManager.getDefault().sendTextMessage(Configuration.TEL_SERVEUR_DEFAUT, null, msg, null, null);
        }
    }

    /**
     * Constructeur de la classe SmsSender permettant d'envoyer un sms au numéro choisi
     * @param num : numéro du destinataire
     * @param msg : message à envoyer
     */
    public SmsSender(String num, String msg){
        if(num.length()>= 4 && msg.length() > 0){
            SmsManager.getDefault().sendTextMessage(num, null, msg, null, null);
        }
    }
}
