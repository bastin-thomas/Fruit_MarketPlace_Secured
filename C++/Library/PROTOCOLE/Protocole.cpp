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

    Send(socket, s.str());

    rep = Receive(socket);

    s1 = mystrtok(rep, '@');
    
    if(s1.size() == 1){
        throw "erreur protocole";
    }

    s2 = mystrtok(s1.at(1), '#');

    if(s2.size() == 1){
        throw "erreur protocole";
    }

    if(s2[0] == "ko"){
        throw s2[1];
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

    if(s2[0] == "ko"){
        throw s2[1];
    }
}




///////////////////////////////
/////// Server Response ///////
///////////////////////////////

/// @brief Main logic of the protocol server
/// @param message message send by client
/// @param Caddie the current cadie of the thread
/// @return the server response to be send
string SMOP(string message, vector<caddieRows>* Caddie){
    vector<string> CommandElems;
    
    //split string in two parts, one with command, other with parameters
    CommandElems = mystrtok(message, '@');

    vector<string> CommandParam = mystrtok(CommandElems[1], '#');

    if(CommandElems[0] == "LOGIN"){
        return ResponseLogin(CommandParam, Caddie);
    }
    else if(CommandElems[0] == "CREATELOGIN"){
        return ResponseCreateLogin(CommandParam, Caddie);
    }
    else if(CommandElems[0] == "CONSULT"){
        return ResponseConsult(CommandParam, Caddie);
    }
    else if(CommandElems[0] == "ACHAT"){
        return ResponseAchat(CommandParam, Caddie);
    }
    else if(CommandElems[0] == "CADDIE"){
        return ResponseCaddie(CommandParam, Caddie);
    }
    else if(CommandElems[0] == "CANCEL"){
        return ResponseCancel(CommandParam, Caddie);
    }
    else if(CommandElems[0] == "CANCELALL"){
        return ResponseCancelAll(CommandParam, Caddie);
    }
    else if(CommandElems[0] == "CONFIRMER"){
        return ResponseConfirmer(CommandParam, Caddie);
    }
    else if(CommandElems[0] == "LOGOUT"){
        return ResponseLogout(CommandParam, Caddie);
    }
    else{
        return "CRITICAL";
    }
}

/// @brief Server Logic on LOGIN request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseLogin(vector<string> protocolCommand, vector<caddieRows>* Caddie)
{
    Login user;
    user.nom = protocolCommand[0];
    user.mdp = protocolCommand[1];

    
}

/// @brief Server Logic on CREATELOGIN request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseCreateLogin(vector<string> protocolCommand, vector<caddieRows>* Caddie)
{
    return string();
}

/// @brief Server Logic on CONSULT request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseConsult(vector<string> protocolCommand, vector<caddieRows>* Caddie)
{
    return string();
}

/// @brief Server Logic on ACHAT request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseAchat(vector<string> protocolCommand, vector<caddieRows>* Caddie)
{
    return string();
}

/// @brief Server Logic on CADDIE request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseCaddie(vector<string> protocolCommand, vector<caddieRows>* Caddie)
{
    return string();
}

/// @brief Server Logic on CANCEL request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseCancel(vector<string> protocolCommand, vector<caddieRows>* Caddie)
{
    return string();
}

/// @brief Server Logic on CANCELALL request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseCancelAll(vector<string> protocolCommand, vector<caddieRows>* Caddie)
{
    return string();
}

/// @brief Server Logic on CONFIRMER request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseConfirmer(vector<string> protocolCommand, vector<caddieRows>* Caddie)
{
    return string();
}

/// @brief Server Logic on LOGOUT request
/// @param protocolCommand parameters from the string command
/// @return the server response to be send
string ResponseLogout(vector<string> protocolCommand, vector<caddieRows>* Caddie)
{
    return string();
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