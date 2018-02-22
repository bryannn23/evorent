package application;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Create_Billing_Statement implements Initializable {
	
	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();

	@FXML private TextField txtStatementNo;
	@FXML private TextField txtAmount;
	
	@FXML private DatePicker dpDate;
	@FXML private DatePicker dpDueDate;
	
	@FXML private ComboBox<String> cmbOccupancy;
	@FXML private ComboBox<String> cmbStatementType;
	@FXML private ComboBox<String> cmbMonth;
	@FXML private ComboBox<String> cmbBSStatus;
	
	@FXML private Label lblStatementNo;
	@FXML private Label lblOccupancy;
	@FXML private Label lblStatementType;
	@FXML private Label lblDate;
	@FXML private Label lblMonth;
	@FXML private Label lblDueDate;
	@FXML private Label lblAmount;
	@FXML private Label lblBSStatus;
	@FXML private Label lblMessage;
	
	@FXML private TableView<TableBillingStatement> tblBillingFinalize;
	@FXML private VBox vb;
	
	@FXML Button btnCancel;
	@FXML private Button btnUpdate;
	@FXML private Button btnCreate;
	@FXML private Button btnUpdateM;
	@FXML private Button btnFinalize;
	@FXML private Button btnPay;
	
	private Stage primaryStage;
	String occupancy , type, month, date, duedate, status, billing;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnUpdate.setVisible(false);
		btnUpdateM.setVisible(false);
		btnCreate.setVisible(false);
		btnPay.setVisible(false);
		btnFinalize.setDisable(true);

		lblStatementNo.setVisible(false);
		lblOccupancy.setVisible(false);
		lblStatementType.setVisible(false);
		lblDate.setVisible(false);
		lblMonth.setVisible(false);
		lblDueDate.setVisible(false);
		lblAmount.setVisible(false);
		lblBSStatus.setVisible(false);
		lblMessage.setVisible(false);

		TextFormatter<String> tfstate = modular.getTextFlexiFormatter(50, 'a'); 
		txtStatementNo.setTextFormatter(tfstate);
		TextFormatter<String> tfamt = modular.getTextFlexiFormatter(12, 'n');
		txtAmount.setTextFormatter(tfamt);
		
		comboBox.statementType(cmbStatementType);
		comboBox.monthName(cmbMonth);
		comboBox.statementStatus(cmbBSStatus);
		
		try {
			
			dbcon.cmbDisplay(cmbOccupancy, " PRIMARY_TENANT_DETAILS.* ", "TENANT_NAME", 
					"OCCUPANCY_DETAILS "
					+ " LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
					+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ",
					"OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
							+ "OCCUPANCY_DETAILS.OCCP_STATUS <> 3 " );
			
		} catch (SQLException e1) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error display combo box list : " + e1.getMessage());
		}
		
		new AutoCompleteComboBoxListener<String>(cmbOccupancy);
		new AutoCompleteComboBoxListener<String>(cmbStatementType);
		new AutoCompleteComboBoxListener<String>(cmbMonth);
		new AutoCompleteComboBoxListener<String>(cmbBSStatus);
		
		setupBillingStatement_TableView();
		
	}

	public void setupBillingStatement_TableView() {

		tblCol.TableBillingStatement(tblBillingFinalize);

		tblBillingFinalize.setStyle("-fx-table-cell-border-color: transparent; -fx-table-column-border-color: transparent; ");
        
		tblBillingFinalize.setOnMouseClicked(e -> {
			try {
				TableBillingStatement selected = tblBillingFinalize.getSelectionModel().getSelectedItem();
				billing = selected.getStateID();
				billingView(billing);
				
			} catch (Exception ex) {
			}
			if (billing != null) {
				btnCreate.setVisible(false);
				btnUpdate.setVisible(true);
			} else
				return;
		});
		
	}
	
	public void create(ActionEvent event) throws SQLException {

		lblMessage.setText("");
			
		if ( !txtStatementNo.getText().isEmpty() && cmbOccupancy.getValue() != null ) 
		{
		
			setval();
				
			modular.createWithFinalize(" BILLING_STATEMENT_DETAILS ", " STATEMENT_ID IS NULL ", 
					lblMessage, 
					" 0, '"+ txtStatementNo.getText() +"', "+ occupancy +", "+ type +", "+ date +", "+ month +", "
							+ " "+ duedate+", "+ txtAmount.getText().replaceAll(",", "") +", "+ status +", now(), "
							+ " "+Globals.G_Employee_ID+", 'N', null, null, null " , 
							event );
			
			clear();
				
		}
		else {
			check();
		}
	}

	public void check() {

		if (txtStatementNo.getText().isEmpty())
			lblStatementNo.setVisible(true);
		else
			lblStatementNo.setVisible(false);
			
		if (cmbOccupancy.getValue() == null )
			lblOccupancy.setVisible(true);
		else
			lblOccupancy.setVisible(false);
			
			lblMessage.setVisible(true);
			lblMessage.setText("Field required!");
			
		}

	public void clear() throws SQLException {
			
		if (lblMessage.getText().equals("Record already exist.")) {
				
			lblMessage.setVisible(true);
			lblMessage.setText("Record already exist.");
				
		} else {
				
			txtStatementNo.clear();
			cmbOccupancy.setValue(null);
			cmbStatementType.setValue(null);
			dpDate.setValue(null);
			cmbMonth.setValue(null);
			dpDueDate.setValue(null);
			txtAmount.clear();
			cmbBSStatus.setValue(null);
				
			dbcon.tblBillingStatement(tblBillingFinalize, " BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
					+ "BILLING_STATEMENT_DETAILS.FINALIZED_RECORD = 'N' " );
			btnFinalize.setDisable(false);
			billing = null;
				
			lblStatementNo.setVisible(false);
			lblOccupancy.setVisible(false);
			lblStatementType.setVisible(false);
			lblDate.setVisible(false);
			lblMonth.setVisible(false);
			lblDueDate.setVisible(false);
			lblAmount.setVisible(false);
			lblBSStatus.setVisible(false);
			lblMessage.setVisible(false);
				
		}
	}
		
	public void cancel() throws SQLException {
			 
		modular.cancel("BILLING_STATEMENT_DETAILS", primaryStage);

	}
		
	public void setval () throws SQLException {
		
		if (txtAmount.getText().isEmpty()) txtAmount.setText("0");
		
		if (cmbStatementType.getValue() == null )
			type = null;
		else if (cmbStatementType.getValue().equalsIgnoreCase("Rent/Lease")) 
			type = "0";
		else if (cmbStatementType.getValue().equalsIgnoreCase("Association")) 
			type = "1";
		else if (cmbStatementType.getValue().equalsIgnoreCase("Electricity"))
			type = "2";
		else if (cmbStatementType.getValue().equalsIgnoreCase("Water")) 
			type = "3";
		else if (cmbStatementType.getValue().equalsIgnoreCase("Repairs")) 
			type = "4";
		else if (cmbStatementType.getValue().equalsIgnoreCase("Others")) 
			type = "5";
		
		if (cmbMonth.getValue() == null) 
			month = null;
		else if (cmbMonth.getValue().equalsIgnoreCase("January"))
			month = "1";
		else if (cmbMonth.getValue().equalsIgnoreCase("February"))
			month = "2";
		else if (cmbMonth.getValue().equalsIgnoreCase("March"))
			month = "3";
		else if (cmbMonth.getValue().equalsIgnoreCase("April"))
			month = "4";
		else if (cmbMonth.getValue().equalsIgnoreCase("May"))
			month = "5";
		else if (cmbMonth.getValue().equalsIgnoreCase("June"))
			month = "6";
		else if (cmbMonth.getValue().equalsIgnoreCase("July"))
			month = "7";
		else if (cmbMonth.getValue().equalsIgnoreCase("August"))
			month = "8";
		else if (cmbMonth.getValue().equalsIgnoreCase("September"))
			month = "9";
		else if (cmbMonth.getValue().equalsIgnoreCase("October"))
			month = "10";
		else if (cmbMonth.getValue().equalsIgnoreCase("November"))
			month = "11";
		else if (cmbMonth.getValue().equalsIgnoreCase("December"))
			month = "12";
		
		if (cmbBSStatus.getValue() == null )
			status = null;
		else if (cmbBSStatus.getValue().equalsIgnoreCase("For Printing")) 
			status = "0";
		else if (cmbBSStatus.getValue().equalsIgnoreCase("For Distribution"))
			status = "1";
		else if (cmbBSStatus.getValue().equalsIgnoreCase("For Re-Printing"))
			status = "2";
		else if (cmbBSStatus.getValue().equalsIgnoreCase("Delivered"))
			status = "3";
		else if (cmbBSStatus.getValue().equalsIgnoreCase("Paid"))
			status = "4";
		else if (cmbBSStatus.getValue().equalsIgnoreCase("Overdue"))
			status = "5";
			
		if (dpDate.getValue() == null)
			date = null;
		else {
			date = dpDate.getValue().toString();
			date = "'" + date + "'";
		}
		
		if (dpDueDate.getValue() == null)
			duedate = null;
		else {
			duedate = dpDueDate.getValue().toString();
			duedate = "'" + duedate + "'";
		}
		
		occupancy = dbcon.getIDs(" OCCUPANCY_DETAILS.* ", "OCCUPANCY_DETAILS "
				+ " LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ",
				"OCCP_ID", "OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND PRIMARY_TENANT_DETAILS.TENANT_NAME = '"+ cmbOccupancy.getValue() +"' "
						+ "AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" ");
		
	}

	public void finalize(ActionEvent event) throws SQLException {
			  
		modular.finalize("BILLING_STATEMENT_DETAILS");
		((Node) event.getSource()).getScene().getWindow().hide();

	}
		
	public void billingView(String id) throws SQLException {
			
		String query = "SELECT BILLING_STATEMENT_DETAILS.*, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT "
				+ " FROM BILLING_STATEMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ " WHERE BILLING_STATEMENT_DETAILS.STATEMENT_ID = "+id+" ";
			
		Date date, duedate;
		String type, month, stat;
		
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
				
				txtStatementNo.setText(rs.getString("STATEMENT_NUMBER"));
				cmbOccupancy.setValue(rs.getString("TENANT"));
				
				type = rs.getString("STATEMENT_TYPE");
				
				if (type == null) cmbStatementType.setValue(null);
				else if (type.equals("0")) cmbStatementType.setValue("Rent/Lease");
				else if (type.equals("1")) cmbStatementType.setValue("Association");
				else if (type.equals("2")) cmbStatementType.setValue("Electricity");
				else if (type.equals("3")) cmbStatementType.setValue("Water");
				else if (type.equals("4")) cmbStatementType.setValue("Repairs");
				else if (type.equals("5")) cmbStatementType.setValue("Others");
				
				date = rs.getDate("STATEMENT_DATE"); 
	                
				if (date == null) {
					 
					 dpDate.getEditor().setText(rs.getString("STATEMENT_DATE")); 
				 }
	            else dpDate.setValue(date.toLocalDate());
				 
				duedate = rs.getDate("STATEMENT_DUEDATE"); 
                
				if (duedate == null) {
					 
					 dpDueDate.getEditor().setText(rs.getString("STATEMENT_DUEDATE")); 
				 }
	            else dpDueDate.setValue(date.toLocalDate());
				 
				month = rs.getString("STATEMENT_MONTH");
				
				if (month == null) cmbMonth.setValue(null);
				else if (month.equals("1")) cmbMonth.setValue("January");
				else if (month.equals("2")) cmbMonth.setValue("February");
				else if (month.equals("3")) cmbMonth.setValue("March");
				else if (month.equals("4")) cmbMonth.setValue("April");
				else if (month.equals("5")) cmbMonth.setValue("May");
				else if (month.equals("6")) cmbMonth.setValue("June");
				else if (month.equals("7")) cmbMonth.setValue("July");
				else if (month.equals("8")) cmbMonth.setValue("August");
				else if (month.equals("9")) cmbMonth.setValue("September");
				else if (month.equals("10")) cmbMonth.setValue("October");
				else if (month.equals("11")) cmbMonth.setValue("November");
				else if (month.equals("12")) cmbMonth.setValue("December");
				
				txtAmount.setText(rs.getString("STATEMENT_AMOUNT"));
				
				stat = rs.getString("STATEMENT_STATUS");
				
				if (stat == null) cmbBSStatus.setValue(null);
				else if (stat.equals("0")) cmbBSStatus.setValue("For Printing");
				else if (stat.equals("1")) cmbBSStatus.setValue("For Distribution");
				else if (stat.equals("2")) cmbBSStatus.setValue("For Re-printing");
				else if (stat.equals("3")) cmbBSStatus.setValue("Delivered");
				else if (stat.equals("4")) cmbBSStatus.setValue("Paid");
				else if (stat.equals("5")) cmbBSStatus.setValue("Overdue");
					
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected billing statement "+id+" : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
	}

	public void upd() throws SQLException {

		modular.updaterecord(" BILLING_STATEMENT_DETAILS ", " STATEMENT_NUMBER, OCCP_ID, STATEMENT_TYPE, "
			+ "STATEMENT_DATE, STATEMENT_MONTH, STATEMENT_DUEDATE, STATEMENT_AMOUNT, STATEMENT_STATUS, "
			+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
			
			" STATEMENT_NUMBER, OCCP_ID, STATEMENT_TYPE, STATEMENT_DATE, STATEMENT_MONTH, "
			+ "STATEMENT_DUEDATE, STATEMENT_AMOUNT, STATEMENT_STATUS, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "
			+ ""+ Building_Detail.billing +" ",
			" STATEMENT_ID = "+Building_Detail.billing+" ");
			
		}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( !txtStatementNo.getText().isEmpty() && cmbOccupancy.getValue() != null ) 
		{
			setval();
				
			modular.editrecordwithfinalize(" BILLING_STATEMENT_DETAILS ", " STATEMENT_ID = 0 " ,
					lblMessage,
					" STATEMENT_NUMBER = '"+ txtStatementNo.getText() +"', OCCP_ID = "+ occupancy +", "
					+ " STATEMENT_TYPE = "+ type +", STATEMENT_DATE = "+ date +" , STATEMENT_MONTH = "+ month +", "
					+ "STATEMENT_DUEDATE = "+ duedate +", STATEMENT_AMOUNT = "+ txtAmount.getText().replaceAll(",", "") +", "
					+ "STATEMENT_STATUS = "+ status +", DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" STATEMENT_ID = "+ billing +" ",
				   event, btnUpdate, btnCreate);
				
				clear();
							
		} else {
			check();
					 
		}

	}

	public void updateM(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( !txtStatementNo.getText().isEmpty() && cmbOccupancy.getValue() != null ) 
		{
						
			upd();
			setval();
				
			modular.editrecord(" BILLING_STATEMENT_DETAILS ", " STATEMENT_ID = 0 " ,
					lblMessage,
					" STATEMENT_NUMBER = '"+ txtStatementNo.getText() +"', OCCP_ID = "+ occupancy +", "
					+ " STATEMENT_TYPE = "+ type +", STATEMENT_DATE = "+ date +" , STATEMENT_MONTH = "+ month +", "
					+ "STATEMENT_DUEDATE = "+ duedate +", STATEMENT_AMOUNT = "+ txtAmount.getText().replaceAll(",", "") +", "
					+ "STATEMENT_STATUS = "+ status +", DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" STATEMENT_ID = "+ Building_Detail.billing +" ",
				   event );
							
		} else {
			check();
					 
		}

	}
	
	public void pay (ActionEvent event) {
		
		 try {
	            Stage primaryStage = new Stage();
	            primaryStage.initModality(Modality.APPLICATION_MODAL);
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/CreatePayment.fxml"));
	            Parent root = (Parent) loader.load();

	            Create_Payment cont = loader.getController();
	            cont.processAddOption(primaryStage);
	            
	            Scene scene = new Scene(root);
	            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
	            primaryStage.setScene(scene);
	            primaryStage.setMaximized(true);
	            primaryStage.showAndWait();

	        } catch (Exception e) {
	            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error rent payment : " + e.getMessage());
	        } 
	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
		try {
			modular.cancelreq(" BILLING_STATEMENT_DETAILS ", primaryStage, e);
		} catch (SQLException e2) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing statement : " + e2.getMessage());
		}
		});
		
		btnCreate.setVisible(true);

	}

	public void processEditOption(String uID, Stage parent_primaryStage) {
			
		btnUpdateM.setVisible(true);
		btnPay.setVisible(true);
		btnCancel.setText("CLOSE");
		
		//vb.getChildren().removeAll(tblBillingFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		//vb.setMaxWidth(0);
		//sizeview();
		primaryStage = parent_primaryStage;
		
		if (Building_Detail.billing != null ) {
			
			try {
			
				billingView(Building_Detail.billing);
	
				dbcon.tblBillingStatementView(tblBillingFinalize, 
						" BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
						+ "BILLING_STATEMENT_DETAILS.STATEMENT_NUMBER = '"+ Building_Detail.billingNo.replaceAll("\\s", "") +"' " );
				
				billing = Building_Detail.billing;
				
			} catch (SQLException e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing statement EDIT : " + e.getMessage());
			}
		}
		
		if (Billing_Statement.billing != null ) {
			
			try {
				
				billingView(Billing_Statement.billing);
	
				dbcon.tblBillingStatementView(tblBillingFinalize, 
						" BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
						+ "BILLING_STATEMENT_DETAILS.STATEMENT_NUMBER = '"+ Billing_Statement.billingno.replaceAll("\\s", "") +"' " );
				
				billing = Billing_Statement.billing;
				
			} catch (SQLException e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing statement EDIT : " + e.getMessage());
			}
		}
		
		primaryStage.setOnCloseRequest(e -> {
				 
			try {
				modular.cancelreq("BILLING_STATEMENT_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing statement EDIT CANCEL : " + e2.getMessage());
			}
				
		});
			
	}

	public void sizeview() {
			
		txtStatementNo.setPrefWidth(410);
		cmbOccupancy.setPrefWidth(410);
		cmbStatementType.setPrefWidth(410);
		dpDate.setPrefWidth(410);
		cmbMonth.setPrefWidth(410);
		dpDueDate.setPrefWidth(410);
		txtAmount.setPrefWidth(410);
		cmbBSStatus.setPrefWidth(410);

		lblMessage.setLayoutX(535);
		lblStatementNo.setLayoutX(535);
		lblOccupancy.setLayoutX(535);
		lblStatementType.setLayoutX(535);
		lblDate.setLayoutX(535);
		lblMonth.setLayoutX(535);
		lblDueDate.setLayoutX(535);
		lblAmount.setLayoutX(535);
		lblBSStatus.setLayoutX(535);

	}

	public void processViewOption(String uID) {
		
		vb.getChildren().removeAll(tblBillingFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		btnCancel.setText("CLOSE");
			
		txtStatementNo.setDisable(true);
		cmbOccupancy.setDisable(true);
		cmbStatementType.setDisable(true);
		dpDate.setDisable(true);
		cmbMonth.setDisable(true);
		dpDueDate.setDisable(true);
		txtAmount.setDisable(true);
		cmbBSStatus.setDisable(true);

		if (Building_Detail.billing != null ) {
			
			try {
			
				billingView(Building_Detail.billing);
	
			} catch (SQLException e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing statement EDIT : " + e.getMessage());
			}
		}
		
		if (Billing_Statement.billing != null ) {
			
			try {
				
				billingView(Billing_Statement.billing);
	
			} catch (SQLException e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing statement EDIT : " + e.getMessage());
			}
		}
			 
	}
	
}
