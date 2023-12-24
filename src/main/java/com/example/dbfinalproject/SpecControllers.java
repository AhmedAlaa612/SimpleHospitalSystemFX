package com.example.dbfinalproject;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.Period;

public class SpecControllers implements Initializable{
    @FXML
    private TableView<Spec> specTable;
    @FXML private TableColumn<Spec, Integer> specId;
    @FXML private TableColumn<Spec, String> specName;
    @FXML private TableColumn<Spec, Void> deleteBtn;
    @FXML private TextField searchField;
    @FXML private TextField AddspecField;
    public static ObservableList<Spec> specList = FXCollections.observableArrayList();


    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
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
        specId.setCellValueFactory(new PropertyValueFactory<Spec, Integer>("id"));
        specName.setCellValueFactory(new PropertyValueFactory<Spec, String>("name"));
        deleteBtn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("\u2716");

            {
                deleteButton.setOnAction(event -> {
                    Spec spec = getTableRow().getItem();
                    if (spec != null) {
                        deleteSpec(spec);
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
        specTable.setItems(specList);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterSpec(newValue);
        });
        specList.addListener((ListChangeListener.Change<? extends Spec> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    specTable.setItems(specList);
                }
            }
        });
    }
    private void filterSpec(String searchText) {
        ObservableList<Spec> filteredList = FXCollections.observableArrayList();

        for (Spec spec : specList) {
            if (spec.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(spec);
            }
        }

        specTable.setItems(filteredList);
    }
    public void deleteSpec(Spec spec) {
        try {
            Connection con = new DatabaseConnection().getConnection();
            String sql = "DELETE FROM specialization WHERE spec_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, spec.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Spec deleted successfully");
                specList.remove(spec); // Remove the patient from the observable list
            } else {
                System.out.println("Error deleting Spec");
            }
            con.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delete Error");
            alert.setHeaderText(null);
            alert.setContentText("You can't delete this specialization because it has doctors in it, fire them first");
            alert.showAndWait();
            return;
        }
    }
    public void AddBtn(){
        String name = AddspecField.getText();
        // Check if all fields are filled
        if (name.isEmpty()) {
            // Show a message if any field is empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incomplete Information");
            alert.setHeaderText(null);
            alert.setContentText("Please add specialization Name");
            alert.showAndWait();
            return;
        }
        try {
            Connection con = new DatabaseConnection().getConnection();
            String sql = "INSERT INTO specialization (name) VALUES (?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
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
        AddspecField.clear();
    }
    private void refreshTable(){
        specList.clear();
        try{
            String sql = "SELECT * FROM specialization";
            Connection con = new DatabaseConnection().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Spec newSpec= new Spec(rs.getInt(1), rs.getString(2));
                specList.add(newSpec);
            }
            con.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
