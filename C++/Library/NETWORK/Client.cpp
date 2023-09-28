#include "./TCP/TCP.hpp"

int sEcoute;
int sService;

int main(){
    sEcoute = ClientSocket("127.0.0.1", 50001);
    cout << "Log success" << endl;
    return 0;
}


