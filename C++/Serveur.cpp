#include "./Library/DATABASE/db.hpp"
#include "./Library/PROTOCOLE/Protocole.hpp"
#include "./Library/PROPERTIES/Properties.hpp"
#include "./Library/TCP/TCP.hpp"
#include "./Library/THREAD/mylibthread_POSIX.h"

//Utility Functions
void initSig(void);
void SIG_INT(int sig_num);
void initMut(void);
void initCond(void);
void initServices(void);

void showQueue(void);



//Global Variable
int sListen = -1;

vector<int> pendingClientQueue;

int WriteCurs = 0;
int ReadCurs = 0;

fstream mylog;


db* DataBase;

//Mutex and VarCond
pthread_mutex_t mutexService;
pthread_cond_t condService;


// Config Loading
ServerProperties prop;

int main(){
    int sService = -1;
    char* ipclient;

    //Open a fileLog
    mylog.open("Servlog.txt", ios::out);
    // Redirect cerr to file
    cerr.rdbuf(mylog.rdbuf());

    prop = getServerProperties();

    cerr << "Init DB Connexion: " << prop.db_ip << ", " << prop.db_user << ", " << prop.db_pass << ", " << prop.db_name << endl;
    DataBase = new db(prop.db_ip, prop.db_user, prop.db_pass, prop.db_name);
    
    
    //Do some initialization thing
    initSig();
    initMut();
    initCond();
    initServices();
    

    try{
        sListen = ServerSocket(prop.port);
        ListenOnly(sListen);
        cerr << "Listen Reussi" << endl;
    }
    catch(const char* message){
        cerr << "MAIN THREAD(error): " << message << endl;
        cout << "Le socket existe déjà sur la machine." << endl;
        exit(50);
    }
    
    try{
        while((sService = AcceptOnly(sListen, ipclient)) != -1) {
            //Add the element to the vector and show it
            pendingClientQueue.push_back(sService); 
            showQueue();
                   
            //Increment, or put to 0 if end of array.
            WriteCurs++;
            condSig(&condService);
        }
    }
    catch(const char* message){
        cerr << "MAIN THREAD(error): " << message << endl;
        cout << "Erreur critique lors de l'accept" << endl;
        exit(51);
    }
    

    cout << "ListenThread has crashed" << endl;
    return 50;
    
   return 0;
}


/*******************************************\
 *                                          *
 *          Service Thread                  *
 *                                          *
\*******************************************/
void ServiceThread(void){
    int sService = 0;

    while(true){
        vector<caddieRows> Caddie;
        string idUser;

        //Wait a condsignal from listen thread
        mLock(&mutexService);
        while( ReadCurs >= WriteCurs ){
            pthread_cond_wait(&condService, &mutexService);
        }

        //Read socket and free mutex
        sService = pendingClientQueue[ReadCurs];
        pendingClientQueue[ReadCurs] = -1;
        //Increment
        ReadCurs++;
        mUnLock(&mutexService);

        

        // Thread logic
        bool endConnexion = false;
        while(!endConnexion){
            string response;
            string message = Receive(sService);
            cerr << "Message received: " << message << endl;
            
            try{
                response = sSMOP(message, &Caddie, DataBase);
            }
            catch(const char * m){
                cout << "Cant send the message due: " << m << endl;
            }
            catch(bool finish){
                endConnexion = finish;
                continue;
            }
            
            cerr << "Message send: " << response << endl;
            
            try{
                Send(sService, response);
            }
            catch(const char * m){
                cout << "Cant send the message due: " << m << endl;
            }
        }
        
        close(sService);
        //End Client Connexion, return in a wait state (cond wait) 
    }
}




/********************************************\
 *                                           *
 *          Utility Functions                *
 *                                           *
\********************************************/

// Initialize the pool of socket service threads
void initServices(void){
    pthread_t handler;

    for(int i = 0; i<prop.nbrMaxClients ; i++){
        pthread_create(&handler, NULL, (void* (*)(void*))ServiceThread, NULL);
        pthread_detach(handler);
    }   
}


// Initialisation of all used mutex
void initMut(void){
    int error;

    // Initialisation mutexService
    if((error = mInitDef(&mutexService)) != 0){
        cerr << "(SERVEUR " << getTid() << ") Erreur Initialisation mutexService: "<<error<<endl;
        exit(2);
    }
}


// Initialisation Condition variable
void initCond(void){
    int error;

    // Init Var Condition
    if((error = pthread_cond_init(&condService, NULL)) != 0){
        cerr << "(SERVEUR " << getTid() << ") Erreur Initialisation condService: "<<error<<endl;
        exit(2);
    }
}


// Initialize the Signals and set a mask on others
void initSig(void){
    //Init a full mask except sigint    
    sigset_t mask;
    sigfillset(&mask);
    sigdelset(&mask, SIGINT);
    sigprocmask(SIG_SETMASK, &mask, NULL);


    /*SIGINT*/
    struct sigaction signal_action;                /* define table */
    signal_action.sa_handler = SIG_INT;   /* insert handler function */
    signal_action.sa_flags = 0;                    /* init the flags field */
    sigemptyset( &signal_action.sa_mask );     /* are no masked interrupts */
    sigaction( SIGINT, &signal_action, NULL ); /* install the signal_action */
}


//Show in console the queue state
void showQueue(void){
    cerr << "SocketQueue: ";
    for(int i = 0; i< pendingClientQueue.size(); i++){
        cerr << "[" << pendingClientQueue[i] << "] ";
    }
    cerr << endl;
    cerr << "WriteCurs: " << WriteCurs;
    cerr << " -- ReadCurs: " << ReadCurs << endl;
}


/********************************************\
 *                                           *
 *          Signals Functions                *
 *                                           *
\********************************************/

// Signal Override
void SIG_INT(int sig_num){

    close(sListen);

    for(int i = 0 ; i < pendingClientQueue.size() ; i++){
        close(pendingClientQueue[i]);
    }

    cerr<<"\nSIGINT Received"<<endl;
    exit(0);
}