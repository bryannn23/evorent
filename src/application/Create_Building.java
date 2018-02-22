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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Create_Building implements Initializable {
	
	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues combo = new ComboBoxValues();

	@FXML private TextField txtBuildingName;
	@FXML private TextField txtNoOfFloors;
	@FXML private TextField txtDateEstablished;
	@FXML private TextField txtContactInfo;
	
	@FXML private TextArea txtAddress;
	@FXML private TextArea txtFeatures;
	@FXML private TextArea txtRemarks;
	
	@FXML private ComboBox<String> cmbCondition;
	
	@FXML private ImageView imgBuilding1;
	@FXML private ImageView imgBuilding2;
	@FXML private ImageView imgBuilding3;
	@FXML private ImageView imgBuilding4;
	
	@FXML private Label lblBuildingName;
	@FXML private Label lblAddress;
	@FXML private Label lblContactInfo;
	@FXML private Label lblDateEstablished;
	@FXML private Label lblNoOfFloors;
	@FXML private Label lblFeatures;
	@FXML private Label lblCondition;
	@FXML private Label lblRemarks;
	@FXML private Label lblMessage;
	
	@FXML Button btnCancel;
	@FXML Button btnUpdate;
	@FXML Button btnCreate;
	
	private Stage primaryStage;
	
	FileChooser fileChooser = new FileChooser();
    Image image1, image2, image3, image4;
    File file, file1, file2, file3;
    FileInputStream getFile, getFile1, getFile2, getFile3 ;
    BufferedImage bufferedImage;
    List<File> list;
    
    String condi;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnUpdate.setVisible(false);
		btnCreate.setVisible(false);

		lblBuildingName.setVisible(false);
		lblAddress.setVisible(false);
		lblContactInfo.setVisible(false);
		lblDateEstablished.setVisible(false);
		lblNoOfFloors.setVisible(false);
		lblFeatures.setVisible(false);
		lblCondition.setVisible(false);
		lblRemarks.setVisible(false);
		lblMessage.setVisible(false);

		TextFormatter<String> tfname = modular.getTextFlexiFormatter(100, 'a'); 
		txtBuildingName.setTextFormatter(tfname);
		TextFormatter<String> tfadd = modular.getTextFlexiFormatter(500, 'a');
		txtAddress.setTextFormatter(tfadd);
		TextFormatter<String> tfcont = modular.getTextFlexiFormatter(12, 'n');
		txtContactInfo.setTextFormatter(tfcont);
		TextFormatter<String> tfdate= modular.getTextFlexiFormatter(50, 'a');
		txtDateEstablished.setTextFormatter(tfdate);
		TextFormatter<String> tffloor = modular.getTextFlexiFormatter(2, 'n');
		txtNoOfFloors.setTextFormatter(tffloor);
		TextFormatter<String> tffeature = modular.getTextFlexiFormatter(1000, 'a');
		txtFeatures.setTextFormatter(tffeature);
		TextFormatter<String> tfrem = modular.getTextFlexiFormatter(1000, 'a');
		txtRemarks.setTextFormatter(tfrem);
		
		combo.condition(cmbCondition);
		
		new AutoCompleteComboBoxListener<String>(cmbCondition);
		
		imgBuilding1.setOnMouseClicked( e-> {
			  try {
		            Stage primaryStage = new Stage();
		            primaryStage.initModality(Modality.APPLICATION_MODAL);
		            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View_Image.fxml"));
		            Parent root = (Parent) loader.load();

		            View_Image cont = loader.getController();
		            cont.processViewImage(primaryStage, image1);
		            cont.btnClose.setOnAction( e1 -> primaryStage.close());
		            
		            Scene scene = new Scene(root);
		            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
		            primaryStage.setScene(scene);
		            primaryStage.showAndWait();

		        } catch (Exception e1) {
		            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error view image : " + e1.getMessage());
		        }
		});

		imgBuilding2.setOnMouseClicked( e-> {
			  try {
		            Stage primaryStage = new Stage();
		            primaryStage.initModality(Modality.APPLICATION_MODAL);
		            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View_Image.fxml"));
		            Parent root = (Parent) loader.load();

		            View_Image cont = loader.getController();
		            cont.processViewImage(primaryStage, image2);
		            cont.btnClose.setOnAction( e1 -> primaryStage.close());
		            
		            Scene scene = new Scene(root);
		            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
		            primaryStage.setScene(scene);
		            primaryStage.showAndWait();

		        } catch (Exception e1) {
		            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error view image : " + e1.getMessage());
		        }
		});
		
		imgBuilding3.setOnMouseClicked( e-> {
			  try {
		            Stage primaryStage = new Stage();
		            primaryStage.initModality(Modality.APPLICATION_MODAL);
		            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View_Image.fxml"));
		            Parent root = (Parent) loader.load();

		            View_Image cont = loader.getController();
		            cont.processViewImage(primaryStage, image3);
		            cont.btnClose.setOnAction( e1 -> primaryStage.close());
		            
		            Scene scene = new Scene(root);
		            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
		            primaryStage.setScene(scene);
		            primaryStage.showAndWait();

		        } catch (Exception e1) {
		            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error view image : " + e1.getMessage());
		        }
		});
		
		imgBuilding4.setOnMouseClicked( e-> {
			  try {
		            Stage primaryStage = new Stage();
		            primaryStage.initModality(Modality.APPLICATION_MODAL);
		            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View_Image.fxml"));
		            Parent root = (Parent) loader.load();

		            View_Image cont = loader.getController();
		            cont.processViewImage(primaryStage, image4);
		            cont.btnClose.setOnAction( e1 -> primaryStage.close());
		            
		            Scene scene = new Scene(root);
		            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
		            primaryStage.setScene(scene);
		            primaryStage.showAndWait();

		        } catch (Exception e1) {
		            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error view image : " + e1.getMessage());
		        }
		});
		
	}

    public void chooseImg1 (ActionEvent event) throws FileNotFoundException {

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg", "*.gif", "*.bmp") ;
        
        fileChooser.getExtensionFilters().addAll(extFilter);
       // file = fileChooser.showOpenDialog(primaryStage);
        
         list =fileChooser.showOpenMultipleDialog(primaryStage);
        
        if (list != null) {
           
               for(int i=0; i<list.size(); i++){
               
            	   if (i == 0) {
            		   file = new File(list.get(i).getPath());
            		   try {
            	             bufferedImage = ImageIO.read(file);
            	             image1 = SwingFXUtils.toFXImage(bufferedImage, null);
            	             imgBuilding1.setImage(image1);
            	           
            	        } catch (IOException ex) {
            	          Logger.getLogger(JavaFXImageBuilder.class.getName()).log(Level.SEVERE, null, ex);
            	        }
            	   }
            	   else if (i == 1) {
            		   file = new File(list.get(i).getPath());
            		   try {
            	             bufferedImage = ImageIO.read(file);
            	             image2 = SwingFXUtils.toFXImage(bufferedImage, null);
            	             imgBuilding2.setImage(image2);
            	           
            	        } catch (IOException ex) {
            	          Logger.getLogger(JavaFXImageBuilder.class.getName()).log(Level.SEVERE, null, ex);
            	        }
            	   }
            	   else if (i == 2) {
            		   file = new File(list.get(i).getPath());
            		   try {
            	             bufferedImage = ImageIO.read(file);
            	             image3 = SwingFXUtils.toFXImage(bufferedImage, null);
            	             imgBuilding3.setImage(image3);
            	           
            	        } catch (IOException ex) {
            	          Logger.getLogger(JavaFXImageBuilder.class.getName()).log(Level.SEVERE, null, ex);
            	        }
            	   }
            	   else if (i == 3) {
            		   file = new File(list.get(i).getPath());
            		   try {
            	             bufferedImage = ImageIO.read(file);
            	             image4 = SwingFXUtils.toFXImage(bufferedImage, null);
            	             imgBuilding4.setImage(image4);
            	           
            	        } catch (IOException ex) {
            	          Logger.getLogger(JavaFXImageBuilder.class.getName()).log(Level.SEVERE, null, ex);
            	        }
            	   }
            }
               
           
        }
        
       /* try {
             bufferedImage = ImageIO.read(file);
             image = SwingFXUtils.toFXImage(bufferedImage, null);
             imgBuilding1.setImage(image);
           
        } catch (IOException ex) {
          Logger.getLogger(JavaFXImageBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }   
        */
        getFile = new FileInputStream ( file );
        
    }

	public void create(ActionEvent event) throws SQLException {

		if (!txtBuildingName.getText().isEmpty() && !txtNoOfFloors.getText().isEmpty()) {
			
			lblMessage.setText("");
			/*modular.create(" BUILDING_DETAILS ", " BLDG_NAME = '"+txtBuildingName.getText()+"' AND "
					+ " BLDG_ADDRESS = '"+ txtAddress.getText()+"' ", 
					lblMessage, 
					"0, '"+txtBuildingName.getText()+"' , '"+txtAddress.getText()+"', '"+txtContactInfo.getText()+"', "
					+ " '"+ txtDateEstablished.getText()+"', "+ txtNoOfFloors.getText().replaceAll(",", "")+", "
					+ " '"+ txtFeatures.getText()+"', "+ condi +", '"+ txtRemarks.getText()+"' "
					+ " now(), "+Globals.G_Employee_ID+", null, null, null ", event);*/
			
			setval();

	        String check = "SELECT * FROM BUILDING_DETAILS WHERE BLDG_NAME = '"+txtBuildingName.getText()+"' AND "
					+ " BLDG_ADDRESS = '"+ txtAddress.getText()+"' ";
	        
	        ps = dbcon.connect.prepareStatement(check);
	        rs = ps.executeQuery();
	        
	        if (rs.next()) {
	            lblMessage.setVisible(true);
	            lblMessage.setText("Record already exist.");
	            
	        } else {
	            
	            String query = "INSERT INTO BUILDING_DETAILS VALUES ( 0, '"+txtBuildingName.getText()+"' , '"+txtAddress.getText()+"', '"+txtContactInfo.getText()+"', "
					+ " '"+ txtDateEstablished.getText()+"', "+ txtNoOfFloors.getText().replaceAll(",", "")+", "
					+ " '"+ txtFeatures.getText()+"', "+ condi +", '"+ txtRemarks.getText()+"', ?, ?, ?, ?, "
					+ " now(), "+Globals.G_Employee_ID+", 'Y', null, null, null ) ";
	            
	            try {
	            	
	            if (list != null) {
	                     
	                  if (list.size() == 1) {
	                	  
	                  		file = new File(list.get(0).getPath());
	                        ps = dbcon.connect.prepareStatement(query);
		                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length() );
		                    ps.setString(2, null);
		                    ps.setString(3, null);
		                    ps.setString(4, null);
		                    ps.executeUpdate();
		                    
		                    Alert alert = new Alert(AlertType.INFORMATION);
		    				alert.setTitle("Information");
		    				alert.setHeaderText(null);
		    				alert.setContentText("You have successfully added a record.");
		    				alert.showAndWait();

		    				((Node) event.getSource()).getScene().getWindow().hide();
		    				
	                 }
	                else if (list.size() == 2) {

                  		file = new File(list.get(0).getPath());
                  		file1 = new File(list.get(1).getPath());
                        ps = dbcon.connect.prepareStatement(query);
	                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length() );
	                    ps.setBinaryStream(2, (InputStream)getFile, (int) file1.length());
	                    ps.setString(3, null);
	                    ps.setString(4, null);
	                    ps.executeUpdate();
	                    
	                    Alert alert = new Alert(AlertType.INFORMATION);
	    				alert.setTitle("Information");
	    				alert.setHeaderText(null);
	    				alert.setContentText("You have successfully added a record.");
	    				alert.showAndWait();

	    				((Node) event.getSource()).getScene().getWindow().hide();
	                }
	               else if (list.size() == 3) {

                 		file = new File(list.get(0).getPath());
                 		file1 = new File(list.get(1).getPath());
                 		file2 = new File(list.get(2).getPath());
                 		
                        ps = dbcon.connect.prepareStatement(query);
	                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length() );
	                    ps.setBinaryStream(2, (InputStream)getFile, (int) file1.length());
	                    ps.setBinaryStream(3, (InputStream)getFile, (int) file2.length());
	                    ps.setString(4, null);
	                    ps.executeUpdate();
	                    
	                    Alert alert = new Alert(AlertType.INFORMATION);
	    				alert.setTitle("Information");
	    				alert.setHeaderText(null);
	    				alert.setContentText("You have successfully added a record.");
	    				alert.showAndWait();

	    				((Node) event.getSource()).getScene().getWindow().hide();
	    				
	                }
	              else if (list.size() == 4) {

                		file = new File(list.get(0).getPath());
                		file1 = new File(list.get(1).getPath());
                		file2 = new File(list.get(2).getPath());
                		file3 = new File(list.get(3).getPath());
                      
                		ps = dbcon.connect.prepareStatement(query);
	                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length() );
	                    ps.setBinaryStream(2, (InputStream)getFile, (int) file1.length());
	                    ps.setBinaryStream(3, (InputStream)getFile, (int) file2.length());
	                    ps.setBinaryStream(4, (InputStream)getFile, (int) file3.length());
	                    ps.executeUpdate();
	                    
	                    Alert alert = new Alert(AlertType.INFORMATION);
	    				alert.setTitle("Information");
	    				alert.setHeaderText(null);
	    				alert.setContentText("You have successfully added a record.");
	    				alert.showAndWait();

	    				((Node) event.getSource()).getScene().getWindow().hide();
	                  }
	            }
	            else {

	            	 ps = dbcon.connect.prepareStatement(query);
	                    ps.setString(1, null);
	                    ps.setString(2, null);
	                    ps.setString(3, null);
	                    ps.setString(4, null);
	                    ps.executeUpdate();
	                    
	                    Alert alert = new Alert(AlertType.INFORMATION);
	    				alert.setTitle("Information");
	    				alert.setHeaderText(null);
	    				alert.setContentText("You have successfully added a record.");
	    				alert.showAndWait();

	    				((Node) event.getSource()).getScene().getWindow().hide();
	    				
	            }
	            	
	                /*if (imgBuilding1.getImage() != null && imgBuilding2.getImage() == null && 
	                		imgBuilding3.getImage() == null && imgBuilding4.getImage() == null ) {
	                	
	                    ps = dbcon.connect.prepareStatement(query);
	                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length() );
	                    ps.setString(2, null);
	                    ps.setString(3, null);
	                    ps.setString(4, null);
	                    ps.executeUpdate();
	                    
	                    Alert alert = new Alert(AlertType.INFORMATION);
	    				alert.setTitle("Information");
	    				alert.setHeaderText(null);
	    				alert.setContentText("You have successfully added a record.");
	    				alert.showAndWait();

	    				((Node) event.getSource()).getScene().getWindow().hide();
	    				
	                }
	                else if (imgBuilding1.getImage() != null && imgBuilding2.getImage() != null && 
	                		imgBuilding3.getImage() == null && imgBuilding4.getImage() == null ) {
	                	
	                    ps = dbcon.connect.prepareStatement(query);
	                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length() );
	                    ps.setString(2, null);
	                    ps.setString(3, null);
	                    ps.setString(4, null);
	                    ps.executeUpdate();
	                    
	                    Alert alert = new Alert(AlertType.INFORMATION);
	    				alert.setTitle("Information");
	    				alert.setHeaderText(null);
	    				alert.setContentText("You have successfully added a record.");
	    				alert.showAndWait();

	    				((Node) event.getSource()).getScene().getWindow().hide();
	    				
	                }
	                else {
	                    ps = dbcon.connect.prepareStatement(query);
	                    ps.setString(1, null);
	                    ps.executeUpdate();
	                    
	                    Alert alert = new Alert(AlertType.INFORMATION);
	    				alert.setTitle("Information");
	    				alert.setHeaderText(null);
	    				alert.setContentText("You have successfully added a record.");
	    				alert.showAndWait();

	    				((Node) event.getSource()).getScene().getWindow().hide();
	    				
	                }*/

	            } catch (Exception e) {
	                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert building details : " + e.getMessage());
	            } finally {
	                ps.close();
	            }
	        }
	            
			
			
		}
		else {
			check();
		}
	}

	public void check() {

		if (txtBuildingName.getText().isEmpty())
			lblBuildingName.setVisible(true);
		else
			lblBuildingName.setVisible(false);
		
		if (txtNoOfFloors.getText().isEmpty())
			lblNoOfFloors.setVisible(true);
		else 
			lblNoOfFloors.setVisible(false);
		
		lblMessage.setVisible(true);
		lblMessage.setText("Field required!");
	}

	public void setval () {
	
		if (cmbCondition.getValue() == null)
			condi = null;
		else if (cmbCondition.getValue().equalsIgnoreCase("New"))
			condi = "0";
		else if (cmbCondition.getValue().equalsIgnoreCase("Well Maintained"))
			condi = "1";
		else if (cmbCondition.getValue().equalsIgnoreCase("Needs Renovation"))
			condi = "2";
		else if (cmbCondition.getValue().equalsIgnoreCase("Under Renovation"))
			condi = "3";
		else if (cmbCondition.getValue().equalsIgnoreCase("Renovated"))
			condi = "4";
		else if (cmbCondition.getValue().equalsIgnoreCase("Retired"))
			condi = "5";
		else if (cmbCondition.getValue().equalsIgnoreCase("Others"))
			condi = "6";
		
		if (txtContactInfo.getText().isEmpty()) txtContactInfo.setText("0");
		
	}
	
	public void cancel(ActionEvent event) throws SQLException {
		
		modular.cancel(" BUILDING_DETAILS", primaryStage);
		((Node)event.getSource()).getScene().getWindow().hide();

	}

	public void viewBuilding(String id) throws SQLException {
		
		String query = "SELECT * FROM BUILDING_DETAILS WHERE BLDG_ID = "+id+" ";
		
		String condi, img1, img2, img3, img4 ;
		
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()){	
				
				txtBuildingName.setText(rs.getString("BLDG_NAME"));
				txtAddress.setText(rs.getString("BLDG_ADDRESS"));
				txtContactInfo.setText(rs.getString("BLDG_CONTACT_INFO"));
				txtDateEstablished.setText(rs.getString("BLDG_DATE_EST"));
				txtNoOfFloors.setText(rs.getString("BLDG_FLOORS"));
				txtFeatures.setText(rs.getString("BLDG_FEATURES"));
				
				condi = rs.getString("BLDG_CONDITION");
				
				if (condi == null) cmbCondition.setValue(null);
				else if (condi.equals("0")) cmbCondition.setValue("NEW");
				else if (condi.equals("1")) cmbCondition.setValue("WELL MAINTAINED");
				else if (condi.equals("2")) cmbCondition.setValue("NEEDS RENOVATION");
				else if (condi.equals("3")) cmbCondition.setValue("UNDER RENOVATION");
				else if (condi.equals("4")) cmbCondition.setValue("RENOVATED");
				else if (condi.equals("5")) cmbCondition.setValue("REITRED");
				else if (condi.equals("6")) cmbCondition.setValue("OTHERS");
				
				txtRemarks.setText(rs.getString("BLDG_REMARKS"));
				
			 img1 = rs.getString("BLDG_PHOTO1");
	                
	            if (img1 != null) {
	                  InputStream input = rs.getBinaryStream("BLDG_PHOTO1");
	                  OutputStream output = new FileOutputStream(new File ("src/picture/bldgPhoto1.jpg"));
	                  byte[] content = new byte[3000];
	                  int size = 0;
	                    
	                  while ( (size = input.read(content)) != -1) {
	                     output.write(content, 0, size);
	                  }
	                    
	                  output.close();
	                  input.close();
	                    
	                  image1 = new Image( "file:src/picture/bldgPhoto1.jpg", 1000, 1500, true, true);
	                  imgBuilding1.setImage(image1);
	              }
	            else {
	                 imgBuilding1.setImage(null);
	            }

			img2 = rs.getString("BLDG_PHOTO2");
		                
		         if (img2 != null) {
		            InputStream input = rs.getBinaryStream("BLDG_PHOTO2");
		            OutputStream output = new FileOutputStream(new File ("src/picture/bldgPhoto2.jpg"));
		            byte[] content = new byte[3000];
		            int size = 0;
		                    
		            while ( (size = input.read(content)) != -1) {
		                output.write(content, 0, size);
		            }
		                    
		            output.close();
		            input.close();
		                    
		            image2 = new Image( "file:src/picture/bldgPhoto2.jpg", 1000, 1500, true, true);
		            imgBuilding2.setImage(image2);
		         }
		         else {
		            imgBuilding2.setImage(null);
		        }

		   img3 = rs.getString("BLDG_PHOTO3");
	                
	            if (img3 != null) {
	                InputStream input = rs.getBinaryStream("BLDG_PHOTO3");
	                OutputStream output = new FileOutputStream(new File ("src/picture/bldgPhoto3.jpg"));
	                byte[] content = new byte[3000];
	                int size = 0;
	                    
	                while ( (size = input.read(content)) != -1) {
	                   output.write(content, 0, size);
	                }
	                    
	                output.close();
	                input.close();
	                    
	                image3 = new Image( "file:src/picture/bldgPhoto3.jpg", 1000, 1500, true, true);
	                imgBuilding3.setImage(image3);
	             }
	             else {
	                imgBuilding3.setImage(null);
	             }
	            

			img4 = rs.getString("BLDG_PHOTO4");
	                
	            if (img4 != null) {
	                InputStream input = rs.getBinaryStream("BLDG_PHOTO4");
	                OutputStream output = new FileOutputStream(new File ("src/picture/bldgPhoto4.jpg"));
	                byte[] content = new byte[3000];
	                int size = 0;
	                    
	                while ( (size = input.read(content)) != -1) {
	                    output.write(content, 0, size);
	                }
	                    
	                output.close();
	                input.close();
	                    
	                image4 = new Image( "file:src/picture/bldgPhoto4.jpg", 1000, 1500, true, true);
	                imgBuilding4.setImage(image4);
	           }
	            else {
	                imgBuilding4.setImage(null);
	           }
	                
			}
		}	
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected building "+id+" : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void upd() throws SQLException {

		modular.updaterecord(" BUILDING_DETAILS ", " BLDG_NAME, BLDG_ADDRESS, BLDG_CONTACT_INFO, BLDG_DATE_EST, "
				+ "BLDG_FLOORS, BLDG_FEATURES, BLDG_CONDITION, BLDG_REMARKS, BLDG_PHOTO1, BLDG_PHOTO2, BLDG_PHOTO3, BLDG_PHOTO4, "
				+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ",
				" BLDG_NAME, BLDG_ADDRESS, BLDG_CONTACT_INFO, BLDG_DATE_EST, BLDG_FLOORS, BLDG_FEATURES, BLDG_CONDITION, "
				+ " BLDG_REMARKS, BLDG_PHOTO1, BLDG_PHOTO2, BLDG_PHOTO3, BLDG_PHOTO4, "
				+ " DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "+Main_Window.dashboard+" ", " BLDG_ID = "+Main_Window.dashboard+" ");
	}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtBuildingName.getText().isEmpty() && !txtNoOfFloors.getText().isEmpty()) {	
			
			upd();
			setval();
			
			lblMessage.setText("");			
			String check = "SELECT * FROM BUILDING_DETAILS WHERE REFERENCE_ID IS NULL AND BLDG_NAME = '"+txtBuildingName.getText()+"' AND "
					+ " BLDG_ADDRESS = '"+ txtAddress.getText()+"' AND BLDG_ID <> "+ Main_Window.dashboard+" ";

	        ps = dbcon.connect.prepareStatement(check);
	        rs = ps.executeQuery();
	        if (rs.next()) {
	            lblMessage.setVisible(true);
	            lblMessage.setText("Record already exist.");
	            
	        } else {
	        	
	            String query = "UPDATE BUILDING_DETAILS SET BLDG_NAME = '"+txtBuildingName.getText()+"', "
	            		+ "BLDG_ADDRESS = '"+ txtAddress.getText()+"', BLDG_CONTACT_INFO = "+ txtContactInfo.getText().replaceAll(",", "")+", "
	            		+ "BLDG_DATE_EST = '"+ txtDateEstablished.getText()+"', BLDG_FLOORS = "+ txtNoOfFloors.getText().replaceAll(",", "")+", "
	            		+ "BLDG_FEATURES = '"+ txtFeatures.getText()+"', BLDG_CONDITION = "+ condi +", BLDG_REMARKS = '"+ txtRemarks.getText()+"', "
	            		+ "BLDG_PHOTO1 = ?, BLDG_PHOTO2 = ?, BLDG_PHOTO3 = ?, BLDG_PHOTO4 = ?, DATETIME_ENTRY = now(),"
	            		+ "SYSTEM_ACCOUNT_ID = "+ Globals.G_Employee_ID+"  WHERE BLDG_ID = "+ Main_Window.dashboard+" ";

	            try {
	            	
	            	if (list != null) {
	                     
		                  if ( list.size() == 1  ) {
		                	  
		                  		file = new File(list.get(0).getPath());
		                  		
		                  		getFile = new FileInputStream ( file );
		                  		
		                        ps = dbcon.connect.prepareStatement(query);
			                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length());
			                    ps.setString(2, null);
			                    ps.setString(3, null);
			                    ps.setString(4, null);
			                    ps.executeUpdate();
			                    
			                    Alert alert = new Alert(AlertType.INFORMATION);
			    				alert.setTitle("Information");
			    				alert.setHeaderText(null);
			    				alert.setContentText("You have successfully updated a record.");
			    				alert.showAndWait();

			    				((Node) event.getSource()).getScene().getWindow().hide();
			    				
		                 }
		                else if ( list.size() == 2  ) {

	                  		file = new File(list.get(0).getPath());
	                  		file1 = new File(list.get(1).getPath());
	                  		
	                  		getFile = new FileInputStream ( file );
	                		getFile1 = new FileInputStream ( file1 );
	                  		
	                        ps = dbcon.connect.prepareStatement(query);
		                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length());
		                    ps.setBinaryStream(2, (InputStream)getFile1, (int) file1.length());
		                    ps.setString(3, null);
		                    ps.setString(4, null);
		                    ps.executeUpdate();
		                    
		                    Alert alert = new Alert(AlertType.INFORMATION);
		    				alert.setTitle("Information");
		    				alert.setHeaderText(null);
		    				alert.setContentText("You have successfully updated a record.");
		    				alert.showAndWait();

		    				((Node) event.getSource()).getScene().getWindow().hide();
		                }
		               else if ( list.size() == 3 ) {

	                 		file = new File(list.get(0).getPath());
	                 		file1 = new File(list.get(1).getPath());
	                 		file2 = new File(list.get(2).getPath());
	                 		
	                 		getFile = new FileInputStream ( file );
	                		getFile1 = new FileInputStream ( file1 );
	                		getFile2 = new FileInputStream ( file2 );
	                 		
	                        ps = dbcon.connect.prepareStatement(query);
		                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length());
		                    ps.setBinaryStream(2, (InputStream)getFile1, (int) file1.length());
		                    ps.setBinaryStream(3, (InputStream)getFile2, (int) file2.length());
		                    ps.setString(4, null);
		                    ps.executeUpdate();
		                    
		                    Alert alert = new Alert(AlertType.INFORMATION);
		    				alert.setTitle("Information");
		    				alert.setHeaderText(null);
		    				alert.setContentText("You have successfully updated a record.");
		    				alert.showAndWait();

		    				((Node) event.getSource()).getScene().getWindow().hide();
		    				
		                }
		              else if ( list.size() == 4 ) {

	                		file = new File(list.get(0).getPath());
	                		file1 = new File(list.get(1).getPath());
	                		file2 = new File(list.get(2).getPath());
	                		file3 = new File(list.get(3).getPath());
	                      
	                		getFile = new FileInputStream ( file );
	                		getFile1 = new FileInputStream ( file1 );
	                		getFile2 = new FileInputStream ( file2 );
	                		getFile3 = new FileInputStream ( file3 );
	                		 
	                		ps = dbcon.connect.prepareStatement(query);
		                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length());
		                    ps.setBinaryStream(2, (InputStream)getFile1, (int) file1.length());
		                    ps.setBinaryStream(3, (InputStream)getFile2, (int) file2.length());
		                    ps.setBinaryStream(4, (InputStream)getFile3, (int) file3.length());
		                    ps.executeUpdate();
		                    
		                    Alert alert = new Alert(AlertType.INFORMATION);
		    				alert.setTitle("Information");
		    				alert.setHeaderText(null);
		    				alert.setContentText("You have successfully updated a record.");
		    				alert.showAndWait();

		    				((Node) event.getSource()).getScene().getWindow().hide();
		                  }
		            }
	            	
		            else if (list == null) {
		            	
		            	 if ( imgBuilding1.getImage() != null && imgBuilding2.getImage() == null 
			            		  && imgBuilding3.getImage() == null && imgBuilding4.getImage() == null ) {
		                	  
		            		 if (getFile == null) 
			                    {
			                        file = new File("src/picture/bldgPhoto1.jpg") ;
			                        getFile = new FileInputStream ( file );
			                    }
			                    
			                    ps = dbcon.connect.prepareStatement(query);
			                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length());
			                    ps.setString(2, null);
			                    ps.setString(3, null);
			                    ps.setString(4, null);
			                    ps.executeUpdate();
		            		 
			                    Alert alert = new Alert(AlertType.INFORMATION);
			    				alert.setTitle("Information");
			    				alert.setHeaderText(null);
			    				alert.setContentText("You have successfully updated a record.");
			    				alert.showAndWait();

			    				((Node) event.getSource()).getScene().getWindow().hide();
			    				
		                 }
		                else if ( imgBuilding1.getImage() != null && imgBuilding2.getImage() != null 
		                		&& imgBuilding3.getImage() == null && imgBuilding4.getImage() == null ) {

		                	if (getFile == null) 
		                    {
		                        file = new File("src/picture/bldgPhoto1.jpg") ;
		                        getFile = new FileInputStream ( file );
		                    }
		                	
		                	if (getFile1 == null) 
		                    {
		                        file1 = new File("src/picture/bldgPhoto2.jpg") ;
		                        getFile1 = new FileInputStream ( file1 );
		                    }
	                  		
	                        ps = dbcon.connect.prepareStatement(query);
		                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length());
		                    ps.setBinaryStream(2, (InputStream)getFile1, (int) file1.length());
		                    ps.setString(3, null);
		                    ps.setString(4, null);
		                    ps.executeUpdate();
		                    
		                    Alert alert = new Alert(AlertType.INFORMATION);
		    				alert.setTitle("Information");
		    				alert.setHeaderText(null);
		    				alert.setContentText("You have successfully updated a record.");
		    				alert.showAndWait();

		    				((Node) event.getSource()).getScene().getWindow().hide();
		                }
		               else if ( imgBuilding1.getImage() != null && imgBuilding2.getImage() != null 
			            		  && imgBuilding3.getImage() != null && imgBuilding4.getImage() == null ) {

		            	   if (getFile == null) 
		                    {
		                        file = new File("src/picture/bldgPhoto1.jpg") ;
		                        getFile = new FileInputStream ( file );
		                    }
		                	
		                	if (getFile1 == null) 
		                    {
		                        file1 = new File("src/picture/bldgPhoto2.jpg") ;
		                        getFile1 = new FileInputStream ( file1 );
		                    }
		                	
		                	if (getFile2 == null) 
		                    {
		                        file2 = new File("src/picture/bldgPhoto3.jpg") ;
		                        getFile2 = new FileInputStream ( file2 );
		                    }
	                 		
	                        ps = dbcon.connect.prepareStatement(query);
		                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length());
		                    ps.setBinaryStream(2, (InputStream)getFile1, (int) file1.length());
		                    ps.setBinaryStream(3, (InputStream)getFile2, (int) file2.length());
		                    ps.setString(4, null);
		                    ps.executeUpdate();
		                    
		                    Alert alert = new Alert(AlertType.INFORMATION);
		    				alert.setTitle("Information");
		    				alert.setHeaderText(null);
		    				alert.setContentText("You have successfully updated a record.");
		    				alert.showAndWait();

		    				((Node) event.getSource()).getScene().getWindow().hide();
		    				
		                }
		              else if ( imgBuilding1.getImage() != null && imgBuilding2.getImage() != null 
		            		  && imgBuilding3.getImage() != null && imgBuilding4.getImage() != null ) {

		            	  	if (getFile == null) 
		                    {
		                        file = new File("src/picture/bldgPhoto1.jpg") ;
		                        getFile = new FileInputStream ( file );
		                    }
		                	
		                	if (getFile1 == null) 
		                    {
		                        file1 = new File("src/picture/bldgPhoto2.jpg") ;
		                        getFile1 = new FileInputStream ( file1 );
		                    }
	                  		
		                	if (getFile2 == null) 
		                    {
		                        file2 = new File("src/picture/bldgPhoto3.jpg") ;
		                        getFile2 = new FileInputStream ( file2 );
		                    }
		                	
		                	if (getFile3 == null) 
		                    {
		                        file3 = new File("src/picture/bldgPhoto4.jpg") ;
		                        getFile3 = new FileInputStream ( file3 );
		                    }
	                  		
	                		ps = dbcon.connect.prepareStatement(query);
		                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length());
		                    ps.setBinaryStream(2, (InputStream)getFile1, (int) file1.length());
		                    ps.setBinaryStream(3, (InputStream)getFile2, (int) file2.length());
		                    ps.setBinaryStream(4, (InputStream)getFile3, (int) file3.length());
		                    ps.executeUpdate();
		                    
		                    Alert alert = new Alert(AlertType.INFORMATION);
		    				alert.setTitle("Information");
		    				alert.setHeaderText(null);
		    				alert.setContentText("You have successfully updated a record.");
		    				alert.showAndWait();

		    				((Node) event.getSource()).getScene().getWindow().hide();
		                  }
		            	 
		            else {

			            	 ps = dbcon.connect.prepareStatement(query);
			                    ps.setString(1, null);
			                    ps.setString(2, null);
			                    ps.setString(3, null);
			                    ps.setString(4, null);
			                    ps.executeUpdate();
			                    
			                    Alert alert = new Alert(AlertType.INFORMATION);
			    				alert.setTitle("Information");
			    				alert.setHeaderText(null);
			    				alert.setContentText("You have successfully updated a record.");
			    				alert.showAndWait();

			    				((Node) event.getSource()).getScene().getWindow().hide();
			    				
			            }
		            }
		            
		            
		            
	            	

	              /*  if (imgBuilding1.getImage() != null) {
	                    
	                    if (getFile == null) 
	                    {
	                        file = new File("") ;
	                        getFile = new FileInputStream ( file );
	                    }
	                    
	                    ps = dbcon.connect.prepareStatement(query);
	                    ps.setBinaryStream (1, (InputStream)getFile, (int) file.length() );
	                    ps.executeUpdate();
	                    
	                }
	                else {
	                	
	                    ps = dbcon.connect.prepareStatement(query);
	                    ps.setString(1, null);
	                    ps.executeUpdate();
	                }

	                Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText(null);
					alert.setContentText("You have successfully updated a record.");
					alert.showAndWait();
					((Node) event.getSource()).getScene().getWindow().hide();
					*/
	                
	            } catch (Exception e) {
	                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err edit building details : " + e.getMessage());
	            } finally {
	                ps.close();
	            }
	        } 
	        
			} else {
				check();
				 
			}

	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		btnCreate.setVisible(true);
		
		primaryStage.setOnCloseRequest(e -> {
			 
			try {
				modular.cancelreq(" BUILDING_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err create building details : " + e2.getMessage());
			}
		});
		

	}

	public void processEditOption(String uID, Stage parent_primaryStage) {
		
		//sizeview();
		btnCancel.setText("CLOSE");
		
		try {
			viewBuilding(Main_Window.dashboard);
			primaryStage = parent_primaryStage;
			btnUpdate.setVisible(true);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err building edit : " +e.getMessage());
		}
		
		primaryStage.setOnCloseRequest(e -> {
			 
			try {
				modular.cancelreq(" BUILDING_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err building details EDIT CANCEL : " + e2.getMessage());
			}
			
		});
		
	}

	/*public void sizeview() {
		
		txtBuildingName.setPrefWidth(410);
		txtAddress.setPrefWidth(410);
		txtContactInfo.setPrefWidth(410);

		lblBuildingName.setLayoutX(535);
		lblAddress.setLayoutX(535);
		lblContactInfo.setLayoutX(535);

	}*/

	
}
