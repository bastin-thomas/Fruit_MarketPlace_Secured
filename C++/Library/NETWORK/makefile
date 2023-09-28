.SILENT:

*.o = $(bin)TCP.o
bin = ./.obj/
LibTCP = ./TCP/

ALL: $(*.o) test.app test1.app 

test.app: ./test.cpp $(*.o)
				echo test.app
				@mkdir -p $(bin)
				g++ ./test.cpp $(*.o) -o test.app -std=gnu++2a -lpthread

test1.app: ./test1.cpp $(*.o)
				echo test1.app
				@mkdir -p $(bin)
				g++ ./test1.cpp $(*.o) -o test1.app -std=gnu++2a -lpthread

$(bin)TCP.o:	$(LibTCP)TCP.cpp $(LibTCP)TCP.hpp
						echo TCP.o
						@mkdir -p $(bin)
						g++ $(LibTCP)TCP.cpp -c -o $(bin)TCP.o -std=gnu++2a -lpthread


#Clean/Clear des .o et de l'application
clean:	
		rm $(*.o)
		rm test.app
		rm test1.app
		clear

clear:	
		rm $(*.o)
		rm test.app
		rm test1.app
		clear