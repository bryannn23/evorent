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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Occupancy_Detail implements Initializable {

	public DBconnection dbcon = new DBconnection();
	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	
	
	@FXML private TableView<TableOccupancyDetail> tblOccupancyDetail;
	
	@FXML private Button btnAdd;
	@FXML private Button btnEdit;
	@FXML private Button btnView;
	@FXML private Button btnDelete;

	public static String occpDetail;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		btnEdit.setDisable(true);
		btnView.setDisable(true);
		
		tblCol.TableOccupancyDetail(tblOccupancyDetail);

		tblOccupancyDetail.setStyle("-fx-table-cell-border-color: transparent; ");
		
		tblOccupancyDetail.setOnMouseClicked( e-> {
	        	
	            try {
	            	TableOccupancyDetail selected = tblOccupancyDetail.getSelectionModel().getSelectedItem();
	                occpDetail = selected.getOccpID();
	                
	            } catch (Exception ex) {
	                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err occp occpDetail table : " + ex.getMessage());
	            }

	            if (occpDetail != null) {
	                btnEdit.setDisable(false);
	                btnView.setDisable(false);
	            }
	            
		});
	     
	}

	public void changeScreen (ActionEvent event, String typeOfButton, String filename ) {
		
		 if (typeOfButton == "TableOccupancyDetail Edit" || typeOfButton == "TableOccupancyDetail View") {
        	 
        	 if (occpDetail == null)
        		 return;
        	 
         }
		 
        try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            Parent root = (Parent) loader.load();

            if (typeOfButton == "TableOccupancyDetail Add") {
                Create_Occupancy_Detail cont = loader.getController();
                cont.processAddOption(primaryStage);
                
            } else if (typeOfButton == "TableOccupancyDetail Edit") {
            	Create_Occupancy_Detail cont = loader.getController();
                cont.processEditOption(occpDetail, primaryStage);
                
            } else if (typeOfButton == "TableOccupancyDetail View") {
            	Create_Occupancy_Detail cont = loader.getController();
                cont.processViewOption(occpDetail);
                
            } 
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
            
            if (typeOfButton == "TableOccupancyDetail Add" || typeOfButton == "TableOccupancyDetail Edit"  ) {
            	
            	refresh();
            	
            }
        
        }
        catch (Exception e) {
        	EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen : " + typeOfButton + ": " + e.getMessage());
        }
    
}
	
	public void refresh() throws SQLException {
		
		tblOccupancyDetail.getItems().clear();
		
		if (Building_Detail.unitDetails != null) {
				
			dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND "
				+ "OCCUPANCY_DETAILS.UNIT_ID = "+ Building_Detail.unitDetails +" ");
		}
		else if (Building_Detail.tenant != null) {
				
			dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND "
				+ "OCCUPANCY_DETAILS.TENANT_ID = "+ Building_Detail.tenant +" ");
		}
			
	}
	
	public void occpCreate(ActionEvent event) {

		changeScreen(event, "TableOccupancyDetail Add", "/application/CreateOccupancyDetail.fxml");
    }

    public void occpEdit(ActionEvent event) {

    	changeScreen(event, "TableOccupancyDetail Edit", "/application/CreateOccupancyDetail.fxml");
    }

    public void occpView(ActionEvent event) {

    	changeScreen(event, "TableOccupancyDetail View", "/application/CreateOccupancyDetail.fxml");
    }

    public void occpDelete (ActionEvent event) throws SQLException {
    
    	if (occpDetail == null)
    		return;
    	
    	modular.deletemes("OCCUPANCY_DETAILS", "REFERENCE_ID", occpDetail, "OCCP_REMARKS", "OCCP_ID");
    	refresh();
    	occpDetail = null;
    	
    }


	public void currentOccupancy (Stage parent_primaryStage) throws SQLException {

		tblOccupancyDetail.getItems().clear();
			
		if (Building_Detail.unitDetails != null) {
				
			dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND "
				+ "OCCUPANCY_DETAILS.UNIT_ID = "+ Building_Detail.unitDetails +" ");
		}
		else if (Building_Detail.tenant != null) {
				
			dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND "
				+ "OCCUPANCY_DETAILS.TENANT_ID = "+ Building_Detail.tenant +" ");
		}
			
	}

	public void occupancyHistory( Stage parent_primaryStage) throws SQLException {
	    
		btnAdd.setVisible(false);
		btnEdit.setVisible(false);
		btnView.setVisible(false);
		btnDelete.setVisible(false);
		
		tblOccupancyDetail.getItems().clear();
		
		TableColumn<TableOccupancyDetail, String> tcref = new TableColumn<>("REFERENCE");
		tcref.setCellValueFactory(new PropertyValueFactory<>("refid"));
		
		tcref.setCellFactory(column -> {
			return new TableCell<TableOccupancyDetail, String>() {
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
						setText(null);
						this.getTableRow().setStyle("");
					} else {
						setText("EDITED");
						this.getTableRow().setStyle("-fx-background-color: #d1b07b");
						
					}
				}
			};
		});	
		tblOccupancyDetail.getColumns().add(tcref);
			
		if (Building_Detail.unitDetails != null) {
				
			dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.UNIT_ID = "+ Building_Detail.unitDetails +" ");
		}
		else if (Building_Detail.tenant != null) {
				
			dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.TENANT_ID = "+ Building_Detail.tenant +" ");
		}
		else if (Building_Detail.occpDetail != null ) {
			
			dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.OCCP_ID = "+ Building_Detail.occpDetail+" OR "
					+ "OCCUPANCY_DETAILS.REFERENCE_ID = "+ Building_Detail.occpDetail +" ");
		}
		
	}
    
}
