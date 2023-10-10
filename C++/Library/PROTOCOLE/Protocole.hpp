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

using namespace std;

struct Login{
	string nom;
	string mdp;
};

struct articles{
	int idArticle;
	string intitule;
	float prix;
	int stock;
	string image;
};

struct achats{
	int idArticle;
	int quantitee;
	float prix;
};

struct caddieRows{
	int idArticle;
	string intitule;
	int quantitee;
	float prix;
};





////////////////////////////////
/////// Client Request /////////
////////////////////////////////

// LOGIN //
void SendLogin(int socket, string nom, string mdp);
void SendCreateLogin(int socket, string nom, string mdp);



///////////////////////////////
/////// Server Response ///////
///////////////////////////////

// Protocol Server Main Logic //
string SMOP(string message, vector<caddieRows>* Caddie);


string ResponseLogin(string protocolCommand, vector<caddieRows>* Caddie);
string ResponseCreateLogin(string protocolCommand, vector<caddieRows>* Caddie);
string ResponseConsult(string protocolCommand, vector<caddieRows>* Caddie);
string ResponseAchat(string protocolCommand, vector<caddieRows>* Caddie);
string ResponseCaddie(string protocolCommand, vector<caddieRows>* Caddie);
string ResponseCancel(string protocolCommand, vector<caddieRows>* Caddie);
string ResponseCancelAll(string protocolCommand, vector<caddieRows>* Caddie);
string ResponseConfirmer(string protocolCommand, vector<caddieRows>* Caddie);
string ResponseLogout(string protocolCommand, vector<caddieRows>* Caddie);





////////////////////////////////
////////// UTILS ///////////////
////////////////////////////////

vector<string> mystrtok(string str, char delim);

#endif