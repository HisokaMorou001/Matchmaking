import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    static int ngg = 4;
    static int dml = 5;
    static ArrayList<Giocatore> giocatori = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(5000);
        System.out.println("Server avviato");

        while (true) {
            Socket socket = server.accept();
            new Thread(new Gestore(socket)).start();
        }
    }

    static synchronized void aggiungi(Giocatore g) {
        giocatori.add(g);
        controllaGruppi();
    }

    static void controllaGruppi() {
        Collections.sort(giocatori,
            Comparator.comparingInt(g -> g.livello));

        while (giocatori.size() >= ngg) {
            int minimo = giocatori.get(0).livello;
            int massimo = giocatori.get(ngg - 1).livello;

            if (massimo - minimo > dml) {
                break;
            }

            ArrayList<Giocatore> gruppo =
                new ArrayList<>(giocatori.subList(0, ngg));

            giocatori.subList(0, ngg).clear();

            for (Giocatore g : gruppo) {
                g.invia(gruppo);
            }

            System.out.println("Gruppo creato!");
        }
    }

    static class Giocatore {
        String nickname;
        String ip;
        int livello;
        PrintWriter out;

        Giocatore(String n, int l, String ip, PrintWriter out) {
            nickname = n;
            livello = l;
            this.ip = ip;
            this.out = out;
        }

        void invia(ArrayList<Giocatore> gruppo) {
            out.println("GRUPPO TROVATO");

            for (Giocatore g : gruppo) {
                out.println(g.nickname + " | " + g.ip
                    + " | Livello: " + g.livello);
            }

            out.println("FINE");
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
                String ip = socket.getInetAddress().getHostAddress();

                Giocatore g = new Giocatore(nickname, livello, ip, out);

                System.out.println(nickname + " in attesa");
                aggiungi(g);

                String riga;
                while ((riga = in.readLine()) != null) {
                    // Il client resta collegato.
                }

            } catch (Exception e) {
                System.out.println("Connessione terminata");
            }
        }
    }
}