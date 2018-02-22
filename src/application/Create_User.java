package application;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Create_User implements Initializable {
	
	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();

	@FXML private TextField txtUsername;
	@FXML private TextField txtPassword;
	@FXML private TextField txtConfirmPass;
	@FXML private TextField txtunmaskpass;
	@FXML private TextField txtunmaskcpass;
	@FXML private TextField txtPINCode;
	
	@FXML private ComboBox<String> cmbUserAccess;
	@FXML private ComboBox<String> cmbUserStatus;
	@FXML private ComboBox<String> cmbUser;

	@FXML private Label lblUsername;
	@FXML private Label lblPassword;
	@FXML private Label lblConfirmPass;
	@FXML private Label lblUserAccess;
	@FXML private Label lblUserStatus;
	@FXML private Label lblStatus;
	@FXML private Label lblPin;
	@FXML private Label lblPINCode;
	@FXML private Label lblus;
	@FXML private Label lblUser;
	
	@FXML private VBox vb;

	@FXML Button btnShowPass;
	@FXML Button btnHidePass;
	@FXML Button btnCancel;
	@FXML Button btnUpdate;
	@FXML Button btnUpdate1;
	@FXML Button btnCreate;
	@FXML Button btnFinalize;
	
	@FXML private TableView<TableUser> tblFinalize;
	
	private Stage primaryStage;
	public static String user = null;

	int access, ustatus ;
	String profile;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnUpdate.setVisible(false);
		btnUpdate1.setVisible(false);
		btnCreate.setVisible(false);
		btnFinalize.setDisable(true);
		btnHidePass.setVisible(false);
		txtunmaskpass.setVisible(false);
		txtunmaskcpass.setVisible(false);
		lblPINCode.setVisible(false);
		btnFinalize.setDisable(true);

		lblUsername.setVisible(false);
		lblPassword.setVisible(false);
		lblConfirmPass.setVisible(false);
		lblUserAccess.setVisible(false);
		lblUserStatus.setVisible(false);
		lblUser.setVisible(false);
		lblStatus.setVisible(false);
		
		lblus.setVisible(false);
		cmbUserStatus.setVisible(false);

		TextFormatter<String> tf = getTextFormatter();
		txtUsername.setTextFormatter(tf);
		TextFormatter<String> tfpass = modular.getTextFlexiFormatter(15, 'a'); 
		txtPassword.setTextFormatter(tfpass);
		TextFormatter<String> tfcpass = modular.getTextFlexiFormatter(15, 'a');
		txtConfirmPass.setTextFormatter(tfcpass);
		TextFormatter<String> tfup = modular.getTextFlexiFormatter(15, 'a');
		txtunmaskpass.setTextFormatter(tfup);
		TextFormatter<String> tfucp = modular.getTextFlexiFormatter(15, 'a');
		txtunmaskcpass.setTextFormatter(tfucp);
		TextFormatter<String> tfpin = modular.getTextFlexiFormatter(7, 'n');
		txtPINCode.setTextFormatter(tfpin);

		txtunmaskpass.textProperty().bindBidirectional(txtPassword.textProperty());
		txtunmaskcpass.textProperty().bindBidirectional(txtConfirmPass.textProperty());
		
		cmbUserStatus.getItems().addAll("ACTIVE", "DEACTIVE" );

		cmbUserStatus.setOnAction(e -> {
			if (cmbUserStatus.getValue().equals("ACTIVE"))
				ustatus = 1;
			else if (cmbUserStatus.getValue().equals("DEACTIVE"))
				ustatus = 0;
			
		});

		try {
			dbcon.cmbDisplay(cmbUserAccess, "*", "USER_ACCESS_NAME", " USER_ACCESS_RIGHTS ", "USER_ACCESS_ID > 0" );
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of user access right: " +e.getMessage());
		}

		cmbUserAccess.setOnAction(e -> {
			
			if (cmbUserAccess.getValue().equals("PROGRAMMER")) {
				
				access = 0;
			}
			else if (cmbUserAccess.getValue().equals("EXECUTIVE")) {
				
				access = 1;
				txtPINCode.setVisible(true);
				lblPin.setVisible(true);
				cmbUser.getItems().clear();
				
				try {
					dbcon.cmbDisplay(cmbUser, "*", "EXEC_NAME", " EXECUTIVE_DETAILS ", " REFERENCE_ID IS NULL ");
					
				} catch (SQLException e1) {
					EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of exec: " +e1.getMessage());
				}
				
			} else if (cmbUserAccess.getValue().equals("ADMINISTRATOR")) {
				
				access = 2;
				txtPINCode.setVisible(true);
				lblPin.setVisible(true);
				cmbUser.getItems().clear();
				
				
				try {
					dbcon.cmbDisplay(cmbUser, "*", "ADMIN_NAME", " ADMIN_DETAILS ", " REFERENCE_ID IS NULL ");
					
				} catch (SQLException e1) {
					EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of admin: " +e1.getMessage());
				}
				
			} else if (cmbUserAccess.getValue().equals("TENANT")) {
				
				access = 3;
				txtPINCode.setVisible(false);
				lblPin.setVisible(false);
				cmbUser.getItems().clear();
				
				try {
					dbcon.cmbDisplay(cmbUser, "*", "TENANT_NAME", " PRIMARY_TENANT_DETAILS ", " REFERENCE_ID IS NULL ");
					
				} catch (SQLException e1) {
					EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of tenant: " +e1.getMessage());
				}
			} 
		});

		new AutoCompleteComboBoxListener<String>(cmbUserAccess);
		new AutoCompleteComboBoxListener<String>(cmbUserStatus);
		new AutoCompleteComboBoxListener<String>(cmbUser);
		
		setupUserRecord_TableView();

	}

	public void setupUserRecord_TableView() {

		tblCol.TableUser(tblFinalize); 

		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableUser selected = tblFinalize.getSelectionModel().getSelectedItem();
				user = selected.getUserid();
				userview(user);
				
			} catch (Exception ex) {
			}
			if (user != null) {
				btnCreate.setVisible(false);
				btnUpdate.setVisible(true);
			} else
				return;
		});
	}

	public void create(ActionEvent event) throws SQLException {

		lblStatus.setText("");
		
		if (access != 3 && txtPINCode.getText().isEmpty()) {
			
			lblPINCode.setVisible(true);
			lblStatus.setText("Field required!");
			
		}
		else {
			
			if ((!txtUsername.getText().isEmpty()) && (!txtPassword.getText().isEmpty())
					&& (!txtConfirmPass.getText().isEmpty())  && (cmbUserAccess.getValue() != null) 
					&& cmbUser.getValue() != null ) 
			{
				
				if ((txtUsername.getText().length() >= 5) && (txtPassword.getText().length() >= 5)) {
					
					if (txtPassword.getText().equals(txtConfirmPass.getText())) {
						
						setval();
						
						modular.create(" USER_PROFILE ", " USERNAME = '"+txtUsername.getText()+"' AND "
								+ "USER_ACCESS_ID = "+ access +" AND PROFILE_ID = "+profile+" ", 
								lblStatus, 
								" 0, '"+txtUsername.getText()+"', '"+txtPassword.getText()+"', '"+txtPINCode.getText()+"', "
								+ " 1, "+access+", "+profile+", now(), "+Globals.G_Employee_ID+", 'Y', "
								+ " null, null, null ", event);
						 
						clear();
						
					} else {
						lblPassword.setVisible(true);
						lblConfirmPass.setVisible(true);
						lblStatus.setVisible(true);
						lblStatus.setText("Password and confirm password do not match!");
						 
					}
				} else {
					
					if (txtUsername.getText().length() < 5)
						lblUsername.setVisible(true);
					else
						lblUsername.setVisible(false);
					
					if (txtPassword.getText().length() < 5)
						lblPassword.setVisible(true);
					else
						lblPassword.setVisible(false);
					
					lblStatus.setVisible(true);
					lblStatus.setText("Username or password must have at least 5 or more characters.");
					 	
				}
			} else {
				check();
				 
			}
		}
	}

	public void check() {

		if (txtUsername.getText().isEmpty())
			lblUsername.setVisible(true);
		else
			lblUsername.setVisible(false);
		
		if (txtPassword.getText().isEmpty())
			lblPassword.setVisible(true);
		else
			lblPassword.setVisible(false);
		
		if (txtConfirmPass.getText().isEmpty())
			lblConfirmPass.setVisible(true);
		else
			lblConfirmPass.setVisible(false);
		
		if (cmbUserAccess.getValue() == null)
			lblUserAccess.setVisible(true);
		else
			lblUserAccess.setVisible(false);
		
		if (cmbUser.getValue() == null)
			lblUser.setVisible(true);
		else
			lblUser.setVisible(false);
		
		lblStatus.setVisible(true);
		lblStatus.setText("Field required!");
	}

	public void clear() throws SQLException {
		
		if (lblStatus.getText().equals("Record already exist.")) {
			
			lblStatus.setVisible(true);
			lblStatus.setText("Record already exist.");
			
		} else {
			
			txtUsername.clear();
			txtPassword.clear();
			txtConfirmPass.clear();
			txtunmaskcpass.clear();
			txtunmaskpass.clear();
			cmbUserAccess.setValue(null);
			txtPINCode.clear();
			cmbUser.setValue(null);
			cmbUserStatus.setValue(null);

			dbcon.tblUser(tblFinalize, " USER_PROFILE.REFERENCE_ID IS NULL AND USER_PROFILE.FINALIZED_RECORD = 'N' ");
			btnFinalize.setDisable(false);
			user = null;
			
			lblUsername.setVisible(false);
			lblPassword.setVisible(false);
			lblConfirmPass.setVisible(false);
			lblStatus.setVisible(false);
			lblUserAccess.setVisible(false);
			lblUser.setVisible(false);
			
		}
	}

	public void setval() throws SQLException {
	
		if (cmbUserAccess.getValue().equalsIgnoreCase("Programmer")) {
		
			access = 0;
			profile = "0";
		}
		
		else if (cmbUserAccess.getValue().equalsIgnoreCase("Executive")) {
			
			access = 1;
			profile = dbcon.getIDs("*", "EXECUTIVE_DETAILS", "EXEC_ID", "REFERENCE_ID IS NULL AND EXEC_NAME = '"+ cmbUser.getValue() +"' ");
		}
		
		else if (cmbUserAccess.getValue().equalsIgnoreCase("Administrator")) {
		
			access = 2;
			profile = dbcon.getIDs("*", "ADMIN_DETAILS", "ADMIN_ID", "REFERENCE_ID IS NULL AND ADMIN_NAME = '"+ cmbUser.getValue() +"' ");
		}
		
		else if (cmbUserAccess.getValue().equalsIgnoreCase("Tenant")) {
		
			access = 3;
			profile = dbcon.getIDs("*", "PRIMARY_TENANT_DETAILS", "TENANT_ID", "REFERENCE_ID IS NULL AND TENANT_NAME = '"+ cmbUser.getValue() +"' ");
		}
			
	}
	
	public void finalize(ActionEvent event) throws SQLException {
		  
		modular.finalize("USER_PROFILE");
		((Node) event.getSource()).getScene().getWindow().hide();

	}

	public void showPass(ActionEvent event) {
		txtunmaskpass.setVisible(true);
		txtunmaskcpass.setVisible(true);
		txtPassword.setVisible(false);
		txtConfirmPass.setVisible(false);
		btnShowPass.setVisible(false);
		btnHidePass.setVisible(true);

		String ps = txtPassword.getText();
		String cps = txtConfirmPass.getText();
		txtunmaskpass.setText(ps);
		txtunmaskcpass.setText(cps);

	}

	public void hidePass(ActionEvent event) {
		txtunmaskpass.setVisible(false);
		txtunmaskcpass.setVisible(false);
		txtPassword.setVisible(true);
		txtConfirmPass.setVisible(true);
		btnShowPass.setVisible(true);
		btnHidePass.setVisible(false);

		String ps = txtPassword.getText();
		String cps = txtConfirmPass.getText();
		txtunmaskpass.setText(ps);
		txtunmaskcpass.setText(cps);

	}

	public void cancel() throws SQLException {
		 
		modular.cancel("USER_PROFILE", primaryStage);

	}

	public void update(ActionEvent event) throws SQLException {
		
		lblStatus.setText("");
		
		if (access != 3 && txtPINCode.getText().isEmpty()) {
			
			lblPINCode.setVisible(true);
			lblStatus.setText("Field required!");
			
		} else {
			
			if ((!txtUsername.getText().isEmpty()) && (!txtPassword.getText().isEmpty())
					&& (!txtConfirmPass.getText().isEmpty()) && (cmbUserAccess.getValue() != null) 
					&& (cmbUser.getValue() != null) ) {
				
				if ((txtUsername.getText().length() >= 5) && (txtPassword.getText().length() >= 5)) {
					
					if (txtPassword.getText().equals(txtConfirmPass.getText())) {

						setval();
						
						modular.editrecordwithfinalize(" USER_PROFILE ", " USERNAME = '"+txtUsername.getText()+"' AND "
								+ "USER_ACCESS_ID = "+ access +" AND PROFILE_ID = "+ profile +" "
								+ " AND USER_PROFILE_ID <> "+user+" ",
								lblStatus, 
								" USERNAME = '"+txtUsername.getText()+"', PASSWORD = '"+txtPassword.getText()+"', PIN_CODE= '"+txtPINCode.getText()+"', "
								+ " USER_STATUS = "+ ustatus +", USER_ACCESS_ID = "+ access +", PROFILE_ID = "+ profile +", "
								+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ", 
								" USER_PROFILE_ID = "+user+" ", 
								event, 
								btnUpdate, btnCreate);
						
						clear();
						
					} else {
						lblPassword.setVisible(true);
						lblConfirmPass.setVisible(true);
						lblStatus.setVisible(true);
						lblStatus.setText("Password and confirm password do not match!");
						 
					}
					
				} else {
					
					if (txtUsername.getText().length() < 5)
						lblUsername.setVisible(true);
					else
						lblUsername.setVisible(false);
					if (txtPassword.getText().length() < 5)
						lblPassword.setVisible(true);
					else
						lblPassword.setVisible(false);
					
					lblStatus.setVisible(true);
					lblStatus.setText("Username or password must have at least 5 or more characters.");
					 
				}
			} else {
				check();

			}
		}
	}

	public void userview(String id) throws SQLException {
		
		String query = "SELECT USER_PROFILE.*, USER_ACCESS_RIGHTS.USER_ACCESS_NAME AS ACCESS, "
				+ "CASE USER_PROFILE.USER_ACCESS_ID "
				+ "WHEN '1' THEN EXECUTIVE_DETAILS.EXEC_NAME "
				+ "WHEN '2' THEN ADMIN_DETAILS.ADMIN_NAME "
				+ "WHEN '3' THEN PRIMARY_TENANT_DETAILS.TENANT_NAME "
				+ "WHEN '0' THEN 'PROGRAMMER' "
				+ "ELSE ' ' "
				+ "END AS PROFILE_NAME  "
				+ " FROM USER_PROFILE "
				+ "LEFT JOIN USER_ACCESS_RIGHTS ON USER_ACCESS_RIGHTS.USER_ACCESS_ID = USER_PROFILE.USER_ACCESS_ID "
				+ "LEFT JOIN EXECUTIVE_DETAILS ON EXECUTIVE_DETAILS.EXEC_ID = USER_PROFILE.PROFILE_ID "
				+ "LEFT JOIN ADMIN_DETAILS ON ADMIN_DETAILS.ADMIN_ID = USER_PROFILE.PROFILE_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = USER_PROFILE.PROFILE_ID "
				+ " WHERE USER_PROFILE.USER_PROFILE_ID = "+id+" ";
		
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()){	
				
				txtUsername.setText(rs.getString("USERNAME"));
				txtPassword.setText(rs.getString("PASSWORD"));
				txtunmaskpass.setText(rs.getString("PASSWORD"));
				txtConfirmPass.setText(rs.getString("PASSWORD"));
				txtunmaskcpass.setText(rs.getString("PASSWORD"));
				
				ustatus = rs.getInt("USER_STATUS");
				if (ustatus == 1) cmbUserStatus.setValue("Active"); 
				else if (ustatus == 0) cmbUserStatus.setValue("Deactive");

				access = rs.getInt("USER_ACCESS_ID");
				cmbUserAccess.setValue(rs.getString("ACCESS"));
				
				if (access == 0) {
					
					cmbUserAccess.setValue("PROGRAMMER");
					txtPINCode.setVisible(true);
					lblPin.setVisible(true);
					txtPINCode.setText(rs.getString("PIN_CODE"));
				}
				else if (access == 1) {
					
					cmbUserAccess.setValue("EXECUTIVE");
					txtPINCode.setVisible(true);
					lblPin.setVisible(true);
					txtPINCode.setText(rs.getString("PIN_CODE"));
				}
				else if (access == 2)  {
					
					cmbUserAccess.setValue("ADMINISTRATOR");
					txtPINCode.setVisible(true);
					lblPin.setVisible(true);
					txtPINCode.setText(rs.getString("PIN_CODE"));
				}
				else if (access == 3) 
					cmbUserAccess.setValue("TENANT");	
				
				cmbUser.setValue(rs.getString("PROFILE_NAME"));
			}
		}	
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected user "+id+" : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void upd() throws SQLException {

		modular.updaterecord(" USER_PROFILE ", " USERNAME, PASSWORD, PIN_CODE, USER_STATUS, USER_ACCESS_ID, PROFILE_ID, "
				+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", " USERNAME, PASSWORD, PIN_CODE, USER_STATUS, USER_ACCESS_ID, PROFILE_ID, "
				+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "+Main_Window.userlist+" ", " USER_PROFILE_ID = "+Main_Window.userlist+" ");
	}

	public void updateM(ActionEvent event) throws SQLException {

		lblStatus.setText("");
		
		if (access != 3 && txtPINCode.getText().isEmpty()) {
			
			lblPINCode.setVisible(true);
			lblStatus.setText("Field required!");
		} 
		else {
			
			if ((!txtUsername.getText().isEmpty()) && (!txtPassword.getText().isEmpty())
					&& (!txtConfirmPass.getText().isEmpty()) && (cmbUserAccess.getValue() != null) 
					&& (cmbUser.getValue() != null) ) {
				
				if ((txtUsername.getText().length() >= 5) && (txtPassword.getText().length() >= 5)) {
					if (txtPassword.getText().equals(txtConfirmPass.getText())) {
						
						upd();
						setval();
							
						modular.editrecord(" USER_PROFILE ", " USERNAME = '"+txtUsername.getText()+"' AND "
								+ "USER_ACCESS_ID = "+ access +" AND PROFILE_ID = "+ profile+" "
								+ " AND USER_PROFILE_ID <> "+Main_Window.userlist+" ",
								lblStatus, 
								" USERNAME = '"+txtUsername.getText()+"', PASSWORD = '"+txtPassword.getText()+"',  "
								+ " PIN_CODE = '"+ txtPINCode.getText()+"' , USER_STATUS = "+ustatus+", "
								+ "USER_ACCESS_ID = "+access+", PROFILE_ID = "+ profile +" , "
								+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
								" USER_PROFILE_ID = "+Main_Window.userlist+" ", 
								event);
						
					} else {
						
						lblPassword.setVisible(true);
						lblConfirmPass.setVisible(true);
						lblStatus.setVisible(true);
						lblStatus.setText("Password and confirm password do not match!");
						 
					}
				} else {
					
					if (txtUsername.getText().length() < 5)
						lblUsername.setVisible(true);
					else
						lblUsername.setVisible(false);
					if (txtPassword.getText().length() < 5)
						lblPassword.setVisible(true);
					else
						lblPassword.setVisible(false);
					
					lblStatus.setVisible(true);
					lblStatus.setText("Username or password must have at least 5 or more characters.");
					 
				}
				
			} else {
				check();
				 
			}
		}

	}

	public void processAddOption(Stage parent_primaryStage) {

		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
			 
			try {
				modular.cancelreq("USER_PROFILE", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err userprofile : " + e2.getMessage());
			}
		});
		btnCreate.setVisible(true);
		
		if (Tenant_Detail.tenant != null) {
			
			cmbUserAccess.setDisable(true);
			cmbUser.setDisable(true);
			cmbUserAccess.setValue("TENANT");
			cmbUser.setValue(Tenant_Detail.tenantName);
			
		}
		else if (Main_Window.admin != null) {

			cmbUserAccess.setDisable(true);
			cmbUser.setDisable(true);
			cmbUserAccess.setValue("ADMINISTRATOR");
			cmbUser.setValue(Main_Window.adminName);

		}
		else if (Main_Window.exec != null) {

			cmbUserAccess.setDisable(true);
			cmbUser.setDisable(true);
			cmbUserAccess.setValue("EXECUTIVE");
			cmbUser.setValue(Main_Window.execName);
			
		}

	}

	public void processEditOption(String uID, Stage parent_primaryStage) {
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		btnCancel.setText("CLOSE");
		
		vb.setMaxWidth(0);
		
		sizeview();
		
		txtunmaskpass.setPrefWidth(380);
		txtPassword.setPrefWidth(380);
		btnShowPass.setLayoutX(490);
		btnHidePass.setLayoutX(490);
		
		try {
			userview(Main_Window.userlist);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err UPedit : " + e.getMessage());
		}
	
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
			 
			try {
				modular.cancelreq("USER_PROFILE", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err userprofile : " + e2.getMessage());
			}
			
		});
		
		btnUpdate1.setVisible(true);
		cmbUserStatus.setVisible(true);
		lblus.setVisible(true);

	}

	public void sizeview() {
		
		cmbUserStatus.setPrefWidth(410);
		cmbUser.setPrefWidth(410);
		cmbUserAccess.setPrefWidth(410);
		
		txtUsername.setPrefWidth(410);
		txtConfirmPass.setPrefWidth(410);
		txtunmaskcpass.setPrefWidth(410);
		txtPINCode.setPrefWidth(410);

		lblPassword.setLayoutX(535);
		lblConfirmPass.setLayoutX(535);
		lblStatus.setLayoutX(535);
		lblUsername.setLayoutX(535);
		lblUserAccess.setLayoutX(535);
		lblPINCode.setLayoutX(535);
		lblUser.setLayoutX(535);
		
	}

	public void processViewOption(String uID) {
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		
		sizeview();
		btnCancel.setText("CLOSE");
		
		btnShowPass.setLayoutX(490);
		btnHidePass.setLayoutX(490);
		btnShowPass.setVisible(true);
		btnHidePass.setVisible(true);
		
		txtunmaskpass.setPrefWidth(380);
		txtPassword.setPrefWidth(380);
		
		txtUsername.setDisable(true);
		txtPassword.setDisable(true);
		txtConfirmPass.setDisable(true);
		cmbUserAccess.setDisable(true);
		cmbUserStatus.setDisable(true);
		cmbUser.setDisable(true);
		txtunmaskpass.setDisable(true);
		txtunmaskpass.setDisable(true);
		txtPINCode.setDisable(true);

		try {
			userview(Main_Window.userlist);
			tblFinalize.setDisable(true);
			cmbUserStatus.setVisible(true);
			lblus.setVisible(true);

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err user record view : " + e.getMessage());
		}
		 
	}

	public TextFormatter<String> getTextFormatter() {
		UnaryOperator<Change> filter = getFilter();
		TextFormatter<String> tf = new TextFormatter<>(filter);
		return tf;

	}

	private UnaryOperator<Change> getFilter() {
		return change -> {

			String text = change.getText();
			if (change.isContentChange()) {
				if (change.getControlNewText().length() > 15)
					return null;
			}
			if (text.matches("[A-Za-z0-9]+") || text.isEmpty()) {
				return change;
			}

			return null;
		};
	}
	
}
