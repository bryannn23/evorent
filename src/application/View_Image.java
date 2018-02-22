package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class View_Image implements Initializable {

    private Stage primaryStage;
    
    @FXML Button btnClose;
    @FXML ImageView imgViewImage;
    @FXML BorderPane bp;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
        
    }
    
    public void close (ActionEvent event) {
        
        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    public void processViewImage(Stage parent_primaryStage, Image img) {
        
        primaryStage = parent_primaryStage;
        
         imgViewImage.setImage(img);
         imgViewImage.setPreserveRatio(true); 
         imgViewImage.setSmooth(true);
        // imgViewImage.fitWidthProperty().bind(bp.widthProperty()); 
        // imgViewImage.fitHeightProperty().bind(bp.heightProperty());
    }
}
