/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.java_c.client.Utils.MarketModel;

/**
 *
 * @author Arkios
 */
public class Achats {
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private int idArticle;    
    private int quantitee;
    private float prix;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public Achats(int idArticle, int quantitee, float prix) {
        this.idArticle = idArticle;
        this.quantitee = quantitee;
        this.prix = prix;
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
}
