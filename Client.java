import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        Scanner tastiera = new Scanner(System.in);

        System.out.print("IP server: ");
        String ip = tastiera.nextLine();

        System.out.print("Porta: ");
        int porta = Integer.parseInt(tastiera.nextLine());

        System.out.print("Nickname: ");
        String nickname = tastiera.nextLine();

        System.out.print("Livello: ");
        int livello = Integer.parseInt(tastiera.nextLine());

        Socket socket = new Socket(ip, porta);

        PrintWriter out = new PrintWriter(
            socket.getOutputStream(), true);

        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));

        out.println(nickname);
        out.println(livello);

        System.out.println("In attesa della partita...");

        String riga;
        while ((riga = in.readLine()) != null) {
            if (riga.equals("FINE")) {
                break;
            }

            System.out.println(riga);
        }

        socket.close();
    }
}