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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Create_Administrator implements Initializable {
	
	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();

	@FXML private TextField txtName;
	@FXML private TextField txtMobileNo;
	@FXML private TextField txtEmailAdd;
	
	@FXML private TextArea txtRemarks;
	
	@FXML private DatePicker dpBday;
	
	@FXML private ImageView imgPhoto;
	
	@FXML private Label lblName;
	@FXML private Label lblMobileNo;
	@FXML private Label lblBday;
	@FXML private Label lblEmailAdd;
	@FXML private Label lblRemarks;
	@FXML private Label lblMessage;
	
	@FXML Button btnCancel;
	@FXML Button btnUpdate;
	@FXML Button btnCreate;
	@FXML Button btnChoosePhoto;
	@FXML private Button btnAdminCreateAccount;
	
	private Stage primaryStage;
	String admin , bday;
	
	FileChooser fileChooser = new FileChooser();
    Image image;
    File file;
    FileInputStream getFile;
    BufferedImage bufferedImage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnUpdate.setVisible(false);
		btnCreate.setVisible(false);
		btnAdminCreateAccount.setVisible(false);

		lblName.setVisible(false);
		lblMobileNo.setVisible(false);
		lblBday.setVisible(false);
		lblEmailAdd.setVisible(false);
		lblRemarks.setVisible(false);
		lblMessage.setVisible(false);

		TextFormatter<String> tfname = modular.getTextFlexiFormatter(100, 'a'); 
		txtName.setTextFormatter(tfname);
		TextFormatter<String> tfno = modular.getTextFlexiFormatter(12, 'n');
		txtMobileNo.setTextFormatter(tfno);
		TextFormatter<String> tfeadd = modular.getTextFlexiFormatter(100, 'a');
		txtEmailAdd.setTextFormatter(tfeadd);
		TextFormatter<String> tfrem = modular.getTextFlexiFormatter(1000, 'a');
		txtRemarks.setTextFormatter(tfrem);
		
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
	
	}

    public void adminCreateAccount (ActionEvent event) {
    	
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
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen create admin account: " + e.getMessage());
        }
    	
    }

	public void chooseFile (ActionEvent event) throws FileNotFoundException {

	        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Images (.png, .jpg, .gif, .bmp )", 
	        		"*.png", "*.jpg", "*.gif", "*.bmp") ;
	        
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
		
		if (!txtName.getText().isEmpty() )
		{
		
			setval();
			
			modular.createWithPhoto(" ADMIN_DETAILS ", " ADMIN_NAME = '"+ txtName.getText().replaceAll("'", "`") +"' AND REFERENCE_ID IS NULL ", 
					lblMessage, 
					" 0, '"+txtName.getText().replaceAll("'", "`")+"', '"+txtMobileNo.getText().replaceAll(",", "")+"', "+ bday +", "
							+ " '"+txtEmailAdd.getText().replaceAll("'", "`")+"', 0, '"+txtRemarks.getText().replaceAll("'", "`")+"', ?, "
							+ "now(), "+Globals.G_Employee_ID+", 'Y', null, null, null " , 
					imgPhoto, file, getFile, event );
		
			} else {
				check();
				 
			}
		
	}

	public void check() {

		if (txtName.getText().isEmpty())
			lblName.setVisible(true);
		else
			lblName.setVisible(false);
		
		lblMessage.setVisible(true);
		lblMessage.setText("Field required!");
	}

	public void cancel() throws SQLException {
		 
		modular.cancel("ADMIN_DETAILS", primaryStage);

	}

	public void setval() {
		
		if (dpBday.getValue() == null)
			bday = null;
		else {
			bday = dpBday.getValue().toString();
			bday = "'" + bday + "'";
		}
		
	}
	
	public void execview(String id) throws SQLException {
		
		String query = "SELECT * FROM ADMIN_DETAILS WHERE ADMIN_ID = "+id+" ";
		
		String img;
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()){	
				
				txtName.setText(rs.getString("ADMIN_NAME"));
				txtMobileNo.setText(rs.getString("ADMIN_MOBILE_NO"));
				txtEmailAdd.setText(rs.getString("ADMIN_EMAIL_ADD"));
				txtRemarks.setText(rs.getString("ADMIN_REMARKS"));
				
				 Date bday = rs.getDate("ADMIN_BDAY"); 
	                
				 if (bday == null) {
					 
					 dpBday.getEditor().setText(rs.getString("ADMIN_BDAY")); 
				 }
	             else dpBday.setValue(bday.toLocalDate());

				 img = rs.getString("ADMIN_PHOTO");
	                
	                if (img != null) {
	                    InputStream input = rs.getBinaryStream("ADMIN_PHOTO");
	                    OutputStream output = new FileOutputStream(new File ("src/picture/adminPhoto.jpg"));
	                    byte[] content = new byte[3000];
	                    int size = 0;
	                    
	                    while ( (size = input.read(content)) != -1) {
	                        output.write(content, 0, size);
	                    }
	                    
	                    output.close();
	                    input.close();
	                    
	                    image = new Image( "file:src/picture/adminPhoto.jpg", 1000, 1500, false, false);
	                    imgPhoto.setImage(image);
	                }
	                else {
	                    imgPhoto.setImage(null);
	                }
	                
			}
		}	
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected admin "+id+" : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void upd() throws SQLException {

		modular.updaterecord(" ADMIN_DETAILS ", " ADMIN_NAME, ADMIN_MOBILE_NO, ADMIN_BDAY, "
				+ " ADMIN_EMAIL_ADD, ADMIN_USER_ACCT, ADMIN_REMARKS, ADMIN_PHOTO, "
				+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", "ADMIN_NAME, ADMIN_MOBILE_NO, ADMIN_BDAY, "
				+ " ADMIN_EMAIL_ADD, ADMIN_USER_ACCT, ADMIN_REMARKS, ADMIN_PHOTO, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "
				+ ""+Main_Window.admin+" ", " ADMIN_ID = "+Main_Window.admin+" ");
		
	}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtName.getText().isEmpty() )
		{
					
			upd();
			setval();
			
			/*modular.editrecord(" ADMIN_DETAILS ", " ADMIN_NAME = '"+txtName.getText()+"' "
					+ " AND ADMIN_ID <> "+Main_Window.admin+" ",
					lblMessage, 
					" ADMIN_NAME = '"+txtName.getText()+"', ADMIN_MOBILE_NO = '"+txtMobileNo.getText()+"', ADMIN_BDAY = "+ bday +", "
					+ " ADMIN_EMAIL_ADD = '"+ txtEmailAdd.getText()+"', ADMIN_CONTACT_INFO = '"+txtRemarks.getText()+"', "
					+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" ADMIN_ID = "+Main_Window.admin+" ", event);
			*/
			
			modular.editwithPhoto(" ADMIN_DETAILS ", " ADMIN_NAME = '"+txtName.getText().replaceAll("'", "`")+"' "
					+ " AND ADMIN_ID <> "+Main_Window.admin+" " ,
					lblMessage,
					" ADMIN_NAME = '"+txtName.getText().replaceAll("'", "`")+"', ADMIN_MOBILE_NO = '"+txtMobileNo.getText().replaceAll(",", "")+"',"
							+ " ADMIN_BDAY = "+ bday +", ADMIN_EMAIL_ADD = '"+ txtEmailAdd.getText().replaceAll("'", "`")+"', "
									+ "ADMIN_REMARKS = '"+txtRemarks.getText().replaceAll("'", "`")+"', "
							+ " ADMIN_PHOTO = ?, DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" ADMIN_ID = "+Main_Window.admin+" ",
				   imgPhoto, file, getFile, "src/picture/adminPhoto.jpg", event);
						
			} else {
				check();
				 
			}

	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
			 
			try {
				modular.cancelreq(" ADMIN_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err admin : " + e2.getMessage());
			}
		});
		btnCreate.setVisible(true);

	}

	public void processEditOption(String uID, Stage parent_primaryStage) {
		
		sizeview();
		btnUpdate.setVisible(true);
		btnCancel.setText("CLOSE");
		
		try {
			execview(Main_Window.admin);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err admin EDIT : " + e.getMessage());
		}
	
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
			 
			try {
				modular.cancelreq("ADMIN_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err ADMIN EDIT CANCEL : " + e2.getMessage());
			}
			
		});
		
	}

	public void sizeview() {
		
		txtName.setPrefWidth(410);
		txtMobileNo.setPrefWidth(410);
		txtEmailAdd.setPrefWidth(410);
		txtRemarks.setPrefWidth(410);
		dpBday.setPrefWidth(410);
		imgPhoto.setFitWidth(400);
		imgPhoto.setFitWidth(200);

		lblMessage.setLayoutX(535);
		lblName.setLayoutX(535);
		lblMobileNo.setLayoutX(535);
		lblEmailAdd.setLayoutX(535);
		lblRemarks.setLayoutX(535);
		lblBday.setLayoutX(535);
		btnChoosePhoto.setLayoutX(400);

	}

	public void processViewOption(String uID) {
	
		sizeview();
		btnCancel.setText("CLOSE");
		btnAdminCreateAccount.setVisible(true);
		
		txtName.setDisable(true);
		txtEmailAdd.setDisable(true);
		txtMobileNo.setDisable(true);
		txtRemarks.setDisable(true);
		dpBday.setDisable(true);
		imgPhoto.setDisable(true);

		try {
			execview(Main_Window.admin);

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err admin record view : " + e.getMessage());
		}
		 
	}
	
}
