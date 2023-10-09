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

#define IP      "localhost"
#define USER    "Student"
#define PASS    "PassStudent1_"
#define DB_NAME "PourStudent"


using namespace std;

class db
{
    private:
        MYSQL* connexion;

        vector<MYSQL_ROW> select(string requete);
        void insert(string requete);

    public:
        db();
        db(string ip, string user, string password, string database);
        ~db();


        void db::Login(string login, string passwd);
        void db::CreateLogin(string login, string passwd);
};

#endif