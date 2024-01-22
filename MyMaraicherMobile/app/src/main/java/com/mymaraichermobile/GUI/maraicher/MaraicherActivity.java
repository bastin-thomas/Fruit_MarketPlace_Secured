package com.mymaraichermobile.GUI.maraicher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mymaraichermobile.GUI.PopupMessage;
import com.mymaraichermobile.R;
import com.mymaraichermobile.model.Achats;
import com.mymaraichermobile.model.Articles;
import com.mymaraichermobile.model.CaddieRows;
import com.mymaraichermobile.model.ProtocoleMarket.ProtocoleClient;
import com.mymaraichermobile.model.SocketHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MaraicherActivity extends AppCompatActivity {

    PopupMessage popupMessage = new PopupMessage();

    //region Private variables
    private MaraicherActivity currentActivity;
    // Infos connexions
    private ProtocoleClient client;
    private String loginId;

    // Variables pour stocker les IDs
    private ImageView imageFruitView;
    private Button previousButton;
    private Button nextButton;
    private Button deleteButton;
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
    private Context context;
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
        context = this.getApplicationContext();
        currentActivity = this;
        // variables IDS
        imageFruitView = findViewById(R.id.imageView);
        quantityEditText = findViewById(R.id.quantityEditText);
        articleListView = findViewById(R.id.articleListView);
        priceListView = findViewById(R.id.priceListView);
        stockListView = findViewById(R.id.stockListView);

        // variables Buttons
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        Button addCaddieButton = findViewById(R.id.addCaddieButton);
        deleteButton = findViewById(R.id.deleteButton);
        Button deleteAllButton = findViewById(R.id.deleteAllButton);
        Button confirmButton = findViewById(R.id.confirmButton);

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

    //region Events

    /*
     *  Application
     *  S'exécute avant que l'application s'arrête
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

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
    }

    // Caddie
    private void showPreviousElement() {
        if (this.currentArticle <= 1) {
            this.previousButton.setEnabled(false);
        }

        if (!this.nextButton.isEnabled()) {
            this.nextButton.setEnabled(true);
        }

        refreshArticle(currentArticle - 1); // Met à jour les détails du produit affiché précédent
        this.currentArticle--;
    }

    private void showNextElement() {
        if (this.currentArticle >= 20) {
            this.nextButton.setEnabled(false);
        }

        if (!this.previousButton.isEnabled()) {
            this.previousButton.setEnabled(true);
        }

        refreshArticle(this.currentArticle + 1); // Met à jour les détails du produit affiché précédent
        this.currentArticle++;
    }

    // Met à jour les détails du produit affiché en fonction de l'élément sélectionné
    @SuppressLint({"SetTextI18n", "StaticFieldLeak"})
    private void refreshArticle(int index) {

        try {
            new AsyncTask<Bundle, Void, HashMap<String, Object>>() {
                @Override
                protected HashMap<String, Object> doInBackground(Bundle... bundles) {
                    HashMap<String, Object> props = new HashMap<>();
                    Articles art;

                    try {
                        art = client.sendConsult(index);
                        props.put("articles", art);
                        props.put(getString(R.string.isfailed), false);
                    } catch (Exception ex) {
                        props.put(getString(R.string.exception), ex);
                        props.put(getString(R.string.isfailed), true);
                    }

                    return props;
                }

                @Override
                protected void onPostExecute(HashMap<String, Object> props) {
                    super.onPostExecute(props);
                    if (!((Boolean) props.get(getString(R.string.isfailed)))) {
                        Articles art = (Articles) props.get("articles");

                        try {
                            articleListView.setText("");
                            priceListView.setText("");
                            stockListView.setText("");


                            String nomImage = art.getImage().toLowerCase();
                            nomImage = nomImage.replace(".jpg", "");

                            int resID = getResources().getIdentifier(nomImage, "drawable", getPackageName());
                            Picasso.get().load(resID).into(imageFruitView);
                        } catch (Exception ex) {
                            currentActivity.runOnUiThread(() -> {
                                popupMessage.afficherPopupErreur(getString(R.string.refresh_article), getString(R.string.error) + ex.getMessage(), currentActivity);
                            });
                        }

                        try {
                            @SuppressLint("DefaultLocale") String prixArtTmp = String.format("%.2f", art.getPrix());
                            articleListView.setText(art.getIntitule());
                            priceListView.setText(prixArtTmp);
                            stockListView.setText("" + art.getStock());
                        } catch (Exception ex) {
                            currentActivity.runOnUiThread(() -> {
                                popupMessage.afficherPopupErreur(getString(R.string.refresh_article), "Update View : " + ex.getMessage(), currentActivity);
                            });
                        }
                    } else {

                        Exception ex = (Exception) props.get(getString(R.string.exception));
                        switch (Objects.requireNonNull(ex.getMessage())) {
                            case "ENDCONNEXION" -> {
                                currentActivity.runOnUiThread(() ->
                                        popupMessage.afficherPopupErreur(getString(R.string.error_previous_button),
                                                (getString(R.string.error) + ex.getMessage()), currentActivity));
                            }

                            case "NO_ARTICLE_FOUND" -> {
                            } // On désactive le bouton car plus d'article précédent

                            case "PARAMS_FORMAT_ERROR" -> {
                                currentActivity.runOnUiThread(() ->
                                        popupMessage.afficherPopupErreur(getString(R.string.error_previous_button),
                                                (getString(R.string.error_format) + ex.getMessage()), currentActivity));
                            }

                            default -> {
                                currentActivity.runOnUiThread(() ->
                                        popupMessage.afficherPopupErreur(getString(R.string.error_previous_button),
                                                (getString(R.string.unknown_error)
                                                + ex.getMessage()), currentActivity));
                            }
                        }
                    }
                }
            }.execute().get();
        } catch (Exception e) {
            Log.d("ASYNCTACK", Objects.requireNonNull(e.getMessage()));
        }
    }

    // Met à jour le panier avec la liste des articles
    @SuppressLint({"SetTextI18n", "StaticFieldLeak"})
    private void refreshCaddie() {
        new AsyncTask<Bundle, Void, HashMap<String, Object>>() {
            @Override
            protected HashMap<String, Object> doInBackground(Bundle... bundles) {
                HashMap<String, Object> props = new HashMap<String, Object>();
                ArrayList<CaddieRows> newCaddie;

                try {
                    newCaddie = client.sendCaddie();
                    props.put(getString(R.string.isfailed), false);
                    props.put("caddie", newCaddie);
                } catch (Exception ex) {
                    props.put(getString(R.string.exception), ex);
                    props.put(getString(R.string.isfailed), true);
                }
                return props;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> props) {
                super.onPostExecute(props);
                if (!((Boolean) props.get(getString(R.string.isfailed)))) {
                    ArrayList<CaddieRows> list = (ArrayList<CaddieRows>) props.get("caddie");
                    ArrayAdapter<String> tmpAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice);


                    caddie.clear();
                    caddie = list;
                    totalPrice = 0.0F;

                    // Récuperation des données de la bdd
                    for (CaddieRows tmp : list) {
                        float totalTmp = tmp.getPrix() * ((float) tmp.getQuantitee());
                        String totalArtTmp = String.format("%.2f", totalTmp);
                        String prixArt = String.format("%.2f", tmp.getPrix());

                        totalPrice += tmp.getPrix() * ((float) tmp.getQuantitee());

                        tmpAdapter.add(getString(R.string.product) + " " + tmp.getIntitule() + "   |  " + getString(R.string.quantity) + " "
                                + tmp.getQuantitee() + "   | " + getString(R.string.unitPriceText)
                                + " " + prixArt + "   | " + getString(R.string.total) + " " + totalArtTmp);
                    }

                    totalText.setText(String.format("%.2f", totalPrice));

                    caddieListView.setAdapter(tmpAdapter);
                    tmpAdapter.notifyDataSetChanged();
                } else {
                    Exception ex = (Exception) props.get(getString(R.string.exception));
                    switch (Objects.requireNonNull(ex.getMessage())) {
                        case "ENDCONNEXION" -> {
                            currentActivity.runOnUiThread(() -> {
                                popupMessage.afficherPopupErreur(getString(R.string.refresh_caddie),
                                        getString(R.string.error)
                                                + ex.getMessage(), currentActivity);
                            });
                        }

                        case "PARAMS_FORMAT_ERROR" -> {
                            currentActivity.runOnUiThread(() -> {
                                popupMessage.afficherPopupErreur(getString(R.string.refresh_caddie),
                                        getString(R.string.error_format)
                                                + ex.getMessage(), currentActivity);
                            });
                        }

                        default -> {
                            currentActivity.runOnUiThread(() -> {
                                popupMessage.afficherPopupErreur(getString(R.string.refresh_caddie),
                                        getString(R.string.unknown_error)
                                                + ex.getMessage(), currentActivity);
                            });
                        }
                    }
                }
            }
        }.execute();
    }

    // Ajoute le produit choisit à la liste des produits
    @SuppressLint("StaticFieldLeak")
    private void addToCaddie() {
        new AsyncTask<Bundle, Void, HashMap<String, Object>>() {
            @Override
            protected HashMap<String, Object> doInBackground(Bundle... bundles) {
                HashMap<String, Object> props = new HashMap<String, Object>() {
                };
                int quantitee = Integer.parseInt(quantityEditText.getText().toString());

                if (quantitee > 0) {
                    try {
                        client.sendAchat(currentArticle, quantitee);
                        props.put(getString(R.string.isfailed), false);
                    } catch (Exception ex) {
                        props.put(getString(R.string.exception), ex);
                        props.put(getString(R.string.isfailed), true);
                    }
                }
                return props;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> props) {
                super.onPostExecute(props);
                if (!((Boolean) props.get(getString(R.string.isfailed)))) {
                    refreshArticle(currentArticle);
                    refreshCaddie();
                } else {
                    Exception ex = (Exception) props.get(getString(R.string.exception));
                    switch (Objects.requireNonNull(ex.getMessage())) {
                        case "ENDCONNEXION" -> {
                            currentActivity.runOnUiThread(() ->
                                    popupMessage.afficherPopupErreur(getString(R.string.sendachat_buy),
                                            getString(R.string.error)
                                                    + ex.getMessage(), currentActivity));
                        }

                        case "PARAMS_FORMAT_ERROR" -> {
                            currentActivity.runOnUiThread(() ->
                                    popupMessage.afficherPopupErreur(getString(R.string.sendachat_buy),
                                            getString(R.string.error_format)
                                                    + ex.getMessage(), currentActivity));
                        }

                        case "NO_MORE_STOCK" -> {
                        }

                        default -> {
                            currentActivity.runOnUiThread(() ->
                                    popupMessage.afficherPopupErreur(getString(R.string.sendachat_buy),
                                            getString(R.string.unknown_error)
                                                    + ex.getMessage(), currentActivity));
                        }
                    }
                }
            }
        }.execute();
    }

    // Supprime l'article sélectionné
    @SuppressLint("StaticFieldLeak")
    private void deleteArticle() {
        new AsyncTask<Bundle, Void, HashMap<String, Object>>() {
            @Override
            protected HashMap<String, Object> doInBackground(Bundle... bundles) {
                HashMap<String, Object> props = new HashMap<String, Object>() {
                };

                try {
                    int index = caddieListView.getCheckedItemPosition();
                    int idArticle = caddie.get(index).getIdArticle();
                    client.sendCancel(idArticle);
                    props.put(getString(R.string.isfailed), false);

                } catch (Exception ex) {
                    props.put(getString(R.string.exception), ex);
                    props.put(getString(R.string.isfailed), true);
                }

                return props;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> props) {
                super.onPostExecute(props);
                if (!((Boolean) props.get(getString(R.string.isfailed)))) {
                    refreshArticle(currentArticle);
                    refreshCaddie();
                } else {
                    Exception ex = (Exception) props.get(getString(R.string.exception));
                    switch (Objects.requireNonNull(ex.getMessage())) {
                        case "ENDCONNEXION" -> {
                            currentActivity.runOnUiThread(() ->
                                    popupMessage.afficherPopupErreur(getString(R.string.delete_button),
                                            getString(R.string.error)
                                                    + ex.getMessage(), currentActivity));
                            props.put(getString(R.string.isfailed), true);
                        }

                        case "PARAMS_FORMAT_ERROR" -> {
                            currentActivity.runOnUiThread(() ->
                                    popupMessage.afficherPopupErreur(getString(R.string.delete_button),
                                            getString(R.string.error_format)
                                                    + ex.getMessage(), currentActivity));
                            props.put(getString(R.string.isfailed), true);
                        }

                        default -> {
                            currentActivity.runOnUiThread(() ->
                                    popupMessage.afficherPopupErreur(getString(R.string.delete_button),
                                            getString(R.string.unknown_error)
                                                    + ex.getMessage(), currentActivity));
                            props.put(getString(R.string.isfailed), true);
                        }
                    }
                }
            }
        }.execute();
    }

    // Supprime tous les articles du panier
    @SuppressLint("StaticFieldLeak")
    private void deleteCaddie() {
        if (!caddie.isEmpty()) {
            new AsyncTask<Bundle, Void, HashMap<String, Object>>() {
                @Override
                protected HashMap<String, Object> doInBackground(Bundle... bundles) {
                    HashMap<String, Object> props = new HashMap<String, Object>() {
                    };

                    try {
                        client.sendCancelAll();
                        props.put(getString(R.string.isfailed), false);

                    } catch (Exception ex) {
                        props.put(getString(R.string.exception), ex);
                        props.put(getString(R.string.isfailed), true);
                    }

                    return props;
                }

                @Override
                protected void onPostExecute(HashMap<String, Object> props) {
                    super.onPostExecute(props);
                    if (!((Boolean) props.get(getString(R.string.isfailed)))) {
                        refreshArticle(currentArticle);
                        refreshCaddie();
                    } else {
                        Exception ex = (Exception) props.get(getString(R.string.exception));
                        switch (Objects.requireNonNull(ex.getMessage())) {
                            case "ENDCONNEXION" -> {
                                currentActivity.runOnUiThread(() ->
                                        popupMessage.afficherPopupErreur(getString(R.string.deleteall_caddie_button),
                                                getString(R.string.error)
                                                        + ex.getMessage(), currentActivity));
                            }

                            case "PARAMS_FORMAT_ERROR" -> {
                                currentActivity.runOnUiThread(() ->
                                        popupMessage.afficherPopupErreur(getString(R.string.deleteall_caddie_button),
                                                getString(R.string.error_format)
                                                        + ex.getMessage(), currentActivity));
                            }

                            default -> {
                                currentActivity.runOnUiThread(() ->
                                        popupMessage.afficherPopupErreur(getString(R.string.deleteall_caddie_button),
                                                getString(R.string.unknown_error)
                                                        + ex.getMessage(), currentActivity));
                            }
                        }
                    }
                }
            }.execute();
        }
    }

    // On confirme le panier et on crée la facture
    @SuppressLint("StaticFieldLeak")
    private void sendConfirm() {
        new AsyncTask<Bundle, Void, HashMap<String, Object>>() {
            @Override
            protected HashMap<String, Object> doInBackground(Bundle... bundles) {
                HashMap<String, Object> props = new HashMap<String, Object>() {
                };

                try {
                    client.sendConfirmer(loginId);
                    props.put(getString(R.string.isfailed), false);
                } catch (Exception ex) {
                    props.put(getString(R.string.exception), ex);
                    props.put(getString(R.string.isfailed), true);
                }
                return props;
            }

            @Override
            protected void onPostExecute(HashMap<String, Object> props) {
                super.onPostExecute(props);

                if (!((Boolean) props.get(getString(R.string.isfailed)))) {
                    refreshArticle(currentArticle);
                    refreshCaddie();

                    currentActivity.popupMessage.afficherPopupErreur("SUCCESS CONFIRM",
                            getString(R.string.Confirm_Success), currentActivity);

                } else {
                    Exception ex = (Exception) props.get(getString(R.string.exception));
                    switch (Objects.requireNonNull(ex.getMessage())) {
                        case "ENDCONNEXION" -> {
                            currentActivity.popupMessage.afficherPopupErreur(getString(R.string.confirm),
                                    getString(R.string.error)
                                            + ex.getMessage(), currentActivity);
                        }

                        case "PARAMS_FORMAT_ERROR" -> {
                            currentActivity.popupMessage.afficherPopupErreur(getString(R.string.confirm),
                                    getString(R.string.error_format)
                                            + ex.getMessage(), currentActivity);
                        }

                        case "ERROR_BILL" -> {
                            currentActivity.popupMessage.afficherPopupErreur(getString(R.string.confirm),
                                    getString(R.string.error)
                                            + ex.getMessage(), currentActivity);
                        }

                        default -> {
                            currentActivity.popupMessage.afficherPopupErreur(getString(R.string.confirm),
                                    getString(R.string.unknown_error)
                                            + ex.getMessage(), currentActivity);
                        }
                    }
                }
            }
        }.execute();
    }
}
//endregion