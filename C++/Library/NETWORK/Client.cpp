#include "./TCP/TCP.hpp"

int sClient;

int main(){
    sClient = ClientSocket("127.0.0.1", 50001);
    cout << "Log success" << endl;
    
    Send(sClient, "Hello from client");

    string message = Receive(sClient);

    cout << "Message received: " << message <<  endl;

    close(sClient);
    return 0;
}