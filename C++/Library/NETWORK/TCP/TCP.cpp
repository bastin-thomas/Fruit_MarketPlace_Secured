#include "TCP.hpp"


int ServerSocket(int port){
    int sEcoute = -1;
    struct addrinfo *result;

    //Call 'Socket()' to create the socket
    if((sEcoute = socket(AF_INET, SOCK_STREAM, 0)) == -1){
        perror("TCP Error: Socket not bindable");
        exit(1);
    }

    cout << "A Server Socket has been created ( " << sEcoute << " )" << endl;


    // construit l’adresse réseau de la socket par appel à getaddrinfo()
    try{
        result = Getaddrinfo("0.0.0.0", to_string(port));
    }
    catch(const char* errorMessage){
        cout << "ERROR: " << errorMessage << endl;
    }
    
    
    // fait appel à bind() pour lier la socket à l’adresse réseau
    char bindhost[NI_MAXHOST];
    char bindport[NI_MAXSERV];

    struct addrinfo* info;

    getnameinfo(result->ai_addr, result->ai_addrlen, bindhost,
    NI_MAXHOST, bindport, NI_MAXSERV, NI_NUMERICSERV | NI_NUMERICHOST);

    printf("Mon Adresse IP: %s -- Mon Port: %s\n",bindhost,bindport);

    // Liaison de la socket à l'adresse
    if (bind(sEcoute, result->ai_addr, result->ai_addrlen) < 0)
    {
        perror("Erreur de bind()");
        exit(1);
    }
    freeaddrinfo(result);
    printf("bind() reussi !\n");

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
    if(Listen(sEcoute) == -1){
        perror("Erreur de Listen();");
        exit(1);
    }

    cout<<"listen() réussi !"<<endl;

    // fait appel à accept()

    int sService;
    if ((sService = accept(sEcoute,NULL,NULL)) == -1)
    {
        perror("Erreur de accept()");
        exit(1);
    }
    printf("accept() reussi !\n");
    printf("socket de service = %d\n",sService);

    // récupère éventuellement l’adresse IP distante du client 
    // qui vient de se connecter.

    struct sockaddr_in adrClient;
    socklen_t adrClientLen;
    char bindhost[NI_MAXHOST];
    char bindport[NI_MAXSERV];
    
    getpeername(sService,(struct sockaddr*)&adrClient,&adrClientLen);
    
    getnameinfo((struct sockaddr*) &adrClient,
    adrClientLen,bindhost,NI_MAXHOST,bindport,
    NI_MAXSERV,NI_NUMERICSERV | NI_NUMERICHOST);
    
    ipClient = bindhost;
    printf("Client connecte --> Adresse IP: %s -- Port: %s\n",bindhost, bindport);

    return sService;
}

int Listen(int sEcoute){
    return listen(sEcoute,SOMAXCON);
}




int ClientSocket(char* ipServeur,int port){
    int sClient;
    struct addrinfo *result;

    memset(&result, 0, sizeof(struct addrinfo));

    // Creation de la socket
    if ((sClient = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        perror("Erreur de socket()");
        exit(1);
    }

    printf("socket creee = %d\n",sClient);

    // Construction de l'adresse du serveur
    try{
        result = Getaddrinfo("0.0.0.0", to_string(port));
    }
    catch(const char* errorMessage){
        cout << "ERROR: " << errorMessage << endl;
    }
    

    // Demande de connexion
    if (connect(sClient,result->ai_addr,result->ai_addrlen) == -1)
    {
        perror("Erreur de connect()");
        exit(1);
    }

    printf("connect() reussi !\n");

    return sClient;
}



int Send(int sSocket,char* data,int taille){
    return -1;
}


int Receive(int sSocket,char* data){
    return -1;
}


