.SILENT:

*.o = mylibthread_POSIX.o
bin = ./.obj/

ALL: $(*.o)

mylibthread_POSIX.o:	mylibthread_POSIX.cpp mylibthread_POSIX.h
						echo mylibthread_POSIX.o
						g++	mylibthread_POSIX.cpp -c -o mylibthread_POSIX.o


#Clean/Clear des .o et de l'application
clean:	
		rm $(*.o)
		clear

clear:	
		rm $(*.o)
		clear