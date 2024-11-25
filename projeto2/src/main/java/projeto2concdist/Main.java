package projeto2concdist;

import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;

public class Main {
    public static void main(String[] args) {
        // Caminhos para os arquivos JSON A e B
        String caminhoArquivoA = "projeto2/arquivos.json/data_A.json"; // Substituir pelo path do data A
        String caminhoArquivoB = "projeto2/arquivos.json/data_B.json"; // Substituir pelo path do data B

        // Carrega os dados divididos
        JsonNode[] nodes = carregarDados(caminhoArquivoA, caminhoArquivoB);
        if (nodes == null) {
            System.out.println("Erro ao carregar os arquivos JSON.");
            return;  // Encerra a execução se houver erro no carregamento dos arquivos
        }

        // Inicializa e inicia os servidores
        iniciarServidores(nodes[0], nodes[1]);

        // Inicia o Cliente
        iniciarCliente();
    }

    // Função para carregar os dados dos arquivos JSON
    private static JsonNode[] carregarDados(String caminhoArquivoA, String caminhoArquivoB) {
        return CarregaDados.iniciarLeitura(caminhoArquivoA, caminhoArquivoB);
    }

    // Função para inicializar e iniciar os servidores
    private static void iniciarServidores(JsonNode rootNodeA, JsonNode rootNodeB) {
        // Criação das instâncias para os servidores
        Servidor_A servidorA = new Servidor_A(rootNodeA); // Passa dados para o Servidor A
        Servidor_B servidorB = new Servidor_B(rootNodeB); // Passa dados para o Servidor B

        // Criação das threads para os servidores
        Thread threadSB = new Thread(() -> servidorB.iniciarServidor()); // Chama iniciarServidor do Servidor B
        Thread threadSA = new Thread(() -> servidorA.iniciarServidor()); // Chama iniciarServidor do Servidor A

        // Inicia os servidores
        threadSB.start();
        aguardarServidorAtivo(2000); // Garante que o Servidor B esteja ativo antes do Servidor A

        threadSA.start();
        aguardarServidorAtivo(2000); // Garante que o Servidor A esteja ativo antes de iniciar o cliente
    }

    // Função para aguardar que o servidor esteja ativo por um tempo determinado
    private static void aguardarServidorAtivo(long tempoEmMilissegundos) {
        try {
            Thread.sleep(tempoEmMilissegundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Função para iniciar o Cliente e receber consultas
    private static void iniciarCliente() {
        Cliente cliente = new Cliente();
        System.out.println("Digite suas consultas (ou 'sair' para encerrar):");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String consulta = scanner.nextLine();

                // Envia a consulta ao servidor
                cliente.fazerConsulta(consulta);
                
                if (consulta.equalsIgnoreCase("sair")) {
                    System.out.println("Encerrando o cliente...");
                    break;  // Encerra o loop e o cliente
                }
            }
        }
    }
}