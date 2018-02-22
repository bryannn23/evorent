package application;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Create_Contractor implements Initializable {

	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();

	@FXML private TextField txtName;
	@FXML private TextArea txtAddress;
	@FXML private TextArea txtContactPerson;
	@FXML private TextField txtContactNumber;
	@FXML private TextArea txtRemarks;
	
	@FXML private Label lblName;
	@FXML private Label lblAddress;
	@FXML private Label lblContactPerson;
	@FXML private Label lblContactNumber;
	@FXML private Label lblRemarks;
	@FXML private Label lblMessage;
	
	@FXML private TableView<TableContractor> tblFinalize;
	
	@FXML private Button btnCreate;
	@FXML Button btnCancel;
	@FXML private Button btnUpdate;
	@FXML private Button btnUpdateM;
	@FXML private Button btnFinalize;
	
	@FXML private VBox vb;
	Stage primaryStage;
	
	String contr ;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		btnCreate.setVisible(false);
		btnUpdate.setVisible(false);
		btnUpdateM.setVisible(false);
		btnFinalize.setDisable(true);
		
		lblName.setVisible(false);
		lblAddress.setVisible(false);
		lblContactPerson.setVisible(false);
		lblContactNumber.setVisible(false);
		lblRemarks.setVisible(false);
		lblMessage.setVisible(false);
		
		TextFormatter<String> tfname = modular.getTextFlexiFormatter(100, 'a'); 
		txtName.setTextFormatter(tfname);
		TextFormatter<String> tfdesc = modular.getTextFlexiFormatter(500, 'a');
		txtAddress.setTextFormatter(tfdesc);
		TextFormatter<String> tfarea = modular.getTextFlexiFormatter(100, 'a');
		txtContactPerson.setTextFormatter(tfarea);
		TextFormatter<String> tfprice = modular.getTextFlexiFormatter(12, 'n'); 
		txtContactNumber.setTextFormatter(tfprice);
		TextFormatter<String> tfmax = modular.getTextFlexiFormatter(500, 'a'); 
		txtRemarks.setTextFormatter(tfmax);
		
	
		setUpContractor_TableView();
	}
	
	public void setUpContractor_TableView() {

		tblCol.TableContractor(tblFinalize);

		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableContractor selected = tblFinalize.getSelectionModel().getSelectedItem();
				contr = selected.getContrID();
				contrView(contr);
				
			} catch (Exception ex) {
			}
			if (contr != null) {
				btnCreate.setVisible(false);
				btnUpdate.setVisible(true);
				
			} else
				return;
		});
	}
	
	public void create(ActionEvent event) throws SQLException {

		lblMessage.setText("");
			
		if (!txtName.getText().isEmpty() )
		{
			
			modular.createWithFinalize(" CONTRACTOR_DETAILS ", " CONTR_NAME = '"+ txtName.getText() +"' ", 
					lblMessage, 
					" 0, '"+ txtName.getText() +"', '"+ txtAddress.getText() +"',"
							+ " '"+ txtContactPerson.getText() +"', "
					+ " '"+ txtContactNumber.getText().replaceAll(",", "")+"', '"+ txtRemarks.getText() +"', "
							+ "now(),"
					+ " "+Globals.G_Employee_ID+", 'N', null, null, null " , 
					event );
			
			clear();
				
		} else {
			check();
					 
		}
	}

	public void check() {

		if (txtName.getText().isEmpty())
			lblName.setVisible(true);
		else
			lblName.setVisible(false);
			
			lblMessage.setVisible(true);
			lblMessage.setText("Field required!");
			
		}

	public void clear() throws SQLException {
			
		if (lblMessage.getText().equals("Record already exist.")) {
				
			lblMessage.setVisible(true);
			lblMessage.setText("Record already exist.");
				
		} else {
				
			txtName.clear();
			txtAddress.clear();
			txtContactPerson.clear();
			txtContactNumber.clear();
			txtRemarks.clear();
				
			dbcon.tblContractor(tblFinalize, " REFERENCE_ID IS NULL AND FINALIZED_RECORD = 'N' ");
			btnFinalize.setDisable(false);
			contr = null;
				
			lblName.setVisible(false);
			lblAddress.setVisible(false);
			lblContactPerson.setVisible(false);
			lblContactNumber.setVisible(false);
			lblRemarks.setVisible(false);
			lblMessage.setVisible(false);
				
		}
	}
		
	public void cancel() throws SQLException {
			 
		modular.cancel("CONTRACTOR_DETAILS", primaryStage);

	}
	
	public void finalize(ActionEvent event) throws SQLException {
			  
		modular.finalize("CONTRACTOR_DETAILS");
		((Node) event.getSource()).getScene().getWindow().hide();

	}
		
	public void contrView(String id) throws SQLException {
			
		String query = "SELECT * FROM CONTRACTOR_DETAILS WHERE CONTR_ID = "+id+" ";
			
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
					
				txtName.setText(rs.getString("CONTR_NAME"));
				
				String add = rs.getString("CONTR_ADDRESS");
				if (add == null) add = "";
				txtAddress.setText(add);
				
				String contper = rs.getString("CONTR_CONTACTPERSON");
				if (contper == null) contper = "";
				txtContactPerson.setText(contper);
				
				String num = rs.getString("CONTR_CONTACT_NO");
				if (num == null) num = "";
				txtContactNumber.setText(num);
				txtRemarks.setText(rs.getString("CONTR_REMARKS"));
					
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected contractor "+id+" : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
		}

	public void upd() throws SQLException {

		modular.updaterecord(" CONTRACTOR_DETAILS ", " CONTR_NAME, CONTR_ADDRESS, CONTR_CONTACTPERSON, "
				+ "CONTR_CONTACT_NO, CONTR_REMARKS, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
			"  CONTR_NAME, CONTR_ADDRESS, CONTR_CONTACTPERSON, CONTR_CONTACT_NO, CONTR_REMARKS, "
			+ " DATETIME_ENTRY, SYSTEM_ACCOUNT_ID,  "+ Building_Detail.contr +" ",
			" CONTR_ID = "+Building_Detail.contr+" ");
			
		}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtName.getText().isEmpty() )
		{
				
			modular.editrecordwithfinalize(" CONTRACTOR_DETAILS ", " CONTR_NAME = '"+ txtName.getText() +"' "
					+ " AND CONTR_ID <> "+ contr +" " ,
					lblMessage,
					" CONTR_NAME = '"+ txtName.getText() +"', CONTR_ADDRESS = '"+txtAddress.getText() +"', "
					+ "CONTR_CONTACTPERSON = '"+ txtContactPerson.getText() +"', "
					+ "CONTR_CONTACT_NO = '"+ txtContactNumber.getText().replaceAll(",", "") +"', "
					+ "CONTR_REMARKS = '"+ txtRemarks.getText() +"', "
					+ " DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" CONTR_ID = "+ contr +" ",
				   event, btnUpdate, btnCreate);
				
				clear();
							
		} else {
			check();
					 
		}

	}

	public void updateM(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtName.getText().isEmpty() )
		{
					
			upd();
				
			modular.editrecord(" CONTRACTOR_DETAILS ", " CONTR_NAME = '"+ txtName.getText() +"' "
					+ " AND CONTR_ID <> "+ Building_Detail.contr +" AND REFERENCE_ID IS NULL " ,
					lblMessage,
					" CONTR_NAME = '"+ txtName.getText() +"',  "
					+ "CONTR_ADDRESS = '"+txtAddress.getText()+"', "
					+ "CONTR_CONTACTPERSON = '"+ txtContactPerson.getText()+"', "
					+ "CONTR_CONTACT_NO = '"+ txtContactNumber.getText().replaceAll(",", "")+"', "
					+ "CONTR_REMARKS = '"+ txtRemarks.getText() +"', "
					+ " DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" CONTR_ID = "+ Building_Detail.contr +" ",
				   event );
							
		} else {
			check();
					 
		}

	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
		try {
			modular.cancelreq(" CONTRACTOR_DETAILS ", primaryStage, e);
		} catch (SQLException e2) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err contr : " + e2.getMessage());
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
			contrView(Building_Detail.contr);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err contr EDIT : " + e.getMessage());
		}
		
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
			try {
				modular.cancelreq("CONTRACTOR_DETAILS ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err contr EDIT CANCEL : " + e2.getMessage());
			}
				
		});
			
	}

	public void sizeview() {
			
		txtName.setPrefWidth(410);
		txtAddress.setPrefWidth(410);
		txtContactPerson.setPrefWidth(410);
		txtContactNumber.setPrefWidth(410);
		txtRemarks.setPrefWidth(410);

		lblMessage.setLayoutX(535);
		lblAddress.setLayoutX(535);
		lblContactPerson.setLayoutX(535);
		lblContactNumber.setLayoutX(535);
		lblRemarks.setLayoutX(535);
		lblName.setLayoutX(535);

	}

	public void processViewOption(String uID) {
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		btnCancel.setText("CLOSE");
		
		txtName.setDisable(true);
		txtAddress.setDisable(true);
		txtContactPerson.setDisable(true);
		txtContactNumber.setDisable(true);
		txtRemarks.setDisable(true);

		try {
			contrView(Building_Detail.contr);

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err contr record view : " + e.getMessage());
		}
			 
	}

}
