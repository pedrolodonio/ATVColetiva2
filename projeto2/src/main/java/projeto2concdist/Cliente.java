package projeto2concdist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {


    public void FazConsulta (String consulta){
       
        try (
        Socket socket = new Socket ("localhost",8080);
        PrintWriter out = new PrintWriter (socket.getOutputStream(),true);
        BufferedReader in = new BufferedReader (new InputStreamReader (socket.getInputStream()))
        ) {

            out.println(consulta);

            String resposta = in.readLine();

            System.out.println(" "+ resposta);
        } catch(IOException e){
            e.printStackTrace();
        }
        
        
}
    

}