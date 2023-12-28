package com.mymaraichermobile.model;

import android.content.Context;

import com.mymaraichermobile.configuration.ConfigManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketClient {
    //region Private Variables
    private final Socket socket;
    private final DataOutputStream dos;
    private final DataInputStream dis;

    //endregion

    //region Constructeurs
    public SocketClient(Context context) throws NumberFormatException, IOException {
        socket = new Socket(ConfigManager.getIp(context), Integer.parseInt(ConfigManager.getPort(context)));

        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

    }

    //endregion

    //region Methods

    public boolean isAlive(){
        if(socket.isClosed() || !socket.isConnected()){
            return false;
        }
        else{
            return true;
        }
    }

    public String receive() throws IOException{
        StringBuilder buffer = new StringBuilder();


        boolean eot = false;
        while(!eot) // Read byte by byte and if reach end of requestion stop the loop
        {
            byte b1 = dis.readByte();

            if (b1 == (byte) '&')
            {
                byte b2 = dis.readByte();
                if (b2 == (byte) ')') {
                    eot = true;
                }
                else{
                    buffer.append((char)b1);
                    buffer.append((char)b2);
                }
            }
            else{
                buffer.append((char)b1);
            }
        }

        return buffer.toString();
    }

    public void send(String msg) throws IOException{
        String trame = msg + '&' + ')';

        dos.write(trame.getBytes(StandardCharsets.ISO_8859_1));
        dos.flush();
    }

    public void close() throws IOException
    {
        this.socket.close();
    }

    //endregion

}
