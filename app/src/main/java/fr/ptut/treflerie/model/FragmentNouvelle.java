package fr.ptut.treflerie.model;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.Toast;

import fr.ptut.treflerie.FragmentDialogPopup;
import fr.ptut.treflerie.MainActivity;
import fr.ptut.treflerie.MyCountDownTimer;
import fr.ptut.treflerie.R;
import fr.ptut.treflerie.controller.Configuration;
import fr.ptut.treflerie.controller.DoneOnEditorActionListener;
import fr.ptut.treflerie.database.MessageManager;
import fr.ptut.treflerie.database.ParametreManager;

/**
 * Created by benja on 07/12/2017.
 */

public class FragmentNouvelle extends Fragment{

    private View myView;
    private ParametreManager parametreManager;
    private MessageManager messageManager;
    private boolean commentaireActif;

    private boolean commentaireTropLong;
    private EditText formDest;
    private EditText formMontant;
    private EditText formCommentaire;
    private TextView textCommentaireCompteur;
    private Switch switchCommentaire;
    private Button benvoyer;

    private MyCountDownTimer myCountDownTimer = new MyCountDownTimer(40*1000, 1000);

    private ImageView infobulle;


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

        // Set count down timer source activity.
        myCountDownTimer.setSourceActivity((MainActivity) getActivity());

        parametreManager = new ParametreManager(myView.getContext());
        messageManager = new MessageManager(myView.getContext());
        parametreManager.open();
        messageManager.open();

        infobulle = myView.findViewById(R.id.nouvelle_infobulle);

        infobulle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment dialog = new FragmentDialogPopup();
                dialog.show(getFragmentManager(), "infobulle_nouvelle");
            }
        });

        benvoyer = myView.findViewById(R.id.nouvelle_envoyer);
        benvoyer.setEnabled(false);

        benvoyer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!formDest.getText().toString().equals("") && !formMontant.getText().toString().equals("")) {
                    //SmsManager.getDefault().sendTextMessage(parametreManager.getParametre().getTelServeur(), null, formMontant.getText().toString()+"/"+formDest.getText().toString(), null, null);
                    String montantStr = formMontant.getText().toString();
                    String destinaireStr = formDest.getText().toString();
                    String message = montantStr + "/" + destinaireStr;
                    if (commentaireActif) {
                        String commentaire = formCommentaire.getText().toString();
                        message += "##" + commentaire;
                    }

                    String[] items = messageManager.getMessageByTag("solde").getLibelle().split(" ");
                    Double montant = Double.parseDouble(items[0].replace(",", "."));
                    Double montantSaisi = Double.parseDouble(formMontant.getText().toString());
                    if(montant >= montantSaisi){
                        double montantMax = parametreManager.getParametre().getMontantMax();
                        if(montantSaisi <= montantMax ) {
                            SmsManager.getDefault().sendTextMessage(parametreManager.getParametre().getTelServeur(), null,message, null, null);
                            myCountDownTimer.start();
                            Toast.makeText(getActivity().getBaseContext(),"Transaction en cours... Veuillez patienter",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity().getBaseContext(),"Vous avez atteint le plafond du solde que vous avez fixé. Pour le modifier, veuillez vous rendre dans les paramètres de votre compte.",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity().getBaseContext(),"Le solde de votre compte est insuffisant pour réaliser cette transaction.",Toast.LENGTH_LONG).show();
                    }
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
        if (!formDest.getText().toString().equals("") && !formMontant.getText().toString().equals("")) {
            if (commentaireActif) {
                if (commentaireTropLong) {
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

    public MyCountDownTimer getTimer(){
        return this.myCountDownTimer;
    }

}
