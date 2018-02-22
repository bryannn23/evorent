package application;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Issued_Receipt implements Initializable {

	public DBconnection dbcon = new DBconnection();
	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	
	@FXML private TableView<TableRentPayment> tblIssuedReceipt;
	
	@FXML private Button btnIRAdd;
	@FXML private Button btnIREdit;
	@FXML private Button btnIRView;
	@FXML private Button btnIRDelete;
	
	public static String receipt, tenant, occp;
	
	Stage primaryStage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		tblCol.TableRentPayment(tblIssuedReceipt);
		btnIREdit.setDisable(true);
		btnIRView.setDisable(true);
		btnIRDelete.setDisable(true);
		

		tblIssuedReceipt.setStyle("-fx-table-cell-border-color: transparent; ");
		
		tblIssuedReceipt.setOnMouseClicked( e-> {
	        	
	            try {
	            	TableRentPayment selected = tblIssuedReceipt.getSelectionModel().getSelectedItem();
	                receipt = selected.getPayID();
	                tenant = selected.getOccpID();
	                occp = selected.getOccupancy();
	                
	                
	            } catch (Exception ex) {
	                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err receipt table : " + ex.getMessage());
	            }

	            if (receipt != null) {
	                btnIREdit.setDisable(false);
	                btnIRView.setDisable(false);
	                btnIRDelete.setDisable(false);
	                
	            }
	            
		});
	       
		
		try {
			refresh();
		} catch (SQLException e) {
			 EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err receipt table refresh: " + e.getMessage());
		}
		
	}

	public void refresh() throws SQLException {
		    
		tblIssuedReceipt.getItems().clear();
		dbcon.tblRentPayment(tblIssuedReceipt, " RENT_PAYMENT_DETAILS.REFERENCE_ID IS NULL AND "
				+ " RENT_PAYMENT_DETAILS.OCCP_ID = "+ Occupancy.occpID +" ");

	    }

	public void changeScreen (ActionEvent event, String typeOfButton, String filename ) {
		
		 if (typeOfButton == "TableRentPayment Edit" || typeOfButton == "TableRentPayment View") {
        	 
        	 if (receipt == null)
        		 return;
        	 
         }
		 
        try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            Parent root = (Parent) loader.load();

            if (typeOfButton == "TableRentPayment Edit") {
            	Create_Rent_Payment cont = loader.getController();
                cont.processEditOption(receipt, primaryStage);
                
            } else if (typeOfButton == "TableRentPayment View") {
            	Create_Rent_Payment cont = loader.getController();
                cont.processViewOption(receipt);
                cont.btnCancel.setOnAction( e-> primaryStage.close() );
            } 
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
            
            if (typeOfButton == "TableRentPayment Add" || typeOfButton == "TableRentPayment Edit"  ) {
            	
            	refresh();
            	
            }
        
        }
        catch (Exception e) {
        	EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen : " + typeOfButton + ": " + e.getMessage());
        }
    
}
	

    public void receiptCreate(ActionEvent event) {

    	//changeScreen(event, "TableRentPayment Add", "/application/CreatePayment.fxml");

    	try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/CreatePayment.fxml"));
            Parent root = (Parent) loader.load();

            Create_Payment cont = loader.getController();
            cont.processAddOption(primaryStage);
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            refresh();
            receipt = null; 
            

        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error rent payment : " + e.getMessage());
        } 
    	
      }

    public void receiptEdit(ActionEvent event) {

    	changeScreen(event, "TableRentPayment Edit", "/application/CreateRentPayment.fxml");
    	receipt = null;
    	
    }

    public void receiptView(ActionEvent event) {

    	changeScreen(event, "TableRentPayment View", "/application/CreateRentPayment.fxml");
    	receipt = null;
    	
     }

    public void receiptDelete (ActionEvent event) throws SQLException {
        
    	if (receipt == null)
    		return;
    	
    	modular.deletemes("RENT_PAYMENT_DETAILS", "REFERENCE_ID", receipt, "PAYMENT_RECEIPT_NO", "PAYMENT_ID");
    	refresh();
    	receipt = null;
    	
    }
    
}
