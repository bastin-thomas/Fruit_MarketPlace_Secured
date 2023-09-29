#include "./TCP/TCP.hpp"

int sEcoute;
int sService;

int main(){
    sEcoute = ServerSocket(50001);

    sService = Accept(sEcoute, NULL);

    cout << "Log succes" << endl;

    string message = Receive(sService);

    cout << "Message received: " << message << endl;

    Send(sService, "Hello From Server");
    


    close(sEcoute);
    close(sService);
    return 0;
}