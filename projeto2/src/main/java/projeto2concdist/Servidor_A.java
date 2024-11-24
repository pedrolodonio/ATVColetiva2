package projeto2concdist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor_A {

    private static final int PORTA_A = 8080; // Porta do Servidor A
    private static final String HOST_B = "localhost"; // Endereço do Servidor B
    private static final int PORTA_B = 8081; // Porta do Servidor B

    public void iniciarServidor() {
        try (ServerSocket serverSocket = new ServerSocket(PORTA_A)) {
            System.out.println("Servidor A iniciado na porta " + PORTA_A);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Conexão estabelecida com cliente: " + clientSocket.getInetAddress());

                    BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter respostaCliente = new PrintWriter(clientSocket.getOutputStream(), true);

                    // Recebe a consulta do cliente
                    String consulta = entradaCliente.readLine();
                    System.out.println("Consulta recebida do cliente: " + consulta);

                    // Encaminha a consulta para o Servidor B
                    String respostaServidorB = consultarServidorB(consulta);

                    // Retorna a resposta do Servidor B para o cliente
                    respostaCliente.println(respostaServidorB);

                } catch (IOException e) {
                    System.err.println("Erro ao processar cliente: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao iniciar o Servidor A: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String consultarServidorB(String consulta) {
        try (Socket socketB = new Socket(HOST_B, PORTA_B);
             PrintWriter saidaB = new PrintWriter(socketB.getOutputStream(), true);
             BufferedReader entradaB = new BufferedReader(new InputStreamReader(socketB.getInputStream()))) {

            // Envia a consulta para o Servidor B
            saidaB.println(consulta);

            // Aguarda e retorna a resposta do Servidor B
            return entradaB.readLine();

        } catch (IOException e) {
            System.err.println("Erro ao se comunicar com o Servidor B: " + e.getMessage());
            return "Erro ao buscar no Servidor B.";
        }
    }
}