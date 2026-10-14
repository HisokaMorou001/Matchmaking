import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    static ArrayList<String> giocatori = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(5000);
        System.out.println("Server avviato");

        while (true) {
            Socket socket = server.accept();
            new Thread(new Gestore(socket)).start();
        }
    }

    static class Gestore implements Runnable {
        Socket socket;

        Gestore(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

                PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

                String nickname = in.readLine();
                int livello = Integer.parseInt(in.readLine());

                String giocatore = nickname + " - Livello: " + livello
                    + " - IP: "
                    + socket.getInetAddress().getHostAddress();

                synchronized (giocatori) {
                    giocatori.add(giocatore);
                    System.out.println("Giocatori registrati:");

                    for (String g : giocatori) {
                        System.out.println(g);
                    }
                }

                out.println("Registrazione completata!");

                socket.close();

            } catch (Exception e) {
                System.out.println("Errore client");
            }
        }
    }
}