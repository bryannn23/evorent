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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Tenant_Detail implements Initializable {

	public DBconnection dbcon = new DBconnection();
	public TableColumns tblCol = new TableColumns();
	
	@FXML private TableView<TableOccupancyDetail> tblOccupancyHistory;
	@FXML private TableView<TableOccupancyDetail> tblCurrentOccupancy;
	
	@FXML private TextField txtMobileNo;
	@FXML private TextField txtLandline;
	@FXML private TextField txtGender;
	@FXML private TextField txtEAdd;
	@FXML private TextField txtBday;

	@FXML private TextArea txtRemarks;
	@FXML private TextArea txtAddress;
	
	@FXML private ImageView imgTDPhoto;
	
	@FXML private Label lblTenant;
	
	@FXML private Button btnOccupancyHistory;
	@FXML private Button btnCurrentOccupancy;
	@FXML private Button btnTDCreateAccount;
	
	public static String tenant, tenantName;
	
	Image image;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		txtAddress.setEditable(false);
		txtMobileNo.setEditable(false);
		txtLandline.setEditable(false);
		txtEAdd.setEditable(false);
		txtBday.setEditable(false);
		txtGender.setEditable(false);
		txtRemarks.setEditable(false);
		
		
		btnTDCreateAccount.setVisible(false);
		
		initializeTenantDetail();
		initialize_OccupancyDetailScreen();
		initialize_OccpHistoryScreen();
		
		imgTDPhoto.setOnMouseClicked( e-> {
			
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
	
	public void openfxml(ActionEvent event, String typeOfButton, String filename) {

		try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            Parent root = (Parent) loader.load();
            
            if (typeOfButton == "TENANTDETAIL EDIT") {
            	Create_Tenant cont = loader.getController();
                cont.processEditOption(tenant, primaryStage);
            
            } 
            else if (typeOfButton == "OCCUPANCY DETAIL VIEW") {
            	Occupancy cont = loader.getController();
            	
            }
            else if (typeOfButton == "OCCUPANCY HISTORY VIEW") {
            	Occupancy_Detail cont = loader.getController();
            	cont.occupancyHistory( primaryStage);
            	
            }
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            initializeTenantDetail();
            
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen : "+ typeOfButton +" " + e.getMessage());
        }
		
    }

	public void initializeTenantDetail() {
		
		if (Building_Detail.tenant != null) {
			
			tenant = Building_Detail.tenant;
			
			try {
				
				TenantDetailView(Building_Detail.tenant);
				
			} catch (SQLException e) {
				
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display tenant details : " +e.getMessage());
			}
		}
		else if (Building_Detail.occpTenant != null ){
			
			tenant = Building_Detail.occpTenant;
			
			try {
				
				TenantDetailView(Building_Detail.occpTenant);
				
			} catch (SQLException e) {
				
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display tenant details : " +e.getMessage());
			}
		}
		else if (Occupancy.tenant != null ) {

			tenant = Occupancy.tenant;
			
			try {
				
				TenantDetailView(Occupancy.tenant);
				
			} catch (SQLException e) {
				
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display tenant details : " +e.getMessage());
			}
		}
		
	}
	
	public void TenantDetailView ( String id ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM PRIMARY_TENANT_DETAILS WHERE TENANT_ID = "+id+" " ;
		
		String img, gen ;
		
		try{
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				lblTenant.setText(rs.getString("TENANT_NAME"));
				tenantName = rs.getString("TENANT_NAME");
				
				txtAddress.setText(rs.getString("TENANT_ADDRESS"));
				
				String mobile = rs.getString("TENANT_MOBILE_NO");
				if (mobile.equals("0")) mobile = "";
				txtMobileNo.setText(mobile);
				
				String landline = rs.getString("TENANT_LANDLINE_NO");
				if (landline.equals("0")) landline = "";
				txtLandline.setText(landline);
				
				txtEAdd.setText(rs.getString("TENANT_EMAIL_ADD"));
				txtBday.setText(rs.getString("TENANT_BDAY"));
				
				gen = rs.getString("TENANT_GENDER");
				
				if (gen == null) txtGender.setText("");
				else if (gen.equals("0")) txtGender.setText("MALE");
				else if (gen.equals("1")) txtGender.setText("FEMALE");
				
				txtRemarks.setText(rs.getString("TENANT_REMARKS"));
				
				img = rs.getString("TENANT_PHOTO");
                
                if (img != null) {
                    InputStream input = rs.getBinaryStream("TENANT_PHOTO");
                    OutputStream output = new FileOutputStream(new File ("src/picture/tenantPhoto.jpg"));
                    byte[] content = new byte[3000];
                    int size = 0;
                    
                    while ( (size = input.read(content)) != -1) {
                        output.write(content, 0, size);
                    }
                    
                    output.close();
                    input.close();
                    
                    image = new Image( "file:src/picture/tenantPhoto.jpg", 1000, 1500, true, true);
                    imgTDPhoto.setImage(image);
                }
                else {
                	imgTDPhoto.setImage(null);
                }
				 
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display tenant details : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void tenantEdit (ActionEvent event) {
		
		//openfxml(event, "TENANTDETAIL EDIT", "/application/CreateTenant.fxml");
		
		try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/CreateTenant.fxml"));
            Parent root = (Parent) loader.load();
            
            Create_Tenant cont = loader.getController();
            cont.processEditOption(tenant, primaryStage);
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            initializeTenantDetail();
            
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen tenant detail edit: " + e.getMessage());
        }
		
	}
	
	public void tenanctCreateAccount (ActionEvent event) {
		
		try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/CreateUser.fxml"));
            Parent root = (Parent) loader.load();
            
            Create_User cont = loader.getController();
            cont.processAddOption(primaryStage);
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
            
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen create user for tenant " + e.getMessage());
        }
	}
	
    public void initialize_OccupancyDetailScreen() {
    	
    	setupOccupancyDetail_TableView();
    }

    public void setupOccupancyDetail_TableView() {

        tblCol.TableOccupancyDetail(tblCurrentOccupancy);

        tblCurrentOccupancy.widthProperty().addListener(new ChangeListener<Number>() {

 			@Override
 			public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
 				// TODO Auto-generated method stub
 				 Pane header = (Pane) tblCurrentOccupancy.lookup("TableHeaderRow");
 	                if (header.isVisible()){
 	                    header.setMaxHeight(0);
 	                    header.setMinHeight(0);
 	                    header.setPrefHeight(0);
 	                    header.setVisible(false);
 	                }
 			}
         });
        
        tblCurrentOccupancy.setStyle("-fx-table-cell-border-color: transparent; ");
        
        try {
			dbcon.tblOccupancyDetail(tblCurrentOccupancy, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND"
					+ " OCCUPANCY_DETAILS.TENANT_ID = "+ tenant +" ");
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err occp CURRENT : " + e.getMessage());
		}
        
    }

    public void occpDetailViewAll(ActionEvent event) {

    	openfxml(event, "OCCUPANCY DETAIL VIEW", "/application/Occupancy.fxml");
    	
    }

    public void initialize_OccpHistoryScreen() {
      	
    	setupOccpHistory_TableView();
    	
      }

    public void setupOccpHistory_TableView() {

        tblCol.TableOccupancyDetail(tblOccupancyHistory);

        tblOccupancyHistory.widthProperty().addListener(new ChangeListener<Number>() {

 			@Override
 			public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
 				// TODO Auto-generated method stub
 				 Pane header = (Pane) tblOccupancyHistory.lookup("TableHeaderRow");
 	                if (header.isVisible()){
 	                    header.setMaxHeight(0);
 	                    header.setMinHeight(0);
 	                    header.setPrefHeight(0);
 	                    header.setVisible(false);
 	                }
 			}
         });
        
        tblOccupancyHistory.setStyle("-fx-table-cell-border-color: transparent; ");
        
        try {
			dbcon.tblOccupancyDetail(tblOccupancyHistory, " OCCUPANCY_DETAILS.TENANT_ID = "+ tenant +" ");
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err occp history : " + e.getMessage());
		}
      }

    public void occpHistoryViewAll(ActionEvent event) {

    	openfxml(event, "OCCUPANCY HISTORY VIEW", "/application/OccupancyDetail.fxml");
    	
    }

	

}
