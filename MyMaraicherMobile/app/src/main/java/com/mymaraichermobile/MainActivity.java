package com.mymaraichermobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleLanguageAndConfiguration(context.getApplicationContext());

        setContentView(R.layout.activity_main);

        openLoginFragment(context);
    }

    //region Methods


    private void handleLanguageAndConfiguration(Context context) {
        String selectedLanguage = LanguageManager.getLanguage(context);

        if (LanguageManager.isLanguageChanged(context, selectedLanguage.toLowerCase())) {

            LanguageManager.changeLanguage(context, selectedLanguage.toLowerCase());
        }
    }

    private void openLoginFragment(Context context) {
        // Création d'une instance du fragment Login
        LoginFragment loginFragment = new LoginFragment();

        // Récupération du gestionnaire de fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Début de la transaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Remplacement du contenu du conteneur par le fragment Login
        transaction.replace(R.id.fragmentContainer, loginFragment);

        // Ajout de la transaction à la pile arrière pour permettre le retour en arrière si nécessaire
        transaction.addToBackStack(null);

        // Validation de la transaction
        transaction.commit();
    }

    // Paramètres de l'application
    public void openSettings(View view) {
        Intent intent = new Intent(context, SettingsActivity.class);
        startActivity(intent);
    }

    //endregion

}
