
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    static int ngg = 4;
    static int dml = 5;
    static int porta = 5000;

    static ArrayList<Giocatore> giocatori = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        ServerSocket server = new ServerSocket(porta);

        System.out.println("Server avviato sulla porta " + porta);

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            try {
                server.close();
            } catch (IOException e) {
                System.out.println("Server terminato");
            }
        }).start();

        while (!server.isClosed()) {
            try {
                Socket socket = server.accept();
                new Thread(new GestoreClient(socket)).start();
            } catch (IOException e) {
                if (!server.isClosed()) {
                    System.out.println("Errore di connessione");
                }
            }
        }

        System.out.println("Server terminato");
    }

    static synchronized void aggiungi(Giocatore g) {
        giocatori.add(g);
        System.out.println(g.nickname + " in attesa");
        controllaGruppi();
    }

    static void controllaGruppi() {

        boolean trovato = true;

        while (trovato) {
            trovato = false;

            Collections.sort(giocatori,
                Comparator.comparingInt(g -> g.livello));

            for (int i = 0; i <= giocatori.size() - ngg; i++) {

                int minimo = giocatori.get(i).livello;
                int massimo = giocatori.get(i + ngg - 1).livello;

                if (massimo - minimo <= dml) {

                    ArrayList<Giocatore> gruppo =
                        new ArrayList<>(giocatori.subList(i, i + ngg));

                    giocatori.removeAll(gruppo);

                    for (Giocatore g : gruppo) {
                        g.inviaLista(gruppo);
                    }

                    System.out.println("Gruppo creato!");
                    trovato = true;
                    break;
                }
            }
        }
    }

    static class Giocatore {
        String nickname;
        int livello;
        String ip;
        PrintWriter out;

        Giocatore(String nickname, int livello,
                  String ip, PrintWriter out) {
            this.nickname = nickname;
            this.livello = livello;
            this.ip = ip;
            this.out = out;
        }

        void inviaLista(ArrayList<Giocatore> gruppo) {
            out.println("GRUPPO TROVATO");

            for (Giocatore g : gruppo) {
                out.println(g.nickname + " | " + g.ip
                            + " | Livello: " + g.livello);
            }

            out.println("FINE");
        }
    }

    static class GestoreClient implements Runnable {
        Socket socket;

        GestoreClient(Socket socket) {
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

                String ip = socket.getInetAddress().getHostAddress();

                Giocatore g = new Giocatore(
                    nickname, livello, ip, out);

                aggiungi(g);

                while (!socket.isClosed()) {
                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                System.out.println("Client disconnesso");
            }
        }
    }
}
