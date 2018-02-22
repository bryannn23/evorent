package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Unit_Details implements Initializable {

	public DBconnection dbcon = new DBconnection();
	public TableColumns tblCol = new TableColumns();
	
	@FXML private TableView<TableUnitMaintenance> tblUDUnitMaintenance;
	@FXML private TableView<TableUnitIssue> tblUDUnitIssue;
	
	@FXML private TextField txtUDArea;
	@FXML private TextField txtUDAssociationFee;
	@FXML private TextField txtUDBath;
	@FXML private TextField txtUDBedroom;
	@FXML private TextField txtUDBldgName;
	@FXML private TextField txtUDCapacity;
	@FXML private TextField txtUDCondition;
	@FXML private TextField txtUDFeatures;
	@FXML private TextField txtUDFloor;
	@FXML private TextField txtUDLocation;
	@FXML private TextField txtUDRent;
	@FXML private TextField txtUDType;
	
	@FXML private ImageView imgUDPhoto;
	
	@FXML private Label lblUnit;
	@FXML private Label lblUDUnitIssue;
	@FXML private Label lblUDUnitMaintenance;
	@FXML private Label lblUDArea;
	@FXML private Label lblUDAssociationFee;
	
	@FXML private Button btnUDAddMxAct;
	@FXML private Button btnUDAddIssue;
	@FXML private Button btnUDViewAct;
	@FXML private Button btnUDViewIssue;
	@FXML private Button btnUODHistory;
	@FXML private Button btnUODView;
	
	public static String issue, maintenance, unitName, unitDetails;
	
	Image image;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		txtUDBldgName.setEditable(false);
		txtUDFloor.setEditable(false);
		txtUDLocation.setEditable(false);
		txtUDType.setEditable(false);
		txtUDArea.setEditable(false);
		txtUDFeatures.setEditable(false);
		txtUDBedroom.setEditable(false);
		txtUDBath.setEditable(false);
		txtUDCapacity.setEditable(false);
		txtUDCondition.setEditable(false);
		txtUDRent.setEditable(false);
		txtUDAssociationFee.setEditable(false);
		
		
		initialize_UnitIssueScreen();
		initialize_UnitMaintenanceScreen();
		
		try {
			refresh();
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error refresh unit issue and maintenance : " + e.getMessage());
		}
		
		imgUDPhoto.setOnMouseClicked( e-> {
			
			viewImage(image);
			
		});
		
	}

	public void viewImage (Image img) {
		
		try {
		     Stage primaryStage = new Stage();
		     primaryStage.initModality(Modality.APPLICATION_MODAL);
		     FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View_Image.fxml"));
		     Parent root = (Parent) loader.load();

		     View_Image cont = loader.getController();
		     cont.processViewImage(primaryStage, img);
		     cont.btnClose.setOnAction( e1 -> primaryStage.close());
		            
		     Scene scene = new Scene(root);
		     scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
		     primaryStage.setScene(scene);
		     primaryStage.showAndWait();

		 } catch (Exception e1) {
		     EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error view image : " + e1.getMessage());
		 }
		
	}
	
	public void refresh () throws SQLException {
		
		if (Building_Detail.unitDetails != null) {
			
			unitDetails = Building_Detail.unitDetails;
			try {
				
				UnitView(Building_Detail.unitDetails);
				
			} catch (SQLException e) {
				
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display unit details : " +e.getMessage());
			}
		}
		else {
			
			try {
				
				UnitView(Building_Detail.occpUnit);
				unitDetails = Building_Detail.occpUnit;
				
			} catch (SQLException e) {
				
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display unit details : " +e.getMessage());
			}
		}
		
		tblUDUnitMaintenance.getItems().clear();
		dbcon.tblUnitMaintenance(tblUDUnitMaintenance, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND "
				+ " UNIT_MAINTENANCE.UNIT_ID = "+ Building_Detail.unitDetails +" " );
		
		tblUDUnitIssue.getItems().clear();
		dbcon.tblUnitIssue(tblUDUnitIssue, " UNIT_ISSUES.REFERENCE_ID IS NULL AND UNIT_ISSUES.UNIT_ID = "+ Building_Detail.unitDetails +" " );
		
	}
	
	public void openfxml(ActionEvent event, String typeOfButton, String filename) {

		String unit ;
		
		if (Building_Detail.unitDetails != null) unit = Building_Detail.unitDetails;
		else unit = Building_Detail.occpUnit;
		
	        try {
	            Stage primaryStage = new Stage();
	            primaryStage.initModality(Modality.APPLICATION_MODAL);
	            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
	            Parent root = (Parent) loader.load();

	            if (typeOfButton == "TableUnitIssue Add") {
	            	Create_Unit_Issue cont = loader.getController();
	            	cont.processAddOption(primaryStage);
	            	
	            } else if (typeOfButton == "TableUnitMaintenance Add") {
	            	Create_Unit_Maintenance cont = loader.getController();
	            	cont.processAddOption(primaryStage);
	            	
	            } else if (typeOfButton == "TableUnitDetail Edit" ) {
	            	Create_Unit_Detail cont = loader.getController();
	            	cont.processEditOption(unit, primaryStage);
	            
	            } else if (typeOfButton == "OCCUPANCY HISTORY") {
	            	Occupancy_Detail cont = loader.getController();
	            	cont.occupancyHistory(primaryStage);
	            	
	            }
	            
	            
	            Scene scene = new Scene(root);
	            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
	            primaryStage.setScene(scene);
	            primaryStage.showAndWait();

	            if ( typeOfButton == "TableUnitIssue Add" || typeOfButton == "TableUnitMaintenance Add" 
	            		|| typeOfButton == "TableUnitDetail Edit" || typeOfButton == "OCCUPANCY HISTORY" ) {
	            	
	            	refresh();
	            	
	            }
	            
	        } catch (Exception e) {
	            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen : " + typeOfButton + ": " + e.getMessage());
	        }
	    }

	public void mainfxml (ActionEvent event, String filename ) {
		
		try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            Parent root = (Parent) loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            refresh();
            	
            
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen "+ filename +": " + e.getMessage());
        }
		
	}
	
    public void unitDetailEdit (ActionEvent event) {

          openfxml(event, "TableUnitDetail Edit", "/application/CreateUnitDetail.fxml");
          
    }

	public void UnitView ( String id ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT UNIT_DETAILS.*, BUILDING_DETAILS.BLDG_NAME AS BLDG, "
				+ "BUILDING_DETAILS.BLDG_FLOORS AS FLOORS, UNIT_TYPE.UNIT_TYPE_NAME AS TYPE "
				+ " FROM UNIT_DETAILS "
				+ "LEFT JOIN BUILDING_DETAILS ON BUILDING_DETAILS.BLDG_ID = UNIT_DETAILS.BLDG_ID "
				+ "LEFT JOIN UNIT_TYPE ON UNIT_TYPE.UNIT_TYPE_ID = UNIT_DETAILS.UNIT_TYPE_ID "
				+ " WHERE UNIT_DETAILS.UNIT_ID = "+id+" " ;
		
		String img, loc, feature, condi ;
		
		try{
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				unitName = rs.getString("UNIT_NAME");
				lblUnit.setText(rs.getString("UNIT_NAME"));
				txtUDBldgName.setText(rs.getString("BLDG"));
				txtUDFloor.setText(rs.getString("FLOORS"));
				
				loc = rs.getString("UNIT_LOCATION");
				
				if (loc.equals("0")) txtUDLocation.setText("Not Applicable");
				else if (loc.equals("1")) txtUDLocation.setText("North");
				else if (loc.equals("2")) txtUDLocation.setText("South");
				else if (loc.equals("3")) txtUDLocation.setText("East");
				else if (loc.equals("4")) txtUDLocation.setText("West");
				else if (loc.equals("5")) txtUDLocation.setText("North East");
				else if (loc.equals("6")) txtUDLocation.setText("North West");
				else if (loc.equals("7")) txtUDLocation.setText("South East");
				else if (loc.equals("8")) txtUDLocation.setText("South West");
				
				txtUDType.setText(rs.getString("TYPE"));
				txtUDArea.setText(rs.getString("UNIT_AREA"));
				
				feature = rs.getString("UNIT_FEATURES");
				
				if (feature.equals("0")) txtUDFeatures.setText("Bare");
				else if (feature.equals("1")) txtUDFeatures.setText("Partially Furnished");
				else if (feature.equals("2")) txtUDFeatures.setText("Furnished");
				else if (feature.equals("3")) txtUDFeatures.setText("Fully Furnished");
				
				txtUDBedroom.setText(rs.getString("UNIT_BEDROOM"));
				txtUDBath.setText(rs.getString("UNIT_BATHROOM"));
				txtUDCapacity.setText(rs.getString("UNIT_MAX_CAPACITY"));
				
				condi = rs.getString("UNIT_CONDITION");
				
				if (condi.equals("0")) txtUDCondition.setText("NEW");
				else if (condi.equals("1")) txtUDCondition.setText("WELL MAINTAINED");
				else if (condi.equals("2")) txtUDCondition.setText("NEEDS RENOVATION");
				else if (condi.equals("3")) txtUDCondition.setText("UNDER RENOVATION");
				else if (condi.equals("4")) txtUDCondition.setText("RENOVATED");
				else if (condi.equals("5")) txtUDCondition.setText("OTHERS");
				
				txtUDRent.setText(rs.getString("UNIT_RENT_AMOUNT"));
				txtUDAssociationFee.setText(rs.getString("UNIT_ASSOC_AMOUNT"));
				
				img = rs.getString("UNIT_PHOTO");
                
                if (img != null) {
                    InputStream input = rs.getBinaryStream("UNIT_PHOTO");
                    OutputStream output = new FileOutputStream(new File ("src/picture/udPhoto.jpg"));
                    byte[] content = new byte[3000];
                    int size = 0;
                    
                    while ( (size = input.read(content)) != -1) {
                        output.write(content, 0, size);
                    }
                    
                    output.close();
                    input.close();
                    
                    image = new Image( "file:src/picture/udPhoto.jpg", 1000, 1500, true, true);
                    imgUDPhoto.setImage(image);
                }
                else {
                	imgUDPhoto.setImage(null);
                }
				 
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display unit details : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

    public void initialize_UnitIssueScreen() {
    	
    	tblCol.TableUnitIssue(tblUDUnitIssue);
    	tblUDUnitIssue.setStyle("-fx-table-cell-border-color: transparent; ");
    	
    }

    public void unitIssueCreate(ActionEvent event) {

        openfxml(event, "TableUnitIssue Add", "/application/CreateUnitIssue.fxml");
    }
    
    public void unitIssueViewAll(ActionEvent event) {

    	try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UnitIssues.fxml"));
            Parent root = (Parent) loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            refresh();
            
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen unit issues: " + e.getMessage());
        }
    	
    }

    public void initialize_UnitMaintenanceScreen() {
      	
    	 tblCol.TableUnitMaintenance(tblUDUnitMaintenance);
    	 tblUDUnitMaintenance.setStyle("-fx-table-cell-border-color: transparent; ");
    	 
      }

    public void unitMaintenanceCreate(ActionEvent event) {

          openfxml(event, "TableUnitMaintenance Add", "/application/CreateUnitMaintenance.fxml");
      }

    public void unitMaintenanceViewAll(ActionEvent event) {

    	try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UnitMaintenance.fxml"));
            Parent root = (Parent) loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            refresh();
            
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen unit maintenance: " + e.getMessage());
        }
    	
    }

    public void occpDetail(ActionEvent event) {

       mainfxml(event, "/application/Occupancy.fxml");
    }

    public void occpDetailHistory(ActionEvent event) {

         openfxml(event, "OCCUPANCY HISTORY", "/application/OccupancyDetail.fxml");
     }

 
	

}
