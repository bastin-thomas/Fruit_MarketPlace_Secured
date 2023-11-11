/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package be.hepl.my_jdbc_bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.TableModel;

/**
 *
 * @author Arkios
 */
public abstract class JDBC_Bean implements Serializable {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private Connection db;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public JDBC_Bean()
    {
        //Connect to the DB using a mysql string
        db = null;
    }
    
    public JDBC_Bean(String urlconnexion) throws ClassNotFoundException, SQLException 
    {
        //Connect to the DB using a mysql string
        db = DriverManager.getConnection(urlconnexion);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Getters and Setters">
    /**
     * Get the value of db
     *
     * @return the value of db
     */
    public Connection getDb() {
        return db;
    }

    /**
     * Set the value of db
     *
     * @param db new value of db
     */
    public void setDb(Connection db) {
        this.db = db;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">

    /**
     * select /SQL/
     * @param select
     * @param from
     * @param where
     * @return ResultSet
     * @throws SQLException
     */
    protected ResultSet select(String select, String from, String where) throws SQLException {
        
        Statement statement = db.createStatement();
        String sql = "SELECT " + select + " FROM " + from + " WHERE " + where;
        ResultSet rs = statement.executeQuery(sql);               
        return rs;
    }

    /**
     * insert /SQL/
     * @param table
     * @param columns
     * @param values
     * @throws SQLException
     */
    protected void insert(String table, String columns, String values) throws SQLException {
        Statement statement = db.createStatement();
        String sql = "INSERT INTO " + table + " ( " + columns + " ) VALUES( " + values + " )";
        statement.executeUpdate(sql);
    }
    
    /**
     * update /SQL/
     * @param Update
     * @param set
     * @param where
     * @throws SQLException
     */
    protected void update(String Update, String set, String where) throws SQLException {
        Statement statement = db.createStatement();
        String sql = "UPDATE " + Update + " SET " + set + " WHERE " + where;
        statement.executeUpdate(sql);
    }

    /**
     * delete /SQL/
     * @param DeleteFrom
     * @param where
     * @throws SQLException
     */
    protected void delete(String DeleteFrom, String where) throws SQLException {
        Statement statement = db.createStatement();
        String sql = "DELETE FROM " + DeleteFrom + " WHERE " + where;
        statement.executeUpdate(sql);
    }
    
    /**
     * Close the DB Connection
     * @throws SQLException
     */
    protected void Close() throws SQLException
    {
        db.close();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    // </editor-fold>
}
