package com.mymaraichermobile.client;

import android.content.Context;

import com.mymaraichermobile.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClient {
    //region Private Variables
    private final Socket socket;
    private final DataOutputStream dos;
    private final DataInputStream dis;

    //endregion

    //region Constructeurs
    public SocketClient(Context context) throws NumberFormatException, IOException {
        String ip = context.getResources().getString(R.string.ip_serveur);
        int port = Integer.parseInt(context.getResources().getString(R.string.port_serveur));

        socket = new Socket(ip, port);

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

            if (b1 == (byte) R.string.EndOfRequest1)
            {
                byte b2 = dis.readByte();
                if (b2 == (byte) R.string.EndOfRequest2){
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
        String trame = msg + (R.string.EndOfRequest1) + (R.string.EndOfRequest1);
        dos.write(trame.getBytes());
        dos.flush();
    }
    public void close() throws IOException
    {
        this.socket.close();
    }

    //endregion

}
