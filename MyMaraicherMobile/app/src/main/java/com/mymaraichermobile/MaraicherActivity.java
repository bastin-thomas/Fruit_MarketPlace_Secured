package com.mymaraichermobile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mymaraichermobile.client.Articles;
import com.mymaraichermobile.client.CaddieRows;
import com.mymaraichermobile.client.ProtocoleClient;
import com.mymaraichermobile.client.SocketClient;
import com.mymaraichermobile.configuration.ConfigActivity;
import com.mymaraichermobile.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

public class MaraicherActivity extends AppCompatActivity {
    // TODO Faire LoginId + add and delete(/ALl) Buttons

    //region Private variables

    ConfigActivity configActivity;

    // Infos connexions
    private SocketClient socket;
    private ProtocoleClient client;
    private String loginId;

    // Variables pour stocker les IDs
    private ImageView imageFruitView;
    private Button previousButton;
    private Button nextButton;
    private Button addCaddieButton;
    private Button deleteButton;
    private Button deleteAllButton;
    private Button confirmButton;
    private TextView articleListView;
    private TextView priceListView;
    private TextView stockListView;

    // Variables pour le panier
    private ListView caddieListView;
    private TextView totalText;
    private float totalPrice;
    private int currentArticle;
    private ArrayList<CaddieRows> caddie;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maraicher);
        setTitle(getString(R.string.titleMaraicher));

        //region GetIntent

        // On récupère la socket et le protocole lors de la connexion
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            socket = extras.getParcelable("socket_key");
            client = extras.getParcelable("protocol_key");

            Log.d("SOCKET", "Socket : " + socket);
            Log.d("CLIENT", "Client : " + client);
        }

        //endregion

        //region Variables initialisation

        // variables IDS
        imageFruitView = findViewById(R.id.imageView);
        quantityEditText = findViewById(R.id.quantityEditText);
        articleListView = findViewById(R.id.articleListView);
        priceListView = findViewById(R.id.priceListView);
        stockListView = findViewById(R.id.stockListView);

        // variables Buttons
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        addCaddieButton = findViewById(R.id.addCaddieButton);
        deleteButton = findViewById(R.id.deleteButton);
        deleteAllButton = findViewById(R.id.deleteAllButton);
        confirmButton = findViewById(R.id.confirmButton);

        previousButton.setEnabled(false);

        // variables panier
        currentArticle = 0;
        totalPrice = (float) 0.0;
        caddie = new ArrayList<>();
        caddieListView = findViewById(R.id.caddieListView);
        totalText = findViewById(R.id.totalText);

        //endregion

        //region Button Listener
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPreviousElement();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextElement();
            }
        });

        addCaddieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCaddie();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteArticle();
            }
        });

        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCaddie();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendConfirm(); // Envoie la requête de confirmation au serveur pour valider le Caddie
            }
        });

        //endregion

    }

    //region Methods

    // Application
    @Override
    protected void onDestroy() { // FINI
        super.onDestroy();

        try {
            client.sendCancelAll();
            client.sendLogout();
            client.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Paramètres
    public void openSettings(View view) { // FINI
        Intent intent = new Intent(this, SettingsActivity.class);

        intent.putExtra("class_name", MaraicherActivity.class.getName());
        startActivity(intent);

        finish();
    }

    // Pour revenir à la page de connexion
    public void Disconnect(View view) { // FINI
        try {
            client.sendCancelAll();
            client.sendLogout();
            client.close();

        } catch (Exception ex) {
            this.runOnUiThread(() ->
                    configActivity.afficherPopupErreur("ERROR LOGOUT DISCONNECT : ", String.valueOf(ex.getMessage()), this));
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        finish();
    }

    // Caddie
    private void showPreviousElement() { // FINI
        if(!this.previousButton.isEnabled())
        {
            this.nextButton.setEnabled(true);
        }

        try{

            refreshArticle(currentArticle - 1); // Met à jour les détails du produit affiché précédent
            currentArticle--;


        } catch (Exception ex) {
            switch (ex.getMessage()) {

                case "ENDCONNEXION" -> {
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("ERROR PREVIOUS BUTTON", ("Erreur transmission des données : " + ex.getMessage()), this));
                }

                case "NO_ARTICLE_FOUND" -> // On désactive le bouton car plus d'article précédent
                        this.previousButton.setEnabled(false);

                case "PARAMS_FORMAT_ERROR" -> {

                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("ERROR FORMAT PREVIOUS BUTTON", ("MAUVAIS FORMAT : " + ex.getMessage()), this));
                }

                default ->
                        this.runOnUiThread(() ->
                                configActivity.afficherPopupErreur("UNKNOW ERROR PREVIOUS BUTTON", ("ERREUR INCONNUE : " + ex.getMessage()), this));
            }

        }

    }

    private void showNextElement() { // FINI
        if (!this.nextButton.isEnabled()) {
            this.previousButton.setEnabled(true);
        }

        try {

            refreshArticle(currentArticle + 1); // Met à jour les détails du produit affiché précédent
            currentArticle++;


        } catch (Exception ex) {
            switch (ex.getMessage()) {

                case "ENDCONNEXION" -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("ERROR PREVIOUS BUTTON", ("Erreur transmission des données : " + ex.getMessage()), this));

                case "NO_ARTICLE_FOUND" -> // On désactive le bouton car plus d'article suivant
                        this.nextButton.setEnabled(false);

                case "PARAMS_FORMAT_ERROR" -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("ERROR FORMAT PREVIOUS BUTTON", ("MAUVAIS FORMAT : " + ex.getMessage()), this));

                default -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("UNKNOW ERROR PREVIOUS BUTTON", ("ERREUR INCONNUE : " + ex.getMessage()), this));
            }

        }
    }

    private void refreshArticle(int index) throws Exception { // FINI
        // Met à jour les détails du produit affiché en fonction de l'image actuelle
        Articles art = client.sendConsult(index);

        try {

            this.articleListView.setText("");
            this.priceListView.setText("");
            this.stockListView.setText("");

            this.imageFruitView.setImageDrawable(Drawable.createFromPath("./images/" + art.getImage()));

        } catch (Exception ex) {

            this.runOnUiThread(() ->
                    configActivity.afficherPopupErreur("LOAD FILE", "CANNOT LOAD FILE : " + ex.getMessage(), this));
            return;
        }

        this.articleListView.setText(art.getIntitule());
        this.priceListView.setText("" + art.getPrix());
        this.stockListView.setText("" + art.getStock());

    }

    private void RefreshCaddie() { // FINI
        // Met à jour l'affichage du panier avec la liste des articles actuels
        caddie.clear();

        ArrayAdapter<Vector<String>> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        adapter.clear();

        try{
            caddie = client.sendCaddie();
        }catch(Exception ex){
            switch (Objects.requireNonNull(ex.getMessage())) {
                case "ENDCONNEXION":
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("ERROR CONNECTION REFRESH CADDIE", "ERREUR TRANSMISSION DES DONNEES : " + ex.getMessage(), this));
                    break;

                case "PARAMS_FORMAT_ERROR":
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("ERROR FORMAT REFRESH CADDIE", "MAUVAIS FORMAT : " + ex.getMessage(), this));
                    break;

                default:
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("UNKNOW ERROR REFRESH CADDIE", "ERREUR INCONNUE : " + ex.getMessage(), this));
                    break;
            }
        }

        this.totalPrice = 0;

        // Récuperation des données de la bdd
        for(CaddieRows tmp : caddie){
            Vector<String> row = new Vector<>();
            row.add("" + tmp.getIntitule());
            row.add("" + tmp.getQuantitee());
            row.add("" + tmp.getPrix());

            this.totalPrice += tmp.getPrix() * ((float)tmp.getQuantitee());

            adapter.add(row);

        }

        this.caddieListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        this.totalText.setText("" + totalPrice);
    }

    private void addToCaddie () {
        // Ajoute le produit actuel à la liste des produits
        // articleList.add(article);

        RefreshCaddie(); // Met à jour l'affichage du panier
    }

    private void deleteArticle() {
        // Supprime l'article sélectionné


        // Met à jour le panier
        RefreshCaddie();

    }

    private void deleteCaddie() {
        // Supprime tous les articles du panier


        // Met à jour le panier
        RefreshCaddie();

    }

    private void sendConfirm() { // FINI
        // On confirme le panier et on crée la facture

        try {
            client.sendConfirmer(loginId);

        } catch(Exception ex){
            switch(ex.getMessage()) {
                case "ENDCONNEXION" -> {
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("ERROR DATA CONFIRM", "ERREUR TRANSMISSION DONNEES : "
                                    + ex.getMessage(), this));
                    return;
                }

                case "PARAMS_FORMAT_ERROR" -> {
                    this.runOnUiThread(() -> configActivity.afficherPopupErreur("ERROR FORMAT CONFIRM", "MAUVAIS FORMAT : "
                            + ex.getMessage(), this));
                    return;
                }

                case "ERROR_BILL" -> {
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("ERROR SERVEUR/BILL CONFIRM", "ERREUR CREATION FACTURE : "
                                    + ex.getMessage(), this));
                    return;
                }

                default -> {
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("DEFAULT ERROR CONFIRM", "ERREUR INCONNUE : "
                                    + ex.getMessage(), this));
                    return;
                }
            }
        }

        this.runOnUiThread(() ->
                configActivity.afficherPopupErreur("SUCCESS CONFIRM", "La commande a bien été enregistrée.", this));

        RefreshCaddie();
    }

    //endregion

}