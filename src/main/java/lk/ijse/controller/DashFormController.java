package lk.ijse.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DashFormController {
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
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblTotalCustomers;

    @FXML
    private Label lblTotalOrders;

    @FXML
    private Label lblTotalSupplers;

}
