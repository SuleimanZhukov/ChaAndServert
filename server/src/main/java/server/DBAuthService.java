package server;

public class DBAuthService implements AuthService {

    @Override
    public String getNickByLoginAndPassword(String login, String pswd) {
        return SQLHandler.getNickByLoginAndPassword(login, pswd);
    }

    @Override
    public Boolean registration(String login, String pswd, String nick) {
        return SQLHandler.registration(login, pswd, nick);
    }

    @Override
    public Boolean changeNick(String oldNick, String newNick) {
        return SQLHandler.changeNick(oldNick, newNick);
    }
}
