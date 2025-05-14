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
    private alRecibirMensaje Escuchador = null;  //interfaz para avisar si recibe un mensaje
    private boolean corriendo = false;

    PrintWriter out;   //para enviar mensajes al servidor
    BufferedReader in;  //para leer los mensajes desde el servidor

    public TCPClient50(String ip,alRecibirMensaje listener) {
        SERVERIP = ip;
        Escuchador = listener;  //iniciar la ip y el escuchador
    }
    
    public void run() { //logica principal del cliente 
        corriendo = true;
        try {
            InetAddress serverDireccion = InetAddress.getByName(SERVERIP); //obteniendo ip
            System.out.println("TCP Client : Conectando con "+serverDireccion); //
            Socket servidor = new Socket(serverDireccion, SERVERPORT); //conectandose al servidor
            try { //canal de salida
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(servidor.getOutputStream())), true);
                System.out.println("TCP Client"+ "out(enviar datos al servidor) canal de salida creado");
                System.out.println("TCP Client"+ "in(leer datos del servidor) canal de entrada creado ");
                in = new BufferedReader(new InputStreamReader(servidor.getInputStream()));
                while (corriendo) { //mientras este activo
                    servermsj = in.readLine(); //si hay mensaje y escuchador(listener)
                    if (servermsj != null && Escuchador != null) {
                        Escuchador.mensajeRecibido(servermsj);//lama al metodo para procesarlo 
                    }
                    servermsj = null;
                }
            } catch (Exception e) {
                System.out.println("TCPclient"+ "S: Error"+e);

            } finally {
                servidor.close();
            }
        } catch (Exception e) {
            System.out.println("TCPclient "+ "no se hizo el try externo "+ e);
        }
    }

    public void enviarMensaje(String message){
        if (out != null && !out.checkError()) { //si out canal de salida esta listo
            out.println(message);   //se envia el mensaje
            out.flush();
        }
    }

    public interface alRecibirMensaje { //interfaz para manejar mensajes, modificad por Cliente50
        public void mensajeRecibido(String message);
    }

    public void stopClient(){
        corriendo = false;   //hace que el bucle de repeticion termine
    }

}