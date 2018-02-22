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

public class Building_Maintenance implements Initializable {

	public DBconnection dbcon = new DBconnection();
	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();
	
	@FXML private TableView<TableBuildingMaintenance> tblMaintenanceActivity;
	
	@FXML private Button btnMAAdd;
	@FXML private Button btnMAEdit;
	@FXML private Button btnMAView;
	
	public static String maintenance;
	
	Stage primaryStage;
	
	@FXML private ComboBox<String> cmbActivity;
	@FXML private ComboBox<String> cmbStatus;
	@FXML private Button btnGenerate;
	String status, activity;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		comboBox.bldgMxActivity(cmbActivity);
		cmbActivity.getItems().add("All");
		comboBox.bldgMXStatus(cmbStatus);
		
		cmbActivity.setOnAction( e-> {
			
			cmbStatus.getItems().clear();
			comboBox.bldgMXStatus(cmbStatus);
		});
		
		tblCol.TableBuildingMaintenance(tblMaintenanceActivity);
		
		tblMaintenanceActivity.setStyle("-fx-table-cell-border-color: transparent; ");
		
		tblMaintenanceActivity.setOnMouseClicked( e-> {
	        	
	            try {
	            	TableBuildingMaintenance selected = tblMaintenanceActivity.getSelectionModel().getSelectedItem();
	                maintenance = selected.getMxID();
	                
	            } catch (Exception ex) {
	                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err bldg maintenance table : " + ex.getMessage());
	            }

	            if (maintenance != null) {
	                btnMAEdit.setDisable(false);
	                btnMAView.setDisable(false);
	            }
	            
		});
	       
		
		btnMAEdit.setDisable(true);
		btnMAView.setDisable(true);
		
