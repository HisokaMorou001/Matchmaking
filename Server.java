import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(5000);
        System.out.println("Server avviato");

        Socket socket = server.accept();

        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));

        PrintWriter out = new PrintWriter(
            socket.getOutputStream(), true);

        String nickname = in.readLine();
        String livello = in.readLine();

        System.out.println("Giocatore: " + nickname);
        System.out.println("Livello: " + livello);

        out.println("Sei collegato al server!");

        socket.close();
        server.close();
    }
}