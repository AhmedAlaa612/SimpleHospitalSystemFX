package com.example.dbfinalproject;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardController implements Initializable {
    @FXML
    private TableView<Appointment> appTable;
    @FXML private TableColumn<Appointment, Integer> turn;
    @FXML private TableColumn<Appointment, Integer> patId;
    @FXML private TableColumn<Appointment, Void> deleteBtn;
    @FXML private TableColumn<Appointment, Integer> docId;
    @FXML private TableColumn<Appointment, String> patName;
    @FXML private TableColumn<Appointment, String> docName;
    @FXML private TableColumn<Appointment, String> specName;
    @FXML private Text patCount;
    @FXML private ComboBox<Doctor> doctorBox;
    public static ObservableList<Appointment> appList = FXCollections.observableArrayList();
    public static ObservableList<Doctor> doctorList = FXCollections.observableArrayList();
    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        appList.clear();
        try{
            String sql = "SELECT * FROM appointments INNER JOIN patient ON appointments.pat_id = patient.pat_id INNER JOIN doctor ON appointments.doc_id = doctor.id INNER JOIN specialization ON doctor.spec_id = specialization.spec_id order by appointments.stat_id desc, appointments.id";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Appointment newApp = new Appointment(rs.getInt(1), rs.getInt(3), rs.getInt(4), rs.getString(6) +" "+rs.getString(7), rs.getString(12) +" "+rs.getString(13), rs.getString(18), rs.getInt(2));
                appList.add(newApp);
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        turn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("turn"));
        patId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("patId"));
        docId.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("docId"));
        patName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("patName"));
        docName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("docName"));
        specName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("specName"));
        deleteBtn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("\u2716");

            {
                deleteButton.setOnAction(event -> {
                    Appointment app = getTableRow().getItem();
                    if (app != null) {
                        deleteApp(app);
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
        appList.addListener((ListChangeListener.Change<? extends Appointment> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    appTable.setItems(appList);
                    appCount();
                    loadDoctors();
                }
            }
        });
        appTable.setRowFactory(tv -> new TableRow<Appointment>() {
            @Override
            protected void updateItem(Appointment item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getStatId() == 1) {
                        setStyle("-fx-background-color: #ffffbf;");
                    } else if (item.getStatId() == 2) {
                        setStyle("-fx-background-color: #e54848;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        appTable.setItems(appList);
        appCount();
        loadDoctors();
    }
    public void deleteApp(Appointment app) {
        try {
            Connection con = new DatabaseConnection().getConnection();
            String sql = "DELETE FROM appointments WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, app.getTurn());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Appointment canceled successfully");
                appList.remove(app); // Remove the patient from the observable list
            } else {
                System.out.println("Error deleting appointment");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getNextPat(){
        Doctor doc = doctorBox.getValue();
        // Check if all fields are filled
        if (doc == null) {
            // Show a message if any field is empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No doctor selected");
            alert.setHeaderText(null);
            alert.setContentText("please select a doctor");
            alert.showAndWait();
            return;
        }
        try {
            Connection con = new DatabaseConnection().getConnection();
            String sql = "DELETE FROM appointments where doc_id = ? order by stat_id, id  LIMIT 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, doc.getId());
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
        doctorBox.setValue(null);
    }
    private void refreshTable(){
        appList.clear();
        try{
            String sql = "SELECT * FROM appointments INNER JOIN patient ON appointments.pat_id = patient.pat_id INNER JOIN doctor ON appointments.doc_id = doctor.id INNER JOIN specialization ON doctor.spec_id = specialization.spec_id order by stat_id desc, id";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Appointment newApp = new Appointment(rs.getInt(1), rs.getInt(3), rs.getInt(4), rs.getString(6) +" "+rs.getString(7), rs.getString(12) +" "+rs.getString(13), rs.getString(18), rs.getInt(2));
                appList.add(newApp);
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    private void appCount(){
        try{
            String sql = "SELECT COUNT(*) FROM appointments";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                patCount.setText(String.valueOf(rs.getInt(1)));
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void loadDoctors(){
        doctorList.clear();
        try{
            String sql = "SELECT * FROM doctor INNER JOIN specialization ON doctor.spec_id = specialization.spec_id having doctor.id in (SELECT DISTINCT doc_id FROM appointments)";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Doctor newDoc = new Doctor(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(8), rs.getInt(5), rs.getString(6), rs.getInt(4));
                doctorList.add(newDoc);
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        doctorBox.setItems(doctorList);
    }
}
