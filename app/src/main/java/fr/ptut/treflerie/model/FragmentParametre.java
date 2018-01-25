package fr.ptut.treflerie.model;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fr.ptut.treflerie.R;
import fr.ptut.treflerie.database.Parametre;
import fr.ptut.treflerie.database.ParametreManager;

/**
 * Created by benja on 07/12/2017.
 */

public class FragmentParametre extends Fragment{

    View myView;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_parametre, container, false);

        TextView labelNom = myView.findViewById(R.id.l_param_nom);
        TextView labelTelServeur = myView.findViewById(R.id.l_param_telServeur);
        TextView labelMontantMax = myView.findViewById(R.id.l_param_montantMax);
        ParametreManager parametreManager = new ParametreManager(this.getActivity().getBaseContext());
        parametreManager.open();
        Parametre param = parametreManager.getParametre();
        labelNom.setText("Nom d'utilisateur : " + param.getNom());
        labelTelServeur.setText("Numéro du serveur : " + param.getTelServeur());
        labelMontantMax.setText("Limite montant max par transaction : " + Double.toString(param.getMontantMax()) + " Trèfles");



        Button bmodifier = myView.findViewById(R.id.b_param_modifier);
        bmodifier.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentParametreModif()).commit();
            }
        });


        return myView;
    }
}
