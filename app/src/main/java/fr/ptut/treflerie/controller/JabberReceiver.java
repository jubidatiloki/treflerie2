package fr.ptut.treflerie.controller;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.io.IOException;

/**
 * Created by pierrick on 1/22/18.
 */

public class JabberReceiver extends Thread implements IncomingChatMessageListener{

    private Context context;
    private Intent intent;
    private Receiver receiver;
    private AbstractXMPPConnection connection;

    private String msgToSend;

    public JabberReceiver(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        this.receiver = new Receiver();
    }

    public void run() {
        Chat chat = null;
        int tries = 3;// Nombre d'essai de connexion

        // On essaye de se connecter
        while(chat == null && tries > 0) {
            tries--;
            chat = connect();

            if(chat == null) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Si échec de connexion
        if ( chat == null)
            return;

        // On envoie le message
        while (true)
            sendNow(chat);

    }

    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        String messageBody = message.getBody();
        System.out.println(messageBody);
        Toast.makeText(context, messageBody, Toast.LENGTH_LONG).show();
        receiver.onReceive(context, intent, messageBody);
    }

    /*
    Définie la string a envoyer
    seule cette fonction est accessible
    sendNow() est appellée automatiquement
    */
    public void send(String message) {
        this.msgToSend = message;
    }

    private void sendNow(Chat chat) {
        if(this.msgToSend != null) {
            try {
                chat.send(this.msgToSend);
                this.msgToSend = null;
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Chat connect() {
        System.out.println("Connection...");

        try {
            final XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword("rpierrick", "soleil")
                    .setXmppDomain("mmtux.fr")
                    .setPort(5222)
                    .build();

            this.connection = new XMPPTCPConnection(config);
            this.connection.connect().login();

            // Assume we've created an XMPPConnection name "connection"
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(this); // ajout listener
            EntityBareJid jid = JidCreate.entityBareFrom("volet@mmtux.fr");
            Chat chat = chatManager.chatWith(jid);
            chat.send("S?");

            return chat;
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        } catch (InterruptedException e1) {
            e1.printStackTrace();
            return null;
        } catch (XMPPException e1) {
            e1.printStackTrace();
            return null;
        } catch (SmackException e1) {
            e1.printStackTrace();
            return null;
        }
    }
}
