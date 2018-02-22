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

public class Unit_Issues implements Initializable {

	public DBconnection dbcon = new DBconnection();
	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();
	
	@FXML private TableView<TableUnitIssue> tblUnitIssue;
	
	@FXML private Button btnUIAdd;
	@FXML private Button btnUIEdit;
	@FXML private Button btnUIView;
	@FXML private Button btnUIDelete;
	
	@FXML private ComboBox<String> cmbStatus;
	@FXML private Button btnGenerate;
	String status;
	
	public static String unitIssue;
	
	Stage primaryStage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		comboBox.unitIssue(cmbStatus);
		cmbStatus.getItems().add("All");
		
		btnUIEdit.setDisable(true);
		btnUIView.setDisable(true);
		btnUIDelete.setDisable(true);
		
		tblCol.TableUnitIssue(tblUnitIssue);
		
		tblUnitIssue.setStyle("-fx-table-cell-border-color: transparent; ");
		
		tblUnitIssue.setOnMouseClicked( e-> {
	        	
	            try {
	            	TableUnitIssue selected = tblUnitIssue.getSelectionModel().getSelectedItem();
	                unitIssue = selected.getUnitIssue();
	                
	            } catch (Exception ex) {
	                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit unitIssue table : " + ex.getMessage());
	            }

	            if (unitIssue != null) {
	                btnUIEdit.setDisable(false);
	                btnUIView.setDisable(false);
	                btnUIDelete.setDisable(false);
	            }
	            
		});
		
		
		try {
			refresh();
		} catch (SQLException e) {
			 EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit unitIssue table refresh : " + e.getMessage());
		}
		
	}

	public void setval() {

		if (cmbStatus.getValue() == null)
			status = null;
		else if (cmbStatus.getValue().equalsIgnoreCase("For Scheduling"))
			status = "1";
		else if (cmbStatus.getValue().equalsIgnoreCase("On-going Maintenance"))
			status = "2";
		else if (cmbStatus.getValue().equalsIgnoreCase("On-hold"))
			status = "3";
		else if (cmbStatus.getValue().equalsIgnoreCase("Cancelled"))
			status = "4";
		else if (cmbStatus.getValue().equalsIgnoreCase("Completed"))
			status = "5";
		else if (cmbStatus.getValue().equalsIgnoreCase("Others"))
			status = "6";
		
	}

	public void generate (ActionEvent event) throws SQLException {
		
		setval();
		
		if (cmbStatus.getValue().equals("All")) {
		    
			tblUnitIssue.getItems().clear();
			dbcon.tblUnitIssue(tblUnitIssue, " UNIT_ISSUES.REFERENCE_ID IS NULL AND UNIT_ISSUES.UNIT_ID = "+ Unit_Details.unitDetails +" ");
			
		}
		else {
		    
			tblUnitIssue.getItems().clear();
			dbcon.tblUnitIssue(tblUnitIssue, " UNIT_ISSUES.REFERENCE_ID IS NULL AND UNIT_ISSUES.UNIT_ID = "+ Unit_Details.unitDetails +" "
					+ " AND UNIT_ISSUES.UNIT_ISSUE_STATUS = "+ status +" ");
			
		}
	}
 	
	public void refresh() throws SQLException {
		    
		tblUnitIssue.getItems().clear();
		dbcon.tblUnitIssue(tblUnitIssue, " UNIT_ISSUES.REFERENCE_ID IS NULL AND UNIT_ISSUES.UNIT_ID = "+ Unit_Details.unitDetails +" ");
		
	    }

	public void changeScreen (ActionEvent event, String typeOfButton, String filename ) {
		
		 if (typeOfButton == "TableUnitIssue Edit" || typeOfButton == "TableUnitIssue View") {
        	 
        	 if (unitIssue == null)
        		 return;
        	 
         }
		 
        try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            Parent root = (Parent) loader.load();

            if (typeOfButton == "TableUnitIssue Add") {
                Create_Unit_Issue cont = loader.getController();
                cont.processAddOption(primaryStage);
                
            } else if (typeOfButton == "TableUnitIssue Edit") {
            	Create_Unit_Issue cont = loader.getController();
                cont.processEditOption(unitIssue, primaryStage);
                
            } else if (typeOfButton == "TableUnitIssue View") {
            	Create_Unit_Issue cont = loader.getController();
                cont.processViewOption(unitIssue);
                
            } 
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
            
            if (typeOfButton == "TableUnitIssue Add" || typeOfButton == "TableUnitIssue Edit"  ) {
            	
            	refresh();
            	
            }
        
        }
        catch (Exception e) {
        	EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen : " + typeOfButton + ": " + e.getMessage());
        }
    
}
	
	public void unitIssueCreate(ActionEvent event) {

		changeScreen(event, "TableUnitIssue Add", "/application/CreateUnitIssue.fxml");
    }

    public void unitIssueEdit(ActionEvent event) {

    	changeScreen(event, "TableUnitIssue Edit", "/application/CreateUnitIssue.fxml");
    }

    public void unitIssueView(ActionEvent event) {

    	changeScreen(event, "TableUnitIssue View", "/application/CreateUnitIssue.fxml");
    }
    
    public void unitIssueDelete (ActionEvent event) throws SQLException {
        
    	if (unitIssue == null)
    		return;
    	
    	modular.deletemes("UNIT_ISSUES", "REFERENCE_ID", unitIssue, "UNIT_ISSUE_REMARLS", "UNIT_ISSUE_ID");
    	refresh();
    	unitIssue = null;
    	
    }

}
