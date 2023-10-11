.SILENT:

bin = ./.obj/

Lib = ./Library/

LibTCP = $(Lib)TCP/
LibProperties = $(Lib)PROPERTIES/
LibThread = $(Lib)THREAD/
LibProtocole = $(Lib)PROTOCOLE/
ClientQt = ./ClientQt/
LibDB= $(Lib)DATABASE/

QtLibParam = -Wno-unused-parameter -pipe -g -Wall -W -D_REENTRANT -fPIC -DQT_DEPRECATED_WARNINGS -DQT_QML_DEBUG -DQT_WIDGETS_LIB -DQT_GUI_LIB -DQT_CORE_LIB -I../UNIX_DOSSIER_FINAL -I. -isystem /usr/include/qt5 -isystem /usr/include/qt5/QtWidgets -isystem /usr/include/qt5/QtGui -isystem /usr/include/qt5/QtCore -I. -I. -I/usr/lib64/qt5/mkspecs/linux-g++
QtClientParam = /usr/lib64/libQt5Widgets.so /usr/lib64/libQt5Gui.so /usr/lib64/libQt5Core.so /usr/lib64/libGL.so -lpthread
MySQLParam = -I/usr/include/mysql -m64 -L/usr/lib64/mysql -lmysqlclient -lpthread -lz -lm -lrt -lssl -lcrypto -ldl

Extension = .app


*.o = $(bin)TCP.o $(bin)Properties.o $(bin)mylibthreadPOSIX.o $(bin)Protocole.o $(bin)db.o

ALL: Client$(Extension) Serveur$(Extension)

$(bin)mainclient.o:	$(ClientQt)mainclient.cpp
					echo mainclient.o
					@mkdir -p $(bin)
					g++ $(QtLibParam) $(ClientQt)mainclient.cpp -c -o $(bin)mainclient.o -std=gnu++2a -lpthread

$(bin)windowclient.o:	$(ClientQt)windowclient.cpp $(bin)Protocole.o $(ClientQt)windowclient.h $(ClientQt)ui_windowclient.h
						echo windowclient.o
						@mkdir -p $(bin)
						g++ $(QtLibParam) $(ClientQt)windowclient.cpp -c -o $(bin)windowclient.o -std=gnu++2a -lpthread 

$(bin)moc_windowclient.o:	$(ClientQt)moc_windowclient.cpp  $(ClientQt)windowclient.h
							echo moc_windowclient.o
							@mkdir -p $(bin)
							g++ $(QtLibParam) $(ClientQt)moc_windowclient.cpp -c -o $(bin)moc_windowclient.o -std=gnu++2a -lpthread 


Client$(Extension): $(*.o) $(bin)mainclient.o $(bin)windowclient.o $(bin)moc_windowclient.o 
					echo Client$(Extension)
					@mkdir -p $(bin)
					g++ $(QtClientParam) $(MySQLParam) $(*.o) $(bin)mainclient.o $(bin)windowclient.o $(bin)moc_windowclient.o -o Client$(Extension) -std=gnu++2a -lpthread 

Serveur$(Extension): ./Serveur.cpp $(LibTCP)TCP.hpp $(bin)db.o $(LibDB)db.hpp $(bin)Properties.o $(bin)Protocole.o $(LibProperties)Properties.hpp $(LibProtocole)Protocole.hpp $(*.o) $(LibThread)mylibthread_POSIX.h
				echo Serveur.app
				@mkdir -p $(bin)
				g++ ./Serveur.cpp $(MySQLParam) $(*.o) -o Serveur$(Extension) -std=gnu++2a -lpthread



$(bin)TCP.o:	$(LibTCP)TCP.cpp  $(LibTCP)TCP.hpp
				echo TCP.o
				@mkdir -p $(bin)
				g++ $(LibTCP)TCP.cpp -c -o $(bin)TCP.o -std=gnu++2a -lpthread

$(bin)Properties.o:		$(LibProperties)Properties.cpp  $(LibProperties)Properties.hpp
						echo Properties.o
						@mkdir -p $(bin)
						g++ $(LibProperties)Properties.cpp -c -o $(bin)Properties.o -std=gnu++2a -lpthread

$(bin)db.o: $(LibDB)db.cpp $(LibDB)db.hpp $(LibProtocole)Structure.hpp
			echo db.o
			@mkdir -p $(bin)
			g++ $(MySQLParam) $(LibDB)db.cpp -c -o $(bin)db.o -std=gnu++2a 

$(bin)mylibthreadPOSIX.o: $(LibThread)mylibthread_POSIX.cpp $(LibThread)mylibthread_POSIX.h
						 echo mylibthreadPOSIX.o
						 @mkdir -p $(bin)
						 g++ $(LibThread)mylibthread_POSIX.cpp -c -o $(bin)mylibthreadPOSIX.o -std=gnu++2a

$(bin)Protocole.o:		$(LibProtocole)Protocole.cpp  $(LibProtocole)Protocole.hpp $(LibProtocole)Structure.hpp $(LibDB)db.hpp
						echo Protocole.o
						@mkdir -p $(bin)
						g++ $(MySQLParam) $(LibProtocole)Protocole.cpp -c -o $(bin)Protocole.o -std=gnu++2a -lpthread





rm.o = $(bin)TCP.o $(bin)Properties.o $(bin)mylibthreadPOSIX.o $(bin)Protocole.o $(bin)db.o $(bin)moc_windowclient.o $(bin)windowclient.o $(bin)mainclient.o

#Clean/Clear des .o et de l'application
clean:	
		rm -f $(rm.o)
		rm -f Serveur$(Extension)
		rm -f Client$(Extension)
		clear

clear:	clean