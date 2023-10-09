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

//Do DataBase Login Job
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

//Do DataBase CreateLogin Job
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
        request << "INSERT INTO accounts VALUES (" << login << "," << passwd << ");";
        this->insert(request);
    }
    catch(const char *){
        stringstream newm;
        newm << SQLHEADER << m;
        throw newm.str().c_str();
    }
}

//Do DataBase Consult Job
articles db::Consult(int idArticle){
    vector<MYSQL_ROW> result;
    articles article;
    stringstream request;
    request << "SELECT intitule, prix, stock, image FROM articles WHERE id=" << idArticle << ";";

    try{
        result = this->select(request.str());
    }
    catch(const char * m){
        stringstream newm;
        newm << SQLHEADER << m;
        throw newm.str().c_str();
    }

    if(result.size() == 0){
        throw "NO_ARTICLE";
    }

    //from parameters
    article.idArticle = idArticle;

    //from db
    article.intitule = result[0][0];
    article.prix = result[0][1];
    article.stock = result[0][2];
    article.image = result[0][3];

    //return result
    return article;
}

//Do DataBase Achat Job
achats db::Achat(int idArticle, int quantitee){
    stringstream request;
    int newstock;
    articles article = this->Consult(idArticle);
    
    if((newstock = (article.stock - quantitee)) < 0){
        throw "STOCK_TOO_LOW";
    }
 
    request << "UPDATE SET stock = "<< newstock <<" WHERE id =" << idArticle << ";";

    try{
        this->update(request);
    }
    catch(const char * m){
        stringstream newm;
        newm << SQLHEADER << m;
        throw newm.str().c_str();
    }
}

//Do DataBase Cancel Job
vector<caddieRows> db::Cancel(int idArticle, vector<caddieRows> caddie){
    
}

//Do DataBase CancelAll Job
vector<caddieRows> db::CancelAll(vector<caddieRows> caddie){

}

//Do DataBase Confirmer Job
int db::Confirmer(string idClient, vector<caddieRows> caddie){
    vector<MYSQL_ROW> result;
    int montant = 0;
    
    for(caddieRows row : caddie){
        montant+=row.prix*row.quantitee;
    }

    try{
        stringstream request;
        request << "INSERT INTO factures (idClient, montant) VALUES (" << idClient << "," << montant << ") RETURNING id;";
        result = this->select(request);
    }
    catch(const char *){
        stringstream newm;
        newm << SQLHEADER << m;
        throw newm.str().c_str();
    }

    return stoi(result[0][0]);
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

void db::update(string requete){
    this->insert(requete);
}

//Delete