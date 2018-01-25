package fr.ptut.treflerie.model;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    View myView;
    private Button benvoyer;
    private MyCountDownTimer myCountDownTimer = new MyCountDownTimer(40*1000, 1000);
    private ParametreManager parametreManager;
    private MessageManager messageManager;
    private ImageView infobulle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_nouvelle, container, false);

        final EditText formDest = myView.findViewById(R.id.nouvelle_dest);
        final EditText formMontant = myView.findViewById(R.id.nouvelle_montant);
        formDest.setOnEditorActionListener(new DoneOnEditorActionListener());
        formMontant.setOnEditorActionListener(new DoneOnEditorActionListener());

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
        benvoyer.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!formDest.getText().toString().equals("") && !formMontant.getText().toString().equals("")) {

                    String[] items = messageManager.getMessageByTag("solde").getLibelle().split(" ");
                    Double montant = Double.parseDouble(items[0].replace(",", "."));
                    Double montantSaisi = Double.parseDouble(formMontant.getText().toString());
                    if(montant >= montantSaisi){
                        double montantMax = parametreManager.getParametre().getMontantMax();
                        if(montantSaisi <= montantMax ) {
                            SmsManager.getDefault().sendTextMessage(Configuration.TEL_SERVEUR_DEFAUT, null, formMontant.getText().toString() + "/" + formDest.getText().toString(), null, null);
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

    public MyCountDownTimer getTimer(){
        return this.myCountDownTimer;
    }
}