		try {
			refresh();
		} catch (SQLException e) {
			 EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err bldg maintenance table refresh: " + e.getMessage());
		}
		
	}
	
	public void setval () throws SQLException {
		
		if (cmbActivity.getValue() == null)
			activity = null;
		else if (cmbActivity.getValue().equalsIgnoreCase("Others Maintenance")) 
			activity = "0";
		else if (cmbActivity.getValue().equalsIgnoreCase("Building Interior Inspection"))
			activity = "1";
		else if (cmbActivity.getValue().equalsIgnoreCase("Building Exterior Inspection"))
			activity = "2";
		else if (cmbActivity.getValue().equalsIgnoreCase("General Unit Inspection"))
			activity = "3";
		else if (cmbActivity.getValue().equalsIgnoreCase("Sidings (Wash, Re-paint, Waterproofing)"))
			activity = "4";
		else if (cmbActivity.getValue().equalsIgnoreCase("Windows (Wash, Re-caulk)"))
			activity = "5";
		else if (cmbActivity.getValue().equalsIgnoreCase("Doors (Wash, Re-paint)"))
			activity = "6";
		else if (cmbActivity.getValue().equalsIgnoreCase("Decks and Stairs (Clean, Paint)"))
			activity = "7";
		else if (cmbActivity.getValue().equalsIgnoreCase("Plumbing"))
			activity = "8";
		else if (cmbActivity.getValue().equalsIgnoreCase("Electrical (Maintenance)"))
			activity = "9";
		else if (cmbActivity.getValue().equalsIgnoreCase("Pest Control"))
			activity = "10";
		
		else if (cmbActivity.getValue().equalsIgnoreCase("Others Repairs and Fixtures"))
			activity = "11";
		else if (cmbActivity.getValue().equalsIgnoreCase("Interior Makeover")) 
			activity = "12";
		else if (cmbActivity.getValue().equalsIgnoreCase("Signage (Clean, Repair)"))
			activity = "13";
		else if (cmbActivity.getValue().equalsIgnoreCase("Lighting (Clean, Change)")) 
			activity = "14";
		else if (cmbActivity.getValue().equalsIgnoreCase("Roof (Clean, Waterproofing, Leaks, Repairs)"))
			activity = "15";
		else if (cmbActivity.getValue().equalsIgnoreCase("Foundation (Repair)"))
			activity = "16";
		else if (cmbActivity.getValue().equalsIgnoreCase("Gutter (Clean, Repair, Re-paint"))
			activity = "17";
		
		if (cmbStatus.getValue() == null)
			status = null;
		else if (cmbStatus.getValue().equalsIgnoreCase("Scheduled for Maintenance"))
			status = "0";
		else if (cmbStatus.getValue().equalsIgnoreCase("Waiting for Contractor"))
			status = "1";
		else if (cmbStatus.getValue().equalsIgnoreCase("On-Going Maintenance"))
			status = "2";
		else if (cmbStatus.getValue().equalsIgnoreCase("Still/Terminated Maintenance"))
			status = "3";
		else if (cmbStatus.getValue().equalsIgnoreCase("Maintenance Completed"))
			status = "4";
			
	}

	public void generate (ActionEvent event ) throws SQLException {
		
		setval();
		
		if (cmbActivity.getValue().equals("All") && cmbStatus.getValue() == null ) {
			
			tblMaintenanceActivity.getItems().clear();
			dbcon.tblBuildingMaintenance(tblMaintenanceActivity, " BUILDING_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "BUILDING_MAINTENANCE.BLDG_ID = "+ Main_Window.dashboard +" " );
		}
		else if (cmbActivity.getValue().equals("All") && cmbStatus.getValue() != null ) {
			
			tblMaintenanceActivity.getItems().clear();
			dbcon.tblBuildingMaintenance(tblMaintenanceActivity, " BUILDING_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "BUILDING_MAINTENANCE.BLDG_ID = "+ Main_Window.dashboard +" AND "
					+ "BUILDING_MAINTENANCE.BLDG_MX_STATUS = "+ status +" " );
		}
		else if (cmbStatus.getValue() == null && cmbActivity.getValue() != null ) {
		    
			tblMaintenanceActivity.getItems().clear();
			dbcon.tblBuildingMaintenance(tblMaintenanceActivity, " BUILDING_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "BUILDING_MAINTENANCE.BLDG_ID = "+ Main_Window.dashboard +" AND "
					+ "BUILDING_MAINTENANCE.BLDG_MX_ACTIVITY = "+ activity +" " );

		}
		else if (cmbStatus.getValue() != null && cmbActivity.getValue() == null ) {

			tblMaintenanceActivity.getItems().clear();
			dbcon.tblBuildingMaintenance(tblMaintenanceActivity, " BUILDING_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "BUILDING_MAINTENANCE.BLDG_ID = "+ Main_Window.dashboard +" AND "
					+ "BUILDING_MAINTENANCE.BLDG_MX_STATUS = "+ status +" " );

		}
		else if (cmbStatus.getValue() != null && cmbActivity.getValue() != null ) {

			tblMaintenanceActivity.getItems().clear();
			dbcon.tblBuildingMaintenance(tblMaintenanceActivity, " BUILDING_MAINTENANCE.REFERENCE_ID IS NULL AND "
					+ "BUILDING_MAINTENANCE.BLDG_ID = "+ Main_Window.dashboard +" AND "
					+ "BUILDING_MAINTENANCE.BLDG_MX_STATUS = "+ status +" AND "
					+ " BUILDING_MAINTENANCE.BLDG_MX_ACTIVITY = "+ activity +" " );

		}
		
	}
	
	public void refresh() throws SQLException {
		    
		tblMaintenanceActivity.getItems().clear();
		dbcon.tblBuildingMaintenance(tblMaintenanceActivity, " BUILDING_MAINTENANCE.REFERENCE_ID IS NULL AND "
				+ "BUILDING_MAINTENANCE.BLDG_ID = "+ Main_Window.dashboard +" " );

	    }

	public void changeScreen (ActionEvent event, String typeOfButton, String filename ) {
		
		 if (typeOfButton == "TableBuildingMaintenance Edit" || typeOfButton == "TableBuildingMaintenance View") {
        	 
        	 if (maintenance == null)
        		 return;
        	 
         }
		 
        try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            Parent root = (Parent) loader.load();

            if (typeOfButton == "TableBuildingMaintenance Add") {
                Create_Building_Maintenance cont = loader.getController();
                cont.processAddOption(primaryStage);
                
            } else if (typeOfButton == "TableBuildingMaintenance Edit") {
            	Create_Building_Maintenance cont = loader.getController();
                cont.processEditOption(maintenance, primaryStage);
                
            } else if (typeOfButton == "TableBuildingMaintenance View") {
            	Create_Building_Maintenance cont = loader.getController();
                cont.processViewOption(maintenance);
                
            } 
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
            
            if (typeOfButton == "TableBuildingMaintenance Add" || typeOfButton == "TableBuildingMaintenance Edit"  ) {
            	
            	refresh();
            	
            }
        
        }
        catch (Exception e) {
        	EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen : " + typeOfButton + ": " + e.getMessage());
        }
    
}
	

    public void bldgMaintenanceCreate(ActionEvent event) {

    	changeScreen(event, "TableBuildingMaintenance Add", "/application/CreateMaintenanceActivity.fxml");
      }

    public void bldgMaintenanceEdit(ActionEvent event) {

    	changeScreen(event, "TableBuildingMaintenance Edit", "/application/CreateMaintenanceActivity.fxml");
    }

    public void bldgMaintenanceView(ActionEvent event) {

    	changeScreen(event, "TableBuildingMaintenance View", "/application/CreateMaintenanceActivity.fxml");
     }

}
