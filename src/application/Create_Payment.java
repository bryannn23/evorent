package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

public class Create_Payment implements Initializable {

	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();
	NumbersToWords NTW = new NumbersToWords();
	
	@FXML private ComboBox<String> cmbOccupancy;
	@FXML private ComboBox<String> cmbStatement;
	@FXML private ComboBox<String> cmbPayment;
	@FXML private ComboBox<String> cmbReceipt;
	
	@FXML private DatePicker dpDate;
	
	@FXML private TextField txtAmount;
	@FXML private TextField txtTotalAmount;
	@FXML private TextField txtAmountDue;
	
	@FXML private Label lblDate;
	@FXML private Label lblAmount;
	@FXML private Label lblPayment;
	@FXML private Label lblReceipt;
	@FXML private Label lblMessage;
	@FXML private Label lblDatePayment;
	@FXML private Label lblReceiptNo;
	@FXML private Label lblTenantName;
	@FXML private Label lblAmountToWords;
	@FXML private Label lblAmountInPeso;
	@FXML private Label lblOccupancy;
	@FXML private Label lblStatement;
	
	@FXML private Button btnCreate;
	@FXML Button btnCancel;
	@FXML private Button btnPrint;
	@FXML private Button btnFinalize;
	
	//@FXML private TableView<TableRentPayment> tblFinalize;
	@FXML private TableView<TablePayment> tblFinalize;
	
	@FXML private TableView<TableBilling> tblBillList;
	
	String occupancy, statement, payment, via, rent;
	String amount, status;
	Double balance;
	
	String totalToWords, receiptNo, receipt;
	int receiptNum;

	String tenantName, tenantAddress;

	LocalDateTime df = LocalDateTime.now();
	DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	String date = df.format(formatters);
	
	private Stage primaryStage;
	
	@FXML private TextField txtTIN;
	@FXML private TextField txtParti1;
	@FXML private TextField txtParti2;
	@FXML private TextField txtParti3;
	@FXML private TextField txtParti4;
	@FXML private TextField txtParti5;
	@FXML private TextField txtParti6;
	@FXML private TextField txtParti7;
	@FXML private TextField txtAmt1;
	@FXML private TextField txtAmt2;
	@FXML private TextField txtAmt3;
	@FXML private TextField txtAmt4;
	@FXML private TextField txtAmt5;
	@FXML private TextField txtAmt6;
	@FXML private TextField txtAmt7;
	
	@FXML private TextArea txtBuName;
	@FXML private TextArea txtRemarks;	
	
	@FXML private Label lblTenantAddress;
	@FXML private Label lblBuName;
	@FXML private Label lblTIN;
	@FXML private Label lblRemarks;
	@FXML private Label lblAD;
	
	String totalBill, totalPayment, prevMonth, totalFinalBill;
	double bal;

	@FXML private Button btnGenerate;
	@FXML private Button btnUpdate;
	String rNO;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		btnCreate.setVisible(false);
		btnFinalize.setDisable(true);
		btnUpdate.setVisible(false);
		btnGenerate.setVisible(false);
		
		lblDate.setVisible(false);
		lblAmount.setVisible(false);
		lblPayment.setVisible(false);
		lblReceipt.setVisible(false);
		lblMessage.setVisible(false);
		lblOccupancy.setVisible(false);
		lblStatement.setVisible(false);
		
		comboBox.paymentVIA(cmbPayment);
		comboBox.paymentReceipt(cmbReceipt);
		
		
		new AutoCompleteComboBoxListener<String>(cmbOccupancy);
		new AutoCompleteComboBoxListener<String>(cmbStatement);
		new AutoCompleteComboBoxListener<String>(cmbPayment);
		new AutoCompleteComboBoxListener<String>(cmbReceipt);
		
