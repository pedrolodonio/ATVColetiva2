package projeto2concdist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor_B {

    private static final int PORTA_B = 8081; // Porta do Servidor B

    public void iniciarServidor() {
        try (ServerSocket socketServidor = new ServerSocket(PORTA_B)) {
            System.out.println("Servidor B iniciado na porta " + PORTA_B);

            while (true) {
                try (Socket socket = socketServidor.accept()) {
                    System.out.println("Conexão estabelecida com " + socket.getInetAddress());

                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

                    // Recebe a consulta
                    String busca = entrada.readLine();
                    System.out.println("Consulta recebida: " + busca);

                    // Realiza a busca nos dados locais
                    String resultado = buscaLocal(busca);

                    // Retorna o resultado ao Servidor A
                    saida.println(resultado);

                } catch (IOException e) {
                    System.err.println("Erro ao processar conexão: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao iniciar o Servidor B: " + e.getMessage());
            e.printStackTrace();
        }
    }
