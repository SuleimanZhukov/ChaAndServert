package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    private Controller controller;
    @FXML
    public Label label;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField textField;

    public void login(ActionEvent actionEvent) {
        controller.login(textField.getText().trim(), passwordField.getText().trim());
        textField.getScene().getWindow().hide();
    }

    public void registration(ActionEvent actionEvent) {
        controller.registrationWindow();
        controller.getRegWindow().show();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
