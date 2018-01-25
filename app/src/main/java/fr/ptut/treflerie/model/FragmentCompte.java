package fr.ptut.treflerie.model;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import fr.ptut.treflerie.FragmentDialogPopup;
import fr.ptut.treflerie.R;
import fr.ptut.treflerie.controller.*;
import fr.ptut.treflerie.database.*;

/**
 * Created by benja on 07/12/2017.
 */

public class FragmentCompte extends Fragment{

    private View myView;
    private ParametreManager parametreManager;
    private MessageManager messageManager;
    private TextView solde;
    private TextView nom;
    private Button bactualiser;
    private ImageView infobulle;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_compte, container, false);
        parametreManager = new ParametreManager(this.getActivity().getBaseContext());
        messageManager = new MessageManager(this.getActivity().getBaseContext());
        parametreManager.open();
        messageManager.open();

        nom = myView.findViewById(R.id.compte_nom);
        solde = myView.findViewById(R.id.compte_solde);
        infobulle = myView.findViewById(R.id.compte_infobulle);

        infobulle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment dialog = new FragmentDialogPopup();
                dialog.show(getFragmentManager(), "infobulle_compte");
            }
        });

        if(parametreManager.nombreDeLigne() != 1 ){
            initialisation();
        }else {
            Parametre param = parametreManager.getParametre();
            nom.setText(param.getNom());
            if(messageManager.nombreDeLigne()>0) {
                solde.setText(messageManager.getMessageByTag("solde").getLibelle() + " Trèfles");
            }else{
                solde.setText("0 Trèfle");
            }
            new SmsSender(Configuration.SMS_SOLDE, myView.getContext());
        }
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
        if(messageManager.nombreDeLigne()>0) {
            String[] items = messageManager.getMessageByTag("solde").getLibelle().split(" ");
            Double montant = Double.parseDouble(items[0].replace(",", "."));
            if (montant > 0) {
                solde.setText(Double.toString(montant).replace(".", ",") + " Trèfles");
            } else {
                solde.setText(montant + "Trèfle");
            }
        }else{
            solde.setText("0 Trèfle");
        }
    }

    public void initialisation(){
        parametreManager.insertParametre(new Parametre());
        new SmsSender(Configuration.SMS_SOLDE, myView.getContext());
        nom.setText("Paramètrage du compte en cours, en attente du serveur");
        nom.setTextSize(10);

    }

}
