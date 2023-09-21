#include "TCP.h"


int ServerSocket(int port){
    int sEcoute = -1;

    //Call 'Socket()' to create the socket
    if((sEcoute = socket(AF_INET, SOCK_STREAM, 0)) == -1){
        perror("TCP Error: Socket not bindable");
        exit(1);
    }

    cout << "A Server Socket has been created (" << sEcoute << ")" << endl;

    

    // construit l’adresse réseau de la socket par appel à getaddrinfo()

    // fait appel à bind() pour lier la socket à l’adresse réseau


}


int Accept(int sEcoute,char *ipClient){

}


int ClientSocket(char* ipServeur,int portServeur){

}



int Send(int sSocket,char* data,int taille){

}


int Receive(int sSocket,char* data){

}


