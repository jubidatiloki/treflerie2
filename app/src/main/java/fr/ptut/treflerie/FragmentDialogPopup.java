package fr.ptut.treflerie;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.ptut.treflerie.model.FragmentCompte;

/**
 * Created by benja on 24/01/2018.
 */

public class FragmentDialogPopup extends DialogFragment {




    public FragmentDialogPopup(){}


    public final static String EXTRA_MESSAGE = "fr.ptut.treeflerie.model.MESSAGE";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        /*
        if(getActivity().getIntent()!=null){
            String message = getActivity().getIntent().getStringExtra(this.EXTRA_MESSAGE);
        }*/

        String tag = getTag();
        switch (tag){
            case "infobulle_aide":
                builder.setTitle("Explications - aide");
                builder.setMessage(R.string.infobulle_aide);
                break;
            case "infobulle_compte":
                builder.setTitle("Explications - compte");
                builder.setMessage(R.string.infobulle_compte);
                break;
            case "infobulle_derniere":
                builder.setTitle("Explications - dernière transaction");
                builder.setMessage(R.string.infobulle_derniere);
                break;
            case "infobulle_mois":
                builder.setTitle("Explications - bilan");
                builder.setMessage(R.string.infobulle_mois);
                break;
            case "infobulle_nouvelle":
                builder.setTitle("Explications - nouvelle transaction");
                builder.setMessage(R.string.infobulle_nouvelle);
                break;
            default:
                builder.setTitle("Explications - 42");
                builder.setMessage("bug dans la matrice");
                break;
        }



        builder.setPositiveButton("d\'accord                       ", new okOnClickListener());


        // Create the AlertDialog object and return it
        return builder.create();

    }

    private final class okOnClickListener implements  DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {

        }
    }



}
