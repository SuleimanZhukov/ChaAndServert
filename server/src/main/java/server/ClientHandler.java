package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private Server server;
    private Socket socket;

    private DataInputStream in;
    private DataOutputStream out;

    private String nickname;

    public ClientHandler(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/reg")) {
                            String[] token = str.split("\\s+");
                            boolean reg = server.getAuthService().registration(token[1], token[2], token[3]);
                            if (reg) {
                                sendMsg("/regok");
                                continue;
                            } else {
                                continue;
                            }
                        }

                        if (str.startsWith("/login")) {
                            String[] token = str.split("\\s+");
                            String nick = server.getAuthService().getNickByLoginAndPassword(token[1], token[2]);
                            if (nick != null) {
                                nickname = nick;
                                sendMsg("/go " + nickname);
                                break;
                            }
                        }
                    }

                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/pvt")) {
                            String[] token = str.split("\\s+", 3);
                            server.privateMsg(this, token[1], token[2]);
                        }

                        if (str.startsWith("/chnick")) {

                            String[] token = str.split("\\s+");
                            if (token[2].contains(" ")) {
                                sendMsg("No spaces");
                                continue;
                            }
                            String newNick = str.split("\\s+")[2];
                            Boolean bool = server.getAuthService().changeNick(token[1], token[2]);
                            if (bool) {
                                sendMsg("/yournickis " + newNick);
                                this.nickname = newNick;
                            } else {
                                sendMsg("Nickname is already in use");
                            }
                        }

                        if (str.equals("/ends")) {
                            break;
                        }

                        if (!str.startsWith("/")) {
                            server.broadcastMsg(this.nickname + ": " + str);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.unsubscribe(this);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }
}
