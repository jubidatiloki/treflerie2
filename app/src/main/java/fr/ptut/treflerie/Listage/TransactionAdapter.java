package fr.ptut.treflerie.Listage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fr.ptut.treflerie.R;
import fr.ptut.treflerie.database.Transaction;

/**
 * Created by benja on 22/01/2018.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<Transaction> transactionsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date, interlocuteur, montant;

        private MyViewHolder(View view) {
            super(view);
            date =  view.findViewById(R.id.date);
            interlocuteur = view.findViewById(R.id.interlocuteur);
            montant = view.findViewById(R.id.montant);
        }
    }


    public TransactionAdapter(List<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_historique, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Transaction transaction = transactionsList.get(position);
        String[] date = transaction.getDate().split(" ");
        String newDate = "Le " + date[0] + " à " + date[1].split(":")[0] + "h" + date[1].split(":")[1];
        holder.date.setText(newDate);
        if(transaction.getReception() == 1) {
            holder.interlocuteur.setText("Reçu du compte n°" + transaction.getInterlocuteur());
        }else{
            holder.interlocuteur.setText("Envoyé au compte n°" + transaction.getInterlocuteur());
        }
        holder.montant.setText("Montant: " + Double.toString(transaction.getMontant()) + " Trèfles");
    }

    @Override
    public int getItemCount() {
        return transactionsList.size();
    }
}
