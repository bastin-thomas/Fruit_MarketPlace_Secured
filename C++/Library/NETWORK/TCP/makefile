.SILENT:

*.o = $(bin)ServiceSocket.o $(bin)ListenSocket.o $(bin)ClientSocket.o
bin = ./.obj/
Libls = ./ListenSocket/
Liblss = ./ServiceSocket/
Liblc = ./client_socket/
LibNt = ./

ALL: $(*.o)

$(bin)client_socket.o:	$(Liblc)client_socket.cpp $(Liblc)client_socket.hpp
						echo client_socket.o
						g++ $(Liblc)client_socket.cpp -c -o $(bin)client_socket.o

$(bin)ServiceSocket.o:	$(Liblss)servicesocket.cpp $(Liblss)servicesocket.hpp
						echo ServiceSocket.o
						g++ $(Liblss)servicesocket.cpp -c -o $(bin)ServiceSocket.o

$(bin)ListenSocket.o:	$(Libls)listensocket.cpp $(Libls)listensocket.hpp $(Liblss)servicesocket.hpp
						echo ListenSocket.o
						g++	$(Libls)listensocket.cpp -c -o $(bin)ListenSocket.o


#Clean/Clear des .o et de l'application
clean:	
		rm $(*.o)
		clear

clear:	
		rm $(*.o)
		clear