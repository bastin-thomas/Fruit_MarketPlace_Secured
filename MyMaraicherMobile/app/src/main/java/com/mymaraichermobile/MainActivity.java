package com.mymaraichermobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mymaraichermobile.login.LoginFragment;
import com.mymaraichermobile.settings.LanguageManager;
import com.mymaraichermobile.settings.SettingsActivity;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageManager.handleLanguageAndConfiguration(this);

        setContentView(R.layout.activity_main);

        openLoginFragment();
    }

    //region Methods

    private void openLoginFragment() {
        // Création d'une instance du fragment Login
        LoginFragment loginFragment = new LoginFragment();

        // Récupération du gestionnaire de fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Début de la transaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Remplacement du contenu du conteneur par le fragment Login
        transaction.replace(R.id.fragmentContainer, loginFragment);

        // Validation de la transaction
        transaction.commit();
    }

    // Paramètres de l'application
    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);

        // Pour retrouver la classe qui utilise l'activité
        intent.putExtra("class_name", MainActivity.class.getName());
        startActivity(intent);

        finish();
    }

    //endregion

}
