/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.model;

/**
 *
 * @author Arkios
 */
public class Sale {    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private String intitule;
    private int quantiteVendue;
    private float prixUnite;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public Sale() {
        intitule = "";
        quantiteVendue = -1;
        prixUnite = Float.NaN;
    }

    public Sale(String intitule, int quantiteVendue, float prixUnite) {
        this.intitule = intitule;
        this.quantiteVendue = quantiteVendue;
        this.prixUnite = prixUnite;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
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
     * Get the value of prixUnite
     *
     * @return the value of prixUnite
     */
    public float getPrixUnite() {
        return prixUnite;
    }

    /**
     * Set the value of prixUnite
     *
     * @param prixUnite new value of prixUnite
     */
    public void setPrixUnite(float prixUnite) {
        this.prixUnite = prixUnite;
    }

    
    /**
     * Get the value of quantiteVendue
     *
     * @return the value of quantiteVendue
     */
    public int getQuantiteVendue() {
        return quantiteVendue;
    }

    /**
     * Set the value of quantiteVendue
     *
     * @param quantiteVendue new value of quantiteVendue
     */
    public void setQuantiteVendue(int quantiteVendue) {
        this.quantiteVendue = quantiteVendue;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    // </editor-fold>
}
