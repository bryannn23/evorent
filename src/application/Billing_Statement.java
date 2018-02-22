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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Billing_Statement implements Initializable {

	public DBconnection dbcon = new DBconnection();
	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	
	@FXML private TableView<TableBillingStatement> tblBillingStatement;
	
	@FXML private Button btnBSAdd;
	@FXML private Button btnBSEdit;
	@FXML private Button btnBSView;
	@FXML private Button btnBSDelete;
	
	public static String billing, billingno, billingTenant, billingOccp;
	
	Stage primaryStage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		tblCol.TableBillingStatement(tblBillingStatement);

		tblBillingStatement.setStyle("-fx-table-cell-border-color: transparent; ");
		
		tblBillingStatement.setOnMouseClicked( e-> {
	        	
	            try {
	            	TableBillingStatement selected = tblBillingStatement.getSelectionModel().getSelectedItem();
	                billing = selected.getStateID();
	                billingno = selected.getStateNo();
	                billingTenant = selected.getOccpID();
	                billingOccp = selected.getStateOccp();
	                
	            } catch (Exception ex) {
	                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit billing table : " + ex.getMessage());
	            }

	            if (billing != null) {
	                btnBSEdit.setDisable(false);
	                btnBSView.setDisable(false);
	                btnBSDelete.setDisable(false);
	                
	            }
	            
		});
	       
		btnBSEdit.setDisable(true);
		btnBSView.setDisable(true);
		btnBSDelete.setDisable(true);
		
		try {
			refresh();
		} catch (SQLException e) {
			 EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing table refresh: " + e.getMessage());
		}
		
	}

	public void refresh() throws SQLException {
		    
		tblBillingStatement.getItems().clear();
		dbcon.tblBillingStatement(tblBillingStatement, " BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
				+ "BILLING_STATEMENT_DETAILS.OCCP_ID = "+ Occupancy.occpID +" " );

	    }

	public void changeScreen (ActionEvent event, String typeOfButton, String filename ) {
		
		 if (typeOfButton == "TableBillingStatement Edit" || typeOfButton == "TableBillingStatement View") {
        	 
        	 if (billing == null)
        		 return;
        	 
         }
		 
        try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            Parent root = (Parent) loader.load();

            if (typeOfButton == "TableBillingStatement Add") {
                Billing_Voucher cont = loader.getController();
                cont.processAddOption(primaryStage);
                
            } else if (typeOfButton == "TableBillingStatement Edit") {
            	Create_Billing_Statement cont = loader.getController();
                cont.processEditOption(billing, primaryStage);
                
            } else if (typeOfButton == "TableBillingStatement View") {
            	Create_Billing_Statement cont = loader.getController();
                cont.processViewOption(billing);
                cont.btnCancel.setOnAction( e-> primaryStage.close() );
            } 
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
            
            if (typeOfButton == "TableBillingStatement Add" || typeOfButton == "TableBillingStatement Edit"  ) {
            	
            	refresh();
            	
            }
        
        }
        catch (Exception e) {
        	EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen : " + typeOfButton + ": " + e.getMessage());
        }
    
}
	
    public void billingCreate(ActionEvent event) {

    	//changeScreen(event, "TableBillingStatement Add", "/application/BillingVoucher.fxml");
    	
    	  try {
              Stage primaryStage = new Stage();
              primaryStage.initModality(Modality.APPLICATION_MODAL);
              FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/BillingVoucher.fxml"));
              Parent root = (Parent) loader.load();

              Billing_Voucher cont = loader.getController();
              cont.processAddOption(primaryStage);
              
              Scene scene = new Scene(root);
              scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
              primaryStage.setScene(scene);
              primaryStage.setMaximized(true);
              primaryStage.showAndWait();
              
              refresh();
              billing = null;

          } catch (Exception e) {
              EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error billing statement : " + e.getMessage());
          } 
      }

    public void billingEdit(ActionEvent event) {

    	changeScreen(event, "TableBillingStatement Edit", "/application/CreateBillingStatement.fxml");
    	billing = null;
    	
    }

    public void billingView(ActionEvent event) {

    	changeScreen(event, "TableBillingStatement View", "/application/CreateBillingStatement.fxml");
    	billing = null;
    	
     }

    public void billingDelete (ActionEvent event) throws SQLException {
    
    	if (billing == null)
    		return;
    	
    	modular.deletemes("BILLING_STATEMENT_DETAILS", "REFERENCE_ID", billing, "STATEMENT_NUMBER", "STATEMENT_ID");
    	refresh();
    	billing = null;
    	
    }

}
