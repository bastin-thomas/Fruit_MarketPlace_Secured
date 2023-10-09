#include "db.hpp"

#define SQLHEADER "SQLERROR_"

db::db(string ip, string user, string password, string database)
{
    connexion = mysql_init(NULL);
    mysql_real_connect(connexion, IP, USER, PASS, DB_NAME, 0, 0, 0);
}

db::~db()
{
    mysql_close(connexion);
}


void db::Login(string login, string passwd){
    vector<MYSQL_ROW> result;
    stringstream request;
    request << "SELECT password FROM accounts WHERE login=" << login << ";";

    try{
        result = this->select(request.str());
    }
    catch(const char * m){
        stringstream newm;
        newm << SQLHEADER << m;
        throw newm.str().c_str();
    }


    if(result.size() == 0){
        throw "NO_LOGIN";
    }

    string dbpasswd = result[0][0];

    if(dbpasswd != passwd){
        throw "BAD_LOGIN";
    }
}


void db::CreateLogin(string login, string passwd){
    vector<MYSQL_ROW> result;
    
    try{
        stringstream request;
        request << "SELECT password FROM accounts WHERE login=" << login << ";";

        result = this->select(request.str());
    }
    catch(const char * m){
        stringstream newm;
        newm << SQLHEADER << m;
        throw newm.str().c_str();
    }


    if(result.size() != 0){
        throw "LOGIN_EXIST";
    }

    try{
        stringstream request;
        "insert into articles values (NULL,'%s',%f,%d,'%s');"
        request << "INSERT INTO accounts VALUES (" << login << "," << passwd << ");";
        this->insert();
    }
    catch(const char *){
        stringstream newm;
        newm << SQLHEADER << m;
        throw newm.str().c_str();
    }
}


/*************************************\
* Select row                          *
* Use to do select query              *
*                                     *
* return a vector of sql row or null  *
\*************************************/
vector<MYSQL_ROW> db::select(string requete){
    MYSQL_RES* result;
    MYSQL_ROW row;

    vector<MYSQL_ROW> rows;

    //Select query execution
    if(mysql_query(connexion, requete.c_str()) != 0){
        string message = "MySqlQuery: ";
        message += mysql_error(connexion);
        throw  message.c_str();
    }

    //catch request result
    if((result = mysql_store_result(connexion)) == NULL){
        string message = "MySqlStoreResult: ";
        message += mysql_error(connexion);
        throw  message.c_str();
    }

    //Put result in a vector of row
    int i = 0;
    while((row = mysql_fetch_row(result)) != NULL){
        rows.push_back(row);
        i++;
    }

    return rows;
}


/*************************************\
* Insert row                          *
* Use to do Insert query              *
*                                     *
* return true or false                *
\*************************************/
void db::insert(string requete){
    if(mysql_query(connexion,requete.c_str()) == -1){
        string message = "MySqlQuery: ";
        message += mysql_error(connexion);
        throw  message.c_str();
    }
}

//Delete