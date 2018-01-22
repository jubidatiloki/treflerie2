package fr.ptut.treflerie.model;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fr.ptut.treflerie.R;
import fr.ptut.treflerie.controller.*;
import fr.ptut.treflerie.database.*;

/**
 * Created by benja on 07/12/2017.
 */

public class FragmentCompte extends Fragment{

    View myView;
    ParametreManager parametreManager;
    private MessageManager messageManager;
    TextView solde;
    Button bactualiser;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_compte, container, false);
        parametreManager = new ParametreManager(this.getActivity().getBaseContext());
        messageManager = new MessageManager(this.getActivity().getBaseContext());
        parametreManager.open();
        messageManager.open();
        TextView nom = myView.findViewById(R.id.compte_nom);
        solde = myView.findViewById(R.id.compte_solde);
        Parametre param = parametreManager.getParametre();
        nom.setText(param.getNom());
        solde.setText(messageManager.getMessageByTag("solde").getLibelle()+ " Trèfles");
        new SmsSender(Configuration.SMS_SOLDE);

        bactualiser = myView.findViewById(R.id.compte_actualiser);
        bactualiser.setClickable(true);
        bactualiser.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paramSolde();
            }
        });


        return myView;
    }

    public void paramSolde(){
        String[] items = messageManager.getMessageByTag("solde").getLibelle().split(" ");
        Double montant = Double.parseDouble(items[0].replace(",", "."));
        if(montant > 0) {
            solde.setText(Double.toString(montant).replace(".", ",") + " Trèfles");
        }else{
            solde.setText(montant + "Trèfle");
        }
    }

}