		setupRentPayment_TableView();
		
	}

	public void setupRentPayment_TableView() {

		tblCol.TableBillingRent(tblBillList);
		tblCol.TablePayment(tblFinalize);
		
		/*tblCol.TableRentPayment(tblFinalize);

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
		});*/
	}
	
	public void create(ActionEvent event) throws SQLException {

		lblMessage.setText("");
			
		if ( cmbOccupancy.getValue() != null && cmbStatement.getValue() != null 
				&& dpDate.getValue() != null ) 
		{
		
			setval();
			
			try {
				receiptNo = dbcon.receiptNo();
				
			} catch (SQLException e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err last receipt no. : " +e.getMessage());
			}
			
			if (receiptNo == null) {
				receiptNum = Integer.parseInt(Building_Detail.bldgReceipt) + 1 ;
			}
				
			else {
				receiptNum = Integer.parseInt(receiptNo) + 1;
			}
				
			modular.createWithFinalize(" RENT_PAYMENT_DETAILS ", " PAYMENT_ID = 0 ", 
					lblMessage, 
					" 0, "+ occupancy +", '"+ cmbStatement.getValue() +"', "+ receiptNum +", "+ payment +", "
					+ " "+ txtAmount.getText().replaceAll(",", "") +", "+ via+", "+ receipt +",  now(), "
							+ " "+Globals.G_Employee_ID+", 'N', null, null, null " , 
					event );
			
			lblRemarks.setText(txtRemarks.getText());
			
			display();
			generate(event);
			btnFinalize.setDisable(false); 
			
		}
		else {
			check();
		}
	}

	public void display () throws SQLException {
		
		Double num = Double.parseDouble(txtAmount.getText().replaceAll(",", ""));

        int peso = (int) Math.floor(num);
        double cents = num - peso;
        int centsAsInt = (int) (100 * cents);
        
        if (centsAsInt > 0) {
            
        	totalToWords = NTW.convert(peso) + " Pesos And " + NTW.convert(centsAsInt) + " Centavos Only";
        }
        else {
            
        	totalToWords = NTW.convert(peso) + " Pesos Only";
        }

        lblDatePayment.setText(date);
        lblReceiptNo.setText(Integer.toString(receiptNum));
        lblAmountToWords.setText(totalToWords);
        txtTotalAmount.setText(txtAmount.getText());
        lblAmountInPeso.setText(txtAmount.getText());
        
        lblBuName.setText(txtBuName.getText());
        lblTIN.setText(txtTIN.getText());
        lblRemarks.setText(txtRemarks.getText());
      
		
	}

	public void tenantDetail (String id) throws SQLException {
		
		String query = "SELECT PRIMARY_TENANT_DETAILS.*  "
				+ " FROM OCCUPANCY_DETAILS "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE OCCUPANCY_DETAILS.OCCP_ID = "+ id +" AND OCCUPANCY_DETAILS.REFERENCE_ID IS NULL ";
		
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
					
				lblTenantName.setText(rs.getString("TENANT_NAME"));
				lblTenantAddress.setText(rs.getString("TENANT_ADDRESS"));
				
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of tenant related from billing : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
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
		
		if (dpDate.getValue() == null)
			lblDate.setVisible(true);
		else 
			lblDate.setVisible(false);
			
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
			dpDate.setValue(null);
			cmbPayment.setValue(null);
			cmbReceipt.setValue(null);
			txtAmount.clear();
				
			//dbcon.tblRentPayment(tblFinalize, "RENT_PAYMENT_DETAILS.REFERENCE_ID IS NULL AND RENT_PAYMENT_DETAILS.FINALIZED_RECORD = 'N' " );
			btnFinalize.setDisable(false);
			rent = null;
				
			//lblOccupancy.setVisible(false);
			//lblStatement.setVisible(false);
			lblDate.setVisible(false);
			lblAmount.setVisible(false);
			lblPayment.setVisible(false);
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
		
		if (dpDate.getValue() == null)
			payment = null;
		else {
			payment = dpDate.getValue().toString();
			payment = "'" + payment + "'";
		}
		
		if (cmbPayment.getValue() == null)
			via = null;
		else if (cmbPayment.getValue().equalsIgnoreCase("Post Dated Check"))
			via = "0";
		else if (cmbPayment.getValue().equalsIgnoreCase("CASH"))
			via = "1";
		else if (cmbPayment.getValue().equalsIgnoreCase("Bank Deposit"))
			via = "2";
		else if (cmbPayment.getValue().equalsIgnoreCase("Others"))
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
			
		String query = "SELECT RENT_PAYMENT_DETAILS.*, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT, "
				+ "PRIMARY_TENANT_DETAILS.TENANT_ADDRESS AS ADDRESS "
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
					 
					 dpDate.getEditor().setText(rs.getString("PAYMENT_DATE")); 
				 }
	            else dpDate.setValue(date.toLocalDate());

				txtAmount.setText(rs.getString("PAYMENT_AMOUNT"));
				
				via = rs.getString("PAYMENT_VIA");
				
				if (via == null) cmbPayment.setValue(null);
				else if (via.equals("0")) cmbPayment.setValue("Post Dated Check");
				else if (via.equals("1")) cmbPayment.setValue("Cash");
				else if (via.equals("2")) cmbPayment.setValue("Bank Deposit");
				else if (via.equals("3")) cmbPayment.setValue("OTHERS");
				
				receipt = rs.getString("PAYMENT_RECEIPT");
				
				if (receipt == null) cmbReceipt.setValue(null);
				else if (receipt.equals("0")) cmbReceipt.setValue("For Printing");
				else if (receipt.equals("1")) cmbReceipt.setValue("For Delivery");
				else if (receipt.equals("2")) cmbReceipt.setValue("Issued");
				else if (receipt.equals("3")) cmbReceipt.setValue("Others");
					
				Double num = Double.parseDouble(txtAmount.getText().replaceAll(",", ""));

		        int peso = (int) Math.floor(num);
		        double cents = num - peso;
		        int centsAsInt = (int) (100 * cents);
		        
		        if (centsAsInt > 0) {
		            
		        	totalToWords = NTW.convert(peso) + " Pesos And " + NTW.convert(centsAsInt) + " Centavos Only";
		        }
		        else {
		            
		        	totalToWords = NTW.convert(peso) + " Pesos Only";
		        }

		        lblDatePayment.setText(date.toString());
		        lblReceiptNo.setText(rs.getString("PAYMENT_RECEIPT_NO"));
		        rNO = rs.getString("PAYMENT_RECEIPT_NO");
		        receiptNum = rs.getInt("PAYMENT_RECEIPT_NO");
		        lblAmountToWords.setText(totalToWords);
		        txtTotalAmount.setText(txtAmount.getText());
		        lblAmountInPeso.setText(txtAmount.getText());
		        
		        lblBuName.setText(txtBuName.getText());
		        lblTIN.setText(txtTIN.getText());
		        lblRemarks.setText(txtRemarks.getText());
				
				
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
			+ "PAYMENT_AMOUNT, PAYMENT_VIA, PAYMENT_RECEIPT, DATETIME_ENTRY, "
			+ "SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
			
			" OCCP_ID, STATEMENT_NUMBER, PAYMENT_RECEIPT_NO, PAYMENT_DATE, PAYMENT_AMOUNT, PAYMENT_VIA, PAYMENT_RECEIPT, "
			+ " DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "+ Building_Detail.rentPayment +" ",
			" PAYMENT_ID = "+ Building_Detail.rentPayment +" ");
			
		}

