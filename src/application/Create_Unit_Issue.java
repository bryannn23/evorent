package application;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Create_Unit_Issue implements Initializable {

	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();

	@FXML private ComboBox<String> cmbUnit;
	@FXML private ComboBox<String> cmbUIStatus;
	
	@FXML private DatePicker dpInspectionDate;
	
	@FXML private TextArea txtDesc;
	@FXML private TextArea txtActPlan;
	@FXML private TextArea txtRemarks;
	
	@FXML private TextField txtIssue;
	
	@FXML private Label lblUnit;
	@FXML private Label lblUIStatus;
	@FXML private Label lblInspectionDate;
	@FXML private Label lblIssue;
	@FXML private Label lblDesc;
	@FXML private Label lblActPlan;
	@FXML private Label lblRemarks;
	@FXML private Label lblMessage;
	
	@FXML private TableView<TableUnitIssue> tblFinalize;
	
	@FXML private Button btnCreate;
	@FXML Button btnCancel;
	@FXML private Button btnUpdate;
	@FXML private Button btnUpdateM;
	@FXML private Button btnFinalize;
	
	@FXML private VBox vb;
	
	String unit, status, date, unitIssue;
	Stage primaryStage;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnUpdate.setVisible(false);
		btnUpdateM.setVisible(false);
		btnCreate.setVisible(false);
		btnFinalize.setDisable(true);

		lblUnit.setVisible(false);
		lblInspectionDate.setVisible(false);
		lblIssue.setVisible(false);
		lblDesc.setVisible(false);
		lblActPlan.setVisible(false);
		lblRemarks.setVisible(false);
		lblUIStatus.setVisible(false);
		lblMessage.setVisible(false);

			TextFormatter<String> tfissue = modular.getTextFlexiFormatter(100, 'a'); 
			txtIssue.setTextFormatter(tfissue);
			TextFormatter<String> tfdesc = modular.getTextFlexiFormatter(1000, 'a');
			txtDesc.setTextFormatter(tfdesc);
			TextFormatter<String> tfplan = modular.getTextFlexiFormatter(500, 'a');
			txtActPlan.setTextFormatter(tfplan);
			TextFormatter<String> tfrem = modular.getTextFlexiFormatter(1000, 'a');
			txtRemarks.setTextFormatter(tfrem);
			
		comboBox.unitIssue(cmbUIStatus);
		
		try {
			
			dbcon.cmbDisplay(cmbUnit, "*", "UNIT_NAME", "UNIT_DETAILS", "REFERENCE_ID IS NULL AND BLDG_ID = "+ Main_Window.dashboard +" " );
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display unit list: " + e.getMessage());
		}
		
		new AutoCompleteComboBoxListener<String>(cmbUIStatus);
		new AutoCompleteComboBoxListener<String>(cmbUnit);
		
		setupUnitIssue_TableView();
		
}

	public void setupUnitIssue_TableView() {

		tblCol.TableUnitIssue(tblFinalize);

		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableUnitIssue selected = tblFinalize.getSelectionModel().getSelectedItem();
				unitIssue = selected.getUnitIssue();
				unitIssueView(unitIssue);
				
			} catch (Exception ex) {
			}
			if (unitIssue != null) {
				btnCreate.setVisible(false);
				btnUpdate.setVisible(true);
			} else
				return;
		});
	}
	
	public void create(ActionEvent event) throws SQLException {

		lblMessage.setText("");
			
		if (!txtIssue.getText().isEmpty() && cmbUnit.getValue() != null )
		{
			
			setval();
				
			modular.createWithFinalize(" UNIT_ISSUES ", " UNIT_ISSUE_ID IS NULL ", 
					lblMessage, 
					" 0, "+ unit +", "+ date +", '"+txtIssue.getText()+"', '"+ txtDesc.getText()+"', '"+txtActPlan.getText()+"', "
					+ " "+ status+", '"+ txtRemarks.getText() +"', now(), "+Globals.G_Employee_ID+", 'N', null, null, null " , 
					event );
			
			clear();
				
		} else {
			check();
					 
		}
	}

	public void check() {

		if (txtIssue.getText().isEmpty())
			lblIssue.setVisible(true);
		else
			lblIssue.setVisible(false);
			
		if (cmbUnit.getValue() == null )
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
				
			cmbUnit.setValue(null);
			dpInspectionDate.setValue(null);
			txtIssue.clear();
			txtDesc.clear();
			txtActPlan.clear();
			txtRemarks.clear();
			cmbUIStatus.setValue(null);
				
			dbcon.tblUnitIssue(tblFinalize, " UNIT_ISSUES.REFERENCE_ID IS NULL AND UNIT_ISSUES.FINALIZED_RECORD = 'N' " );
			btnFinalize.setDisable(false);
			unitIssue = null;
				
			lblUnit.setVisible(false);
			lblInspectionDate.setVisible(false);
			lblIssue.setVisible(false);
			lblDesc.setVisible(false);
			lblActPlan.setVisible(false);
			lblRemarks.setVisible(false);
			lblUIStatus.setVisible(false);
			lblMessage.setVisible(false);
				
		}
	}
		
	public void cancel() throws SQLException {
			 
		modular.cancel("UNIT_ISSUES", primaryStage);

	}
		
	public void setval () throws SQLException {
			
		unit = dbcon.getIDs("*", "UNIT_DETAILS", "UNIT_ID", "REFERENCE_ID IS NULL AND UNIT_NAME = '"+ cmbUnit.getValue()+"' "
				+ "AND BLDG_ID = "+ Main_Window.dashboard +" ");	
		
		if (dpInspectionDate.getValue() == null)
			date = null;
		else {
			date = dpInspectionDate.getValue().toString();
			date = "'" + date + "'";
		}
		
		if (cmbUIStatus.getValue() == null)
			status = null;
		else if (cmbUIStatus.getValue().equalsIgnoreCase("For Scheduling"))
			status = "1";
		else if (cmbUIStatus.getValue().equalsIgnoreCase("On-going Maintenance"))
			status = "2";
		else if (cmbUIStatus.getValue().equalsIgnoreCase("On-hold"))
			status = "3";
		else if (cmbUIStatus.getValue().equalsIgnoreCase("Cancelled"))
			status = "4";
		else if (cmbUIStatus.getValue().equalsIgnoreCase("Completed"))
			status = "5";
		else if (cmbUIStatus.getValue().equalsIgnoreCase("Others"))
			status = "6";
		
	}

	public void finalize(ActionEvent event) throws SQLException {
			  
		modular.finalize("UNIT_ISSUES");
		((Node) event.getSource()).getScene().getWindow().hide();

	}
		
	public void unitIssueView(String id) throws SQLException {
			
		String query = "SELECT UNIT_ISSUES.*, UNIT_DETAILS.UNIT_NAME AS UNIT "
				+ " FROM UNIT_ISSUES "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = UNIT_ISSUES.UNIT_ID "
				+ " WHERE UNIT_ISSUES.UNIT_ISSUE_ID = "+id+" ";
			
		String stat;
		
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
					
				cmbUnit.setValue(rs.getString("UNIT"));
				
				 Date inspection = rs.getDate("UNIT_INSPECTION_DATE"); 
	                
				 if (inspection == null) {
					 
					 dpInspectionDate.getEditor().setText(rs.getString("UNIT_INSPECTION_DATE")); 
				 }
	             else dpInspectionDate.setValue(inspection.toLocalDate());
				 
				txtIssue.setText(rs.getString("UNIT_ISSUE_NAME"));
				txtDesc.setText(rs.getString("UNIT_ISSUE_DESC"));
				txtActPlan.setText(rs.getString("UNIT_ISSUE_ACTPLAN"));
				txtRemarks.setText(rs.getString("UNIT_ISSUE_REMARKS"));
				
				stat = rs.getString("UNIT_ISSUE_STATUS");
				
				if (stat == null) cmbUIStatus.setValue(null);
				else if (stat.equals("1")) cmbUIStatus.setValue("For Scheduling");
				else if (stat.equals("2")) cmbUIStatus.setValue("On-going Maintenance");
				else if (stat.equals("3")) cmbUIStatus.setValue("On-hold");
				else if (stat.equals("4")) cmbUIStatus.setValue("Cancelled");
				else if (stat.equals("5")) cmbUIStatus.setValue("Completed");
				else if (stat.equals("6")) cmbUIStatus.setValue("Others");
					
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected unitIssue "+id+" : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
		}

	public void upd() throws SQLException {

		modular.updaterecord(" UNIT_ISSUES ", " UNIT_ID, UNIT_INSPECTION_DATE, UNIT_ISSUE_NAME, "
			+ "UNIT_ISSUE_DESC, UNIT_ISSUE_ACTPLAN, UNIT_ISSUE_STATUS, UNIT_ISSUE_REMARKS, "
			+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
			"  UNIT_ID, UNIT_INSPECTION_DATE, UNIT_ISSUE_NAME, UNIT_ISSUE_DESC, UNIT_ISSUE_ACTPLAN,"
			+ " UNIT_ISSUE_STATUS, UNIT_ISSUE_REMARKS, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "
			+ ""+ Unit_Issues.unitIssue +" ",
			" UNIT_ISSUE_ID = "+Unit_Issues.unitIssue+" ");
			
		}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtIssue.getText().isEmpty() && cmbUnit.getValue() != null )
		{
			setval();
				
			modular.editrecordwithfinalize(" UNIT_ISSUES ", " UNIT_ISSUE_ID = 0  " ,
					lblMessage,
					" UNIT_ID = "+ unit +", UNIT_INSPECTION_DATE = "+ date +", "
					+ " UNIT_ISSUE_NAME = '"+txtIssue.getText()+"', UNIT_ISSUE_DESC = '"+ txtDesc.getText() +"' ,"
					+ "UNIT_ISSUE_ACTPLAN = '"+ txtActPlan.getText() +"', UNIT_ISSUE_STATUS = "+ status +", "
					+ "UNIT_ISSUE_REMARKS = '"+ txtRemarks.getText() +"', DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" UNIT_ISSUE_ID = "+ unitIssue +" ",
				   event, btnUpdate, btnCreate);
				
				clear();
							
		} else {
			check();
					 
		}

	}

	public void updateM(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtIssue.getText().isEmpty() && cmbUnit.getValue() != null )
		{
						
			upd();
			setval();
				
			modular.editrecord(" UNIT_ISSUES ", " UNIT_ISSUE_ID = 0 " , //UNIT_ISSUE_ID <> "+ Unit_Issues.unitIssue +" 
					lblMessage,
					" UNIT_ID = "+ unit +", UNIT_INSPECTION_DATE = "+ date +", "
					+ " UNIT_ISSUE_NAME = '"+txtIssue.getText()+"', UNIT_ISSUE_DESC = '"+ txtDesc.getText() +"' ,"
					+ "UNIT_ISSUE_ACTPLAN = '"+ txtActPlan.getText() +"', UNIT_ISSUE_STATUS = "+ status +", "
					+ "UNIT_ISSUE_REMARKS = '"+ txtRemarks.getText() +"', DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" UNIT_ISSUE_ID = "+ Unit_Issues.unitIssue +" ",
				   event);
							
		} else {
			check();
					 
		}

	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
		try {
			modular.cancelreq(" UNIT_ISSUES ", primaryStage, e);
		} catch (SQLException e2) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unitIssue : " + e2.getMessage());
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
			unitIssueView(Unit_Issues.unitIssue);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unitIssue EDIT : " + e.getMessage());
		}
		
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
			try {
				modular.cancelreq("UNIT_ISSUES ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit unitIssue EDIT CANCEL : " + e2.getMessage());
			}
				
		});
			
	}

	public void sizeview() {
			
		cmbUnit.setPrefWidth(410);
		dpInspectionDate.setPrefWidth(410);
		txtIssue.setPrefWidth(410);
		txtDesc.setPrefWidth(410);
		txtActPlan.setPrefWidth(410);
		txtRemarks.setPrefWidth(410);
		cmbUIStatus.setPrefWidth(410);

		lblMessage.setLayoutX(535);
		lblUnit.setLayoutX(535);
		lblInspectionDate.setLayoutX(535);
		lblIssue.setLayoutX(535);
		lblDesc.setLayoutX(535);
		lblActPlan.setLayoutX(535);
		lblRemarks.setLayoutX(535);
		lblUIStatus.setLayoutX(535);

	}

	public void processViewOption(String uID) {
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		btnCancel.setText("CLOSE");
			
		cmbUnit.setDisable(true);
		dpInspectionDate.setDisable(true);
		txtIssue.setDisable(true);
		txtDesc.setDisable(true);
		txtActPlan.setDisable(true);
		txtRemarks.setDisable(true);
		cmbUIStatus.setDisable(true);

		try {
			unitIssueView(Unit_Issues.unitIssue);

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unitIssue record view : " + e.getMessage());
		}
			 
	}
		
}
