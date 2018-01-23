package fr.ptut.treflerie.model;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.ptut.treflerie.R;
import fr.ptut.treflerie.controller.Configuration;
import fr.ptut.treflerie.controller.DoneOnEditorActionListener;
import fr.ptut.treflerie.database.ParametreManager;

/**
 * Created by benja on 07/12/2017.
 */

public class FragmentNouvelle extends Fragment{

    View myView;
    private Button benvoyer;
    private ParametreManager parametreManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_nouvelle, container, false);

        final EditText formDest = myView.findViewById(R.id.nouvelle_dest);
        final EditText formMontant = myView.findViewById(R.id.nouvelle_montant);
        formDest.setOnEditorActionListener(new DoneOnEditorActionListener());
        formMontant.setOnEditorActionListener(new DoneOnEditorActionListener());

        parametreManager = new ParametreManager(myView.getContext());
        parametreManager.open();

        benvoyer = myView.findViewById(R.id.nouvelle_envoyer);
        benvoyer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!formDest.getText().toString().equals("") && !formMontant.getText().toString().equals("")) {
                    SmsManager.getDefault().sendTextMessage(parametreManager.getParametre().getTelServeur(), null, formMontant.getText().toString()+"/"+formDest.getText().toString(), null, null);
                    Toast.makeText(getActivity().getBaseContext(), "Transaction en cours", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity().getBaseContext(), "Les champs destinataire et montant doivent être remplis.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return myView;
    }
}
