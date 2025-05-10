package redesOk;
//Lanza un TCPServer en un hilo separado, mientras tanto el hilo principal lee lines
//desde consola (entrada de usuario) y las envia al cliente conectado, tambien define un metodo de 
//callback que se ejecuta cuando el servidor recibe un mensaje desde el cliente
import java.util.Scanner;

public class Servidor50 {
   TCPServer50 tcpserver50;
   Scanner sc;
   public static void main(String[] args) {
       Servidor50 objser = new Servidor50(); //creando una instancia y llamando a iniciar
       objser.iniciar();
   }
   void iniciar(){
       new Thread(   //lanzand un hilo para manejar la conexion TCP
            new Runnable() {

                @Override
                public void run() {
                      tcpserver50 = new TCPServer50(
                        new TCPServer50.alrecibirMensaje(){  //sobreescribe /* public interface AlrecibirMensaje*/
                            @Override
                            public void mensajeRecibido(String message){
                                ServidorRecibe(message);
                            }
                        }
                    );
                    tcpserver50.run();                   
                }
            }
        ).start();
        //-----------------
        String salir = "n";   //el hilo principal espera comandos 
        sc = new Scanner(System.in);
        System.out.println("Servidor:corriendo recibir de consola, 's' para salir");
        while( !salir.equals("s")){
            salir = sc.nextLine();
            ServidorEnvia(salir);
       }
       System.out.println("Servidor:se escribio s, saliendo"); 
   
   }
   void ServidorRecibe(String llego){
       System.out.println("SERVIDOR El mensaje:" + llego);
   }
   void ServidorEnvia(String envia){
        if (tcpserver50 != null) {
            tcpserver50.enviarMensajeTCPServer(envia);
        }
   }
}
/*
 * 
 * 
 * 
 * 
 * 
 * 
 */