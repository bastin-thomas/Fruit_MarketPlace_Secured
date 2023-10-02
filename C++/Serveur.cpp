#include "./Library/PROPERTIES/Properties.hpp"
#include "./Library/TCP/TCP.hpp"

int sEcoute;
int sService;

// Chargement de la config
ServerProperties prop = getServerProperties();

int main(){
    sEcoute = ServerSocket(prop.port);
    sService = Accept(sEcoute, NULL);

    cout << "Log succes" << endl;

    string message = Receive(sService);

    cout << "Message received: " << message << endl;

    Send(sService, "Hello From Server"); 

    // Faire protocole, Menu, pôle Thread, mutex
    

    close(sEcoute);
    close(sService);
    return 0;
}