/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.payement_protocol.model;
import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 *
 * @author Arkios
 */
public class Facture implements Serializable 
{
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private int Id;
    private String idClient;
    private java.sql.Timestamp date;
    private float prix;
    private boolean payed;
    private ArrayList<Sale> sales;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     *
     */
    public Facture() {
        Id = -1;
        idClient = null;
        date = null;
        prix = Float.NaN;
        payed = false;
        sales = new ArrayList<>();
    }
    
    /**
     *
     * @param Id
     * @param idClient
     * @param date
     * @param prix
     * @param payed
     */
    public Facture(int Id, String idClient, Timestamp date, float prix, boolean payed) {
        this.Id = Id;
        this.idClient = idClient;
        this.date = date;
        this.prix = prix;
        this.payed = payed;
        sales = new ArrayList<>();
    }
    
    /**
     *
     * @param Id
     * @param idClient
     * @param date
     * @param prix
     * @param payed
     * @param sales
     */
    public Facture(int Id, String idClient, Timestamp date, float prix, boolean payed, ArrayList<Sale> sales) {
        this.Id = Id;
        this.idClient = idClient;
        this.date = date;
        this.prix = prix;
        this.payed = payed;
        this.sales = sales;
    }
    
    /**
     * 
     * @param result
     * @throws SQLException
     */
    public Facture(ResultSet result) throws Exception {
        try{
            this.Id = result.getInt("id");
            this.idClient = result.getString("idClient");
            this.date = result.getTimestamp("date");
            this.prix = result.getFloat("montant");
            this.payed = result.getBoolean("payé");
        }
        catch(SQLException ex)
        {
            throw new Exception("ROW_ERROR", ex);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public ArrayList<Sale> getSales() {
        return sales;
    }

    public void setSales(ArrayList<Sale> sales) {
        this.sales = sales;
    }
    
    /**
     * Get the value of payed
     *
     * @return the value of payed
     */
    public boolean isPayed() {
        return payed;
    }

    /**
     * Set the value of payed
     *
     * @param payed new value of payed
     */
    public void setPayed(boolean payed) {
        this.payed = payed;
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
     * Get the value of date
     *
     * @return the value of date
     */
    public Timestamp getDate() {
        return date;
    }

    /**
     * Set the value of date
     *
     * @param date new value of date
     */
    public void setDate(Timestamp date) {
        this.date = date;
    }

    
    
    
    /**
     * Get the value of idClient
     *
     * @return the value of idClient
     */
    public String getIdClient() {
        return idClient;
    }

    /**
     * Set the value of idClient
     *
     * @param idClient new value of idClient
     */
    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    
    
    /**
     * Get the value of Id
     *
     * @return the value of Id
     */
    public int getId() {
        return Id;
    }

    /**
     * Set the value of Id
     *
     * @param Id new value of Id
     */
    public void setId(int Id) {
        this.Id = Id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    // </editor-fold>
}
