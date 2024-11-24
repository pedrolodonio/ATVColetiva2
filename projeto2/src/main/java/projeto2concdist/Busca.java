package projeto2concdist;

public class Busca {

    // Método para calcular a tabela de falhas do padrão
    public int[] calcularTabelaFalhas(String padrao) {
        int m = padrao.length();
        int[] tabelaFalhas = new int[m];
        int j = 0; // Tamanho do prefixo mais longo que é também sufixo

        // Inicializa o índice da tabela de falhas
        tabelaFalhas[0] = 0; 

        // Preprocessamento da tabela de falhas
        for (int i = 1; i < m; i++) {
            while (j > 0 && padrao.charAt(i) != padrao.charAt(j)) {
                j = tabelaFalhas[j - 1]; // Recuar no padrão
            }
            if (padrao.charAt(i) == padrao.charAt(j)) {
                j++; // Expande o prefixo/sufixo
            }
            tabelaFalhas[i] = j; // Atualiza a tabela
        }
        return tabelaFalhas;
    }

    // Método para realizar a busca no texto com o algoritmo KMP
    public boolean kmpBusca(String texto, String padrao) {
        texto = texto.toLowerCase();  // Converte o texto para minúsculas
        padrao = padrao.toLowerCase(); // Converte o padrão para minúsculas
        
        int n = texto.length();
        int m = padrao.length();
        int[] tabelaFalhas = calcularTabelaFalhas(padrao);
        int j = 0; // Índice para o padrão
    
        // Percorre o texto procurando pelo padrão
        for (int i = 0; i < n; i++) {
            while (j > 0 && texto.charAt(i) != padrao.charAt(j)) {
                j = tabelaFalhas[j - 1]; // Recuar no padrão
            }
            if (texto.charAt(i) == padrao.charAt(j)) {
                j++; // Avança no padrão
            }
            if (j == m) { // Encontrou uma correspondência
                return true; // O padrão foi encontrado no texto
            }
        }
        return false; // Nenhuma correspondência encontrada
    }
    
}