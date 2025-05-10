package redesOk;

import java.io.BufferedReader; //para leer datos del socket in
import java.io.PrintWriter; //para escribir datos al servidor out
import java.net.ServerSocket; //para manejar conexiones entrantes
import java.net.Socket; //para conexiones de cliente

public class TCPServer50 {
    private String message;//ultimo mensaje recibido
    int ncli = 0; //numero de clientes conectados 
    public static final int SERVERPORT = 5000; //puerto del servidor
    private alrecibirMensaje messageListener = null; //interfaz para manejar mensajes recibidos
    private boolean running = false; //controla si el servidor sigue activo
    TCPServerThread50[] envioclienteshilos = new TCPServerThread50[10]; //arreglo de  hilos cliente
    PrintWriter mOut; //para enviar datos out
    BufferedReader in; //para recibir datos in
    ServerSocket serverSocket; //el socket que escucha conexiones
    //el constructor pide una interface OnMessageReceived
    //constructor recibe el listener que maneja los mensajes recibidos
    public TCPServer50(alrecibirMensaje messageListener) {
        this.messageListener = messageListener; //guarda el listener                                                                     
    }
    public alrecibirMensaje obtenerMensajeListener(){
        return this.messageListener; //devuelve el listener
    }
    //envia un mensajes a todos los clientes conectados
    public void enviarMensajeTCPServer(String message){
        for (int i = 1; i <= ncli; i++) { //recorre todos los clientes conectados
            envioclienteshilos[i].enviarMensaje(message);  //envia mensaje al cliente
            System.out.println("ENVIANDO A JUGADOR " + (i)); //mostrando mensaje de envio
        }
    }
    //metodo principal que inicia el SERVIDOR
    public void run(){
        running = true; //marcando servidor como activo
        try{
            System.out.println("TCP Server"+"Connecting...");
            serverSocket = new ServerSocket(SERVERPORT); //abre el puerto
            
            while(running){ //BUCLE de aceptacion de clientes
                Socket client = serverSocket.accept();  //espera conexion entrante
                System.out.println("TCP Server"+"Receiving clientes..");
                ncli++;
                System.out.println("Engendrado " + ncli); //crea nuevo hilo cliente
                envioclienteshilos[ncli] = new TCPServerThread50(client,this,ncli,envioclienteshilos);
                Thread t = new Thread(envioclienteshilos[ncli]);//envolvemos el hilo
                t.start();//iniciando el hilo cliente
                System.out.println("Nuevo conectado:"+ ncli+" jugadores conectados");
            }
            
        }catch( Exception e){
            System.out.println("Error, se finalizo TCPServer50"+e.getMessage());//mostrando error si ocurre 
        }finally{

        }
    }//devuelve todos los hilos clientes activos
    public  TCPServerThread50[] obtenerClientes(){
        return envioclienteshilos;
    } 
    //interfaz usada para manejar los mensajes que llegan al sevidor
    public interface alrecibirMensaje {
        public void mensajeRecibido(String message);
    }
}
