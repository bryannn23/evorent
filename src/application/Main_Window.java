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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.sun.javafx.fxml.builder.JavaFXImageBuilder;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main_Window implements Initializable {

	 @FXML private Label lblWelcomeMessage;
	 @FXML private Label lblDateTime;
	 
	 public DBconnection dbcon = new DBconnection();
	 PreparedStatement ps = null;
	 ResultSet rs = null;
	 
	 public TableColumns tblCol = new TableColumns();
	 public Modular_Class modular = new Modular_Class();
	 
	 //user
	 @FXML private TableView<TableUser> tblUserList;
	 @FXML private Button btnUserView;
	 @FXML private Button btnUserEdit;
	 @FXML private Button btnUserAdd;
	 public static String userlist;
	   
	 //userprofile
	 @FXML private TextField txtProfileUsername;
	 @FXML private TextField txtProfilePassword;
	 @FXML private TextField txtProfilePasswordView;
	 @FXML private TextField txtProfilePIN;
	 @FXML private TextField txtProfilePINCode;
	 @FXML private TextField txtProfileName;
	 @FXML private TextArea txtProfileAddress;
	 @FXML private TextField txtProfileMobileNo;
	 @FXML private TextField txtProfileEmailAdd;
	 @FXML private ImageView imgProfile;
	 @FXML private DatePicker dpProfileBday;
	 
	 @FXML private Button btnProfileChangePassword;
	 @FXML private Button btnProfileChangePINCode;
	 @FXML private Button btnProfileViewPassword;
	 @FXML private Button btnProfileHidePassword;
	 @FXML private Button btnProfileViewPIN;
	 @FXML private Button btnProfileHidePIN;

	 Image image;
	 
	 @FXML private TableView<TableUserTrackLogs> tblProfileLog;
		 
	 //dashboard
	 @FXML Label lblDashboard;
	 @FXML private TableView<TableDashboard> tblDashboard;
	 @FXML private Button btnAddBldg;
	 public static String dashboard;
	 int buildingcount;
		    
	 //ADMINISTRATOR
	 @FXML private TableView<TableAdminDetails> tblAdministrator;
	 @FXML private Button btnAdminPrint;
	 @FXML private Button btnAdminView;
	 @FXML private Button btnAdminDelete;
	 @FXML private Button btnAdminAdd;
	 @FXML private Button btnAdminEdit;
	 public static String admin;
	 public static String adminName;
	 
	 //executive
	 @FXML private TableView<TableExecutive> tblExecutive;
	 @FXML private Button btnExecutivePrint;
	 @FXML private Button btnExecutiveView;
	 @FXML private Button btnExecutiveDelete;
	 @FXML private Button btnExecutiveAdd;
	 @FXML private Button btnExecutiveEdit;
	 public static String exec;
	 public static String execName;
	 
	 //ADMIN MANAGEMENT
	 @FXML private TableView<TableAdminManagement> tblAdminManagement;
	 @FXML private Button btnAdminMngmtDelete;
	 @FXML private Button btnAdminMngmtView;
	 @FXML private Button btnAdminMngmtAdd;
	 @FXML private Button btnAdminMngmtEdit;
	 public static String adminmngmt;
	 
	 @FXML public TabPane tabPane_Modules;
	 @FXML private Tab tabUserCreation;
	 @FXML private Tab tabUserProfile;
	 @FXML private Tab tabProfiles;
	 @FXML private Tab tabDashboard;
	 @FXML private Tab tabSettings;
	 
	//settings
	 @FXML private ImageView imgCompanyLogoHeader;
	@FXML private ImageView imgCompanyLogo;
	@FXML private TextField txtCompanyName;
	@FXML private TextArea txtCompanyAddress;
	@FXML private TextField txtCompanyPhone;
	@FXML private TextField txtCompanyWebsite;
	@FXML private TextField txtCompanyEmailAdd;
	@FXML private TextField txtCompanyTIN;
		
	@FXML private Label lblCompanyLogo;
	@FXML private Label lblCompanyName;
	@FXML private Label lblCompanyAddress;
	@FXML private Label lblCompanyPhone;
	@FXML private Label lblCompanyWebsite;
	@FXML private Label lblCompanyEmailAdd;
	@FXML private Label lblMessage;
		
	Image imgLogo;
	File fileLogo ; 
	FileChooser fileChooser = new FileChooser();
	FileInputStream getFile;
	BufferedImage bufferedImage;
	Stage primaryStage;
	    
	@FXML private Button btnCompanyCreateSetting;
	@FXML private Button btnCompanyAddSetting;
	@FXML private Button btnCompanyEditSetting;
	@FXML private Button btnCompanyUpdate;
	@FXML private Button btnUploadLogo;
	@FXML private Button btnCompanyCancel;
		
	int setting;
	String settingID;
		
	 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		
        main_createTimer();
        showAllowableTabs();
        lblWelcomeMessage.setText("Welcome, " + Globals.G_Employee_Name + "!");
        lblDateTime.setText(new SimpleDateFormat("MMMM d, yyyy | h:mm a").format(new Date()));
        
        initializeSettings();
        
        try {
        	
			initialize_DashboardScreen();
			refresh();
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err user profile details and reload data : " + e.getMessage());
		}
        
        initialize_UserListScreen();
        initialize_AdministratorScreen();
        initialize_ExecutiveScreen();
        initialize_userTrackLogs();
        initialize_AdminManagementScreen();
        
        if (image != null ) {
        	imgProfile.setOnMouseClicked( e-> {
				
				viewImage(image);
				
			});
		}
    	
	}

    public void main_createTimer() {

    	  final Timeline timeline = new Timeline();
          timeline.setCycleCount(Animation.INDEFINITE);
          timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000), e -> {
              lblDateTime.setText(new SimpleDateFormat("MMMM d, yyyy | h:mm a").format(new Date()));
          }));
          timeline.play();
          
    }

    public void showAllowableTabs() {

    	if ( Globals.G_Employee_Access_ID != Globals.G_DEVELOPER_RIGHTS ) {
    		
    		tabPane_Modules.getTabs().remove(tabUserCreation);
    	}
    	if (Globals.G_Employee_Access_ID == Globals.G_ADMIN_RIGHTS) {
    		
    		tabPane_Modules.getTabs().remove(tabProfiles);
    		tabPane_Modules.getTabs().remove(tabSettings);
    	}
    	
    }
    
    /*public void showAllowableTabs() {
		
		tabPane_Modules.getTabs().remove(tabPayables);
		tabPane_Modules.getTabs().remove(tabReceivables);
		tabPane_Supplier.getTabs().remove(tabSupplier_SC);
		tabPane_Projects.getTabs().remove(tabProjects_WO);
		
		if (Globals.G_Employee_Access_ID != Globals.G_DEVELOPER_RIGHTS) {
						
			tabPane_Modules.getTabs().remove(tabConfiguration);
			tabPane_Modules.getTabs().remove(tabTracking);
		}
		if (Globals.G_Employee_Access_ID == Globals.G_MEMBER_RIGHTS)
		{
			tabPane_Modules.getTabs().remove(tabUsers);
			tabPane_Expense.getTabs().remove(tabExpense_EC);
			tabPane_Expense.getTabs().remove(tabExpense_ERT);
			tabPane_Projects.getTabs().remove(tabProjects_CR);
			tabPane_Projects.getTabs().remove(tabProjects_WO);
			tabPane_Projects.getTabs().remove(tabProjects_SPR);
			tabPane_Projects.getTabs().remove(tabProjects_PR);
			tabPane_Payables.getTabs().remove(tabPayables_LP);
			tabPane_Payables.getTabs().remove(tabPayables_NCL);
			tabPane_Modules.getTabs().remove(tabReceivables);
			//tabPane_Modules.getTabs().remove(tabDashboard);
			tabPane_Modules.getTabs().remove(tabBank);
			tabPane_Modules.getTabs().remove(tabPayables);
			tabPane_Modules.getTabs().remove(tabLender);
			tabPane_Capital.getTabs().remove(tabCIList);
			tabPane_Capital.getTabs().remove(tabLender_Record);
			tabPane_Modules.getTabs().remove(tabTracking);
			
			
		}
		if (Globals.G_Employee_Access_ID == Globals.G_ADMIN_RIGHTS){
			tabPane_Modules.getTabs().remove(tabUserProfile);
			tabPane_Supplier.getTabs().remove(tabSupplier_SC);
		//	tabPane_Modules.getTabs().remove(tabDashboard);
			tabPane_Modules.getTabs().remove(tabPayables);
			tabPane_Modules.getTabs().remove(tabReceivables);
			tabPane_Disbursement.getTabs().remove(tabPettyCash);
			tabPane_Modules.getTabs().remove(tabTracking);
			
		}
	}*/

    public void logout(ActionEvent event) throws SQLException {
       
    	Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log-out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            ((Node) event.getSource()).getScene().getWindow().hide();

            openfxml(event, "Log Out", "/application/LogIn.fxml");
            
        } else {
           
            return;
        }
    }
    
    public void refresh() throws SQLException {
    
    	userprofile();
    	
    	buildingcount = dbcon.countBuilding();
    	
    	if (buildingcount != 0) {
    		tblDashboard.setVisible(true);
    		lblDashboard.setVisible(false);

    	}
    	else {
    		lblDashboard.setVisible(true);
    	}
    	
    	tblUserList.getItems().clear();
        dbcon.tblUser(tblUserList, "USER_PROFILE.USER_PROFILE_ID > 1 AND USER_PROFILE.REFERENCE_ID IS NULL");
          
        if (Globals.G_Employee_Access_ID == Globals.G_DEVELOPER_RIGHTS || 
        		Globals.G_Employee_Access_ID == Globals.G_EXECUTIVE_RIGHTS ) {
        	
        	 tblDashboard.getItems().clear();
             dbcon.tblDashboard(tblDashboard, " REFERENCE_ID IS NULL ");
             
        }
        else if (Globals.G_Employee_Access_ID == Globals.G_ADMIN_RIGHTS ) {
        	
        	 tblDashboard.getItems().clear();
             dbcon.tblAdminBuilding(tblDashboard, " BUILDING_DETAILS.REFERENCE_ID IS NULL AND ADMIN_DETAILS.ADMIN_ID = "+ Globals.G_Profile_ID +" ");
             
        }
       
        tblAdministrator.getItems().clear();
        dbcon.tblAdminDetails(tblAdministrator, " REFERENCE_ID IS NULL " );
        
        if ( Globals.G_Employee_Access_ID != Globals.G_DEVELOPER_RIGHTS) {
        
	        tblExecutive.getItems().clear();
	        dbcon.tblExecutiveDetails(tblExecutive, " REFERENCE_ID IS NULL AND EXEC_NAME = '"+ Globals.G_Employee_Name +"' ");
        }
        else {
        	
        	tblExecutive.getItems().clear();
        	dbcon.tblExecutiveDetails(tblExecutive, " REFERENCE_ID IS NULL " );
        }
        
        tblProfileLog.getItems().clear();
        dbcon.tblUserTrackLogs(tblProfileLog);
        
        tblAdminManagement.getItems().clear();
        dbcon.tblAdminManagement(tblAdminManagement, "ADMIN_MANAGEMENT.REFERENCE_ID IS NULL ");
        
        
    	setting = dbcon.countSetting( "COMPANY_SETTINGS" , "REFERENCE_ID IS NULL");
        	
        if (setting != 0) {
        	btnCompanyEditSetting.setVisible(true);
        	btnCompanyCreateSetting.setVisible(false);
        	btnCompanyAddSetting.setVisible(false);
        		
        	settingDisable();
        		
        	btnUploadLogo.setDisable(true);
        	btnCompanyUpdate.setVisible(false);
        	btnCompanyCancel.setVisible(false);	

        }
        else {
        	btnCompanyCreateSetting.setVisible(true); 
        	btnCompanyAddSetting.setVisible(false);
        	btnCompanyCancel.setVisible(false);
        		
        }
        
        SettingView();
        	
       
    }

    public void openfxml(ActionEvent event, String typeOfButton, String filename) {

    	 if (typeOfButton == "TableUser Edit" || typeOfButton == "TableUser View") {
            
    		 if (userlist == null)
                 return;
         } else if (typeOfButton == "TableAdmin Edit" || typeOfButton == "TableAdmin View") {
        	 
        	 if (admin == null)
        		 return;
         } else if (typeOfButton == "TableExec Edit" || typeOfButton == "TableExec View") {
        	 
        	 if (exec == null)
        		 return;
         } else if (typeOfButton == "TableAdminMngmt Edit" || typeOfButton == "TableAdminMngmt View" ) {
        	 
        	 if (adminmngmt == null)
        		 return;
         } else if (typeOfButton == "TableAdmin CreateAccount" ) {
        	 
        	 if (admin == null)
        		 return;
         } else if (typeOfButton == "TableExec CreateAccount") {
        	 
        	 if (exec == null)
        		 return;
         }
    	 
    	 
        try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
            Parent root = (Parent) loader.load();

            if (typeOfButton == "TableUser Add") {
                Create_User cont = loader.getController();
                cont.processAddOption(primaryStage);
                
            } else if (typeOfButton == "TableUser Edit") {
            	Create_User cont = loader.getController();
                cont.processEditOption(userlist, primaryStage);
                
            } else if (typeOfButton == "TableUser View") {
            	Create_User cont = loader.getController();
                cont.processViewOption(userlist);
                cont.btnCancel.setOnAction(e -> primaryStage.close());
                
            } else if (typeOfButton == "BuildingDetail Add") {
            	Create_Building cont = loader.getController();
            	cont.processAddOption(primaryStage);
            	
            } else if (typeOfButton == "TableAdmin Add") {
                Create_Administrator cont = loader.getController();
                cont.processAddOption(primaryStage);
                
            } else if (typeOfButton == "TableAdmin Edit") {
            	Create_Administrator cont = loader.getController();
                cont.processEditOption(admin, primaryStage);
                
            } else if (typeOfButton == "TableAdmin View") {
            	Create_Administrator cont = loader.getController();
                cont.processViewOption(admin);
                cont.btnCancel.setOnAction(e -> primaryStage.close());
                
            }  else if (typeOfButton == "TableExec Add") {
            	Create_Executive cont = loader.getController();
                cont.processAddOption(primaryStage);
                
            } else if (typeOfButton == "TableExec Edit") {
            	Create_Executive cont = loader.getController();
                cont.processEditOption(exec, primaryStage);
                
            } else if (typeOfButton == "TableExec View") {
            	Create_Executive cont = loader.getController();
                cont.processViewOption(exec);
                cont.btnCancel.setOnAction(e -> primaryStage.close());
                
            } else if (typeOfButton == "TableAdminMngmt Add") {
            	Create_Admin_Management cont = loader.getController();
            	cont.processAddOption(primaryStage);
            	
            } else if (typeOfButton == "TableAdminMngmt Edit") {
            	Create_Admin_Management cont = loader.getController();
            	cont.processEditOption(adminmngmt, primaryStage);
            	
            } else if (typeOfButton == "TableAdminMngmt View") {
            	Create_Admin_Management cont = loader.getController();
            	cont.processViewOption(adminmngmt);
            	cont.btnCancel.setOnAction(e -> primaryStage.close());
            	
            } else if (typeOfButton == "TableAdmin CreateAccount" ) {
            	Create_User cont = loader.getController();
            	cont.processAddOption(primaryStage);
            	
            } else if (typeOfButton == "TableExec CreateAccount") {
            	Create_User cont = loader.getController();
            	cont.processAddOption(primaryStage);
            	
            }
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.showAndWait();

            if (typeOfButton == "TableUser Add" || typeOfButton == "TableUser Edit" 
            		|| typeOfButton == "BuildingDetail Add" || typeOfButton == "TableAdmin Add" 
            		|| typeOfButton == "TableAdmin Edit" || typeOfButton == "TableExec Add" 
            		|| typeOfButton == "TableExec Edit" || typeOfButton == "Change Password" 
            		|| typeOfButton == "Change PIN" || typeOfButton ==  "BuildingDetail Add" 
            		|| typeOfButton == "TableAdminMngmt Add" || typeOfButton == "TableAdminMngmt Edit" 
            		|| typeOfButton == "TableAdmin CreateAccount" || typeOfButton == "TableExec CreateAccount" ) {
            	
            	refresh();
            	
            }
            
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen : " + typeOfButton + ": " + e.getMessage());
        }
    }

    public void userprofile() throws SQLException {
    	
        txtProfileUsername.setEditable(false);
        txtProfilePassword.setEditable(false);
        txtProfilePasswordView.setEditable(false);
        txtProfilePIN.setEditable(false);
        txtProfilePINCode.setEditable(false);
        txtProfileName.setEditable(false);
        txtProfileAddress.setEditable(false);
        dpProfileBday.setEditable(false);
        txtProfileMobileNo.setEditable(false);
        txtProfileEmailAdd.setEditable(false);
        txtProfilePasswordView.setVisible(false);
        txtProfilePINCode.setVisible(false);
        
        btnProfileHidePassword.setVisible(false);
        btnProfileHidePIN.setVisible(false);

        String query = "SELECT USER_PROFILE.*, USER_ACCESS_RIGHTS.USER_ACCESS_NAME AS ACCESS, "
        		+ "CASE USER_PROFILE.USER_ACCESS_ID "
				+ "WHEN '1' THEN EXECUTIVE_DETAILS.EXEC_NAME "
				+ "WHEN '2' THEN ADMIN_DETAILS.ADMIN_NAME "
				+ "WHEN '3' THEN PRIMARY_TENANT_DETAILS.TENANT_NAME "
				+ "WHEN '0' THEN 'PROGRAMMER' "
				+ "ELSE ' ' "
				+ "END AS PROFILE_NAME , "
				+ "CASE USER_PROFILE.USER_ACCESS_ID "
				+ "WHEN '1' THEN EXECUTIVE_DETAILS.EXEC_EMAIL_ADD "
				+ "WHEN '2' THEN ADMIN_DETAILS.ADMIN_EMAIL_ADD "
				+ "WHEN '3' THEN PRIMARY_TENANT_DETAILS.TENANT_EMAIL_ADD "
				+ "WHEN '0' THEN 'Mindanao Ave., Quezon City' "
				+ "ELSE ' ' "
				+ "END AS ADDRESS, "
				+ "CASE USER_PROFILE.USER_ACCESS_ID "
				+ "WHEN '1' THEN EXECUTIVE_DETAILS.EXEC_MOBILE_NO "
				+ "WHEN '2' THEN ADMIN_DETAILS.ADMIN_MOBILE_NO "
				+ "WHEN '3' THEN PRIMARY_TENANT_DETAILS.TENANT_MOBILE_NO "
				+ "WHEN '0' THEN '00000' "
				+ "ELSE ' ' "
				+ "END AS MOBILE, "
				+ "CASE USER_PROFILE.USER_ACCESS_ID "
				+ "WHEN '1' THEN EXECUTIVE_DETAILS.EXEC_EMAIL_ADD "
				+ "WHEN '2' THEN ADMIN_DETAILS.ADMIN_EMAIL_ADD "
				+ "WHEN '3' THEN PRIMARY_TENANT_DETAILS.TENANT_EMAIL_ADD "
				+ "WHEN '0' THEN 'jazainnovations.com' "
				+ "ELSE ' ' "
				+ "END AS EADD, "
				+ "CASE USER_PROFILE.USER_ACCESS_ID "
				+ "WHEN '1' THEN EXECUTIVE_DETAILS.EXEC_BDAY "
				+ "WHEN '2' THEN ADMIN_DETAILS.ADMIN_BDAY "
				+ "WHEN '3' THEN PRIMARY_TENANT_DETAILS.TENANT_BDAY "
				+ "WHEN '0' THEN '01-01-17' "
				+ "ELSE ' ' "
				+ "END AS BDAY,  "
				+ "CASE USER_PROFILE.USER_ACCESS_ID "
				+ "WHEN '1' THEN EXECUTIVE_DETAILS.EXEC_PHOTO "
				+ "WHEN '2' THEN ADMIN_DETAILS.ADMIN_PHOTO "
				+ "WHEN '3' THEN PRIMARY_TENANT_DETAILS.TENANT_PHOTO "
				+ "WHEN '0' THEN ' ' "
				+ "ELSE ' ' "
				+ "END AS PHOTO  "
				+ " FROM USER_PROFILE "
				+ "LEFT JOIN USER_ACCESS_RIGHTS ON USER_ACCESS_RIGHTS.USER_ACCESS_ID = USER_PROFILE.USER_ACCESS_ID "
				+ "LEFT JOIN EXECUTIVE_DETAILS ON EXECUTIVE_DETAILS.EXEC_ID = USER_PROFILE.PROFILE_ID "
				+ "LEFT JOIN ADMIN_DETAILS ON ADMIN_DETAILS.ADMIN_ID = USER_PROFILE.PROFILE_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = USER_PROFILE.PROFILE_ID "
				+ " WHERE USER_PROFILE.USER_PROFILE_ID = ?";
        
        String img;

        try {
            ps = dbcon.connect.prepareStatement(query);
            ps.setInt(1, Globals.G_Employee_ID);
            rs = ps.executeQuery();
            if (rs.next()) {
                txtProfileUsername.setText(rs.getString("USERNAME"));
                
                txtProfilePassword.setText(rs.getString("PASSWORD"));
                txtProfilePasswordView.setText(rs.getString("PASSWORD"));
                
                txtProfilePIN.setText(rs.getString("PIN_CODE"));
                txtProfilePINCode.setText(rs.getString("PIN_CODE"));
                
                txtProfileName.setText(rs.getString("PROFILE_NAME"));
                txtProfileAddress.setText(rs.getString("ADDRESS"));
                
                String num = rs.getString("MOBILE");
                if (num.equals("0")) num = "";
                
                txtProfileMobileNo.setText(num);
                txtProfileEmailAdd.setText(rs.getString("EADD"));

                Date bday = rs.getDate("BDAY"); 
                
                if (bday == null) dpProfileBday.getEditor().setText(rs.getString("BDAY"));
                else dpProfileBday.getEditor().setText(rs.getString("BDAY"));
                
                img = rs.getString("PHOTO");
                
                if (img != null) {
                    InputStream input = rs.getBinaryStream("PHOTO");
                    OutputStream output = new FileOutputStream(new File ("src/picture/userPhoto.jpg"));
                    byte[] content = new byte[300000];
                    int size = 0;
                    
                    while ( (size = input.read(content)) != -1) {
                        output.write(content, 0, size);
                    }
                    
                    output.close();
                    input.close();
                    
                    image = new Image( "file:src/picture/userPhoto.jpg", 1200, 1200, false, false);
                    imgProfile.setImage(image);
                }
                else {
                	imgProfile.setImage(null);
                }

                
            }
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err user profile details : " + e.getMessage());
        } finally {
            ps.close();
            rs.close();
        }
        
        if (imgProfile.getImage() == null) imgProfile.setDisable(true);
        else imgProfile.setDisable(false);
        
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
    public void initialize_userTrackLogs () {
    	
    	setupUserTrackLogs_TableView();
    }
    
    public void setupUserTrackLogs_TableView() {

        tblCol.TableUserTrackLogs(tblProfileLog);
        tblProfileLog.setStyle("-fx-table-cell-border-color: transparent; ");

    }
    
    public void passwordView (ActionEvent event) {
    	
    	btnProfileViewPassword.setVisible(false);
    	btnProfileHidePassword.setVisible(true);
    	txtProfilePasswordView.setVisible(true);
    	txtProfilePassword.setVisible(false);
    }
    
    public void passwordHide (ActionEvent event) {
    	
    	btnProfileViewPassword.setVisible(true);
    	btnProfileHidePassword.setVisible(false);
    	txtProfilePasswordView.setVisible(false);
    	txtProfilePassword.setVisible(true);
    }
    
    public void pinView (ActionEvent event) {
    	
    	btnProfileViewPIN.setVisible(false);
    	btnProfileHidePIN.setVisible(true);
    	txtProfilePINCode.setVisible(true);
    	txtProfilePIN.setVisible(false);
    }
    
    public void pinHide (ActionEvent event) {
    	
    	btnProfileViewPIN.setVisible(true);
    	btnProfileHidePIN.setVisible(false);
    	txtProfilePINCode.setVisible(false);
    	txtProfilePIN.setVisible(true);
    }
    
    public void initialize_UserListScreen() {
    	
        btnUserView.setDisable(true);
        btnUserEdit.setDisable(true);

        setupUserList_TableView();
    }

    public void users_chosen(Event e) {

        btnUserView.setDisable(true);
        btnUserEdit.setDisable(true);
    }

    public void setupUserList_TableView() {

        tblCol.TableUser(tblUserList);

        tblUserList.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
				// TODO Auto-generated method stub
				 Pane header = (Pane) tblUserList.lookup("TableHeaderRow");
	                if (header.isVisible()){
	                    header.setMaxHeight(0);
	                    header.setMinHeight(0);
	                    header.setPrefHeight(0);
	                    header.setVisible(false);
	                }
			}
        });
        
        tblUserList.setStyle("-fx-table-cell-border-color: transparent; ");
        
        tblUserList.setOnMouseClicked(e -> {
        	
            try {
                TableUser selected = tblUserList.getSelectionModel().getSelectedItem();
                userlist = selected.getUserid();
                
            } catch (Exception ex) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err user profile display : " + ex.getMessage());
            }

            if (userlist != null) {
                btnUserView.setDisable(false);
                btnUserEdit.setDisable(false);
            }
            
        });
    }

    public void userCreate(ActionEvent event) {

        openfxml(event, "TableUser Add", "/application/CreateUser.fxml");
    }

    public void userEdit(ActionEvent event) {

        openfxml(event, "TableUser Edit", "/application/CreateUser.fxml");
    }

    public void userView(ActionEvent event) {

        openfxml(event, "TableUser View", "/application/CreateUser.fxml");
    }

    public void changepass(ActionEvent event) throws SQLException {

        openfxml(event, "Change Password", "/application/ChangePassword.fxml");

    }  
    
    public void changePIN(ActionEvent event) throws SQLException {

        openfxml(event, "Change PIN", "/application/ChangePINCode.fxml");

    }  

    public void initialize_DashboardScreen() throws SQLException {

    	lblDashboard.setVisible(false);
    	tblDashboard.setVisible(false);
    	
    	setupDashBoard_TableView();
    	
    }
    
    public void setupDashBoard_TableView() {

       tblCol.Dashboard(tblDashboard);

       tblDashboard.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
				// TODO Auto-generated method stub
				 Pane header = (Pane) tblDashboard.lookup("TableHeaderRow");
	                if (header.isVisible()){
	                    header.setMaxHeight(0);
	                    header.setMinHeight(0);
	                    header.setPrefHeight(0);
	                    header.setVisible(false);
	                }
			}
        });
        
        tblDashboard.setStyle("-fx-table-cell-border-color: transparent; ");
        
        tblDashboard.setOnMouseClicked(e -> {

        	try {
                TableDashboard selected = tblDashboard.getSelectionModel().getSelectedItem();
                dashboard = selected.getBldgid();
                
            } catch (Exception ex) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err building list display : " + ex.getMessage());
            }
        	
        	if (dashboard != null) {
        		
        		try {
                	((Node)e.getSource()).getScene().getWindow().hide();
        			Stage primaryStage = new Stage();
        			
        			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/BuildingDetail.fxml"));
        			Parent root = (Parent) loader.load();
        			
        			Building_Detail bldg = loader.getController();
        			//main.showAllowableTabs();
        			
        			Scene scene = new Scene(root);
        			scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
        			primaryStage.setScene(scene);
        			primaryStage.setMaximized(true);
        			primaryStage.setTitle("EvoRent V1.1.0");
        			primaryStage.show();
                	
                	}
                	catch (Exception ex) {
                        EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err building detail pop up : " + ex.getMessage());
                    }

        	}
        
        });
        
    }
    
    public void buildingCreate(ActionEvent event) {

        openfxml(event, "BuildingDetail Add", "/application/CreateBuilding.fxml");
    }

    public void initialize_AdministratorScreen() {
    	
        btnAdminDelete.setDisable(true);
        btnAdminEdit.setDisable(true);
        btnAdminView.setDisable(true);
        setupAdministrator_TableView();
    }

    public void setupAdministrator_TableView() {

        tblCol.TableAdminDetails(tblAdministrator);

        tblAdministrator.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
				// TODO Auto-generated method stub
				 Pane header = (Pane) tblAdministrator.lookup("TableHeaderRow");
	                if (header.isVisible()){
	                    header.setMaxHeight(0);
	                    header.setMinHeight(0);
	                    header.setPrefHeight(0);
	                    header.setVisible(false);
	                }
			}
        });
        
        tblAdministrator.setStyle("-fx-table-cell-border-color: transparent; ");
        
        tblAdministrator.setOnMouseClicked(e -> {
        	
            try {
            	TableAdminDetails selected = tblAdministrator.getSelectionModel().getSelectedItem();
                admin = selected.getAdminID();
                adminName = selected.getAdminName();
                
            } catch (Exception ex) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err admin profile display : " + ex.getMessage());
            }

            if (admin != null) {
                btnAdminDelete.setDisable(false);
                btnAdminEdit.setDisable(false);
                btnAdminView.setDisable(false);
            }
            
        });
    }

    public void adminCreate(ActionEvent event) {

        openfxml(event, "TableAdmin Add", "/application/CreateAdministrator.fxml");
        admin = null;
        
    }

    public void adminEdit(ActionEvent event) {

        openfxml(event, "TableAdmin Edit", "/application/CreateAdministrator.fxml");
        admin = null;
        
    }

    public void adminView(ActionEvent event) {

        openfxml(event, "TableAdmin View", "/application/CreateAdministrator.fxml");
        admin = null;
        
    }
    
    public void adminDelete (ActionEvent event) throws SQLException {
    
    	if (admin == null)
    		return;
    	
    	modular.deletemes("ADMIN_DETAILS", "REFERENCE_ID", admin, "ADMIN_REMARKS", "ADMIN_ID");
    	refresh();
    	admin = null;
    	
    }

    public void initialize_ExecutiveScreen() {
    	
        btnExecutiveDelete.setDisable(true);
        btnExecutiveEdit.setDisable(true);
        btnExecutiveView.setDisable(true);

        setupExecutive_TableView();
    }

    public void setupExecutive_TableView() {

        tblCol.TableExecutive(tblExecutive);

        tblExecutive.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
				// TODO Auto-generated method stub
				 Pane header = (Pane) tblExecutive.lookup("TableHeaderRow");
	                if (header.isVisible()){
	                    header.setMaxHeight(0);
	                    header.setMinHeight(0);
	                    header.setPrefHeight(0);
	                    header.setVisible(false);
	                }
			}
        });
        
        tblExecutive.setStyle("-fx-table-cell-border-color: transparent; ");
        
        tblExecutive.setOnMouseClicked(e -> {
        	
            try {
                TableExecutive selected = tblExecutive.getSelectionModel().getSelectedItem();
                exec = selected.getExecID();
                execName = selected.getExecName();
                
            } catch (Exception ex) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err exec profile display : " + ex.getMessage());
            }

            if (exec != null) {
                btnExecutiveDelete.setDisable(false);
                btnExecutiveEdit.setDisable(false);
                btnExecutiveView.setDisable(false);
            }
            
        });
    }

    public void execCreate(ActionEvent event) {

        openfxml(event, "TableExec Add", "/application/CreateExecutive.fxml");
        exec = null;
        
    }

    public void execEdit(ActionEvent event) {

        openfxml(event, "TableExec Edit", "/application/CreateExecutive.fxml");
        exec = null;
        
    }

    public void execView(ActionEvent event) {

        openfxml(event, "TableExec View", "/application/CreateExecutive.fxml");
        exec = null;
        
    }

    public void execDelete (ActionEvent event) throws SQLException {
    
    	if (exec == null)
    		return;
    	
    	modular.deletemes("EXECUTIVE_DETAILS", "REFERENCE_ID", exec, "EXEC_REMARKS", "EXEC_ID");
    	refresh();
    	exec = null;
    	
    }
    
    public void initialize_AdminManagementScreen() {
    	
        btnAdminMngmtDelete.setDisable(true);
        btnAdminMngmtEdit.setDisable(true);
        btnAdminMngmtView.setDisable(true);

        setupAdminManagement_TableView();
    }

    public void setupAdminManagement_TableView() {

        tblCol.TableAdminMngmt(tblAdminManagement);

        tblAdminManagement.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
				// TODO Auto-generated method stub
				 Pane header = (Pane) tblAdminManagement.lookup("TableHeaderRow");
	                if (header.isVisible()){
	                    header.setMaxHeight(0);
	                    header.setMinHeight(0);
	                    header.setPrefHeight(0);
	                    header.setVisible(false);
	                }
			}
        });
        
        tblAdminManagement.setStyle("-fx-table-cell-border-color: transparent; ");
        
        tblAdminManagement.setOnMouseClicked(e -> {
        	
            try {
                TableAdminManagement selected = tblAdminManagement.getSelectionModel().getSelectedItem();
                adminmngmt = selected.getMngmtID();
                
            } catch (Exception ex) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err admin mngmt table : " + ex.getMessage());
            }

            if (adminmngmt != null) {
                btnAdminMngmtDelete.setDisable(false);
                btnAdminMngmtEdit.setDisable(false);
                btnAdminMngmtView.setDisable(false);
            }
            
        });
    }

    public void adminMngmtCreate(ActionEvent event) {

        openfxml(event, "TableAdminMngmt Add", "/application/CreateAdminManagement.fxml");
    }

    public void adminMngmtEdit(ActionEvent event) {

        openfxml(event, "TableAdminMngmt Edit", "/application/CreateAdminManagement.fxml");
    }

    public void adminMngmtView(ActionEvent event) {

        openfxml(event, "TableAdminMngmt View", "/application/CreateAdminManagement.fxml");
    }

    public void adminMngmtDelete (ActionEvent event) throws SQLException {
    
    	if (adminmngmt == null)
    		return;
    	
    	modular.deletemes("ADMIN_MANAGEMENT", "REFERENCE_ID", adminmngmt, "MNGMT_REMARKS", "MNGMT_ID");
    	refresh();
    	adminmngmt = null;
    	
    }
    
	public void initializeSettings() {
		
		try {
			settingID = dbcon.getSettingID();
		} catch (SQLException e1) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err get id of company setting: " +e1.getMessage());
		}
		
		btnCompanyCreateSetting.setVisible(false);
		btnCompanyAddSetting.setVisible(false);
		btnCompanyEditSetting.setVisible(false);
		btnCompanyUpdate.setVisible(false);
		btnCompanyCancel.setVisible(false);
		
		settingClear();
		
		TextFormatter<String> tfname = modular.getTextFlexiFormatter(100, 'a'); 
		txtCompanyName.setTextFormatter(tfname);
		TextFormatter<String> tfadd = modular.getTextFlexiFormatter(1000, 'a'); 
		txtCompanyAddress.setTextFormatter(tfadd);
		TextFormatter<String> tfphone = modular.getTextFlexiFormatter(50, 'a'); 
		txtCompanyPhone.setTextFormatter(tfphone);
		TextFormatter<String> tfweb = modular.getTextFlexiFormatter(100, 'a'); 
		txtCompanyWebsite.setTextFormatter(tfweb);
		TextFormatter<String> tfeadd = modular.getTextFlexiFormatter(100, 'a'); 
		txtCompanyEmailAdd.setTextFormatter(tfeadd);
		TextFormatter<String> tftin = modular.getTextFlexiFormatter(50, 'a'); 
		txtCompanyTIN.setTextFormatter(tftin);
		
		
		imgCompanyLogo.setOnMouseClicked( ea-> {
			
			if (imgCompanyLogo.getImage() != null) {
				   
			        try {
			            Stage primaryStage = new Stage();
			            primaryStage.initModality(Modality.APPLICATION_MODAL);
			            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View_Image.fxml"));
			            Parent root = (Parent) loader.load();

			            View_Image cont = loader.getController();
			            cont.processViewImage(primaryStage, imgLogo);
			            cont.btnClose.setOnAction( e -> primaryStage.close());
			            
			            Scene scene = new Scene(root);
			            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
			            primaryStage.setScene(scene);
			            primaryStage.showAndWait();

			        } catch (Exception e) {
			            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error view image : " + e.getMessage());
			        } }
			        else {
			           
			        	lblCompanyLogo.setVisible(true);
			            lblMessage.setVisible(true);
			            lblMessage.setText("No uploaded image.");
			            return;
			        }
		});
		
		settingDisable();
		
	}

	public void createSetting (ActionEvent event) {
		
		settingEnable();
		btnCompanyCreateSetting.setVisible(false);
		btnCompanyAddSetting.setVisible(true);
		btnCompanyCancel.setVisible(true);
		
	}
	
	public void cancelSetting (ActionEvent event) throws SQLException {
		
		refresh();
		settingDisable();
		
	}
	
	public void settingDisable () {
		
		txtCompanyName.setDisable(true);
    	txtCompanyAddress.setDisable(true);
    	txtCompanyPhone.setDisable(true);
    	txtCompanyWebsite.setDisable(true);
    	txtCompanyEmailAdd.setDisable(true);
    	txtCompanyTIN.setDisable(true);
    	
    	btnUploadLogo.setDisable(true);
    	
	}
	
	public void settingEnable () {
		
		txtCompanyName.setDisable(false);
    	txtCompanyAddress.setDisable(false);
    	txtCompanyPhone.setDisable(false);
    	txtCompanyWebsite.setDisable(false);
    	txtCompanyEmailAdd.setDisable(false);
    	txtCompanyTIN.setDisable(false);
    	
    	btnUploadLogo.setDisable(false);
	}
	
	public void settingClear() {
		
		lblMessage.setVisible(false);
		lblCompanyLogo.setVisible(false);
		lblCompanyName.setVisible(false);
		lblCompanyAddress.setVisible(false);
		lblCompanyPhone.setVisible(false);
		lblCompanyWebsite.setVisible(false);
		lblCompanyEmailAdd.setVisible(false);
	}
	
	public void chooseFile (ActionEvent event) throws FileNotFoundException {

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg", "*.gif", "*.bmp") ;
        
        fileChooser.getExtensionFilters().addAll(extFilter);
        fileLogo = fileChooser.showOpenDialog(primaryStage);
        
        try {
             bufferedImage = ImageIO.read(fileLogo);
             imgLogo = SwingFXUtils.toFXImage(bufferedImage, null);
             imgCompanyLogo.setImage(imgLogo);
           
        } catch (IOException ex) {
          Logger.getLogger(JavaFXImageBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
        getFile = new FileInputStream ( fileLogo );
        
    }

	public void SettingView ( ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM COMPANY_SETTINGS WHERE REFERENCE_ID IS NULL AND SETTING_ID = "+ settingID +" ";
		
		String imglogo ;
		
		try{
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				txtCompanyName.setText(rs.getString("COMPANY_NAME"));
				txtCompanyAddress.setText(rs.getString("COMPANY_ADDRESS"));
				txtCompanyPhone.setText(rs.getString("COMPANY_PHONE"));
				txtCompanyWebsite.setText(rs.getString("COMPANY_WEBSITE"));
				txtCompanyEmailAdd.setText(rs.getString("COMPANY_EMAIL_ADDRESS"));
				txtCompanyTIN.setText(rs.getString("COMPANY_TIN_NO"));
				
				 imglogo = rs.getString("COMPANY_LOGO");
	                
		            if (imglogo != null) {
		                  InputStream input = rs.getBinaryStream("COMPANY_LOGO");
		                  OutputStream output = new FileOutputStream(new File ("src/picture/CompanyLogo101.jpg"));
		                  byte[] content = new byte[300000];
		                  int size = 0;
		                    
		                  while ( (size = input.read(content)) != -1) {
		                     output.write(content, 0, size);	
		                  }
		                    
		                  output.close();
		                  input.close();
		                    
		                  imgLogo = new Image( "file:src/picture/CompanyLogo101.jpg", 1000, 1500, true, true);
		                  imgCompanyLogo.setImage(imgLogo);
		                  imgCompanyLogoHeader.setImage(imgLogo);
		              }
		            else {
		                 imgCompanyLogo.setImage(null);
		                 imgCompanyLogoHeader.setImage(null);
		                 
		            }

				
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display setting details : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void SettingAdd (ActionEvent event) throws SQLException {
		
		if ( imgCompanyLogo.getImage() != null && !txtCompanyName.getText().isEmpty() && 
				!txtCompanyAddress.getText().isEmpty() )
		{

	        String check = "SELECT * FROM COMPANY_SETTINGS WHERE REFERENCE_ID IS NULL AND SETTING_ID <> "+ settingID +" ";
	        
	        ps = dbcon.connect.prepareStatement(check);
	        rs = ps.executeQuery();
	        
	        if (rs.next()) {
	            lblMessage.setVisible(true);
	            lblMessage.setText("Record already exist.");
	            
	        } else {
	            
	            String query = "INSERT INTO COMPANY_SETTINGS VALUES ( 0, "
	            		+ " ?, '"+ txtCompanyName.getText().replaceAll("'", "`") +"', "
						+ " '"+ txtCompanyAddress.getText().replaceAll("'", "`") +"', "
						+ " '"+ txtCompanyPhone.getText().replaceAll("'", "`") +"', "
						+ " '"+ txtCompanyWebsite.getText().replaceAll("'", "`") +"', "
						+ " '"+ txtCompanyEmailAdd.getText().replaceAll("'", "`") +"', "
						+ " '"+ txtCompanyTIN.getText().replaceAll("'", "`") +"', "
						+ " now(), "+ Globals.G_Employee_ID +", null, null, null )";
	            
	            try {
	            	
	                if (imgCompanyLogo.getImage() != null) {
	                	
	                    ps = dbcon.connect.prepareStatement(query);
	                    ps.setBinaryStream (1, (InputStream)getFile, (int) fileLogo.length() );
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
	    			alert.setContentText("You have successfully added a record.");
	    			alert.showAndWait();
	    			
	                refresh();
	                getFile = null;
	                settingClear();
	
	            } catch (Exception e) {
	                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert COMPANY_SETTINGS : " + e.getMessage());
	            } finally {
	                ps.close();
	            }
	        } 
	        }
	        else {
	        	
	        	settingCheck();
	        	
	        }
        
	}
	
	public void SettingEdit (ActionEvent event) {
		
		settingEnable();
		
		btnCompanyUpdate.setVisible(true);
		btnCompanyCancel.setVisible(true);
		
	}
	
	public void upd() throws SQLException {

		modular.updaterecord(" COMPANY_SETTINGS ", " COMPANY_LOGO, COMPANY_NAME, COMPANY_ADDRESS, COMPANY_PHONE, "
				+ "COMPANY_WEBSITE, COMPANY_EMAIL_ADDRESS, COMPANY_TIN_NO, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
			
			" COMPANY_LOGO, COMPANY_NAME, COMPANY_ADDRESS, COMPANY_PHONE, COMPANY_WEBSITE,"
			+ "COMPANY_EMAIL_ADDRESS, COMPANY_TIN_NO, "
			+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "+ settingID +" ",
			"  REFERENCE_ID IS NULL AND SETTING_ID = "+ settingID +" ");
			
		}
	
	public void SettingUpdate (ActionEvent event) throws SQLException {

		if ( imgCompanyLogo.getImage() != null && !txtCompanyName.getText().isEmpty() && 
				!txtCompanyAddress.getText().isEmpty()  )
		{

			upd();
        	
		String check = "SELECT * FROM COMPANY_SETTINGS WHERE REFERENCE_ID IS NULL AND SETTING_ID <> "+ settingID +" ";

        ps = dbcon.connect.prepareStatement(check);
        rs = ps.executeQuery();
        if (rs.next()) {
            lblMessage.setVisible(true);
            lblMessage.setText("Record already exist.");
            
        } else {
        	
        	
            String query = "UPDATE COMPANY_SETTINGS SET COMPANY_LOGO = ?, COMPANY_NAME = '"+ txtCompanyName.getText().replaceAll("'", "`") +"', "
						+ " COMPANY_ADDRESS = '"+ txtCompanyAddress.getText().replaceAll("'", "`")+"', "
						+ " COMPANY_PHONE = '"+ txtCompanyPhone.getText().replaceAll("'", "`") +"', "
						+ " COMPANY_WEBSITE = '"+ txtCompanyWebsite.getText().replaceAll("'", "`") +"', "
						+ " COMPANY_EMAIL_ADDRESS = '"+ txtCompanyEmailAdd.getText().replaceAll("'", "`") +"', "
						+ " COMPANY_TIN_NO = '"+ txtCompanyTIN.getText().replaceAll("'", "`") +"', "
						+ " DATETIME_ENTRY = now(), SYSTEM_ACCOUNT_ID = "+ Globals.G_Employee_ID +" WHERE "
						+ "REFERENCE_ID IS NULL AND SETTING_ID = "+ settingID +" ";

            try {

                if (imgCompanyLogo.getImage() != null) {
                    
                    if (getFile == null) 
                    {
                        fileLogo = new File("src/picture/CompanyLogo101.jpg") ;
                        getFile = new FileInputStream (fileLogo);
                    }
                    
                    ps = dbcon.connect.prepareStatement(query);
                    ps.setBinaryStream (1, (InputStream)getFile, (int) fileLogo.length() );
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
				
                refresh();
                getFile = null;
                settingClear();
                
            } catch (Exception e) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err edit company settings : " + e.getMessage());
            } finally {
                ps.close();
            }
            
        	}
		}
        else {
        	
        	settingCheck();
        }
	
	}
	
	public void settingCheck() {
		
		if (imgCompanyLogo.getImage() == null) lblCompanyLogo.setVisible(true);
    	else lblCompanyLogo.setVisible(false);
    	
    	if (txtCompanyName.getText().isEmpty()) lblCompanyName.setVisible(true);
    	else lblCompanyName.setVisible(false);
    	
    	if (txtCompanyAddress.getText().isEmpty()) lblCompanyAddress.setVisible(true);
    	else lblCompanyAddress.setVisible(false);
    	
    	lblMessage.setVisible(true);
    	
    	
	}

	
}
