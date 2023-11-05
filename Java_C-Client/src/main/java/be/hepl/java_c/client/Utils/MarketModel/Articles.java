/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.java_c.client.Utils.MarketModel;

import be.hepl.java_c.client.Utils.Consts;

/**
 *
 * @author Arkios
 */
public class Articles {
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private int idArticle;
    private String intitule;
    private float prix;
    private int stock;
    private String image;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">

    /**
     * 
     * @param idArticle
     * @param intitule
     * @param prix
     * @param stock
     * @param image
     */
    public Articles(int idArticle, String intitule, float prix, int stock, String image)
    {
        this.idArticle = idArticle;
        this.intitule = intitule;
        this.prix = prix;
        this.stock = stock;
        this.image = image;
    }
    
    
    /**
     * 
     * @param params
     */
    public Articles(String params) throws NumberFormatException
    {
        String[] paramTokens = params.split("" + Consts.SplitParameters);

        this.idArticle = Integer.parseInt(paramTokens[0]);
        this.intitule = paramTokens[1];
        this.stock = Integer.parseInt(paramTokens[2]);
        this.prix = Float.parseFloat(paramTokens[3]);
        this.image = paramTokens[4];
        
        
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * Get the value of image
     *
     * @return the value of image
     */
    public String getImage() {
        return image;
    }

    /**
     * Set the value of image
     *
     * @param image new value of image
     */
    public void setImage(String image) {
        this.image = image;
    }


    /**
     * Get the value of stock
     *
     * @return the value of stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Set the value of stock
     *
     * @param stock new value of stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    
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
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    // </editor-fold>

    @Override
    public String toString() {
        return idArticle + ", " + intitule + ", " + prix + ", " + stock + ", " + image;
    }
}
