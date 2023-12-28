package com.mymaraichermobile.model;

import androidx.annotation.NonNull;

public class Articles {

    // <editor-fold defaultstate="collapsed" desc="Properties">
    private int idArticle;
    private String intitule;
    private float prix;
    private int stock;
    private String image;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructeurs">

    public Articles(int idArticle, String intitule, float prix, int stock, String image)
    {
        this.idArticle = idArticle;
        this.intitule = intitule;
        this.prix = prix;
        this.stock = stock;
        this.image = image;
    }

    public Articles(String params) throws NumberFormatException
    {
        String[] paramTokens = params.split("" + "#");

        this.idArticle = Integer.parseInt(paramTokens[0]);
        this.intitule = paramTokens[1];
        this.stock = Integer.parseInt(paramTokens[2]);
        this.prix = Float.parseFloat(paramTokens[3]);
        this.image = paramTokens[4];
    }
    //</editor-fold>

    //<editor-fold desc="Setters/Getters">
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }


    // Récupère l'id de l'article
    public int getIdArticle() {
        return idArticle;
    }

    // Initialise l'id de l'article
    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }
    //</editor-fold>


    //<editor-fold desc="Methods">

    @NonNull
    @Override
    public String toString() {
        return idArticle + ", " + intitule + ", " + prix + ", " + stock + ", " + image;
    }

    //</editor-fold>

}
