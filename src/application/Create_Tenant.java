package application;

import java.awt.image.BufferedImage;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Create_Tenant implements Initializable {
	
	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();

	@FXML private TextField txtName;
	@FXML private TextField txtMobileNo;
	@FXML private TextField txtLandlineNo;
	@FXML private TextField txtEmailAdd;
	
	@FXML private TextArea txtAddress;
	@FXML private TextArea txtRemarks;
	
	@FXML private ComboBox<String> cmbStatus;
	@FXML private Label lblTStatus;
	@FXML private Label lblStat;
	
	@FXML private DatePicker dpBday;
	
	@FXML private ImageView imgPhoto;
	
	@FXML private RadioButton rdMale;
	@FXML private RadioButton rdFemale;
	
	ToggleGroup rdGender = new ToggleGroup();

	@FXML private Label lblName;
	@FXML private Label lblAddress;
	@FXML private Label lblMobileNo;
	@FXML private Label lblLandlineNo;
	@FXML private Label lblBday;
	@FXML private Label lblEmailAdd;
	@FXML private Label lblGender;
	@FXML private Label lblRemarks;
	@FXML private Label lblMessage;
	
	@FXML private TableView<TableTenantDetail> tblFinalize;
	@FXML private VBox vb;
	
	@FXML Button btnCancel;
	@FXML private Button btnUpdate;
	@FXML private Button btnCreate;
	@FXML private Button btnUpdateM;
	@FXML private Button btnFinalize;
	@FXML private Button btnChoosePhoto;
	
	private Stage primaryStage;
	String tenant ;
	
	FileChooser fileChooser = new FileChooser();
    Image image;
    File file;
    FileInputStream getFile;
    BufferedImage bufferedImage;

    String gender, status, bday; 
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		setupTenantDetails_TableView();
		
		btnUpdate.setVisible(false);
		btnUpdateM.setVisible(false);
		btnCreate.setVisible(false);
		btnFinalize.setDisable(true);

		lblName.setVisible(false);
		lblAddress.setVisible(false);
		lblMobileNo.setVisible(false);
		lblLandlineNo.setVisible(false);
		lblBday.setVisible(false);
		lblEmailAdd.setVisible(false);
		lblGender.setVisible(false);
		lblRemarks.setVisible(false);
		lblMessage.setVisible(false);
		lblTStatus.setVisible(false);

		TextFormatter<String> tfname = modular.getTextFlexiFormatter(100, 'a'); 
		txtName.setTextFormatter(tfname);
		TextFormatter<String> tfno = modular.getTextFlexiFormatter(11, 'n');
		txtMobileNo.setTextFormatter(tfno);
		TextFormatter<String> tfland = modular.getTextFlexiFormatter(11, 'n');
		txtLandlineNo.setTextFormatter(tfland);
		TextFormatter<String> tfeadd = modular.getTextFlexiFormatter(100, 'a');
		txtEmailAdd.setTextFormatter(tfeadd);
		TextFormatter<String> tfrem = modular.getTextFlexiFormatter(500, 'a');
		txtAddress.setTextFormatter(tfrem);
		TextFormatter<String> tfremarks = modular.getTextFlexiFormatter(50, 'a');
		txtRemarks.setTextFormatter(tfremarks);
		
		rdMale.setToggleGroup(rdGender);
		rdFemale.setToggleGroup(rdGender);
		
		imgPhoto.setOnMouseClicked( ea-> {
			
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
			           
			            lblMessage.setVisible(true);
			            lblMessage.setText("No uploaded image.");
			            return;
			        }
		});
		
		cmbStatus.getItems().addAll("Active", "Inactive");
		
	
	}

	public void setupTenantDetails_TableView() {

		tblCol.TableTenantDetail(tblFinalize);

		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableTenantDetail selected = tblFinalize.getSelectionModel().getSelectedItem();
				tenant = selected.getTenantID();
				tenantview(tenant);
				
			} catch (Exception ex) {
			}
			if (tenant != null) {
				btnCreate.setVisible(false);
				btnUpdate.setVisible(true);
			} else
				return;
		});
	}
	
	public void chooseFile (ActionEvent event) throws FileNotFoundException {

	        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg", "*.gif", "*.bmp") ;
	        
	        fileChooser.getExtensionFilters().addAll(extFilter);
	        file = fileChooser.showOpenDialog(primaryStage);
	        
	        try {
	             bufferedImage = ImageIO.read(file);
	             image = SwingFXUtils.toFXImage(bufferedImage, null);
	            imgPhoto.setImage(image);
	           
	        } catch (IOException ex) {
	          Logger.getLogger(JavaFXImageBuilder.class.getName()).log(Level.SEVERE, null, ex);
	        }   
	        
	        getFile = new FileInputStream ( file );
	        
	    }

	public void create(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtName.getText().isEmpty() && !txtAddress.getText().isEmpty() )
		{
		
			setval();
			
			modular.createWithPhotoFinal(" PRIMARY_TENANT_DETAILS ", " TENANT_NAME = '"+ txtName.getText() +"' AND "
					+ "TENANT_BDAY = "+ bday +" AND REFERENCE_ID IS NULL ", 
					lblMessage, 
					" 0, '"+txtName.getText()+"', '"+txtAddress.getText()+"', "+txtMobileNo.getText().replaceAll(",", "")+", "
							+ " "+ txtLandlineNo.getText().replaceAll(",", "")+", '"+txtEmailAdd.getText()+"',"
							+ " "+ bday +", "+ gender+", "
							+ " 0, '"+ txtRemarks.getText() +"', "+ status +", ?, now(), "+Globals.G_Employee_ID+", 'N', null, null, null " , 
					imgPhoto, file, getFile, event );
		
			clear();
			
			} else {
				check();
				 
			}
	}

	public void check() {

		if (txtName.getText().isEmpty())
			lblName.setVisible(true);
		else
			lblName.setVisible(false);
		
		if (txtAddress.getText().isEmpty())
			lblAddress.setVisible(true);
		else
			lblAddress.setVisible(false);
		
		lblMessage.setVisible(true);
		lblMessage.setText("Field required!");
		
	}

	public void clear() throws SQLException {
		
		if (lblMessage.getText().equals("Record already exist.")) {
			
			lblMessage.setVisible(true);
			lblMessage.setText("Record already exist.");
			
		} else {
			
			txtName.clear();
			txtAddress.clear();
			txtMobileNo.clear();
			txtLandlineNo.clear();
			txtEmailAdd.clear();
			txtRemarks.clear();
			//cmbStatus.setValue(null);
			dpBday.setValue(null);
			
			imgPhoto.setImage(null);
			rdFemale.setSelected(false);
			rdMale.setSelected(false);

			dbcon.tblTenantDetail(tblFinalize, " PRIMARY_TENANT_DETAILS.REFERENCE_ID IS NULL AND PRIMARY_TENANT_DETAILS.FINALIZED_RECORD = 'N' ");
			btnFinalize.setDisable(false);
			tenant = null;
			
			lblName.setVisible(false);
			lblAddress.setVisible(false);
			lblMobileNo.setVisible(false);
			lblLandlineNo.setVisible(false);
			lblEmailAdd.setVisible(false);
			lblBday.setVisible(false);
			lblGender.setVisible(false);
			lblRemarks.setVisible(false);
			lblMessage.setVisible(false);
			lblTStatus.setVisible(false);
			
		}
	}
	
	public void cancel() throws SQLException {
		 
		modular.cancel("PRIMARY_TENANT_DETAILS", primaryStage);

	}
	
	public void setval () {
		
		if (!rdMale.isSelected() && !rdFemale.isSelected()) gender = null;
		else if (rdMale.isSelected()) gender = "0";
		else if (rdFemale.isSelected()) gender = "1";
		
		if (txtMobileNo.getText().isEmpty()) txtMobileNo.setText("0");
		if (txtLandlineNo.getText().isEmpty()) txtLandlineNo.setText("0");
		
		if (cmbStatus.getValue().equals("Active")) status = "1";
		else if (cmbStatus.getValue().equals("Inactive")) status = "0";
		
		if (dpBday.getValue() == null)
			bday = null;
		else {
			bday = dpBday.getValue().toString();
			bday = "'" + bday + "'";
		} 
		
		
	}

	public void finalize(ActionEvent event) throws SQLException {
		  
		modular.finalize("PRIMARY_TENANT_DETAILS");
		((Node) event.getSource()).getScene().getWindow().hide();

	}
	
	public void tenantview(String id) throws SQLException {
		
		String query = "SELECT * FROM PRIMARY_TENANT_DETAILS WHERE TENANT_ID = "+id+" ";
		
		String img, gen;
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()){	
				
				txtName.setText(rs.getString("TENANT_NAME"));
				txtAddress.setText(rs.getString("TENANT_ADDRESS"));
				txtMobileNo.setText(rs.getString("TENANT_MOBILE_NO"));
				txtLandlineNo.setText(rs.getString("TENANT_LANDLINE_NO"));
				txtEmailAdd.setText(rs.getString("TENANT_EMAIL_ADD"));
				
				gen = rs.getString("TENANT_GENDER"); 
				
				if (gen == null) {
					rdMale.setSelected(false);
					rdFemale.setSelected(false);
				}
				else if (gen.equals("0")) rdMale.setSelected(true); 
				else if (gen.equals("1")) rdFemale.setSelected(true);
				  
				 Date date = rs.getDate("TENANT_BDAY"); 
	                
				 if (date == null) {
					 
					 dpBday.getEditor().setText(rs.getString("TENANT_BDAY")); 
				 }
	             else dpBday.setValue(date.toLocalDate());
				 
				txtRemarks.setText(rs.getString("TENANT_REMARKS"));
				
				status = rs.getString("TENANT_STATUS");
				
				if (status.equals("1")) cmbStatus.setValue("Active");
				else if (status.equals("0")) cmbStatus.setValue("Inactive");
				else cmbStatus.setValue("");

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
	                    imgPhoto.setImage(image);
	                }
	                else {
	                    imgPhoto.setImage(null);
	                }
	                
			}
		}	
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected tenant "+id+" : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void upd() throws SQLException {

		modular.updaterecord(" PRIMARY_TENANT_DETAILS ", " TENANT_NAME, TENANT_ADDRESS, TENANT_MOBILE_NO, "
				+ "TENANT_LANDLINE_NO, TENANT_EMAIL_ADD, TENANT_BDAY, TENANT_GENDER, TENANT_USERACCT, TENANT_REMARKS, TENANT_STATUS, "
				+ " TENANT_PHOTO, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
				"TENANT_NAME, TENANT_ADDRESS, TENANT_MOBILE_NO, TENANT_LANDLINE_NO, TENANT_EMAIL_ADD, TENANT_BDAY, "
				+ " TENANT_GENDER, TENANT_USERACCT, TENANT_REMARKS, TENANT_STATUS, TENANT_PHOTO, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "
				+ ""+Tenant_Detail.tenant+" ",
				" TENANT_ID = "+Tenant_Detail.tenant+" ");
		
	}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtName.getText().isEmpty() && !txtAddress.getText().isEmpty() )
		{
			setval();
			
			modular.editwithPhotoFinal(" PRIMARY_TENANT_DETAILS ", " TENANT_NAME = '"+txtName.getText()+"' "
					+ " AND TENANT_BDAY = "+ bday +" AND TENANT_ID <> "+ tenant +" " ,
					lblMessage,
					" TENANT_NAME = '"+txtName.getText()+"', TENANT_ADDRESS = '"+ txtAddress.getText()+"',"
					+ " TENANT_MOBILE_NO = "+txtMobileNo.getText().replaceAll(",", "")+", "
					+ "TENANT_LANDLINE_NO = "+ txtLandlineNo.getText().replaceAll(",", "")+" ,"
					+ "TENANT_EMAIL_ADD = '"+ txtEmailAdd.getText()+"', TENANT_BDAY = "+ bday +", "
					+ "  TENANT_GENDER = "+ gender +", TENANT_REMARKS = '"+ txtRemarks.getText() +"', "
					+ "TENANT_STATUS = "+ status +",  TENANT_PHOTO = ?, "
					+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" TENANT_ID = "+ tenant +" ",
				   imgPhoto, file, getFile,
				   "src/picture/tenantPhoto.jpg",
				   event, btnUpdate, btnCreate);
			
			clear();
						
			} else {
				check();
				 
			}

	}

	public void updateM(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtName.getText().isEmpty() && !txtAddress.getText().isEmpty() )
		{
					
			upd();
			setval();
			
			modular.editwithPhoto(" PRIMARY_TENANT_DETAILS ", " TENANT_NAME = '"+txtName.getText()+"' "
					+ " AND TENANT_BDAY = "+ bday +" AND TENANT_ID <> "+Tenant_Detail.tenant+" " ,
					lblMessage,
					" TENANT_NAME = '"+txtName.getText()+"', TENANT_ADDRESS = '"+ txtAddress.getText()+"',"
							+ " TENANT_MOBILE_NO = "+txtMobileNo.getText().replaceAll(",", "")+", "
							+ "TENANT_LANDLINE_NO = "+ txtLandlineNo.getText().replaceAll(",", "")+" ,"
							+ "TENANT_EMAIL_ADD = '"+ txtEmailAdd.getText()+"', TENANT_BDAY = "+ bday +", "
							+ "  TENANT_GENDER = "+ gender +", TENANT_REMARKS = '"+ txtRemarks.getText() +"', "
							+ "TENANT_STATUS = "+ status +", TENANT_PHOTO = ?, "
							+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" TENANT_ID = "+Tenant_Detail.tenant+" ",
				   imgPhoto, file, getFile,
				   "src/picture/tenantPhoto.jpg", event);
						
			} else {
				check();
				 
			}

	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
			 
			try {
				modular.cancelreq(" PRIMARY_TENANT_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err tenant : " + e2.getMessage());
			}
		});
		btnCreate.setVisible(true);
		cmbStatus.setVisible(false);
		cmbStatus.setValue("Active");
		lblStat.setVisible(false);

	}

	public void processEditOption(String uID, Stage parent_primaryStage) {
		
		btnUpdateM.setVisible(true);
		btnCancel.setText("CLOSE");
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		
		try {
			tenantview(Tenant_Detail.tenant);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err tenant EDIT : " + e.getMessage());
		}
	
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
			 
			try {
				modular.cancelreq("PRIMARY_TENANT_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err TENANT EDIT CANCEL : " + e2.getMessage());
			}
			
		});
		
	}

	public void sizeview() {
		
		txtName.setPrefWidth(410);
		txtMobileNo.setPrefWidth(410);
		txtLandlineNo.setPrefWidth(410);
		txtEmailAdd.setPrefWidth(410);
		txtAddress.setPrefWidth(410);
		dpBday.setPrefWidth(410);
		txtRemarks.setPrefWidth(410);
		cmbStatus.setPrefWidth(410);
		cmbStatus.setVisible(true);

		lblTStatus.setLayoutX(535);
		lblMessage.setLayoutX(535);
		lblName.setLayoutX(535);
		lblAddress.setLayoutX(535);
		lblMobileNo.setLayoutX(535);
		lblLandlineNo.setLayoutX(535);
		lblEmailAdd.setLayoutX(535);
		lblGender.setLayoutX(535);
		lblRemarks.setLayoutX(535);
		lblBday.setLayoutX(535);
		
		btnChoosePhoto.setLayoutX(370);

	}

	public void processViewOption(String uID) {
	
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		btnCancel.setText("CLOSE");
		
		txtName.setDisable(true);
		txtEmailAdd.setDisable(true);
		txtMobileNo.setDisable(true);
		txtLandlineNo.setDisable(true);
		txtAddress.setDisable(true);
		dpBday.setDisable(true);
		txtRemarks.setDisable(true);
		imgPhoto.setDisable(true);
		rdMale.setDisable(true);
		rdFemale.setDisable(true);

		try {
			tenantview(Tenant_Detail.tenant);

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err tenant record view : " + e.getMessage());
		}
		 
	}
	
}
