package fr.ptut.treflerie.model;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import fr.ptut.treflerie.FragmentDialogPopup;
import fr.ptut.treflerie.R;
import fr.ptut.treflerie.controller.Configuration;
import fr.ptut.treflerie.controller.SmsSender;
import fr.ptut.treflerie.database.MessageManager;

/**
 * Created by benja on 07/12/2017.
 */

public class FragmentDerniere extends Fragment{

    View myView;
    private TextView monLabel;
    private TextView maDate;
    private TextView monDest;
    private TextView monMontant;
    private MessageManager messageManager;
    private ImageView infobulle;
    Button bactualiser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_derniere, container, false);
        new SmsSender(Configuration.SMS_DERNIERE, myView.getContext());
        monLabel = myView.findViewById(R.id.derniere_label);
        maDate = myView.findViewById(R.id.derniere_date);
        monDest = myView.findViewById(R.id.derniere_dest);
        monMontant = myView.findViewById(R.id.derniere_montant);
        messageManager = new MessageManager(myView.getContext());
        messageManager.open();
        // au moins une transaction effectuée
        paramDerniere();

        bactualiser = myView.findViewById(R.id.derniere_actualiser);
        bactualiser.setClickable(true);
        bactualiser.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paramDerniere();
            }
        });

        infobulle = myView.findViewById(R.id.derniere_infobulle);

        infobulle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment dialog = new FragmentDialogPopup();
                dialog.show(getFragmentManager(), "infobulle_derniere");
            }
        });

        return myView;
    }

    public void paramDerniere() {
        if(!messageManager.getMessageByTag("derniere").getLibelle().equals(Configuration.MESSAGE_VIDE_DEFAUT)){
            String[] items = messageManager.getMessageByTag("derniere").getLibelle().split(" ");
            String date = items[0].replace(".", "/");
            //   date   numCompte    nom    prenom    montant    recu|pour


            maDate.setText("Le "+ date);
            if(items[5].equals("recu")) {
                monDest.setText("Reçu de : " + items[2] + " " + items[3] + " (compte n°" + items[1] + ")");
            }else{
                monDest.setText("Envoyé à : " + items[2] + " " + items[3] + " (compte n°" + items[1] + ")");
            }

            monMontant.setText("Le montant de " + items[4] + "Trèfles");


            monLabel.setVisibility(View.INVISIBLE);
            maDate.setVisibility(View.VISIBLE);
            monDest.setVisibility(View.VISIBLE);
            monMontant.setVisibility(View.VISIBLE);
        }else{
            monLabel.setVisibility(View.VISIBLE);
            maDate.setVisibility(View.INVISIBLE);
            monDest.setVisibility(View.INVISIBLE);
            monMontant.setVisibility(View.INVISIBLE);
        }
    }

}
