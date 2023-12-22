package com.mymaraichermobile.client;

import android.util.Log;

import com.mymaraichermobile.R;

import java.io.IOException;
import java.util.ArrayList;

public class ProtocoleClient {

    //region Private Variables

    private SocketClient socket;

    //endregion


    //region Constructeurs

    public ProtocoleClient(SocketClient sock){
        socket = sock;
    }

    //endregion

    //region Methods

    public SocketClient getSocket() {
        return socket;
    }

    public void setSocket(SocketClient socket) {
        this.socket = socket;
    }

    public void sendLogin(String nom, String mdp) throws Exception{
        String message = "";

        //Send and wait for response
        try{
            socket.send("LOGIN" + (R.string.SplitCommand) + nom + (R.string.SplitParameters) + mdp);
            Log.d("TRACE LOGIN", "Nom : " + nom + " mdp : " + mdp);
            message = socket.receive();
        }catch(IOException ex){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        //Split Commands name and parameters
        String[] commandsToken = message.split("" + (R.string.SplitCommand));
        if(message.equals(String.valueOf(R.string.critical) + (R.string.SplitCommand)) || commandsToken.length == 1){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        //Check if OK, then do nothing
        if(commandsToken[1].equals("OK")){
            return;
        }

        //If not Ok check params
        String[] paramsToken = commandsToken[1].split("" + (R.string.SplitParameters));

        //If KO Throw an exceptions based on type of params
        if(paramsToken[0].equals("KO")){
            throw new Exception(paramsToken[1]);
        }
    }

    public void sendCreateLogin(String nom, String mdp) throws Exception{
        String message = "";

        //Send and wait for response
        try{
            socket.send("CREATELOGIN" + R.string.SplitCommand + nom + R.string.SplitParameters  + mdp);
            message = socket.receive();
        }catch(IOException ex){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        //Split Commands name and parameters
        String[] commandsToken = message.split("" + R.string.SplitCommand);
        if(message.equals(String.valueOf(R.string.critical) + R.string.SplitCommand) || commandsToken.length == 1){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        //Check if not OK, then throw error
        if(!commandsToken[1].equals("OK"))
        {
            throw new Exception("new Account creation Error");
        }
    }

    public Articles sendConsult(int idArticle) throws Exception{
        String message = "";
        Articles article = null;

        //Send and wait for response
        try{
            socket.send("CONSULT" + (R.string.SplitCommand) + idArticle + (R.string.SplitParameters));
            message = socket.receive();
        }catch(IOException ex){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        //Split Commands name and parameters
        String[] commandsToken = message.split("" + (R.string.SplitCommand));
        if(message.equals(String.valueOf(R.string.critical) + (R.string.SplitCommand)) || commandsToken.length == 1){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        if(commandsToken[1].equals("-1")){
            throw new Exception(String.valueOf(R.string.no_article_found));
        }

        try
        {
            article = new Articles(commandsToken[1]);
        }
        catch(NumberFormatException ex)
        {
            throw new Exception(String.valueOf(R.string.params_format_error));
        }

        Log.d("SENDCONSULT","Article = "+ article);
        return article;
    }


    //On ajoute un élément au caddie
    public Achats sendAchat(int idArticle, int quantitee) throws Exception{
        String message = "";
        Achats achat = null;

        //Send and wait for response
        try{
            socket.send("ACHAT" + (R.string.SplitCommand) + idArticle + (R.string.SplitParameters) + quantitee);
            message = socket.receive();
        }catch(IOException ex){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        //Split Commands name and parameters
        String[] commandsToken = message.split("" + (R.string.SplitCommand));
        if(message.equals(String.valueOf(R.string.critical) + (R.string.SplitCommand)) || commandsToken.length == 1){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        if(commandsToken[1].equals("-1")){
            throw new Exception(String.valueOf(R.string.no_article_found));
        }

        try
        {
            achat = new Achats(commandsToken[1]);
        }
        catch(NumberFormatException ex)
        {
            throw new Exception(String.valueOf(R.string.params_format_error));
        }

        Log.d("ACHAT SENDACHAT","Achat = " + achat);
        return achat;
    }

    // On récupère le caddie
    public ArrayList<CaddieRows> sendCaddie() throws Exception{
        String message = "";
        ArrayList<CaddieRows> caddie = new ArrayList<>();


        //Send and wait for response
        try{
            socket.send("CADDIE" + (R.string.SplitCommand));
            message = socket.receive();
            Log.d("SENDCADDIE","Message Received: " + message);
        }catch(IOException ex){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        //Split Commands name and parameters
        String[] commandsToken = message.split("" + (R.string.SplitCommand));
        if(message.equals(String.valueOf(R.string.critical) + (R.string.SplitCommand))){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        if(message.equals("CADDIE" + (R.string.SplitCommand))){
            return caddie;
        }

        if(commandsToken[1].equals("-1")){
            throw new Exception("ERROR_OPERATION");
        }

        try
        {
            String[] rowList = commandsToken[1].split("" + (R.string.SplitList));

            for(String tmp : rowList){
                caddie.add(new CaddieRows(tmp));
            }
        }
        catch(NumberFormatException ex)
        {
            throw new Exception(String.valueOf(R.string.params_format_error));
        }
        Log.d("SENDCADDIE ROW","Caddie: " );
        for(CaddieRows row : caddie){
            Log.d("","ROW : " + row);
        }

        return caddie;
    }

    // Supprime un objet précis du caddie
    public void sendCancel(int idArticle) throws Exception{
        String message = "";

        //Send and wait for response
        try{
            socket.send("CANCEL" + (R.string.SplitCommand) + idArticle + (R.string.SplitParameters));
            message = socket.receive();
        }catch(IOException ex){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        //Split Commands name and parameters
        String[] commandsToken = message.split("" + (R.string.SplitCommand));
        if(message.equals(String.valueOf(R.string.critical) + (R.string.SplitCommand))){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        if(!commandsToken[1].equals("OK")){
            throw new Exception(String.valueOf(R.string.cancel_error));
        }
    }

    // Supprime tous les objets du caddie
    public void sendCancelAll() throws Exception{
        String message = "";

        try{
            socket.send("CANCELALL" + (R.string.SplitCommand));
            message = socket.receive();
        }catch(IOException ex){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        String[] commandsToken = message.split("" + (R.string.SplitCommand));
        if(message.equals(String.valueOf(R.string.critical) + (R.string.SplitCommand))){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        if(!commandsToken[1].equals("OK")){
            throw new Exception(String.valueOf(R.string.cancel_error));
        }
    }

    public void sendConfirmer(String nom) throws Exception{
        String message = "";

        try{
            socket.send("CONFIRMER" + (R.string.SplitCommand) + nom + (R.string.SplitParameters));
            message = socket.receive();
        }catch(IOException ex){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        String[] commandsToken = message.split("" + (R.string.SplitCommand));
        if(message.equals(String.valueOf(R.string.critical) + (R.string.SplitCommand)) || commandsToken.length == 1){
            throw new Exception(String.valueOf(R.string.endconnexion));
        }

        if(commandsToken[1].equals("-1")){
            throw new Exception("ERROR_BILL");
        }
    }

    public void sendLogout() throws IOException{
        socket.send("LOGOUT" + (R.string.SplitCommand));
    }

    public void close()
    {
        try {
            this.socket.close();
        } catch (IOException ex) {
            Log.d("CLOSE","Cannot Close the ClientSocket");
            System.exit(120);
        }
    }

    //endregion
}
