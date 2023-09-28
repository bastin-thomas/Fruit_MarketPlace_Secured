.SILENT:

*.o = $(bin)TCP.o
bin = ./.obj/
LibTCP = ./TCP/
Extension = .app

ALL: $(*.o) Serveur Client 

Serveur$(Extension): ./Serveur.cpp $(*.o)
				echo Serveur
				@mkdir -p $(bin)
				g++ ./Serveur.cpp $(*.o) -o Serveur$(Extension) -std=gnu++2a -lpthread

Client$(Extension): ./Client.cpp $(*.o)
				echo Client
				@mkdir -p $(bin)
				g++ ./Client.cpp $(*.o) -o Client$(Extension) -std=gnu++2a -lpthread

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