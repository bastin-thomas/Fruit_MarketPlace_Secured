package com.mymaraichermobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.titleLogin));

        //region FRAGMENT LOGIN
        /* FRAGMENT LOGIN */
        // Créez une instance du fragment qu'on veut démarrer
        LoginFragment lFragment = new LoginFragment();

        // On récupère le gestionnaire de fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Commencez une transaction pour ajouter le fragment au conteneur
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Remplacez le contenu du conteneur par votre fragment
        transaction.replace(R.id.fragmentContainer, lFragment);

        // Ajoutez la transaction à la pile arrière pour permettre le retour en arrière si nécessaire
        transaction.addToBackStack(null);

        // Validez la transaction
        transaction.commit();
        //endregion
    }

    // Paramètres de l'application
    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


}
