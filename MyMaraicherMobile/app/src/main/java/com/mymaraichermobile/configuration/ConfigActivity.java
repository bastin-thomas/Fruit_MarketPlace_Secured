package com.mymaraichermobile.configuration;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mymaraichermobile.MainActivity;
import com.mymaraichermobile.settings.LanguageManager;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LanguageManager.handleLanguageAndConfiguration(this);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);


        finish(); // Ferme l'activité en cours
    }

    public void afficherPopupErreur(String titre, String message, Context context) {
        // Créer une boîte de dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Définir le titre et le message de la boîte de dialogue
        builder.setTitle(titre)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action à effectuer lorsque l'utilisateur appuie sur le bouton "OK"
                        dialog.dismiss(); // Fermer la boîte de dialogue
                    }
                });

        // Afficher la boîte de dialogue
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
