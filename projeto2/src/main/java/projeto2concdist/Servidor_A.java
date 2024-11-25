package projeto2concdist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;

public class Servidor_A {

    private static final int PORTA_A = 8080; // Porta do Servidor A
    private static final String HOST_B = "localhost"; // Endereço do Servidor B
    private static final int PORTA_B = 8081; // Porta do Servidor B
    private JsonNode dados; // Dados que o Servidor A irá usar para buscar

    // Construtor que recebe o JsonNode contendo os dados
    public Servidor_A(JsonNode dados) {
        this.dados = dados;
    }

    public void iniciarServidor() {
        try (ServerSocket serverSocket = new ServerSocket(PORTA_A)) {
            System.out.println("Servidor A iniciado na porta " + PORTA_A);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {

                    BufferedReader entradaCliente = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter respostaCliente = new PrintWriter(clientSocket.getOutputStream(), true);

                    // Recebe a consulta do cliente
                    String consulta = entradaCliente.readLine();

                    // Se a consulta for "sair", fecha o servidor
                    if (consulta.equalsIgnoreCase("sair")) {
                        System.out.println("Comando 'sair' recebido. Encerrando o Servidor A.");
                        
                        // Envia a consulta 'sair' para o Servidor B antes de encerrar
                        consultarServidorB(consulta);
                        
                        break; // Sai do loop e encerra o servidor
                    }                    

                    // Realiza a busca nos dados de A
                    String respostaServidorA = realizarBusca(consulta);

                    // Sempre consulta o Servidor B, independentemente de encontrar ou não no A
                    String respostaServidorB = consultarServidorB(consulta);

                    // Combina os resultados de A e B
                    String resultadoFinal = combinarResultados(respostaServidorA, respostaServidorB);

                    // Envia a resposta final para o cliente
                    respostaCliente.println(resultadoFinal);

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

// Método para realizar a busca nos dados de A
private String realizarBusca(String consulta) {
    Busca busca = new Busca();  // Instancia a classe de busca KMP
    StringBuilder resultado = new StringBuilder();

    // Verifica se a consulta é válida
    if (consulta == null || consulta.trim().isEmpty()) {
        return "Consulta inválida ou vazia.";
    }

    // Função auxiliar para realizar a busca em uma chave específica
    Consumer<String> buscarEmChave = (String chave) -> {
        JsonNode node = dados.path(chave); // Obtém o nó correspondente
        if (!node.isMissingNode() && node.fieldNames().hasNext()) {
            node.fieldNames().forEachRemaining(nomeChave -> {
                String textoOriginal = node.path(nomeChave).asText(); // Mantém o texto original
                String textoNormalizado = textoOriginal.toLowerCase().trim(); // Normaliza o texto para a busca

                // Verifica se a consulta está presente no texto
                if (busca.kmpBusca(textoNormalizado, consulta.toLowerCase().trim())) {
                    // Registra a localização do texto encontrado
                    resultado.append(chave).append(" - ").append(nomeChave).append(": ").append(textoOriginal).append("\n");
                }
            });
        }
    };

    // Realiza a busca em "title" e "abstract"
    buscarEmChave.accept("title");
    buscarEmChave.accept("abstract");

    // Retorna os resultados encontrados ou uma string vazia
    return resultado.length() == 0 ? "" : resultado.toString();
}



    // Método para consultar o Servidor B
    private String consultarServidorB(String consulta) {
        try (Socket socketB = new Socket(HOST_B, PORTA_B);
             PrintWriter saidaB = new PrintWriter(socketB.getOutputStream(), true);
             BufferedReader entradaB = new BufferedReader(new InputStreamReader(socketB.getInputStream()))) {

            // Envia a consulta para o Servidor B
            saidaB.println(consulta);

            // Aguarda e retorna a resposta do Servidor B
            StringBuilder respostaB = new StringBuilder();
            String linha;
            while ((linha = entradaB.readLine()) != null) {
                respostaB.append(linha).append("\n");
            }
            return respostaB.toString();

        } catch (IOException e) {
            System.err.println("Erro ao se comunicar com o Servidor B: " + e.getMessage());
            return "Erro ao buscar no Servidor B.";
        }
    }

    // Combina os resultados de A e B
private String combinarResultados(String respostaA, String respostaB) {
    StringBuilder resultadoFinal = new StringBuilder();

    // Se resposta A estiver vazia, não adicione nada
    if (respostaA != null && !respostaA.isEmpty()) {
        resultadoFinal.append(respostaA);
    } 

    // Sempre adiciona a resposta de B, caso tenha
    if (respostaB != null && !respostaB.isEmpty()) {
        resultadoFinal.append(respostaB);
    } 

    return resultadoFinal.toString();

}

}