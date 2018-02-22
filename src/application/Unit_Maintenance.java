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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Unit_Maintenance implements Initializable {

	public DBconnection dbcon = new DBconnection();
	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();
	
	@FXML private TableView<TableUnitMaintenance> tblUnitMaintenance;
	
	@FXML private Button btnUMAdd;
	@FXML private Button btnUMEdit;
	@FXML private Button btnUMView;
	
	public static String maintenance;
	
	Stage primaryStage;

	@FXML private ComboBox<String> cmbActivity;
	@FXML private ComboBox<String> cmbStatus;
	@FXML private ComboBox<String> cmbRequest;
	@FXML private Button btnGenerate;
	String status, activity, request;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		comboBox.unitMxActivity(cmbActivity);
		cmbActivity.getItems().add("All");
		comboBox.unitMXStatus(cmbStatus);
		comboBox.unitRequest(cmbRequest);
		
		cmbActivity.setOnAction( e-> {
			
			cmbStatus.getItems().clear();
			cmbRequest.getItems().clear();
			
			comboBox.unitMXStatus(cmbStatus);
			comboBox.unitRequest(cmbRequest);
			
		});
		
		tblCol.TableUnitMaintenance(tblUnitMaintenance);
		btnUMEdit.setDisable(true);
		btnUMView.setDisable(true);
		
		try {
			refresh();
		} catch (SQLException e) {
			 EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit maintenance table refresh: " + e.getMessage());
		}
		
	}
	
	public void setval () throws SQLException {

		if (cmbActivity.getValue() == null)
			activity = null;
		else if (cmbActivity.getValue().equalsIgnoreCase("Others Maintenance")) 
			activity = "0";
		else if (cmbActivity.getValue().equalsIgnoreCase("Unit Interior Inspection"))
			activity = "1";
		else if (cmbActivity.getValue().equalsIgnoreCase("Unit Exterior Inspection"))
			activity = "2";
		else if (cmbActivity.getValue().equalsIgnoreCase("Walls (Wash, Re-paint)"))
			activity = "3";
		else if (cmbActivity.getValue().equalsIgnoreCase("Windows (Wash, Re-caulk)"))
			activity = "4";
		else if (cmbActivity.getValue().equalsIgnoreCase("Doors (Wash, Re-paint)"))
			activity = "5";
		else if (cmbActivity.getValue().equalsIgnoreCase("Plumbing (Maintenance)"))
			activity = "6";
		else if (cmbActivity.getValue().equalsIgnoreCase("Electrical (Maintenance)"))
			activity = "7";
		else if (cmbActivity.getValue().equalsIgnoreCase("Pest Control"))
			activity = "8";
		
		else if (cmbActivity.getValue().equalsIgnoreCase("Others Repairs and Fixtures"))
			activity = "11";
		else if (cmbActivity.getValue().equalsIgnoreCase("Interior Makeover"))
			activity = "12";
		else if (cmbActivity.getValue().equalsIgnoreCase("Lighting (Clean, Change)"))
			activity = "13";
		else if (cmbActivity.getValue().equalsIgnoreCase("Foundation (Repair)"))
			activity = "14";
		else if (cmbActivity.getValue().equalsIgnoreCase("Furniture (Repair, Change)"))
			activity = "15";
		else if (cmbActivity.getValue().equalsIgnoreCase("Appliances (Repair, Change)"))
			activity = "16";
		
		if (cmbStatus.getValue() == null)
			status = null;
		else if (cmbStatus.getValue().equalsIgnoreCase("Scheduled for Maintenance"))
			status = "0";
		else if (cmbStatus.getValue().equalsIgnoreCase("Waiting for Contractor"))
			status = "1";
		else if (cmbStatus.getValue().equalsIgnoreCase("Undergoing Maintenance"))
			status = "2";
		else if (cmbStatus.getValue().equalsIgnoreCase("Still/Terminated Maintenance"))
			status = "3";
		else if (cmbStatus.getValue().equalsIgnoreCase("Maintenance Completed"))
			status = "4";
		

		if (cmbRequest.getValue() == null)
			request = null;
		else if (cmbRequest.getValue().equalsIgnoreCase("Admin Initiative"))
			request = "0";
		else if (cmbRequest.getValue().equalsIgnoreCase("Tenant Request"))
			request = "1";
		
			
	}

	public void generate (ActionEvent event ) throws SQLException {
		
		setval();
		
		if (cmbActivity.getValue().equals("All") && cmbStatus.getValue() == null && cmbRequest.getValue() == null ) {

			tblUnitMaintenance.getItems().clear();
			dbcon.tblUnitMaintenance(tblUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "UNIT_MAINTENANCE.UNIT_ID = "+ Unit_Details.unitDetails +" " );

		}
		else if (cmbActivity.getValue().equals("All") && cmbStatus.getValue() != null && cmbRequest.getValue() == null ) {

			tblUnitMaintenance.getItems().clear();
			dbcon.tblUnitMaintenance(tblUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "UNIT_MAINTENANCE.UNIT_ID = "+ Unit_Details.unitDetails +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_STATUS = "+ status +" " );

		}
		else if (cmbActivity.getValue().equals("All") && cmbStatus.getValue() == null && cmbRequest.getValue() != null ) {

			tblUnitMaintenance.getItems().clear();
			dbcon.tblUnitMaintenance(tblUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "UNIT_MAINTENANCE.UNIT_ID = "+ Unit_Details.unitDetails +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_REQUEST = "+ request +" " );

		}
		else if (cmbActivity.getValue().equals("All") && cmbStatus.getValue() != null && cmbRequest.getValue() != null ) {

			tblUnitMaintenance.getItems().clear();
			dbcon.tblUnitMaintenance(tblUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "UNIT_MAINTENANCE.UNIT_ID = "+ Unit_Details.unitDetails +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_STATUS = "+ status +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_REQUEST = "+ request +" " );

		}
		else if (cmbStatus.getValue() == null && cmbActivity.getValue() != null && cmbRequest.getValue() == null ) {
		    
			tblUnitMaintenance.getItems().clear();
			dbcon.tblUnitMaintenance(tblUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "UNIT_MAINTENANCE.UNIT_ID = "+ Unit_Details.unitDetails +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_ACTIVITY = "+ activity +" " );

		}
		else if (cmbStatus.getValue() != null && cmbActivity.getValue() == null && cmbRequest.getValue() == null ) {

			tblUnitMaintenance.getItems().clear();
			dbcon.tblUnitMaintenance(tblUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "UNIT_MAINTENANCE.UNIT_ID = "+ Unit_Details.unitDetails +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_STATUS = "+ status +" " );

		}
		else if (cmbStatus.getValue() == null && cmbActivity.getValue() == null && cmbRequest.getValue() != null ) {

			tblUnitMaintenance.getItems().clear();
			dbcon.tblUnitMaintenance(tblUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "UNIT_MAINTENANCE.UNIT_ID = "+ Unit_Details.unitDetails +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_REQUEST = "+ request +" " );
		}
		else if (cmbStatus.getValue() != null && cmbActivity.getValue() != null && cmbRequest.getValue() == null) {

			tblUnitMaintenance.getItems().clear();
			dbcon.tblUnitMaintenance(tblUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "UNIT_MAINTENANCE.UNIT_ID = "+ Unit_Details.unitDetails +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_ACTIVITY = "+ activity +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_STATUS = "+ status +" " );

		}
		else if (cmbStatus.getValue() != null && cmbActivity.getValue() == null && cmbRequest.getValue() != null ) {

			tblUnitMaintenance.getItems().clear();
			dbcon.tblUnitMaintenance(tblUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "UNIT_MAINTENANCE.UNIT_ID = "+ Unit_Details.unitDetails +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_STATUS = "+ status +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_REQUEST = "+ request +" " );

		}
		else if (cmbStatus.getValue() == null && cmbActivity.getValue() != null && cmbRequest.getValue() != null ) {

			tblUnitMaintenance.getItems().clear();
			dbcon.tblUnitMaintenance(tblUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "UNIT_MAINTENANCE.UNIT_ID = "+ Unit_Details.unitDetails +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_REQUEST = "+ request +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_ACTIVITY = "+ activity +" " );

		}
		else if (cmbStatus.getValue() != null && cmbActivity.getValue() != null && cmbRequest.getValue() != null ) {

			tblUnitMaintenance.getItems().clear();
			dbcon.tblUnitMaintenance(tblUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "UNIT_MAINTENANCE.UNIT_ID = "+ Unit_Details.unitDetails +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_ACTIVITY = "+ activity +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_STATUS = "+ status +" AND "
					+ " UNIT_MAINTENANCE.UNIT_MX_REQUEST = "+ request +" " );

		}
		
	}

	public void refresh() throws SQLException {
		    
		tblUnitMaintenance.getItems().clear();
		dbcon.tblUnitMaintenance(tblUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
				+ "UNIT_MAINTENANCE.UNIT_ID = "+ Unit_Details.unitDetails +" " );

		tblUnitMaintenance.setStyle("-fx-table-cell-border-color: transparent; ");
		
		tblUnitMaintenance.setOnMouseClicked( e-> {
	        	
	            try {
	            	TableUnitMaintenance selected = tblUnitMaintenance.getSelectionModel().getSelectedItem();
	                maintenance = selected.getMxID();
	                
	            } catch (Exception ex) {
	                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit maintenance table : " + ex.getMessage());
	            }

	            if (maintenance != null) {
	                btnUMEdit.setDisable(false);
	                btnUMView.setDisable(false);
	            }
	            
		});
	       
	    }

	public void changeScreen (ActionEvent event, String typeOfButton, String filename ) {
		
		 if (typeOfButton == "TableUnitMaintenance Edit" || typeOfButton == "TableUnitMaintenance View") {
        	 
        	 if (maintenance == null)
        		 return;
        	 
         }
		 
        try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            Parent root = (Parent) loader.load();

            if (typeOfButton == "TableUnitMaintenance Add") {
                Create_Unit_Maintenance cont = loader.getController();
                cont.processAddOption(primaryStage);
                
            } else if (typeOfButton == "TableUnitMaintenance Edit") {
            	Create_Unit_Maintenance cont = loader.getController();
                cont.processEditOption(maintenance, primaryStage);
                
            } else if (typeOfButton == "TableUnitMaintenance View") {
            	Create_Unit_Maintenance cont = loader.getController();
                cont.processViewOption(maintenance);
                
            } 
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
            
            if (typeOfButton == "TableUnitMaintenance Add" || typeOfButton == "TableUnitMaintenance Edit"  ) {
            	
            	refresh();
            	
            }
        
        }
        catch (Exception e) {
        	EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen : " + typeOfButton + ": " + e.getMessage());
        }
    
}
	

    public void unitMaintenanceCreate(ActionEvent event) {

    	changeScreen(event, "TableUnitMaintenance Add", "/application/CreateUnitMaintenance.fxml");
      }

    public void unitMaintenanceEdit(ActionEvent event) {

    	changeScreen(event, "TableUnitMaintenance Edit", "/application/CreateUnitMaintenance.fxml");
    }

    public void unitMaintenanceView(ActionEvent event) {

    	changeScreen(event, "TableUnitMaintenance View", "/application/CreateUnitMaintenance.fxml");
     }

    public void unitMaintenanceDelete (ActionEvent event) throws SQLException {
        
    	if (maintenance == null)
    		return;
    	
    	modular.deletemes("UNIT_MAINTENANCE", "REFERENCE_ID", maintenance, "UNIT_MX_REMARLS", "UNIT_MX_ID");
    	refresh();
    	maintenance = null;
    	
    }

    
}
