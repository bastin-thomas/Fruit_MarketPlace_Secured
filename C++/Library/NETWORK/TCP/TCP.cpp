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
    if(ListenOnly(sEcoute) == -1){
        perror("Erreur de Listen();");
        exit(1);
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
        perror("Erreur de accept()");
        exit(1);
    }
    printf("accept() reussi !\n");
    printf("socket de service = %d\n",sService);

    // récupère éventuellement l’adresse IP distante du client 
    // qui vient de se connecter.

    
    char bindhost[NI_MAXHOST];
    char bindport[NI_MAXSERV];
    
    getpeername(sService,(struct sockaddr*)&adrClient, &adrClientLen);
    
    getnameinfo((struct sockaddr*) &adrClient, adrClientLen, 
    bindhost, NI_MAXHOST, bindport, NI_MAXSERV, NI_NUMERICSERV | NI_NUMERICHOST);

    printf("Client connecte --> Adresse IP: %s -- Port: %s\n",bindhost, bindport);

    ipClient = bindhost;
    return sService;
}







int ClientSocket(char* ipServeur, int port){
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
        result = Getaddrinfo(ipServeur, to_string(port));
    }
    catch(const char* errorMessage){
        cout << "ERROR: " << errorMessage << endl;
    }
    

    // Demande de connexion
    if (connect(sClient,result->ai_addr, result->ai_addrlen) == -1)
    {
        perror("Erreur de connect()");
        exit(1);
    }

    printf("connect() reussi !\n");

    return sClient;
}


//Permet d'envoyée des bytes sur un socket
int Send(int sSocket,char* data,int taille){
    if (taille > TAILLE_MAX_DATA)
        return -1;
    
    // Preparation de la charge utile
    char trame[TAILLE_MAX_DATA+2];
    memcpy(trame,data,taille);

    //Ajout des caractères de fin de chaines
    trame[taille] = '#';
    trame[taille+1] = ')';
    
    // Ecriture sur la socket
    int retval = write(sSocket,trame,taille+2)-2;

    return retval;
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
        if (lu1 == '#')
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


