import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        Scanner tastiera = new Scanner(System.in);

        System.out.print("IP server: ");
        String ip = tastiera.nextLine();

        Socket socket = new Socket(ip, 5000);

        PrintWriter out = new PrintWriter(
            socket.getOutputStream(), true);

        BufferedReader in = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));

        System.out.print("Nickname: ");
        String nickname = tastiera.nextLine();

        System.out.print("Livello: ");
        String livello = tastiera.nextLine();

        out.println(nickname);
        out.println(livello);

        System.out.println(in.readLine());

        socket.close();
    }
}