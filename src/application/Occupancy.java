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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Occupancy implements Initializable {

	public DBconnection dbcon = new DBconnection();
	public TableColumns tblCol = new TableColumns();
	
	@FXML private TableView<TableBillingStatement> tblTDBilling;
	@FXML private TableView<TableRentPayment> tblTDReceipt;
	
	@FXML private TextArea txtTDFellowTenants;
	@FXML private TextArea txtTDRemarks;
	@FXML private TextField txtEndLease;
	@FXML private TextField txtNoOfTenants;
	@FXML private TextField txtStartLease;
	@FXML private TextField txtStatusOccp;
	@FXML private TextField txtTDAssociationFee;
	@FXML private TextField txtTDBillingCycle;
	@FXML private TextField txtTDDeposit;
	@FXML private TextField txtTDRemDeposit;
	@FXML private TextField txtTDRentAmount;
	@FXML private TextField txtUnitDelivery;
	
	@FXML private ImageView imgTDPhoto;
	
	@FXML private Label lblTenant;
	
	@FXML private Button btnTDAddBilling;
	@FXML private Button btnTDReceipt;
	@FXML private Button btnTDViewBilling;
	@FXML private Button btnTDViewTenant;
	@FXML private Button btnTDViewReceipt;
	@FXML private Button btnTDViewUnit;
	@FXML private Button btnODGenerateBilling;
	
	public static String billing, receipt, tenant, occpID;
	
	Stage primaryStage;
	
	Image image;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		txtUnitDelivery.setEditable(false);
		txtStartLease.setEditable(false);
		txtEndLease.setEditable(false);
		txtNoOfTenants.setEditable(false);
		txtTDFellowTenants.setEditable(false);
		txtTDBillingCycle.setEditable(false);
		txtTDRentAmount.setEditable(false);
		txtTDAssociationFee.setEditable(false);
 		txtTDDeposit.setEditable(false);
		txtTDRemDeposit.setEditable(false);
		txtTDRemarks.setEditable(false);
		
		
		initialize_ReceiptScreen();
		initialize_BillingScreen();
		
		try {
			refresh();
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err refresh billing and receipt table : " +e.getMessage());
		}
		
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
		
	public void refresh () throws SQLException {

		if (Building_Detail.occpDetail != null ) {
			
			tenant = dbcon.getIDs("*", "OCCUPANCY_DETAILS", "TENANT_ID", "OCCP_ID = "+ Building_Detail.occpDetail +" ");
			occpID = Building_Detail.occpDetail;
			
			try {
				
				OccupancyDetailView( "OCCUPANCY_DETAILS.OCCP_ID = " +Building_Detail.occpDetail+ " ");
				
			} catch (SQLException e) {
				
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display occp details : " +e.getMessage());
			}
			
		}
		else if (Building_Detail.unitDetails != null) {
			
			tenant = dbcon.getIDs("*", "OCCUPANCY_DETAILS", "TENANT_ID", "UNIT_ID = "+ Building_Detail.unitDetails +" AND "
					+ "REFERENCE_ID IS NULL ");
			
			occpID = dbcon.getIDs("*", "OCCUPANCY_DETAILS", "OCCP_ID", "UNIT_ID = "+ Building_Detail.unitDetails +" AND "
					+ "REFERENCE_ID IS NULL ");
					
			try {	
				
				OccupancyDetailView( "OCCUPANCY_DETAILS.UNIT_ID = " +Building_Detail.unitDetails+ " AND "
						+ "OCCUPANCY_DETAILS.REFERENCE_ID IS NULL ");
				
			} catch (SQLException e) {
				
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display occp details : " +e.getMessage());
			}
		}
		else if (Building_Detail.tenant != null) {
			
			tenant = Building_Detail.tenant;
			
			occpID = dbcon.getIDs("*", "OCCUPANCY_DETAILS", "OCCP_ID", "TENANT_ID = "+ Building_Detail.tenant+" AND "
					+ "REFERENCE_ID IS NULL ");
		}
		
		
		tblTDBilling.getItems().clear();
		dbcon.tblBillingStatement(tblTDBilling, " BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
				+ "BILLING_STATEMENT_DETAILS.OCCP_ID = "+ occpID +" ");
		
		tblTDReceipt.getItems().clear();
		dbcon.tblRentPayment(tblTDReceipt, " RENT_PAYMENT_DETAILS.REFERENCE_ID IS NULL AND "
				+ " RENT_PAYMENT_DETAILS.OCCP_ID = "+ occpID +" " );
		
	}
	
	public void openfxml(ActionEvent event, String typeOfButton, String filename) {

	        try {
	            Stage primaryStage = new Stage();
	            primaryStage.initModality(Modality.APPLICATION_MODAL);
	            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
	            Parent root = (Parent) loader.load();

	            if (typeOfButton == "TableRentPayment Add") {
	            	Create_Payment cont = loader.getController();
	            	cont.processAddOption(primaryStage);
	            	
	            } else if (typeOfButton == "TableBillingStatement Add") {
	            	Create_Billing_Statement cont = loader.getController();
	            	cont.processAddOption(primaryStage);
	            	
	            } else if (typeOfButton == "TableOccpDetail Edit" ) {
	            	Create_Occupancy_Detail cont = loader.getController();
	            	cont.processEditOption(Building_Detail.occpDetail, primaryStage);
	            	
	            } else if (typeOfButton == "Generate Billing Statement") {
	            	Billing_Voucher cont = loader.getController();
	            	cont.processAddOption(primaryStage);
	            	
	            }
	            
	            Scene scene = new Scene(root);
	            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
	            primaryStage.setScene(scene);
	            primaryStage.setMaximized(true);
	            primaryStage.showAndWait();

	            if ( typeOfButton == "TableRentPayment Add" || typeOfButton == "TableBillingStatement Add" 
	            		|| typeOfButton == "TableOccpDetail Edit" || typeOfButton == "TenantDetail View" 
	            		|| typeOfButton == "UnitDetail View" || typeOfButton == "Generate Billing Statement" ) {
	            	
	            	refresh();
	            	
	            }
	            
	        } catch (Exception e) {
	            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen : " + typeOfButton + ": " + e.getMessage());
	        }
	    }
	
	 public void occpDetailEdit (ActionEvent event) {

         openfxml(event, "TableOccpDetail Edit", "/application/CreateOccupancyDetail.fxml");
         
   }

	public void OccupancyDetailView ( String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT OCCUPANCY_DETAILS.*, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT, "
				+ " UNIT_DETAILS.UNIT_ASSOC_AMOUNT AS ASSOC, UNIT_DETAILS.UNIT_NAME AS UNIT, "
				+ "UNIT_DETAILS.UNIT_PHOTO AS PHOTO "
				+ " FROM OCCUPANCY_DETAILS "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE "+condi+" " ;
		
		String img, del, stat, start, end ;
		
		byte[] fileBytes;
		String file; 
		String fileName, contractName = null;
		boolean doc = false, pdf = false;
		
		try{
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				lblTenant.setText(rs.getString("UNIT") + " - " + rs.getString("TENANT"));
				
				del = rs.getString("OCCP_DELIVERY");
				
				if (del == null) txtUnitDelivery.setText("");
				else if (del.equals("0")) txtUnitDelivery.setText("Bare");
				else if (del.equals("1")) txtUnitDelivery.setText("Partially Furnished");
				else if (del.equals("2")) txtUnitDelivery.setText("Furnished");
				else if (del.equals("3")) txtUnitDelivery.setText("Fully Furnished");
				
				start = rs.getString("OCCP_START");
				
				if (start == null) start = "";
				txtStartLease.setText(start);
				
				end = rs.getString("OCCP_END");
				
				if (end == null) end = "";
				txtEndLease.setText(end);
				
				stat = rs.getString("OCCP_STATUS");
				
				if (stat == null) txtStatusOccp.setText("");
				else if (stat.equals("0")) txtStatusOccp.setText("New Tenant");
				else if (stat.equals("1")) txtStatusOccp.setText("Unit Transfer");
				else if (stat.equals("2")) txtStatusOccp.setText("Building Transfer");
				else if (stat.equals("3")) txtStatusOccp.setText("End of Contract");
				else if (stat.equals("4")) txtStatusOccp.setText("Renewed Contract");
				else if (stat.equals("5")) txtStatusOccp.setText("Others");
				
				txtNoOfTenants.setText(rs.getString("OCCP_NO_OF_TENANT"));
				txtTDFellowTenants.setText(rs.getString("OCCP_FELLOW_TENANT"));
				txtTDBillingCycle.setText(rs.getString("OCCP_BILLING_CYCLE"));
				txtTDRentAmount.setText(rs.getString("OCCP_RENT_AMOUNT"));
				txtTDAssociationFee.setText(rs.getString("ASSOC"));
				
				txtTDDeposit.setText(rs.getString("OCCP_DEPOSIT"));
				txtTDRemDeposit.setText(rs.getString("OCCP_REM_DEPOSIT"));
				txtTDRemarks.setText(rs.getString("OCCP_REMARKS"));
				
				img = rs.getString("PHOTO");
                
                if (img != null) {
                    InputStream input = rs.getBinaryStream("PHOTO");
                    OutputStream output = new FileOutputStream(new File ("src/picture/unitContractPhoto.jpg"));
                    byte[] content = new byte[3000];
                    int size = 0;
                    
                    while ( (size = input.read(content)) != -1) {
                        output.write(content, 0, size);
                    }
                    
                    output.close();
                    input.close();
                    
                    image = new Image( "file:src/picture/unitContractPhoto.jpg", 1000, 1500, true, true);
                    imgTDPhoto.setImage(image);
                }
                else {
                	imgTDPhoto.setImage(null);
                }
				 
                fileName = rs.getString("OCCP_CONTRACT_NAME");

				if (fileName == null) {
					fileName = "";
				}
				else if (fileName.endsWith(".docx")) {
					
					doc = true;
					
				}
				else if (fileName.endsWith(".pdf")) {
					
					pdf = true;
				}
				
				if (fileName.equals("")) contractName = "src/picture/contractFile.docx";
				
				else if (doc == true) contractName = "src/picture/contractFile.docx";
				
				else if (pdf == true )
					contractName = "src/picture/contractFile.pdf";
					
				file = rs.getString("OCCP_CONTRACT_PHOTO");
				
	            if (file != null ) {
	            	
	            	fileBytes = rs.getBytes("OCCP_CONTRACT_PHOTO");
	                OutputStream targetFile=  new FileOutputStream( contractName);
	                file = contractName;

	                targetFile.write(fileBytes);
	                targetFile.close();
	                
	            }
	            else {
	                file = null;
	            }
				
				 
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display occupancy details : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

    public void initialize_ReceiptScreen() {
    	
    	 tblCol.TableRentPayment(tblTDReceipt);
    	 tblTDReceipt.setStyle("-fx-table-cell-border-color: transparent; ");
    	 
    }

    public void receiptCreate(ActionEvent event) {

        openfxml(event, "TableRentPayment Add", "/application/CreatePayment.fxml");
    }
    
    public void receiptViewAll(ActionEvent event) {

    	try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Receipt.fxml"));
            Parent root = (Parent) loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            refresh();
            
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen receipt: " + e.getMessage());
        }
    	
    }

    public void initialize_BillingScreen() {
      	
    	 tblCol.TableBillingStatement(tblTDBilling);
    	 tblTDBilling.setStyle("-fx-table-cell-border-color: transparent; ");
    	 
    }

    public void BillingCreate(ActionEvent event) {

        openfxml(event, "TableBillingStatement Add", "/application/CreateBillingStatement.fxml");
     
    }
    
    public void GenerateBilling (ActionEvent event) {
    	
    	openfxml(event, "Generate Billing Statement", "/application/BillingVoucher.fxml");
    	
    }

    public void BillingViewAll(ActionEvent event) {

    	try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/BillingStatement.fxml"));
            Parent root = (Parent) loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            refresh();
            
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen billing: " + e.getMessage());
        }
    	
    }

    public void unitDetail(ActionEvent event) {

       openfxml(event, "UnitDetail View", "/application/UnitDetails.fxml");
    }

    public void tenantDetail(ActionEvent event) {

         openfxml(event, "TenantDetail View", "/application/TenantDetail.fxml");
     }

 
	

}
