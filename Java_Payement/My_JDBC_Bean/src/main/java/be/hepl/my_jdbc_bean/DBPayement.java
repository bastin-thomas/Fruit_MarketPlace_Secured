/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.my_jdbc_bean;

import be.hepl.payement_protocol.model.Facture;
import be.hepl.payement_protocol.model.Sale;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Arkios
 */
public class DBPayement extends JDBC_Bean {
    // <editor-fold defaultstate="collapsed" desc="Properties">
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public DBPayement() throws ClassNotFoundException, SQLException {
        String urlconnexion = "jdbc:mariadb://localhost:3306/PourStudent?user=Student&password=PassStudent1_";
        //Connect to the DB using a mysql string
        this.setDb(DriverManager.getConnection(urlconnexion));
    }
    
    
    /**
     *
     * @param urlconnexion
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public DBPayement(String urlconnexion) throws ClassNotFoundException, SQLException {
        //Connect to the DB using a mysql string
        this.setDb(DriverManager.getConnection(urlconnexion));
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     * « Login » Login, password       Oui ou non              Vérification du login et du mot
     *           (d’un employé)                                passe dans la table des employés
     * @param login
     * @param password
     * @return 
     * @throws java.lang.Exception 
     */
    public boolean Login(String login, String password) throws Exception{
        ResultSet result;
        
        try {
            result = select("password", "employees", "login="+login);
        } catch (SQLException ex) {
            throw new Exception("SQL_ERROR", ex);
        }
        
        //Check if there is a first row:
        if(!result.next())
        {
            throw new Exception("NO_LOGIN");
        }
        
        String DB_Password =result.getString("password");
        if(!DB_Password.equals(password))
        {
            throw new Exception("BAD_LOGIN");
        }
        
        return true;
    }
    
    
    /**
     * « Get Sales » idFacture       idFacture, date, montant, payé, Liste<article>         Permettrait de récupérer l’ensemble des articles 
     *                                                                                      concernant une facture dont on fournirait l’id au serveur.
     * @param idFacture
     * @return
     * @throws Exception
     */
    public ArrayList<Sale> GetSales(int idFacture) throws Exception{
        ResultSet result;
        ArrayList<Sale> sales = new ArrayList<>();
        
        try {
            result = select("articles.intitule AS intitule, ventes.quantité AS quantite, articles.prix AS prixUnite", 
                    "ventes INNER JOIN articles ON(articles.id = ventes.idArticle) ", 
                        "ventes.idFacture"+idFacture);
        } catch (SQLException ex) {
            throw new Exception("SQL_ERROR", ex);
        }
        
        while(result.next())
        {
            sales.add(new Sale(result));
        }
                
        return sales;
    }
    
    
    /**
     * « Get Factures » idClient       Liste des factures (idFacture, date, montant, payé)     On récupère simplement les
     *                (fournie par le client sur place)                                        factures du client dans la table
     *                                                                                         factures (sans le contenu détaillé
     *                                                                                         de la commande donc)
     *
     * @param idClient
     * @return
     * @throws Exception
     */
    public ArrayList<Facture> GetFactures(int idClient) throws Exception{
        ArrayList<Facture> bills = new ArrayList<>();
        ResultSet result;
        
        try {
            result = select("id, idClient, date, montant, payé", 
                              "factures INNER JOIN accounts ON(factures.idClient = accounts.login) ", 
                                  "accounts.login='"+idClient+"'");
        } catch (SQLException ex) {
            throw new Exception("SQL_ERROR", ex);
        }
        
        
        while(result.next())
        {
            bills.add(new Facture(result));
        }
        
        return bills;
    }
    

    /**
     * « Pay Facture » idFacture, nom, num carte VISA          Oui ou non                   Le serveur se contente de vérifier
                                                               (carte VISA invalide)        la validité du numéro de carte →
                                                                                            si ok, on considère que le
                                                                                            paiement est réalisé
     * @param idFacture
     * @return
     * @throws Exception
     */
    public boolean PayFacture(int idFacture) throws Exception{
        try {
            update("factures","payé=1","id=" + idFacture);
        } catch (SQLException ex) {
            System.out.println("SQL_ERROR[" + ex.getErrorCode() + "]: " + ex.getMessage());
            return false;
        }
        
        return true;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    // </editor-fold>
}
