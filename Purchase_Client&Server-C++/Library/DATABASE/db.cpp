#include "db.hpp"


db::db()
{
    connexion = mysql_init(NULL);
    connexion = mysql_real_connect(connexion, IP, USER, PASS, DB_NAME, 0, 0, 0);
}

db::db(string ip, string user, string password, string database)
{
    connexion = mysql_init(NULL);
    connexion = mysql_real_connect(connexion, ip.c_str(), user.c_str(), password.c_str(), database.c_str(), 0, 0, 0);
}

db::~db()
{
    mysql_close(connexion);
}

/// @brief Do DataBase Login Job
/// @param login user login
/// @param passwd user password
/// @return true if good, throw constchar* if error
bool db::Login(string login, string passwd){
    vector<vector<string>> result;
    stringstream request;

    request << "SELECT password FROM accounts WHERE login=\"" << login << "\";";
    try{
        result = this->select(request.str());
    }
    catch(const char * m){
        cerr << SQLHEADER << m;
        throw "SQLERROR";
    }


    if(result.size() == 0){
        throw "NO_LOGIN";
    }

    string dbpasswd = result[0][0];

    if(dbpasswd != passwd){
        throw "BAD_LOGIN";
    }

    return true;
}

//Do DataBase CreateLogin Job
bool db::CreateLogin(string login, string passwd){

    vector<vector<string>> result;
    try{
        stringstream request;
        request << "SELECT password FROM accounts WHERE login=\"" << login << "\";";

        result = this->select(request.str());
    }
    catch(const char * m){
        stringstream newm;
        cerr << SQLHEADER << m << endl;
        throw "SQLERROR";
    }


    if(result.size() != 0){
        throw "LOGIN_EXIST";
    }

    try{
        stringstream request;
        request << "INSERT INTO accounts VALUES (\"" << login << "\",\"" << passwd << "\");";
        this->insert(request.str());
    }
    catch(const char * m){
        stringstream newm;
        cerr << SQLHEADER << m << endl;
        throw "SQLERROR";
    }

    return true;
}

//Do DataBase Consult Job
articles db::Consult(int idArticle){

    vector<vector<string>> result;
    articles article;
    stringstream request;
    request << "SELECT intitule, prix, stock, image FROM articles WHERE id=\"" << idArticle << "\";";

    try{
        result = this->select(request.str());
    }
    catch(const char * m){
        stringstream newm;
        cerr << SQLHEADER << m << endl;
        throw "SQLERROR";
    }

    if(result.size() == 0){
        throw "NO_ARTICLE";
    }

    //from parameters
    article.idArticle = idArticle;

    //from db
    article.intitule = result[0][0];
    article.prix = stof(result[0][1]);
    article.stock = stoi(result[0][2]);
    article.image = result[0][3];

    //return result
    return article;
}

//Do DataBase Achat Job
achats db::Achat(int idArticle, int quantitee){

    int newstock;
    stringstream request;
    achats achat;

    articles article;
    try{
        article = this->Consult(idArticle);
    }
    catch(const char * m){
        //If doesnot exist: return -1
        cerr << m << endl;
        achat.idArticle = idArticle;
        achat.prix = article.prix;
        achat.quantitee = -1;

        return achat;
    }
    
    
    if((newstock = (article.stock - quantitee)) < 0){
        //If no stock return 0;
        achat.quantitee = 0;
    }
    else{
        try{
            //If stock and exist, remove stock from db and return achat; (be added to the caddie)
            request << "UPDATE articles SET stock = \""<< newstock <<"\" WHERE id =\"" << idArticle << "\";";
            this->update(request.str());

            achat.quantitee = quantitee;
        }
        catch(const char * m){
            stringstream newm;
            cerr << SQLHEADER << m << endl;
            throw "SQLERROR";
        }
    }
    
    achat.idArticle = idArticle;
    achat.prix = article.prix;
    
    return achat;
}

