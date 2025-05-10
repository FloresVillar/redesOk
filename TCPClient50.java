package redesOk;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient50 {

    private String servermsj;
    public  String SERVERIP; 
    public static final int SERVERPORT = 5000;
    private alRecibirMensaje mMessageListener = null;  //interfaz para avisar si recibe un mensaje
    private boolean mRun = false;

    PrintWriter out;   //para enviar mensajes al servidor
    BufferedReader in;  //para leer los mensajes desde el servidor

    public TCPClient50(String ip,alRecibirMensaje listener) {
        SERVERIP = ip;
        mMessageListener = listener;  //iniciar la ip y el escuchador
    }
    public void enviarMensaje(String message){
        if (out != null && !out.checkError()) { //si out canal de salida esta listo
            out.println(message);   //se envia el mensaje
            out.flush();
        }
    }
    public void stopClient(){
        mRun = false;   //hace que el bucle de repeticion termine
    }
    public void run() { //logica principal del cliente 
        mRun = true;
        try {
            InetAddress serverAddr = InetAddress.getByName(SERVERIP); //obteniendo ip
            System.out.println("TCP Client : Conectando con "+serverAddr); //
            Socket socket = new Socket(serverAddr, SERVERPORT); //conectandose al servidor
            try { //canal de salida
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                System.out.println("TCP Client"+ "out(enviar datos al servidor) canal de salida creado");
                System.out.println("TCP Client"+ "in(leer datos del servidor) canal de entrada creado ");
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (mRun) { //mientras este activo
                    servermsj = in.readLine(); //si hay mensaje y escuchador(listener)
                    if (servermsj != null && mMessageListener != null) {
                        mMessageListener.mensajeRecibido(servermsj);//lama al metodo para procesarlo 
                    }
                    servermsj = null;
                }
            } catch (Exception e) {
                System.out.println("TCPclient"+ "S: Error"+e);

            } finally {
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("TCPclient"+ "C: Error"+ e);
        }
    }
    public interface alRecibirMensaje { //interfaz para manejar mensajes, modificad por Cliente50
        public void mensajeRecibido(String message);
    }
}