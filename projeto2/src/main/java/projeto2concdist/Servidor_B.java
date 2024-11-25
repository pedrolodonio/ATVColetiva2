package projeto2concdist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

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
    // Verifica se a consulta é válida
    if (consulta == null || consulta.trim().isEmpty()) {
        return "Consulta inválida ou vazia.";
    }

    StringBuilder resultados = new StringBuilder();
    // Obtem os nós "title" e "abstract"
    JsonNode titulos = dados.path("title");
    JsonNode resumos = dados.path("abstract");

    // Verifica se há dados disponíveis para busca
    if ((titulos.isMissingNode() || !titulos.fieldNames().hasNext()) &&
        (resumos.isMissingNode() || !resumos.fieldNames().hasNext())) {
        return "Nenhum título ou resumo disponível para busca.";
    }

    // Instancia a classe Busca para utilizar o algoritmo KMP
    Busca busca = new Busca();
    String consultaNormalizada = consulta.toLowerCase().trim();

    // Função auxiliar para realizar a busca em um nó específico
    Consumer<JsonNode> realizarBusca = (JsonNode nodo) -> {
        nodo.fieldNames().forEachRemaining(nomeChave -> {
            String textoOriginal = nodo.path(nomeChave).asText(); // Obtém o texto original
            String textoNormalizado = textoOriginal.toLowerCase().trim(); // Normaliza o texto

            // Usa o KMP para verificar se a consulta está presente no texto
            if (busca.kmpBusca(textoNormalizado, consultaNormalizada)) {
                // Adiciona o índice e o texto original ao resultado
                resultados.append(nomeChave).append(": ").append(textoOriginal).append("\n");
            }
        });
    };

    // Realiza a busca em "title"
    if (!titulos.isMissingNode()) {
        realizarBusca.accept(titulos);
    }
    // Realiza a busca em "abstract"
    if (!resumos.isMissingNode()) {
        realizarBusca.accept(resumos);
    }

    // Retorna os resultados encontrados ou uma mensagem padrão se nada for encontrado
    return resultados.length() > 0 ? resultados.toString().trim() : "Nenhum resultado encontrado.";
}

    
}