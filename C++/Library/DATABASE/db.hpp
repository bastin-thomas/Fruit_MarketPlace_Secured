#ifndef DB_HPP
#define DB_CPP

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <iostream>
#include <string>
#include <sstream>

#include <mysql/mysql.h>
#include <vector>

#include "../PROTOCOLE/Protocole.hpp"

#define IP      "192.168.1.19"
#define USER    "Student"
#define PASS    "PassStudent1_"
#define DB_NAME "PourStudent"


#define SQLHEADER "SQLERROR_"


using namespace std;

class db
{
    private:
        MYSQL* connexion;

        vector<vector<string>> select(string requete);
        void insert(string requete);
        void update(string requete);

    public:
        db();
        db(string ip, string user, string password, string database);
        ~db();


        bool Login(string login, string passwd);
        bool CreateLogin(string login, string passwd);

        articles Consult(int idArticle);

        achats Achat(int idArticle, int quantitee);

        vector<caddieRows> Cancel(int idArticle, vector<caddieRows> caddie);

        vector<caddieRows> CancelAll(vector<caddieRows> caddie);

        int Confirmer(string idClient, vector<caddieRows> caddie);
};

#endif