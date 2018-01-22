package fr.ptut.treflerie.model;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import fr.ptut.treflerie.R;

/**
 * Created by benja on 07/12/2017.
 */

public class FragmentAide extends Fragment{

    View myView;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_aide, container, false);
        FrameLayout web = myView.findViewById(R.id.aide_fweb);
        web.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Appeler une URL web
                String url = "http://treflerie.info";
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                startActivity(intent);
            }
        });

        FrameLayout fb = myView.findViewById(R.id.aide_ffacebook);
        fb.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Appeler une URL web
                String url = "http://www.facebook.com/LaTreflerie";
                Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
                startActivity(intent);
            }
        });
        FrameLayout mail = myView.findViewById(R.id.aide_fmail);
        mail.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               envoiMail();
            }
        });

        return myView;
    }

    protected void envoiMail() {
        String[] TO = {"benjamin.tytgat@etu.unilim.fr"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Le problème rencontré est ..");

        try {
            startActivity(Intent.createChooser(emailIntent, "envoyer un mail avec"));
            Log.i("mail envoyé", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(myView.getContext(), "Il n'y a pas de client installé permettant l'envoi de mail.", Toast.LENGTH_SHORT).show();
        }
    }
}
