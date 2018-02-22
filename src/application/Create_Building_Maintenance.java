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

public class Create_Building_Maintenance implements Initializable {

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

	@FXML private ComboBox<String> cmbActivity;
	@FXML private ComboBox<String> cmbBMStatus;
	@FXML private ComboBox<String> cmbBMContractor;
	
	@FXML private DatePicker dpDate;
	
	@FXML private TextArea txtDesc;
	@FXML private TextArea txtRemarks;
	
	@FXML private TextField txtReference;
	@FXML private TextField txtTotalCost;
	
	@FXML private Label lblReference;
	@FXML private Label lblDate;
	@FXML private Label lblActivity;
	@FXML private Label lblDesc;
	@FXML private Label lblContractor;
	@FXML private Label lblBMStatus;
	@FXML private Label lblTotalCost;
	@FXML private Label lblRemarks;
	@FXML private Label lblMessage;
	
	@FXML private TableView<TableBuildingMaintenance> tblFinalize;
	
	@FXML private Button btnCreate;
	@FXML Button btnCancel;
	@FXML private Button btnUpdate;
	@FXML private Button btnUpdateM;
	@FXML private Button btnFinalize;
	
	@FXML private VBox vb;
	
	String status, date, activity, maintenance, contractor;
	String mxStatus;
	Stage primaryStage;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnUpdate.setVisible(false);
		btnUpdateM.setVisible(false);
		btnCreate.setVisible(false);
		btnFinalize.setDisable(true);

		lblReference.setVisible(false);
		lblDate.setVisible(false);
		lblActivity.setVisible(false);
		lblDesc.setVisible(false);
		lblContractor.setVisible(false);
		lblBMStatus.setVisible(false);
		lblTotalCost.setVisible(false);
		lblRemarks.setVisible(false);
		lblMessage.setVisible(false);

		TextFormatter<String> tfref = modular.getTextFlexiFormatter(20, 'n'); 
		txtReference.setTextFormatter(tfref);
		TextFormatter<String> tfdesc = modular.getTextFlexiFormatter(1000, 'a');
		txtDesc.setTextFormatter(tfdesc);
		TextFormatter<String> tfcost = modular.getTextFlexiFormatter(12, 'n');
		txtTotalCost.setTextFormatter(tfcost);
		TextFormatter<String> tfrem = modular.getTextFlexiFormatter(1000, 'a');
		txtRemarks.setTextFormatter(tfrem);
			
		comboBox.bldgMxActivity(cmbActivity);
		comboBox.bldgMXStatus(cmbBMStatus);
		
		try {
			dbcon.cmbDisplay(cmbBMContractor, "*", "CONTR_NAME", "CONTRACTOR_DETAILS", "REFERENCE_ID IS NULL" );
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of building: " +e.getMessage());
		}
		
		new AutoCompleteComboBoxListener<String>(cmbActivity);
		new AutoCompleteComboBoxListener<String>(cmbBMStatus);
		new AutoCompleteComboBoxListener<String>(cmbBMContractor);
		
