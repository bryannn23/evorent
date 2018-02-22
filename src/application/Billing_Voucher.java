package application;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

public class Billing_Voucher implements Initializable {

	public DBconnection dbcon = new DBconnection();
	
	PreparedStatement ps1 = null;
	ResultSet rs1 = null;
	
	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues combo = new ComboBoxValues();
	NumbersToWords NTW = new NumbersToWords();
	
	@FXML private TableView<TableBilling> tblBillingStatement;
	@FXML private TableView<TableBillingFinalize> tblFinalize;
	
	@FXML private ComboBox<String> cmbMonth;
	@FXML private ComboBox<String> cmbDetail;
	@FXML private ComboBox<String> cmbUnit;
	@FXML private ComboBox<String> cmbTenant;
	
	@FXML private TextField txtBalance;
	@FXML private TextField txtTotalAmount;
	@FXML private TextField txtAmount;
	@FXML private TextField txtBillingCycle;
	@FXML private TextField txtRentAmount;
	@FXML private TextField txtAssocFee;
	
	@FXML private TextArea txtBilledTo;
	
	@FXML private Label lblDueDate;
	@FXML private Label lblDate;
	@FXML private Label lblStateNo;
	@FXML private Label lblMessage;
	@FXML private Label lblMonth;
	@FXML private Label lblAmount;
	@FXML private Label lblDetails;
	@FXML private Label lblUnit;
	@FXML private Label lblTenant;
	@FXML private Label lblBillingCycle;
	
	@FXML private Button btnCancel;
	@FXML private Button btnAddDetail;
	@FXML private Button btnGenerateBilling;
	@FXML private Button btnPrint;
	@FXML private Button btnFinalize;
	
	String month, statementno ;
	String type , amount, totalToWords ;
	
	Double total; 
	int stateno;
	
	String tname, taddress, tlandline, tphone, temailadd, occpID, occpIDUnit;
	Image imgLogo;
		
	Stage primaryStage;
	String addDet, billingID;
	
	String totalBill, totalPayment, prevMonth;
	double balance;
	
	LocalDateTime df = LocalDateTime.now();
	DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	String datenow = df.format(formatters);
	LocalDate dueDate;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		btnGenerateBilling.setDisable(true);
		btnFinalize.setDisable(true);
		
		lblUnit.setVisible(false);
		lblTenant.setVisible(false);
		lblBillingCycle.setVisible(false);
		lblDetails.setVisible(false);
		lblMonth.setVisible(false);
		lblAmount.setVisible(false);
		lblMessage.setVisible(false);
		txtTotalAmount.setEditable(false);
		
		txtBalance.setDisable(true);
		TextFormatter<String> tfamt = modular.getTextFlexiFormatter(12, 'n');
		txtAmount.setTextFormatter(tfamt);
		
		cmbMonth.setDisable(true);
		txtAmount.setDisable(true);
		
		combo.monthName(cmbMonth);
		combo.statementType(cmbDetail);
		
		cmbDetail.setOnMouseClicked( e-> {
			
			cmbMonth.setDisable(false);
		});
		
		cmbMonth.setOnMouseEntered( e-> {
			
			txtAmount.setDisable(false);
			
			if (cmbDetail.getValue().equalsIgnoreCase("Rent/Lease")) {

				txtAmount.setText(txtRentAmount.getText());
				
			}
			else {
				txtAmount.setText("");
			}
			
			
		});
		
		initializaTable();
		
		new AutoCompleteComboBoxListener<String>(cmbDetail);
		new AutoCompleteComboBoxListener<String>(cmbMonth);
		
		try {
			statementno = dbcon.statementNo();
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err last billing statement no. : " +e.getMessage());
		}
		
		
		if (statementno == null) {
			stateno = Integer.parseInt(Building_Detail.bldgBilling) + 1 ;
			System.out.println(stateno);
		}
			
