#include "./TCP/TCP.hpp"

int sEcoute;
int sService;

int main(){
    sEcoute = ClientSocket("10.59.22.30", 50001);
    cout << "Log success" << endl;


    return 0;
}


