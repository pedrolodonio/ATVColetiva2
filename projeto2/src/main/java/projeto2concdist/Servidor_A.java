package projeto2concdist;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



public class Servidor_A {

    public void iniciarservidor(){

        int port = 8080;


        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Servidor A iniciado na porta "+ port);

            while (true) { 
                try (Socket clientSocket = serverSocket.accept()){
                    System.out.println("conexão estabelecida com " + clientSocket.getInetAddress());

                    PrintWriter resposta = new PrintWriter (clientSocket.getOutputStream(),true);
                    resposta.println(" Teste de conexão bem sucedido");
                } catch(IOException e){
                    e.printStackTrace();
                }
                
            }
        }catch (IOException e){
            e.printStackTrace();
        
    }
}
}