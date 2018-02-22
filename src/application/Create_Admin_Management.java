package application;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class Create_Admin_Management implements Initializable {
	
	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	
	@FXML private ComboBox<String> cmbAdmin;
	@FXML private ComboBox<String> cmbBuilding;
	
	@FXML private RadioButton rdInactive;
	@FXML private RadioButton rdActive;
	
	ToggleGroup rdStatus = new ToggleGroup();
	
	@FXML private TextArea txtRemarks;
	
	@FXML private Label lblAdmin;
	@FXML private Label lblBuilding;
	@FXML private Label lblRemarks;
	@FXML private Label lblMngmtStatus;
	@FXML private Label lblMessage;
	
	@FXML private Button btnCreate;
	@FXML private Button btnUpdate;
	@FXML  Button btnCancel;
	
	String admin, bldg;
	int status;
	
	Stage primaryStage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnUpdate.setVisible(false);
		btnCreate.setVisible(false);

		lblAdmin.setVisible(false);
		lblBuilding.setVisible(false);
		lblRemarks.setVisible(false);
		lblMngmtStatus.setVisible(false);
		lblMessage.setVisible(false);

		TextFormatter<String> tfrem = modular.getTextFlexiFormatter(1000, 'a'); 
		txtRemarks.setTextFormatter(tfrem);
		
		rdInactive.setToggleGroup(rdStatus);
		rdActive.setToggleGroup(rdStatus);
		
		try {
			dbcon.cmbDisplay(cmbAdmin, "*", "ADMIN_NAME", " ADMIN_DETAILS ", " REFERENCE_ID IS NULL ");
			dbcon.cmbDisplay(cmbBuilding, "*", "BLDG_NAME", " BUILDING_DETAILS ", " REFERENCE_ID IS NULL "); //AND BLDG_ID = "+ Main_Window.dashboard +"
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of admin, building: " +e.getMessage());
		}
		
		new AutoCompleteComboBoxListener<String>(cmbAdmin);
		new AutoCompleteComboBoxListener<String>(cmbBuilding);
		
	}

	public void create(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (cmbAdmin.getValue() != null && cmbBuilding.getValue() != null && 
				(rdInactive.isSelected() || rdActive.isSelected()) )
		{
		
			setval();
			
			modular.create(" ADMIN_MANAGEMENT ", " ADMIN_ID = "+ admin +" AND "
					+ "BLDG_ID = "+ bldg +" AND MNGMT_STATUS = "+ status +" ", 
					lblMessage, 
					" 0, "+ admin+", "+ bldg +", "+status+", '"+ txtRemarks.getText().replaceAll("'", "`")+"', "
					+ " now(), "+Globals.G_Employee_ID+", 'N', null, null, null " , 
					 event );
		
			} else {
				check();
				 
			}
	}

	public void check() {

		if (cmbAdmin.getValue() == null)
			lblAdmin.setVisible(true);
		else
			lblAdmin.setVisible(false);
		
		if (cmbBuilding.getValue() == null)
			lblBuilding.setVisible(true);
		else
			lblBuilding.setVisible(false);
		
		if (!rdInactive.isSelected() && !rdActive.isSelected()) 
			lblMngmtStatus.setVisible(true);
		else 
			lblMngmtStatus.setVisible(false);
		
		lblMessage.setVisible(true);
		lblMessage.setText("Field required!");
		
	}

	public void cancel() throws SQLException {
		 
		modular.cancel("ADMIN_MANAGEMENT", primaryStage);

	}
	
	public void setval () throws SQLException {
		
		if (rdInactive.isSelected()) status = 0;
		else if (rdActive.isSelected()) status = 1;
		
		admin = dbcon.getIDs("*", "ADMIN_DETAILS", "ADMIN_ID", " REFERENCE_ID IS NULL AND ADMIN_NAME = '"+ cmbAdmin.getValue() +"' ");
		bldg = dbcon.getIDs("*", "BUILDING_DETAILS", "BLDG_ID", "REFERENCE_ID IS NULL AND BLDG_NAME = '"+ cmbBuilding.getValue() +"' " );
		//AND BLDG_ID = "+ Main_Window.dashboard +"
		
	}

	public void mngmtview(String id) throws SQLException {
		
		String query = "SELECT ADMIN_MANAGEMENT.*, ADMIN_DETAILS.ADMIN_NAME AS ADMIN,"
				+ " BUILDING_DETAILS.BLDG_NAME AS BLDG"
				+ " FROM ADMIN_MANAGEMENT "
				+ "LEFT JOIN ADMIN_DETAILS ON ADMIN_DETAILS.ADMIN_ID = ADMIN_MANAGEMENT.ADMIN_ID "
				+ "LEFT JOIN BUILDING_DETAILS ON BUILDING_DETAILS.BLDG_ID = ADMIN_MANAGEMENT.BLDG_ID "
				+ "WHERE ADMIN_MANAGEMENT.MNGMT_ID = "+id+" ";
		
		String status;
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()){	
				
				cmbAdmin.setValue(rs.getString("ADMIN"));
				cmbBuilding.setValue(rs.getString("BLDG"));
				
				status = rs.getString("MNGMT_STATUS"); 
				
				if (status.equals("0")) rdInactive.setSelected(true); 
				else rdActive.setSelected(true);
				  
				txtRemarks.setText(rs.getString("MNGMT_REMARKS"));

			}
		}	
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected admin management "+id+" : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void upd() throws SQLException {

		modular.updaterecord(" ADMIN_MANAGEMENT ", " ADMIN_ID, BLDG_ID, MNGMT_STATUS, "
				+ "MNGMT_REMARKS, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
				"ADMIN_ID, BLDG_ID, MNGMT_STATUS, MNGMT_REMARKS, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "
				+ ""+Main_Window.adminmngmt+" ",
				" MNGMT_ID = "+Main_Window.adminmngmt+" ");
		
	}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (cmbAdmin.getValue() != null && cmbBuilding.getValue() != null && 
				(rdInactive.isSelected() || rdActive.isSelected()) )
		{
					
			upd();
			setval();
			
			modular.editrecord(" ADMIN_MANAGEMENT ", " ADMIN_ID = "+ admin +" AND "
					+ "BLDG_ID = "+ bldg +" AND MNGMT_STATUS = "+ status +" AND "
					+ "MNGMT_ID <> "+ Main_Window.adminmngmt +" " ,
					lblMessage,
					" ADMIN_ID = "+ admin +", BLDG_ID = "+ bldg +", "
					+ "  MNGMT_STATUS = "+ status +", MNGMT_REMARKS = '"+ txtRemarks.getText().replaceAll("'", "`")+"' , "
					+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" MNGMT_ID = "+Main_Window.adminmngmt+" ", event);
						
			} else {
				check();
				 
			}

	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
			 
			try {
				modular.cancelreq(" ADMIN_MANAGEMENT ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err admin management : " + e2.getMessage());
			}
		});
		btnCreate.setVisible(true);

	}

	public void processEditOption(String uID, Stage parent_primaryStage) {
		
		sizeview();
		btnUpdate.setVisible(true);
		btnCancel.setText("CLOSE");
		
		try {
			mngmtview(Main_Window.adminmngmt);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err admin management EDIT : " + e.getMessage());
		}
	
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
			 
			try {
				modular.cancelreq("ADMIN_MANAGEMENT ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err admin management EDIT CANCEL : " + e2.getMessage());
			}
			
		});
		
	}

	public void sizeview() {
		
		cmbAdmin.setPrefWidth(410);
		cmbBuilding.setPrefWidth(410);
		txtRemarks.setPrefWidth(410);

		lblMessage.setLayoutX(535);
		lblAdmin.setLayoutX(535);
		lblBuilding.setLayoutX(535);
		lblMngmtStatus.setLayoutX(535);
		lblRemarks.setLayoutX(535);

	}

	public void processViewOption(String uID) {
	
		sizeview();
		btnCancel.setText("CLOSE");
		
		txtRemarks.setDisable(true);
		cmbAdmin.setDisable(true);
		cmbBuilding.setDisable(true);
		rdInactive.setDisable(true);
		rdActive.setDisable(true);
		
		try {
			mngmtview(Main_Window.adminmngmt);

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err admin management record view : " + e.getMessage());
		}
		 
	}
}
