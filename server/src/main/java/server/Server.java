package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class Server {
    private AuthService authService;
    private ServerSocket server;
    private Socket socket;
    private final int PORT = 8981;
    private List<ClientHandler> clients;

    public Server() {
        clients = new Vector();
        authService = new DBAuthService();

        if (!SQLHandler.connect()) {
            throw new RuntimeException("Couldn't connect to database.");
        }

        try {
            server = new ServerSocket(PORT);
            System.out.println("Server online");

            while (true) {
                socket = server.accept();
                System.out.println("Client connected");
                subscribe(new ClientHandler(socket, this));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.close();
                SQLHandler.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public void subscribe(ClientHandler c) {
        clients.add(c);
    }

    public void unsubscribe(ClientHandler c) {
        clients.remove(c);
    }

    public void broadcastMsg(String msg) {
        for (ClientHandler clientHandler : clients) {
            clientHandler.sendMsg(msg);
        }
    }

    public void privateMsg(ClientHandler clientHandler, String receiver, String text) {
        boolean bool = false;
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(receiver)) {
                client.sendMsg(String.format("%s [private] %s: %s", clientHandler.getNickname(), receiver, text));
                clientHandler.sendMsg(String.format("%s [private] %s: %s", clientHandler.getNickname(), receiver, text));
                bool = true;
            }
        }
        if (!bool) {
            clientHandler.sendMsg("There's no such person or he is offline.");
        }
    }
}
