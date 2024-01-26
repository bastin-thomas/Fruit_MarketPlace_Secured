#ifndef TCP_H
#define TCP_H

#include <iostream>
#include <string>
#include <string.h>

#include <netdb.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdio.h>

using namespace std;

#define TAILLE_MAX_DATA 10000
#define SOMAXCON 5

int ServerSocket(int port);
int Accept(int sEcoute,char *ipClient);

int ClientSocket(string ipServeur, int port);

int Send(int sSocket,char* data,int taille);
int Receive(int sSocket,char* data);

int Send(int sSocket, string data);
string Receive(int sSocket);

struct addrinfo* Getaddrinfo(string ip, string port);

int ListenOnly(int sEcoute);
int AcceptOnly(int sEcoute, char *ipClient);

#endif