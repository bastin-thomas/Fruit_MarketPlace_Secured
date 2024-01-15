/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package be.hepl.stockmanagement.HttpServer;

import be.hepl.generic_server_tcp.Logger;
import be.hepl.stockmanagement.Utils.Consts;
import be.hepl.stockmanagement.Utils.DBStock;
import com.sun.net.httpserver.HttpExchange;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 *
 * @author Arkios
 */
public class IndexHandler extends MyHttpHandler {

    public IndexHandler(Logger Log, DBStock db) {
        super(Log, db);
    }
    
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath(); // all string
        String requestMethod = exchange.getRequestMethod();
        
        log.Trace("IndexHandler (method:" + requestMethod + ") = " + requestPath);
        
        
        //HtmlPage Handling
        if(requestPath.endsWith("/")){
            File htmlpage = new File(Consts.WebSitePath + "index.html"); // load Meta-data
            if (htmlpage.exists()){
                exchange.sendResponseHeaders(200, htmlpage.length());
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                
                OutputStream os = exchange.getResponseBody();
                Files.copy(htmlpage.toPath(), os);
                os.close();
                
                log.Trace("Renvoie de la page: " + htmlpage.getName());
            }
            else Erreur404(exchange);
        } else 
        
        if(requestPath.endsWith(".html")){
            File htmlpage = new File(Consts.WebSitePath + requestPath);
            if (htmlpage.exists()){
                exchange.sendResponseHeaders(200, htmlpage.length());
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                
                OutputStream os = exchange.getResponseBody();
                Files.copy(htmlpage.toPath(), os);
                os.close();
                
                log.Trace("Renvoie de la page: " + htmlpage.getName());
            }
            else Erreur404(exchange);
        } else
        
        //Css Handling
        if (requestPath.endsWith(".css"))
        {
            File cssfile = new File(Consts.WebSitePath + requestPath);
            
            if (cssfile.exists()){
                exchange.sendResponseHeaders(200, cssfile.length());
                exchange.getResponseHeaders().set("Content-Type", "text/css");
                
                OutputStream os = exchange.getResponseBody();
                Files.copy(cssfile.toPath(), os);
                os.close();
                log.Trace("Renvoie du fichier css: " + cssfile.getName());
            }
            else Erreur404(exchange);
        } else
            
        //image handling
        if (requestPath.endsWith(".png"))
        {
            File imageFile = new File(Consts.WebSitePath + requestPath);
            
            if (imageFile.exists()){
                exchange.sendResponseHeaders(200, imageFile.length());
                exchange.getResponseHeaders().set("Content-Type", "image/png");
                
                OutputStream os = exchange.getResponseBody();
                Files.copy(imageFile.toPath(), os);
                os.close();
                log.Trace("Renvoie de l'image: " + imageFile.getName());
            }
            else Erreur404(exchange);
        } else
            
        //image handling
        if (requestPath.endsWith(".jpg"))
        {
            File imageFile = new File(Consts.WebSitePath + requestPath);
            
            if (imageFile.exists()){
                exchange.sendResponseHeaders(200, imageFile.length());
                exchange.getResponseHeaders().set("Content-Type", "image/jpeg");
                
                OutputStream os = exchange.getResponseBody();
                Files.copy(imageFile.toPath(), os);
                os.close();
                log.Trace("Renvoie de l'image: " + imageFile.getName());
            } // send auto closed
            else Erreur404(exchange);
        } else
        
        //scripts handling
        if (requestPath.endsWith(".js"))
        {
            File imageFile = new File(Consts.WebSitePath + requestPath);
            
            if (imageFile.exists()){
                exchange.sendResponseHeaders(200, imageFile.length());
                exchange.getResponseHeaders().set("Content-Type", "application/javascript");
                
                OutputStream os = exchange.getResponseBody();
                Files.copy(imageFile.toPath(), os);
                os.close();
                log.Trace("Renvoie du script: " + imageFile.getName());
            }
            else Erreur404(exchange);
        }
        else Erreur404(exchange);
    }
}
