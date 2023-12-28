package com.mymaraichermobile.model;

import androidx.annotation.NonNull;

public class CaddieRows {
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private int idArticle;
    private String intitule;
    private int quantitee;
    private float prix;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public CaddieRows(int idArticle, String intitule, int quantitee, float prix) {
        this.idArticle = idArticle;
        this.intitule = intitule;
        this.quantitee = quantitee;
        this.prix = prix;
    }

    public CaddieRows(String params) {
        String[] paramTokens = params.split("" + "#");

        this.idArticle = Integer.parseInt(paramTokens[0]);
        this.intitule = paramTokens[1];
        this.quantitee = Integer.parseInt(paramTokens[2]);
        this.prix = Float.parseFloat(paramTokens[3]);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * Get the value of prix
     *
     * @return the value of prix
     */
    public float getPrix() {
        return prix;
    }

    /**
     * Set the value of prix
     *
     * @param prix new value of prix
     */
    public void setPrix(float prix) {
        this.prix = prix;
    }


    /**
     * Get the value of quantitee
     *
     * @return the value of quantitee
     */
    public int getQuantitee() {
        return quantitee;
    }

    /**
     * Set the value of quantitee
     *
     * @param quantitee new value of quantitee
     */
    public void setQuantitee(int quantitee) {
        this.quantitee = quantitee;
    }


    /**
     * Get the value of intitule
     *
     * @return the value of intitule
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Set the value of intitule
     *
     * @param intitule new value of intitule
     */
    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }


    /**
     * Get the value of idArticle
     *
     * @return the value of idArticle
     */
    public int getIdArticle() {
        return idArticle;
    }

    /**
     * Set the value of idArticle
     *
     * @param idArticle new value of idArticle
     */
    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    @NonNull
    @Override
    public String toString() {
        return "CaddieRows{" + "idArticle=" + idArticle + ", intitule=" + intitule + ", quantitee=" + quantitee + ", prix=" + prix + '}';
    }
    // </editor-fold>

}
