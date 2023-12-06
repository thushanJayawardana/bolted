package lk.ijse.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.dto.RegisterDto;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DashboardFormController {
    @FXML
    private AnchorPane rootNode;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;
    private RegisterDto userDto;

    public void initialize(){
        setDateAndTime();
    }
    private void setDateAndTime(){
        Platform.runLater(() -> {
            lblDate.setText(String.valueOf(LocalDate.now()));

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), event -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
                String timeNow = LocalTime.now().format(formatter);
                lblTime.setText(timeNow);
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
    }
    @FXML
    void btnItemOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane=FXMLLoader.load(this.getClass().getResource("/view/item_form.fxml"));
        this.rootNode.getChildren().clear();
        this.rootNode.getChildren().add(anchorPane);
    }
    @FXML
    void btnBillingOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane=FXMLLoader.load(this.getClass().getResource("/view/billing_form.fxml"));
        this.rootNode.getChildren().clear();
        this.rootNode.getChildren().add(anchorPane);
    }

    @FXML
    void btnOrderOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane=FXMLLoader.load(this.getClass().getResource("/view/order_form.fxml"));
        this.rootNode.getChildren().clear();
        this.rootNode.getChildren().add(anchorPane);
    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane=FXMLLoader.load(this.getClass().getResource("/view/customers_form.fxml"));
        this.rootNode.getChildren().clear();
        this.rootNode.getChildren().add(anchorPane);
    }

    @FXML
    void btnEmployeeOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane=FXMLLoader.load(this.getClass().getResource("/view/employee_form.fxml"));
        this.rootNode.getChildren().clear();
        this.rootNode.getChildren().add(anchorPane);
    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane=FXMLLoader.load(this.getClass().getResource("/view/supplier_form.fxml"));
        this.rootNode.getChildren().clear();
        this.rootNode.getChildren().add(anchorPane);
    }


    @FXML
    void btnProductOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(this.getClass().getResource("/view/productCategory_form.fxml"));
        this.rootNode.getChildren().clear();
        this.rootNode.getChildren().add(anchorPane);
    }
    @FXML
    void btnLogOutOnAction(ActionEvent event) throws IOException {
        rootNode.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"))));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }
    @FXML
    void btnDashboardOnAction(MouseEvent event) throws IOException {
        AnchorPane anchorPane=FXMLLoader.load(this.getClass().getResource("/view/dash_form.fxml"));
        this.rootNode.getChildren().clear();
        this.rootNode.getChildren().add(anchorPane);
    }


    public void setUser(RegisterDto userDto) {
        this.userDto = userDto;
    }

}
