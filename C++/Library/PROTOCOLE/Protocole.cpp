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
    
    if(s1.size() == 1){
        throw "erreur protocole";
    }

    if(s1[1] == "OK")
        return;

    s2 = mystrtok(s1.at(1), '#');

    if(s2[0] == "KO"){
        if(s2[1] == "NO_LOGIN")
            throw "Le login encodé n'existe pas";

        else if(s2[1] == "BAD_LOGIN")
            throw "Le mot de passe entré n'existe pas";
            
        else
            throw "Erreur inattendue";
    }
}

void SendCreateLogin(int socket, string nom, string mdp){
    stringstream s;
    vector<string> s1,s2;
    string rep = "CREATELOGIN@";

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

    s << rep << idArticle << "#";
    
    cout << s.str() << endl;

    Send(socket, s.str());

    rep = Receive(socket);
    cout << rep << endl;
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

    cout << s.str() << endl;

    Send(socket, s.str());

    rep = Receive(socket);

    cout << rep << endl;

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
vector<caddieRows> SendCaddie(int socket){
    vector<caddieRows> Caddie;
    stringstream s;
    vector<string> s1, s2, rowList;
    string rep = "CADDIE@";

    s << rep;

    Send(socket, s.str());

    rep = Receive(socket);


    if(rep == "CADDIE@"){
        return Caddie;
    }

    s1 = mystrtok(rep, '@');

    if(s1[1] == "-1"){
        throw "Erreur de l'operation d'envoi du Caddie";
    }

    rowList = mystrtok(s1[1], '~');
   
    for(string row : rowList){
        if(row == "") break;
        caddieRows tmp;
        s2 = mystrtok(row, '#');

        tmp.idArticle = stoi(s2[0]);
        tmp.intitule = s2[1];
        tmp.quantitee = stoi(s2[2]);
        tmp.prix = stof(s2[3]);

        Caddie.push_back(tmp);
    }
 
    return Caddie; 
}

// CANCEL //
void SendCancel(int socket, int idArticle){
    stringstream s;
    vector<string> s1;
    string rep = "CANCEL@";

    s << rep << idArticle << "#";

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
int SendConfirmer(int socket, string nom){
    stringstream s;
    vector<string> s1,s2;
    string rep = "CONFIRMER@";

    s << rep << nom << "#";

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

    s << rep << endl;

    Send(socket, s.str());
}




///////////////////////////////
/////// Server Response ///////
///////////////////////////////

/// @brief Main logic of the protocol server
/// @param message message send by client
/// @param Caddie the current cadie of the thread
/// @return the server response to be send
string sSMOP(string message, vector<caddieRows>* Caddie, db* DataBase, pthread_mutex_t mutexDB){
    vector<string> CommandElems;
    string response;
    //split string in two parts, one with command, other with parameters
    CommandElems = mystrtok(message, '@');

    vector<string> CommandParam = mystrtok(CommandElems[1], '#');

    if(CommandElems[0] == "LOGIN"){
        
        mLock(&mutexDB);
        response = ResponseLogin(CommandParam, Caddie, DataBase);
        mUnLock(&mutexDB);
        return response;
    }
    else if(CommandElems[0] == "CREATELOGIN"){
        mLock(&mutexDB);
        response = ResponseCreateLogin(CommandParam, Caddie, DataBase);
        mUnLock(&mutexDB);
        return response;
    }
    else if(CommandElems[0] == "CONSULT"){
        mLock(&mutexDB);
        response = ResponseConsult(CommandParam, Caddie, DataBase);
        mUnLock(&mutexDB);
        return response;
    }
    else if(CommandElems[0] == "ACHAT"){
        mLock(&mutexDB);
        response = ResponseAchat(CommandParam, Caddie, DataBase);
        mUnLock(&mutexDB);
        return response;
    }
    else if(CommandElems[0] == "CADDIE"){
        return ResponseCaddie(CommandParam, Caddie, DataBase);
    }
    else if(CommandElems[0] == "CANCEL"){
        mLock(&mutexDB);
        response = ResponseCancel(CommandParam, Caddie, DataBase);
        mUnLock(&mutexDB);
        return response;
    }
    else if(CommandElems[0] == "CANCELALL"){
        mLock(&mutexDB);
        response = ResponseCancelAll(CommandParam, Caddie, DataBase);
        mUnLock(&mutexDB);
        return response;
    }
    else if(CommandElems[0] == "CONFIRMER"){
        mLock(&mutexDB);
        response = ResponseConfirmer(CommandParam, Caddie, DataBase);
        mUnLock(&mutexDB);
        return response;
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
    articles article;
    achats achat;
    stringstream message;
    vector<caddieRows> Caddietmp = *Caddie;
    
    int idArticle = stoi(protocolCommand[0]);
    int quantitee = stoi(protocolCommand[1]);
    
    try{
        article = DataBase->Consult(idArticle);
        achat = DataBase->Achat(idArticle, quantitee);

        if(achat.quantitee == -1){
            cerr << "ACHAT@-1" << endl;
            return "ACHAT@-1";
        }
    }
    catch(const char* m){
        cerr << "ACHAT@-1" << endl;
        return "ACHAT@-1";
    }

    message << "ACHAT@" << achat.idArticle << "#" << achat.quantitee << "#" << achat.prix;


    bool found = false;
    int i = 0;
    for(caddieRows row : Caddietmp){
        if(row.idArticle == idArticle){
            found = true;
            Caddie->at(i).quantitee = Caddie->at(i).quantitee + achat.quantitee;
            return message.str();
        }
        i++;
    }

    
    if(!found && achat.quantitee != 0){
        caddieRows newrow;

        //Add a new row to the caddie
        newrow.idArticle = idArticle;
        newrow.intitule = article.intitule;
        newrow.prix = achat.prix;
        newrow.quantitee = achat.quantitee;

        Caddie->push_back(newrow);
    }

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
    
    for(int i = 0; i< Caddie->size() ; i++){
        message << Caddie->at(i).idArticle << "#" << Caddie->at(i).intitule << "#" << Caddie->at(i).quantitee << "#" << Caddie->at(i).prix << "~";
    }

    return message.str();
}

/// @brief Server Logic on CANCEL request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseCancel(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase)
{
    int idArticle = stoi(protocolCommand[0]);
    
    try{
        DataBase->Cancel(idArticle, Caddie);
    }
    catch(const char* m){
        return "CANCEL@KO";
    }

    return "CANCEL@OK";
}

/// @brief Server Logic on CANCELALL request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseCancelAll(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase)
{   
    vector<caddieRows> tmp = *Caddie;

    try{
        for(caddieRows row : tmp){
            DataBase->Cancel(row.idArticle, Caddie);
        }
    }
    catch(const char* m){
        return "CANCEL@KO";
    }
    
    return "CANCEL@OK";
}

/// @brief Server Logic on CONFIRMER request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseConfirmer(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase)
{
    int idFacture;
    stringstream message;
    string idClient = protocolCommand[0];

    try{
        idFacture = DataBase->Confirmer(idClient, Caddie);
    }
    catch(const char* m){
        return "CONFIRMER@-1";
    }
    
    message << "CONFIRMER@" << idFacture;

    for(caddieRows row : *Caddie){
        cout << row.intitule << endl;
    }

    return message.str();
}

/// @brief Server Logic on LOGOUT request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseLogout(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase)
{
    throw true;
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