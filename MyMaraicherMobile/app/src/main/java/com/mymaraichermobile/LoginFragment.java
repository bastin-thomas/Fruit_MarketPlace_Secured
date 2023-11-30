package com.mymaraichermobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Context context = getContext();

        EditText usernameInput = view.findViewById(R.id.loginInput);
        EditText passwordInput = view.findViewById(R.id.passwordInput);
        Button loginButton = view.findViewById(R.id.loginButton);

        // Récupérer le nom d'utilisateur et le mot de passe
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        boolean isValid = true;

        // TODO: Logique vérification : Envoyer une requête au serveur


        System.out.println("Username: " + username + ", Password: " + password);

        loginButton.setOnClickListener((View.OnClickListener) v -> {
            // Si login bon, on passe à la page du Maraicher
            if(isValid) {
                Intent intent = new Intent(context, MaraicherActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}