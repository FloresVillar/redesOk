package redesOk;
import java.io.BufferedReader;  //leer datos del socket in LEER
import java.io.BufferedWriter; //para escribir texto con buffer out ENVIAR
import java.io.InputStreamReader; //convierte flujo de bytes a caracteres
import java.io.OutputStreamWriter; //convierte caracteres a bytes
import java.io.PrintWriter; //para escribir texto en el socket de forma habil ?

import java.net.Socket; //clase para manejar conexion con un cliente


public class TCPServerThread50 extends Thread{ //clase que maneja un cliente , extiende de Thread
    
    private Socket client; //socket del cliente conectado 
    private TCPServer50 tcpserver; //referencia al servidor principal 
    private int clientID;                 
    private boolean corriendo = false; //bandera para controlar si el hilo sigue activa
    public PrintWriter out; //permite enviar mensajes al cliente
    public BufferedReader in; //permite recibir mensajes del cliente
    private TCPServer50.alrecibirMensaje Escuchador = null;//listener para mensajes recibidos
    private String mensaje; //almacena mensaje actual
    TCPServerThread50[] cli_amigos;//arreglo de referencia a otros clientes

    public TCPServerThread50(Socket client_, TCPServer50 tcpserver_, int clientID_,TCPServerThread50[] cli_ami_) {
        this.client = client_; //guarda socket del cliente
        this.tcpserver = tcpserver_; //guarda referencia al servidor principal 
        this.clientID = clientID_; //guardando ID cliente
        this.cli_amigos = cli_ami_; //guarda referencia a los demas  clientes
    }
    
     public void trabajen(int cli){ //metodo que envia un mensaje de trabajo a un cliente     
         out.println("ORDENANDO A ["+cli+"] que trabaje"); //envia mensaje al cliente correspondiente
    }
    
    public void run() { //metodo que se ejecuta cuando inicia el hilo
        corriendo = true; //activa la bandera de ejecucion
        try {
            try {               
                boolean soycontador = false; //variable aulixiar sin uso aqui
                //para escribir datos en el cliente , out
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                System.out.println("TCP Server TCPServerThread "+ "canal de salida out creado para escribir en client");
                //obtiene el listener del servidor principal
                Escuchador = tcpserver.obtenerListener();
                //crea objeto para leer del socket del cliente
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("TCP Server TCPServerThread "+ "canal de entrada in creado para leer de client");
                while (corriendo) {  //bucle principal de recepcion
                    mensaje = in.readLine(); //lee mensaje de cliente
                    //si hay mensaje y hay listener , lo notificamos
                    if (mensaje != null && Escuchador != null) {
                        Escuchador.mensajeRecibido(mensaje); //se delega manejo de mensaje
                        System.out.println("TCP Server TCPServer50Thread mensaje "+ mensaje);
                    }
                    

                    mensaje = null; //reiniciando el mensaje
                }
                
            } catch (Exception e) {
                System.out.println("TCP Server TCPServer50Thread try interno "+ "Error"+ e);
            } finally {
                client.close();
            }

        } catch (Exception e) {
            System.out.println("TCP Server TCPServer50Thread try exter "+ "C: Error"+ e);
        }
    }
    
    public void stopClient(){ //para detener la ejecucion del hilo
        corriendo = false;
    }
    
    public void enviarMensaje(String message){//funcion de trabajo, enviando mensaje al cliente
        if (out != null && !out.checkError()) { //canal para enviar escribir en cliente
            out.println( message); //escribiendo el mensaje
            out.flush(); //forzando el envio inmediato
        } // Servidor50 → usa ServidorEnvia(){tcp50server.enviarMensajeTCPServer }→TCPserver50 enviarMensajeTCPSever(){envioclienteshilo[i].enviarMensaje(){out}}  es el mismo mensaje a todos los clientes
    }                                                                                                                                                      //out esta en run(), para escribir o enviar mensajes a cliente                                   
    
}
/*clase que maneja un cliente
 * import java.io.BufferedReader;
 * import java.io.BufferedWriter;
 * import java.io.PrintWriter;
 * import java.net.socket;
 * import java.io.InputStreamReader;
 * import java.io.OutputStreamWriter;
 * public class TCPServerThread50 {
 *      TCPServer50 tcpserver;;
 *      Socket cliente;
 *      ID_cliente;
 *      PrintWriter out = null;
 *      BufferredReader in = null;
 *      TCPServer50.alRecibirMensaje mensajeListener 
 *      corriendo = false;
 *      TCPServerThread [] hilos_clientes;
 *      constructor(TCPSserver server,Socket cliente,amigos_clientes,id_Cliente){
 *              server  = server; cliente = cliente; amigos_clientes,id_cliente=id_cliente;
 *          }
 *      run(){
 *              try{
 *                  out= new PrintWriter(new BufferedWriter(new OutputStreamWriter(cliente.getOutputStream())))
 *                  in = new BufferedReader(new InputStreamReader(cliente.getInputStream()))
 *                  mensajeListener = server.obtenerMensajeListener()
 *                  while(corriendo){
 *                      mensaje  =in.readLine()
 *                      if(mensaje and mensajeListener){
 *                           mensajeListener.mensajeRecibido(mensaje);   
 *                      }
 *                  }
 *              }catch(InterruptedException e){e.printStackTrace()}
 *          }
 *      }
 *      enviarMensaje(mensaje){
 *          if(out and !out.checkError){
 *              out.prinln(mensaje);
 *              out.fflush();
 *          }
 *      }
 *  }
 * 
 * 
 * 
 * 
 * import java.io.BufferedReader;
 * import jva.io.BufferedWriter;
 * import java.io.InputStreamReader;
 * import java.io.OutputStreamWriter;
 * import java.io.PrintWriter;
 * import java.net.Socket;
 * puclic class TCPServerThread {
 *      public BufferedReader in;
 *      public PrintWriter out;
 *      Socket cliente;
 *      boolean corriendo = false;
 *      TCPServer tcpserver;
 *      TCPServerThread[] amigo_clientes;
 *      public TCPServer.alRecibirMensaje escuchador= null;
 *      TCPServerThread(Socket cliente,TCPServer tcpserver,int id,TCPServerThread amigo_clientes){
 *          cliente = cliente;
 *          tcpserver = tcpserver;
 *          amigo_clientes = amigo_clientes;
 *          id = id
 *         }
 *      void run(){
 *         try{
 *              out =new PrintWriter( new BufferedWriter(new OutputStreamWriter(cliente.getStreamWriter())))
 *              in = new BufferedReader(new InputStreamReader(cliente.getStreamReader()))
 *              escuchador = server.obtenerMensajeListener()
 *              while (!corriendo){
 *                  mensaje = in.readLine()    
 *                  if(mensaje && escuchador){
 *                          cliente.escucharMensaje(mensaje);
 *                              
 *                  }
 *              }
 *  
 *          }
 *      }
 *      void enviarMensaje(mensaje){
 *          if(out.check()){
 *              out.println()
 *              out.fflush()
 *          }
 *       }
 * } 
 * 
 * 
 * 
























 * 
 */