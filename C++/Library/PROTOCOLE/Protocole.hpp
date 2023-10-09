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

////////////////////////////////
////////// REQUEST /////////////
////////////////////////////////

// PARSING REQUEST //
// string ParseRequest();

// LOGIN //
void SendLogin(int socket, string nom, string mdp);
void SendCreateLogin(int socket, string nom, string mdp);

// OPERATION //
string SendOperation(string sendOpe);

// LOGOUT //
string SendLogout(string sendOut);





////////////////////////////////
////////// UTILS ///////////////
////////////////////////////////

vector<string> mystrtok(string str, char delim);

#endif