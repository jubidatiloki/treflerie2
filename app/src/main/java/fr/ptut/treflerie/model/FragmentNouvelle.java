package fr.ptut.treflerie.model;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
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
    private ParametreManager parametreManager;
    private boolean commentaireActif;
    private boolean commentaireTropLong;

    private EditText formDest;
    private EditText formMontant;
    private EditText formCommentaire;
    private TextView textCommentaireCompteur;
    private Switch switchCommentaire;
    private Button benvoyer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_nouvelle, container, false);

        formDest = myView.findViewById(R.id.nouvelle_dest);
        formMontant = myView.findViewById(R.id.nouvelle_montant);
        formCommentaire = myView.findViewById(R.id.nouvelle_commentaire);
        switchCommentaire = myView.findViewById(R.id.nouvelle_commentaire_switch);
        textCommentaireCompteur = myView.findViewById(R.id.nouvelle_commentaire_compteur);

        formDest.setOnEditorActionListener(new DoneOnEditorActionListener());
        formMontant.setOnEditorActionListener(new DoneOnEditorActionListener());
        formCommentaire.setOnEditorActionListener(new DoneOnEditorActionListener());

        formDest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                updateButtonEnvoyer();
            }
        });

        formMontant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                updateButtonEnvoyer();
            }
        });

        commentaireTropLong = false;
        formCommentaire.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                int longueurCommentaire = editable.length();
                String message = longueurCommentaire + "/" + Configuration.COMMENTAIRE_LONGUEUR_MAX;

                commentaireTropLong = longueurCommentaire > Configuration.COMMENTAIRE_LONGUEUR_MAX;
                if(commentaireTropLong) {
                    textCommentaireCompteur.setTextColor(getResources().getColor(R.color.colorAccent));
                    message += " Le message est trop long.";
                } else {
                    textCommentaireCompteur.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

                textCommentaireCompteur.setText(message);
                updateButtonEnvoyer();
            }
        });

        commentaireActif = switchCommentaire.isChecked();
        updateVisibilityCommentaire();

        switchCommentaire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                commentaireActif = checked;
                updateVisibilityCommentaire();
                updateButtonEnvoyer();
            }
        });

        parametreManager = new ParametreManager(myView.getContext());
        parametreManager.open();

        benvoyer = myView.findViewById(R.id.nouvelle_envoyer);
        benvoyer.setEnabled(false);

        benvoyer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!formDest.getText().toString().equals("") && !formMontant.getText().toString().equals("")) {
                    //SmsManager.getDefault().sendTextMessage(parametreManager.getParametre().getTelServeur(), null, formMontant.getText().toString()+"/"+formDest.getText().toString(), null, null);
                    String montant = formMontant.getText().toString();
                    String destinaire = formDest.getText().toString();
                    String message = montant + "/" + destinaire;
                    if (commentaireActif) {
                        String commentaire = formCommentaire.getText().toString();
                        message += "##" + commentaire;
                    }

                    SmsManager.getDefault().sendTextMessage(parametreManager.getParametre().getTelServeur(), null, message, null, null);
                    Toast.makeText(getActivity().getBaseContext(), "Transaction en cours", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity().getBaseContext(), "Les champs destinataire et montant doivent être remplis.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return myView;
    }

    private void updateVisibilityCommentaire() {
        if(commentaireActif) {
            formCommentaire.setVisibility(View.VISIBLE);
            textCommentaireCompteur.setVisibility(View.VISIBLE);
        } else {
            formCommentaire.setVisibility(View.INVISIBLE);
            textCommentaireCompteur.setVisibility(View.INVISIBLE);
        }
    }

    private void updateButtonEnvoyer() {
        // Si les champs sont remplis
        if(!formDest.getText().toString().equals("") && !formMontant.getText().toString().equals("")) {
            if(commentaireActif) {
                if(commentaireTropLong) {
                    benvoyer.setEnabled(false);
                } else {
                    benvoyer.setEnabled(true);
                }
            } else {
                benvoyer.setEnabled(true);
            }
        } else {
            benvoyer.setEnabled(false);
        }
    }
}
