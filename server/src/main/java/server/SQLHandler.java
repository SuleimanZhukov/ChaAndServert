package server;

import java.sql.*;

public class SQLHandler {
    private static Connection connection;
    private static PreparedStatement register;
    private static PreparedStatement login;
    private static PreparedStatement changeNick;
    private static ResultSet rs;

    public static boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:simpleChat.db");
            prepareStatements();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void prepareStatements() {
        try {
            register = connection.prepareStatement("INSERT INTO users (login, pswd, nick) VALUES (?, ?, ?);");
            login = connection.prepareStatement("SELECT nick FROM users WHERE login = ? AND pswd = ?;");
            changeNick = connection.prepareStatement("UPDATE users SET nick = ? WHERE nick = ?;");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean registration(String login, String pswd, String nick) {
        try {
            register.setString(1, login);
            register.setString(2, pswd);
            register.setString(3, nick);
            register.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public static String getNickByLoginAndPassword(String log,String pswd) {
        String nick = null;
        try {
            login.setString(1, log);
            login.setString(2, pswd);
            rs = login.executeQuery();

            if (rs.next()) {
                nick = rs.getString(1);
            }

            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return nick;
    }

    public static boolean changeNick(String login, String newNick) {
        try {
            changeNick.setString(1, newNick);
            changeNick.setString(2, login);
            changeNick.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
