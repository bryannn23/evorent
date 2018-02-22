package application;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.sun.javafx.fxml.builder.JavaFXImageBuilder;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Create_Occupancy_Detail implements Initializable {
	
	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();

	@FXML private TextField txtBillingCycle;
	@FXML private TextField txtRentAmount;
	@FXML private TextField txtDeposit;
	@FXML private TextField txtRemainingDeposit;
	@FXML private TextField txtNoOfTenant;
	@FXML private TextField txtFile;
	
	@FXML private TextArea txtRemarks;
	@FXML private TextArea txtFellowTenant;
	
	@FXML private DatePicker dpStart;
	@FXML private DatePicker dpEnd;
	
	@FXML private ComboBox<String> cmbTenant;
	@FXML private ComboBox<String> cmbUnit;
	@FXML private ComboBox<String> cmbDelivery;
	@FXML private ComboBox<String> cmbODStatus;
	
	@FXML private Label lblTenant;
	@FXML private Label lblUnit;
	@FXML private Label lblDelivery;
	@FXML private Label lblStart;
	@FXML private Label lblEnd;
	@FXML private Label lblBillingCycle;
	@FXML private Label lblRentAmount;
	@FXML private Label lblODStatus;
	@FXML private Label lblDeposit;
	@FXML private Label lblRemainingDeposit;
	@FXML private Label lblNoOfTenant;
	@FXML private Label lblFellowTenant;
	@FXML private Label lblRemarks;
	@FXML private Label lblPhoto;
	@FXML private Label lblMessage;
	
	@FXML private TableView<TableOccupancyDetail> tblFinalize;
	@FXML private VBox vb;
	
	@FXML Button btnCancel;
	@FXML private Button btnUpdate;
	@FXML private Button btnCreate;
	@FXML private Button btnUpdateM;
	@FXML private Button btnFinalize;
	@FXML private Button btnChoosePhoto;
	@FXML private Button btnOpenFile;
	
	private Stage primaryStage;
	String tenant , unit, delivery, status, start, end, occpDetail, finalName, fName;
	
	FileChooser fileChooser = new FileChooser();
    File file;
    FileInputStream getFile;
    byte[] pdfData ;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnUpdate.setVisible(false);
		btnUpdateM.setVisible(false);
		btnCreate.setVisible(false);
		btnFinalize.setDisable(true);
		
		txtFile.setEditable(false);

		lblTenant.setVisible(false);
		lblUnit.setVisible(false);
		lblDelivery.setVisible(false);
		lblStart.setVisible(false);
		lblEnd.setVisible(false);
		lblBillingCycle.setVisible(false);
		lblRentAmount.setVisible(false);
		lblODStatus.setVisible(false);
		lblDeposit.setVisible(false);
		lblRemainingDeposit.setVisible(false);
		lblNoOfTenant.setVisible(false);
		lblFellowTenant.setVisible(false);
		lblRemarks.setVisible(false);
		lblPhoto.setVisible(false);
		lblMessage.setVisible(false);

		TextFormatter<String> tfbill = modular.getTextFlexiFormatter(2, 'n'); 
		txtBillingCycle.setTextFormatter(tfbill);
		TextFormatter<String> tfrent = modular.getTextFlexiFormatter(12, 'n');
		txtRentAmount.setTextFormatter(tfrent);
		TextFormatter<String> tfdepo = modular.getTextFlexiFormatter(12, 'n');
		txtDeposit.setTextFormatter(tfdepo);
		TextFormatter<String> tfremdepo = modular.getTextFlexiFormatter(12, 'n');
		txtRemainingDeposit.setTextFormatter(tfremdepo);
		TextFormatter<String> tften = modular.getTextFlexiFormatter(2, 'n');
		txtNoOfTenant.setTextFormatter(tften);
		TextFormatter<String> tfrem = modular.getTextFlexiFormatter(1000, 'a');
		txtRemarks.setTextFormatter(tfrem);
		
		comboBox.unitFeatures(cmbDelivery);
		comboBox.occpStatus(cmbODStatus);
		
		try {
			
			dbcon.cmbDisplay(cmbTenant, "*", "TENANT_NAME", "PRIMARY_TENANT_DETAILS", "REFERENCE_ID IS NULL "
			+ "AND TENANT_ID NOT IN "
			+ " ( SELECT OCCUPANCY_DETAILS.TENANT_ID FROM OCCUPANCY_DETAILS  "
			+ " LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
			+ " WHERE OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND OCCUPANCY_DETAILS.OCCP_STATUS <> 3 AND "
			+ " UNIT_DETAILS.BLDG_ID <> "+ Main_Window.dashboard +" ) "
			+ " GROUP BY TENANT_ID" );
			
			dbcon.cmbDisplay(cmbUnit, "UNIT_DETAILS.*", "UNIT_NAME", "UNIT_DETAILS "
					+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.UNIT_ID = UNIT_DETAILS.UNIT_ID ", 
					"UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
							+ "AND OCCUPANCY_DETAILS.REFERENCE_ID IS NULL "
							+ "AND ( OCCUPANCY_DETAILS.UNIT_ID IS NULL || OCCUPANCY_DETAILS.OCCP_STATUS = 3) " );
			
		} catch (SQLException e1) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error display combo box list : " + e1.getMessage());
		}
		
		/*imgPhoto.setOnMouseClicked( ea-> {
			
			   if (imgPhoto.getImage() != null) {
				   
			        try {
			            Stage primaryStage = new Stage();
			            primaryStage.initModality(Modality.APPLICATION_MODAL);
			            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View_Image.fxml"));
			            Parent root = (Parent) loader.load();

			            View_Image cont = loader.getController();
			            cont.processViewImage(primaryStage, image);
			            cont.btnClose.setOnAction( e -> primaryStage.close());
			            
			            Scene scene = new Scene(root);
			            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
			            primaryStage.setScene(scene);
			            primaryStage.showAndWait();

			        } catch (Exception e) {
			            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error view image : " + e.getMessage());
			        } }
			        else {
			           
			        	lblPhoto.setVisible(true);
			            lblMessage.setVisible(true);
			            lblMessage.setText("No uploaded image.");
			            return;
			        }
		});*/
		
		cmbUnit.setOnAction( e-> {
			
			try {
				dbcon.occpPrice(cmbUnit, txtRentAmount, cmbDelivery);
			} catch (SQLException e1) {
				 EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error get price and delivery of "+ cmbUnit.getValue() +" : " + e1.getMessage());
			}
		});
	
		new AutoCompleteComboBoxListener<String>(cmbTenant);
		new AutoCompleteComboBoxListener<String>(cmbUnit);
		new AutoCompleteComboBoxListener<String>(cmbDelivery);
		new AutoCompleteComboBoxListener<String>(cmbODStatus);
	
		setupOccpDetail_TableView();
		
	}

	public void setupOccpDetail_TableView() {

		tblCol.TableOccupancyDetail(tblFinalize);

		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableOccupancyDetail selected = tblFinalize.getSelectionModel().getSelectedItem();
				occpDetail = selected.getOccpID();
				occpView("OCCUPANCY_DETAILS.OCCP_ID = "+ occpDetail + " ");
				//occpView(occpDetail);
				
			} catch (Exception ex) {
			}
			if (occpDetail != null) {
				btnCreate.setVisible(false);
				btnUpdate.setVisible(true);
			} else
				return;
		});
	}
	
	public void chooseFile (ActionEvent event) throws IOException {

	        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Text Files", "*.pdf", "*.docx" ) ;
	        
	        fileChooser.getExtensionFilters().addAll(extFilter);
	        file = fileChooser.showOpenDialog(primaryStage);
	        
	        txtFile.setText(file.getAbsolutePath());
	        //finalName = file.getPath();
	        finalName = file.getName();
	        
	        pdfData = new byte[(int) file.length()];
	        DataInputStream dis = new DataInputStream(new FileInputStream(file));
	        dis.readFully(pdfData);  
	        dis.close();   
	        
	    }

	public void openFile(ActionEvent event) {
		
		if (txtFile.getText().isEmpty()) {
			
			lblPhoto.setVisible(true);
			lblMessage.setVisible(true);
			lblMessage.setText("No file to open.");
		}
		else {
			
			lblPhoto.setVisible(false);
			lblMessage.setVisible(false);
			
			if (Desktop.isDesktopSupported())
		     {
		      try
		      {
		       Desktop.getDesktop().open(file);
		      }
		      catch (IOException e)
		      {

		}
		     }}
		
	}
	
	public void create(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbTenant.getValue() != null && cmbUnit.getValue() != null )
		{
		
			setval();
			
			if (dpStart.getValue() == null && dpEnd.getValue() == null ) {
				
				modular.createWithFileFinal(" OCCUPANCY_DETAILS ", " UNIT_ID = "+ unit +" AND REFERENCE_ID IS NULL ", 
						lblMessage, 
						" 0, "+ tenant +", "+ unit +", "+ delivery +", "+ start +", "+ end +", "
						+ ""+txtBillingCycle.getText().replaceAll(",", "")+", "+ txtRentAmount.getText().replaceAll(",", "")+", "
						+ " "+ status +", "+ txtDeposit.getText().replaceAll(",", "")+", "
						+ " "+txtRemainingDeposit.getText().replaceAll(",", "")+", "
						+ " "+ txtNoOfTenant.getText().replaceAll(",", "") +", '"+ txtFellowTenant.getText() +"', "
						+ " '"+ txtRemarks.getText() +"', '"+ finalName +"',  ?, now(), "+Globals.G_Employee_ID+", 'N', null, null, null " , 
						finalName, pdfData, event );
			
				clear();
			}
			
			else {
				if (dpEnd.getValue().isAfter(dpStart.getValue())) {

					modular.createWithFileFinal(" OCCUPANCY_DETAILS ", " UNIT_ID = "+ unit +" AND REFERENCE_ID IS NULL ", 
							lblMessage, 
							" 0, "+ tenant +", "+ unit +", "+ delivery +", "+ start +", "+ end +", "
							+ ""+txtBillingCycle.getText().replaceAll(",", "")+", "+ txtRentAmount.getText().replaceAll(",", "")+", "
							+ " "+ status +", "+ txtDeposit.getText().replaceAll(",", "")+", "
							+ " "+txtRemainingDeposit.getText().replaceAll(",", "")+", "
							+ " "+ txtNoOfTenant.getText().replaceAll(",", "") +", '"+ txtFellowTenant.getText() +"', "
							+ " '"+ txtRemarks.getText() +"', '"+ finalName +"',  ?, now(), "+Globals.G_Employee_ID+", 'N', null, null, null " , 
							finalName, pdfData, event );
				
					clear();
				}
				else {
					lblEnd.setVisible(true);
					lblMessage.setVisible(true);
					lblMessage.setText("Invalid input of END date. ");
				}
			}
			
			} else {
				check();
				 
			}
	}

	public void check() {

		if ( cmbTenant.getValue() == null )
			lblTenant.setVisible(true);
		else
			lblTenant.setVisible(false);
		
		if ( cmbUnit.getValue() == null )
			lblUnit.setVisible(true);
		else
			lblUnit.setVisible(false);
		
		lblMessage.setVisible(true);
		lblMessage.setText("Field required!");
		
	}

	public void clear() throws SQLException {
		
		if (lblMessage.getText().equals("Record already exist.")) {
			
			lblMessage.setVisible(true);
			lblMessage.setText("Record already exist.");
			
		} else {
			
			cmbTenant.setValue(null);
			cmbUnit.setValue(null);
			cmbDelivery.setValue(null);
			dpStart.setValue(null);
			dpEnd.setValue(null);
			txtBillingCycle.clear();
			txtRentAmount.clear();
			cmbODStatus.setValue(null);
			txtDeposit.clear();
			txtRemainingDeposit.clear();
			txtNoOfTenant.clear();
			txtFellowTenant.clear();
			txtRemarks.clear();
			txtFile.clear();

			dbcon.tblOccupancyDetail(tblFinalize, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND OCCUPANCY_DETAILS.FINALIZED_RECORD = 'N' " );
			btnFinalize.setDisable(false);
			occpDetail = null;
			
			lblTenant.setVisible(false);
			lblUnit.setVisible(false);
			lblDelivery.setVisible(false);
			lblStart.setVisible(false);
			lblEnd.setVisible(false);
			lblBillingCycle.setVisible(false);
			lblRentAmount.setVisible(false);
			lblODStatus.setVisible(false);
			lblDeposit.setVisible(false);
			lblRemainingDeposit.setVisible(false);
			lblNoOfTenant.setVisible(false);
			lblFellowTenant.setVisible(false);
			lblRemarks.setVisible(false);
			lblPhoto.setVisible(false);
			lblMessage.setVisible(false);
			
		}
	}
	
	public void cancel() throws SQLException {
		 
		modular.cancel("OCCUPANCY_DETAILS", primaryStage);

	}
	
	public void setval () throws SQLException {
		
		if (txtBillingCycle.getText().isEmpty()) txtBillingCycle.setText("0");
		if (txtRentAmount.getText().isEmpty()) txtRentAmount.setText("0");
		if (txtDeposit.getText().isEmpty()) txtDeposit.setText("0");
		if (txtRemainingDeposit.getText().isEmpty()) txtRemainingDeposit.setText("0");
		if (txtNoOfTenant.getText().isEmpty()) txtNoOfTenant.setText("0");
		
		tenant = dbcon.getIDs("*", "PRIMARY_TENANT_DETAILS", "TENANT_ID", "REFERENCE_ID IS NULL AND TENANT_NAME = '"+ cmbTenant.getValue() +"' ");
		unit = dbcon.getIDs("*", "UNIT_DETAILS", "UNIT_ID", "REFERENCE_ID IS NULL AND UNIT_NAME = '"+ cmbUnit.getValue() +"' AND "
				+ "BLDG_ID = "+ Main_Window.dashboard +" ");
		
		if (cmbDelivery.getValue() == null)
			delivery = null;
		else if (cmbDelivery.getValue().equalsIgnoreCase("Bare"))
			delivery = "0";
		else if (cmbDelivery.getValue().equalsIgnoreCase("Partially Furnished"))
			delivery = "1";
		else if (cmbDelivery.getValue().equalsIgnoreCase("Furnished"))
			delivery = "2";
		else if (cmbDelivery.getValue().equalsIgnoreCase("Fully Furnished"))
			delivery = "3";
		
		if (dpStart.getValue() == null)
			start = null;
		else {
			start = dpStart.getValue().toString();
			start = "'" + start + "'";
			
		}
		
		if (dpEnd.getValue() == null)
			end = null;
		else {
			end = dpEnd.getValue().toString();
			end = "'" + end + "'";
		}
		
		if (cmbODStatus.getValue() == null)
			status = null;
		else if (cmbODStatus.getValue().equalsIgnoreCase("New Tenant"))
			status = "0";
		else if (cmbODStatus.getValue().equalsIgnoreCase("Unit Transfer"))
			status = "1";
		else if (cmbODStatus.getValue().equalsIgnoreCase("Building Transfer"))
			status = "2";
		else if (cmbODStatus.getValue().equalsIgnoreCase("End of Contract"))
			status = "3";
		else if (cmbODStatus.getValue().equalsIgnoreCase("Renewed Contract"))
			status = "4";
		else if(cmbODStatus.getValue().equalsIgnoreCase("Others"))
			status = "5";
		
	}

	public void finalize(ActionEvent event) throws SQLException {
		  
		modular.finalize("OCCUPANCY_DETAILS");
		((Node) event.getSource()).getScene().getWindow().hide();

	}
	
	public void occpView(String id) throws SQLException {
		
		String query = "SELECT OCCUPANCY_DETAILS.*, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT, "
				+ "UNIT_DETAILS.UNIT_NAME AS UNIT "
				+ " FROM OCCUPANCY_DETAILS "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE "+id+" ";
		
		String del, status ;
		Date start, end;
		byte[] fileBytes;
		String file; 
		String fileName, contractName = null;
		boolean doc = false, pdf = false;
		
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()){	
				
				cmbTenant.setValue(rs.getString("TENANT"));
				cmbUnit.setValue(rs.getString("UNIT"));
				
				del = rs.getString("OCCP_DELIVERY");
				
				if (del == null) cmbDelivery.setValue(null);
				else if (del.equals("0")) cmbDelivery.setValue("Bare");
				else if (del.equals("1")) cmbDelivery.setValue("Partially Furnished");
				else if (del.equals("2")) cmbDelivery.setValue("Furnished");
				else if (del.equals("3")) cmbDelivery.setValue("Fully Furnished");
				
				start = rs.getDate("OCCP_START"); 
	                
				if (start == null) {
					 
					 dpStart.getEditor().setText(rs.getString("OCCP_START")); 
				 }
	            else dpStart.setValue(start.toLocalDate());
				 
				end = rs.getDate("OCCP_END"); 
	                
				if (end == null) {
					 
					 dpEnd.getEditor().setText(rs.getString("OCCP_END")); 
				 }
	            else dpEnd.setValue(end.toLocalDate());
				 
				txtBillingCycle.setText(rs.getString("OCCP_BILLING_CYCLE"));
				txtRentAmount.setText(rs.getString("OCCP_RENT_AMOUNT"));
				
				status = rs.getString("OCCP_STATUS");
				
				if (status == null) cmbODStatus.setValue(null);
				else if (status.equals("0")) cmbODStatus.setValue("New Tenant");
				else if (status.equals("1")) cmbODStatus.setValue("Unit Transfer");
				else if (status.equals("2")) cmbODStatus.setValue("Building Transfer");
				else if (status.equals("3")) cmbODStatus.setValue("End of Contract");
				else if (status.equals("4")) cmbODStatus.setValue("Renewed Contract");
				else if (status.equals("5")) cmbODStatus.setValue("Others");
				
				txtDeposit.setText(rs.getString("OCCP_DEPOSIT"));
				txtRemainingDeposit.setText(rs.getString("OCCP_REM_DEPOSIT"));
				txtNoOfTenant.setText(rs.getString("OCCP_NO_OF_TENANT"));
				txtFellowTenant.setText(rs.getString("OCCP_FELLOW_TENANT"));
				txtRemarks.setText(rs.getString("OCCP_REMARKS"));

				 
                fileName = rs.getString("OCCP_CONTRACT_NAME");
                finalName = fileName;
				txtFile.setText(fileName);
				
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
				
	            fName = contractName;    
			}
		}	
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected occp detail "+id+" : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void upd() throws SQLException {

		if (Occupancy_Detail.occpDetail != null) {
			
			modular.updaterecord(" OCCUPANCY_DETAILS ", " TENANT_ID, UNIT_ID, OCCP_DELIVERY, OCCP_START, OCCP_END, "
					+ "OCCP_BILLING_CYCLE, OCCP_RENT_AMOUNT, OCCP_STATUS, OCCP_DEPOSIT, OCCP_REM_DEPOSIT, OCCP_NO_OF_TENANT, "
					+ "OCCP_FELLOW_TENANT, OCCP_REMARKS, OCCP_CONTRACT_NAME, OCCP_CONTRACT_PHOTO, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
					" TENANT_ID, UNIT_ID, OCCP_DELIVERY, OCCP_START, OCCP_END, OCCP_BILLING_CYCLE, OCCP_RENT_AMOUNT, OCCP_STATUS, "
					+ "OCCP_DEPOSIT, OCCP_REM_DEPOSIT, OCCP_NO_OF_TENANT, OCCP_FELLOW_TENANT, OCCP_REMARKS, OCCP_CONTRACT_NAME, OCCP_CONTRACT_PHOTO, "
					+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "
					+ ""+ Occupancy_Detail.occpDetail +" ",
					" OCCP_ID = "+ Occupancy_Detail.occpDetail +" ");
		}
		else if (Building_Detail.occpDetail != null) {
			
			modular.updaterecord(" OCCUPANCY_DETAILS ", " TENANT_ID, UNIT_ID, OCCP_DELIVERY, OCCP_START, OCCP_END, "
					+ "OCCP_BILLING_CYCLE, OCCP_RENT_AMOUNT, OCCP_STATUS, OCCP_DEPOSIT, OCCP_REM_DEPOSIT, OCCP_NO_OF_TENANT, "
					+ "OCCP_FELLOW_TENANT, OCCP_REMARKS, OCCP_CONTRACT_NAME, OCCP_CONTRACT_PHOTO, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
					" TENANT_ID, UNIT_ID, OCCP_DELIVERY, OCCP_START, OCCP_END, OCCP_BILLING_CYCLE, OCCP_RENT_AMOUNT, OCCP_STATUS, "
					+ "OCCP_DEPOSIT, OCCP_REM_DEPOSIT, OCCP_NO_OF_TENANT, OCCP_FELLOW_TENANT, OCCP_REMARKS, OCCP_CONTRACT_NAME, OCCP_CONTRACT_PHOTO, "
					+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "
					+ ""+ Building_Detail.occpDetail +" ",
					" OCCP_ID = "+ Building_Detail.occpDetail +" ");
		}
		
		
	}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbTenant.getValue() != null && cmbUnit.getValue() != null )
		{
			setval();
			
			modular.editwithFileFinal(" OCCUPANCY_DETAILS ", " UNIT_ID = "+ unit +" AND REFERENCE_ID IS NULL "
					+ "AND OCCP_ID <> "+ occpDetail +" " ,
					lblMessage,
					" TENANT_ID = "+ tenant +", UNIT_ID = "+ unit +", OCCP_DELIVERY = "+ delivery +", "
					+ " OCCP_START = "+start+", OCCP_END = "+ end +", "
					+ "OCCP_BILLING_CYCLE = "+ txtBillingCycle.getText().replaceAll(",", "")+" ,"
					+ "OCCP_RENT_AMOUNT = "+ txtRentAmount.getText().replaceAll(",", "")+", OCCP_STATUS = "+ status +", "
					+ "OCCP_DEPOSIT = "+ txtDeposit.getText().replaceAll(",", "") +", "
					+ "OCCP_REM_DEPOSIT = "+ txtRemainingDeposit.getText().replaceAll(",", "")+", "
					+ "OCCP_NO_OF_TENANT = "+ txtNoOfTenant.getText().replaceAll(",", "")+", "
					+ " OCCP_FELLOW_TENANT = '"+ txtFellowTenant.getText() +"', OCCP_REMARKS = '"+ txtRemarks.getText()+"', "
							+ " OCCP_CONTRACT_NAME = '"+ finalName +"' , "
					+ " OCCP_CONTRACT_PHOTO = ?, DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" OCCP_ID = "+ occpDetail +" ",
				   fName, pdfData, 
				   event, btnUpdate, btnCreate);
			
			clear();
						
			} else {
				check();
				 
			}

	}

	public void updateM(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbTenant.getValue() != null && cmbUnit.getValue() != null )
		{
					
			upd();
			setval();
			
			if (Occupancy_Detail.occpDetail != null) {
				
				modular.editwithFile(" OCCUPANCY_DETAILS ", " UNIT_ID = "+ unit +" AND REFERENCE_ID IS NULL AND "
						+ "OCCP_ID <> "+ Occupancy_Detail.occpDetail +" " ,
						lblMessage,
						" TENANT_ID = "+ tenant +", UNIT_ID = "+ unit +", OCCP_DELIVERY = "+ delivery +", "
						+ " OCCP_START = "+start+", OCCP_END = "+ end +", "
						+ "OCCP_BILLING_CYCLE = "+ txtBillingCycle.getText().replaceAll(",", "")+" ,"
						+ "OCCP_RENT_AMOUNT = "+ txtRentAmount.getText().replaceAll(",", "")+", OCCP_STATUS = "+ status +", "
						+ "OCCP_DEPOSIT = "+ txtDeposit.getText().replaceAll(",", "") +", "
						+ "OCCP_REM_DEPOSIT = "+ txtRemainingDeposit.getText().replaceAll(",", "")+", "
						+ "OCCP_NO_OF_TENANT = "+ txtNoOfTenant.getText().replaceAll(",", "")+", "
						+ " OCCP_FELLOW_TENANT = '"+ txtFellowTenant.getText() +"', OCCP_REMARKS = '"+ txtRemarks.getText()+"', "
								+ " OCCP_CONTRACT_NAME = '"+ finalName +"' , "
						+ " OCCP_CONTRACT_PHOTO = ?, DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
						" OCCP_ID = "+ Occupancy_Detail.occpDetail +" ",
						fName, pdfData,
					   event );
							
			}
			else if (Building_Detail.occpDetail != null ){
				
				modular.editwithFile(" OCCUPANCY_DETAILS ", " TENANT_ID = "+ tenant +" AND "
						+ "UNIT_ID = "+ unit +" AND REFERENCE_ID IS NULL AND OCCP_ID <> "+ Occupancy_Detail.occpDetail +" " ,
						lblMessage,
						" TENANT_ID = "+ tenant +", UNIT_ID = "+ unit +", OCCP_DELIVERY = "+ delivery +", "
						+ " OCCP_START = "+start+", OCCP_END = "+ end +", "
						+ "OCCP_BILLING_CYCLE = "+ txtBillingCycle.getText().replaceAll(",", "")+" ,"
						+ "OCCP_RENT_AMOUNT = "+ txtRentAmount.getText().replaceAll(",", "")+", OCCP_STATUS = "+ status +", "
						+ "OCCP_DEPOSIT = "+ txtDeposit.getText().replaceAll(",", "") +", "
						+ "OCCP_REM_DEPOSIT = "+ txtRemainingDeposit.getText().replaceAll(",", "")+", "
						+ "OCCP_NO_OF_TENANT = "+ txtNoOfTenant.getText().replaceAll(",", "")+", "
						+ " OCCP_FELLOW_TENANT = '"+ txtFellowTenant.getText() +"', OCCP_REMARKS = '"+ txtRemarks.getText()+"', "
								+ " OCCP_CONTRACT_NAME = '"+ finalName +"' , "
						+ " OCCP_CONTRACT_PHOTO = ?, DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
						" OCCP_ID = "+ Building_Detail.occpDetail +" ",
						fName, pdfData,
					   event );
							
			}
			else if (Building_Detail.unitDetails != null ) {
				
				modular.editwithFile(" OCCUPANCY_DETAILS ", " TENANT_ID = "+ tenant +" AND "
						+ "UNIT_ID = "+ unit +" AND REFERENCE_ID IS NULL AND OCCP_ID <> "+ Occupancy_Detail.occpDetail +" " ,
						lblMessage,
						" TENANT_ID = "+ tenant +", UNIT_ID = "+ unit +", OCCP_DELIVERY = "+ delivery +", "
						+ " OCCP_START = "+start+", OCCP_END = "+ end +", "
						+ "OCCP_BILLING_CYCLE = "+ txtBillingCycle.getText().replaceAll(",", "")+" ,"
						+ "OCCP_RENT_AMOUNT = "+ txtRentAmount.getText().replaceAll(",", "")+", OCCP_STATUS = "+ status +", "
						+ "OCCP_DEPOSIT = "+ txtDeposit.getText().replaceAll(",", "") +", "
						+ "OCCP_REM_DEPOSIT = "+ txtRemainingDeposit.getText().replaceAll(",", "")+", "
						+ "OCCP_NO_OF_TENANT = "+ txtNoOfTenant.getText().replaceAll(",", "")+", "
						+ " OCCP_FELLOW_TENANT = '"+ txtFellowTenant.getText() +"', OCCP_REMARKS = '"+ txtRemarks.getText()+"', "
								+ " OCCP_CONTRACT_NAME = '"+ finalName +"' , "
						+ " OCCP_CONTRACT_PHOTO = ?, DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
						" UNIT_ID = "+ Building_Detail.unitDetails +" AND REFERENCE_ID IS NULL ",
						fName, pdfData,
					   event );
							
			}
			
			} else {
				check();
				 
			}

	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
			 
			try {
				modular.cancelreq(" OCCUPANCY_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err occupancy detail add : " + e2.getMessage());
			}
		});
		btnCreate.setVisible(true);

	}

	public void processEditOption(String uID, Stage parent_primaryStage) {
		
		btnUpdateM.setVisible(true);
		btnCancel.setText("CLOSE");
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		
		if (Occupancy_Detail.occpDetail != null ) {
			
			try {
				occpView("OCCUPANCY_DETAILS.OCCP_ID = "+ Occupancy_Detail.occpDetail+ " ");
			} catch (SQLException e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err occp detail EDIT : " + e.getMessage());
			}
		}
		else if (Building_Detail.occpDetail != null ) {

			try {
				occpView("OCCUPANCY_DETAILS.OCCP_ID = "+ Building_Detail.occpDetail+ " ");
			} catch (SQLException e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err occp detail EDIT : " + e.getMessage());
			}
		}
		else if (Building_Detail.unitDetails != null ){
			
			try {
				occpView(" OCCUPANCY_DETAILS.UNIT_ID = "+ Building_Detail.unitDetails +" AND OCCUPANCY_DETAILS.REFERENCE_ID IS NULL " );
			} catch (SQLException e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err occp detail EDIT : " + e.getMessage());
			}
		}
		
	
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
			 
			try {
				modular.cancelreq("OCCUPANCY_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err occp detail EDIT CANCEL : " + e2.getMessage());
			}
			
		});
		
	}

	public void sizeview() {
		
		cmbTenant.setPrefWidth(410);
		cmbUnit.setPrefWidth(410);
		cmbDelivery.setPrefWidth(410);
		dpStart.setPrefWidth(410);
		dpEnd.setPrefWidth(410);
		txtBillingCycle.setPrefWidth(410);
		txtRentAmount.setPrefWidth(410);
		cmbODStatus.setPrefWidth(410);
		txtDeposit.setPrefWidth(410);
		txtRemainingDeposit.setPrefWidth(410);
		txtNoOfTenant.setPrefWidth(410);
		txtFellowTenant.setPrefWidth(410);
		txtRemarks.setPrefWidth(410);
		txtFile.setPrefWidth(410);

		lblMessage.setLayoutX(535);
		lblTenant.setLayoutX(535);
		lblUnit.setLayoutX(535);
		lblDelivery.setLayoutX(535);
		lblStart.setLayoutX(535);
		lblEnd.setLayoutX(535);
		lblBillingCycle.setLayoutX(535);
		lblRentAmount.setLayoutX(535);
		lblODStatus.setLayoutX(535);
		lblDeposit.setLayoutX(535);
		lblRemainingDeposit.setLayoutX(535);
		lblNoOfTenant.setLayoutX(535);
		lblFellowTenant.setLayoutX(535);
		lblRemarks.setLayoutX(535);
		lblPhoto.setLayoutX(535);

	}

	public void processViewOption(String uID) {
	
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		btnCancel.setText("CLOSE");
		
		cmbTenant.setDisable(true);
		cmbUnit.setDisable(true);
		cmbDelivery.setDisable(true);
		dpStart.setDisable(true);
		dpEnd.setDisable(true);
		txtBillingCycle.setDisable(true);
		txtRentAmount.setDisable(true);
		cmbODStatus.setDisable(true);
		txtDeposit.setDisable(true);
		txtRemainingDeposit.setDisable(true);
		txtNoOfTenant.setDisable(true);
		txtFellowTenant.setDisable(true);
		txtRemarks.setDisable(true);
		txtFile.setDisable(true);

		try {
			occpView( Occupancy_Detail.occpDetail );

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err occp detail record view : " + e.getMessage());
		}
		 
	}
	
}
