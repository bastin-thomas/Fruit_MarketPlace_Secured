package com.mymaraichermobile.message;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PopupMessage extends AppCompatActivity {

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
