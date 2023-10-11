#include "./Properties.hpp"


//Utility Functions
vector<string> getTokens(string line, const wchar_t * sep){
    vector<string> tokens;
    wstring temp;

    //Convert string to wstring
    wstringstream wss(wstring_convert<codecvt_utf8<wchar_t>>().from_bytes(line));

    while(getline(wss, temp, *sep)){
        //1rst convert wstring to string
        //2nd add to the tokens list
        tokens.push_back(wstring_convert<codecvt_utf8<wchar_t>>().to_bytes(temp));
    }

    return tokens;
}



ClientProperties getClientProperties(){
    ifstream read;
    ofstream write;
    ClientProperties prop;

    const string path = "./Client.cfg";

    vector<string> index;
    vector<string> hashmap;

    string line;

    read.open(path);

    if(read.fail()){
        write.open(path);
        write << "ServerIP=192.168.1.61" << endl;
        write << "ServerPort=50001" << endl;
        write.close();

        read.open(path);
    }


    while(getline(read, line)){
        index.push_back(line);
    }

    for(int i = 0 ; i<index.size() ; i++){
        hashmap = getTokens(index[i],L"=");
        
        if(hashmap[0].compare("ServerIP") == 0){
            prop.ip = hashmap[1];
            continue;
        }

        if(hashmap[0].compare("ServerPort") == 0){
            prop.port = stoi(hashmap[1]);
            continue;
        }
    }

    read.close();
    return prop;
}


ServerProperties getServerProperties(){
    ifstream read;
    ofstream write;
    ServerProperties prop;

    const string path = "./Server.cfg";

    vector<string> index;
    vector<string> hashmap;

    string line;

    read.open(path);

    if(read.fail()){
        write.open(path);
        write << "ServerPort=50001" << endl;
        write << "ServerMaxClient=5" << endl;
        write << "DB_IP=192.168.1.19" << endl;
        write << "DB_USER=Student" << endl;
        write << "DB_PASS=PassStudent1_" << endl;
        write << "DB_NAME=PourStudent" << endl;
        write.close();

        read.open(path);
    }


    while(getline(read, line)){
        index.push_back(line);
    }

    for(int i = 0 ; i<index.size() ; i++){
        hashmap = getTokens(index[i],L"=");
        
        if(hashmap[0].compare("ServerMaxClient") == 0){
            prop.nbrMaxClients = stoi(hashmap[1]);
            continue;
        }

        if(hashmap[0].compare("ServerPort") == 0){
            prop.port = stoi(hashmap[1]);
            continue;
        }

        if(hashmap[0].compare("DB_IP") == 0){
            prop.db_ip = hashmap[1];
            continue;
        }

        if(hashmap[0].compare("DB_USER") == 0){
            prop.db_user = hashmap[1];
            continue;
        }

        if(hashmap[0].compare("DB_PASS") == 0){
            prop.db_pass = hashmap[1];
            continue;
        }

        if(hashmap[0].compare("DB_NAME") == 0){
            prop.db_name = hashmap[1];
            continue;
        }
    }

    read.close();
    return prop;
}