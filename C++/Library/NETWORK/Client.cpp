#include "./TCP/TCP.hpp"

// Menu
#define LOGIN   1
#define HMAT    2
#define LISTCMD 3
#define CHMAT   4 
#define ASKMAT  5
#define EXIT    6

// Chargement de la config
properties prop = load_properties(FILENAME);

void handlerSIGINT(int sig);

int sClient;

int main(){

    sClient = ClientSocket(prop.ip, prop.port);
    cout << "Log success" << endl;
    
    Send(sClient, "Hello from client");

    string message = Receive(sClient);

    cout << "Message received: " << message <<  endl;

    // Faire protocole, Menu, pôle Thread, mutex

    close(sClient);
    return 0;
}