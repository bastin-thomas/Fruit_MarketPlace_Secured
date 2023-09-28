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
#define SOMAXCON 50



int ServerSocket(int port);
int Accept(int sEcoute,char *ipClient);

int ClientSocket(char* ipServeur,int portServeur);

int Send(int sSocket,char* data,int taille);
int Receive(int sSocket,char* data);


struct addrinfo* Getaddrinfo(string ip, string port);

int Listen(int sEcoute);

#endif