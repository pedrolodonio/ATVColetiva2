package projeto2concdist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConexaoCliente {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ConexaoCliente(String host, int porta) throws IOException {
        
        this.socket = new Socket(host, porta);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void enviarConsulta(String consulta) {
        try {
            
            out.println(consulta);
        } catch (Exception e) {
            System.err.println("Erro ao enviar consulta: " + e.getMessage());
        }
    }

    public String receberResposta() {
        try {
            
            return in.readLine();
        } catch (IOException e) {
            System.err.println("Erro ao receber resposta: " + e.getMessage());
            return "Erro ao receber resposta do servidor.";
        }
    }

    public void fecharConexao() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            System.err.println("Erro ao fechar a conexão: " + e.getMessage());
        }
    }
}