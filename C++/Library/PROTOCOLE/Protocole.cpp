#include "Protocole.hpp"

////////////////////////////////
/////// Client Request /////////
////////////////////////////////

// LOGIN //
void SendLogin(int socket, string nom, string mdp){
    stringstream s;
    vector<string> s1,s2;
    string rep = "LOGIN@";

    s << rep << nom << "#" << mdp;


    cerr << s.str() << endl;

    Send(socket, s.str());

    rep = Receive(socket);

    cerr << rep << endl;

    s1 = mystrtok(rep, '@');
    
    for(string row : s1){
        cout << row << endl;
    }

    if(s1.size() == 1){
        throw "erreur protocole";
    }

    s2 = mystrtok(s1.at(1), '#');

    if(s2.size() == 1){
        throw "erreur protocole";
    }

    if(s2[0] != "OK"){
        throw "Erreur création Login";
    }
}

void SendCreateLogin(int socket, string nom, string mdp){
    stringstream s;
    vector<string> s1,s2;
    string rep = "LOGIN@";

    s << rep << nom << "#" << mdp;

    Send(socket, s.str());

    rep = Receive(socket);

    s1 = mystrtok(rep, '@');

    s2 = mystrtok(s1[1], '#');

    if(s2[0] != "OK"){
        throw "Erreur création nouveau Login";
    }
}

// CONSULT //
articles SendConsult(int socket, int idArticle){
    articles article;
    stringstream s;
    vector<string> s1,s2;
    string rep = "CONSULT@";

    s << rep << idArticle;;

    Send(socket, s.str());

    rep = Receive(socket);

    s1 = mystrtok(rep, '@');

    if(s1[1] == "-1"){
        throw "Article non trouvé";
    }

    s2 = mystrtok(s1[1], '#');

    article.idArticle = stoi(s2[0]);

    article.intitule = s2[1];

    article.stock = stoi(s2[2]);

    article.prix = stof(s2[3]);

    article.image = s2[4];

    return article;
}

// ACHAT //
achats SendAchat(int socket, int idArticle, int quantitee){
    achats achat;
    stringstream s;
    vector<string> s1,s2;
    string rep = "ACHAT@";

    s << rep << idArticle << "#" << quantitee;

    Send(socket, s.str());

    rep = Receive(socket);

    s1 = mystrtok(rep, '@');

    if(s1[1] == "-1"){
        throw "Article non trouvé";
    }

    s2 = mystrtok(s1[1], '#');
    
    achat.idArticle = stoi(s2[0]);

    achat.quantitee = stoi(s2[1]);

    achat.prix = stof(s2[2]);

    if(achat.prix <= 0){
        throw "Stock insuffisant";
    }

    return achat;
}

// CADDIE //
void SendCaddie(int socket){
    stringstream s;
    vector<string> s1;
    string rep = "CADDIE@";

    Send(socket, s.str());

    rep = Receive(socket);

    s1 = mystrtok(rep, '@');

    if(s1[1] == "-1"){
        throw "Erreur de l'opération d'envoi du Caddie";
    }
}

// CANCEL //
void SendCancel(int socket, int idArticle){
    stringstream s;
    vector<string> s1;
    string rep = "CANCEL@";

    s << rep << idArticle;

    Send(socket, s.str());

    rep = Receive(socket);

    s1 = mystrtok(rep, '@');

    if(s1[1] != "OK"){
        throw "Erreur de suppresion d'un article du Caddie";
    }
}

void SendCancelAll(int socket){
    stringstream s;
    vector<string> s1;
    string rep = "CANCELALL@";

    s << rep;

    Send(socket, s.str());

    rep = Receive(socket);

    s1 = mystrtok(rep, '@');

    if(s1[1] != "OK"){
        throw "Erreur lors du Cancel_ALL";
    }
}

// CONFIRMER //
int SendConfirmer(int socket){
    stringstream s;
    vector<string> s1,s2;
    string rep = "CONFIRMER@";

    s << rep;

    Send(socket, s.str());

    rep = Receive(socket);

    s1 = mystrtok(rep, '@');

    if(s1[1] == "-1"){
        throw "Erreur création du numéro de facture";
    }

    return stoi(s1[1]);
}

// LOGOUT //
void SendLogout(int socket){
    stringstream s;
    
    string rep = "LOGOUT@";

    s << rep;

    Send(socket, s.str());
}




///////////////////////////////
/////// Server Response ///////
///////////////////////////////

