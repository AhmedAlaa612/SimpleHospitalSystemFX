package com.example.dbfinalproject;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class AddPatController {
    @FXML TextField fnameField;
    @FXML TextField lnameField;
    @FXML TextField phoneField;
    @FXML DatePicker dateField;
    @FXML RadioButton maleBtn;
    @FXML RadioButton femaleBtn;
    @FXML Button submitBtn;
    @FXML Button cancelBtn;
    @FXML
    public void submitBtn(){
        String fname = fnameField.getText();
        String lname = lnameField.getText();
        String phone = phoneField.getText();
        LocalDate birthdate = dateField.getValue();
        boolean isMale = maleBtn.isSelected();
        // Check if all fields are filled
        if (fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || birthdate == null
                || (!isMale && !femaleBtn.isSelected())) {
            // Show a message if any field is empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all fields.");
            alert.showAndWait();
            return;
        }
        try {
            Connection con = new DatabaseConnection().getConnection();
            String sql = "INSERT INTO patient (fname, lname, phone, gender, birthdate) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, phone);
            ps.setString(4, isMale ? "Male" : "Female");
            ps.setDate(5, java.sql.Date.valueOf(birthdate));
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Success");
            } else {
                System.out.println("Error");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        refreshTable();
        fnameField.clear();
        lnameField.clear();
        phoneField.clear();
        dateField.setValue(null);
        maleBtn.setSelected(false);
        femaleBtn.setSelected(false);
        Stage stage = (Stage)submitBtn.getScene().getWindow();
        stage.close();
    }
    @FXML
    private void cancelBtn() {
        Stage stage = (Stage)cancelBtn.getScene().getWindow();
        stage.close();
    }
    private void refreshTable(){
        PatientsController.patList.clear();
        AppointmentController.patientList.clear();
        try{
            String sql = "SELECT * FROM patient";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Patient newPat = new Patient(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6).toLocalDate(), PatientsController.getAge(rs.getDate(6).toLocalDate()));
                PatientsController.patList.add(newPat);
                AppointmentController.patientList.add(newPat);
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
