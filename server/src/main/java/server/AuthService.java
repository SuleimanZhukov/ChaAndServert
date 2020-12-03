package server;

public interface AuthService {
    String getNickByLoginAndPassword(String login, String pswd);

    Boolean registration(String login, String pswd, String nick);

    Boolean changeNick(String oldNick, String newNick);
}
