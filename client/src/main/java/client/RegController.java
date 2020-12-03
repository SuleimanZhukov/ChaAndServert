package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegController {
    private Controller controller;

    @FXML
    public TextField login;
    @FXML
    public TextField nick;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Label label;

    public void registration(ActionEvent actionEvent) {
        controller.registration(login.getText().trim(), passwordField.getText().trim(), nick.getText().trim());
        login.getScene().getWindow().hide();
    }

    public void cancel(ActionEvent actionEvent) {
        login.getScene().getWindow().hide();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
