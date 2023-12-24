module com.example.dbfinalproject {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.dbfinalproject to javafx.fxml;
    exports com.example.dbfinalproject;
}