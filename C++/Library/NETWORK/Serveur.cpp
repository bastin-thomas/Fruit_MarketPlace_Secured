#include "./TCP/TCP.hpp"

int sEcoute;
int sService;

int main(){
    sEcoute = ServerSocket(50001);

    sService = Accept(sEcoute, NULL);

    cout << "Log succes" << endl;

    cout << "Wait message" << endl;

    char message[TAILLE_MAX_DATA-10];
    string mymessagestring = "Hello from Server";

    int retval;
    if((retval = Receive(sService, message)) == -1){
        perror("Erreur lors de la réception du message");
        cout << retval << endl;
    }

    cout << "Message received: " << message << endl;

    strcpy(message, mymessagestring.c_str());

    Send(sService, message, mymessagestring.length());
    


    close(sEcoute);
    close(sService);
    return 0;
}