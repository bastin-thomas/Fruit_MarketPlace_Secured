package com.mymaraichermobile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getString(R.string.titleSettings));

        findViewById(R.id.backButton).setOnClickListener(view -> {
            finish(); // Termine l'activité en cours, ramenant l'utilisateur à la page précédente
        });
    }

}
