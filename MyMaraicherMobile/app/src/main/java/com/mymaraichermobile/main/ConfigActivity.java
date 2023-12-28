package com.mymaraichermobile.main;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mymaraichermobile.configuration.ConfigManager;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConfigManager.handleLanguageAndConfiguration(this);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish(); // Ferme l'activité en cours
    }

}