		setupBldgMaintenance_TableView();
		
	}

	public void setupBldgMaintenance_TableView() {

		tblCol.TableBuildingMaintenance(tblFinalize);
		
		tblFinalize.setStyle("-fx-table-cell-border-color: transparent; ");

		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableBuildingMaintenance selected = tblFinalize.getSelectionModel().getSelectedItem();
				maintenance = selected.getMxID();
				bldgmx(maintenance);
				
			} catch (Exception ex) {
			}
			if (maintenance != null) {
				btnCreate.setVisible(false);
				btnUpdate.setVisible(true);
			} else
				return;
		});
	}
	
	public void create(ActionEvent event) throws SQLException {

		lblMessage.setText("");
			
		if (cmbBMContractor.getValue() != null )
		{
			
			setval();
			
			if (contractor == null) {
				dbcon.createContractor("CONTRACTOR_DETAILS", " 0, '"+ cmbBMContractor.getValue()+"', null, null, null, null, now(), "+ Globals.G_Employee_ID+", "
						+ "'N', null, null, null ");
				
				contractor = dbcon.getIDs("*", "CONTRACTOR_DETAILS", "CONTR_ID", " REFERENCE_ID IS NULL AND CONTR_NAME = '"+ cmbBMContractor.getValue() +"' ");
				
			}
			
			modular.createWithFinalize(" BUILDING_MAINTENANCE ", " BLDG_ID = "+ Main_Window.dashboard +" AND BLDG_MX_DATE = "+ date +" "
					+ "AND BLDG_MX_ACTIVITY = "+ activity +" ", 
					lblMessage, 
					" 0, "+ Main_Window.dashboard +", "+ txtReference.getText().replaceAll(",", "")+", "+ date +", "+ activity +", '"+ txtDesc.getText() +"', "+ contractor +", "
					+ " "+ status +", '"+ txtTotalCost.getText().replaceAll(",", "") +"', '"+ txtRemarks.getText() +"', now(),"
					+ " "+Globals.G_Employee_ID+", 'N', null, null, null " , 
					event );
			
			if (lblMessage.getText() != "Record already exist.") {
				
				if (status.equals("4")) {
					
					String id = dbcon.getLastID("BLDG_MX_ID", "BUILDING_MAINTENANCE");
					
					if (activity == "0" || activity == "1" || activity == "2" || activity == "3" || activity == "4" 
							|| activity == "5" || activity == "6" || activity == "7" || activity == "8" || 
							activity == "9" || activity == "10" ) {
						
						dbcon.createContractor("EXPENSE_RECORD", " 0, 'BUILDING MAINTENANCE', "+ id +", "
								+ " "+ txtReference.getText().replaceAll(",", "")+", "+ contractor+", "
								+ " "+ date +", 7, '"+ txtRemarks.getText() +"', "+ txtTotalCost.getText().replaceAll(",", "")+", "
								+ " "+ Main_Window.dashboard +", now(), "+ Globals.G_Employee_ID+", 'N', null, null, null ");
					}
					else if (activity == "11" || activity == "12" || activity == "13" || activity == "14" 
							|| activity == "15" || activity == "16") {
						
						dbcon.createContractor("EXPENSE_RECORD", " 0, 'BUILDING MAINTENANCE', "+ id +", "+ txtReference.getText().replaceAll(",", "")+", "
								+ " "+ contractor+",  "+ date +", 8, '"+ txtRemarks.getText() +"', "+ txtTotalCost.getText().replaceAll(",", "")+", "
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

		if (cmbBMContractor.getValue() == null)
			lblContractor.setVisible(true);
		else
			lblContractor.setVisible(false);
			
			lblMessage.setVisible(true);
			lblMessage.setText("Field required!");
			
		}

	public void clear() throws SQLException {
			
		if (lblMessage.getText().equals("Record already exist.")) {
				
			lblMessage.setVisible(true);
			lblMessage.setText("Record already exist.");
				
		} else {
				
			dpDate.setValue(null);
			cmbActivity.setValue(null);
			txtDesc.clear();
			txtReference.clear();
			cmbBMContractor.setValue(null);
			cmbBMStatus.setValue(null);
			txtTotalCost.clear();
			txtRemarks.clear();
			cmbBMStatus.setValue(null);
				
			dbcon.tblBuildingMaintenance(tblFinalize, " BUILDING_MAINTENANCE.REFERENCE_ID IS NULL AND BUILDING_MAINTENANCE.FINALIZED_RECORD = 'N' " );
			btnFinalize.setDisable(false);
			maintenance = null;
				
			lblReference.setVisible(false);
			lblDate.setVisible(false);
			lblActivity.setVisible(false);
			lblDesc.setVisible(false);
			lblContractor.setVisible(false);
			lblBMStatus.setVisible(false);
			lblTotalCost.setVisible(false);
			lblRemarks.setVisible(false);
			lblMessage.setVisible(false);
				
		}
	}
		
	public void cancel() throws SQLException {
			 
		//modular.cancel("BUILDING_MAINTENANCE", primaryStage);

		String query = "DELETE FROM BUILDING_MAINTENANCE WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
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
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err bldg maintenances cancel : " + e.getMessage());
			} finally {
				ps.close();
			}

			try {
				
				ps1 = dbcon.connect.prepareStatement(contractor);
				ps1.executeUpdate();
				
			}
			catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err contractor cancel : " + e.getMessage());
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
			
		if (txtReference.getText().isEmpty()) txtReference.setText("0");
		if (txtTotalCost.getText().isEmpty()) txtTotalCost.setText("0");
		
		contractor = dbcon.getIDs("*", "CONTRACTOR_DETAILS", "CONTR_ID", "REFERENCE_ID IS NULL AND CONTR_NAME = '"+ cmbBMContractor.getValue()+"' ");
		
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
		else if (cmbActivity.getValue().equalsIgnoreCase("Building Interior Inspection"))
			activity = "1";
		else if (cmbActivity.getValue().equalsIgnoreCase("Building Exterior Inspection"))
			activity = "2";
		else if (cmbActivity.getValue().equalsIgnoreCase("General Unit Inspection"))
			activity = "3";
		else if (cmbActivity.getValue().equalsIgnoreCase("Sidings (Wash, Re-paint, Waterproofing)"))
			activity = "4";
		else if (cmbActivity.getValue().equalsIgnoreCase("Windows (Wash, Re-caulk)"))
			activity = "5";
		else if (cmbActivity.getValue().equalsIgnoreCase("Doors (Wash, Re-paint)"))
			activity = "6";
		else if (cmbActivity.getValue().equalsIgnoreCase("Decks and Stairs (Clean, Paint)"))
			activity = "7";
		else if (cmbActivity.getValue().equalsIgnoreCase("Plumbing"))
			activity = "8";
		else if (cmbActivity.getValue().equalsIgnoreCase("Electrical (Maintenance)"))
			activity = "9";
		else if (cmbActivity.getValue().equalsIgnoreCase("Pest Control"))
			activity = "10";
		
		else if (cmbActivity.getValue().equalsIgnoreCase("Others Repairs and Fixtures"))
			activity = "11";
		else if (cmbActivity.getValue().equalsIgnoreCase("Interior Makeover")) 
			activity = "12";
		else if (cmbActivity.getValue().equalsIgnoreCase("Signage (Clean, Repair)"))
			activity = "13";
		else if (cmbActivity.getValue().equalsIgnoreCase("Lighting (Clean, Change)")) 
			activity = "14";
		else if (cmbActivity.getValue().equalsIgnoreCase("Roof (Clean, Waterproofing, Leaks, Repairs)"))
			activity = "15";
		else if (cmbActivity.getValue().equalsIgnoreCase("Foundation (Repair)"))
			activity = "16";
		else if (cmbActivity.getValue().equalsIgnoreCase("Gutter (Clean, Repair, Re-paint"))
			activity = "17";
		
		if (cmbBMStatus.getValue() == null)
			status = null;
		else if (cmbBMStatus.getValue().equalsIgnoreCase("Scheduled for Maintenance"))
			status = "0";
		else if (cmbBMStatus.getValue().equalsIgnoreCase("Waiting for Contractor"))
			status = "1";
		else if (cmbBMStatus.getValue().equalsIgnoreCase("On-Going Maintenance"))
			status = "2";
		else if (cmbBMStatus.getValue().equalsIgnoreCase("Still/Terminated Maintenance"))
			status = "3";
		else if (cmbBMStatus.getValue().equalsIgnoreCase("Maintenance Completed"))
			status = "4";
			
	}

	public void finalize(ActionEvent event) throws SQLException {
			  
		modular.finalize("BUILDING_MAINTENANCE");
		
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
		
	public void bldgmx(String id) throws SQLException {
			
		String query = "SELECT BUILDING_MAINTENANCE.*, BUILDING_DETAILS.BLDG_NAME AS BLDG, "
				+ "CONTRACTOR_DETAILS.CONTR_NAME AS CONTRACTOR "
				+ " FROM BUILDING_MAINTENANCE "
				+ "LEFT JOIN BUILDING_DETAILS ON BUILDING_DETAILS.BLDG_ID = BUILDING_MAINTENANCE.BLDG_ID "
				+ "LEFT JOIN CONTRACTOR_DETAILS ON CONTRACTOR_DETAILS.CONTR_ID = BUILDING_MAINTENANCE.CONTR_ID "
				+ " WHERE BUILDING_MAINTENANCE.BLDG_MX_ID = "+id+" ";
			
		String act, stat; 
		
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
					
				txtReference.setText(rs.getString("BLDG_MX_REF_ID"));
				
				 Date date = rs.getDate("BLDG_MX_DATE"); 
	                
				 if (date == null) {
					 
					 dpDate.getEditor().setText(rs.getString("BLDG_MX_DATE")); 
				 }
	             else dpDate.setValue(date.toLocalDate());
				 
				 act = rs.getString("BLDG_MX_ACTIVITY");

				if (act == null) cmbActivity.setValue(null);
				else if (act.equals("0")) cmbActivity.setValue("Others Maintenace");
				else if (act.equals("1")) cmbActivity.setValue("Building Interior Inspection");
				else if (act.equals("2")) cmbActivity.setValue("Building Exterior Inspection");
				else if (act.equals("3")) cmbActivity.setValue("General Unit Inspection");
				else if (act.equals("4")) cmbActivity.setValue("Sidings (Wash , Re-paint, Waterproofing)");
				else if (act.equals("5")) cmbActivity.setValue("Windows (Wash, Re-caulk)");
				else if (act.equals("6")) cmbActivity.setValue("Doors (Wash, Re-paint)");
				else if (act.equals("7")) cmbActivity.setValue("Decks and Stairs (Clean, Paint)");
				else if (act.equals("8")) cmbActivity.setValue("Plumbing");
				else if (act.equals("9")) cmbActivity.setValue("Electrical (Maintenance)");
				else if (act.equals("10")) cmbActivity.setValue("Pest Control");

				else if (act.equals("11")) cmbActivity.setValue("Others Repairs and Fixtures");
				else if (act.equals("12")) cmbActivity.setValue("Interior Makeover");
				else if (act.equals("13")) cmbActivity.setValue("Signage (Clean, Repair)");
				else if (act.equals("14")) cmbActivity.setValue("Lighting (Clean, Change)");
				else if (act.equals("15")) cmbActivity.setValue("Roof (Clean, Waterproofing, Leaks, Repairs)");
				else if (act.equals("16")) cmbActivity.setValue("Foundation (Repair)");
				else if (act.equals("17")) cmbActivity.setValue("Gutter (Clean, Repair, Re-paint)");
					
				 
				 txtDesc.setText(rs.getString("BLDG_MX_DESC"));
				 cmbBMContractor.setValue(rs.getString("CONTRACTOR"));
				 
				 stat = rs.getString("BLDG_MX_STATUS");
				 mxStatus = stat;
				 
				 if (stat == null) cmbBMStatus.setValue(null);
				 else if (stat.equals("0")) cmbBMStatus.setValue("Scheduled for Maintenance");
				 else if (stat.equals("1")) cmbBMStatus.setValue("Waiting for Contractor");
				 else if (stat.equals("2")) cmbBMStatus.setValue("On-going Maintenance");
				 else if (stat.equals("3")) cmbBMStatus.setValue("Still/Terminated Maintenance");
				 else if (stat.equals("4")) cmbBMStatus.setValue("Maintenance Completed");
				 
				 txtTotalCost.setText(rs.getString("BLDG_MX_TOTAL_COST"));
 				 txtRemarks.setText(rs.getString("BLDG_MX_REMARKS"));
					
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected maintenance "+id+" : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
		}

	public void upd() throws SQLException {

		modular.updaterecord(" BUILDING_MAINTENANCE ", " BLDG_ID, BLDG_MX_REF_ID, BLDG_MX_DATE, BLDG_MX_ACTIVITY, BLDG_MX_DESC, "
			+ "CONTR_ID, BLDG_MX_STATUS, BLDG_MX_TOTAL_COST, BLDG_MX_REMARKS, "
			+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
			" BLDG_ID, BLDG_MX_REF_ID, BLDG_MX_DATE, BLDG_MX_ACTIVITY, BLDG_MX_DESC, CONTR_ID, BLDG_MX_STATUS, "
			+ " BLDG_MX_TOTAL_COST, BLDG_MX_REMARKS, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "
			+ " "+ Building_Maintenance.maintenance +" ",
			" BLDG_MX_ID = "+Building_Maintenance.maintenance+" ");
			
		}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbBMContractor.getValue() != null  )
		{
			setval();
			
			if (contractor == null) {
				dbcon.createContractor("CONTRACTOR_DETAILS", " 0, '"+ cmbBMContractor.getValue()+"', null, null, null, null, now(), "+ Globals.G_Employee_ID+", "
						+ "'N', null, null, null ");
				
				contractor = dbcon.getIDs("*", "CONTRACTOR_DETAILS", "CONTR_ID", " REFERENCE_ID IS NULL AND CONTR_NAME = '"+ cmbBMContractor.getValue() +"' ");
				
			}
				
			modular.editrecordwithfinalize(" BUILDING_MAINTENANCE ", " BLDG_ID = "+ Main_Window.dashboard +" AND BLDG_MX_DATE = "+ date +" "
					+ "AND BLDG_MX_ACTIVITY = "+ activity +" AND BLDG_MX_ID <> "+ maintenance +" " ,
					lblMessage,
					" BLDG_MX_REF_ID = "+ txtReference.getText().replaceAll(",", "")+", BLDG_MX_DATE = "+ date +", BLDG_MX_ACTIVITY = "+ activity +", "
					+ "BLDG_MX_DESC = '"+txtDesc.getText()+"', CONTR_ID = "+ contractor +" ,"
					+ "BLDG_MX_STATUS = "+ status +", BLDG_MX_TOTAL_COST = "+ txtTotalCost.getText().replaceAll(",", "") +","
					+ " BLDG_MX_REMARKS = '"+ txtRemarks.getText() +"', DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" BLDG_MX_ID = "+ maintenance +" ",
				   event, btnUpdate, btnCreate);
				
			if (lblMessage.getText() != "Record already exist.") {

				if (!mxStatus.equals("4") && status.equals("4")) {
						
						if (activity == "0" || activity == "1" || activity == "2" || activity == "3" || activity == "4" 
								|| activity == "5" || activity == "6" || activity == "7" || activity == "8" 
								|| activity == "9" || activity == "10" ) {
							
							dbcon.createContractor("EXPENSE_RECORD", " 0, 'BUILDING MAINTENANCE', "+ maintenance +", "
									+ " "+ txtReference.getText().replaceAll(",", "")+", "+ contractor+", "
									+ " "+ date +", 7, '"+ txtRemarks.getText() +"', "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ " "+ Main_Window.dashboard +", now(), "+ Globals.G_Employee_ID+", 'N', null, null, null ");
						}
						else if (activity == "11" || activity == "12" || activity == "13" || activity == "14" 
								|| activity == "15" || activity == "16") {
							
							dbcon.createContractor("EXPENSE_RECORD", " 0, 'BUILDING MAINTENANCE', "+ maintenance +", "
									+ " "+ txtReference.getText().replaceAll(",", "")+", "+ contractor+", "
									+ " "+ date +", 8, '"+ txtRemarks.getText() +"', "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ " "+ Main_Window.dashboard +", now(), "+ Globals.G_Employee_ID+", 'N', null, null, null ");
						}
						else {
							
						}
					}
					
				else if (mxStatus.equals("4") && status.equals("4")) {
					
						if (activity == "0" || activity == "1" || activity == "2" || activity == "3" || activity == "4" 
								|| activity == "5" || activity == "6" || activity == "7" || activity == "8" 
								|| activity == "9" || activity == "10" ) {
							
							dbcon.updateRecord("EXPENSE_RECORD", " EXPENSE_REF = "+ txtReference.getText().replaceAll(",", "")+", "
									+ "CONTRACTOR_ID = "+ contractor +", EXPENSE_TXN_DATE = "+ date +", EXPENSE_COA = 7, "
									+ "EXPENSE_DESCRIPTION = '"+ txtDesc.getText() +"', "
									+ "EXPENSE_AMOUNT = "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ", 
									" TABLE_NAME = 'BUILDING MAINTENANCE' AND TABLE_ID = "+ maintenance +" AND REFERENCE_ID IS NULL " );
							
						}
						else if (activity == "11" || activity == "12" || activity == "13" || activity == "14" 
								|| activity == "15" || activity == "16") {
							
							dbcon.updateRecord("EXPENSE_RECORD", " EXPENSE_REF = "+ txtReference.getText().replaceAll(",", "")+", "
									+ "CONTRACTOR_ID = "+ contractor +", EXPENSE_TXN_DATE = "+ date +", EXPENSE_COA = 8, "
									+ "EXPENSE_DESCRIPTION = '"+ txtDesc.getText() +"', "
									+ "EXPENSE_AMOUNT = "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ", 
									" TABLE_NAME = 'BUILDING MAINTENANCE' AND TABLE_ID = "+ maintenance +" AND REFERENCE_ID IS NULL " );
						}
						else {
							
						}
					}
				else if ( mxStatus.equals("4") && !status.equals("4")) {
					
					dbcon.updateRecord("EXPENSE_RECORD", " REFERENCE_ID = "+ maintenance +", EXPENSE_DESCRIPTION = 'CHANGE FROM COMPLETED TO OTHER STATUS' ",
							" TABLE_NAME = 'BUILDING MAINTENANCE' AND TABLE_ID = "+ maintenance+" AND REFERENCE_ID IS NULL " );
				}
			}
				
				clear();
							
		} else {
			check();
					 
		}

	}

	public void updateM(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbBMContractor.getValue() != null  )
		{
						
			upd();
			setval();
				
			if (contractor == null) {
				dbcon.createContractor("CONTRACTOR_DETAILS", " 0, '"+ cmbBMContractor.getValue()+"', null, null, null, null, now(), "+ Globals.G_Employee_ID+", "
						+ "'Y', null, null, null ");
				
				contractor = dbcon.getIDs( "*", "CONTRACTOR_DETAILS", "CONTR_ID", " REFERENCE_ID IS NULL AND CONTR_NAME = '"+ cmbBMContractor.getValue() +"' ");
				
			}
			
			modular.editrecord(" BUILDING_MAINTENANCE ", " BLDG_ID = "+ Main_Window.dashboard +" AND BLDG_MX_DATE = "+ date +" "
					+ "AND BLDG_MX_ACTIVITY = "+ activity +" AND BLDG_MX_ID <> "+ Building_Maintenance.maintenance +" " ,
					lblMessage,
					" BLDG_MX_REF_ID = "+ txtReference.getText().replaceAll(",", "")+", BLDG_MX_DATE = "+ date +", BLDG_MX_ACTIVITY = "+ activity +", "
					+ "BLDG_MX_DESC = '"+txtDesc.getText()+"', CONTR_ID = "+ contractor +" ,"
					+ "BLDG_MX_STATUS = "+ status +", BLDG_MX_TOTAL_COST = "+ txtTotalCost.getText().replaceAll(",", "") +","
					+ " BLDG_MX_REMARKS = '"+ txtRemarks.getText() +"', DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" BLDG_MX_ID = "+ Building_Maintenance.maintenance +" ",
				   event );
				

			if (lblMessage.getText() != "Record already exist.") {

				if (!mxStatus.equals("4") && status.equals("4")) {
						
						if (activity == "0" || activity == "1" || activity == "2" || activity == "3" || activity == "4" 
								|| activity == "5" || activity == "6" || activity == "7" || activity == "8" 
								|| activity == "9" || activity == "10" ) {
							
							dbcon.createContractor("EXPENSE_RECORD", " 0, 'BUILDING MAINTENANCE', "+ Building_Maintenance.maintenance +", "
									+ " "+ txtReference.getText().replaceAll(",", "")+", "+ contractor +", "
									+ " "+ date +", 7, '"+ txtRemarks.getText() +"', "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ " "+ Main_Window.dashboard +", now(), "+ Globals.G_Employee_ID+", 'Y', null, null, null ");
						}
						else if (activity == "11" || activity == "12" || activity == "13" || activity == "14" 
								|| activity == "15" || activity == "16") {
							
							dbcon.createContractor("EXPENSE_RECORD", " 0, 'BUILDING MAINTENANCE', "+ Building_Maintenance.maintenance +", "
									+ " "+ txtReference.getText().replaceAll(",", "")+", "+ contractor +", "
									+ " "+ date +", 8, '"+ txtRemarks.getText() +"', "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ " "+ Main_Window.dashboard +", now(), "+ Globals.G_Employee_ID+", 'Y', null, null, null ");
						}
						else {
							
						}
					}
					
				else if (mxStatus.equals("4") && status.equals("4")) {
					
						if (activity == "0" || activity == "1" || activity == "2" || activity == "3" || activity == "4" 
								|| activity == "5" || activity == "6" || activity == "7" || activity == "8" 
								|| activity == "9" || activity == "10" ) {
							
							dbcon.updateRecord("EXPENSE_RECORD", " EXPENSE_REF = "+ txtReference.getText().replaceAll(",", "")+", "
									+ "CONTRACTOR_ID = "+ contractor +", EXPENSE_TXN_DATE = "+ date +", EXPENSE_COA = 7, "
									+ "EXPENSE_DESCRIPTION = '"+ txtDesc.getText() +"', "
									+ "EXPENSE_AMOUNT = "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ", 
									" TABLE_NAME = 'BUILDING MAINTENANCE' AND TABLE_ID = "+ Building_Maintenance.maintenance +" AND REFERENCE_ID IS NULL " );
							
						}
						else if (activity == "11" || activity == "12" || activity == "13" || activity == "14" 
								|| activity == "15" || activity == "16") {
							
							dbcon.updateRecord("EXPENSE_RECORD", " EXPENSE_REF = "+ txtReference.getText().replaceAll(",", "")+", "
									+ "CONTRACTOR_ID = "+ contractor +", EXPENSE_TXN_DATE = "+ date +", EXPENSE_COA = 8, "
									+ "EXPENSE_DESCRIPTION = '"+ txtDesc.getText() +"', "
									+ "EXPENSE_AMOUNT = "+ txtTotalCost.getText().replaceAll(",", "")+", "
									+ "DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ", 
									" TABLE_NAME = 'BUILDING MAINTENANCE' AND TABLE_ID = "+ Building_Maintenance.maintenance +" AND REFERENCE_ID IS NULL " );
						}
						else {
							
						}
					}
				else if ( mxStatus.equals("4") && !status.equals("4")) {
					
					dbcon.updateRecord("EXPENSE_RECORD", " REFERENCE_ID = "+ Building_Maintenance.maintenance +", EXPENSE_DESCRIPTION = 'CHANGE FROM COMPLETED TO OTHER STATUS' ",
							" TABLE_NAME = 'BUILDING MAINTENANCE' AND TABLE_ID = "+ Building_Maintenance.maintenance +" AND REFERENCE_ID IS NULL " );
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
			modular.cancelreq(" BUILDING_MAINTENANCE ", primaryStage, e);
		} catch (SQLException e2) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err maintenance : " + e2.getMessage());
		}*/
			
			String query = "DELETE FROM BUILDING_MAINTENANCE WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
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
					EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err bldg maintenance : " + e1.getMessage());
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
			bldgmx(Building_Maintenance.maintenance);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err maintenance EDIT : " + e.getMessage());
		}
		
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
			try {
				modular.cancelreq("CONTRACTOR_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err bldg maintenance EDIT CANCEL : " + e2.getMessage());
			}
				
		});
			
	}

	public void sizeview() {
			
		txtReference.setPrefWidth(410);
		dpDate.setPrefWidth(410);
		cmbActivity.setPrefWidth(410);
		cmbBMContractor.setPrefWidth(410);
		txtDesc.setPrefWidth(410);
		cmbBMStatus.setPrefWidth(410);
		txtRemarks.setPrefWidth(410);
		txtTotalCost.setPrefWidth(410);

		lblMessage.setLayoutX(535);
		lblReference.setLayoutX(535);
		lblDate.setLayoutX(535);
		lblActivity.setLayoutX(535);
		lblDesc.setLayoutX(535);
		lblContractor.setLayoutX(535);
		lblRemarks.setLayoutX(535);
		lblBMStatus.setLayoutX(535);
		lblTotalCost.setLayoutX(535);

	}

	public void processViewOption(String uID) {
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		btnCancel.setText("CLOSE");
			
		txtReference.setDisable(true);
		dpDate.setDisable(true);
		cmbActivity.setDisable(true);
		cmbBMContractor.setDisable(true);
		txtDesc.setDisable(true);
		cmbBMStatus.setDisable(true);
		txtTotalCost.setDisable(true);
		txtRemarks.setDisable(true);
		cmbBMStatus.setDisable(true);

		try {
			bldgmx(Building_Maintenance.maintenance);

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err maintenance record view : " + e.getMessage());
		}
			 
	}
		
}
