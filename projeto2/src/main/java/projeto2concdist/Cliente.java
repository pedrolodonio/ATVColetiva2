package projeto2concdist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    private static final String HOST_A = "localhost"; // Endereço do Servidor A
    private static final int PORTA_A = 8080; // Porta do Servidor A

    // Função principal que orquestra a consulta
    public void fazerConsulta(String consulta) {
        try (Socket socket = new Socket(HOST_A, PORTA_A);
             PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            enviarConsulta(saida, consulta);
            String respostaCompleta = lerResposta(entrada);

            exibirResposta(respostaCompleta);

        } catch (IOException e) {
            tratarErro(e);
        }
    }

    // Envia a consulta ao servidor
    private void enviarConsulta(PrintWriter saida, String consulta) {
        saida.println(consulta);
        System.out.println("Consulta enviada ao servidor: " + consulta);
    }

    // Lê a resposta completa do servidor
    private String lerResposta(BufferedReader entrada) throws IOException {
        StringBuilder respostaCompleta = new StringBuilder();
        String linha;
        while ((linha = entrada.readLine()) != null) {
            respostaCompleta.append(linha).append("\n");
        }
        return respostaCompleta.toString();
    }

    // Exibe a resposta recebida do servidor
    private void exibirResposta(String resposta) {
        if (resposta.trim().isEmpty()) {
            System.out.println("Nenhum resultado encontrado.");
        } else {
            System.out.println("Resposta completa do servidor ao cliente:\n" + resposta);
        }
    }

    // Trata qualquer erro na comunicação com o servidor
    private void tratarErro(IOException e) {
        System.err.println("Erro na comunicação com o Servidor A: " + e.getMessage());
        e.printStackTrace();
    }
}
