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

public class Create_Expense implements Initializable {

	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;
	PreparedStatement ps1 = null;
	ResultSet rs1 = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();

	@FXML private ComboBox<String> cmbContractor;
	@FXML private ComboBox<String> cmbCOA;
	
	@FXML private DatePicker dpTxnDate;
	
	@FXML private TextArea txtDesc;
	
	@FXML private TextField txtReference;
	@FXML private TextField txtAmount;
	
	@FXML private Label lblReference;
	@FXML private Label lblContractor;
	@FXML private Label lblTxnDate;
	@FXML private Label lblCOA;
	@FXML private Label lblDesc;
	@FXML private Label lblAmount;
	@FXML private Label lblMessage;
	
	@FXML private TableView<TableExpense> tblFinalize;
	
	@FXML private Button btnCreate;
	@FXML Button btnCancel;
	@FXML private Button btnUpdate;
	@FXML private Button btnUpdateM;
	@FXML private Button btnFinalize;
	
	@FXML private VBox vb;
	
	String contractor, coa, date, expense;
	Stage primaryStage;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		btnUpdate.setVisible(false);
		btnUpdateM.setVisible(false);
		btnCreate.setVisible(false);
		btnFinalize.setDisable(true);

		lblReference.setVisible(false);
		lblContractor.setVisible(false);
		lblTxnDate.setVisible(false);
		lblCOA.setVisible(false);
		lblDesc.setVisible(false);
		lblAmount.setVisible(false);
		lblMessage.setVisible(false);

			TextFormatter<String> tfissue = modular.getTextFlexiFormatter(20, 'n'); 
			txtReference.setTextFormatter(tfissue);
			TextFormatter<String> tfdesc = modular.getTextFlexiFormatter(500, 'a');
			txtDesc.setTextFormatter(tfdesc);
			TextFormatter<String> tfrem = modular.getTextFlexiFormatter(13, 'n');
			txtAmount.setTextFormatter(tfrem);
		
		comboBox.expCOA(cmbCOA);
			
		try {
			
			dbcon.cmbDisplay(cmbContractor, "*", "CONTR_NAME", "CONTRACTOR_DETAILS", "REFERENCE_ID IS NULL" );
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display contractor: " + e.getMessage());
		}
		
		new AutoCompleteComboBoxListener<String>(cmbCOA);
		new AutoCompleteComboBoxListener<String>(cmbContractor);
		
