package fr.ptut.treflerie.model;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.ptut.treflerie.Listage.MyDividerItemDecoration;
import fr.ptut.treflerie.Listage.TransactionAdapter;
import fr.ptut.treflerie.R;
import fr.ptut.treflerie.database.Transaction;
import fr.ptut.treflerie.database.TransactionManager;

/**
 * Created by benja on 22/01/2018.
 */

public class FragmentHistorique extends Fragment{
    View myView;
    private List<Transaction> transactionsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;
    private TransactionManager transactionManager;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_historique, container, false);
        transactionManager = new TransactionManager(this.getActivity().getBaseContext());
        transactionManager.open();

        recyclerView = myView.findViewById(R.id.recycler_view_transaction);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        transactionAdapter = new TransactionAdapter(transactionsList);
        recyclerView.setAdapter(transactionAdapter);


        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        //RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(getActivity().getBaseContext(), LinearLayoutManager.HORIZONTAL, false);

        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));

        // adding inbuilt divider line
        recyclerView.addItemDecoration(new DividerItemDecoration(myView.getContext(), LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(myView.getContext(), LinearLayoutManager.HORIZONTAL, 8));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        prepareTransactionData();


        return myView;
    }

    private void prepareTransactionData() {
        Transaction transaction;
        for(int i=transactionManager.nombreDeLigne()-1; i>0; i--){
            transaction = transactionManager.getTransactionById(i);
            transactionsList.add(transaction);
        }
    }

}

