package com.mymaraichermobile.GUI.maraicher;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
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

import com.mymaraichermobile.GUI.main.MainActivity;
import com.mymaraichermobile.GUI.message.PopupMessage;
import com.mymaraichermobile.GUI.settings.SettingsActivity;
import com.mymaraichermobile.R;
import com.mymaraichermobile.model.Achats;
import com.mymaraichermobile.model.Articles;
import com.mymaraichermobile.model.CaddieRows;
import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleThreaded.ProtocoleClientThreaded;
import com.mymaraichermobile.model.SocketHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class MaraicherActivity extends AppCompatActivity {

    //region Private variables

    PopupMessage popupMessage = new PopupMessage();

    // Infos connexions
    private ProtocoleClientThreaded client;
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
    ArrayAdapter<Achats> adapter;

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
            loginId = extras.getString("loginId_key");
            client = SocketHandler.getProtocol();
            Log.d("ETAT CLIENT", "Client : " + client);
            Log.d("ETAT LOGINID", "Client : " + loginId);
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
        deleteButton.setEnabled(false);

        // variables panier

        currentArticle = 1;
        totalPrice = (float) 0.0;
        caddie = new ArrayList<>();
        caddieListView = findViewById(R.id.caddieListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        caddieListView.setAdapter(adapter);
        caddieListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        totalText = findViewById(R.id.totalText);
        //endregion

        try {
            refreshArticle(currentArticle);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //region Button Listener

        this.caddieListView.setOnItemClickListener((parent, view, index, id) -> {

            if (this.caddieListView.isItemChecked(index)) {

                deleteButton.setEnabled(true);

            } else {

                // Aucun élément sélectionné
                deleteButton.setEnabled(false);

            }

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
                sendConfirm());

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

        } catch (Exception e) {

        }

        try {
            Log.d("TRACE LOGOUT", "client : " + client);

            client.sendLogout();

        } catch (Exception ignored) {

        }

        try {

            client.close();

            SocketHandler.setProtocol(null);

        } catch (Exception ignored) {

        }
    }

    // Paramètres
    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);

        intent.putExtra("class_name", MaraicherActivity.class.getName());
        startActivity(intent);

        finish();
    }

    // Pour revenir à la page de connexion
    public void disconnect(View view) {

        try {

            client.sendCancelAll();

        } catch (Exception ignored) {

        }

        try {
            Log.d("TRACE LOGOUT", "client : " + client);

            client.sendLogout();

        } catch (Exception ignored) {

        }

        try {

            client.close();

            SocketHandler.setProtocol(null);

        } catch (Exception ignored) {

        }

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        finish();
    }

    // Caddie
    private void showPreviousElement() {

        if(!this.nextButton.isEnabled()) {
            this.nextButton.setEnabled(true);
        }

        try{

            refreshArticle(this.currentArticle - 1); // Met à jour les détails du produit affiché précédent
            this.currentArticle--;


        } catch (Exception ex) {
            switch (Objects.requireNonNull(ex.getMessage())) {

                case "ENDCONNEXION" ->
                    this.runOnUiThread(() ->
                            popupMessage.afficherPopupErreur("ERROR PREVIOUS BUTTON", ("Erreur transmission des données : "
                                    + ex.getMessage()), this));

                case "NO_ARTICLE_FOUND" -> // On désactive le bouton car plus d'article précédent
                        this.previousButton.setEnabled(false);

                case "PARAMS_FORMAT_ERROR" ->
                        this.runOnUiThread(() ->
                                popupMessage.afficherPopupErreur("ERROR FORMAT PREVIOUS BUTTON", ("MAUVAIS FORMAT : "
                                    + ex.getMessage()), this));

                default ->
                        this.runOnUiThread(() ->
                                popupMessage.afficherPopupErreur("UNKNOW ERROR PREVIOUS BUTTON", ("ERREUR INCONNUE : "
                                        + ex.getMessage()), this));
            }

        }

    }

    private void showNextElement() {

        if (!this.previousButton.isEnabled()) {
            this.previousButton.setEnabled(true);
        }

        try {

            refreshArticle(this.currentArticle + 1); // Met à jour les détails du produit affiché précédent
            this.currentArticle++;


        } catch (Exception ex) {
            switch (Objects.requireNonNull(ex.getMessage())) {

                case "ENDCONNEXION" -> this.runOnUiThread(() ->
                        popupMessage.afficherPopupErreur("ERROR NEXT BUTTON", ("Erreur transmission des données : "
                                + ex.getMessage()), this));

                case "NO_ARTICLE_FOUND" -> // On désactive le bouton car plus d'article suivant
                        this.nextButton.setEnabled(false);

                case "PARAMS_FORMAT_ERROR" -> this.runOnUiThread(() ->
                        popupMessage.afficherPopupErreur("ERROR FORMAT NEXT BUTTON", ("MAUVAIS FORMAT : "
                                + ex.getMessage()), this));

                default -> this.runOnUiThread(() ->
                        popupMessage.afficherPopupErreur("UNKNOW ERROR NEXT BUTTON", ("ERREUR INCONNUE : "
                                + ex.getMessage()),  this.getBaseContext()));
            }

        }

    }

    // Met à jour les détails du produit affiché en fonction de l'élément sélectionné
    @SuppressLint("SetTextI18n")
    private void refreshArticle(int index) throws Exception {
        Articles art;

        art = client.sendConsult(index);


        try {

            this.articleListView.setText("");
            this.priceListView.setText("");
            this.stockListView.setText("");

            String nomImage = art.getImage().toLowerCase();
            nomImage = nomImage.replace(".jpg","");

            int resID = getResources().getIdentifier(nomImage, "drawable", getPackageName());
            Picasso.get().load(resID).into(imageFruitView);

        } catch (Exception ex) {

            this.runOnUiThread(() ->
                    popupMessage.afficherPopupErreur("LOAD FILE", "CANNOT LOAD FILE : "
                            + ex.getMessage(), this));
        }
        try {

            this.articleListView.setText(art.getIntitule());
            this.priceListView.setText("" + art.getPrix());
            this.stockListView.setText("" + art.getStock());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Mettre à jour la vue du caddie
    private void setAdapter() {

        this.caddieListView.setAdapter(this.adapter);

        this.adapter.notifyDataSetChanged();

    }

    // Met à jour le panier avec la liste des articles
    @SuppressLint("SetTextI18n")
    private void refreshCaddie() {

        this.caddie.clear();

        ArrayAdapter<String> tmpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice);

        try{
            this.caddie = client.sendCaddie();

        }catch(Exception ex){
            switch (Objects.requireNonNull(ex.getMessage())) {
                case "ENDCONNEXION" -> {
                    this.runOnUiThread(() ->
                            popupMessage.afficherPopupErreur("ERROR CONNECTION REFRESH CADDIE", "ERREUR TRANSMISSION DES DONNEES : "
                                    + ex.getMessage(), this));
                    return;
                }

                case "PARAMS_FORMAT_ERROR" -> {
                    this.runOnUiThread(() ->

                            popupMessage.afficherPopupErreur("ERROR FORMAT REFRESH CADDIE", "MAUVAIS FORMAT : "
                                    + ex.getMessage(), this));
                    return;
                }

                default -> {
                    this.runOnUiThread(() ->
                            popupMessage.afficherPopupErreur("UNKNOW ERROR REFRESH CADDIE", "ERREUR INCONNUE : "
                                    + ex.getMessage(), this));
                    return;
                }
            }
        }

        this.totalPrice = 0.0F;

        // Récuperation des données de la bdd
        for(CaddieRows tmp : this.caddie){
            this.totalPrice += tmp.getPrix() * ((float)tmp.getQuantitee());

            tmpAdapter.add(tmp.getIntitule() + "   |   " + tmp.getQuantitee() + "   |   " + tmp.getPrix());
        }

        this.caddieListView.setAdapter(tmpAdapter);
        tmpAdapter.notifyDataSetChanged();
        

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        this.totalText.setText("" + df.format(this.totalPrice));

    }

    // Ajoute le produit choisit à la liste des produits
    private void addToCaddie () {

        Achats achats;

        int quantitee = Integer.parseInt(this.quantityEditText.getText().toString());

        if(quantitee > 0) {
            try{

                achats = client.sendAchat(this.currentArticle, quantitee);

            } catch(Exception ex) {
                switch (Objects.requireNonNull(ex.getMessage())) {
                    case "ENDCONNEXION" -> {
                        this.runOnUiThread(() ->
                                popupMessage.afficherPopupErreur("CONNECTION ERROR SENDACHAT BUY", "ERREUR : "
                                        + ex.getMessage(), this));
                        return;
                    }

                    case "PARAMS_FORMAT_ERROR" -> {
                        this.runOnUiThread(() ->
                                popupMessage.afficherPopupErreur("FORMAT ERROR SENDACHAT BUY", "ERREUR : "
                                        + ex.getMessage(), this));
                        return;
                    }

                    case "NO_MORE_STOCK" -> {
                        this.runOnUiThread(() ->
                                popupMessage.afficherPopupErreur("STOCK ERROR SENDACHAT BUY", "NO MORE STOCK OF THIS ARTICLE ", this));
                        return;
                    }

                    default -> {
                        this.runOnUiThread(() ->
                                popupMessage.afficherPopupErreur("UNKNOW ERROR SENDACHAT BUY", "ERREUR INCONNUE : "
                                        + ex.getMessage(), this));
                        return;
                    }
                }
            }
        } else {
            this.runOnUiThread(() ->
                    popupMessage.afficherPopupErreur("ERROR QUANTITY", getString(R.string.ChoiceQuantity), this));
            return;
        }

        try{

            refreshArticle(this.currentArticle);

            this.adapter.add(achats);

            setAdapter();

        } catch (Exception ex) {
            switch (Objects.requireNonNull(ex.getMessage())) {
                case "ENDCONNEXION":
                    this.runOnUiThread(() ->
                            popupMessage.afficherPopupErreur("CONNECTION ERROR BUY", "ERREUR : "
                                    + ex.getMessage(), this));
                    return;

                case "PARAMS_FORMAT_ERROR":
                    this.runOnUiThread(() ->
                            popupMessage.afficherPopupErreur("FORMAT ERROR BUY", "ERREUR : "
                                    + ex.getMessage(), this));
                    return;

                case "NO_ARTICLE_FOUND":
                default:
                    this.runOnUiThread(() ->
                            popupMessage.afficherPopupErreur("UNKNOW ERROR BUY", "ERREUR INCONNUE : "
                                    + ex.getMessage(), this));
                    return;
            }
        }

        refreshCaddie();

    }

    // Supprime l'article sélectionné
    private void deleteArticle() {

        try{
            int index = this.caddieListView.getCheckedItemPosition();
            int idArticle = this.caddie.get(index).getIdArticle();
            client.sendCancel(idArticle);

        } catch (Exception ex) {
            switch(Objects.requireNonNull(ex.getMessage())) {
                case "ENDCONNEXION" -> {
                    this.runOnUiThread(() ->
                            popupMessage.afficherPopupErreur("CONNECTION ERROR DELETE", "ERREUR : "
                                    + ex.getMessage(), this));
                    return;
                }

                case "PARAMS_FORMAT_ERROR" -> this.runOnUiThread(() ->
                        popupMessage.afficherPopupErreur("FORMAT ERROR DELETE", "ERREUR FORMAT : "
                                + ex.getMessage(), this));

                default -> this.runOnUiThread(() ->
                        popupMessage.afficherPopupErreur("UNKNOW ERROR DELETE", "ERREUR INCONNUE : "
                                + ex.getMessage(), this));
            }

        }

        try {

            this.adapter.remove((Achats) this.caddieListView.getSelectedItem());

            setAdapter();

            refreshCaddie();

            refreshArticle(this.currentArticle);

        } catch (Exception ex) {
            this.runOnUiThread(() ->
                    popupMessage.afficherPopupErreur("ERROR DELETE", "ERREUR : "
                            + ex.getMessage(), this));
        }

    }

    // Supprime tous les articles du panier
    private void deleteCaddie() {
        if(!this.caddie.isEmpty()) {
            try {

                client.sendCancelAll();

            } catch (Exception ex) {
                switch (Objects.requireNonNull(ex.getMessage())) {
                    case "ENDCONNEXION" -> {
                        this.runOnUiThread(() ->
                                popupMessage.afficherPopupErreur("CONNECTION ERROR DELETEALL CADDIE", "ERREUR : "
                                        + ex.getMessage(), this));

                        return;
                    }

                    case "PARAMS_FORMAT_ERROR" -> this.runOnUiThread(() ->
                            popupMessage.afficherPopupErreur("FORMAT ERROR DELETEALL CADDIE", "ERREUR : "
                                    + ex.getMessage(), this));

                    default -> this.runOnUiThread(() ->
                            popupMessage.afficherPopupErreur("UNKNOW ERROR DELETEALL CADDIE", "ERREUR INCONNUE : "
                                    + ex.getMessage(), this));
                }
            }

            try {

                this.adapter.clear();

                setAdapter();

                refreshCaddie();

                refreshArticle(this.currentArticle);

            } catch (Exception ex) {
                this.runOnUiThread(() ->
                        popupMessage.afficherPopupErreur("ERROR DELETEALL CADDIE", "ERREUR : "
                                + ex.getMessage(), this));
            }
        } else {
            this.runOnUiThread(() ->
                    popupMessage.afficherPopupErreur("Erreur suppression",
                            getString(R.string.NOSTOCK), this));

        }
    }

    // On confirme le panier et on crée la facture
    private void sendConfirm() {

        try {

            client.sendConfirmer(this.loginId);

            this.adapter.clear();

            setAdapter();

        } catch(Exception ex){
            switch(Objects.requireNonNull(ex.getMessage())) {
                case "ENDCONNEXION" -> {
                    this.runOnUiThread(() ->
                            popupMessage.afficherPopupErreur("ERROR DATA CONFIRM", "ERREUR TRANSMISSION DONNEES : "
                                    + ex.getMessage(), this));
                    return;
                }

                case "PARAMS_FORMAT_ERROR" -> {
                    this.runOnUiThread(() -> popupMessage.afficherPopupErreur("ERROR FORMAT CONFIRM", "MAUVAIS FORMAT : "
                            + ex.getMessage(), this));
                    return;
                }

                case "ERROR_BILL" -> {
                    this.runOnUiThread(() ->
                            popupMessage.afficherPopupErreur("ERROR SERVEUR/BILL CONFIRM", "ERREUR CREATION FACTURE : "
                                    + ex.getMessage(), this));
                    return;
                }

                default -> {
                    this.runOnUiThread(() ->
                            popupMessage.afficherPopupErreur("DEFAULT ERROR CONFIRM", "ERREUR INCONNUE : "
                                    + ex.getMessage(), this));
                    return;
                }
            }
        }

        this.runOnUiThread(() ->
                popupMessage.afficherPopupErreur("SUCCESS CONFIRM", getString(R.string.Confirm_Success), this));

        refreshCaddie();

    }

    //endregion
}