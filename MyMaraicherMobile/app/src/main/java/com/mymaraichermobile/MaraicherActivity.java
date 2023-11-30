package com.mymaraichermobile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MaraicherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maraicher);
        setTitle(getString(R.string.titleMaraicher));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Gérer la reprise de l'activité
    }
}