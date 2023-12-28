package com.mymaraichermobile.GUI.main;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mymaraichermobile.configuration.ConfigHandler;
import com.mymaraichermobile.GUI.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConfigHandler.handleLanguageAndConfiguration(this);

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish(); // Ferme l'activité en cours
    }

}
