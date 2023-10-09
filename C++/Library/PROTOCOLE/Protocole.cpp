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

// Protocol Server Main Logic //
string SMOP(string message){

    //TODO: PARSE DATA
    return message;
}

void ResponseLogin(int socket, string protocolCommand){

}

void ResponseCreateLogin(int socket, string protocolCommand){

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