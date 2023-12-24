package com.example.dbfinalproject;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Period;

public class PatientsController implements Initializable {
    @FXML private TableView<Patient> patTable;
    @FXML private TableColumn<Patient, Integer> patId;
    @FXML private TableColumn<Patient, String> patFname;
    @FXML private TableColumn<Patient, String> patLname;
    @FXML private TableColumn<Patient, String> patPhone;
    @FXML private TableColumn<Patient, String> patGender;
    @FXML private TableColumn<Patient, LocalDate> patBirthdate;
    @FXML private TableColumn<Patient, Integer> patAge;
    @FXML private TableColumn<Patient, Void> deleteBtn;
    @FXML private TextField searchField;
    public static ObservableList<Patient> patList = FXCollections.observableArrayList();
    @FXML
    private void addPat() throws IOException {
        Main.showWindow("addPat.fxml", "Add Patient");
    }
    public static int getAge(LocalDate birthdate){
        LocalDate now = LocalDate.now();
        Period diff = Period.between(birthdate, now);
        return diff.getYears();
    }
    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        patList.clear();
        try{
            String sql = "SELECT * FROM patient";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Patient newPat = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate(), getAge(rs.getDate(6).toLocalDate()));
                patList.add(newPat);
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        patId.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("id"));
        patFname.setCellValueFactory(new PropertyValueFactory<Patient, String>("fname"));
        patLname.setCellValueFactory(new PropertyValueFactory<Patient, String>("lname"));
        patPhone.setCellValueFactory(new PropertyValueFactory<Patient, String>("phone"));
        patGender.setCellValueFactory(new PropertyValueFactory<Patient, String>("gender"));
        patAge.setCellValueFactory(new PropertyValueFactory<Patient, Integer>("age"));
        patBirthdate.setCellValueFactory(new PropertyValueFactory<Patient, LocalDate>("birthdate"));
        deleteBtn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("\u2716");

            {
                deleteButton.setOnAction(event -> {
                    Patient pat = getTableRow().getItem();
                    if (pat != null) {
                        deletePatient(pat);
                    }
                });
                // buttons style
                deleteButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 0;-fx-font-size: 18px; -fx-min-width: 40px;");
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
                });
        patTable.setItems(patList);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterPatients(newValue);
        });
        patList.addListener((ListChangeListener.Change<? extends Patient> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    patTable.setItems(patList);
                }
            }
        });
    }
    private void filterPatients(String searchText) {
        ObservableList<Patient> filteredList = FXCollections.observableArrayList();

        for (Patient patient : patList) {
            if (patient.getFname().toLowerCase().contains(searchText.toLowerCase()) ||
                    patient.getLname().toLowerCase().contains(searchText.toLowerCase()) ||
                    patient.getPhone().toLowerCase().contains(searchText.toLowerCase()) ||
                    patient.getPhone().contains(searchText.toLowerCase())) {
                filteredList.add(patient);
            }
        }

        patTable.setItems(filteredList);
    }
    public void deletePatient(Patient patient) {
        try {
            Connection con = new DatabaseConnection().getConnection();
            String sql = "DELETE FROM patient WHERE pat_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, patient.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Patient deleted successfully");
                patList.remove(patient); // Remove the patient from the observable list
            } else {
                System.out.println("Error deleting patient");
            }
            con.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("This patient has appointments, you can't delete him");
            alert.showAndWait();
        }
    }
}
