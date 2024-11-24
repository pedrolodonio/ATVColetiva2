package projeto2concdist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor_B {

    private static final int PORTA_B = 8081;

    public void iniciarServidor() {
        try (ServerSocket socketServidor = new ServerSocket(PORTA_B)) {
            System.out.println("Servidor B iniciado na porta " + PORTA_B);

            while (true) {
                try (Socket socket = socketServidor.accept()) {
                    System.out.println("Conexão estabelecida com " + socket.getInetAddress());

                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

                    
                    String busca = entrada.readLine();
                    System.out.println("Consulta recebida: " + busca);

                    
                    String resultado = buscaLocal(busca);

                   
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
    private String buscaLocal(String busca) {
        String[] baseDados = {
            "Artigo sobre computação quântica",
            "Introdução a redes neurais convolucionais",
            "Estudo sobre aprendizado de máquina supervisionado"
        };

        StringBuilder resultados = new StringBuilder();
        for (String artigo : baseDados) {
            if (artigo.toLowerCase().contains(busca.toLowerCase())) {
                resultados.append(artigo).append(" | ");
            }
        }

        return resultados.length() > 0 ? resultados.toString() : "Nenhum resultado encontrado no Servidor B";
    }