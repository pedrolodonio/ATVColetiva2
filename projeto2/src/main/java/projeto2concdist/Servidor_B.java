package projeto2concdist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.fasterxml.jackson.databind.JsonNode;


public class Servidor_B {

    private static final int PORTA_B = 8081; // Porta do Servidor B
    private JsonNode dados; // Variável para armazenar os dados

    // Construtor que recebe o JsonNode
    public Servidor_B(JsonNode dados) {
        this.dados = dados;
    }

    public void iniciarServidor() {
        try (ServerSocket socketServidor = new ServerSocket(PORTA_B)) {
            System.out.println("Servidor B iniciado na porta " + PORTA_B);

            while (true) {
                try (Socket socket = socketServidor.accept()) {

                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

                    // Recebe a consulta
                    String consulta = entrada.readLine();
                    System.out.println("Consulta recebida: " + consulta);

                    // Se a consulta for "sair", fecha o servidor
                    if (consulta.equalsIgnoreCase("sair")) {
                        System.out.println("Comando 'sair' recebido. Encerrando o Servidor B.");
                        break;  // Sai do loop e encerra o servidor
                    }

                    // Realiza a busca nos dados locais
                    String resultado = buscaLocal(consulta);

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

    private String buscaLocal(String consulta) {
        StringBuilder resultados = new StringBuilder();
        JsonNode titulos = dados.path("title");
    
        // Normaliza a consulta (remove espaços extras)
        String consultaNormalizada = consulta.toLowerCase().trim();
    
        // Itera sobre as chaves numéricas do objeto
        titulos.fieldNames().forEachRemaining(nomeChave -> {
            // A chave é o índice numérico como string, e o valor é o título
            String tituloOriginal = titulos.path(nomeChave).asText(); // Mantém o título original
            String tituloNormalizado = tituloOriginal.toLowerCase().trim(); // Normaliza o título
    
            // Verifica se o título contém o padrão de busca
            if (tituloNormalizado.contains(consultaNormalizada)) {
                // Adiciona o índice e o título original
                resultados.append(nomeChave + ": " + tituloOriginal).append("\n");
            }
        });
    
        // Retorna os resultados encontrados ou uma mensagem padrão se nada for encontrado
        return resultados.length() > 0 ? resultados.toString() : "";
    }
}