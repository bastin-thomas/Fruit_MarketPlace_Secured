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
void SMOP();


void ResponseLogin(int socket, string protocolCommand);
void ResponseCreateLogin(int socket, string protocolCommand);





////////////////////////////////
////////// UTILS ///////////////
////////////////////////////////

vector<string> mystrtok(string str, char delim);

#endif