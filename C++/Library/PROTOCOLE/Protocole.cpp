#include "Protocole.hpp"

////// PARSING
// string ParseRequest(T& s){
//     string formatchaine;

//     if constexpr (is_same_v<T, Login>) {
        
//         cout << "chaine : " << s.nom << " " << s.mdp << endl;
        
//         formatchaine += s.nom + "#";
        
//         formatchaine += s.mdp;
        
//         cout << "chaine formatée : " << formatchaine << endl;

//     }
    
//     return formatchaine;
// }

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

////// OPERATION CLIENT

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

string SendOperation(string sendOpe){
    string rep;

    if(sendOpe.length() == 0){

        rep = "OPE#";
        //Operation + champs adéquats
        rep += sendOpe;

    }else
        rep = "LOGIN@ko"; // + raison
        rep += "ERREUR";

    cout << "rep : " << rep << endl;
    return rep;
}

////// LOGOUT
string SendLogout(string sendOut){
    string rep;

    if(sendOut.length() == 0){
        rep = "LOGOUT";
    }

    return rep;
}

















////// OPERATION SERVER

// string RequestLogin(string receiveId){
//     string rep;

//     if(receiveId.length() == 0){
//         rep = "LOGIN@ok#";

//         rep += receiveId;
//     }else
//         rep = "LOGIN@ko";

//     return rep;
// }

// string RequestOperation(string receiveOpe){
//     string rep;

//     if(receiveOpe.length() == 0){
//         rep = "OPE@ok#"; // + résultat

//         rep += receiveOpe;
//     }else
//         rep = "LOGIN@ko#";
//         rep += "ERREUR";

//     return rep;
// }

// ////// LOGOUT
// string RequestLogout(string receiveOut){
//     string rep;

//     if(receiveOut.length() == 0){
//         rep = "LOGOUT@ok";

//     }else
//         rep = "LOGOUT@ko";

//     return rep;
// }