		setupExpense_TableView();
		
}

	public void setupExpense_TableView() {

		tblCol.TableExpense(tblFinalize);

		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableExpense selected = tblFinalize.getSelectionModel().getSelectedItem();
				expense = selected.getExpID();
				expenseView(expense);
				
			} catch (Exception ex) {
			}
			if (expense != null) {
				btnCreate.setVisible(false);
				btnUpdate.setVisible(true);
			} else
				return;
		});
	}
	
	public void create(ActionEvent event) throws SQLException {

		lblMessage.setText("");
			
		if ( cmbContractor.getValue() != null && cmbCOA.getValue() != null )
		{
			
			setval();
			
			if (contractor == null) {
				dbcon.createContractor("CONTRACTOR_DETAILS", " 0, '"+ cmbContractor.getValue()+"', null, null, null, null, now(), "+ Globals.G_Employee_ID+", "
						+ "'N', null, null, null ");
				
				contractor = dbcon.getIDs("*", "CONTRACTOR_DETAILS", "CONTR_ID", " REFERENCE_ID IS NULL AND CONTR_NAME = '"+ cmbContractor.getValue() +"' ");
				
			}
				
			modular.createWithFinalize(" EXPENSE_RECORD ", " EXPENSE_ID IS NULL ", 
					lblMessage, 
					" 0, 'EXPENSE_RECORD', null, "+ txtReference.getText().replaceAll(",", "")+", "+ contractor +", "+ date+", "
					+ " "+ coa +", '"+ txtDesc.getText()+"', "+ txtAmount.getText().replaceAll(",", "")+", "
					+ " "+ Main_Window.dashboard +", now(), "+Globals.G_Employee_ID+", 'N', null, null, null " , 
					event );
			
			clear();
				
		} else {
			check();
					 
		}
	}

	public void check() {

		if (cmbCOA.getValue() == null)
			lblCOA.setVisible(true);
		else
			lblCOA.setVisible(false);
			
		if (cmbContractor.getValue() == null )
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
				
			txtReference.clear();
			cmbContractor.setValue(null);
			dpTxnDate.setValue(null);
			cmbCOA.setValue(null);
			txtDesc.clear();
			txtAmount.clear();
				
			dbcon.tblExpense(tblFinalize, " EXPENSE_RECORD.REFERENCE_ID IS NULL AND EXPENSE_RECORD.FINALIZED_RECORD = 'N' " );
			btnFinalize.setDisable(false);
			expense = null;
				
			lblReference.setVisible(false);
			lblContractor.setVisible(false);
			lblTxnDate.setVisible(false);
			lblCOA.setVisible(false);
			lblDesc.setVisible(false);
			lblAmount.setVisible(false);
			lblMessage.setVisible(false);
				
		}
	}
		
	public void cancel() throws SQLException {
			 
		//modular.cancel("EXPENSE_RECORD", primaryStage);
		
		String query = "DELETE FROM EXPENSE_RECORD WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
				+ Globals.G_Employee_ID + " ";
		
		String contractor = "DELETE FROM CONTRACTOR_DETAILS WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
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
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err expense record cancel : " + e.getMessage());
			} finally {
				ps.close();
			}

			try {
				
				ps1 = dbcon.connect.prepareStatement(contractor);
				ps1.executeUpdate();
				
			}
			catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err contractor cancel from expense record: " + e.getMessage());
			} finally {
				ps1.close();
			}
			
			primaryStage.close();
			
		} else {
			
			return;

		}

	}
		
	public void setval () throws SQLException {
			
		if (txtAmount.getText().isEmpty()) txtAmount.setText("0");
		
		contractor = dbcon.getIDs("*", "CONTRACTOR_DETAILS", "CONTR_ID", "REFERENCE_ID IS NULL AND "
				+ "CONTR_NAME = '"+ cmbContractor.getValue()+"' ");	
		
		if (dpTxnDate.getValue() == null)
			date = null;
		else {
			date = dpTxnDate.getValue().toString();
			date = "'" + date + "'";
		}
		
		if (cmbCOA.getValue() == null)
			coa = null;
		else if (cmbCOA.getValue().equalsIgnoreCase("Other Cost"))
			coa = "0";
		else if (cmbCOA.getValue().equalsIgnoreCase("Management Fees"))
			coa = "1";
		else if (cmbCOA.getValue().equalsIgnoreCase("Agent Rent Collection Fees"))
			coa = "2";
		else if (cmbCOA.getValue().equalsIgnoreCase("Insurance"))
			coa = "3";
		else if (cmbCOA.getValue().equalsIgnoreCase("Cost of Advertising"))
			coa = "4";
		else if (cmbCOA.getValue().equalsIgnoreCase("Cost of Utilities"))
			coa = "5";
		else if (cmbCOA.getValue().equalsIgnoreCase("Provision for Depreciation")) 
			coa = "6";
		else if (cmbCOA.getValue().equalsIgnoreCase("Cost of Maintenance")) 
			coa = "7";
		else if (cmbCOA.getValue().equalsIgnoreCase("Cost of Repairs and Replacement")) 
			coa = "8";
		
	}

	public void finalize(ActionEvent event) throws SQLException {
			  
		modular.finalize("EXPENSE_RECORD");

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
		
		((Node) event.getSource()).getScene().getWindow().hide();

	}
		
	public void expenseView(String id) throws SQLException {
			
		String query = "SELECT EXPENSE_RECORD.*, CONTRACTOR_DETAILS.CONTR_NAME AS CONTR "
				+ " FROM EXPENSE_RECORD "
				+ "LEFT JOIN CONTRACTOR_DETAILS ON CONTRACTOR_DETAILS.CONTR_ID = EXPENSE_RECORD.CONTRACTOR_ID "
				+ " WHERE EXPENSE_RECORD.EXPENSE_ID = "+id+" ";
			
		String stat;
		
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
					
				cmbContractor.setValue(rs.getString("CONTR"));
				
				 Date date = rs.getDate("EXPENSE_TXN_DATE"); 
	                
				 if (date == null) {
					 
					 dpTxnDate.getEditor().setText(rs.getString("EXPENSE_TXN_DATE")); 
				 }
	             else dpTxnDate.setValue(date.toLocalDate());
				 
				txtReference.setText(rs.getString("EXPENSE_REF"));
				txtDesc.setText(rs.getString("EXPENSE_DESCRIPTION"));
				txtAmount.setText(rs.getString("EXPENSE_AMOUNT"));
				
				stat = rs.getString("EXPENSE_COA");
				
				if (stat == null) cmbCOA.setValue(null);
				else if (stat.equals("0")) cmbCOA.setValue("Other Cost");
				else if (stat.equals("1")) cmbCOA.setValue("Management Fees");
				else if (stat.equals("2")) cmbCOA.setValue("Agent Rent Collection Fees");
				else if (stat.equals("3")) cmbCOA.setValue("Insurance");
				else if (stat.equals("4")) cmbCOA.setValue("Cost of Advertising");
				else if (stat.equals("5")) cmbCOA.setValue("Cost of Utilities");
				else if (stat.equals("6")) cmbCOA.setValue("Provision for Depreciation");
				else if (stat.equals("7")) cmbCOA.setValue("Cost of Maintenance");
				else if (stat.equals("8")) cmbCOA.setValue("Cost of Repairs and Replacement");
					
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected expense "+id+" : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
		}

	public void upd() throws SQLException {

		modular.updaterecord(" EXPENSE_RECORD ", " EXPENSE_REF, TABLE_NAME, TABLE_ID, CONTRACTOR_ID, EXPENSE_TXN_DATE, "
			+ "EXPENSE_COA, EXPENSE_DESCRIPTION, EXPENSE_AMOUNT, BLDG_ID, "
			+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
			"  EXPENSE_REF, TABLE_NAME, TABLE_ID, CONTRACTOR_ID, EXPENSE_TXN_DATE, EXPENSE_COA, EXPENSE_DESCRIPTION,"
			+ " EXPENSE_AMOUNT, BLDG_ID, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "
			+ ""+ Building_Detail.expense +" ",
			" EXPENSE_ID = "+ Building_Detail.expense +" ");
			
		}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbContractor.getValue() != null && cmbCOA.getValue() != null )
		{
			setval();
			
			if (contractor == null) {
				dbcon.createContractor("CONTRACTOR_DETAILS", " 0, '"+ cmbContractor.getValue()+"', null, null, null, null, now(), "+ Globals.G_Employee_ID+", "
						+ "'N', null, null, null ");
				
				contractor = dbcon.getIDs("*", "CONTRACTOR_DETAILS", "CONTR_ID", " REFERENCE_ID IS NULL AND CONTR_NAME = '"+ cmbContractor.getValue() +"' ");
				
			}
				
			modular.editrecordwithfinalize(" EXPENSE_RECORD ", " EXPENSE_ID = 0  " ,
					lblMessage,
					" EXPENSE_REF = "+ txtReference.getText().replaceAll(",", "") +", "
					+ " CONTRACTOR_ID = "+ contractor +", EXPENSE_TXN_DATE = "+ date +", "
					+ " EXPENSE_COA = "+ coa +", EXPENSE_DESCRIPTION = '"+ txtDesc.getText() +"' ,"
					+ "EXPENSE_AMOUNT = "+ txtAmount.getText().replaceAll(",", "") +", "
					+ "BLDG_ID = "+ Main_Window.dashboard +", DATETIME_ENTRY = NOW(), "
					+ " SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" EXPENSE_ID = "+ expense +" ",
				   event, btnUpdate, btnCreate);
				
				clear();
							
		} else {
			check();
					 
		}

	}

	public void updateM(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if ( cmbContractor.getValue() != null && cmbCOA.getValue() != null )
		{
						
			upd();
			setval();
			
			if (contractor == null) {
				dbcon.createContractor("CONTRACTOR_DETAILS", " 0, '"+ cmbContractor.getValue()+"', null, null, null, null, now(), "+ Globals.G_Employee_ID+", "
						+ "'Y', null, null, null ");
				
				contractor = dbcon.getIDs("*", "CONTRACTOR_DETAILS", "CONTR_ID", " REFERENCE_ID IS NULL AND CONTR_NAME = '"+ cmbContractor.getValue() +"' ");
				
			}
				
			modular.editrecord(" EXPENSE_RECORD ", " EXPENSE_ID = 0 " , 
					lblMessage,
					" EXPENSE_REF = "+ txtReference.getText().replaceAll(",", "") +", "
					+ " CONTRACTOR_ID = "+ contractor +", EXPENSE_TXN_DATE = "+ date +",  "
					+ " EXPENSE_COA = "+ coa +", EXPENSE_DESCRIPTION = '"+ txtDesc.getText() +"' ,"
					+ "EXPENSE_AMOUNT = "+ txtAmount.getText().replaceAll(",", "") +", "
					+ "BLDG_ID = "+ Main_Window.dashboard +", DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" EXPENSE_ID = "+ Building_Detail.expense +" ",
				   event);
							
		} else {
			check();
					 
		}

	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
		/*try {
			modular.cancelreq(" EXPENSE_RECORD ", primaryStage, e);
		} catch (SQLException e2) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err create expense : " + e2.getMessage());
		}*/
			
			String query = "DELETE FROM EXPENSE_RECORD WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
					+ Globals.G_Employee_ID + " ";
			
			String query2 = "DELETE FROM CONTRACTOR_DETAILS WHERE FINALIZED_RECORD = 'N' AND SYSTEM_ACCOUNT_ID = "
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
					EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err expense record add cancel : " + e1.getMessage());
				}
				
				try {
					
					ps1 = dbcon.connect.prepareStatement(query2);
					ps1.executeUpdate();

				} catch (Exception e1) {
					EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err contractor : " + e1.getMessage());
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
			expenseView(Building_Detail.expense);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err expense EDIT : " + e.getMessage());
		}
		
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
			try {
				modular.cancelreq("EXPENSE_RECORD ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err expense EDIT CANCEL : " + e2.getMessage());
			}
				
		});
			
	}

	public void sizeview() {
			
		txtReference.setPrefWidth(410);
		cmbContractor.setPrefWidth(410);
		dpTxnDate.setPrefWidth(410);
		cmbCOA.setPrefWidth(410);
		txtDesc.setPrefWidth(410);
		txtAmount.setPrefWidth(410);
		
		lblMessage.setLayoutX(535);
		lblReference.setLayoutX(535);
		lblContractor.setLayoutX(535);
		lblTxnDate.setLayoutX(535);
		lblDesc.setLayoutX(535);
		lblCOA.setLayoutX(535);
		lblAmount.setLayoutX(535);

		
	}

	public void processViewOption(String uID) {
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		btnCancel.setText("CLOSE");
			
		txtReference.setDisable(true);
		cmbContractor.setDisable(true);
		dpTxnDate.setDisable(true);
		cmbCOA.setDisable(true);
		txtDesc.setDisable(true);
		txtAmount.setDisable(true);
		
		try {
			expenseView(Building_Detail.expense);

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err expense record view : " + e.getMessage());
		}
			 
	}
		
}
