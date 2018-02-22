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

public class Building_Issues implements Initializable {

	public DBconnection dbcon = new DBconnection();
	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();
	
	@FXML private TableView<TableBuildingIssue> tblBuildingIssue;
	
	@FXML private Button btnBIAdd;
	@FXML private Button btnBIEdit;
	@FXML private Button btnBIView;
	
	@FXML private ComboBox<String> cmbStatus;
	@FXML private Button btnGenerate;
	String status;
	
	public static String issue;
	
	Stage primaryStage;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		comboBox.bldgIssue(cmbStatus);
		cmbStatus.getItems().add("All");
		btnGenerate.setDisable(true);
		
		cmbStatus.setOnAction( e-> {
			
			btnGenerate.setDisable(false);
		});
		
		tblCol.TableBuildingIssue(tblBuildingIssue);
		btnBIEdit.setDisable(true);
		btnBIView.setDisable(true);
		
		tblBuildingIssue.setStyle("-fx-table-cell-border-color: transparent; ");
		
		tblBuildingIssue.setOnMouseClicked( e-> {
	        	
	            try {
	            	TableBuildingIssue selected = tblBuildingIssue.getSelectionModel().getSelectedItem();
	                issue = selected.getBldgIssue();
	                
	            } catch (Exception ex) {
	                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err bldg issue table : " + ex.getMessage());
	            }

	            if (issue != null) {
	                btnBIEdit.setDisable(false);
	                btnBIView.setDisable(false);
	            }
	            
		});
		
		try {
			refresh();
		} catch (SQLException e) {
			 EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err bldg issue table refresh : " + e.getMessage());
		}
		
	}

	public void setval() {
		
		if (cmbStatus.getValue() == null )
			status = null;
		else if (cmbStatus.getValue().equalsIgnoreCase("Reported")) 
			status = "0";
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
		
			tblBuildingIssue.getItems().clear();
			dbcon.tblBuildingIssue(tblBuildingIssue, " BUILDING_ISSUES.REFERENCE_ID IS NULL AND "
					+ " BUILDING_ISSUES.BLDG_ID = "+ Main_Window.dashboard +" ");
		}
		else {
			
			tblBuildingIssue.getItems().clear();
			dbcon.tblBuildingIssue(tblBuildingIssue, " BUILDING_ISSUES.REFERENCE_ID IS NULL AND "
					+ " BUILDING_ISSUES.BLDG_ID = "+ Main_Window.dashboard +" AND "
					+ " BUILDING_ISSUES.BLDG_ISSUE_STATUS = "+ status +" ");
			
		}
		       
	}
	
	public void refresh() throws SQLException {
		    
		tblBuildingIssue.getItems().clear();
		dbcon.tblBuildingIssue(tblBuildingIssue, " BUILDING_ISSUES.REFERENCE_ID IS NULL AND BUILDING_ISSUES.BLDG_ID = "+ Main_Window.dashboard +" ");
	       
	    }

	public void changeScreen (ActionEvent event, String typeOfButton, String filename ) {
		
		 if (typeOfButton == "TableBuildingIssue Edit" || typeOfButton == "TableBuildingIssue View") {
        	 
        	 if (issue == null)
        		 return;
        	 
         }
		 
        try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            Parent root = (Parent) loader.load();

            if (typeOfButton == "TableBuildingIssue Add") {
                Create_Building_Issue cont = loader.getController();
                cont.processAddOption(primaryStage);
                
            } else if (typeOfButton == "TableBuildingIssue Edit") {
            	Create_Building_Issue cont = loader.getController();
                cont.processEditOption(issue, primaryStage);
                
            } else if (typeOfButton == "TableBuildingIssue View") {
            	Create_Building_Issue cont = loader.getController();
                cont.processViewOption(issue);
                
            } 
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
            
            if (typeOfButton == "TableBuildingIssue Add" || typeOfButton == "TableBuildingIssue Edit"  ) {
            	
            	refresh();
            	
            }
        
        }
        catch (Exception e) {
        	EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen : " + typeOfButton + ": " + e.getMessage());
        }
    
}
	
	public void bldgIssueCreate(ActionEvent event) {

		changeScreen(event, "TableBuildingIssue Add", "/application/CreateBuildingIssue.fxml");
    }

    public void bldgIssueEdit(ActionEvent event) {

    	changeScreen(event, "TableBuildingIssue Edit", "/application/CreateBuildingIssue.fxml");
    }

    public void bldgIssueView(ActionEvent event) {

    	changeScreen(event, "TableBuildingIssue View", "/application/CreateBuildingIssue.fxml");
    }

}
