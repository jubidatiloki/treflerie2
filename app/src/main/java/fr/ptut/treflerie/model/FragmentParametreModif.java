package fr.ptut.treflerie.model;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.ptut.treflerie.R;
import fr.ptut.treflerie.controller.DoneOnEditorActionListener;
import fr.ptut.treflerie.database.Parametre;
import fr.ptut.treflerie.database.ParametreManager;

/**
 * Created by benja on 12/12/2017.
 */

public class FragmentParametreModif extends Fragment{

    View myViewModif;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myViewModif = inflater.inflate(R.layout.layout_parametre_modif, container, false);
        final EditText formNom = myViewModif.findViewById(R.id.f_paramM_nom);
        final EditText formTelServeur = myViewModif.findViewById(R.id.f_param_telServeur);
        final EditText formMontantMax = myViewModif.findViewById(R.id.f_param_montantMax);
        final ParametreManager parametreManager = new ParametreManager(this.getActivity().getBaseContext());
        parametreManager.open();
        formNom.setText(parametreManager.getParametre().getNom());
        formNom.setOnEditorActionListener(new DoneOnEditorActionListener());
        formTelServeur.setText(parametreManager.getParametre().getTelServeur(),TextView.BufferType.EDITABLE);
        formTelServeur.setOnEditorActionListener(new DoneOnEditorActionListener());
        formMontantMax.setText(Double.toString(parametreManager.getParametre().getMontantMax()),TextView.BufferType.EDITABLE);
        formMontantMax.setOnEditorActionListener(new DoneOnEditorActionListener());

        Button benregistrer = myViewModif.findViewById(R.id.b_param_enregistrer);
        benregistrer.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){
                if(formTelServeur.getText().toString().equals("") || formMontantMax.getText().toString().equals("") || formNom.getText().toString().equals("")){
                    Toast.makeText(myViewModif.getContext(), "Les formulaires ne doivent pas être vides!!", Toast.LENGTH_LONG).show();
                }

                Parametre paramOrigine = parametreManager.getParametre();
                Parametre param = new Parametre(paramOrigine.getNumCompte(), formTelServeur.getText().toString(), Double.parseDouble(formMontantMax.getText().toString()), formNom.getText().toString(), paramOrigine.getSolde());
                parametreManager.updateParametre(param);


                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentParametre()).commit();
            }
        });

        return myViewModif;
    }
}
