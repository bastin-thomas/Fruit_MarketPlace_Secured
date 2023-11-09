#ifndef STRUCTURE_H
#define STRUCTURE_H

#include <string>
#include <vector>
#include <string.h>

using namespace std;

struct Login{
	string nom;
	string mdp;
};

struct articles{
	int idArticle;
	string intitule;
	float prix;
	int stock;
	string image;
};

struct achats{
	int idArticle;
	int quantitee;
	float prix;
};

struct caddieRows{
	int idArticle;
	string intitule;
	int quantitee;
	float prix;
};

#endif