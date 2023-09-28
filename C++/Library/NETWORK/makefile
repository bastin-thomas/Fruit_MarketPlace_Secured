.SILENT:

*.o = $(bin)TCP.o
bin = ./.obj/
LibTCP = ./TCP/

ALL: $(*.o) Serveur Client 

Serveur: ./Serveur.cpp $(*.o)
				echo Serveur
				@mkdir -p $(bin)
				g++ ./Serveur.cpp $(*.o) -o Serveur -std=gnu++2a -lpthread

Client: ./Client.cpp $(*.o)
				echo Client
				@mkdir -p $(bin)
				g++ ./Client.cpp $(*.o) -o Client -std=gnu++2a -lpthread

$(bin)TCP.o:	$(LibTCP)TCP.cpp $(LibTCP)TCP.hpp
						echo TCP.o
						@mkdir -p $(bin)
						g++ $(LibTCP)TCP.cpp -c -o $(bin)TCP.o -std=gnu++2a -lpthread


#Clean/Clear des .o et de l'application
clean:	
		rm $(*.o)
		rm Serveur
		rm Client
		clear

clear:	
		rm $(*.o)
		rm Serveur
		rm Client
		clear