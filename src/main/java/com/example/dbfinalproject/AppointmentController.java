package com.example.dbfinalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AppointmentController implements Initializable {
    @FXML private ComboBox<Patient> patientBox;
    @FXML private ComboBox<Doctor> doctorBox;
    @FXML private ComboBox<Spec> specBox;
    @FXML private RadioButton normal;
    @FXML private RadioButton urgent;
    @FXML private RadioButton emergency;

    @FXML private void newPatient(){
        try {
            Main.showWindow("addPat.fxml", "New Patient");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML private void addAppointment(){
        Patient pat = patientBox.getSelectionModel().getSelectedItem();
        Doctor doc = doctorBox.getSelectionModel().getSelectedItem();
        Spec spec = specBox.getSelectionModel().getSelectedItem();
        if (pat == null || doc == null || spec == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Information");
            alert.setHeaderText(null);
            alert.setContentText("Please add fill all the fields");
            alert.showAndWait();
            return;
        }
        try{
            String sql = "INSERT INTO appointments (pat_id, doc_id, stat_id) VALUES (?, ?, ?)";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, pat.getId());
            ps.setInt(2, doc.getId());
            if (normal.isSelected()) ps.setInt(3, 0);
            else if (urgent.isSelected()) ps.setInt(3, 1);
            else if (emergency.isSelected()) ps.setInt(3, 2);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) System.out.println("Success");
            else System.out.println("Error");
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        patientBox.setValue(null);
        doctorBox.setValue(null);
        specBox.setValue(null);
        normal.setSelected(true);
        urgent.setSelected(false);
        emergency.setSelected(false);
    }
    public static ObservableList<Doctor> filteredDoctorsList = FXCollections.observableArrayList();
    public static ObservableList<Patient> patientList = FXCollections.observableArrayList();
    public static ObservableList<Spec> specList = FXCollections.observableArrayList();
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        patientList.clear();
        specList.clear();
        filteredDoctorsList.clear();
        loadPatients();
        loadSpecs();
        specBox.setItems(specList);
        specBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateDoctors(newValue);
        });
    }
    private void updateDoctors(Spec spec){
        if (spec == null) return;
        filteredDoctorsList.clear();
        try{
            String sql = "SELECT * FROM doctor INNER JOIN specialization ON doctor.spec_id = specialization.spec_id where specialization.spec_id = ?";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, spec.getId());
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Doctor newDoc = new Doctor(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(8), rs.getInt(5), rs.getString(6), rs.getInt(4));
                filteredDoctorsList.add(newDoc);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        doctorBox.setItems(filteredDoctorsList);
    }
    private void loadPatients(){
        patientList.clear();
        try{
            String sql = "SELECT * FROM patient";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Patient newPat = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate(), PatientsController.getAge(rs.getDate(6).toLocalDate()));
                patientList.add(newPat);
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        patientBox.setItems(patientList);
    }
    private void loadSpecs(){
        specList.clear();
        try{
            String sql = "SELECT * FROM specialization";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Spec newSpec = new Spec(rs.getInt(1), rs.getString(2));
                specList.add(newSpec);
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