/// @brief Main logic of the protocol server
/// @param message message send by client
/// @param Caddie the current cadie of the thread
/// @return the server response to be send
string sSMOP(string message, vector<caddieRows>* Caddie, db* DataBase, string idClient){
    vector<string> CommandElems;
    
    //split string in two parts, one with command, other with parameters
    CommandElems = mystrtok(message, '@');

    vector<string> CommandParam = mystrtok(CommandElems[1], '#');

    if(CommandElems[0] == "LOGIN"){
        return ResponseLogin(CommandParam, Caddie, DataBase);
    }
    else if(CommandElems[0] == "CREATELOGIN"){
        return ResponseCreateLogin(CommandParam, Caddie, DataBase);
    }
    else if(CommandElems[0] == "CONSULT"){
        return ResponseConsult(CommandParam, Caddie, DataBase);
    }
    else if(CommandElems[0] == "ACHAT"){
        return ResponseAchat(CommandParam, Caddie, DataBase);
    }
    else if(CommandElems[0] == "CADDIE"){
        return ResponseCaddie(CommandParam, Caddie, DataBase);
    }
    else if(CommandElems[0] == "CANCEL"){
        return ResponseCancel(CommandParam, Caddie, DataBase);
    }
    else if(CommandElems[0] == "CANCELALL"){
        return ResponseCancelAll(CommandParam, Caddie, DataBase);
    }
    else if(CommandElems[0] == "CONFIRMER"){
        return ResponseConfirmer(CommandParam, Caddie, DataBase, idClient);
    }
    else if(CommandElems[0] == "LOGOUT"){
        return ResponseLogout(CommandParam, Caddie, DataBase);
    }
    else{
        return "CRITICAL";
    }
}

/// @brief Server Logic on LOGIN request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseLogin(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase)
{
    Login user;
    user.nom = protocolCommand[0];
    user.mdp = protocolCommand[1];
    cerr<<"nom: " << user.nom << ", mdp: " << user.mdp << endl;
    try{
        DataBase->Login(user.nom, user.mdp);
    }
    catch(const char* m){
        cerr << m << endl;

        stringstream request;
        request << "LOGIN@KO#" << m;
        
        return request.str();
    }

    return "LOGIN@OK";
}

/// @brief Server Logic on CREATELOGIN request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseCreateLogin(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase)
{
    Login user;
    user.nom = protocolCommand[0];
    user.mdp = protocolCommand[1];

    try{
        DataBase->CreateLogin(user.nom, user.mdp);
    }
    catch(const char* m){
        string request = "CREATELOGIN@KO#";
        cerr << m << endl;
        request += m;
        return request;
    }

    return "CREATELOGIN@OK";
}

/// @brief Server Logic on CONSULT request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseConsult(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase)
{
    articles article;
    stringstream message;
    int idArticle = stoi(protocolCommand[0]);
    

    try{
        article = DataBase->Consult(idArticle);
    }
    catch(const char* m){
        return "CONSULT@-1";
    }
    
    message << "CONSULT@" << article.idArticle << "#" << article.intitule << "#" << article.stock << "#" << article.prix << "#" << article.image;

    return message.str();
}

/// @brief Server Logic on ACHAT request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseAchat(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase)
{
    achats achat;
    stringstream message;
    int idArticle = stoi(protocolCommand[0]);
    int quantitee = stoi(protocolCommand[1]);
    
    try{
        achat = DataBase->Achat(idArticle, quantitee);
    }
    catch(const char* m){
        return "ACHAT@-1";
    }
    
    message << "ACHAT@" << achat.idArticle << "#" << achat.quantitee << "#" << achat.prix;

    return message.str();
}

/// @brief Server Logic on CADDIE request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseCaddie(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase)
{
    string m;
    stringstream message;
    
    message << "CADDIE@"; 

    if(Caddie->size() == 0){
        return message.str();
    }

    for(caddieRows row : *Caddie){
        message << row.idArticle << "#" << row.intitule << "#" << row.quantitee << "#" << row.prix << "~";
    }

    m = message.str();
    m.pop_back();
    
    return m;
}

/// @brief Server Logic on CANCEL request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseCancel(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase)
{
    stringstream message;
    int idArticle = stoi(protocolCommand[0]);
    
    try{
        DataBase->Cancel(idArticle, Caddie);
    }
    catch(const char* m){
        return "CANCEL@KO";
    }

    return "CANCEL@KO";
}

/// @brief Server Logic on CANCELALL request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseCancelAll(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase)
{
    stringstream message;
    int idArticle = stoi(protocolCommand[0]);
    
    try{
        DataBase->Cancel(idArticle, Caddie);
    }
    catch(const char* m){
        return "CANCEL@KO";
    }
    
    return "CANCEL@KO";
}

/// @brief Server Logic on CONFIRMER request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseConfirmer(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase, string idClient)
{
    int idFacture;
    stringstream message;
    
    try{
        idFacture = DataBase->Confirmer(idClient, *Caddie);
    }
    catch(const char* m){
        return "CONFIRMER@-1";
    }
    
    message << "CONFIRMER@" << idFacture;

    return message.str();
}

/// @brief Server Logic on LOGOUT request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseLogout(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase)
{
    return "LOGOUT@";
}













////////////////////////////////
////////// UTILS ///////////////
////////////////////////////////

vector<string> mystrtok(string str, char delim){
    vector<string> tokens;
    string temp = "";
    for(int i = 0; i < str.length(); i++){
        if(str[i] == delim){
            tokens.push_back(temp);
            temp = "";
        }
        else
            temp += str[i];           
    }
    tokens.push_back(temp);
    return tokens;
}