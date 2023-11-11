/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.generic_server_tcp.PooledServer;

import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Arkios
 */
public class WaitingList {
    
    // <editor-fold defaultstate="collapsed" desc="Properties">
    private final ArrayList<Socket> waitingList;
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public WaitingList()
    {
        waitingList = new ArrayList<>();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Methods">
    public synchronized void addConnexion(Socket socket)
    {
        waitingList.addLast(socket);
        notify();
    }
    
    public synchronized Socket getConnexion() throws InterruptedException
    {
        while(waitingList.isEmpty())
        {
            wait();
        } 
        
        return waitingList.removeFirst();
    }
    // </editor-fold>
}
