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

public class FragmentMois extends Fragment{

    View myView;
    private MessageManager messageManager;
    private TextView lmoisRecette;
    private TextView lmoisDepense;
    private TextView lbilan;
    private ImageView infobulle;
    Button bactualiser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.layout_mois, container, false);
        messageManager = new MessageManager(getActivity().getBaseContext());
        messageManager.open();
        new SmsSender(Configuration.SMS_MOIS, myView.getContext());
        lmoisRecette = myView.findViewById(R.id.mois_recette);
        lmoisDepense = myView.findViewById(R.id.mois_depense);
        lbilan = myView.findViewById(R.id.mois_bilan);

        paramMois();
        bactualiser = myView.findViewById(R.id.mois_actualiser);
        bactualiser.setClickable(true);
        bactualiser.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paramMois();
            }
        });

        infobulle = myView.findViewById(R.id.mois_infobulle);

        infobulle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment dialog = new FragmentDialogPopup();
                dialog.show(getFragmentManager(), "infobulle_mois");
            }
        });

        return myView;
    }

    private void paramMois() {
        if(messageManager.getMessageByTag("mois").getLibelle().equals(Configuration.MESSAGE_VIDE_DEFAUT)){
            lmoisRecette.setText("Aucune transaction effectuée durant les deux derniers mois");
            lmoisDepense.setText("");
            lbilan.setText("Bilan : 0 Trèfle");
        }else{
            String[] items = messageManager.getMessageByTag("mois").getLibelle().split(" ");
            lmoisRecette.setText("Recettes : " + items[0] + " Trèfles");
            lmoisDepense.setText("Dépenses : " + items[1] + " Trèfles");
            Double bilan = Double.parseDouble(items[0].replace(",", ".")) - Double.parseDouble(items[1].replace(",","."));
            bilan = (double)Math.round(bilan*100)/100;
            lbilan.setText("Bilan : " + Double.toString(bilan).replace(".",",") + " Trèfles");
        }
    }
}