/*	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbOccupancy.getValue() != null && cmbStatement.getValue() != null 
				&& dpDate.getValue() != null ) 
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

	}*/

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbOccupancy.getValue() != null && cmbStatement.getValue() != null 
				&& dpDate.getValue() != null ) 
		{
						
			upd();
			setval();
				
			modular.editrecord(" RENT_PAYMENT_DETAILS ", " PAYMENT_ID = 0 " ,
					lblMessage,
					" OCCP_ID = "+ occupancy +", STATEMENT_NUMBER = '"+ cmbStatement.getValue() +"', PAYMENT_DATE = "+ payment +", "
					+ " PAYMENT_AMOUNT = "+ txtAmount.getText().replaceAll(",", "") +", "
					+ " PAYMENT_VIA = "+ via +" , PAYMENT_RECEIPT = "+ receipt +", "
					+ " DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" PAYMENT_ID = "+ Building_Detail.rentPayment +" ",
				   event );
							
		} else {
			check();
					 
		}

	}
	
	public void generate (ActionEvent event) throws SQLException {
		
		if (Building_Detail.billingOccp != null ) {
			tenantDetail(Building_Detail.billingOccp);
		}
		else if (Building_Detail.rentOccp != null ) {
			tenantDetail(Building_Detail.rentOccp);
		}
		
		display();
		lblReceiptNo.setText(rNO);
		
		ObservableList<TablePayment> data = FXCollections.observableArrayList();
		
		data.addAll(new TablePayment("1", txtParti1.getText(), txtAmt1.getText().replaceAll(",", "")), 
				new TablePayment ("2", txtParti2.getText(), txtAmt2.getText().replaceAll(",", "")), 
				new TablePayment ( "3", txtParti3.getText(), txtAmt3.getText().replaceAll(",", "")), 
				new TablePayment ( "4", txtParti4.getText(), txtAmt4.getText().replaceAll(",", "")), 
				new TablePayment ( "5", txtParti5.getText(), txtAmt5.getText().replaceAll(",", "")), 
				new TablePayment ( "6", txtParti6.getText(), txtAmt6.getText().replaceAll(",", "")), 
				new TablePayment ( "7", txtParti7.getText(), txtAmt7.getText().replaceAll(",", ""))
				);
		
		tblFinalize.setItems(data);
		
	}
	
	public void print (ActionEvent event) throws SQLException {
		
		int loop, size;

        size = tblFinalize.getItems().size();

        String[][] tableString = new String[size][2];

        for (loop = 0; loop < size; loop++) {
            TablePayment selectedRecord = (TablePayment) tblFinalize.getItems().get(loop);

            tableString[loop][0] = selectedRecord.getPayParti();
            tableString[loop][1] = selectedRecord.getPayAmount();

        }
        
        String preparedby =  Globals.G_Employee_Name ;
        
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        double total = Double.parseDouble(txtAmount.getText().replaceAll(",", ""));
        

        try {
            new GenerateReport().receipt(date, Integer.toString(receiptNum), Building_Detail.bldgName,
            		Building_Detail.bldgAddress, formatter.format(total), lblTenantName.getText(), lblBuName.getText(),
            		lblTenantAddress.getText(), lblAmountToWords.getText(), lblRemarks.getText(), preparedby, 
            		txtParti1.getText(), txtParti2.getText(), txtParti3.getText(), txtParti4.getText(), txtParti5.getText(), 
            		txtParti6.getText(), txtParti7.getText(), txtAmt1.getText().replaceAll(",", ""), 
            		txtAmt2.getText().replaceAll(",", ""), txtAmt3.getText().replaceAll(",", ""), 
            		txtAmt4.getText().replaceAll(",", ""), txtAmt5.getText().replaceAll(",", ""), 
            		txtAmt6.getText().replaceAll(",", ""), txtAmt7.getText().replaceAll(",", ""), 
            		tableString);
            
        } catch (ClassNotFoundException | JRException e1) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err receipt detail : " + e1.toString() + " -- " + e1.getMessage());
        }
        
	}
	
	public void processAddOption(Stage parent_primaryStage) throws SQLException {

		if (Building_Detail.billing != null) {
			
			cmbOccupancy.setEditable(false);
			cmbStatement.setEditable(false);
			
			cmbOccupancy.setValue(Building_Detail.billingTenant.trim());
			cmbStatement.setValue(Building_Detail.billingNo.replaceAll("\\s", ""));
			
			dbcon.tblBillingVoucher(tblBillList, " REFERENCE_ID IS NULL AND "
					+ "STATEMENT_NUMBER = '"+ Building_Detail.billingNo.replaceAll("\\s", "") +"' ");
			
			occupancy = Building_Detail.billingOccp;
			
			totalBill = dbcon.totalBilling(occupancy + " AND "
					+ "BILLING_STATEMENT_DETAILS.STATEMENT_NUMBER <> "+ Building_Detail.billingNo.replaceAll("\\s", "") +" ");
			
			totalPayment = dbcon.totalPayment(occupancy);
			totalFinalBill = dbcon.totalBilling(occupancy);

			if (totalBill == null) totalBill = "0";
			if (totalPayment == null) totalPayment = "0";
					
			double due = Double.parseDouble(totalFinalBill) - Double.parseDouble(totalPayment);
			txtAmountDue.setText(Double.toString(due));
			txtAmountDue.setDisable(true);
			
			
			bal = Double.parseDouble(totalBill) - Double.parseDouble(totalPayment);
			
			if (bal > 0) {
				
				prevMonth = "BALANCE FROM PREVIOUS MONTH(S)";
				
				ObservableList<TableBilling> data = FXCollections.observableArrayList();
				
				data.addAll(new TableBilling(
						 "0",
						prevMonth,
						"", 
						Double.toString(bal)));
				
				tblBillList.getItems().addAll(data);
				
			}
		}
		
		else if (Billing_Statement.billing != null) {
			
			cmbOccupancy.setEditable(false);
			cmbStatement.setEditable(false);
			 
			cmbOccupancy.setValue(Billing_Statement.billingTenant.trim());
			cmbStatement.setValue(Billing_Statement.billingno.replaceAll("\\s", ""));
			
			dbcon.tblBillingVoucher(tblBillList, " REFERENCE_ID IS NULL AND "
					+ "STATEMENT_NUMBER = '"+ Billing_Statement.billingno.replaceAll("\\s", "") +"' ");
			
			occupancy = Billing_Statement.billingOccp;
			
			totalBill = dbcon.totalBilling(occupancy + " AND "
					+ "BILLING_STATEMENT_DETAILS.STATEMENT_NUMBER <> "+ Billing_Statement.billingno.replaceAll("\\s", "") +" ");
			
			totalPayment = dbcon.totalPayment(occupancy);
			totalFinalBill = dbcon.totalBilling(occupancy);

			if (totalBill == null) totalBill = "0";
			if (totalPayment == null) totalPayment = "0";
					
			double due = Double.parseDouble(totalFinalBill) - Double.parseDouble(totalPayment);
			txtAmountDue.setText(Double.toString(due));
			txtAmountDue.setDisable(true);
			
			
			bal = Double.parseDouble(totalBill) - Double.parseDouble(totalPayment);
			
			if (bal > 0) {
				
				prevMonth = "BALANCE FROM PREVIOUS MONTH(S)";
				
				ObservableList<TableBilling> data = FXCollections.observableArrayList();
				
				data.addAll(new TableBilling(
						 "0",
						prevMonth,
						"", 
						Double.toString(bal)));
				
				tblBillList.getItems().addAll(data);
				
			}
		}
		
		else if (Occupancy.occpID != null) {
			
			cmbOccupancy.setEditable(false);
			cmbStatement.setEditable(false);
			
			String tenant = null, statement = null;
			
			tenant = dbcon.getOccupant(Occupancy.occpID);
			statement = dbcon.getStatement(Occupancy.occpID);
			
			cmbOccupancy.setValue(tenant);
			cmbStatement.setValue(statement);
			
			dbcon.tblBillingVoucher(tblBillList, " REFERENCE_ID IS NULL AND "
					+ "STATEMENT_NUMBER = '"+ statement +"' ");
			
			occupancy = Occupancy.occpID;
			
			totalBill = dbcon.totalBilling(occupancy + " AND "
					+ "BILLING_STATEMENT_DETAILS.STATEMENT_NUMBER <> "+ statement +" ");
			
			totalPayment = dbcon.totalPayment(occupancy);
			totalFinalBill = dbcon.totalBilling(occupancy);

			if (totalBill == null) totalBill = "0";
			if (totalPayment == null) totalPayment = "0";
					
			double due = Double.parseDouble(totalFinalBill) - Double.parseDouble(totalPayment);
			txtAmountDue.setText(Double.toString(due));
			txtAmountDue.setDisable(true);
			
			
			bal = Double.parseDouble(totalBill) - Double.parseDouble(totalPayment);
			
			if (bal > 0) {
				
				prevMonth = "BALANCE FROM PREVIOUS MONTH(S)";
				
				ObservableList<TableBilling> data = FXCollections.observableArrayList();
				
				data.addAll(new TableBilling(
						 "0",
						prevMonth,
						"", 
						Double.toString(bal)));
				
				tblBillList.getItems().addAll(data);
				
			}
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
	
	public void processCreateOption(Stage parent_primaryStage) throws SQLException {

		try {
			
			dbcon.cmbDisplay(cmbOccupancy, "PRIMARY_TENANT_DETAILS.*", "TENANT_NAME", "OCCUPANCY_DETAILS "
					+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
					+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ",
					"OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
							+ "OCCUPANCY_DETAILS.OCCP_STATUS <> 3 ");
			
			dbcon.cmbDisplay(cmbStatement, "*", "STATEMENT_NUMBER", "BILLING_STATEMENT_DETAILS", "REFERENCE_ID IS NULL "
					+ " AND STATEMENT_ID IN (SELECT MAX(STATEMENT_ID) FROM BILLING_STATEMENT_DETAILS "
				+ "WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID )" );
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of occp and billing : " + e.getMessage());
		}

		tblBillList.setVisible(false);
		lblAD.setVisible(false);
		txtAmountDue.setVisible(false);
		
		cmbOccupancy.setOnAction( e-> {
			
			try {

				dbcon.setDisplay(cmbStatement, " BILLING_STATEMENT_DETAILS.STATEMENT_NUMBER AS STATENUM", "STATENUM",
						"BILLING_STATEMENT_DETAILS "
						+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
						+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID ",
						" BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
						+ "PRIMARY_TENANT_DETAILS.TENANT_NAME = '"+ cmbOccupancy.getValue()+"' AND "
						+ " STATEMENT_ID IN (SELECT MAX(STATEMENT_ID) FROM BILLING_STATEMENT_DETAILS "
				+ "WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID ) " );
				
			} catch (SQLException e1) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display statement number of the selected occupancy: " + e1.getMessage());
			}
		});
		
		cmbStatement.setOnAction( e-> {
			
			try {
				dbcon.setDisplay(cmbOccupancy, " PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT ", "TENANT", 
						" BILLING_STATEMENT_DETAILS "
						+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
						+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID ",
						" BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
						+ "BILLING_STATEMENT_DETAILS.STATEMENT_NUMBER = '"+ cmbStatement.getValue() +"' " );
				
			} catch (SQLException e1) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display TENANT NAME of the selected STATEMENT NUMBER: " + e1.getMessage());
			}
		});
		
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
			
		btnUpdate.setVisible(true);
		btnGenerate.setVisible(true);
		btnCancel.setText("CLOSE");

		tblBillList.setVisible(false);
		lblAD.setVisible(false);
		txtAmountDue.setVisible(false);
		
		
		try {
			tenantDetail(Building_Detail.rentOccp);
			//display();
			
		} catch (SQLException e1) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err rent payment EDIT print receipt display : " + e1.getMessage());
		}
		
		
		btnFinalize.setVisible(false);
			
		try {
			paymentView(Building_Detail.rentPayment);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err rent payment EDIT : " + e.getMessage());
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

	public void processViewOption(String uID) {
		
		ActionEvent event = null;
		btnFinalize.setVisible(false);
		btnCancel.setText("CLOSE");

		tblBillList.setVisible(false);
		lblAD.setVisible(false);
		txtAmountDue.setVisible(false);
		
		
		try {
			tenantDetail(Building_Detail.rentOccp);
			
		} catch (SQLException e1) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err rent payment EDIT print receipt display : " + e1.getMessage());
		}
		
		
		cmbOccupancy.setDisable(true);
		cmbStatement.setDisable(true);
		dpDate.setDisable(true);
		txtAmount.setDisable(true);
		cmbPayment.setDisable(true);
		cmbReceipt.setDisable(true);

		try {
			paymentView( Building_Detail.rentPayment );
			generate(event);
			display();
			
			lblReceiptNo.setText(rNO);
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err rent payment record view : " + e.getMessage());
		}
			 
	}
	
	
}
