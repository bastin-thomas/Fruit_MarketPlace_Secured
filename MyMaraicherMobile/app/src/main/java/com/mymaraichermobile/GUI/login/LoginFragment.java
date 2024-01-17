package com.mymaraichermobile.GUI.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.mymaraichermobile.GUI.PopupMessage;
import com.mymaraichermobile.GUI.maraicher.MaraicherActivity;
import com.mymaraichermobile.R;
import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleClient;
import com.mymaraichermobile.model.ProtocoleMarket.SocketClient;
import com.mymaraichermobile.model.SocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class LoginFragment extends Fragment {

    //region Private variables
    SocketClient socket;
    ProtocoleClient client;

    PopupMessage popupMessage = new PopupMessage();

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private CheckBox newAccountChecked;
    //endregion

    @SuppressLint("StaticFieldLeak")
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

            // On vérifie si c'est un compte existant ou un nouveau à créer
            new AsyncTask<Bundle, Void, HashMap<String, Object>>() {
                @Override
                protected HashMap<String, Object> doInBackground(Bundle... bundles) {
                    HashMap<String, Object> props = new HashMap<String, Object>(){};

                    try {
                        // Création socket Client
                        socket = new SocketClient(requireContext());

                        // Liaison socket/serveur
                        client = new ProtocoleClient(socket);

                        Log.d("SOCKET", "Socket : " + socket);
                        Log.d("CLIENT", "Client : " + client);

                        props.put("isFailed", false);
                    } catch (NumberFormatException | IOException ex) {
                        props.put("exception", ex);
                        props.put("isFailed", true);
                        return props;
                    }

                    //region Logique connection
                    if (!isChecked) {
                        try {
                            client.sendLogin(usernameInput.getText().toString(), passwordInput.getText().toString());
                            props.put("isFailed", false);
                        } catch (Exception ex) {
                            props.put("exception", ex);
                            props.put("isFailed", true);
                            return props;
                        }
                    } else {
                        try {
                            client.sendCreateLogin(usernameInput.getText().toString(), passwordInput.getText().toString());
                            props.put("isFailed", false);
                        } catch (Exception ex) {
                            props.put("exception", ex);
                            props.put("isFailed", true);
                            return props;
                        }
                    }

                    return props;
                    //endregion
                }

                @Override
                protected void onPostExecute(HashMap<String, Object> props) {
                    super.onPostExecute(props);
                    if (!((Boolean) props.get("isFailed"))) {
                        // Si login bon, on passe à la page du Maraicher
                        Intent intent = new Intent(requireContext(), MaraicherActivity.class);

                        // On transfère les infos vers la page Maraicher
                        SocketHandler.setProtocol(client);
                        intent.putExtra("loginId_key", usernameInput.getText().toString());

                        startActivity(intent);
                    } else {
                        Exception ex = (Exception) props.get("exception");
                        switch (Objects.requireNonNull(ex.getMessage())) {
                            case "ENDCONNEXION" -> {
                                getActivity().runOnUiThread(()->{
                                    popupMessage.afficherPopupErreur("ERROR LOGIN SERVER",
                                            "ERREUR CONNEXION SERVEUR" + ex.getMessage(), requireContext());
                                });
                            }
                            case "NO_LOGIN" -> {
                                getActivity().runOnUiThread(()->{
                                    popupMessage.afficherPopupErreur("ERROR LOGIN ACCOUNT",
                                            "UTILISATEUR EXISTANT : " + ex.getMessage(), requireContext());
                                });
                            }
                            case "BAD_LOGIN" -> {
                                getActivity().runOnUiThread(()->{
                                    popupMessage.afficherPopupErreur("ERROR BAD LOGIN",
                                            "MAUVAIS LOGIN : " + ex.getMessage(), requireContext());
                                });
                            }
                            default -> {
                                getActivity().runOnUiThread(()->{
                                    popupMessage.afficherPopupErreur("UNKNOW ERROR",
                                            "ERREUR INCONNUE : " + ex.getMessage(), requireContext());
                                });
                            }
                        }
                    }
                }
            }.execute();
        });
        //endregion

        return view;
    }

    //region Events

    @Override
    public void onDestroy() { // FINI
        super.onDestroy();

        try {
            client.close();
        } catch (Exception ignored) {

        }
    }

    public void updateButtonState() {
        loginButton.setEnabled(usernameInput.getText().toString().length() > 0 && passwordInput.getText().toString().length() > 0);
    }

    //endregion
}

