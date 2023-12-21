/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.stockmanagement.Utils;

import be.hepl.generic_server_tcp.Logger;
import be.hepl.my_jdbc_bean.JDBC_Bean;
import be.hepl.stockmanagement.model.Article;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Arkios
 */
public class DBStock extends JDBC_Bean {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final Logger logger;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     *
     * @param urlconnexion
     * @param logger
     */
    public DBStock(String urlconnexion, Logger logger) throws SQLException {
        super(urlconnexion);
        this.logger = logger;
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
            result = select("password", "employees", "login='"+login+"'");
        } catch (SQLException ex) {
            throw new Exception("SQL_ERROR", ex);
        }
        
        //Check if there is a first row:
        if(!result.next())
        {
            throw new Exception("NO_LOGIN");
        }
        
        String DB_Password = result.getString("password");
        if(!DB_Password.equals(password))
        {
            throw new Exception("BAD_LOGIN");
        }
        
        return true;
    }
    
    public void UpdateArticles(int idArticle, float prix, int stock) throws Exception{
        update("articles", "prix=" + prix + ", stock=" + stock, "id=" + idArticle);
    }
    
    public ArrayList<Article> getArticles() throws Exception{
        ArrayList<Article> articles = new ArrayList<>();
        ResultSet result;
        
        try {
            result = select("id, intitule, prix, stock, image",  "articles", "1=1");
            
        } catch (SQLException ex) {
            throw new Exception("SQL_ERROR", ex);
        }
        
        while(result.next())
        {
            articles.add(new Article(result));
        }
        
        return articles;
    }
    // </editor-fold>
}
