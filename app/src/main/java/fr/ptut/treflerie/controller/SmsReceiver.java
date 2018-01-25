package fr.ptut.treflerie.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import fr.ptut.treflerie.database.ParametreManager;


/**
 * Created by benja on 14/12/2017.
 */

public class SmsReceiver extends BroadcastReceiver {

    private final String ACTION_RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";

    private Receiver receiver;
    private ParametreManager parametreManager;

    public SmsReceiver() {
        this.receiver = new Receiver();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(ACTION_RECEIVE_SMS)){
            Bundle pudsBundle = intent.getExtras();
            Bundle bundle = intent.getExtras();
            parametreManager = new ParametreManager(context);
            parametreManager.open();
            if (bundle != null) {
                Object[] pdus = (Object[]) pudsBundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (messages.length > -1) {
                    final String messageBody = messages[0].getMessageBody();
                    messageBody.replace("é", "e");
                    final String phoneNumber = messages[0].getDisplayOriginatingAddress();

                    if(phoneNumber.equals(Configuration.TEL_SERVEUR_DEFAUT)) {
                        receiver.onReceive(context, intent, messageBody);

                    }
                }

            }
        }

    }

    /*
    private boolean estUnRecu() { return receiver.estUnRecu(); }
    private boolean estUnSolde() { return receiver.estUnSolde(); }
    private boolean estUneDerniere() { return receiver.estUneDerniere(); }
    private boolean estUnMois(){ return receiver.estUnMois(); }
    private boolean estUnEnvoi(){ return receiver.estUnEnvoi(); }
    */
}
