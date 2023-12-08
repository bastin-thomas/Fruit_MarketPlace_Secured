/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.stockmanagement.HttpServer;

import be.hepl.generic_server_tcp.Logger;
import be.hepl.stockmanagement.Utils.DBStock;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Arkios
 */
public abstract class MyHttpHandler implements HttpHandler {
    protected final Logger log;
    protected final DBStock db;
    
    public MyHttpHandler(Logger Log, DBStock db) {
        this.log = Log;
        this.db = db;
    }
    
    protected void Erreur404(HttpExchange exchange) {
        try {
            String reponse = "Fichier HTML introuvable !!!";
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(404, reponse.length());
            
            OutputStream os = exchange.getResponseBody();
            os.write(reponse.getBytes());
            os.close();
            log.Trace("Page introuvable, renvoie d'une erreur 404");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(IndexHandler.class.getName()).log(Level.SEVERE, null, ex);
            log.Trace("Erreur: " + ex.getMessage());
        }
    }
    
    
    protected void SendResponse(HttpExchange exchange, JSONObject response) throws IOException{       
        exchange.sendResponseHeaders(200, response.toString().getBytes().length);
        exchange.getResponseHeaders().set("Content-Type", "text/json");
        exchange.getResponseBody().write(response.toString().getBytes());
        exchange.getResponseBody().close();
    }
    
    
    protected Map<String, String> parseQueryParams(String query)
    {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null)
        {
            String[] params = query.split("&");
            for (String param : params){
                String[] keyValue = param.split("=");
                if (keyValue.length == 2){
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }

}
