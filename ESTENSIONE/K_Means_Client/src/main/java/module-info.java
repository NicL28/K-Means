module kclient.k_means_client {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens kclient.k_means_client to javafx.fxml;
    exports kclient.k_means_client;
}