#ifndef TCP_H
#define TCP_H

#include <iostream>
#include <string>

#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdio.h>

#define TAILLE_MAX_DATA 10000

int ServerSocket(int port);
int Accept(int sEcoute,char *ipClient);

int ClientSocket(char* ipServeur,int portServeur);

int Send(int sSocket,char* data,int taille);
int Receive(int sSocket,char* data);
#endif