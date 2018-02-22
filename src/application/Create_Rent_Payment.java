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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Create_Rent_Payment implements Initializable {

	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();
	
	@FXML private ComboBox<String> cmbOccupancy;
	@FXML private ComboBox<String> cmbStatement;
	@FXML private ComboBox<String> cmbVIA;
	@FXML private ComboBox<String> cmbReceipt;
	
	@FXML private DatePicker dpPayment;
	
	@FXML private TextField txtAmount;
	
	@FXML private Label lblOccupancy;
	@FXML private Label lblStatement;
	@FXML private Label lblPayment;
	@FXML private Label lblAmount;
	@FXML private Label lblVIA;
	@FXML private Label lblReceipt;
	@FXML private Label lblMessage;
	
	@FXML private Button btnCreate;
	@FXML Button btnCancel;
	@FXML private Button btnUpdate;
	@FXML private Button btnUpdateM;
	@FXML private Button btnFinalize;
	
	@FXML private TableView<TableRentPayment> tblFinalize;
	
	@FXML private VBox vb;
	
	String occupancy, statement, payment, via, receipt, rent;
	String amount, status;
	Double balance;
	
	private Stage primaryStage;
	String rentPayment;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		btnCreate.setVisible(false);
		btnUpdate.setVisible(false);
		btnUpdateM.setVisible(false);
		btnFinalize.setDisable(true);
		
		lblOccupancy.setVisible(false);
		lblStatement.setVisible(false);
		lblPayment.setVisible(false);
		lblAmount.setVisible(false);
		lblVIA.setVisible(false);
		lblReceipt.setVisible(false);
		lblMessage.setVisible(false);
		
		comboBox.paymentVIA(cmbVIA);
		comboBox.paymentReceipt(cmbReceipt);
		
		try {
			
			dbcon.cmbDisplay(cmbOccupancy, "PRIMARY_TENANT_DETAILS.*", "TENANT_NAME", "OCCUPANCY_DETAILS "
					+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
					+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ",
					"OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
							+ "OCCUPANCY_DETAILS.OCCP_STATUS <> 3 ");
			
			dbcon.cmbDisplay(cmbStatement, "*", "STATEMENT_NUMBER", "BILLING_STATEMENT_DETAILS", "REFERENCE_ID IS NULL" );
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of occp and billing : " + e.getMessage());
		}
		
		new AutoCompleteComboBoxListener<String>(cmbOccupancy);
		new AutoCompleteComboBoxListener<String>(cmbStatement);
		new AutoCompleteComboBoxListener<String>(cmbVIA);
		new AutoCompleteComboBoxListener<String>(cmbReceipt);
		
		setupRentPayment_TableView();
		
	}

	public void setupRentPayment_TableView() {

		tblCol.TableRentPayment(tblFinalize);

		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableRentPayment selected = tblFinalize.getSelectionModel().getSelectedItem();
				rent = selected.getPayID();
				paymentView(rent);
				
			} catch (Exception ex) {
			}
			if (rent != null) {
				btnCreate.setVisible(false);
				btnUpdate.setVisible(true);
			} else
				return;
		});
	}
	
	public void create(ActionEvent event) throws SQLException {

		lblMessage.setText("");
			
		if ( cmbOccupancy.getValue() != null && cmbStatement.getValue() != null 
				&& dpPayment.getValue() != null ) 
		{
		
			setval();
				
			modular.createWithFinalize(" RENT_PAYMENT_DETAILS ", " REFERENCE_ID IS NULL AND PAYMENT_ID = 0 ", 
					lblMessage, 
					" 0, "+ occupancy +", "+ cmbStatement.getValue() +", "+ payment +", "+ txtAmount.getText().replaceAll(",", "") +", "
					+ " "+ via+", "+ receipt +", now(), "+Globals.G_Employee_ID+", 'N', null, null, null " , 
					event );
			
			clear();
				
		}
		else {
			check();
		}
	}

	public void check() {

		if ( cmbStatement.getValue() == null )
			lblStatement.setVisible(true);
		else
			lblStatement.setVisible(false);
			
		if (cmbOccupancy.getValue() == null )
			lblOccupancy.setVisible(true);
		else
			lblOccupancy.setVisible(false);
		
		if (dpPayment.getValue() == null ) 
			lblPayment.setVisible(true);
		else 
			lblPayment.setVisible(false);
			
			lblMessage.setVisible(true);
			lblMessage.setText("Field required!");
			
		}

	public void clear() throws SQLException {
			
		if (lblMessage.getText().equals("Record already exist.")) {
				
			lblMessage.setVisible(true);
			lblMessage.setText("Record already exist.");
				
		} else {
				
			cmbOccupancy.setValue(null);
			cmbStatement.setValue(null);
			dpPayment.setValue(null);
			cmbVIA.setValue(null);
			cmbReceipt.setValue(null);
			txtAmount.clear();
				
			dbcon.tblRentPayment(tblFinalize, "RENT_PAYMENT_DETAILS.REFERENCE_ID IS NULL AND RENT_PAYMENT_DETAILS.FINALIZED_RECORD = 'N' " );
			btnFinalize.setDisable(false);
			rent = null;
				
			lblOccupancy.setVisible(false);
			lblStatement.setVisible(false);
			lblPayment.setVisible(false);
			lblAmount.setVisible(false);
			lblVIA.setVisible(false);
			lblReceipt.setVisible(false);
			lblMessage.setVisible(false);
				
		}
	}
		
	public void cancel() throws SQLException {
			 
		modular.cancel("RENT_PAYMENT_DETAILS", primaryStage);

	}
		
	public void setval () throws SQLException {
		
		if (txtAmount.getText().isEmpty()) txtAmount.setText("0");
		
		occupancy = dbcon.getIDs("OCCUPANCY_DETAILS.* ",
				"OCCUPANCY_DETAILS LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ", 
				"OCCP_ID", "OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND PRIMARY_TENANT_DETAILS.TENANT_NAME = '"+ cmbOccupancy.getValue() +"' "
						+ "AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" ");
		
		//statement = dbcon.getIDs("*", "BILLING_STATEMENT_DETAILS", "STATEMENT_ID", "REFERENCE_ID IS NULL AND STATEMENT_NUMBER = '"+ cmbStatement.getValue() +"' ");
		
		if (dpPayment.getValue() == null)
			payment = null;
		else {
			payment = dpPayment.getValue().toString();
			payment = "'" + payment + "'";
		}
		
		if (cmbVIA.getValue() == null)
			via = null;
		else if (cmbVIA.getValue().equalsIgnoreCase("Post Dated Check"))
			via = "0";
		else if (cmbVIA.getValue().equalsIgnoreCase("CASH"))
			via = "1";
		else if (cmbVIA.getValue().equalsIgnoreCase("Bank Deposit"))
			via = "2";
		else if (cmbVIA.getValue().equalsIgnoreCase("Others"))
			via = "3";
		
		if (cmbReceipt.getValue() == null)
			receipt = null;
		else if (cmbReceipt.getValue().equalsIgnoreCase("For Printing"))
			receipt = "0";
		else if (cmbReceipt.getValue().equalsIgnoreCase("For Delivery"))
			receipt = "1";
		else if (cmbReceipt.getValue().equalsIgnoreCase("Issued"))
			receipt = "2";
		else if (cmbReceipt.getValue().equalsIgnoreCase("Others"))
			receipt = "3";
		
		
	}

	public void finalize(ActionEvent event) throws SQLException {
			  
		modular.finalize("RENT_PAYMENT_DETAILS");
		((Node) event.getSource()).getScene().getWindow().hide();

	}
		
	public void paymentView(String id) throws SQLException {
			
		String query = "SELECT RENT_PAYMENT_DETAILS.*, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT "
				+ " FROM RENT_PAYMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = RENT_PAYMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ " WHERE RENT_PAYMENT_DETAILS.PAYMENT_ID = "+id+" ";
			
		Date date ;
		String via, receipt;
		
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
				
				cmbOccupancy.setValue(rs.getString("TENANT"));
				cmbStatement.setValue(rs.getString("STATEMENT_NUMBER"));
				
				date = rs.getDate("PAYMENT_DATE"); 
	                
				if (date == null) {
					 
					 dpPayment.getEditor().setText(rs.getString("PAYMENT_DATE")); 
				 }
	            else dpPayment.setValue(date.toLocalDate());

				txtAmount.setText(rs.getString("PAYMENT_AMOUNT"));
				
				via = rs.getString("PAYMENT_VIA");
				
				if (via == null) cmbVIA.setValue(null);
				else if (via.equals("0")) cmbVIA.setValue("Post Dated Check");
				else if (via.equals("1")) cmbVIA.setValue("Cash");
				else if (via.equals("2")) cmbVIA.setValue("Bank Deposit");
				else if (via.equals("3")) cmbVIA.setValue("OTHERS");
				
				receipt = rs.getString("PAYMENT_RECEIPT");
				
				if (receipt == null) cmbReceipt.setValue(null);
				else if (receipt.equals("0")) cmbReceipt.setValue("For Printing");
				else if (receipt.equals("1")) cmbReceipt.setValue("For Delivery");
				else if (receipt.equals("2")) cmbReceipt.setValue("Issued");
				else if (receipt.equals("3")) cmbReceipt.setValue("Others");
					
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected rent payment "+id+" : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
	}

	public void upd() throws SQLException {

		modular.updaterecord(" RENT_PAYMENT_DETAILS ", " OCCP_ID, STATEMENT_NUMBER, PAYMENT_RECEIPT_NO, PAYMENT_DATE, "
			+ "PAYMENT_AMOUNT, PAYMENT_VIA, PAYMENT_RECEIPT, DATETIME_ENTRY,"
			+ " SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
			
			" OCCP_ID, STATEMENT_NUMBER, PAYMENT_RECEIPT_NO, PAYMENT_DATE, PAYMENT_AMOUNT, PAYMENT_VIA, PAYMENT_RECEIPT, "
			+ " DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "+ rentPayment +" ",
			" PAYMENT_ID = "+ rentPayment +" ");
			
		}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbOccupancy.getValue() != null && cmbStatement.getValue() != null 
				&& dpPayment.getValue() != null ) 
		{
			setval();
				
			modular.editrecordwithfinalize(" RENT_PAYMENT_DETAILS ", " PAYMENT_ID = 0 " ,
					lblMessage,
					" OCCP_ID = "+ occupancy +", STATEMENT_NUMBER = '"+ cmbStatement.getValue() +"', PAYMENT_DATE = "+ payment +", "
					+ " PAYMENT_AMOUNT = "+ txtAmount.getText().replaceAll(",", "") +", "
					+ " PAYMENT_VIA = "+ via +" , PAYMENT_RECEIPT = "+ receipt +", "
					+ " DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" PAYMENT_ID = "+ rent +" ",
				   event, btnUpdate, btnCreate);
				
				clear();
							
		} else {
			check();
					 
		}

	}

	public void updateM(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbOccupancy.getValue() != null && cmbStatement.getValue() != null 
				&& dpPayment.getValue() != null ) 
		{
						
			upd();
			setval();
				
			modular.editrecord(" RENT_PAYMENT_DETAILS ", " PAYMENT_ID = 0 " ,
					lblMessage,
					" OCCP_ID = "+ occupancy +", STATEMENT_NUMBER = '"+ cmbStatement.getValue() +"', PAYMENT_DATE = "+ payment +", "
					+ " PAYMENT_AMOUNT = "+ txtAmount.getText().replaceAll(",", "") +", "
					+ " PAYMENT_VIA = "+ via +" , PAYMENT_RECEIPT = "+ receipt +", "
					+ " DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" PAYMENT_ID = "+ rentPayment +" ",
				   event );
							
		} else {
			check();
					 
		}

	}

	public void processAddOption(Stage parent_primaryStage) throws SQLException {

		if (Building_Detail.billing != null) {
			
			cmbOccupancy.setEditable(false);
			cmbStatement.setEditable(false);
			
			cmbOccupancy.setValue(Building_Detail.billingTenant.replaceAll("\\s", ""));
			cmbStatement.setValue(Building_Detail.billingNo.replaceAll("\\s", ""));
			
		}
		
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
		try {
			modular.cancelreq(" RENT_PAYMENT_DETAILS ", primaryStage, e);
		} catch (SQLException e2) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err rent payment : " + e2.getMessage());
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
		
		if (Building_Detail.rentPayment != null) {
			
			try {
				paymentView(Building_Detail.rentPayment);
			} catch (SQLException e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err rent payment EDIT : " + e.getMessage());
			}
			rentPayment = Building_Detail.rentPayment;
			
		}
		else if (Issued_Receipt.receipt != null ) {
			
			try {
				paymentView(Issued_Receipt.receipt);
			} catch (SQLException e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err rent payment EDIT : " + e.getMessage());
			}
			rentPayment = Issued_Receipt.receipt;
			
		}
		 
		
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
			try {
				modular.cancelreq("RENT_PAYMENT_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err rent payment EDIT CANCEL : " + e2.getMessage());
			}
				
		});
			
	}

	public void sizeview() {
			
		cmbOccupancy.setPrefWidth(410);
		cmbStatement.setPrefWidth(410);
		dpPayment.setPrefWidth(410);
		txtAmount.setPrefWidth(410);
		cmbVIA.setPrefWidth(410);
		cmbReceipt.setPrefWidth(410);

		lblMessage.setLayoutX(535);
		lblOccupancy.setLayoutX(535);
		lblStatement.setLayoutX(535);
		lblPayment.setLayoutX(535);
		lblAmount.setLayoutX(535);
		lblVIA.setLayoutX(535);
		lblReceipt.setLayoutX(535);

	}

	public void processViewOption(String uID) {
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		btnCancel.setText("CLOSE");
			
		cmbOccupancy.setDisable(true);
		cmbStatement.setDisable(true);
		dpPayment.setDisable(true);
		txtAmount.setDisable(true);
		cmbVIA.setDisable(true);
		cmbReceipt.setDisable(true);

		if (Building_Detail.rentPayment != null) {
			
			try {
				paymentView(Building_Detail.rentPayment);
			} catch (SQLException e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err rent payment EDIT : " + e.getMessage());
			}
		}
		else if (Issued_Receipt.receipt != null ) {
			
			try {
				paymentView(Issued_Receipt.receipt);
			} catch (SQLException e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err rent payment EDIT : " + e.getMessage());
			}
		}
			 
	}
	
	
}
