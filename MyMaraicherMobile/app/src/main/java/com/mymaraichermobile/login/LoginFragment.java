package com.mymaraichermobile.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.mymaraichermobile.MaraicherActivity;
import com.mymaraichermobile.R;
import com.mymaraichermobile.client.ProtocoleClient;
import com.mymaraichermobile.client.SocketClient;
import com.mymaraichermobile.configuration.ConfigActivity;

import java.io.IOException;
import java.util.Objects;

public class LoginFragment extends Fragment {

    //region Private variables

    ConfigActivity configActivity = new ConfigActivity();
    SocketClient socket;
    ProtocoleClient client;
    private EditText usernameInput;

    private EditText passwordInput;
    private Button loginButton;
    private CheckBox newAccountChecked;
    
    //endregion

    // Création d'un thread unique pour la connexion socket/serveur
    private class SocketClientThread extends Thread {
        private boolean isChecked;
        public SocketClientThread(boolean isChecked) {
            this.isChecked = isChecked;
        }

        @Override
        public void run() {

            try {
                // Création socket Client
                socket = new SocketClient(requireContext());

                // Liaison socket/serveur
                client = new ProtocoleClient(socket);

                Log.d("SOCKET", "Socket : " + socket);
                Log.d("CLIENT", "Client : " + client);

            } catch (NumberFormatException | IOException e) {

                requireActivity().runOnUiThread(() ->
                        configActivity.afficherPopupErreur("ERROR LOGIN",
                                "Erreur connexion au Serveur + " + e, requireContext()));

                return;
            }

            //region Logique connection

            // On vérifie si c'est un compte existant ou un nouveau à créer

            if(!isChecked) {
                try {
                    Log.d("SENDLOGIN", "TEST");
                    client.sendLogin(usernameInput.getText().toString(), passwordInput.getText().toString());

                } catch (Exception e) {
                    switch (Objects.requireNonNull(e.getMessage())) {
                        case "ENDCONNEXION":
                            Log.d("END LOGIN SERVER", "Erreur connexion au Serveur");
                            break;
                        case "NO_LOGIN":
                            Log.d("NO LOGIN SERVER", "Utilisateur inexistant");
                            break;
                        case "BAD_LOGIN":
                            Log.d("BAD LOGIN SERVER", "Mauvais mot de passe");
                            break;
                        default:
                            Log.d("UNKNOW LOGIN SERVER", "Erreur");
                    }
                }
            } else {

                try {

                    client.sendCreateLogin(usernameInput.getText().toString(), passwordInput.getText().toString());

                }  catch (Exception e) {
                    if (Objects.equals(e.getMessage(), R.string.endconnexion)) {

                        requireActivity().runOnUiThread(() ->
                        configActivity.afficherPopupErreur("ERROR LOGIN", "Erreur connexion au Serveur", requireContext()));

                        return;

                    } else {

                        requireActivity().runOnUiThread(() ->
                        configActivity.afficherPopupErreur("ERROR LOGIN", "L'utilisateur existe déjà", requireContext()));

                        return;

                    }
                }
            }

            //endregion

            // Si login bon, on passe à la page du Maraicher
            Intent intent = new Intent(requireContext(), MaraicherActivity.class);

            // On transfère les infos vers la page Maraicher
            intent.putExtra("socket_key", String.valueOf(socket));
            intent.putExtra("protocol_key", String.valueOf(client));

            startActivity(intent);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //region Variables

        usernameInput = view.findViewById(R.id.loginInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        newAccountChecked = view.findViewById(R.id.checkBoxAccount);

        //endregion

        //region Setters

        loginButton.setEnabled(false);

        //endregion

        //region Vérification not null login
        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                updateButtonState();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                updateButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateButtonState();
            }
        });

        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                updateButtonState();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                updateButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                updateButtonState();
            }
        });

        //endregion

        //region Button click Listener

        loginButton.setOnClickListener(v -> {
            boolean isChecked = newAccountChecked.isChecked();

            new SocketClientThread(isChecked).start();
        });

        //endregion

        return view;
    }

    //region Methods

    @Override
    public void onDestroy() { // FINI
        super.onDestroy();

        try {

            client.sendCancelAll();
            client.sendLogout();
            client.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateButtonState() {
        loginButton.setEnabled(usernameInput.getText().toString().length() > 0 && passwordInput.getText().toString().length() > 0);
    }

    //endregion
}

