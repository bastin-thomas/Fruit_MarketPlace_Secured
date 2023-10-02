#ifndef MYPROPERTIES_H
#define MYPROPERTIES_H

#include <iostream>
#include <string>
#include <string.h>

#include <unistd.h>
#include <stdio.h>
#include <vector>

#include <fstream>
#include <sstream>
#include <wchar.h>
#include <codecvt>
#include <locale>
#include <ctime>

using namespace std;

struct ClientProperties {
	int port;
    string ip;
};

struct ServerProperties {
	int port;
	int nbrMaxClients;
};

ClientProperties getClientProperties();
ServerProperties getServerProperties();

vector<string> getTokens(string line, const wchar_t * sep);

#endif