package com.mymaraichermobile.GUI.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.mymaraichermobile.R;
import com.mymaraichermobile.configuration.ConfigManager;

public class SettingsActivity extends AppCompatActivity {

    //region Variables
    String selectedLanguage = "";
    Context context = this;
    String nameClass;
    Class<?> targetClass;
    EditText choiceIp;
    EditText choicePort;
    Button saveButton;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //region Variables

        String currentLanguage = ConfigManager.getLanguage(this);
        String propIp = ConfigManager.getIp(this);
        String propPort = ConfigManager.getPort(this);
        Spinner languages = findViewById(R.id.languageSpinner);
        saveButton = findViewById(R.id.saveButton);
        choiceIp = findViewById(R.id.ChoiceIp);
        choicePort = findViewById(R.id.ChoicePort);

        // On récupère la classe appellante
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nameClass = extras.getString("class_name");

            try {

                targetClass = Class.forName(nameClass);

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            Log.d("CLASSE", "Classe : " + targetClass.getName());
        }

        // Chargement du fichier de config
        choiceIp.setText(propIp);
        choicePort.setText(propPort);

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
        String lastSelectedLanguage = ConfigManager.getLanguage(this);
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
                    ConfigManager.changeLanguage(context, selectedLanguage.toLowerCase());

                    ConfigManager.refreshUi(context);

                    String cur = ConfigManager.getLanguage(context);
                    Log.d("SettingsActivity", "NomCurrent: " + cur);
                    Log.d("SettingsActivity", "NomSelected: " + selectedLanguage);
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

            Intent intent = new Intent(context, targetClass);
            context.startActivity(intent);
        });

        saveButton.setOnClickListener(v ->
                ConfigManager.saveConfig(context, choiceIp.getText().toString(),
                        Integer.parseInt(choicePort.getText().toString())));

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

    private void saveConfig() {

    }
    //endregion
}
