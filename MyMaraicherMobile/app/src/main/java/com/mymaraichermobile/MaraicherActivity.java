package com.mymaraichermobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mymaraichermobile.client.Achats;
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
    private EditText quantityEditText;
    private float totalPrice;
    private int currentArticle;
    private ArrayList<CaddieRows> caddie;
    ArrayAdapter<Achats> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

    //endregion

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
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
            loginId = extras.getParcelable("loginId_key");

            Log.d("ETAT SOCKET", "Socket : " + socket);
            Log.d("ETAT CLIENT", "Client : " + client);
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

        // Buttons Initialisation
        previousButton.setEnabled(false);
        nextButton.setEnabled(true);

        // variables panier
        currentArticle = 0;
        totalPrice = (float) 0.0;
        caddie = new ArrayList<>();
        caddieListView = findViewById(R.id.caddieListView);
        caddieListView.setAdapter(adapter);
        caddieListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        totalText = findViewById(R.id.totalText);

        //endregion

        //region Button Listener

        caddieListView.setOnItemClickListener((parent, view, index, id) -> {

            /*if (caddieListView.isItemChecked(index)) {

                deleteButton.setEnabled(true);

            } else {

                // Aucun élément sélectionné
                deleteButton.setEnabled(false);

            }*/

            deleteButton.setEnabled(caddieListView.isItemChecked(index));
        });

        previousButton.setOnClickListener(v ->
                showPreviousElement());

        nextButton.setOnClickListener(v ->
                showNextElement());

        addCaddieButton.setOnClickListener(v ->
                addToCaddie());

        deleteButton.setOnClickListener(v ->
                deleteArticle());

        deleteAllButton.setOnClickListener(v ->
                deleteCaddie());

        confirmButton.setOnClickListener(v ->
            sendConfirm()); // Envoie la requête de confirmation au serveur pour valider le Caddie

        //endregion

    }

    //region Methods

    // Application

    // S'exécute avant que l'application s'arrête
    @Override
    protected void onDestroy() {
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
    public void disconnect(View view) {
        try {

            client.sendCancelAll();
            client.sendLogout();
            client.close();

        } catch (Exception ex) {
            this.runOnUiThread(() ->
                    configActivity.afficherPopupErreur("ERROR LOGOUT DISCONNECT : ",
                            String.valueOf(ex.getMessage()), this));
            return;
        }

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        finish();
    }

    // Caddie
    private void showPreviousElement() {

        if(!this.previousButton.isEnabled())
        {
            this.nextButton.setEnabled(true);
        }

        try{

            refreshArticle(currentArticle - 1); // Met à jour les détails du produit affiché précédent
            currentArticle--;


        } catch (Exception ex) {
            switch (Objects.requireNonNull(ex.getMessage())) {

                case "ENDCONNEXION" ->
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("ERROR PREVIOUS BUTTON", ("Erreur transmission des données : "
                                    + ex.getMessage()), this));

                case "NO_ARTICLE_FOUND" -> // On désactive le bouton car plus d'article précédent
                        this.previousButton.setEnabled(false);

                case "PARAMS_FORMAT_ERROR" ->

                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("ERROR FORMAT PREVIOUS BUTTON", ("MAUVAIS FORMAT : "
                                    + ex.getMessage()), this));

                default ->
                        this.runOnUiThread(() ->
                                configActivity.afficherPopupErreur("UNKNOW ERROR PREVIOUS BUTTON", ("ERREUR INCONNUE : "
                                        + ex.getMessage()), this));
            }

        }

    }

    private void showNextElement() {

        if (!this.nextButton.isEnabled()) {
            this.previousButton.setEnabled(true);
        }

        try {

            refreshArticle(currentArticle + 1); // Met à jour les détails du produit affiché précédent
            currentArticle++;


        } catch (Exception ex) {
            switch (Objects.requireNonNull(ex.getMessage())) {

                case "ENDCONNEXION" -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("ERROR PREVIOUS BUTTON", ("Erreur transmission des données : "
                                + ex.getMessage()), this));

                case "NO_ARTICLE_FOUND" -> // On désactive le bouton car plus d'article suivant
                        this.nextButton.setEnabled(false);

                case "PARAMS_FORMAT_ERROR" -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("ERROR FORMAT PREVIOUS BUTTON", ("MAUVAIS FORMAT : "
                                + ex.getMessage()), this));

                default -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("UNKNOW ERROR PREVIOUS BUTTON", ("ERREUR INCONNUE : "
                                + ex.getMessage()), this));
            }

        }

    }

    // Met à jour les détails du produit affiché en fonction de l'élément sélectionné
    @SuppressLint("SetTextI18n")
    private void refreshArticle(int index) {

        Articles art = null;

        try {

            art = client.sendConsult(index);

        } catch (Exception ex) {
            this.runOnUiThread(() ->
                    configActivity.afficherPopupErreur("CONSULT ERROR REFRESH ARTICLE", ("ERREUR : "
                            + ex.getMessage()), this));
            return;
        }

        try {

            this.articleListView.setText("");
            this.priceListView.setText("");
            this.stockListView.setText("");

            this.imageFruitView.setImageDrawable(Drawable.createFromPath("./images/" + art.getImage()));

        } catch (Exception ex) {

            this.runOnUiThread(() ->
                    configActivity.afficherPopupErreur("LOAD FILE", "CANNOT LOAD FILE : "
                            + ex.getMessage(), this));
            return;
        }

        this.articleListView.setText(art.getIntitule());
        this.priceListView.setText("" + art.getPrix());
        this.stockListView.setText("" + art.getStock());

    }

    // Mettre à jour la vue du caddie
    private void setAdapter() {

        caddieListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    // Met à jour le panier avec la liste des articles
    @SuppressLint("SetTextI18n")
    private void refreshCaddie() {

        caddie.clear();

        ArrayAdapter<Vector<String>> tmpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        tmpAdapter.clear();

        try{

            caddie = client.sendCaddie();

        }catch(Exception ex){
            switch (Objects.requireNonNull(ex.getMessage())) {
                case "ENDCONNEXION" -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("ERROR CONNECTION REFRESH CADDIE", "ERREUR TRANSMISSION DES DONNEES : "
                                + ex.getMessage(), this));

                case "PARAMS_FORMAT_ERROR" -> this.runOnUiThread(() ->

                        configActivity.afficherPopupErreur("ERROR FORMAT REFRESH CADDIE", "MAUVAIS FORMAT : "
                                + ex.getMessage(), this));

                default -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("UNKNOW ERROR REFRESH CADDIE", "ERREUR INCONNUE : "
                                + ex.getMessage(), this));
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

            tmpAdapter.add(row);

        }

        this.caddieListView.setAdapter(tmpAdapter);

        tmpAdapter.notifyDataSetChanged();

        this.totalText.setText("" + totalPrice);

    }

    // Ajoute le produit choisit à la liste des produits
    private void addToCaddie () {

        Achats achats = null;

        int quantitee = Integer.parseInt(this.quantityEditText.getText().toString());

        try{

            achats = client.sendAchat(this.currentArticle, quantitee);

        } catch(Exception ex){
            switch (Objects.requireNonNull(ex.getMessage())) {
                case "ENDCONNEXION" -> {
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("CONNECTION ERROR SENDACHAT BUY", "ERREUR : "
                                    + ex.getMessage(), this));
                    return;
                }

                case "PARAMS_FORMAT_ERROR" -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("FORMAT ERROR SENDACHAT BUY", "ERREUR : "
                                + ex.getMessage(), this));

                case "NO_MORE_STOCK" -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("STOCK ERROR SENDACHAT BUY", "NO MORE STOCK OF THIS ARTICLE ", this));

                default -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("UNKNOW ERROR SENDACHAT BUY", "ERREUR INCONNUE : "
                                + ex.getMessage(), this));
            }
        }

        try{

            refreshArticle(currentArticle);

            adapter.add(achats);

            setAdapter();

        } catch (Exception ex) {
            switch (Objects.requireNonNull(ex.getMessage())) {
                case "ENDCONNEXION":
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("CONNECTION ERROR BUY", "ERREUR : "
                                    + ex.getMessage(), this));
                    return;

                case "PARAMS_FORMAT_ERROR":
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("FORMAT ERROR BUY", "ERREUR : "
                                    + ex.getMessage(), this));
                    return;

                case "NO_ARTICLE_FOUND":
                default:
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("UNKNOW ERROR BUY", "ERREUR INCONNUE : "
                                    + ex.getMessage(), this));
                    return;
            }
        }

        refreshCaddie();

    }

    // Supprime l'article sélectionné
    private void deleteArticle() {

        try{

            int index = this.caddieListView.getSelectedItemPosition();
            int idArticle = caddie.get(index).getIdArticle();
            client.sendCancel(idArticle);

        } catch (Exception ex) {
            switch(Objects.requireNonNull(ex.getMessage())) {
                case "ENDCONNEXION" -> {
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("CONNECTION ERROR DELETE", "ERREUR : "
                                    + ex.getMessage(), this));
                    return;
                }

                case "PARAMS_FORMAT_ERROR" -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("FORMAT ERROR DELETE", "ERREUR FORMAT : "
                                + ex.getMessage(), this));

                default -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("UNKNOW ERROR DELETE", "ERREUR INCONNUE : "
                                + ex.getMessage(), this));
            }

        }

        try {

            adapter.remove((Achats) this.caddieListView.getSelectedItem());

            setAdapter();

            refreshCaddie();

            refreshArticle(currentArticle);

        } catch (Exception ex) {
            this.runOnUiThread(() ->
                    configActivity.afficherPopupErreur("ERROR DELETE", "ERREUR : "
                            + ex.getMessage(), this));
        }

    }

    // Supprime tous les articles du panier
    private void deleteCaddie() {
        try {

            client.sendCancelAll();

        } catch (Exception ex) {
            switch(Objects.requireNonNull(ex.getMessage())) {
                case "ENDCONNEXION" -> {
                    this.runOnUiThread(() ->
                            configActivity.afficherPopupErreur("CONNECTION ERROR DELETEALL CADDIE", "ERREUR : "
                                    + ex.getMessage(), this));

                    return;
                }

                case "PARAMS_FORMAT_ERROR" -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("FORMAT ERROR DELETEALL CADDIE", "ERREUR : "
                                + ex.getMessage(), this));

                default -> this.runOnUiThread(() ->
                        configActivity.afficherPopupErreur("UNKNOW ERROR DELETEALL CADDIE", "ERREUR INCONNUE : "
                                + ex.getMessage(), this));
            }
        }

        try {

            adapter.clear();

            setAdapter();

            refreshCaddie();

            refreshArticle(currentArticle);

        } catch (Exception ex) {
            this.runOnUiThread(() ->
                    configActivity.afficherPopupErreur("ERROR DELETEALL CADDIE", "ERREUR : "
                            + ex.getMessage(), this));
        }

    }

    // On confirme le panier et on crée la facture
    private void sendConfirm() {

        try {

            client.sendConfirmer(loginId);

            adapter.clear();

            setAdapter();

        } catch(Exception ex){
            switch(Objects.requireNonNull(ex.getMessage())) {
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

        refreshCaddie();

    }

    //endregion

}