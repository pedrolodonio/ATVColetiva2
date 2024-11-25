package projeto2concdist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class CarregaDados {

    // Método para carregar e verificar os arquivos JSON A e B
    public static JsonNode[] iniciarLeitura(String caminhoArquivoA, String caminhoArquivoB) {
        // Verifica e carrega o arquivo A
        JsonNode rootNodeA = carregarArquivoJSON(new File(caminhoArquivoA));
        // Verifica e carrega o arquivo B
        JsonNode rootNodeB = carregarArquivoJSON(new File(caminhoArquivoB));

        // Se algum arquivo falhou ao carregar, retorna null
        if (rootNodeA == null || rootNodeB == null) {
            System.out.println("Erro ao carregar os arquivos JSON.");
            return null;
        }

        // Log para verificar o tamanho dos arquivos carregados
        // int numElementosA = contarElementos(rootNodeA);
        // int numElementosB = contarElementos(rootNodeB);
        // System.out.println("Arquivo A carregado com " + numElementosA + " elementos."); utilizado apenas para teste
        // System.out.println("Arquivo B carregado com " + numElementosB + " elementos."); utilizado apenas para teste

        // Retorna ambos os arquivos como um array de JsonNode
        return new JsonNode[] { rootNodeA, rootNodeB };
    }

    // Método para carregar e verificar um arquivo JSON
    private static JsonNode carregarArquivoJSON(File arquivo) {
        if (!arquivo.exists()) {
            System.out.println("Erro: Arquivo " + arquivo.getName() + " não encontrado!");
            return null; // Retorna null se o arquivo não for encontrado
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(arquivo); // Retorna o conteúdo do arquivo JSON
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo " + arquivo.getName() + ": " + e.getMessage());
            return null;
        }
    }

    // Método para contar o número de elementos em um JsonNode utilizado em debug
    // private static int contarElementos(JsonNode rootNode) {
    //     // Verifica se a chave 'title' existe no JSON
    //     JsonNode titleNode = rootNode.path("title");
    //     return titleNode.size();  // Conta o número de elementos na chave 'title'
    // }
}
