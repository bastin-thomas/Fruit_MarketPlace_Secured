package be.hepl.payement_protocol.Playground;


import be.hepl.generic_server_tcp.Logger;
import be.hepl.payement_protocol.Utils.DBPayement;
import be.hepl.payement_protocol.model.Facture;
import be.hepl.payement_protocol.model.Sale;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Arkios
 */
public class TestingPlayground {
    
    public static void main(String[] args) {
        Logger logg = new Logger() {
                @Override
                public void Trace(String message) {
                    System.out.println(message);
                }
        };
        
        try {
            DBPayement db = new DBPayement("jdbc:mariadb://192.168.1.19:3306/PourStudent?user=Student&password=PassStudent1_", logg);
            
            
            db.Login("Thomas", "123");
            ArrayList<Facture> bills = db.GetFactures("Thomas");
            
            ArrayList<String> clients = db.GetClientList();
            System.out.println("CLIENT:");
            for(String client : clients)
            {
                System.out.println(client);
            }
            System.out.println();
            System.out.println();
            
            System.out.println("FACTURE CLIENT");
            for(Facture bill : bills)
            {
                System.out.println(bill);
            }
            System.out.println();
            System.out.println();
            
            ArrayList<Sale> sales = db.GetSales(bills.get(0).getId());
            bills.get(0).setSales(sales);
            
            System.out.println("FACTURE[0] Ventes:");
            for(Sale sale : sales)
            {
                System.out.println(sale);
            }
            System.out.println();
            System.out.println();
            
            db.PayFacture(bills.get(0).getId());
            
            System.out.println("FACTURE[0] PAYEE");
            System.out.println();
            System.out.println();
            
        } catch (Exception ex) {
            logg.Trace(ex.getMessage());
        }
    } 
}
