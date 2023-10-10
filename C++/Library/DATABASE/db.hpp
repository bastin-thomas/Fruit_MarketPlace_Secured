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

        vector<MYSQL_ROW> select(string requete);
        void insert(string requete);
        void update(string requete);

    public:
        db();
        db(string ip, string user, string password, string database);
        ~db();


        void Login(string login, string passwd);
        void CreateLogin(string login, string passwd);

        articles Consult(int idArticle);

        void Achat(int idArticle, int quantitee);

        //void db::Caddie();

        void Cancel(int idArticle);

        void CancelAll();

        void Confirmer();

        //void db::Logout();

};

#endif