/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.stockmanagement.HttpServer;

import be.hepl.generic_server_tcp.Logger;
import be.hepl.stockmanagement.Utils.DBStock;
import be.hepl.stockmanagement.model.Article;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author Arkios
 */
public class ApiHandler extends MyHttpHandler {

    private ArrayList<String> connectedClients;

    public ApiHandler(Logger Log, DBStock db) {
        super(Log, db);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();
        
        // CORS (Cross-Origin Resource Sharing)
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "ContentType");
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        
        
        log.Trace("IndexHandler (method:" + requestMethod + ") = " + requestPath);

        if (requestMethod.equalsIgnoreCase("GET")) {
            if (requestPath.endsWith("Articles")) {
                //Manage Articles Procedure
                boolean noProblemo = false;
                String Cause = "";
                JSONObject response = new JSONObject();

                Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());

                if (!queryParams.containsKey("cleSession")) {
                    response.put("Cause", "Paramètre incomplet");
                    SendResponse(exchange, response);
                    return;
                }

                //Check Session Auth:
                //do DB Logic:
                try {
                    ArrayList<Article> articles = db.getArticles();
                    response.put("Articles", articles);
                } catch (Exception ex) {
                    response.put("Cause", Cause);
                }
                
                SendResponse(exchange, response);

            } else if (requestPath.endsWith("Login")) {
                //Manage Login Procedure

            } else if (requestPath.endsWith("Logout")) {
                //Manage Logout procedure
                
                //Check Session Auth:
                
            } else {
                Erreur404(exchange);
            }
        } else if (requestMethod.equalsIgnoreCase("POST")) {
            if (requestPath.endsWith("Articles")) {
                //Manage update of Articles
                boolean noProblemo = false;
                String cause = "";
                JSONObject response = new JSONObject();

                byte[] requestBody = exchange.getRequestBody().readAllBytes();
                String formData = new String(requestBody, StandardCharsets.UTF_8);
                Map<String, String> queryParams = parseQueryParams(formData);

                if (!queryParams.containsKey("cleSession") || !queryParams.containsKey("idArticle")
                        || !queryParams.containsKey("prix") || !queryParams.containsKey("stock")) {

                    response.put("Cause", "Paramètre incomplet");
                    SendResponse(exchange, response);
                    return;
                }

                String cleSession = queryParams.get("cleSession");
                int idArticle = Integer.parseInt(queryParams.get("idArticle"));
                float prix = Float.parseFloat(queryParams.get("prix"));
                int stock = Integer.parseInt(queryParams.get("stock"));

                //Check Session Auth:
                //do DB Logics
                log.Trace("cleSession: " + cleSession);
                log.Trace("idArticles: " + idArticle);
                log.Trace("prix: " + prix);
                log.Trace("stock: " + stock);
                
                try {
                    db.UpdateArticles(idArticle, prix, stock);
                    response.put("Success", true);
                } catch (Exception ex) {
                    log.Trace("UpdateArticles Exception: " + ex.getMessage());
                    response.put("Success", false);
                    response.put("Cause", ex.getMessage());
                }
                
                SendResponse(exchange, response);

            } else {
                Erreur404(exchange);
            }
        } else {
            Erreur404(exchange);
        }
    }

}
