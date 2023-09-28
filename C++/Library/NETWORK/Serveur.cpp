#include "./TCP/TCP.hpp"

int sEcoute;
int sService;

int main(){
    sEcoute = ServerSocket(50001);

    sService = Accept(sEcoute, NULL);
    
    cout << "Log succes" << endl;
    return 0;
}


