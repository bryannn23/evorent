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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Create_Unit_Type implements Initializable {

	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;

	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();

	@FXML private TextField txtName;
	@FXML private TextArea txtDesc;
	@FXML private TextArea txtArea;
	@FXML private TextField txtPrice;
	@FXML private TextField txtMaxCapacity;
	
	@FXML private Label lblName;
	@FXML private Label lblDesc;
	@FXML private Label lblArea;
	@FXML private Label lblPrice;
	@FXML private Label lblMaxCapacity;
	@FXML private Label lblMessage;
	
	@FXML private TableView<TableUnitType> tblFinalize;
	
	@FXML private Button btnCreate;
	@FXML Button btnCancel;
	@FXML private Button btnUpdate;
	@FXML private Button btnUpdateM;
	@FXML private Button btnFinalize;
	
	@FXML private VBox vb;
	Stage primaryStage;
	
	String type;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		btnCreate.setVisible(false);
		btnUpdate.setVisible(false);
		btnUpdateM.setVisible(false);
		btnFinalize.setDisable(true);
		
		lblName.setVisible(false);
		lblDesc.setVisible(false);
		lblArea.setVisible(false);
		lblPrice.setVisible(false);
		lblMaxCapacity.setVisible(false);
		lblMessage.setVisible(false);
		
		TextFormatter<String> tfname = modular.getTextFlexiFormatter(100, 'a'); 
		txtName.setTextFormatter(tfname);
		TextFormatter<String> tfdesc = modular.getTextFlexiFormatter(1000, 'a');
		txtDesc.setTextFormatter(tfdesc);
		TextFormatter<String> tfarea = modular.getTextFlexiFormatter(500, 'a');
		txtArea.setTextFormatter(tfarea);
		TextFormatter<String> tfprice = modular.getTextFlexiFormatter(12, 'n'); 
		txtPrice.setTextFormatter(tfprice);
		TextFormatter<String> tfmax = modular.getTextFlexiFormatter(3, 'n'); 
		txtMaxCapacity.setTextFormatter(tfmax);
		
		setupUnitType_TableView();
		
	}
	
	public void setupUnitType_TableView() {

		tblCol.TableUnitType(tblFinalize);

		tblFinalize.setOnMouseClicked(e -> {
			try {
				TableUnitType selected = tblFinalize.getSelectionModel().getSelectedItem();
				type = selected.getUtID();
				unitType(type);
				
			} catch (Exception ex) {
			}
			if (type != null) {
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
			
			setval();
				
			modular.createWithFinalize(" UNIT_TYPE ", " BLDG_ID = "+ Main_Window.dashboard +" AND UNIT_TYPE_NAME = '"+ txtName.getText() +"' ", 
					lblMessage, 
					" 0, '"+ txtName.getText() +"', "+ Main_Window.dashboard +", '"+ txtDesc.getText() +"', '"+ txtArea.getText()+"', "
					+ " "+ txtPrice.getText().replaceAll(",", "")+", "+ txtMaxCapacity.getText().replaceAll(",", "")+", now(),"
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
			txtDesc.clear();
			txtArea.clear();
			txtPrice.clear();
			txtMaxCapacity.clear();
				
			dbcon.tblUnitType(tblFinalize, " UNIT_TYPE.REFERENCE_ID IS NULL AND UNIT_TYPE.FINALIZED_RECORD = 'N' ");
			btnFinalize.setDisable(false);
			type = null;
				
			lblName.setVisible(false);
			lblDesc.setVisible(false);
			lblArea.setVisible(false);
			lblPrice.setVisible(false);
			lblMaxCapacity.setVisible(false);
			lblMessage.setVisible(false);
				
		}
	}
		
	public void cancel() throws SQLException {
			 
		modular.cancel("UNIT_TYPE", primaryStage);

	}
		
	public void setval () throws SQLException {
			
		if (txtPrice.getText().isEmpty()) txtPrice.setText("0");
		if (txtMaxCapacity.getText().isEmpty()) txtMaxCapacity.setText("0");
			
	}

	public void finalize(ActionEvent event) throws SQLException {
			  
		modular.finalize("UNIT_TYPE");
		((Node) event.getSource()).getScene().getWindow().hide();

	}
		
	public void unitType(String id) throws SQLException {
			
		String query = "SELECT UNIT_TYPE.*, BUILDING_DETAILS.BLDG_NAME AS BLDG "
				+ " FROM UNIT_TYPE "
				+ "LEFT JOIN BUILDING_DETAILS ON BUILDING_DETAILS.BLDG_ID = UNIT_TYPE.BLDG_ID "
				+ " WHERE UNIT_TYPE.UNIT_TYPE_ID = "+id+" ";
			
		try{ 
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
					
				txtName.setText(rs.getString("UNIT_TYPE_NAME"));
				txtDesc.setText(rs.getString("UNIT_TYPE_DESC"));
				txtArea.setText(rs.getString("UNIT_TYPE_AREA"));
				txtPrice.setText(rs.getString("UNIT_TYPE_PRICE"));
				txtMaxCapacity.setText(rs.getString("UNIT_TYPE_MAX_CAPACITY"));
					
				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err view details of selected unit type "+id+" : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
		}

	public void upd() throws SQLException {

		modular.updaterecord(" UNIT_TYPE ", " UNIT_TYPE_NAME, BLDG_ID, UNIT_TYPE_DESC, UNIT_TYPE_AREA, "
				+ "UNIT_TYPE_PRICE, UNIT_TYPE_MAX_CAPACITY, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
			" UNIT_TYPE_NAME, BLDG_ID, UNIT_TYPE_DESC, UNIT_TYPE_AREA, UNIT_TYPE_PRICE, UNIT_TYPE_MAX_CAPACITY, "
			+ " DATETIME_ENTRY, SYSTEM_ACCOUNT_ID,  "+ Building_Detail.type +" ",
			" UNIT_TYPE_ID = "+Building_Detail.type+" ");
			
		}

	public void update(ActionEvent event) throws SQLException {

		lblMessage.setText("");
		
		if (!txtName.getText().isEmpty()  )
		{
			setval();
				
			modular.editrecordwithfinalize(" UNIT_TYPE ", " BLDG_ID = "+ Main_Window.dashboard +" AND UNIT_TYPE_NAME = '"+ txtName.getText() +"' "
					+ " AND UNIT_TYPE_ID <> "+ type +" " ,
					lblMessage,
					" UNIT_TYPE_NAME = '"+ txtName.getText() +"',  "
					+ "UNIT_TYPE_DESC = '"+txtDesc.getText()+"', UNIT_TYPE_AREA = '"+ txtArea.getText()+"', "
					+ "UNIT_TYPE_PRICE = "+ txtPrice.getText().replaceAll(",", "")+", UNIT_TYPE_MAX_CAPACITY = "+ txtMaxCapacity.getText().replaceAll(",", "")+","
					+ " DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" UNIT_TYPE_ID = "+ type +" ",
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
			setval();
				
			modular.editrecord(" UNIT_TYPE ", " BLDG_ID = "+ Main_Window.dashboard +" AND UNIT_TYPE_NAME = '"+ txtName.getText() +"' "
					+ " AND UNIT_TYPE_ID <> "+ Building_Detail.type +" " ,
					lblMessage,
					" UNIT_TYPE_NAME = '"+ txtName.getText() +"',  "
					+ "UNIT_TYPE_DESC = '"+txtDesc.getText()+"', UNIT_TYPE_AREA = '"+ txtArea.getText()+"', "
					+ "UNIT_TYPE_PRICE = "+ txtPrice.getText().replaceAll(",", "")+", "
					+ "UNIT_TYPE_MAX_CAPACITY = "+ txtMaxCapacity.getText().replaceAll(",", "")+", "
					+ " DATETIME_ENTRY = NOW(), SYSTEM_ACCOUNT_ID = "+Globals.G_Employee_ID+" ",
					" UNIT_TYPE_ID = "+ Building_Detail.type +" ",
				   event );
							
		} else {
			check();
					 
		}

	}

	public void processAddOption(Stage parent_primaryStage) {

		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
		try {
			modular.cancelreq(" UNIT_TYPE ", primaryStage, e);
		} catch (SQLException e2) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err type : " + e2.getMessage());
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
			unitType(Building_Detail.type);
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err type EDIT : " + e.getMessage());
		}
		
		primaryStage = parent_primaryStage;
		primaryStage.setOnCloseRequest(e -> {
				 
			try {
				modular.cancelreq("UNIT_TYPE ", primaryStage, e);
			} catch (SQLException e2) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit type EDIT CANCEL : " + e2.getMessage());
			}
				
		});
			
	}

	public void sizeview() {
			
		txtName.setPrefWidth(410);
		txtDesc.setPrefWidth(410);
		txtArea.setPrefWidth(410);
		txtPrice.setPrefWidth(410);
		txtMaxCapacity.setPrefWidth(410);

		lblMessage.setLayoutX(535);
		lblDesc.setLayoutX(535);
		lblArea.setLayoutX(535);
		lblPrice.setLayoutX(535);
		lblMaxCapacity.setLayoutX(535);
		lblName.setLayoutX(535);

	}

	public void processViewOption(String uID) {
		
		vb.getChildren().removeAll(tblFinalize, btnFinalize);
		btnFinalize.setVisible(false);
		vb.setMaxWidth(0);
		sizeview();
		btnCancel.setText("CLOSE");
		
		txtName.setDisable(true);
		txtDesc.setDisable(true);
		txtArea.setDisable(true);
		txtPrice.setDisable(true);
		txtMaxCapacity.setDisable(true);

		try {
			unitType(Building_Detail.type);

		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit type record view : " + e.getMessage());
		}
			 
	}

}
