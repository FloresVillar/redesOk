package redesOk;

import java.util.Scanner;
import redesOk.TCPClient50;

class Cliente50{
    TCPClient50 mTcpClient; //para gestionar la conexion  con el servidor
    Scanner sc;
    public static void main(String[] args)  {
        Cliente50 objcli = new Cliente50();
        objcli.iniciar();
    }
    void iniciar(){
       new Thread(      //se inicia un nuevo hilo para manejar la conexion TCP , evitando bloquear el hilo principal
            new Runnable() {

                @Override
                public void run() { 
                    mTcpClient = new TCPClient50("127.0.0.1",   //creando una instancia de TCPCient50 ,pasar la ip y una implementaciion de la interfaz OnMessageReceived
                        new TCPClient50.alRecibirMensaje(){
                            @Override
                            public void mensajeRecibido(String message){
                                ClienteRecibe(message);
                            }
                        }
                    );
                    mTcpClient.run();                   
                }
            }
        ).start();
        //---------------------------
       
        String entrada = "n";
        sc = new Scanner(System.in);
        System.out.println("Cliente: esperando entrada de consola , 's' para salir");
        while( !entrada.equals("s")){
            entrada = sc.nextLine();
            ClienteEnvia(entrada);
        }
        System.out.println("Cliente:saliendo");
    
    }
    void ClienteRecibe(String llego){
        System.out.println("CLINTE50 El mensaje::" + llego);

    }
    void ClienteEnvia(String envia){
        if (mTcpClient != null) {
            mTcpClient.enviarMensaje(envia);
        }
    }

}
