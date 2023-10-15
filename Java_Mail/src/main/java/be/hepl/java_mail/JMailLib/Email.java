/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.java_mail.JMailLib;

import java.util.ArrayList;
import javax.mail.Message;

/**
 *
 * @author Arkios
 */
public class Email {
    static String charset = "iso-8859-1";
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private String _id;
    private String _from;
    private String _to;
    private String _CC;
    private String _subject;
    private String _message;
    private ArrayList _mimeParts;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructors">
    /**
     * default constructor
     */
    public Email() {
    }
    
    public Email(Message m) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // </editor-fold>

    
    
    // <editor-fold defaultstate="collapsed" desc="Getters/Setters">
    /**
     * Get the value of _mimeParts
     *
     * @return the value of _mimeParts
     */
    public ArrayList getMimeParts() {
        return _mimeParts;
    }

    /**
     * Set the value of _mimeParts
     *
     * @param _mimeParts new value of _mimeParts
     */
    public void setMimeParts(ArrayList _mimeParts) {
        this._mimeParts = _mimeParts;
    }


    /**
     * Get the value of _CC
     *
     * @return the value of _CC
     */
    public String getCC() {
        return _CC;
    }

    /**
     * Set the value of _CC
     *
     * @param _CC new value of _CC
     */
    public void setCC(String _CC) {
        this._CC = _CC;
    }

    /**
     * Get the value of _to
     *
     * @return the value of _to
     */
    public String getTo() {
        return _to;
    }

    /**
     * Set the value of _to
     *
     * @param _to new value of _to
     */
    public void setTo(String _to) {
        this._to = _to;
    }

    /**
     * Get the value of _subject
     *
     * @return the value of _subject
     */
    public String getSubject() {
        return _subject;
    }

    /**
     * Set the value of _subject
     *
     * @param _subject new value of _subject
     */
    public void setSubject(String _subject) {
        this._subject = _subject;
    }

    /**
     * Get the value of _from
     *
     * @return the value of _from
     */
    public String getFrom() {
        return _from;
    }

    /**
     * Set the value of _from
     *
     * @param _from new value of _from
     */
    public void setFrom(String _from) {
        this._from = _from;
    }


    /**
     * Get the value of _id
     *
     * @return the value of _id
     */
    public String getId() {
        return _id;
    }

    /**
     * Set the value of _id
     *
     * @param _id new value of _id
     */
    public void setId(String _id) {
        this._id = _id;
    }
    
    /**
     * Get the value of _message
     *
     * @return the value of _message
     */
    public String getMessage() {
        return _message;
    }

    /**
     * Set the value of _message
     *
     * @param _message new value of _message
     */
    public void setMessage(String _message) {
        this._message = _message;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    @Override
    public String toString() {
        return super.toString(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    // </editor-fold>    
}
