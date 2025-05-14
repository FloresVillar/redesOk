package redesOk;

import java.io.BufferedReader; //para leer datos del socket in
import java.io.PrintWriter; //para escribir datos al servidor out
import java.net.ServerSocket; //para manejar conexiones entrantes
import java.net.Socket; //para conexiones de cliente

public class TCPServer50 {
    private String message;//ultimo mensaje recibido
    int ncli = 0; //numero de clientes conectados 
    public static final int SERVERPORT = 5000; //puerto del servidor
    private alrecibirMensaje listener = null; //interfaz para manejar mensajes recibidos
    private boolean running = false; //controla si el servidor sigue activo
    TCPServerThread50[] clienteshilos = new TCPServerThread50[10]; //arreglo de  hilos cliente
    PrintWriter mOut; //para enviar datos out
    BufferedReader in; //para recibir datos in
    ServerSocket serverSocket; //el socket que escucha conexiones
    //el constructor pide una interface OnMessageReceived
    //constructor recibe el listener que maneja los mensajes recibidos
    public TCPServer50(alrecibirMensaje Listener) {
        this.listener = Listener; //guarda el listener                                                                     
    }
    public alrecibirMensaje obtenerListener(){
        return this.listener; //devuelve el listener
    }
    //envia un mensajes a todos los clientes conectados
    public void enviarMensajeTCPaClientes(String message){
        for (int i = 1; i <= ncli; i++) { //recorre todos los clientes conectados
            clienteshilos[i].enviarMensaje(message);  //envia mensaje al cliente
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
                clienteshilos[ncli] = new TCPServerThread50(client,this,ncli,clienteshilos);
                Thread t = new Thread(clienteshilos[ncli]);//envolvemos el hilo
                t.start();//iniciando el hilo cliente
                System.out.println("Nuevo conectado:"+ ncli+" jugadores conectados");
            }
            
        }catch( Exception e){
            System.out.println("Error, se finalizo TCPServer50"+e.getMessage());//mostrando error si ocurre 
        }finally{

        }
    }//devuelve todos los hilos clientes activos
    public  TCPServerThread50[] obtenerClientes(){
        return clienteshilos;
    } 
    //interfaz usada para manejar los mensajes que llegan al sevidor
    public interface alrecibirMensaje {
        public void mensajeRecibido(String message);
    }
}
