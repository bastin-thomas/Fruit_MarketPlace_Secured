package com.mymaraichermobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {

    //region Private variables
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;

    //endregion
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        handleLanguageAndConfiguration(requireContext());

        //region Variables

        usernameInput = view.findViewById(R.id.loginInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        boolean isValid = true;

        //endregion


        //region Setters

        loginButton.setEnabled(false);

        //endregion



        //region Vérification login
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

        // TODO: Logique vérification : Envoyer une requête au serveur, renvoie un booléen
        SendLogin();

        loginButton.setOnClickListener((View.OnClickListener) v -> {
            // Si login bon, on passe à la page du Maraicher
            if(isValid) {
                Intent intent = new Intent(requireContext(), MaraicherActivity.class);
                startActivity(intent);
            }
        });

        //endregion

        return view;
    }

    //region Methods

    private void handleLanguageAndConfiguration(Context context) {
        String selectedLanguage = LanguageManager.getLanguage(context);

        if (LanguageManager.isLanguageChanged(context, selectedLanguage.toLowerCase())) {

            LanguageManager.changeLanguage(context, selectedLanguage.toLowerCase());
        }
    }

    public void updateButtonState() {
        loginButton.setEnabled(usernameInput.getText().toString().length() > 0 && passwordInput.getText().toString().length() > 0);
    }

    private void SendLogin() {
    }

    //endregion

}