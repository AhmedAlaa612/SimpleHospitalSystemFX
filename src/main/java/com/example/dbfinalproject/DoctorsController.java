package com.example.dbfinalproject;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DoctorsController implements Initializable {
    @FXML
    private TableView<Doctor> doctorTable;
    @FXML private TableColumn<Doctor, Integer> specId;
    @FXML private TableColumn<Doctor, String> specName;
    @FXML private TableColumn<Doctor, Void> deleteBtn;
    @FXML private TableColumn<Doctor, Integer> docId;
    @FXML private TableColumn<Doctor, String> docFname;
    @FXML private TableColumn<Doctor, String> docLname;
    @FXML private TableColumn<Doctor, String> docPhone;
    @FXML private TableColumn<Doctor, Integer> docSalary;
    @FXML private TextField searchField;
    @FXML private TextField AddDocFname;
    @FXML private TextField AddDocLname;
    @FXML private TextField AddDocPhone;
    @FXML private TextField AddDocSalary;
    @FXML private ComboBox<Spec> AddDocSpec;
    public static ObservableList<Doctor> DoctorList = FXCollections.observableArrayList();


    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        DoctorList.clear();
        try{
            String sql = "Select * FROM doctor INNER JOIN specialization ON doctor.spec_id = specialization.spec_id";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Doctor newDoc= new Doctor(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(8), rs.getInt(5), rs.getString(6), rs.getInt(4));
                DoctorList.add(newDoc);
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        docId.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("id"));
        docFname.setCellValueFactory(new PropertyValueFactory<Doctor, String>("fname"));
        docLname.setCellValueFactory(new PropertyValueFactory<Doctor, String>("lname"));
        docPhone.setCellValueFactory(new PropertyValueFactory<Doctor, String>("phone"));
        docSalary.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("salary"));
        specId.setCellValueFactory(new PropertyValueFactory<Doctor, Integer>("specId"));
        specName.setCellValueFactory(new PropertyValueFactory<Doctor, String>("spec"));
        deleteBtn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("\u2716");

            {
                deleteButton.setOnAction(event -> {
                    Doctor Doctor = getTableRow().getItem();
                    if (Doctor != null) {
                        deleteDoc(Doctor);
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
        doctorTable.setItems(DoctorList);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterDoc(newValue);
        });
        DoctorList.addListener((ListChangeListener.Change<? extends Doctor> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    doctorTable.setItems(DoctorList);
                }
            }
        });
        AddDocSpec.setItems(SpecControllers.specList);
    }
    private void filterDoc(String searchText) {
        ObservableList<Doctor> filteredList = FXCollections.observableArrayList();

        for (Doctor doctor : DoctorList) {
            if (doctor.getFname().toLowerCase().contains(searchText.toLowerCase()) ||
                doctor.getLname().toLowerCase().contains(searchText.toLowerCase()) ||
                doctor.getPhone().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(doctor);
            }
        }

        doctorTable.setItems(filteredList);
    }
    public void deleteDoc(Doctor doctor) {
        try {
            Connection con = new DatabaseConnection().getConnection();
            String sql = "DELETE FROM doctor WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, doctor.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Doctor deleted successfully");
                DoctorList.remove(doctor); // Remove the patient from the observable list
            } else {
                System.out.println("Error deleting Doctor");
            }
            con.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete Error");
            alert.setHeaderText(null);
            alert.setContentText("This doctor is assigned to an appointment, you can't delete it");
            alert.showAndWait();
        }
    }
    public void AddBtn(){
        String fname = AddDocFname.getText();
        String lname = AddDocLname.getText();
        String phone = AddDocPhone.getText();
        int salary = Integer.parseInt(AddDocSalary.getText());
        Spec spec = AddDocSpec.getValue();
        // Check if all fields are filled
        if (fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || salary == 0 || spec == null) {
            // Show a message if any field is empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Information");
            alert.setHeaderText(null);
            alert.setContentText("Please add fill all the fields");
            alert.showAndWait();
            return;
        }
        try {
            Connection con = new DatabaseConnection().getConnection();
            String sql = "INSERT INTO doctor (fname, lname, phone, salary, spec_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setString(3, phone);
            ps.setInt(4, salary);
            ps.setInt(5, spec.getId());
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
        AddDocFname.clear();
        AddDocLname.clear();
        AddDocPhone.clear();
        AddDocSalary.clear();
        AddDocSpec.setValue(null);
    }
    private void refreshTable(){
        DoctorList.clear();
        try{
            String sql = "SELECT * FROM doctor INNER JOIN specialization ON doctor.spec_id = specialization.spec_id";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Doctor newDoc= new Doctor(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(8), rs.getInt(5), rs.getString(6), rs.getInt(4));
                DoctorList.add(newDoc);
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