//Do DataBase Cancel Job
void db::Cancel(int idArticle, vector<caddieRows>* caddie){
    int newstock;
    stringstream request;
    caddieRows deletedrow;

    articles article = this->Consult(idArticle);
    
    for(int i = 0; i<caddie->size(); i++){
        if(caddie->at(i).idArticle == idArticle){
            //Remove cadie from the vector
            deletedrow = caddie->at(i);
            caddie->erase(caddie->begin() + i);
            break;
        }
    }

    newstock = article.stock + deletedrow.quantitee;

    try{
        request << "UPDATE articles SET stock = \""<< newstock <<"\" WHERE id =\"" << idArticle << "\";";
        this->update(request.str());
    }
    catch(const char * m){
        cerr << m << endl;
        throw "CANT_CANCEL";
    }
}

//Do DataBase CancelAll Job
void db::CancelAll(vector<caddieRows> * caddie){
    for(caddieRows row : *caddie){
        this->Cancel(row.idArticle, caddie);
    } 
}

//Do DataBase Confirmer Job
int db::Confirmer(string idClient, vector<caddieRows> * caddie){
    vector<vector<string>> result;
    int montant = 0;
    
    //Loop on the caddie to calculate the bill
    for(caddieRows row : *caddie){
        montant+=row.prix*row.quantitee;
    }


    //Insert a new "factures" in the DB
    try{
        stringstream request;
        request << "INSERT INTO factures (idClient, montant) VALUES (\"" << idClient << "\",\"" << montant << "\");";
        this->insert(request.str());
    }
    catch(const char * m){
        stringstream newm;
        cerr << SQLHEADER << m << endl;
        throw "SQLERROR";
    }

    //Get the ID from the last row insterted (factures)
    try{
        stringstream request;
        request << "SELECT LAST_INSERT_ID();";
        result = this->select(request.str());
    }
    catch(const char * m){
        stringstream newm;
        cerr << SQLHEADER << m << endl;
        throw "SQLERROR";
    }

    //put the factures id in a int
    int Factid = stoi(result[0][0]);



    //Loop on all row of the caddie to send them to the "ventes" table
    for(caddieRows row : *caddie){
        try{
            stringstream request;
            request << "INSERT INTO ventes (idFacture, idArticle, quantité) VALUES (\"" << Factid << "\",\"" << row.idArticle << "\",\""<< row.quantitee <<"\");";
            this->insert(request.str());
        }catch(const char * m){
            stringstream newm;
            cerr << SQLHEADER << m << endl;
            throw "SQLERROR";
        }
    }
    
    //Once everything done, empty the caddie (server side)
    caddie->clear();
    
    return Factid;
}



/*************************************\
* Select row                          *
* Use to do select query              *
*                                     *
* return a vector of sql row or null  *
\*************************************/
vector<vector<string>> db::select(string requete){
    MYSQL_RES* result;
    MYSQL_ROW row;
    
    vector<vector<string>> rows;

    //Select query execution
    if(mysql_query(connexion, requete.c_str()) != 0){
        string message = "MySqlQuery: ";
        message += mysql_error(connexion);
        cerr << message << endl;
        throw  "MYSQL ERROR";
    }

    //catch request result
    if((result = mysql_store_result(connexion)) == NULL){
        string message = "MySqlStoreResult: ";
        message += mysql_error(connexion);
        cerr << message << endl;
        throw  "MYSQL ERROR";
    }

    //Put result in a vector of row
    unsigned int num_fields;
    unsigned int i;

    num_fields = mysql_num_fields(result);

    while((row = mysql_fetch_row(result)) != NULL){
        vector<string> newrow;
        unsigned long *lengths;

        lengths = mysql_fetch_lengths(result);
        for(int i =0 ; i<num_fields ; i++){
            newrow.push_back(row[i]);
        }
        
        rows.push_back(newrow);
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
        cerr << message << endl;
        throw  "MYSQL ERROR";
    }
}

void db::update(string requete){
    this->insert(requete);
}

//Delete