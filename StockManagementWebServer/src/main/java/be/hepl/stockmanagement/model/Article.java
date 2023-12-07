/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.stockmanagement.model;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Arkios
 */
public class Article implements Serializable 
{
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private int Id;
    private String intitule;
    private float prix;
    private int stock;
    private String image;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     *
     */
    public Article() {
        Id = -1;
        intitule = null;
        stock = -1;
        prix = Float.NaN;
        image = null;
    }

    public Article(int Id, String intitule, float prix, int stock, String image) {
        this.Id = Id;
        this.intitule = intitule;
        this.prix = prix;
        this.stock = stock;
        this.image = image;
    }

    
    
    
    /**
     * 
     * @param result
     * @throws SQLException
     */
    public Article(ResultSet result) throws Exception {
        try{
            //id, intitule, prix, stock, image
            this.Id = result.getInt("id");
            this.intitule = result.getString("intitule");
            this.prix = result.getFloat("prix");
            this.stock = result.getInt("stock");
            this.image = result.getString("image");
        }
        catch(SQLException ex)
        {
            throw new Exception("ROW_ERROR", ex);
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public int getId() {    
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {    
        this.image = image;
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    @Override
    public String toString() {
        return "Article{" + "Id=" + Id + ", intitule=" + intitule + ", prix=" + prix + ", stock=" + stock + ", image=" + image + '}';
    }
    // </editor-fold>
}
