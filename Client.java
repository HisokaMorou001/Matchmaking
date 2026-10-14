
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {

        Scanner tastiera = new Scanner(System.in);

        System.out.print("IP del server: ");
        String ip = tastiera.nextLine();

        System.out.print("Porta: ");
        int porta = Integer.parseInt(tastiera.nextLine());

        System.out.print("Nickname: ");
        String nickname = tastiera.nextLine();

        System.out.print("Livello: ");
        int livello = Integer.parseInt(tastiera.nextLine());

        Socket socket = new Socket(ip, porta);

        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));

        PrintWriter out = new PrintWriter(
            socket.getOutputStream(), true);

        out.println(nickname);
        out.println(livello);

        System.out.println("In attesa di una partita...");

        String riga;

        while ((riga = in.readLine()) != null) {

            if (riga.equals("FINE")) {
                break;
            }

            System.out.println(riga);
        }

        socket.close();
        System.out.println("Connessione terminata");
    }
}