		else {
			stateno = Integer.parseInt(statementno) + 1;
		}
		
	}

	public void initializaTable () {
		
		tblCol.TableBilling(tblBillingStatement);
		tblCol.TableBillingFinalize(tblFinalize);
		
		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableBillingFinalize selected = tblFinalize.getSelectionModel().getSelectedItem();
				billingID = selected.getStateID();
				
			} catch (Exception ex) {
			}
			if (billingID != null) {
				//btnRemoveDetail.setDisable(false);
				
			} else
				return;
		});
		
	}
	
	/*public void add(ActionEvent event) throws SQLException {
		
		if (cmbDetail.getValue() != null) {

			addDet = cmbDetail.getValue();
			lstDetail.getItems().addAll(addDet);
			cmbDetail.setValue("");
			
		} else {
			
		}
		
	}

	public void del(ActionEvent event) throws SQLException {
		
		final int selectedIdx = lstDetail.getSelectionModel().getSelectedIndex();
		
		if (selectedIdx != -1) {
			
			String itemToRemove = lstDetail.getSelectionModel().getSelectedItem();

			final int newSelectedIdx = (selectedIdx == lstDetail.getItems().size() - 1) ? selectedIdx - 1
					: selectedIdx;

			lstDetail.getItems().remove(selectedIdx);
			lstDetail.getSelectionModel().select(newSelectedIdx);

		}
	}*/
	
	public void setval () throws SQLException {
		
		if (cmbMonth.getValue() == null)
			month = null;
		if (cmbMonth.getValue().equalsIgnoreCase("January"))
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
		
		if (cmbDetail.getValue() == null )
			type = null;
		else if (cmbDetail.getValue().equalsIgnoreCase("Rent/Lease")) 
			type = "0";
		else if (cmbDetail.getValue().equalsIgnoreCase("Association")) 
			type = "1";
		else if (cmbDetail.getValue().equalsIgnoreCase("Electricity"))
			type = "2";
		else if (cmbDetail.getValue().equalsIgnoreCase("Water")) 
			type = "3";
		else if (cmbDetail.getValue().equalsIgnoreCase("Repairs")) 
			type = "4";
		else if (cmbDetail.getValue().equalsIgnoreCase("Others")) 
			type = "5";
		
		occpID = dbcon.getIDs(" OCCUPANCY_DETAILS.* ", "OCCUPANCY_DETAILS "
				+ " LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ",
				"OCCP_ID", "OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND PRIMARY_TENANT_DETAILS.TENANT_NAME = '"+ cmbTenant.getValue() +"' "
				+ "AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" ");
		
		occpIDUnit = dbcon.getIDs(" OCCUPANCY_DETAILS.* ", "OCCUPANCY_DETAILS "
				+ " LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ",
				"OCCP_ID", "OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.UNIT_NAME = '"+ cmbUnit.getValue() +"' "
				+ "AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" ");
		
	}
	
	public void create (ActionEvent event) throws SQLException {
		
		if (cmbMonth.getValue() != null && cmbDetail.getValue() != null && !txtAmount.getText().isEmpty() ) {
			
			setval();
			dueDate =  LocalDate.now().plusDays(Integer.parseInt(Building_Detail.date));
			
			modular.createWithFinalize("BILLING_STATEMENT_DETAILS", "STATEMENT_ID = 0 ", lblDate,  
					" 0, '"+ stateno +"', "+ occpIDUnit +", " +type+ ", now(), "+ month+" , '"+ dueDate +"', "
					+ " "+ txtAmount.getText().replaceAll(",", "") +", 0, null, now(), "+ Globals.G_Employee_ID +", "
							+ " 'N', null ,null, null  ",
					event);
			
			clear();
			
		}
		else {
			
			if (cmbDetail.getValue() == null) lblDetails.setVisible(true);
			else lblDetails.setVisible(false);
			
			if (cmbMonth.getValue() == null) lblMonth.setVisible(true);
			else lblMonth.setVisible(false);
			
			if (txtAmount.getText().isEmpty()) lblAmount.setVisible(true);
			else lblAmount.setVisible(false);
			
			lblMessage.setVisible(true);
		}
		
		
	}

	public void cancel() throws SQLException {
			 
		modular.cancel("BILLING_STATEMENT_DETAILS", primaryStage);

	}
	
	public void clear () throws SQLException {
		
		cmbDetail.setValue(null);
		cmbMonth.setValue(null);
		//txtBalance.setText(null);
		txtAmount.setText(null);
		
		lblDetails.setVisible(false);
		lblMonth.setVisible(false);
		lblMessage.setVisible(false);
		btnFinalize.setDisable(false);
		
		btnGenerateBilling.setDisable(false);
		
		dbcon.tblBillingFinal(tblFinalize);

		if (balance > 0) {
			
			prevMonth = "BALANCE FROM PREVIOUS MONTH(S)";
			
			ObservableList<TableBillingFinalize> data1 = FXCollections.observableArrayList();
			
			data1.addAll(new TableBillingFinalize(
					 "0",
					prevMonth,
					"", 
					Double.toString(balance) ));
			
			tblFinalize.getItems().addAll(data1);
			
		}
		
	}
	
	public void generate (ActionEvent event) throws SQLException {
		
		tenantDetails();
		dueDate =  LocalDate.now().plusDays(Integer.parseInt(Building_Detail.date));
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String due = dueDate.format(formatters);
		
		lblDate.setText(datenow);
		lblStateNo.setText(Integer.toString(stateno));
		lblDueDate.setText(due);
		txtBilledTo.setText(tname + " - " + taddress );
		
		tblFinalize.setItems(null);

		totalAmount();
		
		total = Double.parseDouble(amount) + balance;
		
		txtTotalAmount.setText(total.toString());

		dbcon.tblBillingVoucher(tblBillingStatement, "REFERENCE_ID IS NULL AND SYSTEM_ACCOUNT_ID = "+ Globals.G_Employee_ID +" "
				+ "AND FINALIZED_RECORD = 'N' ");
		
		if (balance > 0) {
			
			prevMonth = "BALANCE FROM PREVIOUS MONTH(S)";
			
			ObservableList<TableBilling> data = FXCollections.observableArrayList();
			
			data.addAll(new TableBilling(
					 "0",
					prevMonth,
					Double.toString(balance), 
					""));
			
			tblBillingStatement.getItems().addAll(data);
		}
		
		Double num = Double.parseDouble(txtTotalAmount.getText().replaceAll(",", ""));

        int peso = (int) Math.floor(num);
        double cents = num - peso;
        int centsAsInt = (int) (100 * cents);
        
        if (centsAsInt > 0) {
            
        	totalToWords = NTW.convert(peso) + " Pesos And " + NTW.convert(centsAsInt) + " Centavos Only";
            
        }
        else {
            
        	totalToWords = NTW.convert(peso) + " Pesos Only";
            
        }
		
	}
	
	public void finalize (ActionEvent event) throws SQLException {
		
		modular.finalize("BILLING_STATEMENT_DETAILS");
		((Node) event.getSource()).getScene().getWindow().hide();
		
	}
	
	public void tenantDetails () throws SQLException {
		
		String query = "SELECT PRIMARY_TENANT_DETAILS.*, OCCUPANCY_DETAILS.*, "
				+ "UNIT_DETAILS.UNIT_NAME AS UNIT, UNIT_DETAILS.UNIT_ASSOC_AMOUNT AS ASSOC "
				+ " FROM OCCUPANCY_DETAILS "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE OCCUPANCY_DETAILS.OCCP_ID = "+ occpIDUnit +" AND OCCUPANCY_DETAILS.REFERENCE_ID IS NULL ";
		
		try{ 
			ps1 = dbcon.connect.prepareStatement(query);
			rs1 = ps1.executeQuery();
				
			while (rs1.next()){	
					
				tname = rs1.getString("TENANT_NAME");
				taddress = rs1.getString("TENANT_ADDRESS");
				tphone = rs1.getString("TENANT_MOBILE_NO");
				tlandline = rs1.getString("TENANT_LANDLINE_NO");
				temailadd = rs1.getString("TENANT_EMAIL_ADD");
				
				if (tphone.equals("0")) tphone = null;
				if (tlandline.equals("0")) tlandline = null;
				
				cmbUnit.setValue(rs1.getString("UNIT"));
				cmbTenant.setValue(rs1.getString("TENANT_NAME"));
				txtBillingCycle.setText(rs1.getString("OCCP_BILLING_CYCLE"));
				txtRentAmount.setText(rs1.getString("OCCP_RENT_AMOUNT"));
				txtAssocFee.setText(rs1.getString("ASSOC"));
				
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of tenant related to the occupancy: " +e.getMessage());
			}
			finally {
				ps1.close();
				rs1.close();
			}
		
	}
	
	public void totalAmount ( ) throws SQLException {
		
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		
		String query = "SELECT SUM(STATEMENT_AMOUNT) AS TOTAL FROM BILLING_STATEMENT_DETAILS WHERE "
				+ "REFERENCE_ID IS NULL AND SYSTEM_ACCOUNT_ID = "+ Globals.G_Employee_ID +" "
						+ "AND FINALIZED_RECORD = 'N' " ;
		
		try{
			ps1 = dbcon.connect.prepareStatement(query);
			rs1 = ps1.executeQuery();
			while (rs1.next()){
				
				amount = rs1.getString("TOTAL");
				
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err total sum of billing statement : " +e.getMessage());
		}
		finally {
			ps1.close();
			rs1.close();
		}
	}
	
	public void occupancyDetails (String id) throws SQLException {
		
		String query = "SELECT PRIMARY_TENANT_DETAILS.*, OCCUPANCY_DETAILS.*, "
				+ "UNIT_DETAILS.UNIT_NAME AS UNIT, UNIT_DETAILS.UNIT_ASSOC_AMOUNT AS ASSOC "
				+ " FROM OCCUPANCY_DETAILS "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE OCCUPANCY_DETAILS.OCCP_ID = "+ id +" AND OCCUPANCY_DETAILS.REFERENCE_ID IS NULL ";
		
		try{ 
			ps1 = dbcon.connect.prepareStatement(query);
			rs1 = ps1.executeQuery();
				
			while (rs1.next()){	
					
				tname = rs1.getString("TENANT_NAME");
				taddress = rs1.getString("TENANT_ADDRESS");
				tphone = rs1.getString("TENANT_MOBILE_NO");
				tlandline = rs1.getString("TENANT_LANDLINE_NO");
				temailadd = rs1.getString("TENANT_EMAIL_ADD");
				
				cmbUnit.setValue(rs1.getString("UNIT"));
				cmbTenant.setValue(rs1.getString("TENANT_NAME"));
				txtBillingCycle.setText(rs1.getString("OCCP_BILLING_CYCLE"));
				txtRentAmount.setText(rs1.getString("OCCP_RENT_AMOUNT"));
				txtAssocFee.setText(rs1.getString("ASSOC"));
				
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of tenant related from occupancy : " +e.getMessage());
			}
			finally {
				ps1.close();
				rs1.close();
			}
		
	}
	
 	public void print (ActionEvent event) throws SQLException {
		
		tenantDetails();
		
        int loop, size;

        size = tblBillingStatement.getItems().size();

        String[][] tableString = new String[size][3];

        for (loop = 0; loop < size; loop++) {
            TableBilling selectedRecord = (TableBilling) tblBillingStatement.getItems().get(loop);

            tableString[loop][0] = selectedRecord.getStateDetail();
            tableString[loop][1] = selectedRecord.getStateBal();
            tableString[loop][2] = selectedRecord.getStateAmount();

        }
        
        String preparedby =  Globals.G_Employee_Name ;
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String due = dueDate.format(formatters);
		
		String amt = total.toString();
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        double totalAmt = Double.parseDouble(amt);
        

        try {
            new GenerateReport().billingStatement(datenow, Integer.toString(stateno) , Building_Detail.bldgName,
            		Building_Detail.bldgAddress, Building_Detail.bldgContact, 
                    Building_Detail.logoFileName, formatter.format(totalAmt), due, tname, taddress, 
                    tlandline, tphone, temailadd, tableString);
            
        } catch (ClassNotFoundException | JRException | SQLException e1) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing statement detail : " + e1.toString() + " -- " + e1.getMessage());
        }
        
		
	}

	public void processAddOption(Stage parent_primaryStage) throws SQLException {

		occpIDUnit = Occupancy.occpID;
		tenantDetails();
		
		totalBill = dbcon.totalBilling(occpIDUnit);
		totalPayment = dbcon.totalPayment(occpIDUnit);
		
		if (totalBill == null) totalBill = "0";
		if (totalPayment == null) totalPayment = "0";
				
		balance = Double.parseDouble(totalBill) - Double.parseDouble(totalPayment);
		txtBalance.setText(Double.toString(balance));
		
		if (balance > 0) {
			
			prevMonth = "BALANCE FROM PREVIOUS MONTH(S)";
			
			ObservableList<TableBillingFinalize> data1 = FXCollections.observableArrayList();
			
			data1.addAll(new TableBillingFinalize(
					 "0",
					prevMonth,
					"", 
					Double.toString(balance)));
			
			tblFinalize.setItems(data1);
			
			ObservableList<TableBilling> data = FXCollections.observableArrayList();
			
			data.addAll(new TableBilling(
					 "0",
					prevMonth,
					Double.toString(balance), 
					""));
			
			tblBillingStatement.setItems(data);
		}
		
		cmbUnit.setDisable(true);
		cmbTenant.setDisable(true);
		txtBillingCycle.setDisable(true);
		txtRentAmount.setDisable(true);
		txtAssocFee.setDisable(true);
		
		primaryStage = parent_primaryStage;
		
		primaryStage.setOnCloseRequest(e -> {
				 
		try {
			modular.cancelreq(" BILLING_STATEMENT_DETAILS ", primaryStage, e);
		} catch (SQLException e2) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing statement details : " + e2.getMessage());
		}
		});

	}
	
	public void processCreateBilling(Stage parent_primaryStage) throws SQLException {

		//dbcon.cmbDisplay(cmbUnit, "*", "UNIT_NAME", "UNIT_DETAILS", "REFERENCE_ID IS NULL AND BLDG_ID = "+ Main_Window.dashboard +" " );
		
		dbcon.cmbDisplay(cmbUnit, " UNIT_DETAILS.* ", "UNIT_NAME", 
				"OCCUPANCY_DETAILS "
				+ " LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ",
				"OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
				+ " AND OCCUPANCY_DETAILS.OCCP_STATUS <> 3 " );
		
		dbcon.cmbDisplay(cmbTenant, " PRIMARY_TENANT_DETAILS.* ", "TENANT_NAME", 
				"OCCUPANCY_DETAILS "
				+ " LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ",
				"OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
						+ " OCCUPANCY_DETAILS.OCCP_STATUS <> 3 " );
		
		cmbTenant.setOnAction( e-> {
			
			try {
				occpID = dbcon.getIDs(" OCCUPANCY_DETAILS.* ", "OCCUPANCY_DETAILS "
						+ " LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
						+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ",
						"OCCP_ID", "OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND PRIMARY_TENANT_DETAILS.TENANT_NAME = '"+ cmbTenant.getValue() +"' "
						+ "AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND OCCUPANCY_DETAILS.OCCP_STATUS <> 3 ");
				
			} catch (SQLException e1) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err get occupancy id : " +e1.getMessage());
			}

			try {
				occupancyDetails(occpID);
				totalBill = dbcon.totalBilling(occpIDUnit);
				totalPayment = dbcon.totalPayment(occpIDUnit);
				
			} catch (SQLException e1) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display details from selected tenant : " +e1.getMessage());
			}

			if (totalBill == null) totalBill = "0";
			if (totalPayment == null) totalPayment = "0";
					
			balance = Double.parseDouble(totalBill) - Double.parseDouble(totalPayment);
			txtBalance.setText(Double.toString(balance));
			
			if (balance > 0) {
				
				prevMonth = "BALANCE FROM PREVIOUS MONTH(S)";
				
				ObservableList<TableBillingFinalize> data1 = FXCollections.observableArrayList();
				
				data1.addAll(new TableBillingFinalize(
						 "0",
						prevMonth,
						"", 
						Double.toString(balance)));
				
				tblFinalize.setItems(data1);
				
				ObservableList<TableBilling> data = FXCollections.observableArrayList();
				
				data.addAll(new TableBilling(
						 "0",
						prevMonth,
						Double.toString(balance), 
						""));
				
				tblBillingStatement.setItems(data);
			}
		});
		
		cmbUnit.setOnAction( e-> {

			try {
				occpIDUnit = dbcon.getIDs(" OCCUPANCY_DETAILS.* ", "OCCUPANCY_DETAILS "
						+ " LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
						+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID ",
						"OCCP_ID", "OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.UNIT_NAME = '"+ cmbUnit.getValue() +"' "
						+ "AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
								+ "OCCUPANCY_DETAILS.OCCP_STATUS <> 3 ");
				
			} catch (SQLException e1) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err get occupancy id : " +e1.getMessage());
			}
			
			try {
				occupancyDetails(occpIDUnit);
				totalBill = dbcon.totalBilling(occpIDUnit);
				totalPayment = dbcon.totalPayment(occpIDUnit);
				
			} catch (SQLException e1) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display details from selected unit : " +e1.getMessage());
			}
			
			if (totalBill == null) totalBill = "0";
			if (totalPayment == null) totalPayment = "0";
					
			balance = Double.parseDouble(totalBill) - Double.parseDouble(totalPayment);
			txtBalance.setText(Double.toString(balance));
			
			if (balance > 0) {
				
				prevMonth = "BALANCE FROM PREVIOUS MONTH(S)";
				
				ObservableList<TableBillingFinalize> data1 = FXCollections.observableArrayList();
				
				data1.addAll(new TableBillingFinalize(
						 "0",
						prevMonth,
						"", 
						Double.toString(balance)));
				
				tblFinalize.setItems(data1);
				
				ObservableList<TableBilling> data = FXCollections.observableArrayList();
				
				data.addAll(new TableBilling(
						 "0",
						prevMonth,
						Double.toString(balance), 
						""));
				
				tblBillingStatement.setItems(data);
			}
			
		});
		
		
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
		try {
			modular.cancelreq(" BILLING_STATEMENT_DETAILS ", primaryStage, e);
		} catch (SQLException e2) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing statement details : " + e2.getMessage());
		}
		});

	}

}
