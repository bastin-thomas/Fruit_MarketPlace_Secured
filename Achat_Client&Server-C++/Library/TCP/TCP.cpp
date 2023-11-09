#include "TCP.hpp"


int ServerSocket(int port){
    int sEcoute = -1;
    struct addrinfo *result;

    //Call 'Socket()' to create the socket
    if((sEcoute = socket(AF_INET, SOCK_STREAM, 0)) == -1){
        perror("Error: Socket not bindable");
        exit(1);
    }

    cerr << "A Server Socket has been created ( " << sEcoute << " )" << endl;


    // construit l’adresse réseau de la socket par appel à getaddrinfo()
    try{
        result = Getaddrinfo("0.0.0.0", to_string(port));
    }
    catch(const char* errorMessage){
        cerr << "ERROR: " << errorMessage << endl;
    }
    
    
    // fait appel à bind() pour lier la socket à l’adresse réseau
    char bindhost[NI_MAXHOST];
    char bindport[NI_MAXSERV];
    struct addrinfo* info;

    getnameinfo(result->ai_addr, result->ai_addrlen, bindhost, NI_MAXHOST, bindport, NI_MAXSERV, NI_NUMERICSERV | NI_NUMERICHOST);

    cout << "Mon Adresse IP: " << bindhost << " -- Mon Port: " << bindport << endl;

    //Force le rebind:
    int flag = 1;
    setsockopt(sEcoute,SOL_SOCKET,SO_REUSEADDR,&flag,sizeof(int));

    // Liaison de la socket à l'adresse
    if (bind(sEcoute, result->ai_addr, result->ai_addrlen) < 0)
    {
        throw "Erreur de bind()";
    }
    freeaddrinfo(result);
    cerr<< "bind() reussi !" << endl;

    return sEcoute;
}

struct addrinfo* Getaddrinfo(std::string ip, std::string port){
    struct addrinfo hints;
    struct addrinfo *result;

    memset(&hints, 0, sizeof(struct addrinfo));

    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE | AI_NUMERICSERV;

    if(getaddrinfo(ip.c_str(), port.c_str(), &hints, &result) != 0){
        throw "Address are not standart, please rewrite them.";
    }

    return result;
}


int Accept(int sEcoute,char *ipClient){
    // fait appel à listen()
    if(ListenOnly(sEcoute) == -1){
        throw "Erreur de Listen();";
    }

    cout<<"listen() réussi !"<<endl;

    // fait appel à accept()
    
    int sService;
    sService = AcceptOnly(sEcoute, ipClient);
    return sService;
}

int ListenOnly(int sEcoute){
    return listen(sEcoute,SOMAXCON);
}

int AcceptOnly(int sEcoute, char *ipClient){
    struct sockaddr_in adrClient;
    socklen_t adrClientLen;
    
    int sService;
    
    if ((sService = accept(sEcoute,(sockaddr*) &adrClient, &adrClientLen)) == -1)
    {
        throw("Erreur de accept()");
    }
    cerr << "accept() reussi !" << endl;
    cerr << "socket de service = " << sService << endl;

    // récupère éventuellement l’adresse IP distante du client 
    // qui vient de se connecter.

    
    char bindhost[NI_MAXHOST];
    char bindport[NI_MAXSERV];
    
    getpeername(sService,(struct sockaddr*)&adrClient, &adrClientLen);
    
    getnameinfo((struct sockaddr*) &adrClient, adrClientLen, 
    bindhost, NI_MAXHOST, bindport, NI_MAXSERV, NI_NUMERICSERV | NI_NUMERICHOST);

    cout << "Client connecte --> Adresse IP: " << bindhost << " -- Port: " << bindport << endl;

    ipClient = bindhost;
    return sService;
}


int ClientSocket(string ipServeur, int port){
    int sClient;
    struct addrinfo *result;
    memset(&result, 0, sizeof(struct addrinfo));

    // Creation de la socket
    if ((sClient = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        throw "Erreur de socket()";
    }

    cerr << "socket creee = " << sClient << endl;

    // Construction de l'adresse du serveur
    try{
        result = Getaddrinfo(ipServeur, to_string(port));
    }
    catch(const char* errorMessage){
        throw errorMessage;
    }
    

    // Demande de connexion
    if (connect(sClient,result->ai_addr, result->ai_addrlen) == -1)
    {
        throw "Connexion au Server impossible";
    }

    cout << "connection established with, IP: " << ipServeur << " -- Port: " << port << endl;

    return sClient;
}

int Send(int sSocket, string data){

    //Mise en mémoire dans un buffer de char.
    char buffer[TAILLE_MAX_DATA];
    strcpy(buffer, data.c_str());

    return Send(sSocket, buffer, data.length());
}


//Permet d'envoyer des bytes sur un socket
int Send(int sSocket,char* data,int taille){
    if (taille > TAILLE_MAX_DATA)
        return -1;
    
    // Preparation de la charge utile
    char trame[TAILLE_MAX_DATA+2];
    memcpy(trame,data,taille);

    //Ajout des caractères de fin de chaines
    trame[taille] = '&';
    trame[taille+1] = ')';
    
    // Ecriture sur la socket
    int retval = write(sSocket,trame,taille+2)-2;

    return retval;
}


string Receive(int sSocket){
    //Mise en mémoire dans un buffer de char.
    char buffer[TAILLE_MAX_DATA];
    string data;
    int size = Receive(sSocket, buffer);

    //Si 0 ou -1 on sort car pas de message dans le string
    if(size == 0 || size == -1){
        return "CRITICAL@";
    }
    buffer[size] = '\0';

    return buffer;
}

//Permet de recevoir des bytes sur la socket
int Receive(int sSocket,char* data){
    bool fini = false;
    int nbLus, i = 0;
    char lu1,lu2;

    while(!fini)
    {
        //Lecture des données sur le pipe
        if ((nbLus = read(sSocket,&lu1,1)) == -1)       
            return -1;
        //If can't read anymore, go out of the function
        if (nbLus == 0) 
            return i;
    

        //If 1st end char found, check if the other is also here
        if (lu1 == '&')
        {
            //Read next char
            if ((nbLus = read(sSocket,&lu2,1)) == -1)
                return -1;

            if (nbLus == 0) return i;
        
            //If 2nd is the end char, end loop
            if (lu2 == ')') 
                fini = true;
            //If not, add the lu1 and lu2 to the localstream
            else
            {
                data[i] = lu1;
                data[i+1] = lu2;
                i += 2;
            }
        }
        //If not a specific char, just add it to the localstream
        else
        {
            data[i] = lu1;
            i++;
        }
    }

    return i;
}


