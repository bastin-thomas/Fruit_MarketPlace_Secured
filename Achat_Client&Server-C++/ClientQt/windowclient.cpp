#include "windowclient.h"
#include "ui_windowclient.h"
#include <QMessageBox>
#include <string>
#include <iostream>
#include <fstream>

using namespace std;

extern WindowClient *w;

#define REPERTOIRE_IMAGES "images/"



WindowClient::WindowClient(QWidget *parent) : QMainWindow(parent), ui(new Ui::WindowClient)
{
    ui->setupUi(this);

    // Configuration de la table du panier (ne pas modifer)
    ui->tableWidgetPanier->setColumnCount(3);
    ui->tableWidgetPanier->setRowCount(0);
    QStringList labelsTablePanier;
    labelsTablePanier << "Article" << "Prix à l'unité" << "Quantité";
    ui->tableWidgetPanier->setHorizontalHeaderLabels(labelsTablePanier);
    ui->tableWidgetPanier->setSelectionMode(QAbstractItemView::SingleSelection);
    ui->tableWidgetPanier->setSelectionBehavior(QAbstractItemView::SelectRows);
    ui->tableWidgetPanier->horizontalHeader()->setVisible(true);
    ui->tableWidgetPanier->horizontalHeader()->setDefaultSectionSize(160);
    ui->tableWidgetPanier->horizontalHeader()->setStretchLastSection(true);
    ui->tableWidgetPanier->verticalHeader()->setVisible(false);
    ui->tableWidgetPanier->horizontalHeader()->setStyleSheet("background-color: lightyellow");

    ui->pushButtonPayer->setText("Confirmer achat");
    setPublicite("--------------------------------------------------------------------");

    //LoadConfigProperties:
    this->properties = getClientProperties();    
}

WindowClient::~WindowClient()
{
    delete ui;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles : ne pas modifier /////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setNom(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditNom->clear();
    return;
  }
  ui->lineEditNom->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
