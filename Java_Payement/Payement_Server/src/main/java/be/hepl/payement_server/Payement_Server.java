/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package be.hepl.payement_server;

import be.hepl.payement_server.Utils.ConfigFolderManager;

import be.hepl.generic_server_tcp.Logger;
import be.hepl.generic_server_tcp.PooledServer.ListenThreadPooled;
import be.hepl.generic_server_tcp.Protocol;

import be.hepl.payement_protocol.Utils.Consts;
import be.hepl.payement_protocol.Utils.DBPayement;
import be.hepl.payement_protocol.protocol.Payement;

import com.formdev.flatlaf.FlatLightLaf;
import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import javax.swing.SpinnerModel;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Arkios
 */
public class Payement_Server extends javax.swing.JFrame implements Logger {
    
    // <editor-fold defaultstate="collapsed" desc="My Properties">
    private boolean isLaunched;
    private ListenThreadPooled serverThread;
    private final Properties config;
    // </editor-fold>
        
    // <editor-fold defaultstate="collapsed" desc="Constructor">
    /**
     * Creates new form Payement_Server2
     */
    public Payement_Server() {
        initComponents();
        isLaunched = false;
        
        ((DefaultTableModel) logs_table.getModel()).setRowCount(0);
        
        //Get Config From File
        config = ConfigFolderManager.LoadProperties();
        
        //Set UI Port
        SpinnerModel model = this.Port_Spinner.getModel();
        model.setValue(Integer.valueOf(config.getProperty(Consts.ConfigPort)));
        
        //Set DB URL
        DBurl_TextField.setText(config.getProperty(Consts.ConfigDBString));
        
        SpinnerModel model1 = this.Pool_Spinner.getModel();
        model1.setValue(Integer.valueOf(config.getProperty(Consts.ConfigPoolSize)));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods">
    /**
     *
     * @param message
     */
    @Override
    public synchronized void Trace(String message) {
        DefaultTableModel modele = (DefaultTableModel) logs_table.getModel();
        Vector<String> ligne = new Vector<>();
        
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");       
        ligne.add(format.format(new Date()));
        ligne.add(Thread.currentThread().getName());
        ligne.add(message);
        modele.insertRow(modele.getRowCount(),ligne);

        //Put the scroll at bottom
        logs_table.scrollRectToVisible(
                logs_table.getCellRect(modele.getRowCount() - 1, 0, true)
        );
    }
    
    /**
     *
     */
    public synchronized void RemoveLogs()
    {
        DefaultTableModel modele = (DefaultTableModel) logs_table.getModel();
        modele.setRowCount(0);
    }
    
    /**
     *
     */
    public synchronized void FreezeUI()
    {
        this.Start_Stop_Button.setText("STOP");
        this.DBurl_TextField.setEnabled(false);
        this.Port_Spinner.setEnabled(false);
        this.AbortButton.setEnabled(true);
        this.Pool_Spinner.setEnabled(false);
        this.RemoveLogs();
    }
    
    /**
     *
     */
    public synchronized void UnFreezeUI()
    {
        this.Start_Stop_Button.setText("START");
        this.DBurl_TextField.setEnabled(true);
        this.Port_Spinner.setEnabled(true);
        this.AbortButton.setEnabled(false);
        this.Pool_Spinner.setEnabled(true);
        this.RemoveLogs();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        AbortButton = new javax.swing.JButton();
        Start_Stop_Button = new javax.swing.JButton();
        Logs = new javax.swing.JLabel();
        Logs1 = new javax.swing.JLabel();
        Port_Spinner = new javax.swing.JSpinner();
        Logs2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        logs_table = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        DBurl_TextField = new javax.swing.JTextField();
        Pool_Spinner = new javax.swing.JSpinner();
        Logs3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(808, 493));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel1.setText("Payement Server Manager");

        AbortButton.setBackground(new java.awt.Color(204, 0, 0));
        AbortButton.setForeground(new java.awt.Color(0, 0, 0));
        AbortButton.setText("ABORT");
        AbortButton.setEnabled(false);
        AbortButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AbortButtonActionPerformed(evt);
            }
        });

        Start_Stop_Button.setBackground(new java.awt.Color(51, 204, 0));
        Start_Stop_Button.setForeground(new java.awt.Color(0, 0, 0));
        Start_Stop_Button.setText("START");
        Start_Stop_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Start_Stop_ButtonActionPerformed(evt);
            }
        });

        Logs.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Logs.setText("Logs :");

        Logs1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Logs1.setText("Port :");

        Port_Spinner.setModel(new javax.swing.SpinnerNumberModel(50002, 50000, 50100, 1));

        Logs2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Logs2.setText("DB URL :");

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        logs_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Horodateur", "Thread", "Message"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        logs_table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        logs_table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        logs_table.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(logs_table);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(6, 16));
        jScrollPane1.setName(""); // NOI18N

        DBurl_TextField.setText("url db");
        DBurl_TextField.setMaximumSize(new java.awt.Dimension(64, 22));
        jScrollPane1.setViewportView(DBurl_TextField);

        Pool_Spinner.setModel(new javax.swing.SpinnerNumberModel(5, 1, 500, 1));

        Logs3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        Logs3.setText("Pool Size :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Logs1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Logs2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AbortButton, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Start_Stop_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Port_Spinner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Pool_Spinner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Logs3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Logs, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 608, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Logs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Start_Stop_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Logs1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Port_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Logs2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Logs3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Pool_Spinner, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addComponent(AbortButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Events">
    private void AbortButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AbortButtonActionPerformed
        serverThread.close();
        System.exit(127);
    }//GEN-LAST:event_AbortButtonActionPerformed
    
    private void Start_Stop_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Start_Stop_ButtonActionPerformed
        if(!isLaunched)
        {     
            //Start the Server      
            DBPayement db = null;
            try
            {
                db = new DBPayement(DBurl_TextField.getText(), this);
            }
            catch(Exception ex)
            {
                this.Trace("Impossible to connect to DB server: " + ex.getMessage());
                return;
            }
            
            Protocol protocol = new Payement(this, db);
            int port = (int) this.Port_Spinner.getModel().getValue();
            int poolSize = (int) this.Pool_Spinner.getModel().getValue();
            
            try {
                FreezeUI();
                serverThread = new ListenThreadPooled(port, protocol, this, poolSize);
                serverThread.start();
                isLaunched = true;
                
            } catch (IOException ex) {
                this.Trace("Error creating ServerThread: " + ex.getMessage());
            }
        }
        else
        {
            //Stop the Server
            serverThread.interrupt();
            
            isLaunched = false;
            UnFreezeUI();
        }
    }//GEN-LAST:event_Start_Stop_ButtonActionPerformed
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int port = (int) this.Port_Spinner.getModel().getValue();
        config.setProperty(Consts.ConfigPort, ""+port);
        
        int poolSize = (int) this.Pool_Spinner.getModel().getValue();
        config.setProperty(Consts.ConfigPoolSize, ""+poolSize);
        
        String url = DBurl_TextField.getText();
        config.setProperty(Consts.ConfigDBString, ""+url);
                
        ConfigFolderManager.SaveProperties(config);
        
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Main">
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        FlatLightLaf.setup();
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Payement_Server().setVisible(true);
            }
        });
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Generated Properties">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AbortButton;
    private javax.swing.JTextField DBurl_TextField;
    private javax.swing.JLabel Logs;
    private javax.swing.JLabel Logs1;
    private javax.swing.JLabel Logs2;
    private javax.swing.JLabel Logs3;
    private javax.swing.JSpinner Pool_Spinner;
    private javax.swing.JSpinner Port_Spinner;
    private javax.swing.JButton Start_Stop_Button;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable logs_table;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
