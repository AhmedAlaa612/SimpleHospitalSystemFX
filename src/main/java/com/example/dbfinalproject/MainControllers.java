package com.example.dbfinalproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainControllers {
    @FXML
    private StackPane contentArea;
    @FXML
    private void setDashboard(){setScene("dashboard.fxml");}
    @FXML
    private void setAppointment(){setScene("appointment.fxml");}
    @FXML private void setPatient(){setScene("patients.fxml");}
    @FXML private void setDoctors(){setScene("doctors.fxml");}
    @FXML private void setSpec(){setScene("specView.fxml");}
    public void setScene(String fxmlFile){
        try {
            Parent fxml = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxml);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
