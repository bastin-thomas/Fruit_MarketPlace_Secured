package com.mymaraichermobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    String selectedLanguage = "";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getString(R.string.titleSettings));

        //region Variables

        String currentLanguage = LanguageManager.getLanguage(this);
        Spinner languages = findViewById(R.id.languageSpinner);

        // Création d'un adaptateur pour les options du Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.language_options,
                android.R.layout.simple_spinner_item
        );

        // On spécifie la disposition pour les options
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // L'adaptateur est appliqué sur le Spinner
        languages.setAdapter(adapter);

        // On récupére l'indice de la dernière valeur sélectionnée
        String lastSelectedLanguage = LanguageManager.getLanguage(this);
        int position = getPositionForLanguage(lastSelectedLanguage);
        languages.setSelection(position);

        //endregion

        //region Listener itemSelected

        // Un écouteur pour détecter les changements de sélection
        languages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedLanguage = getResources().getStringArray(R.array.language_options)[position];

                if (!currentLanguage.equals(selectedLanguage.toLowerCase())) {

                    // Pour changer la langue en fonction de la sélection
                    LanguageManager.changeLanguage(context, selectedLanguage.toLowerCase());

                    // On sauvegarde la langue choisie
                    LanguageManager.saveLanguage(context, selectedLanguage);

                    String cur = LanguageManager.getLanguage(context);
                    Log.d("SettingsActivity", "NomCurrent: " + cur);
                    Log.d("SettingsActivity", "NomSelected: " + selectedLanguage);

                    recreate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Ne rien faire ici
            }
        });
        //endregion

        //region End Settings
        findViewById(R.id.backButton).setOnClickListener(view -> {
            finish();// Termine l'activité en cours, ramenant l'utilisateur à la page précédente

            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        });

        //endregion

    }

    //region Methods
    private int getPositionForLanguage(String language) {
        String[] languageOptions = getResources().getStringArray(R.array.language_options);
        for (int i = 0; i < languageOptions.length; i++) {
            if (languageOptions[i].equalsIgnoreCase(language)) {
                return i;
            }
        }
        return 0; // Par défaut, retournez la première position si la langue n'est pas trouvée
    }
    //endregion
}
