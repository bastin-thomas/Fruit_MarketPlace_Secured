#include "db.hpp"


db::db(string ip, string user, string password, string database)
{
    connexion = mysql_init(NULL);
    mysql_real_connect(connexion, IP, USER, PASS, DB_NAME, 0, 0, 0);
}

db::~db()
{
    mysql_close(connexion);
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
        string message = "DB Access Error: MySqlQuery: ";
        message += mysql_error(connexion);
        throw  message.c_str();
    }

    //catch request result
    if((result = mysql_store_result(connexion)) == NULL){
        string message = "DB Access Error: MySqlStoreResult: ";
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
        string message = "DB Access Error: MySqlQuery: ";
        message += mysql_error(connexion);
        throw  message.c_str();
    }
}

//Delete