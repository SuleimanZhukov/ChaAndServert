package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TextField textField;
    @FXML
    public TextArea textArea;
    @FXML
    public ImageView imageArrow;

    private Socket socket;
    private final int PORT = 8981;
    private final String IP_ADDRESS = "localhost";

    private DataOutputStream out;
    private DataInputStream in;

    private Stage regWindow;
    private Stage loginRegStage;

    private LoginController loginController;
    private RegController regController;

    private String title = "SimpleChat: ";
    private String nickname;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loginWindow();
        setTitle(nickname);
        connect();
    }

    private void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    // Authentication loop
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/regok")) {
                            continue;
                        }

                        if (str.startsWith("/go")) {
                            nickname = str.split("\\s+")[1];
                            break;
                        }
                    }

                    // Work loop
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/yournickis")) {
                            String newNick = str.split("\\s")[1];
                            nickname = newNick;
                            appendText("Your new nick is " + nickname);
                        }

                        if (str.equals("/ends")) {
                            break;
                        }
                        appendText(str + "\n");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
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

    // Sends a message
    public void sendMsg(ActionEvent actionEvent) {
        try {
            out.writeUTF(textField.getText());
            textField.clear();
            textField.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Opens login window
    private void loginWindow() {
        try {
            loginRegStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginWindow.fxml"));

            Parent root = fxmlLoader.load();
            loginRegStage.setTitle("Login");
            loginRegStage.setScene(new Scene(root, 275, 320));
            loginRegStage.getScene().getStylesheets().add("/style.css");

            loginController = fxmlLoader.getController();
            loginController.setController(this);

            loginRegStage.initModality(Modality.APPLICATION_MODAL);

            loginRegStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Opens registration window
    public void registrationWindow() {
        try {
            regWindow = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RegWindow.fxml"));

            Parent root = fxmlLoader.load();
            regWindow.setTitle("Registration");
            regWindow.setScene(new Scene(root, 275, 320));
            regWindow.getScene().getStylesheets().add("/style.css");

            regController = fxmlLoader.getController();
            regController.setController(this);

            regWindow.initModality(Modality.APPLICATION_MODAL);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // appends text to TextArea
    private void appendText(String text) {
        textArea.appendText(text);
    }

    // Sends registration command
    public void registration(String login, String password, String nick) {
        String msg = String.format("/reg %s %s %s", login, password, nick);

        if (socket == null || socket.isClosed()) {
            connect();
        }

        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sends login command
    public void login(String login, String password) {
        String msg = String.format("/login %s %s", login, password);

        if (socket == null || socket.isClosed()) {
            connect();
        }

        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sets title text
    private void setTitle(String nickname) {
        Platform.runLater(() -> {
            ((Stage) textField.getScene().getWindow()).setTitle(title + nickname);
        });
    }

    public Stage getRegWindow() {
        return regWindow;
    }
}
