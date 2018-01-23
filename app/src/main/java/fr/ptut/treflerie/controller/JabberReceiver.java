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

public class JabberReceiver extends Thread {

    private Context context;
    private Intent intent;
    private Receiver receiver;

    public JabberReceiver(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        this.receiver = new Receiver();
    }

    public void run() {
        try {
            final XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                    .setUsernameAndPassword("rpierrick", "soleil")
                    .setXmppDomain("mmtux.fr")
                    .setPort(5222)
                    .build();

            AbstractXMPPConnection connection = new XMPPTCPConnection(config);
            connection.connect().login();

            // Assume we've created an XMPPConnection name "connection"._
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(new IncomingChatMessageListener() {
                @Override
                public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                    newMessage(message);
                }
            });
            EntityBareJid jid = JidCreate.entityBareFrom("volet@mmtux.fr");
            Chat chat = chatManager.chatWith(jid);
            chat.send("S?");
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (XMPPException e1) {
            e1.printStackTrace();
        } catch (SmackException e1) {
            e1.printStackTrace();
        }
    }

    public void newMessage(Message message) {
        String messageBody = message.getBody();
        Toast.makeText(context, messageBody, Toast.LENGTH_LONG).show();
        receiver.onReceive(context, intent, messageBody);
    }
}
