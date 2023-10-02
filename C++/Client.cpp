#include "../TCP/TCP.hpp"
#include "../PROPERTIES/Properties.hpp"

// Chargement de la config
properties prop = load_properties("./Client.cfg");

void handlerSIGINT(int sig);

int sClient;

int main(){

    sClient = ClientSocket(prop.ip, prop.port);
    cout << "Log success" << endl;
    
    Send(sClient, "Hello from client");

    string message = Receive(sClient);

    cout << "Message received: " << message <<  endl;

    close(sClient);
    return 0;
}