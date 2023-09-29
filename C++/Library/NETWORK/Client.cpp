#include "./TCP/TCP.hpp"

int sClient;

int main(){
    sClient = ClientSocket("10.59.22.30", 50001);
    cout << "Log success" << endl;

    char message[TAILLE_MAX_DATA-10];
    string mystringmessage = "Hello from client";

    strcpy(message, mystringmessage.c_str());
    
    Send(sClient, message, mystringmessage.length());

    if(Receive(sClient, message) == -1){
        perror("Erreur lors de la réception du message");
    }

    cout << "Message received: " << message <<  endl;

    close(sClient);
    return 0;
}