
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

        new Thread(() -> {
            Scanner tastiera = new Scanner(System.in);
            tastiera.nextLine();

            try {
                server.close();
            } catch (Exception e) {
                System.out.println("Errore");
            }
        }).start();

        while (!server.isClosed()) {
            try {
                Socket socket = server.accept();
                new Thread(new Gestore(socket)).start();
            } catch (Exception e) {
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

    static synchronized void rimuovi(Giocatore g) {
        giocatori.remove(g);
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
                        g.invia(gruppo);
                    }

                    System.out.println("Gruppo creato");
                    trovato = true;
                    break;
                }
            }
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
            Giocatore g = null;

            try {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

                PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

                String nickname = in.readLine();
                int livello = Integer.parseInt(in.readLine());

                String ip = socket.getInetAddress().getHostAddress();

                g = new Giocatore(nickname, livello, ip, out);
                aggiungi(g);

                while (in.readLine() != null) {
                    // Attende finché il client resta collegato.
                }

            } catch (Exception e) {
                System.out.println("Client disconnesso");
            } finally {
                if (g != null) {
                    rimuovi(g);
                }

                try {
                    socket.close();
                } catch (Exception e) {
                    // Connessione già chiusa.
                }
            }
        }
    }
}
