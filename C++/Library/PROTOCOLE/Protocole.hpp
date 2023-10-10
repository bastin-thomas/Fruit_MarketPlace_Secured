#ifndef PROTOCOLE_H
#define PROTOCOLE_H

#include <iostream>
#include <string>
#include <vector>
#include <string.h>
#include <sstream>
#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>
#include "../TCP/TCP.hpp"
#include "../DATABASE/db.hpp"

using namespace std;

////////////////////////////////
/////// Client Request /////////
////////////////////////////////

// LOGIN //
void SendLogin(int socket, string nom, string mdp);
void SendCreateLogin(int socket, string nom, string mdp);

// CONSULT //
articles SendConsult(int socket, int idArticle);

// ACHAT //
achats SendAchat(int socket, int idArticle, int quantitee);

// CADDIE //
void SendCaddie(int socket);

// CANCEL //
void SendCancel(int socket, int idArticle);
void SendCancelAll(int socket);

// CONFIRMER //
int SendConfirmer(int socket);

// LOGOUT //
void SendLogout(int socket);


///////////////////////////////
/////// Server Response ///////
///////////////////////////////

// Protocol Server Main Logic //
string sSMOP(string message, vector<caddieRows> *Caddie, db *DataBase, string idClient);

string ResponseLogin(vector<string> protocolCommand, vector<caddieRows> *Caddie, db *DataBase);
string ResponseCreateLogin(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase);
string ResponseConsult(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase);
string ResponseAchat(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase);
string ResponseCaddie(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase);
string ResponseCancel(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase);
string ResponseCancelAll(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase);
string ResponseConfirmer(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase, string idClient);
string ResponseLogout(vector<string> protocolCommand, vector<caddieRows>* Caddie, db* DataBase);





////////////////////////////////
////////// UTILS ///////////////
////////////////////////////////

vector<string> mystrtok(string str, char delim);

#endif