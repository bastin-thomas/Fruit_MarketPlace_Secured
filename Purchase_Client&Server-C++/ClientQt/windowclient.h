#ifndef WINDOWCLIENT_H
#define WINDOWCLIENT_H

#include <QMainWindow>
#include <vector>
#include <iostream>
#include <stdio.h>
#include <unistd.h>

#include "./Library/PROPERTIES/Properties.hpp"
#include "./Library/TCP/TCP.hpp"
#include "./Library/THREAD/mylibthread_POSIX.h"
#include "./Library/DATABASE/db.hpp"
#include "./Library/PROTOCOLE/Protocole.hpp"
#include "./Library/PROTOCOLE/Structure.hpp"




QT_BEGIN_NAMESPACE
namespace Ui { class WindowClient; }
QT_END_NAMESPACE

class WindowClient : public QMainWindow
{
    Q_OBJECT

public:
    WindowClient(QWidget *parent = nullptr);
    ~WindowClient();

    // Fonctions utiles (ne pas modifier)
    void setNom(const char *Text);
    const char *getNom();
    void setMotDePasse(const char *Text);
    const char *getMotDePasse();
    void setPublicite(const char *Text);
    void setImage(const char *image);
    int isNouveauClientChecked();
    int getQuantite();
    void loginOK();
    void logoutOK();
    void setArticle(const char *intitule, float prix, int stock, const char *image);
    void setTotal(float montant);

    void ajouteArticleTablePanier(const char *article, float prix, int quantite);
    void videTablePanier();
    void RefreshTablePanier();
    int getIndiceArticleSelectionne();

    // Clic sur la croix de la fenetre
    void closeEvent(QCloseEvent *event);

    // Boites de dialogue
    void dialogueMessage(const char *titre, const char *message);
    void dialogueErreur(const char *titre, const char *message);

private slots:
    void on_pushButtonLogin_clicked();
    void on_pushButtonLogout_clicked();
    void on_pushButtonPrecedent_clicked();
    void on_pushButtonSuivant_clicked();
    void on_pushButtonAcheter_clicked();
    void on_pushButtonSupprimer_clicked();
    void on_pushButtonViderPanier_clicked();
    void on_pushButtonPayer_clicked();

private:
    Ui::WindowClient *ui;

    char motDePasse[20];
    char nom[20];

    int indiceArticleAffiche;

    int port;
    string ip;
    
    ClientProperties properties;

    vector<caddieRows> Caddie;

    int Socket;
    int ServiceSocket[30];
};
#endif // WINDOWCLIENT_H