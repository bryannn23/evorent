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

public class Create_Building_Issue implements Initializable {

	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();

	@FXML private ComboBox<String> cmbBIStatus;
	
	@FXML private DatePicker dpInspectionDate;
	
	@FXML private TextArea txtDesc;
	@FXML private TextArea txtActPlan;
	@FXML private TextArea txtRemarks;
	
	@FXML private TextField txtIssueName;
	
	@FXML private Label lblBIStatus;
	@FXML private Label lblInspectionDate;
	@FXML private Label lblIssueName;
	@FXML private Label lblDesc;
	@FXML private Label lblActPlan;
	@FXML private Label lblRemarks;
	@FXML private Label lblMessage;
	
	@FXML private TableView<TableBuildingIssue> tblFinalize;
	
	@FXML private Button btnCreate;
	@FXML Button btnCancel;
	@FXML private Button btnUpdate;
	@FXML private Button btnUpdateM;
	@FXML private Button btnFinalize;
	
	@FXML private VBox vb;
	
	String status, date, issue;
	Stage primaryStage;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnUpdate.setVisible(false);
		btnUpdateM.setVisible(false);
		btnCreate.setVisible(false);
		btnFinalize.setDisable(true);

		lblInspectionDate.setVisible(false);
		lblIssueName.setVisible(false);
		lblDesc.setVisible(false);
		lblActPlan.setVisible(false);
		lblRemarks.setVisible(false);
		lblBIStatus.setVisible(false);
		lblMessage.setVisible(false);

			TextFormatter<String> tfissue = modular.getTextFlexiFormatter(100, 'a'); 
			txtIssueName.setTextFormatter(tfissue);
			TextFormatter<String> tfdesc = modular.getTextFlexiFormatter(1000, 'a');
			txtDesc.setTextFormatter(tfdesc);
			TextFormatter<String> tfplan = modular.getTextFlexiFormatter(500, 'a');
			txtActPlan.setTextFormatter(tfplan);
			TextFormatter<String> tfrem = modular.getTextFlexiFormatter(1000, 'a');
			txtRemarks.setTextFormatter(tfrem);
			
		comboBox.bldgIssue(cmbBIStatus);
		
		new AutoCompleteComboBoxListener<String>(cmbBIStatus);
		
