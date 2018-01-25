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
import fr.ptut.treflerie.controller.JabberReceiver;
import fr.ptut.treflerie.database.*;
import fr.ptut.treflerie.model.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ParametreManager parametreManager = new ParametreManager(this);
    private MessageManager messageManager = new MessageManager(this);
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.icon_navbar);

        parametreManager.open();
        messageManager.open();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentCompte()).commit();
        if(parametreManager.nombreDeLigne() == 0){
            getSupportActionBar().setTitle("0 Trèfles");
        }else {
            getSupportActionBar().setTitle(parametreManager.getParametre().getNumCompte() + " : " + parametreManager.getParametre().getSolde() + " T");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECEIVE_SMS},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},1);


        //---------------------------------------//
        JabberReceiver jr = new JabberReceiver(this.getApplicationContext(), this.getIntent());
        jr.start();
       // jr.send("V?");
        //---------------------------------------//

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
        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

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
                getSupportActionBar().setTitle(parametreManager.getParametre().getSolde() + " T");
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentDerniere()).commit();
            } else if (id == R.id.nav_mois) {
                getSupportActionBar().setTitle(parametreManager.getParametre().getSolde() + " T");
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentMois()).commit();
            }else if (id == R.id.nav_historique) {
                getSupportActionBar().setTitle(parametreManager.getParametre().getSolde() + " T");
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentHistorique()).commit();
            }else if (id == R.id.nav_nouvelle) {
                getSupportActionBar().setTitle(parametreManager.getParametre().getSolde() + " T");
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentNouvelle()).commit();
            } else if (id == R.id.nav_compte) {
                getSupportActionBar().setTitle(parametreManager.getParametre().getSolde() + " T");
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentCompte()).commit();
            } else if (id == R.id.nav_aide) {
                getSupportActionBar().setTitle(parametreManager.getParametre().getSolde() + " T");
                fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentAide()).commit();
            } else if (id == R.id.nav_parametre) {
                getSupportActionBar().setTitle(parametreManager.getParametre().getSolde() + " T");
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
