package application;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Create_Unit_Maintenance implements Initializable {

	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;
	PreparedStatement ps1 = null;
	ResultSet rs1 = null;
	PreparedStatement ps2 = null;
	ResultSet rs2 = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();

	@FXML private ComboBox<String> cmbIssue;
	@FXML private ComboBox<String> cmbUnit;
	@FXML private ComboBox<String> cmbActivity;
	@FXML private ComboBox<String> cmbRequest;
	@FXML private ComboBox<String> cmbOccupancy;
	@FXML private ComboBox<String> cmbUMStatus;
	@FXML private ComboBox<String> cmbContractor;
	
	@FXML private DatePicker dpDate;
	
	@FXML private TextArea txtDesc;
	@FXML private TextArea txtRemarks;
	
	@FXML private TextField txtTotalCost;
	@FXML private TextField txtReference;
	
	@FXML private Label lblIssue;
	@FXML private Label lblUnit;
	@FXML private Label lblDate;
	@FXML private Label lblActivity;
	@FXML private Label lblDesc;
	@FXML private Label lblRequest;
	@FXML private Label lblOccupancy;
	@FXML private Label lblContractor;
	@FXML private Label lblUMStatus;
	@FXML private Label lblTotalCost;
	@FXML private Label lblRemarks;
	@FXML private Label lblMessage;
	
	@FXML private TableView<TableUnitMaintenance> tblFinalize;
	
	@FXML private Button btnCreate;
	@FXML Button btnCancel;
	@FXML private Button btnUpdate;
	@FXML private Button btnUpdateM;
	@FXML private Button btnFinalize;
	
	@FXML private VBox vb;
	
	String issue, unit, status, date, activity, request, occupancy, unitMaintenance, contr ;
	String mxStatus;
	Stage primaryStage;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnUpdate.setVisible(false);
		btnUpdateM.setVisible(false);
		btnCreate.setVisible(false);
		btnFinalize.setDisable(true);

		lblIssue.setVisible(false);
		lblUnit.setVisible(false);
		lblDate.setVisible(false);
		lblActivity.setVisible(false);
		lblDesc.setVisible(false);
		lblRequest.setVisible(false);
		lblOccupancy.setVisible(false);
		lblContractor.setVisible(false);
		lblUMStatus.setVisible(false);
		lblTotalCost.setVisible(false);
		lblRemarks.setVisible(false);
		lblMessage.setVisible(false);

		
		TextFormatter<String> tfdesc = modular.getTextFlexiFormatter(1000, 'a');
		txtDesc.setTextFormatter(tfdesc);
		TextFormatter<String> tfcost = modular.getTextFlexiFormatter(12, 'n');
		txtTotalCost.setTextFormatter(tfcost);
		TextFormatter<String> tfrem = modular.getTextFlexiFormatter(1000, 'a');
		txtRemarks.setTextFormatter(tfrem);
		TextFormatter<String> tfref = modular.getTextFlexiFormatter(20, 'n');
		txtReference.setTextFormatter(tfref);
			
		comboBox.unitMxActivity(cmbActivity);
		comboBox.unitMXStatus(cmbUMStatus);
		comboBox.unitRequest(cmbRequest);
		
		try {
			
			//dbcon.cmbDisplay(cmbUnit, "*", "UNIT_NAME", "UNIT_DETAILS", "REFERENCE_ID IS NULL AND BLDG_ID = "+ Main_Window.dashboard +" ");
			dbcon.cmbDisplay(cmbContractor, "*", "CONTR_NAME", "CONTRACTOR_DETAILS", "REFERENCE_ID IS NULL" );
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of unit: " +e.getMessage());
		}
		
		cmbUnit.setValue(Unit_Details.unitName);
		
		try {
			cmbIssue.getItems().clear();
			
			dbcon.cmbDisplay(cmbIssue, "UNIT_ISSUES.*", "UNIT_ISSUE_NAME", "UNIT_ISSUES "
					+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = UNIT_ISSUES.UNIT_ID ", 
					"UNIT_ISSUES.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
							+ "UNIT_DETAILS.UNIT_NAME = '"+ cmbUnit.getValue() +"' ");
			
			cmbOccupancy.getItems().clear();
			
			dbcon.setDisplay(cmbOccupancy, "PRIMARY_TENANT_DETAILS.*", "TENANT_NAME", "OCCUPANCY_DETAILS "
					+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
					+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ", 
					"OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
					+ " AND UNIT_DETAILS.UNIT_NAME = '"+ cmbUnit.getValue() +"' AND OCCUPANCY_DETAILS.OCCP_STATUS <> 3 " );
			
			if (cmbOccupancy.getValue() == null) {
				
				dbcon.cmbDisplay(cmbOccupancy, "PRIMARY_TENANT_DETAILS.*", "TENANT_NAME", "OCCUPANCY_DETAILS "
						+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
						+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ", 
						"OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" " );
			}
			
		} catch (SQLException e1) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of unit: " +e1.getMessage());
		}
		
		
		cmbUnit.setOnAction( e-> {
			
			try {
				cmbIssue.getItems().clear();
				
				dbcon.cmbDisplay(cmbIssue, "UNIT_ISSUES.*", "UNIT_ISSUE_NAME", "UNIT_ISSUES "
						+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = UNIT_ISSUES.UNIT_ID ", 
						"UNIT_ISSUES.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
								+ "UNIT_DETAILS.UNIT_NAME = '"+ cmbUnit.getValue() +"' ");
				
				cmbOccupancy.getItems().clear();
				
				dbcon.setDisplay(cmbOccupancy, "PRIMARY_TENANT_DETAILS.*", "TENANT_NAME", "OCCUPANCY_DETAILS "
						+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
						+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ", 
						"OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_NAME = '"+ cmbUnit.getValue() +"' AND OCCUPANCY_DETAILS.OCCP_STATUS <> 3 " );
				
				if (cmbOccupancy.getValue() == null) {
					
					dbcon.cmbDisplay(cmbOccupancy, "PRIMARY_TENANT_DETAILS.*", "TENANT_NAME", "OCCUPANCY_DETAILS "
							+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
							+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ", 
							"OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" " );
				}
				
			} catch (SQLException e1) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of unit: " +e1.getMessage());
			}
			
		});
		
		new AutoCompleteComboBoxListener<String>(cmbUnit);
		new AutoCompleteComboBoxListener<String>(cmbActivity);
		new AutoCompleteComboBoxListener<String>(cmbUMStatus);
		new AutoCompleteComboBoxListener<String>(cmbIssue);
		new AutoCompleteComboBoxListener<String>(cmbRequest);
		new AutoCompleteComboBoxListener<String>(cmbOccupancy);
		new AutoCompleteComboBoxListener<String>(cmbContractor);
		
		setupUnitMaintenance_TableView();
		
	}

	public void setupUnitMaintenance_TableView() {

		tblCol.TableUnitMaintenance(tblFinalize);

		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableUnitMaintenance selected = tblFinalize.getSelectionModel().getSelectedItem();
				unitMaintenance = selected.getMxID();
				unitMx(unitMaintenance);
					
			} catch (Exception ex) {
			}
			if (unitMaintenance != null) {
				btnCreate.setVisible(false);
				btnUpdate.setVisible(true);
			} else
				return;
		});
	
	}
	
	public void create(ActionEvent event) throws SQLException {

		lblMessage.setText("");
			
		if ( cmbIssue.getValue() != null && cmbUnit.getValue() != null )
		{
			
			setval();

			if (contr == null) {
				dbcon.createContractor("CONTRACTOR_DETAILS", " 0, '"+ cmbContractor.getValue()+"', null, null, null, null, now(), "+ Globals.G_Employee_ID+", "
						+ "'N', null, null, null ");
				
				contr = dbcon.getIDs("*", "CONTRACTOR_DETAILS", "CONTR_ID", " REFERENCE_ID IS NULL AND CONTR_NAME = '"+ cmbContractor.getValue() +"' ");
				
			}
			
			modular.createWithFinalize(" UNIT_MAINTENANCE ", " UNIT_ID = "+ unit +" AND UNIT_MX_DATE = "+ date +" "
					+ "AND UNIT_MX_ACTIVITY = "+ activity +" ", 
					lblMessage, 
					" 0, "+ issue +", "+ unit +", "+ txtReference.getText().replaceAll(",", "") +", "+ date +", "
					+ " "+ activity +", '"+ txtDesc.getText() +"', "+ request +", "
					+ " "+ occupancy +", "+ contr +",  "+ status +", "
					+ " "+ txtTotalCost.getText().replaceAll(",", "") +", '"+ txtRemarks.getText() +"', now(),"
					+ " "+Globals.G_Employee_ID+", 'N', null, null, null " , 
					event );
			
			if (lblMessage.getText() != "Record already exist.") {
				
				if (status.equals("4")) {
					
					String id = dbcon.getLastID("UNIT_MX_ID", "UNIT_MAINTENANCE");
					
					if (activity == "0" || activity == "1" || activity == "2" || activity == "3" || activity == "4" 
							|| activity == "5" || activity == "6" || activity == "7" || activity == "8" ) {
						
						dbcon.createContractor("EXPENSE_RECORD", " 0, 'UNIT MAINTENANCE', "+ id +", "
								+ " "+ txtReference.getText().replaceAll(",", "")+", "+ contr+", "
								+ " "+ date +", 7, '"+ txtRemarks.getText() +"', "+ txtTotalCost.getText().replaceAll(",", "")+", "
								+ " "+ Main_Window.dashboard +", now(), "+ Globals.G_Employee_ID+", 'N', null, null, null ");
					}
					else if (activity == "11" || activity == "12" || activity == "13" || activity == "14" 
							|| activity == "15" || activity == "16") {
						
						dbcon.createContractor("EXPENSE_RECORD", " 0, 'UNIT MAINTENANCE', "+ id +", "+ txtReference.getText().replaceAll(",", "")+", "+ contr+", "
								+ " "+ date +", 8, '"+ txtRemarks.getText() +"', "+ txtTotalCost.getText().replaceAll(",", "")+", "
								+ " "+ Main_Window.dashboard +", now(), "+ Globals.G_Employee_ID+", 'N', null, null, null ");
					}
					else {
						
					}
					
				}
			}
			
			clear();
				
		} else {
			check();
					 
		}
	}

	public void check() {

		if (cmbIssue.getValue() == null )
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
				
			cmbIssue.setValue(null);
			cmbUnit.setValue(null);
			dpDate.setValue(null);
			cmbActivity.setValue(null);
			txtDesc.clear();
			cmbRequest.setValue(null);
			cmbOccupancy.setValue(null);
			cmbContractor.setValue(null);
			cmbUMStatus.setValue(null);
			txtTotalCost.clear();
			txtRemarks.clear();
			txtReference.clear();
				
			dbcon.tblUnitMaintenance(tblFinalize, " UNIT_MAINTENANCE.REFERENCE_ID IS NULL AND UNIT_MAINTENANCE.FINALIZED_RECORD = 'N' " );
			btnFinalize.setDisable(false);
			unitMaintenance = null;
				
			lblIssue.setVisible(false);
			lblUnit.setVisible(false);
			lblDate.setVisible(false);
			lblActivity.setVisible(false);
			lblDesc.setVisible(false);
			lblRequest.setVisible(false);
			lblOccupancy.setVisible(false);
			lblContractor.setVisible(false);
			lblUMStatus.setVisible(false);
			lblTotalCost.setVisible(false);
			lblRemarks.setVisible(false);
			lblMessage.setVisible(false);
				
		}
	}
		
	public void cancel() throws SQLException {
			 
		//modular.cancel("UNIT_MAINTENANCE", primaryStage);
		 
		String query = "DELETE FROM UNIT_MAINTENANCE WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
				+ Globals.G_Employee_ID + " ";
		
		String contractor = "DELETE FROM CONTRACTOR_DETAILS WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
				+ Globals.G_Employee_ID + " ";
		
		String expense = "DELETE FROM EXPENSE_RECORD WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
				+ Globals.G_Employee_ID + " ";

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText(
				"Clicking CANCEL without FINALIZING the record, will cause deletion of all created records. Are you sure you want to continue?");

		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK) {
			
			try {
				
				ps = dbcon.connect.prepareStatement(query);
				ps.executeUpdate();
				
			}
			catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit maintenances cancel : " + e.getMessage());
			} finally {
				ps.close();
			}

			try {
				
				ps1 = dbcon.connect.prepareStatement(contractor);
				ps1.executeUpdate();
				
			}
			catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err contractor cancel from unit maintenance: " + e.getMessage());
			} finally {
				ps1.close();
			}
			
			try {
				
				ps2 = dbcon.connect.prepareStatement(expense);
				ps2.executeUpdate();
				
			}
			catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err expense cancel from unit maintenance: " + e.getMessage());
			} finally {
				ps2.close();
			}
			
			primaryStage.close();
			
		} else {
			
			return;

		}

	}
		
	public void setval () throws SQLException {
			
		if (txtTotalCost.getText().isEmpty()) txtTotalCost.setText("0");
		if (txtReference.getText().isEmpty()) txtReference.setText("0");
		
		issue = dbcon.getIDs("UNIT_ISSUES.*", "UNIT_ISSUES "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = UNIT_ISSUES.UNIT_ID ", 
				"UNIT_ISSUE_ID", "UNIT_ISSUES.REFERENCE_ID IS NULL AND UNIT_ISSUES.UNIT_ISSUE_NAME = '"+ cmbIssue.getValue() +"' "
						+ "AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" ");
		
		unit = dbcon.getIDs("*", "UNIT_DETAILS", "UNIT_ID", "REFERENCE_ID IS NULL AND UNIT_NAME = '"+ cmbUnit.getValue() +"' "
				+ "AND BLDG_ID = "+ Main_Window.dashboard +" ");
		
		occupancy = dbcon.getIDs("OCCUPANCY_DETAILS.*", "OCCUPANCY_DETAILS "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ", 
				"OCCP_ID", "OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND PRIMARY_TENANT_DETAILS.TENANT_NAME = '"+ cmbOccupancy.getValue() +"' "
						+ "AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" ");
		
		contr = dbcon.getIDs("*", "CONTRACTOR_DETAILS", "CONTR_ID", "REFERENCE_ID IS NULL AND CONTR_NAME = '"+ cmbContractor.getValue() +"' ");	
		
		if (dpDate.getValue() == null)
			date = null;
		else {
			date = dpDate.getValue().toString();
			date = "'" + date + "'";
			
		}
		
		if (cmbActivity.getValue() == null)
			activity = null;
		else if (cmbActivity.getValue().equalsIgnoreCase("Others Maintenance")) 
			activity = "0";
		else if (cmbActivity.getValue().equalsIgnoreCase("Unit Interior Inspection"))
			activity = "1";
		else if (cmbActivity.getValue().equalsIgnoreCase("Unit Exterior Inspection"))
			activity = "2";
		else if (cmbActivity.getValue().equalsIgnoreCase("Walls (Wash, Re-paint)"))
			activity = "3";
		else if (cmbActivity.getValue().equalsIgnoreCase("Windows (Wash, Re-caulk)"))
			activity = "4";
		else if (cmbActivity.getValue().equalsIgnoreCase("Doors (Wash, Re-paint)"))
			activity = "5";
		else if (cmbActivity.getValue().equalsIgnoreCase("Plumbing (Maintenance)"))
			activity = "6";
		else if (cmbActivity.getValue().equalsIgnoreCase("Electrical (Maintenance)"))
			activity = "7";
		else if (cmbActivity.getValue().equalsIgnoreCase("Pest Control"))
			activity = "8";
		
		else if (cmbActivity.getValue().equalsIgnoreCase("Others Repairs and Fixtures"))
			activity = "11";
		else if (cmbActivity.getValue().equalsIgnoreCase("Interior Makeover"))
			activity = "12";
		else if (cmbActivity.getValue().equalsIgnoreCase("Lighting (Clean, Change)"))
			activity = "13";
		else if (cmbActivity.getValue().equalsIgnoreCase("Foundation (Repair)"))
			activity = "14";
		else if (cmbActivity.getValue().equalsIgnoreCase("Furniture (Repair, Change)"))
			activity = "15";
		else if (cmbActivity.getValue().equalsIgnoreCase("Appliances (Repair, Change)"))
			activity = "16";
		

		if (cmbRequest.getValue() == null)
			request = null;
		else if (cmbRequest.getValue().equalsIgnoreCase("Admin Initiative"))
			request = "0";
		else if (cmbRequest.getValue().equalsIgnoreCase("Tenant Request"))
			request = "1";
		
		if (cmbUMStatus.getValue() == null)
			status = null;
		else if (cmbUMStatus.getValue().equalsIgnoreCase("Scheduled for Maintenance"))
			status = "0";
		else if (cmbUMStatus.getValue().equalsIgnoreCase("Waiting for Contractor"))
			status = "1";
		else if (cmbUMStatus.getValue().equalsIgnoreCase("Undergoing Maintenance"))
			status = "2";
		else if (cmbUMStatus.getValue().equalsIgnoreCase("Still/Terminated Maintenance"))
			status = "3";
		else if (cmbUMStatus.getValue().equalsIgnoreCase("Maintenance Completed"))
			status = "4";
		
	}

	public void finalize(ActionEvent event) throws SQLException {
			  
		modular.finalize("UNIT_MAINTENANCE");

		String query = "UPDATE CONTRACTOR_DETAILS SET FINALIZED_RECORD = 'Y' WHERE SYSTEM_ACCOUNT_ID = "
				+ Globals.G_Employee_ID + " AND FINALIZED_RECORD = 'N' ";
		
		try {
			ps = dbcon.connect.prepareStatement(query);
			ps.executeUpdate();

		} catch (Exception e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err CONTRACTOR_DETAILS : " + e.getMessage());
		} finally {
			ps.close();
		}
		
		String expense = "UPDATE EXPENSE_RECORD SET FINALIZED_RECORD = 'Y' WHERE SYSTEM_ACCOUNT_ID = "
				+ Globals.G_Employee_ID + " AND FINALIZED_RECORD = 'N' ";
		
		try {
			ps1 = dbcon.connect.prepareStatement(expense);
			ps1.executeUpdate();

		} catch (Exception e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err EXPENSE_RECORD : " + e.getMessage());
		} finally {
			ps1.close();
		}
		
		((Node) event.getSource()).getScene().getWindow().hide();

	}
		
	public void unitMx(String id) throws SQLException {
			
		String query = "SELECT UNIT_MAINTENANCE.*, UNIT_ISSUES.UNIT_ISSUE_NAME AS ISSUE, "
				+ "UNIT_DETAILS.UNIT_NAME AS UNIT, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT, "
				+ "CONTRACTOR_DETAILS.CONTR_NAME AS CONTRACTOR "
				+ " FROM UNIT_MAINTENANCE "
				+ "LEFT JOIN UNIT_ISSUES ON UNIT_ISSUES.UNIT_ISSUE_ID = UNIT_MAINTENANCE.UNIT_ISSUE_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = UNIT_MAINTENANCE.UNIT_ID "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = UNIT_MAINTENANCE.OCCP_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN CONTRACTOR_DETAILS ON CONTRACTOR_DETAILS.CONTR_ID = UNIT_MAINTENANCE.UNIT_MX_CONTRACTOR "
				+ " WHERE UNIT_MAINTENANCE.UNIT_MX_ID = "+id+" ";
			
		String act, req, stat;
		
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
					
				cmbIssue.setValue(rs.getString("ISSUE"));
				cmbUnit.setValue(rs.getString("UNIT"));
				
				 Date date = rs.getDate("UNIT_MX_DATE"); 
	                
				 if (date == null) {
					 
					 dpDate.getEditor().setText(rs.getString("UNIT_MX_DATE")); 
				 }
	             else dpDate.setValue(date.toLocalDate());
				 
				 act = rs.getString("UNIT_MX_ACTIVITY");
				 
				 if (act == null) cmbActivity.setValue(null);
					else if (act.equals("0"))  cmbActivity.setValue("Others Maintenance");
					else if (act.equals("1"))  cmbActivity.setValue("Unit Interior Inspection");
					else if (act.equals("2"))  cmbActivity.setValue("Unit Exterior Inspection");
					else if (act.equals("3"))  cmbActivity.setValue("Walls (Wash , Re-paint)");
					else if (act.equals("4"))  cmbActivity.setValue("Windows (Wash, Re-caulk)");
					else if (act.equals("5"))  cmbActivity.setValue("Doors (Wash, Re-paint)");
					else if (act.equals("6"))  cmbActivity.setValue("Plumbing (Maintenance)");
					else if (act.equals("7"))  cmbActivity.setValue("Electrical (Maintenance)");
					else if (act.equals("8"))  cmbActivity.setValue("Pest Control");
					
					else if (act.equals("11"))  cmbActivity.setValue("Others Repairs and Fixtures");
					else if (act.equals("12"))  cmbActivity.setValue("Interior Makeover");
					else if (act.equals("13"))  cmbActivity.setValue("Lighting (Clean, Change)");
					else if (act.equals("14"))  cmbActivity.setValue("Foundation (Repair)");
					else if (act.equals("15"))  cmbActivity.setValue("Furniture (Repair, Change)");
					else if (act.equals("16"))  cmbActivity.setValue("Appliances (Repair, Change)");
				 
				 txtDesc.setText(rs.getString("UNIT_MX_DESC"));
				 
				 req = rs.getString("UNIT_MX_REQUEST");

				 if (req == null) cmbRequest.setValue(null);
				 else if (req.equals("0")) cmbRequest.setValue("Admin Initiative");
				 else if (req.equals("1")) cmbRequest.setValue("Tenant Request");
				 else cmbRequest.setValue(null);

				 cmbOccupancy.setValue(rs.getString("TENANT"));
				 cmbContractor.setValue(rs.getString("CONTRACTOR"));
				 
				 stat = rs.getString("UNIT_MX_STATUS");
				 mxStatus = stat;
				 
				 if (stat == null) cmbUMStatus.setValue(null);
				 else if (stat.equals("0")) cmbUMStatus.setValue("Scheduled for Maintenance");
				 else if (stat.equals("1")) cmbUMStatus.setValue("Waiting for Contractor");
				 else if (stat.equals("2")) cmbUMStatus.setValue("Undergoing Maintenance");
				 else if (stat.equals("3")) cmbUMStatus.setValue("Still/Terminated Maintenance");
				 else if (stat.equals("4")) cmbUMStatus.setValue("Maintenance Completed");
					 
				 txtTotalCost.setText(rs.getString("UNIT_MX_TOTAL_COST"));
 				 txtRemarks.setText(rs.getString("UNIT_MX_REMARKS"));
 				 txtReference.setText(rs.getString("UNIT_MX_REF_ID"));
					
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected unitMaintenance "+id+" : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
		}

	public void upd() throws SQLException {

		modular.updaterecord(" UNIT_MAINTENANCE ", " UNIT_ISSUE_ID, UNIT_ID, UNIT_MX_REF_ID, UNIT_MX_DATE, UNIT_MX_ACTIVITY, UNIT_MX_DESC, "
			+ "UNIT_MX_REQUEST, OCCP_ID, UNIT_MX_CONTRACTOR, UNIT_MX_STATUS, UNIT_MX_TOTAL_COST, UNIT_MX_REMARKS, "
			+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
			" UNIT_ISSUE_ID, UNIT_ID, UNIT_MX_REF_ID, UNIT_MX_DATE, UNIT_MX_ACTIVITY, UNIT_MX_DESC, UNIT_MX_REQUEST, OCCP_ID, "
			+ " UNIT_MX_CONTRACTOR, UNIT_MX_STATUS, UNIT_MX_TOTAL_COST, UNIT_MX_REMARKS, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "
			+ " "+ Unit_Maintenance.maintenance +" ",
			" UNIT_MX_ID = "+ Unit_Maintenance.maintenance +" ");
			
		}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbIssue.getValue() != null && cmbUnit.getValue() != null )
		{
			setval();

			if (contr == null) {
				dbcon.createContractor("CONTRACTOR_DETAILS", " 0, '"+ cmbContractor.getValue()+"', null, null, null, null, now(), "+ Globals.G_Employee_ID+", "
						+ "'N', null, null, null ");
				
				contr = dbcon.getIDs("*", "CONTRACTOR_DETAILS", "CONTR_ID", " REFERENCE_ID IS NULL AND CONTR_NAME = '"+ cmbContractor.getValue() +"' ");
				
			}
			
			modular.editrecordwithfinalize(" UNIT_MAINTENANCE ", " UNIT_ID = "+ unit +" AND UNIT_MX_DATE = "+ date +" "
					+ "AND UNIT_MX_ACTIVITY = "+ activity +" AND UNIT_MX_ID <> "+ unitMaintenance +" " ,
					lblMessage,
					" UNIT_ISSUE_ID = "+ issue +", UNIT_ID = "+ unit +", UNIT_MX_REF_ID = "+ txtReference.getText().replaceAll(",", "")+" , "
					+ " UNIT_MX_DATE = "+ date +", UNIT_MX_ACTIVITY = "+ activity +", "
					+ "UNIT_MX_DESC = '"+txtDesc.getText()+"', UNIT_MX_REQUEST = "+ request +", OCCP_ID = "+ occupancy +", "
					+ "UNIT_MX_CONTRACTOR = "+ contr +", UNIT_MX_STATUS = "+ status +", "
					+ " UNIT_MX_TOTAL_COST = "+ txtTotalCost.getText().replaceAll(",", "") +","
					+ " UNIT_MX_REMARKS = '"+ txtRemarks.getText() +"', DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" UNIT_MX_ID = "+ unitMaintenance +" ",
				   event, btnUpdate, btnCreate);
			
			if (lblMessage.getText() != "Record already exist.") {

				if (!mxStatus.equals("4") && status.equals("4")) {
						
						if (activity == "0" || activity == "1" || activity == "2" || activity == "3" || activity == "4" 
								|| activity == "5" || activity == "6" || activity == "7" || activity == "8" ) {
							
							dbcon.createContractor("EXPENSE_RECORD", " 0, 'UNIT MAINTENANCE', "+ unitMaintenance +", "
									+ " "+ txtReference.getText().replaceAll(",", "")+", "+ contr+", "
									+ " "+ date +", 7, '"+ txtRemarks.getText() +"', "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ " "+ Main_Window.dashboard +", now(), "+ Globals.G_Employee_ID+", 'N', null, null, null ");
						}
						else if (activity == "11" || activity == "12" || activity == "13" || activity == "14" 
								|| activity == "15" || activity == "16") {
							
							dbcon.createContractor("EXPENSE_RECORD", " 0, 'UNIT MAINTENANCE', "+ unitMaintenance +", "
									+ " "+ txtReference.getText().replaceAll(",", "")+", "+ contr+", "
									+ " "+ date +", 8, '"+ txtRemarks.getText() +"', "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ " "+ Main_Window.dashboard +", now(), "+ Globals.G_Employee_ID+", 'N', null, null, null ");
						}
						else {
							
						}
					}
					
				else if (mxStatus.equals("4") && status.equals("4")) {
					
						if (activity == "0" || activity == "1" || activity == "2" || activity == "3" || activity == "4" 
								|| activity == "5" || activity == "6" || activity == "7" || activity == "8" ) {
							
							dbcon.updateRecord("EXPENSE_RECORD", " EXPENSE_REF = "+ txtReference.getText().replaceAll(",", "")+", "
									+ "CONTRACTOR_ID = "+ contr +", EXPENSE_TXN_DATE = "+ date +", EXPENSE_COA = 7, "
									+ "EXPENSE_DESCRIPTION = '"+ txtDesc.getText() +"', "
									+ "EXPENSE_AMOUNT = "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ", 
									" TABLE_NAME = 'UNIT MAINTENANCE' AND TABLE_ID = "+ unitMaintenance +" AND REFERENCE_ID IS NULL " );
							
						}
						else if (activity == "11" || activity == "12" || activity == "13" || activity == "14" 
								|| activity == "15" || activity == "16") {
							
							dbcon.updateRecord("EXPENSE_RECORD", " EXPENSE_REF = "+ txtReference.getText().replaceAll(",", "")+", "
									+ "CONTRACTOR_ID = "+ contr +", EXPENSE_TXN_DATE = "+ date +", EXPENSE_COA = 8, "
									+ "EXPENSE_DESCRIPTION = '"+ txtDesc.getText() +"', "
									+ "EXPENSE_AMOUNT = "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ", 
									" TABLE_NAME = 'UNIT MAINTENANCE' AND TABLE_ID = "+ unitMaintenance +" AND REFERENCE_ID IS NULL " );
						}
						else {
							
						}
					}
				else if ( mxStatus.equals("4") && !status.equals("4")) {
					
					dbcon.updateRecord("EXPENSE_RECORD", " REFERENCE_ID = "+ unitMaintenance +", EXPENSE_DESCRIPTION = 'CHANGE FROM COMPLETED TO OTHER STATUS' ",
							" TABLE_NAME = 'UNIT MAINTENANCE' AND TABLE_ID = "+ unitMaintenance+" AND REFERENCE_ID IS NULL " );
				}
			}
				
				clear();
							
		} else {
			check();
					 
		}

	}

	public void updateM(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbIssue.getValue() != null && cmbUnit.getValue() != null )
		{
						
			upd();
			setval();

			if (contr == null) {
				dbcon.createContractor("CONTRACTOR_DETAILS", " 0, '"+ cmbContractor.getValue()+"', null, null, null, null, now(), "+ Globals.G_Employee_ID+", "
						+ "'Y', null, null, null ");
				
				contr = dbcon.getIDs("*", "CONTRACTOR_DETAILS", "CONTR_ID", " REFERENCE_ID IS NULL AND CONTR_NAME = '"+ cmbContractor.getValue() +"' ");
				
			}
			
			modular.editrecord(" UNIT_MAINTENANCE ", " UNIT_ID = "+ unit +" AND UNIT_MX_DATE = "+ date +" "
					+ "AND UNIT_MX_ACTIVITY = "+ activity +" AND UNIT_MX_ID <> "+ Unit_Maintenance.maintenance +" " ,
					lblMessage,
					" UNIT_ISSUE_ID = "+ issue +", UNIT_ID = "+ unit +", UNIT_MX_REF_ID = "+ txtReference.getText().replaceAll(",", "")+", "
					+ " UNIT_MX_DATE = "+ date +", UNIT_MX_ACTIVITY = "+ activity +", "
					+ "UNIT_MX_DESC = '"+txtDesc.getText()+"', UNIT_MX_REQUEST = "+ request +", OCCP_ID = "+ occupancy +", "
					+ "UNIT_MX_CONTRACTOR = "+ contr +", UNIT_MX_STATUS = "+ status +", "
					+ " UNIT_MX_TOTAL_COST = "+ txtTotalCost.getText().replaceAll(",", "") +","
					+ " UNIT_MX_REMARKS = '"+ txtRemarks.getText() +"', DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" UNIT_MX_ID = "+ Unit_Maintenance.maintenance +" ",
				   event  );
			
			if (lblMessage.getText() != "Record already exist.") {

				if (!mxStatus.equals("4") && status.equals("4")) {
						
						if (activity == "0" || activity == "1" || activity == "2" || activity == "3" || activity == "4" 
								|| activity == "5" || activity == "6" || activity == "7" || activity == "8" ) {
							
							dbcon.createContractor("EXPENSE_RECORD", " 0, 'UNIT MAINTENANCE', "+ Unit_Maintenance.maintenance +", "
									+ " "+ txtReference.getText().replaceAll(",", "")+", "+ contr+", "
									+ " "+ date +", 7, '"+ txtRemarks.getText() +"', "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ " "+ Main_Window.dashboard +", now(), "+ Globals.G_Employee_ID+", 'Y', null, null, null ");
						}
						else if (activity == "11" || activity == "12" || activity == "13" || activity == "14" 
								|| activity == "15" || activity == "16") {
							
							dbcon.createContractor("EXPENSE_RECORD", " 0, 'UNIT MAINTENANCE', "+ Unit_Maintenance.maintenance +", "
									+ " "+ txtReference.getText().replaceAll(",", "")+", "+ contr+", "
									+ " "+ date +", 8, '"+ txtRemarks.getText() +"', "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ " "+ Main_Window.dashboard +", now(), "+ Globals.G_Employee_ID+", 'Y', null, null, null ");
						}
						else {
							
						}
					}
					
				else if (mxStatus.equals("4") && status.equals("4")) {
					
						if (activity == "0" || activity == "1" || activity == "2" || activity == "3" || activity == "4" 
								|| activity == "5" || activity == "6" || activity == "7" || activity == "8" ) {
							
							dbcon.updateRecord("EXPENSE_RECORD", " EXPENSE_REF = "+ txtReference.getText().replaceAll(",", "")+", "
									+ "CONTRACTOR_ID = "+ contr +", EXPENSE_TXN_DATE = "+ date +", EXPENSE_COA = 7, "
									+ "EXPENSE_DESCRIPTION = '"+ txtDesc.getText() +"', "
									+ "EXPENSE_AMOUNT = "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ", 
									" TABLE_NAME = 'UNIT MAINTENANCE' AND TABLE_ID = "+ Unit_Maintenance.maintenance +" AND REFERENCE_ID IS NULL " );
							
						}
						else if (activity == "11" || activity == "12" || activity == "13" || activity == "14" 
								|| activity == "15" || activity == "16") {
							
							dbcon.updateRecord("EXPENSE_RECORD", " EXPENSE_REF = "+ txtReference.getText().replaceAll(",", "")+", "
									+ "CONTRACTOR_ID = "+ contr +", EXPENSE_TXN_DATE = "+ date +", EXPENSE_COA = 8, "
									+ "EXPENSE_DESCRIPTION = '"+ txtDesc.getText() +"', "
									+ "EXPENSE_AMOUNT = "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ", 
									" TABLE_NAME = 'UNIT MAINTENANCE' AND TABLE_ID = "+ Unit_Maintenance.maintenance +" AND REFERENCE_ID IS NULL " );
						}
						else {
							
						}
					}
				else if ( mxStatus.equals("4") && !status.equals("4")) {
					
					dbcon.updateRecord("EXPENSE_RECORD", " REFERENCE_ID = "+ Unit_Maintenance.maintenance +", EXPENSE_DESCRIPTION = 'CHANGE FROM COMPLETED TO OTHER STATUS' ",
							" TABLE_NAME = 'UNIT MAINTENANCE' AND TABLE_ID = "+ Unit_Maintenance.maintenance +" AND REFERENCE_ID IS NULL " );
				}
			}
			
		} else {
			check();
					 
		}

	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
		/*try {
			modular.cancelreq(" UNIT_MAINTENANCE ", primaryStage, e);
		} catch (SQLException e2) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unitMaintenance : " + e2.getMessage());
		}*/
			
			String query = "DELETE FROM UNIT_MAINTENANCE WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
					+ Globals.G_Employee_ID + " ";
			
			String query2 = "DELETE FROM CONTRACTOR_DETAILS WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
					+ Globals.G_Employee_ID + " ";

			String expense = "DELETE FROM EXPENSE_RECORD WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
					+ Globals.G_Employee_ID + " ";

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText(null);
			alert.setContentText(
					"Clicking CANCEL without FINALIZING the record, will cause deletion of all created records. Are you sure you want to continue?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				
				try {
					
					ps = dbcon.connect.prepareStatement(query);
					ps.executeUpdate();

				} catch (Exception e1) {
					EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit maintenance : " + e1.getMessage());
				}
				
				try {
					
					ps1 = dbcon.connect.prepareStatement(query2);
					ps1.executeUpdate();

				} catch (Exception e1) {
					EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err contractor : " + e1.getMessage());
				}

				try {
					
					ps2 = dbcon.connect.prepareStatement(expense);
					ps2.executeUpdate();

				} catch (Exception e1) {
					EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err expense : " + e1.getMessage());
				}
				
				primaryStage.close();
				
			} else {
				e.consume();
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
			unitMx(Unit_Maintenance.maintenance);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unitMaintenance EDIT : " + e.getMessage());
		}
		
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
			try {
				modular.cancelreq("CONTRACTOR_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit unitMaintenance EDIT CANCEL : " + e2.getMessage());
			}
				
		});
			
	}

	public void sizeview() {
			
		cmbIssue.setPrefWidth(410);
		cmbUnit.setPrefWidth(410);
		dpDate.setPrefWidth(410);
		cmbActivity.setPrefWidth(410);
		cmbContractor.setPrefWidth(410);
		cmbRequest.setPrefWidth(410);
		cmbOccupancy.setPrefWidth(410);
		txtDesc.setPrefWidth(410);
		cmbUMStatus.setPrefWidth(410);
		txtRemarks.setPrefWidth(410);
		txtTotalCost.setPrefWidth(410);
		txtReference.setPrefWidth(410);

		lblMessage.setLayoutX(535);
		lblIssue.setLayoutX(535);
		lblUnit.setLayoutX(535);
		lblDate.setLayoutX(535);
		lblActivity.setLayoutX(535);
		lblDesc.setLayoutX(535);
		lblRequest.setLayoutX(535);
		lblOccupancy.setLayoutX(535);
		lblContractor.setLayoutX(535);
		lblRemarks.setLayoutX(535);
		lblUMStatus.setLayoutX(535);
		lblTotalCost.setLayoutX(535);

	}

	public void processViewOption(String uID) {
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		btnCancel.setText("CLOSE");
			
		cmbIssue.setDisable(true);
		cmbUnit.setDisable(true);
		dpDate.setDisable(true);
		cmbActivity.setDisable(true);
		cmbContractor.setDisable(true);
		txtDesc.setDisable(true);
		cmbRequest.setDisable(true);
		cmbOccupancy.setDisable(true);
		cmbUMStatus.setDisable(true);
		txtTotalCost.setDisable(true);
		txtRemarks.setDisable(true);
		cmbUMStatus.setDisable(true);
		txtReference.setDisable(true);

		try {
			unitMx(Unit_Maintenance.maintenance);

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unitMaintenance record view : " + e.getMessage());
		}
			 
	}
		
}