		setupBldgIssue_TableView();
		
}

	public void setupBldgIssue_TableView() {

		tblCol.TableBuildingIssue(tblFinalize);

		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableBuildingIssue selected = tblFinalize.getSelectionModel().getSelectedItem();
				issue = selected.getBldgIssue();
				bldgissue(issue);
				
			} catch (Exception ex) {
			}
			if (issue != null) {
				btnCreate.setVisible(false);
				btnUpdate.setVisible(true);
			} else
				return;
		});
	}
	
	public void create(ActionEvent event) throws SQLException {

		lblMessage.setText("");
			
		if (!txtIssueName.getText().isEmpty() )
		{
			
			setval();
				
			modular.createWithFinalize(" BUILDING_ISSUES ", " BLDG_ISSUES_ID = 0 ", 
					lblMessage, 
					" 0, "+ Main_Window.dashboard +", "+ date +", '"+txtIssueName.getText()+"', '"+ txtDesc.getText()+"', '"+txtActPlan.getText()+"',"
						+ " '"+ txtRemarks.getText() +"', "+ status+", now(), "+Globals.G_Employee_ID+", 'N', null, null, null " , 
					event );
			
			clear();
				
		} else {
			check();
					 
		}
	}

	public void check() {

		if (txtIssueName.getText().isEmpty())
			lblIssueName.setVisible(true);
		else
			lblIssueName.setVisible(false);
			
			lblMessage.setVisible(true);
			lblMessage.setText("Field required!");
			
		}

	public void clear() throws SQLException {
			
		if (lblMessage.getText().equals("Record already exist.")) {
				
			lblMessage.setVisible(true);
			lblMessage.setText("Record already exist.");
				
		} else {
				
			dpInspectionDate.setValue(null);
			txtIssueName.clear();
			txtDesc.clear();
			txtActPlan.clear();
			txtRemarks.clear();
			cmbBIStatus.setValue(null);
				
			dbcon.tblBuildingIssue(tblFinalize, " BUILDING_ISSUES.REFERENCE_ID IS NULL AND BUILDING_ISSUES.FINALIZED_RECORD = 'N' " );
			btnFinalize.setDisable(false);
			issue = null;
				
			lblInspectionDate.setVisible(false);
			lblIssueName.setVisible(false);
			lblDesc.setVisible(false);
			lblActPlan.setVisible(false);
			lblRemarks.setVisible(false);
			lblBIStatus.setVisible(false);
			lblMessage.setVisible(false);
				
		}
	}
		
	public void cancel() throws SQLException {
			 
		modular.cancel("BUILDING_ISSUES", primaryStage);

	}
		
	public void setval () throws SQLException {
			
		if (cmbBIStatus.getValue() == null )
			status = null;
		else if (cmbBIStatus.getValue().equalsIgnoreCase("Reported")) 
			status = "0";
		else if (cmbBIStatus.getValue().equalsIgnoreCase("For Scheduling"))
			status = "1";
		else if (cmbBIStatus.getValue().equalsIgnoreCase("On-going Maintenance"))
			status = "2";
		else if (cmbBIStatus.getValue().equalsIgnoreCase("On-hold"))
			status = "3";
		else if (cmbBIStatus.getValue().equalsIgnoreCase("Cancelled"))
			status = "4";
		else if (cmbBIStatus.getValue().equalsIgnoreCase("Completed"))
			status = "5";
		else if (cmbBIStatus.getValue().equalsIgnoreCase("Others"))
			status = "6";
			
		if (dpInspectionDate.getValue() == null)
			date = null;
		else 
			date = dpInspectionDate.getValue().toString();
			date = "'" + date + "'";
		
	}

	public void finalize(ActionEvent event) throws SQLException {
			  
		modular.finalize("BUILDING_ISSUES");
		((Node) event.getSource()).getScene().getWindow().hide();

	}
		
	public void bldgissue(String id) throws SQLException {
			
		String query = "SELECT BUILDING_ISSUES.*, BUILDING_DETAILS.BLDG_NAME AS BLDG "
				+ " FROM BUILDING_ISSUES "
				+ "LEFT JOIN BUILDING_DETAILS ON BUILDING_DETAILS.BLDG_ID = BUILDING_ISSUES.BLDG_ID "
				+ " WHERE BUILDING_ISSUES.BLDG_ISSUES_ID = "+id+" ";
			
		String stat;
		
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
					
				 Date inspection = rs.getDate("BLDG_INSPECTION_DATE"); 
	                
				 if (inspection == null) {
					 
					 dpInspectionDate.getEditor().setText(rs.getString("BLDG_INSPECTION_DATE")); 
				 }
	             else dpInspectionDate.setValue(inspection.toLocalDate());
				 
				txtIssueName.setText(rs.getString("BLDG_ISSUE_NAME"));
				txtDesc.setText(rs.getString("BLDG_ISSUE_DESC"));
				txtActPlan.setText(rs.getString("BLDG_ISSUE_ACTPLAN"));
				txtRemarks.setText(rs.getString("BLDG_ISSUE_REMARKS"));
				
				stat = rs.getString("BLDG_ISSUE_STATUS");
				if (stat == null) cmbBIStatus.setValue(null);
				else if (stat.equals("0")) cmbBIStatus.setValue("Reported");
				else if (stat.equals("1")) cmbBIStatus.setValue("For Scheduling");
				else if (stat.equals("2")) cmbBIStatus.setValue("On-going Maintenance");
				else if (stat.equals("3")) cmbBIStatus.setValue("On-hold");
				else if (stat.equals("4")) cmbBIStatus.setValue("Cancelled");
				else if (stat.equals("5")) cmbBIStatus.setValue("Completed");
				else if (stat.equals("6")) cmbBIStatus.setValue("Others");
					
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected issue "+id+" : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
		}

	public void upd() throws SQLException {

		modular.updaterecord(" BUILDING_ISSUES ", " BLDG_ID, BLDG_INSPECTION_DATE, BLDG_ISSUE_NAME, "
			+ "BLDG_ISSUE_DESC, BLDG_ISSUE_ACTPLAN, BLDG_ISSUE_REMARKS, BLDG_ISSUE_STATUS, "
			+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
			" BLDG_ID, BLDG_INSPECTION_DATE, BLDG_ISSUE_NAME, BLDG_ISSUE_DESC, BLDG_ISSUE_ACTPLAN, BLDG_ISSUE_REMARKS, "
			+ " BLDG_ISSUE_STATUS, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "
			+ ""+ Building_Issues.issue +" ",
			" BLDG_ISSUES_ID = "+Building_Issues.issue+" ");
			
		}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtIssueName.getText().isEmpty() )
		{
			setval();
				
			modular.editrecordwithfinalize(" BUILDING_ISSUES ", " BLDG_ISSUES_ID = 0 " ,
					lblMessage,
					"  BLDG_INSPECTION_DATE = "+ date +", "
					+ " BLDG_ISSUE_NAME = '"+txtIssueName.getText()+"', BLDG_ISSUE_DESC = '"+ txtDesc.getText() +"' ,"
					+ "BLDG_ISSUE_ACTPLAN = '"+ txtActPlan.getText() +"', BLDG_ISSUE_REMARKS = '"+ txtRemarks.getText() +"', "
					+ "BLDG_ISSUE_STATUS = "+ status +", DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" BLDG_ISSUES_ID = "+ issue +" ",
				   event, btnUpdate, btnCreate);
				
				clear();
							
		} else {
			check();
					 
		}

	}

	public void updateM(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtIssueName.getText().isEmpty() )
		{
						
			upd();
			setval();
				
			modular.editrecord(" BUILDING_ISSUES ", " BLDG_ISSUES_ID = 0 " ,
					lblMessage,
					"  BLDG_INSPECTION_DATE = "+ date +", "
					+ " BLDG_ISSUE_NAME = '"+txtIssueName.getText()+"', BLDG_ISSUE_DESC = '"+ txtDesc.getText() +"' ,"
					+ "BLDG_ISSUE_ACTPLAN = '"+ txtActPlan.getText() +"', BLDG_ISSUE_REMARKS = '"+ txtRemarks.getText() +"', "
					+ "BLDG_ISSUE_STATUS = "+ status +", DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" BLDG_ISSUES_ID = "+ Building_Issues.issue +" ",
				   event);
							
		} else {
			check();
					 
		}

	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
		try {
			modular.cancelreq(" BUILDING_ISSUES ", primaryStage, e);
		} catch (SQLException e2) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err issue : " + e2.getMessage());
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
			bldgissue(Building_Issues.issue);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err issue EDIT : " + e.getMessage());
		}
		
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
			try {
				modular.cancelreq("BUILDING_ISSUES ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err bldg issue EDIT CANCEL : " + e2.getMessage());
			}
				
		});
			
	}

	public void sizeview() {
			
		dpInspectionDate.setPrefWidth(410);
		txtIssueName.setPrefWidth(410);
		txtDesc.setPrefWidth(410);
		txtActPlan.setPrefWidth(410);
		txtRemarks.setPrefWidth(410);
		cmbBIStatus.setPrefWidth(410);

		lblMessage.setLayoutX(535);
		lblInspectionDate.setLayoutX(535);
		lblIssueName.setLayoutX(535);
		lblDesc.setLayoutX(535);
		lblActPlan.setLayoutX(535);
		lblRemarks.setLayoutX(535);
		lblBIStatus.setLayoutX(535);

	}

	public void processViewOption(String uID) {
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		btnCancel.setText("CLOSE");
			
		dpInspectionDate.setDisable(true);
		txtIssueName.setDisable(true);
		txtDesc.setDisable(true);
		txtActPlan.setDisable(true);
		txtRemarks.setDisable(true);
		cmbBIStatus.setDisable(true);

		try {
			bldgissue(Building_Issues.issue);

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err issue record view : " + e.getMessage());
		}
			 
	}
		
}
