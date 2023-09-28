# Variables
OBJS = Client.o Serveur.o $(Lib_Classes)/Reseaux.o

PROGRAM = Client Serveur

Lib_Classes = ./Library

# PROGRAM= 

COMP = cc -m64 -D SUN -D CPP -I -lsnl -I -lsocket -lpthread
# 
# Programmes
.SILENT:

all: $(PROGRAM)


# Objets

Serveur.app:	Serveur.c $(Lib_Classes)/Reseaux.o
			echo Creation de Serveur
			$(COMP) -o Serveur Serveur.c $(Lib_Classes)/Reseaux.o 
			echo Creation de Serveur réussie

Client.app:		Client.c $(Lib_Classes)/Reseaux.o
			echo Creation de Client
			$(COMP) -o Client Client.c $(Lib_Classes)/Reseaux.o 
			echo Creation de Client réussie

# Librairie
Reseaux.o:	$(Lib_Classes)/Reseaux.c $(Lib_Classes)/Reseaux.h
			echo Creation de Reseaux
			$(COMP) $(Lib_Classes)/Reseaux.c -c
			echo Creation de Reseaux réussie

# Utilitaires

clean:
	echo Nettoyage des objets et programmes...
	rm -f $(OBJS)
	rm -f $(PROGRAM)
clobber:
	echo Nettoyage des programmes...
	rm -f $(PROGRAM)
cleanObjs:
	echo Nettoyage des objets...
	rm -f $(OBJS)