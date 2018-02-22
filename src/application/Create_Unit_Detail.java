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

public class Create_Unit_Detail implements Initializable {

	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();

	@FXML private ComboBox<String> cmbLocation;
	@FXML private ComboBox<String> cmbType;
	@FXML private ComboBox<String> cmbFeatures;
	@FXML private ComboBox<String> cmbCondition;
	@FXML private ComboBox<String> cmbFloors;
	
	@FXML private TextArea txtRemarks;

	@FXML private TextField txtName;
	@FXML private TextField txtArea;
	@FXML private TextField txtOtherFeatures;
	@FXML private TextField txtBedroom;
	@FXML private TextField txtBathroom;
	@FXML private TextField txtMaxCapacity;
	@FXML private TextField txtRentAmount;
	@FXML private TextField txtAssocAmount;
	
	@FXML private ImageView imgPhoto;
	
	@FXML private Label lblName;
	@FXML private Label lblFloors;
	@FXML private Label lblLocation;
	@FXML private Label lblType;
	@FXML private Label lblArea;
	@FXML private Label lblFeatures;
	@FXML private Label lblOtherFeatures;
	@FXML private Label lblBedroom;
	@FXML private Label lblBathroom;
	@FXML private Label lblMaxCapacity;
	@FXML private Label lblCondition;
	@FXML private Label lblRentAmount;
	@FXML private Label lblAssocAmount;
	@FXML private Label lblPhoto;
	@FXML private Label lblRemarks;
	@FXML private Label lblMessage;
	
	@FXML private TableView<TableUnitDetail> tblFinalize;
	
	@FXML private Button btnChoose;
	@FXML private Button btnCreate;
	@FXML Button btnCancel;
	@FXML private Button btnUpdate;
	@FXML private Button btnUpdateM;
	@FXML private Button btnFinalize;
	
	@FXML private VBox vb;
	
	String location, type, features, condition, unitDetails;
	Stage primaryStage;
	 
	FileChooser fileChooser = new FileChooser();
    Image image;
    File file;
    FileInputStream getFile;
    BufferedImage bufferedImage;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnUpdate.setVisible(false);
		btnUpdateM.setVisible(false);
		btnCreate.setVisible(false);
		btnFinalize.setDisable(true);

		lblName.setVisible(false);
		lblFloors.setVisible(false);
		lblLocation.setVisible(false);
		lblType.setVisible(false);
		lblArea.setVisible(false);
		lblFeatures.setVisible(false);
		lblOtherFeatures.setVisible(false);
		lblBedroom.setVisible(false);
		lblBathroom.setVisible(false);
		lblMaxCapacity.setVisible(false);
		lblCondition.setVisible(false);
		lblRentAmount.setVisible(false);
		lblAssocAmount.setVisible(false);
		lblPhoto.setVisible(false);
		lblRemarks.setVisible(false);
		lblMessage.setVisible(false);

		TextFormatter<String> tfname = modular.getTextFlexiFormatter(100, 'a'); 
		txtName.setTextFormatter(tfname);
		TextFormatter<String> tfarea = modular.getTextFlexiFormatter(100, 'a');
		txtArea.setTextFormatter(tfarea);
		TextFormatter<String> tfother = modular.getTextFlexiFormatter(1000, 'a');
		txtOtherFeatures.setTextFormatter(tfother);
		TextFormatter<String> tfbed = modular.getTextFlexiFormatter(1, 'n');
		txtBedroom.setTextFormatter(tfbed);
		TextFormatter<String> tfbath = modular.getTextFlexiFormatter(1, 'n');
		txtBathroom.setTextFormatter(tfbath);
		TextFormatter<String> tfmax = modular.getTextFlexiFormatter(10, 'n');
		txtMaxCapacity.setTextFormatter(tfmax);
		TextFormatter<String> tfrent = modular.getTextFlexiFormatter(12, 'n');
		txtRentAmount.setTextFormatter(tfrent);
		TextFormatter<String> tfassoc = modular.getTextFlexiFormatter(12, 'n');
		txtAssocAmount.setTextFormatter(tfassoc);
		TextFormatter<String> tfrem = modular.getTextFlexiFormatter(1000, 'a');
		txtRemarks.setTextFormatter(tfrem);
		//TextFormatter<String> tffloor = modular.getTextFlexiFormatter(2, 'n');
		//txtFloors.setTextFormatter(tffloor);
			
