package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.controller.RegExPatterns.RegExPatterns;
import lk.ijse.dto.RegisterDto;
import lk.ijse.model.RegisterModel;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterFormController{
    @FXML
    private Label registerLabel;

    @FXML
    private AnchorPane root;

    @FXML
    private TextField txtConPassword;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;
    private RegisterModel registerModel = new RegisterModel();

    private void clearFields(){
        txtName.setText("");
        txtPassword.setText("");
        txtConPassword.setText("");
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.root.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.centerOnScreen();
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        String userName = txtName.getText();
        String password = txtPassword.getText();
        String ConPassword = txtConPassword.getText();

        boolean isUserValid = RegExPatterns.getValidName().matcher(userName).matches();
        boolean isPasswordValid = RegExPatterns.getValidPassword().matcher(password).matches();


        if (!isPasswordValid){
            new Alert(Alert.AlertType.ERROR,"Password should contain minimum of four Characters").showAndWait();
            return;
        }
        if (!ConPassword.equals(password)){
            new Alert(Alert.AlertType.ERROR,"Password Did Not Matched").showAndWait();
        }
        else {
            var dto = new RegisterDto(userName, password);
            try {
                boolean checkDuplicates = RegisterModel.check(userName, password);
                if (checkDuplicates) {
                    new Alert(Alert.AlertType.ERROR, "Duplicate Entry").showAndWait();
                    return;
                }
                boolean isRegistered = RegisterModel.registerUser(dto);
                if (isRegistered) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Your Account Has been Created").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    @FXML
    void txtNameOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
        txtConPassword.requestFocus();
    }

    @FXML
    void txtConPasswordOnAction(ActionEvent event) {
        btnRegisterOnAction(new ActionEvent());
    }

}
