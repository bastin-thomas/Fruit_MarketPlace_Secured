/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package be.hepl.payement_protocol.protocol;

/**
 *
 * @author Arkios
 */
public class Payement_Protocol {
    
    /*
    « Login » Login, password       Oui ou non              Vérification du login et du mot
              (d’un employé)                                passe dans la table des employés
    */
    
    /*
    « Get Facture » idFacture       idFacture, date, montant, payé, Liste<article>          Permettrait de récupérer l’ensemble des articles 
                                                                                            concernant une facture dont on fournirait l’id au serveur.
    */
    
    /*
    « Get Factures » idClient       Liste des factures (idFacture, date, montant, payé)     On récupère simplement les
                    (fournie par le client sur place)                                       factures du client dans la table
                                                                                            factures (sans le contenu détaillé
                                                                                            de la commande donc)
    */
    
    /*
    « Pay Facture » idFacture, nom, num carte VISA          Oui ou non                      Le serveur se contente de vérifier
                                                                   (carte VISA invalide)    la validité du numéro de carte →
                                                                                            si ok, on considère que le
                                                                                            paiement est réalisé
    */
    
    /*                                                                                 
    « Logout »
    */
    
    
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