		for (int i = 1; i <= Integer.parseInt(Building_Detail.bldgFloors); i++) {
			
			String a = Integer.toString(i);
			cmbFloors.getItems().addAll(a);
		}
		
		comboBox.unitLocation(cmbLocation);
		comboBox.unitFeatures(cmbFeatures);
		comboBox.condition(cmbCondition);
		
		new AutoCompleteComboBoxListener<String>(cmbLocation);
		new AutoCompleteComboBoxListener<String>(cmbType);
		new AutoCompleteComboBoxListener<String>(cmbFeatures);
		new AutoCompleteComboBoxListener<String>(cmbCondition);
		
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
			           
			        	lblPhoto.setVisible(true);
			            lblMessage.setVisible(true);
			            lblMessage.setText("No uploaded image.");
			            return;
			        }
		});
		

		try {
			
			dbcon.cmbDisplay(cmbType, "*", "UNIT_TYPE_NAME", "UNIT_TYPE", " REFERENCE_ID IS NULL AND BLDG_ID = "+ Main_Window.dashboard +" " );
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of building: " +e.getMessage());
		}
		
		cmbType.setOnAction( e-> {
			
			try {
				 dbcon.unitPrice(cmbType, txtRentAmount, txtArea, txtMaxCapacity);
			} catch (SQLException e1) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err get selected type: " +e1.getMessage());
			}
			
		});
		
		setupUnitDetails_TableView();
		
	}

	public void setupUnitDetails_TableView() {

		tblCol.TableUnitDetail(tblFinalize);

		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableUnitDetail selected = tblFinalize.getSelectionModel().getSelectedItem();
				unitDetails = selected.getUID();
				unitDet(unitDetails);
				
			} catch (Exception ex) {
			}
			if (unitDetails != null) {
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
			
		if (!txtName.getText().isEmpty() && cmbFloors.getValue() != null && cmbType.getValue() != null )
		{
			
			setval();
				
			modular.createWithPhotoFinal(" UNIT_DETAILS ", " UNIT_NAME = '"+ txtName.getText().replaceAll("'", "`") +"' AND "
					+ " BLDG_ID = "+ Main_Window.dashboard +" ", 
					lblMessage, 
					" 0, '"+ txtName.getText().replaceAll("'", "`") +"' , "+ Main_Window.dashboard +", "+ cmbFloors.getValue() +","
					+ " "+ location +", "+ type +", "
					+ "'"+ txtArea.getText().replaceAll("'", "`") +"', "+ features +", '"+ txtOtherFeatures.getText().replaceAll("'", "`")+"', "
					+ " "+ txtBedroom.getText().replaceAll(",", "") +", "+ txtBathroom.getText().replaceAll(",", "")+", "
					+ " "+ txtMaxCapacity.getText().replaceAll(",", "") +", "+ condition +", "+ txtRentAmount.getText().replaceAll(",", "")+", "
					+ " "+ txtAssocAmount.getText().replaceAll(",", "")+", '"+ txtRemarks.getText().replaceAll("'", "`") +"', ?, now(),"
					+ " "+Globals.G_Employee_ID+", 'N', null, null, null " , 
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
			
		if (cmbFloors.getValue() == null )
			lblFloors.setVisible(true);
		else
			lblFloors.setVisible(false);
		
		if (cmbType.getValue() == null )
			lblType.setVisible(true);
		else
			lblType.setVisible(false);
		
			lblMessage.setVisible(true);
			lblMessage.setText("Field required!");
			
		}

	public void clear() throws SQLException {
			
		if (lblMessage.getText().equals("Record already exist.")) {
				
			lblMessage.setVisible(true);
			lblMessage.setText("Record already exist.");
				
		} else {
				
			txtName.clear();
			cmbFloors.setValue(null);
			cmbLocation.setValue(null);
			cmbType.setValue(null);
			txtArea.clear();
			cmbFeatures.setValue(null);
			txtOtherFeatures.clear();
			txtBedroom.clear();
			txtBathroom.clear();
			txtMaxCapacity.clear();
			cmbCondition.setValue(null);
			txtRentAmount.clear();
			txtAssocAmount.clear();
			txtRemarks.clear();
			imgPhoto.setImage(null);
				
			dbcon.tblUnitDetail(tblFinalize, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.FINALIZED_RECORD = 'N' ");
			btnFinalize.setDisable(false);
			unitDetails = null;
				
			lblName.setVisible(false);
			lblFloors.setVisible(false);
			lblLocation.setVisible(false);
			lblType.setVisible(false);
			lblArea.setVisible(false);
			lblFeatures.setVisible(false);
			lblOtherFeatures.setVisible(false);
			lblBedroom.setVisible(false);
			lblBathroom.setVisible(false);
			lblMaxCapacity.setVisible(false);
			lblCondition.setVisible(false);
			lblRentAmount.setVisible(false);
			lblAssocAmount.setVisible(false);
			lblPhoto.setVisible(false);
			lblRemarks.setVisible(false);
			lblMessage.setVisible(false);
				
		}
	}
		
	public void cancel() throws SQLException {
			 
		modular.cancel("UNIT_DETAILS", primaryStage);

	}
		
	public void setval () throws SQLException {
			
		if (txtBedroom.getText().isEmpty())
			txtBedroom.setText("0");
		
		if (txtBathroom.getText().isEmpty()) 
			txtBathroom.setText("0");
		
		if (txtMaxCapacity.getText().isEmpty())
			txtMaxCapacity.setText("0");
		
		if (txtRentAmount.getText().isEmpty())
			txtRentAmount.setText("0");
		
		if (txtAssocAmount.getText().isEmpty())
			txtAssocAmount.setText("0");
		
		if (cmbLocation.getValue() == null)
			location = null;
		else if (cmbLocation.getValue().equalsIgnoreCase("Not Applicable"))
			location = "0";
		else if (cmbLocation.getValue().equalsIgnoreCase("North"))
			location = "1";
		else if (cmbLocation.getValue().equalsIgnoreCase("South"))
			location = "2";
		else if (cmbLocation.getValue().equalsIgnoreCase("East"))
			location = "3";
		else if (cmbLocation.getValue().equalsIgnoreCase("West"))
			location = "4";
		else if (cmbLocation.getValue().equalsIgnoreCase("North East"))
			location = "5";
		else if (cmbLocation.getValue().equalsIgnoreCase("North West"))
			location = "6";
		else if (cmbLocation.getValue().equalsIgnoreCase("South East"))
			location = "7";
		else if (cmbLocation.getValue().equalsIgnoreCase("South West"))
			location = "8";
		
		
		type = dbcon.getIDs("*", "UNIT_TYPE", "UNIT_TYPE_ID", "REFERENCE_ID IS NULL AND UNIT_TYPE_NAME = '"+ cmbType.getValue() +"' AND "
				+ "BLDG_ID = "+ Main_Window.dashboard+" ");
		
		if (cmbFeatures.getValue() == null ) 
			features = null;
		else if (cmbFeatures.getValue().equalsIgnoreCase("Bare"))
			features = "0";
		else if (cmbFeatures.getValue().equalsIgnoreCase("Partially Furnished"))
			features = "1";
		else if (cmbFeatures.getValue().equalsIgnoreCase("Furnished"))
			features = "2";
		else if (cmbFeatures.getValue().equalsIgnoreCase("Fully Furnished"))
			features = "3";
		
		if (cmbCondition.getValue() == null)
			condition = null;
		else if (cmbCondition.getValue().equalsIgnoreCase("New"))
			condition = "0";
		else if (cmbCondition.getValue().equalsIgnoreCase("Well Maintained"))
			condition = "1";
		else if (cmbCondition.getValue().equalsIgnoreCase("Needs Renovation"))
			condition = "2";
		else if (cmbCondition.getValue().equalsIgnoreCase("Under Renovation"))
			condition = "3";
		else if (cmbCondition.getValue().equalsIgnoreCase("Renovated"))
			condition = "4";
		else if (cmbCondition.getValue().equalsIgnoreCase("Others"))
			condition = "5";
		
			
	}

	public void finalize(ActionEvent event) throws SQLException {
			  
		modular.finalize("UNIT_DETAILS");
		((Node) event.getSource()).getScene().getWindow().hide();

	}
		
	public void unitDet(String id) throws SQLException {
			
		String query = "SELECT UNIT_DETAILS.*, BUILDING_DETAILS.BLDG_NAME AS BLDG, "
				+ " UNIT_TYPE.UNIT_TYPE_NAME AS TYPE, "
				+ "UNIT_TYPE.UNIT_TYPE_AREA AS AREA, UNIT_TYPE.UNIT_TYPE_PRICE AS PRICE, "
				+ "UNIT_TYPE.UNIT_TYPE_MAX_CAPACITY AS MAX "
				+ " FROM UNIT_DETAILS "
				+ "LEFT JOIN BUILDING_DETAILS ON BUILDING_DETAILS.BLDG_ID = UNIT_DETAILS.BLDG_ID "
				+ "LEFT JOIN UNIT_TYPE ON UNIT_TYPE.UNIT_TYPE_ID = UNIT_DETAILS.UNIT_TYPE_ID "
				+ " WHERE UNIT_DETAILS.UNIT_ID = "+id+" ";
			 
		String img, loc, feature, condi, area, price, max; 
		
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
					
				txtName.setText(rs.getString("UNIT_NAME"));
				cmbFloors.setValue(rs.getString("UNIT_FLOOR"));
				
				loc = rs.getString("UNIT_LOCATION");
				
				if (loc == null) cmbLocation.setValue(null);
				else if (loc.equals("0")) cmbLocation.setValue("Not Applicable");
				else if (loc.equals("1")) cmbLocation.setValue("North");
				else if (loc.equals("2")) cmbLocation.setValue("South");
				else if (loc.equals("3")) cmbLocation.setValue("East");
				else if (loc.equals("4")) cmbLocation.setValue("West");
				else if (loc.equals("5")) cmbLocation.setValue("North East");
				else if (loc.equals("6")) cmbLocation.setValue("North West");
				else if (loc.equals("7")) cmbLocation.setValue("South East");
				else if (loc.equals("8")) cmbLocation.setValue("South West");
				
				cmbType.setValue(rs.getString("TYPE"));
				
				area = rs.getString("UNIT_AREA");
				if (area.equals(null) || area.equals("")) txtArea.setText(rs.getString("AREA"));
				else txtArea.setText(rs.getString("UNIT_AREA"));
				
				feature = rs.getString("UNIT_FEATURES");
				
				if (feature == null) cmbFeatures.setValue(null);
				else if (feature.equals("0")) cmbFeatures.setValue("Bare");
				else if (feature.equals("1")) cmbFeatures.setValue("Partially Furnished");
				else if (feature.equals("2")) cmbFeatures.setValue("Furnished");
				else if (feature.equals("3")) cmbFeatures.setValue("Fully Furnished");
				
				txtOtherFeatures.setText(rs.getString("UNIT_OTHER_FEATURE"));
				txtBedroom.setText(rs.getString("UNIT_BEDROOM"));
				txtBathroom.setText(rs.getString("UNIT_BATHROOM"));
				
				max = rs.getString("UNIT_MAX_CAPACITY");
				
				if (!max.equals("0")) txtMaxCapacity.setText(rs.getString("UNIT_MAX_CAPACITY"));
				else txtMaxCapacity.setText(rs.getString("MAX"));
				
				condi = rs.getString("UNIT_CONDITION");
				
				if (condi == null) cmbCondition.setValue(null);
				else if (condi.equals("0")) cmbCondition.setValue("NEW");
				else if (condi.equals("1")) cmbCondition.setValue("WELL MAINTAINED");
				else if (condi.equals("2")) cmbCondition.setValue("NEEDS RENOVATION");
				else if (condi.equals("3")) cmbCondition.setValue("UNDER RENOVATION");
				else if (condi.equals("4")) cmbCondition.setValue("RENOVATED");
				else if (condi.equals("5")) cmbCondition.setValue("OTHERS");
				
				price = rs.getString("UNIT_RENT_AMOUNT");
				
				if (!price.equals("0")) txtRentAmount.setText(rs.getString("UNIT_RENT_AMOUNT"));
				else txtRentAmount.setText(rs.getString("PRICE"));
				
				txtAssocAmount.setText(rs.getString("UNIT_ASSOC_AMOUNT"));
 				txtRemarks.setText(rs.getString("UNIT_REMARKS"));
 				 
 				img = rs.getString("UNIT_PHOTO");
                
                if (img != null) {
                    InputStream input = rs.getBinaryStream("UNIT_PHOTO");
                    OutputStream output = new FileOutputStream(new File ("src/picture/unitPhoto.jpg"));
                    byte[] content = new byte[3000];
                    int size = 0;
                    
                    while ( (size = input.read(content)) != -1) {
                        output.write(content, 0, size);
                    }
                    
                    output.close();
                    input.close();
                    
                    image = new Image( "file:src/picture/unitPhoto.jpg", 1000, 1500, true, true);
                    imgPhoto.setImage(image);
                }
                else {
                    imgPhoto.setImage(null);
                }
                
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected unitDetails "+id+" : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
		}

	public void upd() throws SQLException {

		modular.updaterecord(" UNIT_DETAILS ", " UNIT_NAME, BLDG_ID, UNIT_FLOOR, UNIT_LOCATION, UNIT_TYPE_ID, UNIT_AREA, "
			+ "UNIT_FEATURES, UNIT_OTHER_FEATURE, UNIT_BEDROOM, UNIT_BATHROOM, UNIT_MAX_CAPACITY, UNIT_CONDITION, UNIT_RENT_AMOUNT, "
			+ " UNIT_ASSOC_AMOUNT, UNIT_REMARKS, UNIT_PHOTO, "
			+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
			
			" UNIT_NAME, BLDG_ID, UNIT_FLOOR, UNIT_LOCATION, UNIT_TYPE_ID, UNIT_AREA, "
			+ "UNIT_FEATURES, UNIT_OTHER_FEATURE, UNIT_BEDROOM, UNIT_BATHROOM, UNIT_MAX_CAPACITY, UNIT_CONDITION, UNIT_RENT_AMOUNT,"
			+ "UNIT_ASSOC_AMOUNT, UNIT_REMARKS, UNIT_PHOTO, "
			+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "+ Building_Detail.unitDetails +" ",
			" UNIT_ID = "+Building_Detail.unitDetails+" ");
			
		}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtName.getText().isEmpty() && cmbFloors.getValue() != null && cmbType.getValue() != null )
		{
			setval();
				
			modular.editwithPhotoFinal(" UNIT_DETAILS ", " UNIT_NAME = '"+ txtName.getText().replaceAll("'", "`") +"' AND "
					+ "BLDG_ID = "+ Main_Window.dashboard +""
					+ " AND UNIT_ID <> "+ unitDetails +" " ,
					lblMessage,
					" UNIT_NAME = '"+ txtName.getText().replaceAll("'", "`") +"', "
					+ " UNIT_FLOOR = "+ cmbFloors.getValue() +","
					+ " UNIT_LOCATION = "+ location +", UNIT_TYPE_ID = "+ type +", UNIT_AREA = '"+ txtArea.getText().replaceAll("'", "`") +"' , "
					+ " UNIT_FEATURES = "+ features +" , UNIT_OTHER_FEATURE = '"+ txtOtherFeatures.getText().replaceAll("'", "`") +"', "
					+ " UNIT_BEDROOM = '"+ txtBedroom.getText().replaceAll(",", "") +"', UNIT_BATHROOM = '"+ txtBathroom.getText().replaceAll(",", "") +"', "
					+ " UNIT_MAX_CAPACITY = '"+ txtMaxCapacity.getText().replaceAll(",", "")+"', "
					+ " UNIT_CONDITION = "+ condition +", UNIT_RENT_AMOUNT = "+ txtRentAmount.getText().replaceAll(",", "")+", "
					+ " UNIT_ASSOC_AMOUNT = "+ txtAssocAmount.getText().replaceAll(",", "")+", "
					+ " UNIT_REMARKS = '"+ txtRemarks.getText().replaceAll("'", "`") +"',"
					+ "UNIT_PHOTO = ?, DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" UNIT_ID = "+ unitDetails +" ", 
					imgPhoto, file, getFile,  "src/picture/unitPhoto.jpg", 
					event, btnUpdate, btnCreate);
				
				clear();
							
		} else {
			check();
					 
		}

	}

	public void updateM(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtName.getText().isEmpty() && cmbFloors.getValue() != null && cmbType.getValue() != null )
		{
						
			upd();
			setval();
				
			modular.editwithPhoto(" UNIT_DETAILS ", " UNIT_NAME = '"+ txtName.getText().replaceAll("'", "`") +"' AND "
					+ " BLDG_ID = "+ Main_Window.dashboard +""
					+ " AND UNIT_ID <> "+ Building_Detail.unitDetails +" " ,
					lblMessage,
					" UNIT_NAME = '"+ txtName.getText().replaceAll("'", "`") +"', UNIT_FLOOR = "+ cmbFloors.getValue() +", "
					+ " UNIT_LOCATION = "+ location +", "
					+ "UNIT_TYPE_ID = "+ type +", UNIT_AREA = '"+ txtArea.getText().replaceAll("'", "`") +"' , UNIT_FEATURES = "+ features +" , "
					+ "UNIT_OTHER_FEATURE = '"+ txtOtherFeatures.getText().replaceAll("'", "`") +"', UNIT_BEDROOM = '"+ txtBedroom.getText().replaceAll(",", "") +"', "
					+ " UNIT_BATHROOM = '"+ txtBathroom.getText().replaceAll(",", "") +"', UNIT_MAX_CAPACITY = '"+ txtMaxCapacity.getText().replaceAll(",", "")+"', "
					+ " UNIT_CONDITION = "+ condition +", UNIT_RENT_AMOUNT = "+ txtRentAmount.getText().replaceAll(",", "")+", "
					+ " UNIT_ASSOC_AMOUNT = "+ txtAssocAmount.getText().replaceAll(",", "")+", "
					+ " UNIT_REMARKS = '"+ txtRemarks.getText().replaceAll("'", "`") +"',"
					+ "UNIT_PHOTO = ?, DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" UNIT_ID = "+ Building_Detail.unitDetails +" ", 
					imgPhoto, file, getFile,  "src/picture/unitPhoto.jpg", 
					event );
							
		} else {
			check();
					 
		}

	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
		try {
			modular.cancelreq(" UNIT_DETAILS ", primaryStage, e);
		} catch (SQLException e2) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unitDetails : " + e2.getMessage());
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
			
		try {
			unitDet(Building_Detail.unitDetails);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unitDetails EDIT : " + e.getMessage());
		}
		
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
			try {
				modular.cancelreq("UNIT_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err bldg unitDetails EDIT CANCEL : " + e2.getMessage());
			}
				
		});
			
	}

	public void sizeview() {
			
		txtName.setPrefWidth(410);
		cmbFloors.setPrefWidth(410);
		cmbLocation.setPrefWidth(410);
		cmbType.setPrefWidth(410);
		txtArea.setPrefWidth(410);
		cmbFeatures.setPrefWidth(410);
		txtOtherFeatures.setPrefWidth(410);
		txtBedroom.setPrefWidth(410);
		txtBathroom.setPrefWidth(410);
		txtMaxCapacity.setPrefWidth(410);
		cmbCondition.setPrefWidth(410);
		txtRentAmount.setPrefWidth(410);
		txtAssocAmount.setPrefWidth(410);
		txtRemarks.setPrefWidth(410);
		imgPhoto.setFitWidth(410);

		lblMessage.setLayoutX(535);
		lblName.setLayoutX(535);
		lblFloors.setLayoutX(535);
		lblLocation.setLayoutX(535);
		lblType.setLayoutX(535);
		lblArea.setLayoutX(535);
		lblFeatures.setLayoutX(535);
		lblOtherFeatures.setLayoutX(535);
		lblBedroom.setLayoutX(535);
		lblBathroom.setLayoutX(535);
		lblMaxCapacity.setLayoutX(535);
		lblCondition.setLayoutX(535);
		lblRentAmount.setLayoutX(535);
		lblAssocAmount.setLayoutX(535);
		lblRemarks.setLayoutX(535);
		lblPhoto.setLayoutX(535);
		
	}

	public void processViewOption(String uID) {
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		btnCancel.setText("CLOSE");
		
		txtName.setDisable(true);
		cmbFloors.setDisable(true);
		cmbLocation.setDisable(true);
		cmbType.setDisable(true);
		txtArea.setDisable(true);
		cmbFeatures.setDisable(true);
		txtOtherFeatures.setDisable(true);
		txtBedroom.setDisable(true);
		txtBathroom.setDisable(true);
		txtMaxCapacity.setDisable(true);
		cmbCondition.setDisable(true);
		txtRentAmount.setDisable(true);
		txtAssocAmount.setDisable(true);
		txtRemarks.setDisable(true);
		imgPhoto.setDisable(true);

		try {
			unitDet(Building_Detail.unitDetails);

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unitDetails record view : " + e.getMessage());
		}
			 
	}
		
}
