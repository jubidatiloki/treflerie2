package fr.ptut.treflerie;

import android.Manifest;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.ptut.treflerie.controller.Configuration;
import fr.ptut.treflerie.database.*;
import fr.ptut.treflerie.model.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ParametreManager parametreManager = new ParametreManager(this);
    private MessageManager messageManager = new MessageManager(this);
    private TextView titleInit;
    private TextView labelNumCompte;
    private EditText formNumCompte;
    private TextView labelTelServeur;
    private EditText formTelServeur;
    private TextView labelMontantMax;
    private EditText formMontantMax;
    private Button benregistrer;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.icon_navbar);

        titleInit = findViewById(R.id.co_title);
        labelNumCompte = findViewById(R.id.co_l_numCompte);
        formNumCompte = findViewById(R.id.co_f_numCompte);
        labelTelServeur = findViewById(R.id.co_l_telServeur);
        formTelServeur = findViewById(R.id.co_f_telServeur);
        labelMontantMax = findViewById(R.id.co_l_montantMax);
        formMontantMax = findViewById(R.id.co_f_montantMax);
        benregistrer = findViewById(R.id.co_b_enregistrer);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        parametreManager.open();
        messageManager.open();
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);



        //si premiere connexion
        if(parametreManager.nombreDeLigne() != 1){

            initialisation();


         //si pas premiere connexion
        }else{
            titleInit.setVisibility(View.INVISIBLE);
            labelNumCompte.setVisibility(View.INVISIBLE);
            formNumCompte.setVisibility(View.INVISIBLE);
            labelTelServeur.setVisibility(View.INVISIBLE);
            formTelServeur.setVisibility(View.INVISIBLE);
            labelMontantMax.setVisibility(View.INVISIBLE);
            formMontantMax.setVisibility(View.INVISIBLE);
            benregistrer.setVisibility(View.INVISIBLE);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentCompte()).commit();
        }

    }

    private void initialisation() {
        Toast.makeText(this, "initialisation", Toast.LENGTH_SHORT).show();
        formTelServeur.setText(Configuration.TEL_SERVEUR_DEFAUT);
        formMontantMax.setText(Double.toString(Configuration.MONTANT_MAX_DEFAUT));

        benregistrer.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v){

                int numCompte = 0;
                formNumCompte.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Remplissez ce formulaire uniquement si vous disposez déjà d'un compte.", Toast.LENGTH_SHORT).show();
                    }
                });
                if(!formNumCompte.getText().toString().equals("")){
                    numCompte = Integer.parseInt(formNumCompte.getText().toString());
                }
                String telServeur = formTelServeur.getText().toString();
                if(telServeur.equals("")){
                    telServeur = Configuration.TEL_PIERRICK;
                }
                double montantMax = 0;
                if(!formMontantMax.getText().toString().equals("")) {
                    montantMax = Double.parseDouble(formMontantMax.getText().toString());
                }else{
                    montantMax = Configuration.MONTANT_MAX_DEFAUT;
                }
                if(telServeur.length() < 10 || montantMax == 0){
                    Toast.makeText(getBaseContext(), "problème de saisie des champs", Toast.LENGTH_LONG).show();
                    if(telServeur.length() != 10){
                        Toast.makeText(getBaseContext(), "le numéro du serveur doit contenir 10 chiffres", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getBaseContext(), "le montant max doit être supérieur à 0", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Parametre param;
                    if(numCompte == 0) {
                        param = new Parametre(telServeur, montantMax);
                    }else{
                        param = new Parametre(numCompte, telServeur, montantMax);
                        param.setNom("compte n°"+param.getNumCompte());
                    }
                    message = new Message("solde", Configuration.MESSAGE_VIDE_DEFAUT);
                    messageManager.insertMessage(message);
                    message = new Message("derniere", Configuration.MESSAGE_VIDE_DEFAUT);
                    messageManager.insertMessage(message);
                    message = new Message("mois", Configuration.MESSAGE_VIDE_DEFAUT);
                    messageManager.insertMessage(message);
                    message = new Message("envoi", Configuration.MESSAGE_VIDE_DEFAUT);
                    messageManager.insertMessage(message);
                    message = new Message("reception", Configuration.MESSAGE_VIDE_DEFAUT);
                    messageManager.insertMessage(message);

                    System.out.println(param.toString());
                    parametreManager.insertParametre(param);
                    titleInit.setVisibility(View.INVISIBLE);
                    labelNumCompte.setVisibility(View.INVISIBLE);
                    formNumCompte.setVisibility(View.INVISIBLE);
                    labelTelServeur.setVisibility(View.INVISIBLE);
                    formTelServeur.setVisibility(View.INVISIBLE);
                    labelMontantMax.setVisibility(View.INVISIBLE);
                    formMontantMax.setVisibility(View.INVISIBLE);
                    benregistrer.setVisibility(View.INVISIBLE);

                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentCompte()).commit();
                }

            }});
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if(parametreManager.nombreDeLigne() == 1) {
            if (id == R.id.nav_derniere) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentDerniere()).commit();
            } else if (id == R.id.nav_mois) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentMois()).commit();
            } else if (id == R.id.nav_nouvelle) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentNouvelle()).commit();
            } else if (id == R.id.nav_compte) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentCompte()).commit();
            } else if (id == R.id.nav_aide) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentAide()).commit();
            } else if (id == R.id.nav_parametre) {
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentParametre()).commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }else{
            Toast.makeText(this, "finissez l'initialisation", Toast.LENGTH_LONG).show();
            return false;
        }
    }


}