const char* WindowClient::getNom()
{
  strcpy(nom,ui->lineEditNom->text().toStdString().c_str());
  return nom;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setMotDePasse(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditMotDePasse->clear();
    return;
  }
  ui->lineEditMotDePasse->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
const char* WindowClient::getMotDePasse()
{
  strcpy(motDePasse,ui->lineEditMotDePasse->text().toStdString().c_str());
  return motDePasse;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setPublicite(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditPublicite->clear();
    return;
  }
  ui->lineEditPublicite->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setImage(const char* image)
{
  // Met à jour l'image
  char cheminComplet[80];
  sprintf(cheminComplet,"%s%s",REPERTOIRE_IMAGES,image);
  QLabel* label = new QLabel();
  label->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
  label->setScaledContents(true);
  QPixmap *pixmap_img = new QPixmap(cheminComplet);
  label->setPixmap(*pixmap_img);
  label->resize(label->pixmap()->size());
  ui->scrollArea->setWidget(label);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::isNouveauClientChecked()
{
  if (ui->checkBoxNouveauClient->isChecked()) return 1;
  return 0;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setArticle(const char* intitule,float prix,int stock,const char* image)
{
  ui->lineEditArticle->setText(intitule);
  if (prix >= 0.0)
  {
    char Prix[20];
    sprintf(Prix,"%.2f",prix);
    ui->lineEditPrixUnitaire->setText(Prix);
  }
  else ui->lineEditPrixUnitaire->clear();
  if (stock >= 0)
  {
    char Stock[20];
    sprintf(Stock,"%d",stock);
    ui->lineEditStock->setText(Stock);
  }
  else ui->lineEditStock->clear();
  setImage(image);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::getQuantite()
{
  return ui->spinBoxQuantite->value();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setTotal(float total)
{
  if (total >= 0.0)
  {
    char Total[20];
    sprintf(Total,"%.2f",total);
    ui->lineEditTotal->setText(Total);
  }
  else ui->lineEditTotal->clear();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::loginOK()
{
  ui->pushButtonLogin->setEnabled(false);
  ui->pushButtonLogout->setEnabled(true);
  ui->lineEditNom->setReadOnly(true);
  ui->lineEditMotDePasse->setReadOnly(true);
  ui->checkBoxNouveauClient->setEnabled(false);

  ui->spinBoxQuantite->setEnabled(true);
  ui->pushButtonPrecedent->setEnabled(true);
  ui->pushButtonSuivant->setEnabled(true);
  ui->pushButtonAcheter->setEnabled(true);
  ui->pushButtonSupprimer->setEnabled(true);
  ui->pushButtonViderPanier->setEnabled(true);
  ui->pushButtonPayer->setEnabled(true);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::logoutOK()
{
  ui->pushButtonLogin->setEnabled(true);
  ui->pushButtonLogout->setEnabled(false);
  ui->lineEditNom->setReadOnly(false);
  ui->lineEditMotDePasse->setReadOnly(false);
  ui->checkBoxNouveauClient->setEnabled(true);

  ui->spinBoxQuantite->setEnabled(false);
  ui->pushButtonPrecedent->setEnabled(false);
  ui->pushButtonSuivant->setEnabled(false);
  ui->pushButtonAcheter->setEnabled(false);
  ui->pushButtonSupprimer->setEnabled(false);
  ui->pushButtonViderPanier->setEnabled(false);
  ui->pushButtonPayer->setEnabled(false);

  setNom("");
  setMotDePasse("");
  ui->checkBoxNouveauClient->setCheckState(Qt::CheckState::Unchecked);

  setArticle("",-1.0,-1,"");

  w->videTablePanier();
  w->setTotal(-1.0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles Table du panier (ne pas modifier) /////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::ajouteArticleTablePanier(const char* article,float prix,int quantite)
{
    char Prix[20],Quantite[20];

    sprintf(Prix,"%.2f",prix);
    sprintf(Quantite,"%d",quantite);

    // Ajout possible
    int nbLignes = ui->tableWidgetPanier->rowCount();
    nbLignes++;
    ui->tableWidgetPanier->setRowCount(nbLignes);
    ui->tableWidgetPanier->setRowHeight(nbLignes-1,10);

    QTableWidgetItem *item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(article);
    ui->tableWidgetPanier->setItem(nbLignes-1,0,item);

    item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(Prix);
    ui->tableWidgetPanier->setItem(nbLignes-1,1,item);

    item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(Quantite);
    ui->tableWidgetPanier->setItem(nbLignes-1,2,item);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::videTablePanier()
{
    ui->tableWidgetPanier->setRowCount(0);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::getIndiceArticleSelectionne()
{
    QModelIndexList liste = ui->tableWidgetPanier->selectionModel()->selectedRows();
    if (liste.size() == 0) return -1;
    QModelIndex index = liste.at(0);
    int indice = index.row();
    return indice;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions permettant d'afficher des boites de dialogue (ne pas modifier ////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::dialogueMessage(const char* titre,const char* message)
{
   QMessageBox::information(this,titre,message);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::dialogueErreur(const char* titre,const char* message)
{
   QMessageBox::critical(this,titre,message);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////// CLIC SUR LA CROIX DE LA FENETRE /////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::closeEvent(QCloseEvent *event)
{
  on_pushButtonLogout_clicked();

  exit(0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions clics sur les boutons ////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonLogin_clicked()
{
  this->indiceArticleAffiche = 1;

  string nom = getNom();
  string mdp = getMotDePasse();
  articles article;

  cout << "log : " << nom << " " << mdp << endl;

  //Init TCP Connexion
  try{
    this->Socket = ClientSocket(properties.ip, properties.port);
  }
  catch(const char* message){
    cout << "ClientSocket: " << message << endl;
    dialogueErreur("Login", "Impossible de communiquer avec le Serveur");
    return;
  }
  
  try{
    if(isNouveauClientChecked() == true){
      SendCreateLogin(this->Socket,nom, mdp);
    }else{
      SendLogin(this->Socket,nom, mdp);
    }
  }catch(const char * m){
    dialogueErreur("Login", m);

    return;
  }

  
  try{
    article = SendConsult(this->Socket, this->indiceArticleAffiche);
  }catch(const char * m){
    this->indiceArticleAffiche++;
    return;
  }

  setArticle(article.intitule.c_str(), article.prix, article.stock, article.image.c_str());

  loginOK();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonLogout_clicked()
{
  if(ui->pushButtonLogout->isEnabled()){
    try{
      SendCancelAll(this->Socket);
      SendLogout(this->Socket);
    }catch(const char * m){
      if(strcmp(m, "La connexion avec le serveur a été coupée") == 0){
        dialogueErreur("Connexion Server", "La connexion avec le serveur a été coupée");
        logoutOK();
        return;
      }
    }
  }
  
  

  logoutOK();
  ::close(this->Socket);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonSuivant_clicked()
{
  articles article;

  this->indiceArticleAffiche++;

  try{
    article = SendConsult(this->Socket, this->indiceArticleAffiche);

  }catch(const char * m){
    if(strcmp(m, "La connexion avec le serveur a été coupée") == 0){
        dialogueErreur("Connexion Server", "La connexion avec le serveur a été coupée");
        logoutOK();
        return;
      }

    this->indiceArticleAffiche--;
    return;
  }

  setArticle(article.intitule.c_str(), article.prix, article.stock, article.image.c_str());
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonPrecedent_clicked()
{
  articles article;

  this->indiceArticleAffiche--;

  try{
    article = SendConsult(this->Socket, this->indiceArticleAffiche);

  }catch(const char * m){
      if(strcmp(m, "La connexion avec le serveur a été coupée") == 0){
        dialogueErreur("Connexion Server", "La connexion avec le serveur a été coupée");
        logoutOK();
        return;
      }

    this->indiceArticleAffiche++;
    return;
  }

  setArticle(article.intitule.c_str(), article.prix, article.stock, article.image.c_str());
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonAcheter_clicked()
{
  cerr << "indice: " << this->indiceArticleAffiche << endl;

  if(getQuantite()<=0){
    return;
  }

  try{
    SendAchat(this->Socket, this->indiceArticleAffiche, getQuantite());
  }catch(const char * m){
    if(strcmp(m, "La connexion avec le serveur a été coupée") == 0){
        dialogueErreur("Connexion Server", "La connexion avec le serveur a été coupée");
        logoutOK();
        return;
      }

    dialogueErreur("Button Acheter", m);
    return;
  }


  
  articles article;
  try{
    article = SendConsult(this->Socket, this->indiceArticleAffiche);
  }catch(const char * m){
    if(strcmp(m, "La connexion avec le serveur a été coupée") == 0){
        dialogueErreur("Connexion Server", "La connexion avec le serveur a été coupée");
        logoutOK();
        return;
      }

    return;
  }
  setArticle(article.intitule.c_str(), article.prix, article.stock, article.image.c_str());

  RefreshTablePanier();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonSupprimer_clicked()
{
  
  int id = this->Caddie.at(getIndiceArticleSelectionne()).idArticle;

  try{
    SendCancel(this->Socket, id);
  }catch(const char * m){
    if(strcmp(m, "La connexion avec le serveur a été coupée") == 0){
        dialogueErreur("Connexion Server", "La connexion avec le serveur a été coupée");
        logoutOK();
        return;
      }

    dialogueErreur("Button Supprimer", m);
    return;
  }

  RefreshTablePanier();

  articles article;
  try{
    article = SendConsult(this->Socket, this->indiceArticleAffiche);
  }catch(const char * m){
    if(strcmp(m, "La connexion avec le serveur a été coupée") == 0){
        dialogueErreur("Connexion Server", "La connexion avec le serveur a été coupée");
        logoutOK();
        return;
      }

    return;
  }
  setArticle(article.intitule.c_str(), article.prix, article.stock, article.image.c_str());
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonViderPanier_clicked()
{
  try{
    SendCancelAll(this->Socket);
  }catch(const char * m){
    dialogueErreur("Button Vider panier", m);

    return;
  }

  RefreshTablePanier();

  setTotal(0.0);

  articles article;
  try{
    article = SendConsult(this->Socket, this->indiceArticleAffiche);
  }catch(const char * m){
    if(strcmp(m, "La connexion avec le serveur a été coupée") == 0){
        dialogueErreur("Connexion Server", "La connexion avec le serveur a été coupée");
        logoutOK();
        return;
      }

    return;
  }
  setArticle(article.intitule.c_str(), article.prix, article.stock, article.image.c_str());
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonPayer_clicked()
{
  try{
    SendConfirmer(this->Socket, getNom());
  }catch(const char * m){
    if(strcmp(m, "La connexion avec le serveur a été coupée") == 0){
        dialogueErreur("Connexion Server", "La connexion avec le serveur a été coupée");
        logoutOK();
        return;
      }

    dialogueErreur("Button Payer", m);
    return;
  }

  dialogueMessage("Commande Réussie","Félicitation, votre commande a bien été enregistrée. Cependant il vous faut encore la payée.");

  RefreshTablePanier();
  setTotal(-1.0);
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::RefreshTablePanier()
{
  Caddie.empty();
  videTablePanier();

  try{
    Caddie = SendCaddie(this->Socket);
  }catch(const char * m){
    if(strcmp(m, "La connexion avec le serveur a été coupée") == 0){
        dialogueErreur("Connexion Server", "La connexion avec le serveur a été coupée");
        logoutOK();
        return;
      }


    dialogueErreur("Refresh panier", m);
    return;
  }

  float prix = 0;
  for(caddieRows row : Caddie){
    ajouteArticleTablePanier(row.intitule.c_str() , row.prix, row.quantitee);
    prix += row.prix*row.quantitee;
  }
  setTotal(prix);
}