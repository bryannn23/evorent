package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.sun.javafx.fxml.builder.JavaFXImageBuilder;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;

public class Building_Detail implements Initializable {

	public DBconnection dbcon = new DBconnection();
	public TableColumns tblCol = new TableColumns();
	public Modular_Class modular = new Modular_Class();
	public ComboBoxValues comboBox = new ComboBoxValues();
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	@FXML public TabPane tabPane_Building;
	@FXML private Tab tabBuildingInformation;
	@FXML private Tab tabCashflow;
	@FXML private Tab tabUnits;
	@FXML private Tab tabTenants;
	@FXML private Tab tabOccupancy;
	@FXML private Tab tabBilling;
	@FXML private Tab tabPayment;
	@FXML private Tab tabReports;
	 
	@FXML private Label lblWelcomeMessage;
	@FXML private Label lblDateTime;
	
	@FXML ComboBox<String> cmbSearch;
	@FXML TextField txtSearch;
	
	//header1
	@FXML private Label lblMainBuildingName;
	@FXML private Button btnNotifications;
	@FXML private Button btnSwitchBuilding;
	@FXML private ImageView imgBldgLogo;
	
	//building information
	@FXML TextField txtBIName;
	@FXML TextArea txtBIAddress;
	@FXML TextField txtBIContactInfo;
	@FXML TextField txtBINoOfUnits;
	@FXML TextField txtBINoOfFloors;
	@FXML TextArea txtBIOtherFeatures;
	@FXML TextField txtBICondition;
	@FXML TextArea txtBIRemarks;
	
	@FXML private TextField txtBIDateEstablished;
	@FXML private ImageView imgBIBuilding1;
	@FXML private ImageView imgBIBuilding2;
	@FXML private ImageView imgBIBuilding3;
	@FXML private ImageView imgBIBuilding4;

	Image image1, image2, image3, image4;
	
	
	@FXML private Button btnBIEdit;
	@FXML private Button btnBIAddBuildingIssue;
	@FXML private Button btnBIAddMaintenanceAct;
	@FXML private Button btnBIViewAllIssue;
	@FXML private Button btnBIViewAllActivity;
	
	public static String bldgDetail;
	
	//cashflow expenses
	@FXML private TableView<TableExpense> tblExpense;
	@FXML private Button btnExpensesAdd;
	@FXML private Button btnExpensesEdit;
	@FXML private Button btnExpensesView;
	public static String expense;
	

	 @FXML private TableView<TableBillingStatement> tblBillingStatement;
	 @FXML private TableView<TableBuildingIssue> tblBIBuildingIssues;
	 @FXML private TableView<TableBuildingDetail> tblBuildingDetail;
	 @FXML private TableView<TableOccupancyDetail> tblOccupancyDetail; 
	 @FXML private TableView<TableRentPayment> tblRentPayment;
	 @FXML private TableView<TableUnitDetail> tblUnitDetail;
	 @FXML private TableView<TableUnitType> tblUnitType;
	 
	 //tenant
	 @FXML private TableView<TableTenantDetail> tblTenant;
	 @FXML private Button btnTenantAdd;
	 @FXML private Button btnTenantCreateAccount;
	 public static String tenant;
	 
	//building issue
	public static String issue;
	
	//building maintenance
	public static String maintenance;
	@FXML private TableView<TableBuildingMaintenance> tblBIMaintenanceHistory;
	
	
	//unit type
	@FXML private Button btnUTAdd;
	@FXML private Button btnUTEdit;
	@FXML private Button btnUTView;
	@FXML private Button btnUTDelete;
	public static String type;
	
	//unit details
	public static String unitDetails;
	
	//occupancy detail
	@FXML private Button btnOccupancyAdd;
	public static String occpDetail;
	public static String occpUnit;
	public static String occpTenant;
	
	//billing statement
	@FXML private Button btnBillingCreate;
	@FXML private Button btnBillingEdit;
	@FXML private Button btnBillingView;
	public static String billing;
	public static String billingNo;
	public static String billingOccp;
	public static String billingTenant;
	
	//rent payment
	@FXML private Button btnPaymentAdd;
	@FXML private Button btnPaymentEdit;
	@FXML private Button btnPaymentView;
	public static String rentPayment;
	public static String rentOccp;
	
	//contractor
	public static String contr;
	@FXML private TableView<TableContractor> tblContractor;
	@FXML private Button btnContractorAdd;
	@FXML private Button btnContractorEdit;
	@FXML private Button btnContractorView;
	
	
	//print buttons
	@FXML private Button btnBillingPrint;
	@FXML private Button btnPaymentPrint;
	
	//charts
	@FXML private BarChart<String, Double> barChartExpense;
	@FXML private BarChart<String, Double> barChartRevenue;
	@FXML private TextField txtTotalExpense;
	@FXML private TextField txtTotalRevenue;
	
	@FXML private TableView<TableSummary> tblExpSummary;
	@FXML private TableView<TableSummary> tblRevSummary;
	
	
	int billingDuedate, occpEnd;
	
	// report 
	@FXML private DatePicker dpReportStart;
	@FXML private DatePicker dpReportEnd;
	@FXML private ComboBox<String> cmbReportType;
	@FXML private Label lblReportStart;
	@FXML private Label lblReportEnd;
	@FXML private Label lblReportType;
	@FXML private Label lblReportMessage;
	@FXML private Button btnReportGen; 
	Image imgCompanyLogo;
	
	String img; 
	String compName, compAddress, phone ;
	
	//bldg setting
	@FXML private Button btnBldgAddSetting;
	@FXML private Button btnBldgCreateSetting;
	@FXML private Button btnBldgEditSetting;
	@FXML private Button btnBldgUpdateSetting;
	
	@FXML private TextField txtBldgTIN;
	@FXML private TextField txtBldgBilling;
	@FXML private TextField txtBldgReceipt;
	@FXML private TextField txtBldgDueDate;
	
	@FXML private ImageView imgBldgLogoSetting;
	@FXML private ImageView imgBldgWatermark;
	@FXML private Label lblBldgLogo;
	@FXML private Label lblBldgWatermark;
	@FXML private Button btnUploadLogo;
	@FXML private Button btnUploadWatermark;
	
	@FXML private Label lblBldgTIN;
	@FXML private Label lblBldgBilling;
	@FXML private Label lblBldgReceipt;
	@FXML private Label lblBldgDueDate;
	@FXML private Label lblBldgMessage;
	int setting;
	
	Image imgLogoSetting;
	Image imgWatermarkSetting;
	File fileLogo, fileWatermark ; 
	FileChooser fileChooserLogo = new FileChooser();
	FileChooser fileChooserWatermark = new FileChooser();
	FileInputStream getLogo, getWatermark;
	BufferedImage bufferedImageLogo, bufferedImageWatermark ;
	Stage primaryStage;
	
	//for report
	public static String bldgName, bldgAddress, bldgContact ;
	public static String logoFileName, watermark, date, bldgBilling, bldgReceipt, bldgTIN;
	
	//unit list filter
	@FXML private ComboBox<String> cmbUnitType;
	@FXML private ComboBox<String> cmbUnitFeature;
	@FXML private ComboBox<String> cmbUnitCondition;
	@FXML private ComboBox<String> cmbUnitLocation;
	@FXML private Button btnUnitGenerate;
	String unitFeature, unitCondition, unitLocation;
	
	@FXML private ComboBox<String> cmbTenantStatus;
	@FXML private Button btnTenantGenerate;
	String tenantStatus;
	
	@FXML private ComboBox<String> cmbOccpStatus;
	@FXML private ComboBox<String> cmbOccpDelivery;
	@FXML private Button btnOccpGenerate;
	String occpStatus, occpDelivery;
	
	@FXML private ComboBox<String> cmbExpenseCOA;
	@FXML private DatePicker dpExpenseFrom;
	@FXML private DatePicker dpExpenseTo;
	@FXML private Button btnExpenseGenerate;
	@FXML private Label lblExpenseCOA;
	@FXML private Label lblExpenseFrom;
	@FXML private Label lblExpenseTo;
	@FXML private Label lblExpenseMessage;
	@FXML private TextField txtExpenseTotal;
	String expCOA;
	int year = Year.now().getValue();
	LocalDate expDate = LocalDate.of(year, 1, 1);
	
	@FXML private DatePicker dpChartsFrom;
	@FXML private DatePicker dpChartsTo;
	@FXML private Button btnChartsGenerate;
	@FXML private Label lblChartsFrom;
	@FXML private Label lblChartsTo;
	@FXML private Label lblChartsMessage;
	
	public static String bldgFloors;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		main_createTimer();
        lblWelcomeMessage.setText("Welcome, " + Globals.G_Employee_Name + "!");
        lblDateTime.setText(new SimpleDateFormat("MMMM d, yyyy | h:mm a").format(new Date()));
        
		
		initializeBuildingInformation();
		initialize_BuildingIssueScreen();
		initialize_BuildingMaintenanceScreen();
		initialize_UnitTypeScreen();
		initialize_UnitDetailScreen();
		initialize_TenantScreen();
		initialize_OccupancyDetailScreen();
		initialize_ContractorScreen();
		initialize_BillingStatementScreen();
		initialize_RentPaymentScreen();
		initializaCashflowChart();
		initialize_ExpenseScreen();
		initializeReport();
		initializeSettings();
		
		//filter
		initializeFilterUnit();
		initializeFilterTenant();
		initializeFilterOccupancy();
		initializeFilterExpense();
		initializeFilterCharts();
		
		
		try {
			refresh();
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err refresh building details : " + e.getMessage());
		}
		
		if (image1 != null) {
			imgBIBuilding1.setOnMouseClicked( e-> {
				
				viewImage(image1);
				
			});
		}
		
		if (image2 != null ) {
			imgBIBuilding2.setOnMouseClicked( e-> {
				
				viewImage(image2);
				
			});
		}
		
		if (image3 != null ) {
			imgBIBuilding3.setOnMouseClicked( e-> {
				
				viewImage(image3);
				
			});
		}
		
		if (image4 != null ) {
			imgBIBuilding4.setOnMouseClicked( e-> {
				
				viewImage(image4);
				
			});
		}
		
	}

    public void main_createTimer() {

    	  final Timeline timeline = new Timeline();
          timeline.setCycleCount(Animation.INDEFINITE);
          timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000), e -> {
              lblDateTime.setText(new SimpleDateFormat("MMMM d, yyyy | h:mm a").format(new Date()));
          }));
          timeline.play();
          
    }

    public void logout(ActionEvent event) throws SQLException {
       
    	Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log-out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {

            ((Node) event.getSource()).getScene().getWindow().hide();

            openfxml(event, "Log Out", "/application/LogIn.fxml");
            
        } else {
           
            return;
        }
    }
    
	public void initializeFilterUnit () {
		
		btnUnitGenerate.setDisable(true);
		
		cmbUnitLocation.setOnAction( e-> {
			btnUnitGenerate.setDisable(false);
		});
		
		cmbUnitFeature.setOnAction( e-> {
			btnUnitGenerate.setDisable(false);
		});
		
		cmbUnitCondition.setOnAction( e-> {
			btnUnitGenerate.setDisable(false);
		});
		
		cmbUnitType.setOnAction( e-> {
			btnUnitGenerate.setDisable(false);
			cmbUnitFeature.setValue(null);
			cmbUnitLocation.setValue(null);
			cmbUnitCondition.setValue(null);
			
		});
		
	}
	
	public void unitSetval () {
		
		if (cmbUnitLocation.getValue() == null)
			unitLocation = null;
		else if (cmbUnitLocation.getValue().equalsIgnoreCase("Not Applicable"))
			unitLocation = "0";
		else if (cmbUnitLocation.getValue().equalsIgnoreCase("North"))
			unitLocation = "1";
		else if (cmbUnitLocation.getValue().equalsIgnoreCase("South"))
			unitLocation = "2";
		else if (cmbUnitLocation.getValue().equalsIgnoreCase("East"))
			unitLocation = "3";
		else if (cmbUnitLocation.getValue().equalsIgnoreCase("West"))
			unitLocation = "4";
		else if (cmbUnitLocation.getValue().equalsIgnoreCase("North East"))
			unitLocation = "5";
		else if (cmbUnitLocation.getValue().equalsIgnoreCase("North West"))
			unitLocation = "6";
		else if (cmbUnitLocation.getValue().equalsIgnoreCase("South East"))
			unitLocation = "7";
		else if (cmbUnitLocation.getValue().equalsIgnoreCase("South West"))
			unitLocation = "8";
	
		if (cmbUnitFeature.getValue() == null ) 
			unitFeature = null;
		else if (cmbUnitFeature.getValue().equalsIgnoreCase("Bare"))
			unitFeature = "0";
		else if (cmbUnitFeature.getValue().equalsIgnoreCase("Partially Furnished"))
			unitFeature = "1";
		else if (cmbUnitFeature.getValue().equalsIgnoreCase("Furnished"))
			unitFeature = "2";
		else if (cmbUnitFeature.getValue().equalsIgnoreCase("Fully Furnished"))
			unitFeature = "3";
		
		if (cmbUnitCondition.getValue() == null)
			unitCondition = null;
		else if (cmbUnitCondition.getValue().equalsIgnoreCase("New"))
			unitCondition = "0";
		else if (cmbUnitCondition.getValue().equalsIgnoreCase("Well Maintained"))
			unitCondition = "1";
		else if (cmbUnitCondition.getValue().equalsIgnoreCase("Needs Renovation"))
			unitCondition = "2";
		else if (cmbUnitCondition.getValue().equalsIgnoreCase("Under Renovation"))
			unitCondition = "3";
		else if (cmbUnitCondition.getValue().equalsIgnoreCase("Renovated"))
			unitCondition = "4";
		else if (cmbUnitCondition.getValue().equalsIgnoreCase("Others"))
			unitCondition = "5";
	}
	
	public void filterUnit(ActionEvent event) throws SQLException {
		
		unitSetval();
		
		if (cmbUnitType.getValue() == null ){
			
			if (cmbUnitFeature.getValue() != null && cmbUnitCondition.getValue() == null && cmbUnitLocation.getValue() == null ) {
				
				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_FEATURES = "+ unitFeature +" ");
			}
			else if (cmbUnitFeature.getValue() == null && cmbUnitCondition.getValue() != null && cmbUnitLocation.getValue() == null ) {
				
				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_CONDITION = "+ unitCondition +" ");
			}
			else if (cmbUnitFeature.getValue() == null && cmbUnitCondition.getValue() == null && cmbUnitLocation.getValue() != null ) {
				
				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_LOCATION = "+ unitLocation +" ");
			}
			else if (cmbUnitFeature.getValue() != null && cmbUnitCondition.getValue() != null && cmbUnitLocation.getValue() == null ) {
				
				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_FEATURES = "+ unitFeature +" AND UNIT_DETAILS.UNIT_CONDITION = "+ unitCondition +" ");
			}
			else if (cmbUnitFeature.getValue() != null && cmbUnitCondition.getValue() == null && cmbUnitLocation.getValue() != null ) {
				
				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_FEATURES = "+ unitFeature +" AND UNIT_DETAILS.UNIT_LOCATION = "+ unitLocation +" ");
			}
			else if (cmbUnitFeature.getValue() == null && cmbUnitCondition.getValue() != null && cmbUnitLocation.getValue() != null ) {
				
				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_CONDITION = "+ unitCondition +" AND UNIT_DETAILS.UNIT_LOCATION = "+ unitLocation +" ");
			}
			else if (cmbUnitFeature.getValue() != null && cmbUnitCondition.getValue() != null && cmbUnitLocation.getValue() != null ) {
				
				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_FEATURES = "+ unitFeature +" AND UNIT_DETAILS.UNIT_CONDITION = "+ unitCondition +" "
								+ "AND UNIT_DETAILS.UNIT_LOCATION = "+ unitLocation +" ");
			}
		}

	else if (cmbUnitType.getValue().equals("All")) {
			
			if (cmbUnitFeature.getValue() == null && cmbUnitCondition.getValue() == null && cmbUnitLocation.getValue() == null ) {
				
				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" ");
				
			}
			else if (cmbUnitFeature.getValue() != null && cmbUnitCondition.getValue() == null && cmbUnitLocation.getValue() == null ) {

				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_FEATURES = "+ unitFeature +" ");
				
			}
			else if (cmbUnitFeature.getValue() == null && cmbUnitCondition.getValue() != null && cmbUnitLocation.getValue() == null ) {

				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_CONDITION = "+ unitCondition +" ");
				
			}
			else if (cmbUnitFeature.getValue() == null && cmbUnitCondition.getValue() == null && cmbUnitLocation.getValue() != null ) {

				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_LOCATION = "+ unitLocation +" ");
				
			}
			else if (cmbUnitFeature.getValue() != null && cmbUnitCondition.getValue() != null && cmbUnitLocation.getValue() == null ) {

				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_FEATURES = "+ unitFeature +" AND UNIT_DETAILS.UNIT_CONDITION = "+ unitCondition +" ");
				
			}
			else if (cmbUnitFeature.getValue() != null && cmbUnitCondition.getValue() == null && cmbUnitLocation.getValue() != null ) {

				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_FEATURES = "+ unitFeature +" AND UNIT_DETAILS.UNIT_LOCATION = "+ unitLocation +" ");
				
			}
			else if (cmbUnitFeature.getValue() == null && cmbUnitCondition.getValue() != null && cmbUnitLocation.getValue() != null ) {

				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_CONDITION = "+ unitCondition +" AND UNIT_DETAILS.UNIT_LOCATION = "+ unitLocation+" ");
				
			}
			else if (cmbUnitFeature.getValue() != null && cmbUnitCondition.getValue() != null && cmbUnitLocation.getValue() != null ) {

				tblUnitDetail.getItems().clear();
				dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
						+ " AND UNIT_DETAILS.UNIT_FEATURES = "+ unitFeature +" AND UNIT_DETAILS.UNIT_CONDITION = "+ unitCondition +" "
						+ "AND UNIT_DETAILS.UNIT_LOCATION = "+ unitLocation +"  ");
				
			}
		}
		else if (cmbUnitType.getValue() != null) {
				
				if ( cmbUnitFeature.getValue() == null && cmbUnitCondition.getValue() == null && cmbUnitLocation.getValue() == null ) {
					
					tblUnitDetail.getItems().clear();
					dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
							+ " AND UNIT_TYPE.UNIT_TYPE_NAME = '"+ cmbUnitType.getValue() +"'  ");
					
				}
				else if ( cmbUnitFeature.getValue() != null && cmbUnitCondition.getValue() == null && cmbUnitLocation.getValue() == null ) {
					
					tblUnitDetail.getItems().clear();
					dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
							+ " AND UNIT_TYPE.UNIT_TYPE_NAME = '"+ cmbUnitType.getValue() +"' AND UNIT_DETAILS.UNIT_FEATURES = "+ unitFeature +" ");
					
				}
				else if ( cmbUnitFeature.getValue() == null && cmbUnitCondition.getValue() != null && cmbUnitLocation.getValue() == null ) {
					
					tblUnitDetail.getItems().clear();
					dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
							+ " AND UNIT_TYPE.UNIT_TYPE_NAME = '"+ cmbUnitType.getValue() +"' AND UNIT_DETAILS.UNIT_CONDITION = "+ unitCondition +" ");
					
				}
				else if ( cmbUnitFeature.getValue() == null && cmbUnitCondition.getValue() == null && cmbUnitLocation.getValue() != null ) {
					
					tblUnitDetail.getItems().clear();
					dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
							+ " AND UNIT_TYPE.UNIT_TYPE_NAME = '"+ cmbUnitType.getValue() +"' AND UNIT_DETAILS.UNIT_LOCATION = "+ unitLocation +" ");
					
				}
				else if ( cmbUnitFeature.getValue() != null && cmbUnitCondition.getValue() != null && cmbUnitLocation.getValue() == null ) {
					
					tblUnitDetail.getItems().clear();
					dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
							+ " AND UNIT_TYPE.UNIT_TYPE_NAME = '"+ cmbUnitType.getValue() +"' AND UNIT_DETAILS.UNIT_FEATURES = "+ unitFeature +" "
							+ "AND UNIT_DETAILS.UNIT_CONDITION = "+ unitCondition +" ");
					
				}
				else if ( cmbUnitFeature.getValue() != null && cmbUnitCondition.getValue() == null && cmbUnitLocation.getValue() != null ) {
					
					tblUnitDetail.getItems().clear();
					dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
							+ " AND UNIT_TYPE.UNIT_TYPE_NAME = '"+ cmbUnitType.getValue() +"' AND UNIT_DETAILS.UNIT_FEATURES = "+ unitFeature +" "
							+ "AND UNIT_DETAILS.UNIT_LOCATION = "+ unitLocation +" ");
					
				}
				else if ( cmbUnitFeature.getValue() == null && cmbUnitCondition.getValue() != null && cmbUnitLocation.getValue() != null ) {
					
					tblUnitDetail.getItems().clear();
					dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
							+ " AND UNIT_TYPE.UNIT_TYPE_NAME = '"+ cmbUnitType.getValue() +"' AND UNIT_DETAILS.UNIT_CONDITION = "+ unitCondition +" "
							+ "AND UNIT_DETAILS.UNIT_LOCATION = "+ unitLocation +" ");
					
				}
				else if ( cmbUnitFeature.getValue() != null && cmbUnitCondition.getValue() != null && cmbUnitLocation.getValue() != null ) {
					
					tblUnitDetail.getItems().clear();
					dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
							+ " AND UNIT_TYPE.UNIT_TYPE_NAME = '"+ cmbUnitType.getValue() +"' AND UNIT_DETAILS.UNIT_FEATURES = "+ unitFeature +" "
							+ "AND UNIT_DETAILS.UNIT_CONDITION = "+ unitCondition +" AND UNIT_DETAILS.UNIT_LOCATION = "+ unitLocation +" ");
					
				}
		}
		
			
	}

	public void initializeFilterTenant () {
		
		btnTenantGenerate.setDisable(true);
		
		cmbTenantStatus.setOnAction( e-> {
			btnTenantGenerate.setDisable(false);
			
		});
		
		
	}
	
	public void filterTenant(ActionEvent event) throws SQLException {
		
		if (cmbTenantStatus.getValue().equals("ACTIVE")) tenantStatus = "1";
		else if (cmbTenantStatus.getValue().equals("INACTIVE")) tenantStatus = "0";
		
		if (cmbTenantStatus.getValue().equals("All")) {

			tblTenant.getItems().clear();
			dbcon.tblTenantDetail(tblTenant, " PRIMARY_TENANT_DETAILS.REFERENCE_ID IS NULL ");
			
		}
		else if (cmbTenantStatus.getValue() != null ){
			
			tblTenant.getItems().clear();
			dbcon.tblTenantDetail(tblTenant, " PRIMARY_TENANT_DETAILS.REFERENCE_ID IS NULL AND "
					+ " PRIMARY_TENANT_DETAILS.TENANT_STATUS = "+ tenantStatus +" ");
		}
		
			
	}

	public void initializeFilterOccupancy () {
		
		btnOccpGenerate.setDisable(true);
		
		cmbOccpStatus.setOnAction( e-> {
			btnOccpGenerate.setDisable(false);
			
		});
		
		cmbOccpDelivery.setOnAction( e-> {
			btnOccpGenerate.setDisable(false);
		});
		
	}
	
	public void occpSetval() {
		
		if (cmbOccpDelivery.getValue() == null)
			occpDelivery = null;
		else if (cmbOccpDelivery.getValue().equalsIgnoreCase("Bare"))
			occpDelivery = "0";
		else if (cmbOccpDelivery.getValue().equalsIgnoreCase("Partially Furnished"))
			occpDelivery = "1";
		else if (cmbOccpDelivery.getValue().equalsIgnoreCase("Furnished"))
			occpDelivery = "2";
		else if (cmbOccpDelivery.getValue().equalsIgnoreCase("Fully Furnished"))
			occpDelivery = "3";
		
		if (cmbOccpStatus.getValue() == null)
			occpStatus = null;
		else if (cmbOccpStatus.getValue().equalsIgnoreCase("New Tenant"))
			occpStatus = "0";
		else if (cmbOccpStatus.getValue().equalsIgnoreCase("Unit Transfer"))
			occpStatus = "1";
		else if (cmbOccpStatus.getValue().equalsIgnoreCase("Building Transfer"))
			occpStatus = "2";
		else if (cmbOccpStatus.getValue().equalsIgnoreCase("End of Contract"))
			occpStatus = "3";
		else if (cmbOccpStatus.getValue().equalsIgnoreCase("Renewed Contract"))
			occpStatus = "4";
		else if(cmbOccpStatus.getValue().equalsIgnoreCase("Others"))
			occpStatus = "5";
	}

	public void filterOccupancy(ActionEvent event) throws SQLException {
		
		occpSetval();

		if (cmbOccpStatus.getValue().equals("All") && cmbOccpDelivery.getValue() == null ) {

			tblOccupancyDetail.getItems().clear();
			dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL ");
			
		}
		else if (cmbOccpStatus.getValue().equals("All") && cmbOccpDelivery.getValue() != null ) {

			tblOccupancyDetail.getItems().clear();
			dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND "
					+ "OCCUPANCY_DETAILS.OCCP_DELIVERY = "+ occpDelivery +" ");
			
		}
		else if (cmbOccpStatus.getValue() != null && cmbOccpDelivery.getValue() == null ){

			tblOccupancyDetail.getItems().clear();
			dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND "
					+ "OCCUPANCY_DETAILS.OCCP_STATUS = "+ occpStatus +" ");
			
		}
		else if (cmbOccpStatus.getValue() == null && cmbOccpDelivery.getValue() != null ) {
			
			tblOccupancyDetail.getItems().clear();
			dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND "
					+ " OCCUPANCY_DETAILS.OCCP_DELIVERY = "+ occpDelivery +" ");
			
		}
		else if (cmbOccpStatus.getValue() != null && cmbOccpDelivery.getValue() != null ) {

			tblOccupancyDetail.getItems().clear();
			dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND "
					+ "OCCUPANCY_DETAILS.OCCP_STATUS = "+ occpStatus +" AND OCCUPANCY_DETAILS.OCCP_DELIVERY = "+ occpDelivery +" ");
			
		}
		
			
	}

	public void initializeFilterExpense () {
		
		btnExpenseGenerate.setDisable(true);
		lblExpenseCOA.setVisible(false);
		lblExpenseFrom.setVisible(false);
		lblExpenseTo.setVisible(false);
		lblExpenseMessage.setVisible(false);
		
		dpExpenseFrom.setValue(expDate);
		dpExpenseTo.setValue(LocalDate.now());
		
		cmbExpenseCOA.setOnAction( e-> {
			btnExpenseGenerate.setDisable(false);
			
		});
	}
	
	public void expSetval() {
		
		if (cmbExpenseCOA.getValue() == null)
			expCOA = null;
		else if (cmbExpenseCOA.getValue().equalsIgnoreCase("Other Cost"))
			expCOA = "0";
		else if (cmbExpenseCOA.getValue().equalsIgnoreCase("Management Fees"))
			expCOA = "1";
		else if (cmbExpenseCOA.getValue().equalsIgnoreCase("Agent Rent Collection Fees"))
			expCOA = "2";
		else if (cmbExpenseCOA.getValue().equalsIgnoreCase("Insurance"))
			expCOA = "3";
		else if (cmbExpenseCOA.getValue().equalsIgnoreCase("Cost of Advertising"))
			expCOA = "4";
		else if (cmbExpenseCOA.getValue().equalsIgnoreCase("Cost of Utilities"))
			expCOA = "5";
		else if (cmbExpenseCOA.getValue().equalsIgnoreCase("Provision for Depreciation")) 
			expCOA = "6";
		else if (cmbExpenseCOA.getValue().equalsIgnoreCase("Cost of Maintenance")) 
			expCOA = "7";
		else if (cmbExpenseCOA.getValue().equalsIgnoreCase("Cost of Repairs and Replacement")) 
			expCOA = "8";
		
	}

	public void filterExpense(ActionEvent event) throws SQLException {
		
		expSetval();

		if (dpExpenseFrom.getValue() != null && dpExpenseTo.getValue() != null && cmbExpenseCOA.getValue() != null) {
			
			lblExpenseFrom.setVisible(false);
			lblExpenseTo.setVisible(false);
			lblExpenseMessage.setVisible(false);
			
			if (cmbExpenseCOA.getValue().equals("All")) {

				tblExpense.getItems().clear();
				dbcon.tblExpense(tblExpense, " EXPENSE_RECORD.REFERENCE_ID IS NULL AND "
						+ " EXPENSE_RECORD.EXPENSE_TXN_DATE BETWEEN '"+ dpExpenseFrom.getValue() +"' AND '"+ dpExpenseTo.getValue() +"' ");
				
				dbcon.totalExpense(txtExpenseTotal, " REFERENCE_ID IS NULL AND "
						+ " EXPENSE_RECORD.EXPENSE_TXN_DATE BETWEEN '"+ dpExpenseFrom.getValue() +"' AND '"+ dpExpenseTo.getValue() +"' " );
				
			}
			else if (cmbExpenseCOA.getValue() != null) {

				tblExpense.getItems().clear();
				dbcon.tblExpense(tblExpense, " EXPENSE_RECORD.REFERENCE_ID IS NULL AND EXPENSE_RECORD.EXPENSE_COA = "+ expCOA +" "
						+ " AND EXPENSE_RECORD.EXPENSE_TXN_DATE BETWEEN '"+ dpExpenseFrom.getValue() +"' AND '"+ dpExpenseTo.getValue() +"' ");
				
				dbcon.totalExpense(txtExpenseTotal, " EXPENSE_RECORD.REFERENCE_ID IS NULL AND EXPENSE_RECORD.EXPENSE_COA = "+ expCOA +" "
						+ "AND EXPENSE_RECORD.EXPENSE_TXN_DATE BETWEEN '"+ dpExpenseFrom.getValue() +"' AND '"+ dpExpenseTo.getValue() +"' ");
				
			}
			
		}
		else {
			
			if (cmbExpenseCOA.getValue() == null ) lblExpenseCOA.setVisible(true);
			else lblExpenseCOA.setVisible(false);
			
			if (dpExpenseFrom.getValue() == null ) lblExpenseFrom.setVisible(true);
			else lblExpenseFrom.setVisible(false);
			
			if (dpExpenseTo.getValue() == null ) lblExpenseTo.setVisible(true);
			else lblExpenseTo.setVisible(false);
			
			lblExpenseMessage.setVisible(true);
			
		}
			
	}

	public void initializeFilterCharts () {
		
		dpChartsFrom.setValue(expDate);
		dpChartsTo.setValue(LocalDate.now());
		
		lblChartsFrom.setVisible(false);
		lblChartsTo.setVisible(false);
		lblChartsMessage.setVisible(false);
		
	}
	
	public void filterChart(ActionEvent event) throws SQLException {
		
		if (dpChartsFrom.getValue() != null && dpChartsTo.getValue() != null ) {
			
			lblChartsFrom.setVisible(false);
			lblChartsTo.setVisible(false);
			lblChartsMessage.setVisible(false);
			
			barChartRevenue.getData().clear();
			dbcon.RevenueChart(barChartRevenue, " RENT_PAYMENT_DETAILS.REFERENCE_ID IS NULL AND "
					+ " RENT_PAYMENT_DETAILS.PAYMENT_DATE BETWEEN '"+ dpChartsFrom.getValue() +"' AND '"+ dpChartsTo.getValue() +"' ");
			
			dbcon.totalRevenue(txtTotalRevenue, " RENT_PAYMENT_DETAILS.REFERENCE_ID IS NULL AND "
					+ " RENT_PAYMENT_DETAILS.PAYMENT_DATE BETWEEN '"+ dpChartsFrom.getValue() +"' AND '"+ dpChartsTo.getValue() +"' ");
			dbcon.tblRevenueSummary(tblRevSummary, " RENT_PAYMENT_DETAILS.REFERENCE_ID IS NULL AND "
					+ " RENT_PAYMENT_DETAILS.PAYMENT_DATE BETWEEN '"+ dpChartsFrom.getValue() +"' AND '"+ dpChartsTo.getValue() +"'  ");
			
			barChartExpense.getData().clear();
			dbcon.ExpenseChart(barChartExpense, " REFERENCE_ID IS NULL AND "
					+ " EXPENSE_TXN_DATE BETWEEN '"+ dpChartsFrom.getValue() +"' AND '"+ dpChartsTo.getValue() +"' " );
			
			dbcon.totalExpense(txtTotalExpense, " REFERENCE_ID IS NULL AND "
					+ " EXPENSE_TXN_DATE BETWEEN '"+ dpChartsFrom.getValue() +"' AND '"+ dpChartsTo.getValue() +"' " );
			dbcon.tblExpenseSummary(tblExpSummary, " REFERENCE_ID IS NULL AND "
					+ " EXPENSE_TXN_DATE BETWEEN '"+ dpChartsFrom.getValue() +"' AND '"+ dpChartsTo.getValue() +"' ");
		}
		else {
			
			if (dpChartsFrom.getValue() == null ) lblChartsFrom.setVisible(true);
			else lblChartsFrom.setVisible(false);
			
			if (dpChartsTo.getValue() == null ) lblChartsTo.setVisible(true);
			else lblChartsTo.setVisible(false);
			
			lblChartsMessage.setVisible(true);
			
		}
		
			
	}

	public void viewImage (Image img) {
		
			  try {
		            Stage primaryStage = new Stage();
		            primaryStage.initModality(Modality.APPLICATION_MODAL);
		            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View_Image.fxml"));
		            Parent root = (Parent) loader.load();

		            View_Image cont = loader.getController();
		            cont.processViewImage(primaryStage, img);
		            cont.btnClose.setOnAction( e1 -> primaryStage.close());
		            
		            Scene scene = new Scene(root);
		            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
		            primaryStage.setScene(scene);
		            primaryStage.showAndWait();

		        } catch (Exception e1) {
		            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error view image : " + e1.getMessage());
		        }
		
	}
	
	public void refresh() throws SQLException {
		
		dbcon.updateOccupancy();
		
		tblBIMaintenanceHistory.getItems().clear();
		dbcon.tblBuildingMaintenance(tblBIMaintenanceHistory, " BUILDING_MAINTENANCE.REFERENCE_ID IS NULL AND "
				+ " BUILDING_MAINTENANCE.BLDG_ID = "+ Main_Window.dashboard +" ");
		
		tblBIBuildingIssues.getItems().clear();
		dbcon.tblBuildingIssue(tblBIBuildingIssues, " BUILDING_ISSUES.REFERENCE_ID IS NULL AND "
				+ "BUILDING_ISSUES.BLDG_ID = "+ Main_Window.dashboard +" " );
		
		tblUnitType.getItems().clear();
		dbcon.tblUnitType(tblUnitType, " UNIT_TYPE.REFERENCE_ID IS NULL AND UNIT_TYPE.BLDG_ID = "+ Main_Window.dashboard +" ");
		
		tblUnitDetail.getItems().clear();
		dbcon.tblUnitDetail(tblUnitDetail, " UNIT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" ");
		
		tblTenant.getItems().clear();
		dbcon.tblTenantDetail(tblTenant, " PRIMARY_TENANT_DETAILS.REFERENCE_ID IS NULL ");
		
		tblContractor.getItems().clear();
		dbcon.tblContractor(tblContractor, " REFERENCE_ID IS NULL ");
		
		tblOccupancyDetail.getItems().clear();
		dbcon.tblOccupancyDetail(tblOccupancyDetail, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND "
				+ "OCCUPANCY_DETAILS.OCCP_STATUS <> 3 ");
		
		tblBillingStatement.getItems().clear();
		/*dbcon.tblBillingStatement(tblBillingStatement, " BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
				+ "BILLING_STATEMENT_DETAILS.STATEMENT_ID IN ( SELECT MAX(STATEMENT_ID) FROM BILLING_STATEMENT_DETAILS "
				+ "WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID ) ");
		*/
		
		dbcon.tblBillingStatementFinal(tblBillingStatement, "");
		
		tblRentPayment.getItems().clear();
		dbcon.tblRentPayment(tblRentPayment, " RENT_PAYMENT_DETAILS.REFERENCE_ID IS NULL ");
		
		barChartRevenue.getData().clear();
		dbcon.RevenueChart(barChartRevenue, " RENT_PAYMENT_DETAILS.REFERENCE_ID IS NULL ");
		dbcon.totalRevenue(txtTotalRevenue, " RENT_PAYMENT_DETAILS.REFERENCE_ID IS NULL ");
		dbcon.tblRevenueSummary(tblRevSummary, " RENT_PAYMENT_DETAILS.REFERENCE_ID IS NULL ");
		
		barChartExpense.getData().clear();
		dbcon.ExpenseChart(barChartExpense, " REFERENCE_ID IS NULL " );
		dbcon.totalExpense(txtTotalExpense, " REFERENCE_ID IS NULL " );
		dbcon.tblExpenseSummary(tblExpSummary, " REFERENCE_ID IS NULL ");
		
		tblExpense.getItems().clear();
		dbcon.tblExpense(tblExpense, " EXPENSE_RECORD.REFERENCE_ID IS NULL ");
		dbcon.totalExpense(txtExpenseTotal, " REFERENCE_ID IS NULL " );
		
		BuildingView(Main_Window.dashboard);
		
		//billingDuedate = dbcon.tblBillingNotificationCount();
		billingDuedate = dbcon.totalBillDue();
		occpEnd = dbcon.occpDue();
		
		
		if (billingDuedate > 0 || occpEnd > 0) {
			
			btnNotifications.setStyle(" -fx-background-color:#ff6600; ");
			int total = billingDuedate + occpEnd; 
			btnNotifications.setText("( " + Integer.toString(total) +  " ) LIST OF ALL NOTIFICATIONS");
		}
		

    	setting = dbcon.countSetting( "BUILDING_SETTINGS" , "REFERENCE_ID IS NULL AND BLDG_ID = "+ Main_Window.dashboard +" ");
        	
        if (setting != 0) {
        	btnBldgEditSetting.setVisible(true);
        	btnBldgAddSetting.setVisible(false);
        	btnBldgCreateSetting.setVisible(false);
        	btnBldgUpdateSetting.setVisible(false);
        	
        	SettingView();
        	settingDisable();

        }
        else {
        	btnBldgCreateSetting.setVisible(true);
        		
        }
        
        //filters
        cmbUnitLocation.getItems().clear();
        cmbUnitFeature.getItems().clear();
        cmbUnitCondition.getItems().clear();
        cmbUnitType.getItems().clear();
        
        comboBox.unitLocation(cmbUnitLocation);
		comboBox.unitFeatures(cmbUnitFeature);
		comboBox.condition(cmbUnitCondition);
		
        dbcon.cmbDisplay(cmbUnitType, "*", "UNIT_TYPE_NAME", "UNIT_TYPE", 
				" REFERENCE_ID IS NULL AND BLDG_ID = "+ Main_Window.dashboard +" " );
		cmbUnitType.getItems().add("All");
		
		cmbTenantStatus.getItems().clear();
		cmbTenantStatus.getItems().addAll("ACTIVE", "INACTIVE" );
		cmbTenantStatus.getItems().add("All");
	   
		cmbOccpDelivery.getItems().clear();
		cmbOccpStatus.getItems().clear();
		comboBox.unitFeatures(cmbOccpDelivery);
		comboBox.occpStatus(cmbOccpStatus);
		cmbOccpStatus.getItems().add("All");
		
		cmbExpenseCOA.getItems().clear();
		comboBox.expCOA(cmbExpenseCOA);
		cmbExpenseCOA.getItems().add("All");
		
	}

	public void switchBuilding (ActionEvent event) {
		
		tenant = null;
		
		try {
        	((Node)event.getSource()).getScene().getWindow().hide();
			Stage primaryStage = new Stage();
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/MainWindow.fxml"));
			Parent root = (Parent) loader.load();
			
			Main_Window main = loader.getController();
			//main.showAllowableTabs();
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setMaximized(true);
			primaryStage.setTitle("EvoRent V1.1.0");
			primaryStage.show();
        	
        	}
        	catch (Exception ex) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err back to main window : " + ex.getMessage());
            }
	}
	
	public void notification (ActionEvent event) throws SQLException {
		
		try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/ListOfNotification.fxml"));
            Parent root = (Parent) loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            refresh();
            
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen receipt: " + e.getMessage());
        }
		
	}
	
	public void openfxml(ActionEvent event, String typeOfButton, String filename) {

	    	 if (typeOfButton == "BuildingDetail Edit") {
	            
	    		 if (Main_Window.dashboard == null)
	                 return;
	    		 
	         } else if (typeOfButton == "TableBillingStatement Edit" || typeOfButton == "TableBillingStatement View") {
	        	 
	        	 if (billing == null)
	        		 return;
	        	 
	         } else if (typeOfButton == "TableRentPayment Edit" || typeOfButton == "TableRentPayment View") {
	        	 
	        	 if (rentPayment == null) 
	        		 return;
	        	 
	         } else if (typeOfButton == "TableUnitType Edit" || typeOfButton == "TableUnitType View" ) {
	        	 
	        	 if (type == null)
	        		 return;
	         
	         } else if (typeOfButton == "TableContractor Edit" || typeOfButton == "TableContractor View" ) {
	        	 
	        	 if (contr == null)
	        		 return;
	        	 
	         } else if ( typeOfButton == "TableExpense Edit" || typeOfButton == "TableExpense View" ) {
	        	 
	        	 if (expense == null)
	        		 return;
	        	 
	         }
	    	 
	        try {
	            Stage primaryStage = new Stage();
	            primaryStage.initModality(Modality.APPLICATION_MODAL);
	            FXMLLoader loader = new FXMLLoader(getClass().getResource(filename));
	            Parent root = (Parent) loader.load();

	            if (typeOfButton == "BuildingDetail Edit") {
	                Create_Building cont = loader.getController();
	                cont.processEditOption(Main_Window.dashboard, primaryStage);
	                
	            } else if (typeOfButton == "TableBillingStatement Edit") {
	            	Create_Billing_Statement cont = loader.getController();
	                cont.processEditOption(billing, primaryStage);
	                
	            } else if (typeOfButton == "TableBillingStatement View") {
	            	Create_Billing_Statement cont = loader.getController();
	            	cont.processViewOption(billing);
	            	cont.btnCancel.setOnAction(e -> primaryStage.close());
	            	
	            } else if (typeOfButton == "TableBuildingIssue Add") {
	                Create_Building_Issue cont = loader.getController();
	                cont.processAddOption(primaryStage);
	                
	            } else if (typeOfButton == "TableBuildingMaintenance Add") {
	            	Create_Building_Maintenance cont = loader.getController();
	                cont.processAddOption(primaryStage);
	                
	            } else if (typeOfButton == "TableOccupancyDetail Add") {
	            	Create_Occupancy_Detail cont = loader.getController();
	            	cont.processAddOption(primaryStage);
	            	
	            }  else if (typeOfButton == "TableRentPayment Edit") {
	            	Create_Rent_Payment cont = loader.getController();
	            	cont.processEditOption(rentPayment, primaryStage);
	            	
	            } else if (typeOfButton == "TableRentPayment View") {
	            	Create_Rent_Payment cont = loader.getController();
	            	cont.processViewOption(rentPayment);
	            	cont.btnCancel.setOnAction( e-> primaryStage.close());
	            	
	            } else if (typeOfButton == "TableUnitDetail Add") {
	            	Create_Unit_Detail cont = loader.getController();
	            	cont.processAddOption(primaryStage);
	            	
	            } else if (typeOfButton == "TableUnitDetail Edit") {
	            	Create_Unit_Detail cont = loader.getController();
	            	cont.processEditOption(unitDetails, primaryStage);
	            	
	            } else if (typeOfButton == "TableUnitDetail View") {
	            	Create_Unit_Detail cont = loader.getController();
	            	cont.processViewOption(unitDetails);
	            	cont.btnCancel.setOnAction( e-> primaryStage.close());
	            
	            } else if (typeOfButton == "TableUnitType Add") {
	            	Create_Unit_Type cont = loader.getController();
	            	cont.processAddOption(primaryStage);
	            	
	            } else if (typeOfButton == "TableUnitType Edit") {
	            	Create_Unit_Type cont = loader.getController();
	            	cont.processEditOption(type, primaryStage);
	            	
	            } else if (typeOfButton == "TableUnitType View") {
	            	Create_Unit_Type cont = loader.getController();
	            	cont.processViewOption(type);
	            	cont.btnCancel.setOnAction( e-> primaryStage.close());
	            	
	            } else if (typeOfButton == "TableTenant Add") {
	            	Create_Tenant cont = loader.getController();
	            	cont.processAddOption(primaryStage);
	            	
	            } else if (typeOfButton == "TableContractor Add") {
	            	Create_Contractor cont = loader.getController();
	            	cont.processAddOption(primaryStage);
	            	
	            } else if (typeOfButton == "TableContractor Edit") {
	            	Create_Contractor cont = loader.getController();
	            	cont.processEditOption(contr, primaryStage);
	            	
	            } else if (typeOfButton == "TableContractor View") {
	            	Create_Contractor cont = loader.getController();
	            	cont.processViewOption(contr);
	            	
	            } else if (typeOfButton == "TableExpense Create") {
	            	Create_Expense cont = loader.getController();
	            	cont.processAddOption(primaryStage);
	            	
	            } else if (typeOfButton == "TableExpense Edit") {
	            	Create_Expense cont = loader.getController();
	            	cont.processEditOption(expense, primaryStage);
	            	
	            } else if (typeOfButton == "TableExpense View") {
	            	Create_Expense cont = loader.getController();
	            	cont.processViewOption(expense);
	            	
	            }
	            
	            Scene scene = new Scene(root);
	            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
	            primaryStage.setScene(scene);
	            primaryStage.showAndWait();

	            if (typeOfButton == "BuildingDetail Edit" 
	            		|| typeOfButton == "TableBillingStatement Edit" || typeOfButton == "TableBuildingIssue Add" 
	            		|| typeOfButton == "TableBuildingMaintenance Add" || typeOfButton == "TableOccupancyDetail Add" 
	            		|| typeOfButton == "TableRentPayment Edit" 
	            		|| typeOfButton == "TableUnitDetail Add" || typeOfButton == "TableUnitDetail Edit" 
	            		|| typeOfButton == "TableUnitType Add" || typeOfButton == "TableUnitType Edit" 
	            		|| typeOfButton == "TableTenant Add" 
	            		|| typeOfButton == "BuildingIssue View" || typeOfButton == "TableContractor Add" 
	            		|| typeOfButton == "TableContractor Edit" 
	            		|| typeOfButton == "TableExpense Create" || typeOfButton == "TableExpense Edit" ) {
	            	
	            	refresh();
	            	
	            }
	            
	        } catch (Exception e) {
	            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen : " + typeOfButton + ": " + e.getMessage());
	        }
	    }

	public void initializaCashflowChart () {
		
		//barChartExpense.setDisable(true);
		tblCol.TableSummary(tblExpSummary);
		tblCol.TableSummary(tblRevSummary);
		
	}
	
	public void initializeBuildingInformation() {
		
		txtBIName.setText("TRY");
		
		txtBIName.setEditable(false);
		txtBIAddress.setEditable(false);
		txtBIContactInfo.setEditable(false);
		txtBIDateEstablished.setEditable(false);
		txtBINoOfFloors.setEditable(false);
		txtBINoOfUnits.setEditable(false);
		txtBIOtherFeatures.setEditable(false);
		txtBICondition.setEditable(false);
		txtBIRemarks.setEditable(false);
	
	}
	
	public void BuildingView ( String id ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT BUILDING_DETAILS.*, "
				+ "(SELECT COUNT(*) FROM UNIT_DETAILS WHERE BLDG_ID = BUILDING_DETAILS.BLDG_ID ) AS UNIT "
				+ " FROM BUILDING_DETAILS WHERE BLDG_ID = " +id ;
		
		String img1, img2, img3, img4, condi;
		
		try{
			ps = dbcon.connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				String bldgname = rs.getString("BLDG_NAME");
				lblMainBuildingName.setText(bldgname.toUpperCase());
				
				txtBIName.setText(rs.getString("BLDG_NAME"));
				txtBIAddress.setText(rs.getString("BLDG_ADDRESS"));
				
				String num = rs.getString("BLDG_CONTACT_INFO");
				if (num == null) num = "";
				txtBIContactInfo.setText(num);
				
				txtBIDateEstablished.setText(rs.getString("BLDG_DATE_EST"));
				
				bldgFloors = rs.getString("BLDG_FLOORS");
				 txtBINoOfFloors.setText(rs.getString("BLDG_FLOORS"));
				 
				 condi = rs.getString("BLDG_CONDITION");
				 
				 if (condi == null) txtBICondition.setText("");
				 else if (condi.equals("0")) txtBICondition.setText("NEW");
				 else if (condi.equals("1")) txtBICondition.setText("WELL MAINTAINED");
				 else if (condi.equals("2")) txtBICondition.setText("NEEDS RENOVATION");
				 else if (condi.equals("3")) txtBICondition.setText("UNDER RENOVATION");
				 else if (condi.equals("4")) txtBICondition.setText("RENOVATED");
				 else if (condi.equals("5")) txtBICondition.setText("RETIRED");
				 else if (condi.equals("6")) txtBICondition.setText("OTHERS");
				 
				 txtBINoOfUnits.setText(rs.getString("UNIT"));
				 txtBIOtherFeatures.setText(rs.getString("BLDG_FEATURES"));
				 txtBIRemarks.setText(rs.getString("BLDG_REMARKS"));
				 
				 img1 = rs.getString("BLDG_PHOTO1");
	                
		            if (img1 != null) {
		                  InputStream input = rs.getBinaryStream("BLDG_PHOTO1");
		                  OutputStream output = new FileOutputStream(new File ("src/picture/bldgPhoto1.jpg"));
		                  byte[] content = new byte[300000];
		                  int size = 0;
		                    
		                  while ( (size = input.read(content)) != -1) {
		                     output.write(content, 0, size);
		                  }
		                    
		                  output.close();
		                  input.close();
		                    
		                  image1 = new Image( "file:src/picture/bldgPhoto1.jpg", 1000, 1500, true, true);
		                  imgBIBuilding1.setImage(image1);
		                  imgBldgLogo.setImage(image1);
		              }
		            else {
		                 imgBIBuilding1.setImage(null);
		                 imgBldgLogo.setImage(null);
		                 
		            }

				img2 = rs.getString("BLDG_PHOTO2");
			                
			         if (img2 != null) {
			            InputStream input = rs.getBinaryStream("BLDG_PHOTO2");
			            OutputStream output = new FileOutputStream(new File ("src/picture/bldgPhoto2.jpg"));
			            byte[] content = new byte[300000];
			            int size = 0;
			                    
			            while ( (size = input.read(content)) != -1) {
			                output.write(content, 0, size);
			            }
			                    
			            output.close();
			            input.close();
			                    
			            image2 = new Image( "file:src/picture/bldgPhoto2.jpg", 1000, 1500, true, true);
			            imgBIBuilding2.setImage(image2);
			         }
			         else {
			            imgBIBuilding2.setImage(null);
			        }

			   img3 = rs.getString("BLDG_PHOTO3");
		                
		            if (img3 != null) {
		                InputStream input = rs.getBinaryStream("BLDG_PHOTO3");
		                OutputStream output = new FileOutputStream(new File ("src/picture/bldgPhoto3.jpg"));
		                byte[] content = new byte[300000];
		                int size = 0;
		                    
		                while ( (size = input.read(content)) != -1) {
		                   output.write(content, 0, size);
		                }
		                    
		                output.close();
		                input.close();
		                    
		                image3 = new Image( "file:src/picture/bldgPhoto3.jpg", 1000, 1500, true, true);
		                imgBIBuilding3.setImage(image3);
		             }
		             else {
		                imgBIBuilding3.setImage(null);
		             }
		            

				img4 = rs.getString("BLDG_PHOTO4");
		                
		            if (img4 != null) {
		                InputStream input = rs.getBinaryStream("BLDG_PHOTO4");
		                OutputStream output = new FileOutputStream(new File ("src/picture/bldgPhoto4.jpg"));
		                byte[] content = new byte[300000];
		                int size = 0;
		                    
		                while ( (size = input.read(content)) != -1) {
		                    output.write(content, 0, size);
		                }
		                    
		                output.close();
		                input.close();
		                    
		                image4 = new Image( "file:src/picture/bldgPhoto4.jpg", 1000, 1500, true, true);
		                imgBIBuilding4.setImage(image4);
		           }
		            else {
		                imgBIBuilding4.setImage(null);
		           }
		          

					bldgName = bldgname.toUpperCase();
					bldgAddress = rs.getString("BLDG_ADDRESS");
					bldgContact = rs.getString("BLDG_CONTACT_INFO"); 
					
				 
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display bldg details : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void BuildingEdit (ActionEvent event) {
		
		openfxml(event, "BuildingDetail Edit", "/application/CreateBuilding.fxml");
	}
	
    public void initialize_BillingStatementScreen() {
    	
        btnBillingEdit.setDisable(true);
        btnBillingView.setDisable(true);

        setupBillingStatement_TableView();
    }

    public void setupBillingStatement_TableView() {

        tblCol.TableBillingStatement(tblBillingStatement);

        tblBillingStatement.setStyle("-fx-table-cell-border-color: transparent; -fx-table-column-border-color: transparent; ");
        
        tblBillingStatement.setOnMouseClicked(e -> {
        	
            try {
            	TableBillingStatement selected = tblBillingStatement.getSelectionModel().getSelectedItem();
                billing = selected.getStateID();
                billingNo = selected.getStateNo();
                billingOccp = selected.getStateOccp();
                billingTenant = selected.getOccpID();
                
            } catch (Exception ex) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing statement table : " + ex.getMessage());
            }

            if (billing != null) {
               
            	btnBillingEdit.setDisable(false);
            	btnBillingView.setDisable(false);
            	
            }
            
        });
    }

    public void billingAdd(ActionEvent event) {

        try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/BillingVoucher.fxml"));
            Parent root = (Parent) loader.load();

            Billing_Voucher cont = loader.getController();
            cont.processCreateBilling(primaryStage);
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            billing = null;
            refresh();

        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error billing statement : " + e.getMessage());
        } 
    	
    }

    public void billingEdit(ActionEvent event) {

        openfxml(event, "TableBillingStatement Edit", "/application/CreateBillingStatement.fxml");
        billing = null;
        
    }

    public void billingView(ActionEvent event) {

        openfxml(event, "TableBillingStatement View", "/application/CreateBillingStatement.fxml");
        billing = null;
        
    }

    public void initialize_BuildingIssueScreen() {
    	
    	tblCol.TableBuildingIssue(tblBIBuildingIssues);
    	tblBIBuildingIssues.setStyle("-fx-table-cell-border-color: transparent; ");
    	
    }

    public void bldgIssueCreate(ActionEvent event) {

        openfxml(event, "TableBuildingIssue Add", "/application/CreateBuildingIssue.fxml");
    }
    
    public void bldgIssueViewAll(ActionEvent event) {

    	try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/BuildingIssues.fxml"));
            Parent root = (Parent) loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            refresh();
            
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen bldg issues: " + e.getMessage());
        }
    	
    }

    public void initialize_BuildingMaintenanceScreen() {
      
    	tblCol.TableBuildingMaintenance(tblBIMaintenanceHistory);
    	tblBIMaintenanceHistory.setStyle("-fx-table-cell-border-color: transparent; ");
    	
    }

    public void bldgMaintenanceCreate(ActionEvent event) {

          openfxml(event, "TableBuildingMaintenance Add", "/application/CreateMaintenanceActivity.fxml");
      }

    public void bldgMaintenanceViewAll(ActionEvent event) {

    	try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/BuildingMaintenanceActivity.fxml"));
            Parent root = (Parent) loader.load();
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            refresh();
            
        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen bldg maintenance: " + e.getMessage());
        }
    	
    }

    public void initialize_OccupancyDetailScreen() {
          	
       setupOccupancyDetail_TableView();
       
     }

    public void setupOccupancyDetail_TableView() {

         tblCol.TableOccupancyDetail(tblOccupancyDetail);

         tblOccupancyDetail.widthProperty().addListener(new ChangeListener<Number>() {

 			@Override
 			public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
 				// TODO Auto-generated method stub
 				 Pane header = (Pane) tblOccupancyDetail.lookup("TableHeaderRow");
 	                if (header.isVisible()){
 	                    header.setMaxHeight(0);
 	                    header.setMinHeight(0);
 	                    header.setPrefHeight(0);
 	                    header.setVisible(false);
 	                }
 			}
         });
         
         tblOccupancyDetail.setStyle("-fx-table-cell-border-color: transparent; ");
         
         tblOccupancyDetail.setOnMouseClicked(e -> {
              	
          try {
               TableOccupancyDetail selected = tblOccupancyDetail.getSelectionModel().getSelectedItem();
               occpDetail = selected.getOccpID();
               occpUnit = selected.getOcunit();
               occpTenant = selected.getOctenant();
                      
           } catch (Exception ex) {
               EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err occupancy detail table : " + ex.getMessage());
            }

           if (occpDetail != null) {

        	   try {
                 	
         			Stage primaryStage = new Stage();
    	            primaryStage.initModality(Modality.APPLICATION_MODAL);

         			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Occupancy.fxml"));
         			Parent root = (Parent) loader.load();
         			
         			Scene scene = new Scene(root);
         			scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
         			primaryStage.setScene(scene);
         			primaryStage.setMaximized(true);
         			primaryStage.setTitle("EvoRent V1.1.0");
         			primaryStage.showAndWait();
         			
         			refresh();
         			
         			occpDetail = null;
         			occpUnit = null;
         			occpTenant = null;
                 	
                 	}
                 	catch (Exception ex) {
                         EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err occp detail pop up : " + ex.getMessage());
                     }
                 
               }
                  
            });
       }

    public void occpDetailCreate(ActionEvent event) {

          openfxml(event, "TableOccupancyDetail Add", "/application/CreateOccupancyDetail.fxml");

			occpDetail = null;
			occpUnit = null;
			occpTenant = null;
       	
     }

    public void initialize_RentPaymentScreen() {
            	
    	btnPaymentEdit.setDisable(true);
    	btnPaymentView.setDisable(true);
    	
        setupRentPayment_TableView();
          	
    }

    public void setupRentPayment_TableView() {

         tblCol.TableRentPayment(tblRentPayment);

         tblRentPayment.setStyle("-fx-table-cell-border-color: transparent; ");
         
         tblRentPayment.setOnMouseClicked(e -> {
                	
         try {
               TableRentPayment selected = tblRentPayment.getSelectionModel().getSelectedItem();
               rentPayment = selected.getPayID();
               rentOccp = selected.getOccupancy();
                        
          } catch (Exception ex) {
               EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err rent payment table : " + ex.getMessage());
          }

          if (rentPayment != null) {
               btnPaymentEdit.setDisable(false);
               btnPaymentView.setDisable(false);
               
          }
                    
          });
     }

    public void rentPaymentCreate(ActionEvent event) {

    	try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/CreatePayment.fxml"));
            Parent root = (Parent) loader.load();

            Create_Payment cont = loader.getController();
            cont.processCreateOption(primaryStage);
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            rentPayment = null;
            refresh();

        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error rent payment : " + e.getMessage());
        } 
    	
    	
     }

    public void rentPaymentEdit(ActionEvent event) {

    	//openfxml(event, "TableRentPayment Edit", "/application/CreateRentPayment.fxml");
    	//rentPayment = null;
    	
    	try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/CreatePayment.fxml"));
            Parent root = (Parent) loader.load();

            Create_Payment cont = loader.getController();
            cont.processEditOption(rentPayment, primaryStage);
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            rentPayment = null;
            refresh();

        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error rent payment edit : " + e.getMessage());
        } 
     }

    public void rentPaymentView(ActionEvent event) {

    	//openfxml(event, "TableRentPayment View", "/application/CreateRentPayment.fxml");
    	//rentPayment = null;
    	
    	try {
            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/CreatePayment.fxml"));
            Parent root = (Parent) loader.load();

            Create_Payment cont = loader.getController();
            cont.processViewOption(rentPayment);
            cont.btnCancel.setOnAction( e-> primaryStage.close());
            
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.showAndWait();
            
            rentPayment = null;
            refresh();

        } catch (Exception e) {
            EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error rent payment edit : " + e.getMessage());
        } 
    	
    }

    public void initialize_UnitDetailScreen() {
              	
       setupUnitDetail_TableView();
            	
    }

    public void setupUnitDetail_TableView() {

          tblCol.TableUnitDetail(tblUnitDetail);

          tblUnitDetail.widthProperty().addListener(new ChangeListener<Number>() {

  			@Override
  			public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
  				// TODO Auto-generated method stub
  				 Pane header = (Pane) tblUnitDetail.lookup("TableHeaderRow");
  	                if (header.isVisible()){
  	                    header.setMaxHeight(0);
  	                    header.setMinHeight(0);
  	                    header.setPrefHeight(0);
  	                    header.setVisible(false);
  	                }
  			}
          });
          
          tblUnitDetail.setStyle("-fx-table-cell-border-color: transparent; ");
          
          tblUnitDetail.setOnMouseClicked(e -> {
                  	
          try {
              TableUnitDetail selected = tblUnitDetail.getSelectionModel().getSelectedItem();
              unitDetails = selected.getUID();
              
          } catch (Exception ex) {
               EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit detail table : " + ex.getMessage());
          }

          if (unitDetails != null) {
        	  
        	  try {
        			Stage primaryStage = new Stage();
    	            primaryStage.initModality(Modality.APPLICATION_MODAL);

        			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/UnitDetails.fxml"));
        			Parent root = (Parent) loader.load();
        			
        			
        			Scene scene = new Scene(root);
        			scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
        			primaryStage.setScene(scene);
        			primaryStage.setMaximized(true);
        			primaryStage.setTitle("EvoRent V1.1.0");
        			primaryStage.showAndWait();
                	
        			refresh();
        			unitDetails = null;
        			
                	}
                	catch (Exception ex) {
                        EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit detail pop up : " + ex.getMessage());
                    }
          }
                      
          });
    }

    public void unitDetailCreate (ActionEvent event) {

          openfxml(event, "TableUnitDetail Add", "/application/CreateUnitDetail.fxml");
          unitDetails = null;
          
    }

    public void initialize_UnitTypeScreen() {
                    	
         btnUTEdit.setDisable(true);
         btnUTView.setDisable(true);
         btnUTDelete.setDisable(true);

         setupUnitType_TableView();
                  	
     }

    public void setupUnitType_TableView() {

         tblCol.TableUnitType(tblUnitType);

         tblUnitType.setStyle("-fx-table-cell-border-color: transparent; ");
         
         tblUnitType.setOnMouseClicked(e -> {
                        	
              try {
                 TableUnitType selected = tblUnitType.getSelectionModel().getSelectedItem();
                 type = selected.getUtID();
                                
              } catch (Exception ex) {
                  EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err unit type table : " + ex.getMessage());
              }

               if (type != null) {
                    btnUTEdit.setDisable(false);
                    btnUTView.setDisable(false);
                    btnUTDelete.setDisable(false);
               }
                            
           });
      }

    public void unitTypeCreate (ActionEvent event) {

    	openfxml(event, "TableUnitType Add", "/application/CreateUnitType.fxml");
    	type = null;
    	
    }

    public void unitTypeEdit(ActionEvent event) {

        openfxml(event, "TableUnitType Edit", "/application/CreateUnitType.fxml");
        type = null;
        
    }

    public void unitTypeView(ActionEvent event) {

        openfxml(event, "TableUnitType View", "/application/CreateUnitType.fxml");
        type = null;
        
    }

    public void unitTypeDelete (ActionEvent event) throws SQLException {
    
    	if (type == null)
    		return;
    	
    	modular.deletemes("UNIT_TYPE", "REFERENCE_ID", type, "UNIT_TYPE_DESC", "UNIT_TYPE_ID");
    	refresh();
    	type = null;
    	
    }
    
    public void initialize_TenantScreen() {
    	
    	btnTenantCreateAccount.setDisable(true);
    	btnTenantCreateAccount.setVisible(false);
    	
        setupTenant_TableView();
        
    }

    public void setupTenant_TableView() {

        tblCol.TableTenantDetail(tblTenant);

        tblTenant.widthProperty().addListener(new ChangeListener<Number>() {

  			@Override
  			public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
  				// TODO Auto-generated method stub
  				 Pane header = (Pane) tblTenant.lookup("TableHeaderRow");
  	                if (header.isVisible()){
  	                    header.setMaxHeight(0);
  	                    header.setMinHeight(0);
  	                    header.setPrefHeight(0);
  	                    header.setVisible(false);
  	                }
  			}
          });
        
        tblTenant.setStyle("-fx-table-cell-border-color: transparent; ");
        
        tblTenant.setOnMouseClicked(e -> {
        	
            try {
            	TableTenantDetail selected = tblTenant.getSelectionModel().getSelectedItem();
                tenant = selected.getTenantID();
                
            } catch (Exception ex) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err tenant profile display : " + ex.getMessage());
            }
            
          
            if (tenant != null) {

            	  try {
                      Stage primaryStage = new Stage();
                      primaryStage.initModality(Modality.APPLICATION_MODAL);
                      FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/TenantDetail.fxml"));
                      Parent root = (Parent) loader.load();
                      
                      Scene scene = new Scene(root);
                      scene.getStylesheets().add(getClass().getResource("Main_Window.css").toExternalForm());
                      primaryStage.setScene(scene);
                      primaryStage.setMaximized(true);
                      primaryStage.showAndWait();
                      
                      refresh();
                      tenant = null;
                      
                  } catch (Exception ex) {
                      EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error change screen tenant detail: " + ex.getMessage());
                  }

            }
            
        });
    }

    public void tenantCreate(ActionEvent event) {

        openfxml(event, "TableTenant Add", "/application/CreateTenant.fxml");
        tenant = null;
        
    }
    
    public void initialize_ContractorScreen() {
    	
    	btnContractorEdit.setDisable(true);
    	btnContractorView.setDisable(true);
    	
    	setupContractor_TableView();
        
    }

    public void setupContractor_TableView() {

        tblCol.TableContractor(tblContractor);

        tblContractor.setStyle("-fx-table-cell-border-color: transparent; ");
        
        tblContractor.setOnMouseClicked(e -> {
        	
            try {
            	TableContractor selected = tblContractor.getSelectionModel().getSelectedItem();
                contr = selected.getContrID();
                
            } catch (Exception ex) {
                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err contr details display : " + ex.getMessage());
            }
            
          
            if (contr != null) {

            	btnContractorEdit.setDisable(false);
            	btnContractorView.setDisable(false);
            }
            
        });
    }

    public void contrCreate(ActionEvent event) {

        openfxml(event, "TableContractor Add", "/application/CreateContractor.fxml");
        contr = null;
        
    }

    public void contrEdit(ActionEvent event) {

        openfxml(event, "TableContractor Edit", "/application/CreateContractor.fxml");
        contr = null;
        
    }

    public void contrView(ActionEvent event) {

        openfxml(event, "TableContractor View", "/application/CreateContractor.fxml");
        contr = null;
        
    }
    

    public void initialize_ExpenseScreen() {
            	
    	btnExpensesEdit.setDisable(true);
    	btnExpensesView.setDisable(true);
    	
    	setupExpense_TableView();
          	
    }

    public void setupExpense_TableView() {

         tblCol.TableExpense(tblExpense);

         tblExpense.setStyle("-fx-table-cell-border-color: transparent; ");
         
         tblExpense.setOnMouseClicked(e -> {
                	
         try {
               TableExpense selected = tblExpense.getSelectionModel().getSelectedItem();
               expense = selected.getExpID();
                        
          } catch (Exception ex) {
               EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err expense table : " + ex.getMessage());
          }

          if (expense != null) {
               btnExpensesEdit.setDisable(false);
               btnExpensesView.setDisable(false);
               
          }
                    
          });
     }

    public void expenseCreate(ActionEvent event) {

    	openfxml( event, "TableExpense Create", "/application/CreateExpense.fxml");
    	expense = null;
    	
     }

    public void expenseEdit(ActionEvent event) {

    	openfxml(event, "TableExpense Edit", "/application/CreateExpense.fxml");
    	expense = null;
    	
     }

    public void expenseView(ActionEvent event) {

    	openfxml(event, "TableExpense View", "/application/CreateExpense.fxml");
    	expense = null;
    	
    }

    public void initializeReport () {
    	
    	lblReportStart.setVisible(false);
    	lblReportEnd.setVisible(false);
    	lblReportType.setVisible(false);
    	lblReportMessage.setVisible(false);
    	
    	cmbReportType.getItems().addAll("Summary of Expenses" , 
    			"Summary of Cash In" );   	
    
    }
    
    public void genReport (ActionEvent event) throws ClassNotFoundException, JRException, SQLException {
    	
    	refresh(); 
    	
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy  hh:mm:ss a ");
		String preparedby = "Prepared by: " + Globals.G_Employee_Name + " - " + df.format(new Date());
	
    	if (dpReportStart.getValue() != null && dpReportEnd.getValue() != null &&  
    			cmbReportType.getValue() != null  ) {
    		
    		lblReportMessage.setVisible(false);
    		lblReportStart.setVisible(false);
    		lblReportEnd.setVisible(false);
    		lblReportType.setVisible(false);
    		
        	java.util.Date tsDate =   java.util.Date.from(dpReportStart.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    		java.sql.Date sDate = new java.sql.Date(tsDate.getTime());
    		
    		java.util.Date teDate =   java.util.Date.from(dpReportEnd.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    		java.sql.Date eDate = new java.sql.Date(teDate.getTime());
    		
    		String typeOfReport = cmbReportType.getValue();
    		

    		if (cmbReportType.getValue().equals("Summary of Expenses")) {
    			
    			new GenerateReport().SummaryOfExpenses(
    					lblMainBuildingName.getText(), txtBIAddress.getText(), txtBIContactInfo.getText(),
						typeOfReport,
						dpReportStart.getEditor().getText() + " to " + dpReportEnd.getEditor().getText(),
						sDate, eDate, preparedby, Main_Window.dashboard );
    			
    		}
    		else if (cmbReportType.getValue().equals("Summary of Cash In")) {

    			new GenerateReport().SummaryOfCashIn(
    					lblMainBuildingName.getText(), txtBIAddress.getText(), txtBIContactInfo.getText(),
						typeOfReport,
						dpReportStart.getEditor().getText() + " to " + dpReportEnd.getEditor().getText(),
						sDate, eDate, preparedby, Main_Window.dashboard );
   			
    		}

    	}
    	else {
    		
    		if (dpReportStart.getValue() == null ) lblReportStart.setVisible(true);
    		else lblReportStart.setVisible(false);
    		
    		if ( dpReportEnd.getValue() == null) lblReportEnd.setVisible(true);
    		else lblReportEnd.setVisible(false);
    		
    		if (cmbReportType.getValue() == null ) lblReportType.setVisible(true);
    		else lblReportType.setVisible(false);
    		
    		lblReportMessage.setVisible(true);
    		
    	}
    	
    }
	 
	public void initializeSettings() {
			
			btnBldgAddSetting.setVisible(false);
			btnBldgCreateSetting.setVisible(false);
			btnBldgEditSetting.setVisible(false);
			btnBldgUpdateSetting.setVisible(false);
			
			settingClear();
			
			TextFormatter<String> tftin = modular.getTextFlexiFormatter(50, 'a'); 
			txtBldgTIN.setTextFormatter(tftin);
			TextFormatter<String> tfbill = modular.getTextFlexiFormatter(20, 'a'); 
			txtBldgBilling.setTextFormatter(tfbill);
			TextFormatter<String> tfreceipt = modular.getTextFlexiFormatter(20, 'a'); 
			txtBldgReceipt.setTextFormatter(tfreceipt);
			TextFormatter<String> tfdue = modular.getTextFlexiFormatter(2, 'n'); 
			txtBldgDueDate.setTextFormatter(tfdue);
			
			
			settingDisable();
			
			imgBldgLogoSetting.setOnMouseClicked( ea-> {
				
				if (imgBldgLogoSetting.getImage() != null) {
					   
				   try {
					   Stage primaryStage = new Stage();
				       primaryStage.initModality(Modality.APPLICATION_MODAL);
				       FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View_Image.fxml"));
				       Parent root = (Parent) loader.load();

				       View_Image cont = loader.getController();
				       cont.processViewImage(primaryStage, imgLogoSetting);
				       cont.btnClose.setOnAction( e -> primaryStage.close());
				            
				       Scene scene = new Scene(root);
				       scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
				       primaryStage.setScene(scene);
				       primaryStage.showAndWait();

				  } catch (Exception e) {
					  EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error view image : " + e.getMessage());
				  } }
		       else {
				          
				    lblBldgLogo.setVisible(true);
				    lblBldgMessage.setVisible(true);
				    lblBldgMessage.setText("No uploaded image.");
				    return;
			  }
		});

			imgBldgWatermark.setOnMouseClicked( ea-> {
				
				if (imgBldgWatermark.getImage() != null) {
					   
				   try {
					   Stage primaryStage = new Stage();
				       primaryStage.initModality(Modality.APPLICATION_MODAL);
				       FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/View_Image.fxml"));
				       Parent root = (Parent) loader.load();

				       View_Image cont = loader.getController();
				       cont.processViewImage(primaryStage, imgWatermarkSetting);
				       cont.btnClose.setOnAction( e -> primaryStage.close());
				            
				       Scene scene = new Scene(root);
				       scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
				       primaryStage.setScene(scene);
				       primaryStage.showAndWait();

				  } catch (Exception e) {
					  EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error view image : " + e.getMessage());
				  } }
		       else {
				          
				    lblBldgWatermark.setVisible(true);
				    lblBldgMessage.setVisible(true);
				    lblBldgMessage.setText("No uploaded image.");
				    return;
			  }
		});
			
	}

	public void createSetting (ActionEvent event) {
			
		settingEnable();
		btnBldgCreateSetting.setVisible(false);
		btnBldgAddSetting.setVisible(true);
		
	}
		
	public void settingDisable () {
			
		txtBldgTIN.setDisable(true);
	    txtBldgBilling.setDisable(true);
	    txtBldgReceipt.setDisable(true);
	    txtBldgDueDate.setDisable(true);
	    imgBldgLogoSetting.setDisable(true);
	    imgBldgWatermark.setDisable(true);
	    btnUploadLogo.setDisable(true);
	    btnUploadWatermark.setDisable(true);
	    
	    if (imgBldgLogoSetting.getImage() != null)
	    	imgBldgLogoSetting.setDisable(false);
	    else 
	    	imgBldgLogoSetting.setDisable(true);
	    
	    if (imgBldgWatermark.getImage() != null)
	    	imgBldgWatermark.setDisable(false);
	    else
	    	imgBldgWatermark.setDisable(true);
	    
	    		
	}
		
	public void settingEnable () {
			
		txtBldgTIN.setDisable(false);
	    txtBldgBilling.setDisable(false);
	    txtBldgReceipt.setDisable(false);
	    txtBldgDueDate.setDisable(false);
	    
	    imgBldgLogo.setDisable(false);
	    imgBldgWatermark.setDisable(false);
	    btnUploadLogo.setDisable(false);
	    btnUploadWatermark.setDisable(false);
	    	
	}
		
	public void settingClear() {
			
		lblBldgTIN.setVisible(false);
		lblBldgBilling.setVisible(false);
		lblBldgReceipt.setVisible(false);
		lblBldgDueDate.setVisible(false);
		lblBldgMessage.setVisible(false);
		lblBldgLogo.setVisible(false);
		lblBldgWatermark.setVisible(false);

	}
	
	public void chooseFileLogo (ActionEvent event) throws FileNotFoundException {

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg", "*.gif", "*.bmp") ;
        
        fileChooserLogo.getExtensionFilters().addAll(extFilter);
        fileLogo = fileChooserLogo.showOpenDialog(primaryStage);
        
        try {
             bufferedImageLogo = ImageIO.read(fileLogo);
             imgLogoSetting = SwingFXUtils.toFXImage(bufferedImageLogo, null);
             imgBldgLogoSetting.setImage(imgLogoSetting);
           
        } catch (IOException ex) {
          Logger.getLogger(JavaFXImageBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
        getLogo = new FileInputStream ( fileLogo );
        
    }

	public void chooseFileWatermark (ActionEvent event) throws FileNotFoundException {

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg", "*.gif", "*.bmp") ;
        
        fileChooserWatermark.getExtensionFilters().addAll(extFilter);
        fileWatermark = fileChooserWatermark.showOpenDialog(primaryStage);
        
        try {
             bufferedImageWatermark = ImageIO.read(fileWatermark);
             imgWatermarkSetting = SwingFXUtils.toFXImage(bufferedImageWatermark, null);
             imgBldgWatermark.setImage(imgWatermarkSetting);
           
        } catch (IOException ex) {
          Logger.getLogger(JavaFXImageBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
        getWatermark = new FileInputStream ( fileWatermark );
        
    }
	
	public void SettingView ( ) throws SQLException {
			
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			String query = "SELECT * FROM BUILDING_SETTINGS WHERE REFERENCE_ID IS NULL AND BLDG_ID = "+ Main_Window.dashboard +" "
					+ "AND REFERENCE_ID IS NULL ";
			
			String imgLogo, imgWatermark;
			
			try{
				ps = dbcon.connect.prepareStatement(query);
				rs = ps.executeQuery();
				while (rs.next()){
					
					txtBldgTIN.setText(rs.getString("BLDG_TIN_NO"));
					txtBldgBilling.setText(rs.getString("BLDG_BILLING_STATEMENT_NO"));
					txtBldgReceipt.setText(rs.getString("BLDG_RECEIPT_NO"));
					txtBldgDueDate.setText(rs.getString("DUE_DATE"));
					
					 imgLogo = rs.getString("BLDG_LOGO");
		                
			            if (imgLogo != null) {
			                  InputStream input = rs.getBinaryStream("BLDG_LOGO");
			                  OutputStream output = new FileOutputStream(new File ("src/picture/BldgLogo.jpg"));
			                  byte[] content = new byte[300000];
			                  int size = 0;
			                    
			                  while ( (size = input.read(content)) != -1) {
			                     output.write(content, 0, size);	
			                  }
			                    
			                  output.close();
			                  input.close();
			                    
			                  imgLogoSetting = new Image( "file:src/picture/BldgLogo.jpg", 1000, 1500, true, true);
			                  imgBldgLogoSetting.setImage(imgLogoSetting);
			              }
			            else {
			                 imgBldgLogoSetting.setImage(null);
			            }
					
			            imgWatermark = rs.getString("BLDG_WATERMARK");
		                
			            if (imgWatermark != null) {
			                  InputStream input = rs.getBinaryStream("BLDG_WATERMARK");
			                  OutputStream output = new FileOutputStream(new File ("src/picture/BldgWatermark.jpg"));
			                  byte[] content = new byte[300000];
			                  int size = 0;
			                    
			                  while ( (size = input.read(content)) != -1) {
			                     output.write(content, 0, size);	
			                  }
			                    
			                  output.close();
			                  input.close();
			                    
			                  imgWatermarkSetting = new Image( "file:src/picture/BldgWatermark.jpg", 1000, 1500, true, true);
			                  imgBldgWatermark.setImage(imgWatermarkSetting);
			              }
			            else {
			                 imgBldgWatermark.setImage(null);
			            }
			            
			            logoFileName = "src/picture/BldgLogo.jpg";
			            watermark = "src/picture/BldgWatermark.jpg";
			            date = rs.getString("DUE_DATE");
			            bldgBilling = rs.getString("BLDG_BILLING_STATEMENT_NO");
			            bldgReceipt = rs.getString("BLDG_RECEIPT_NO");
			            bldgTIN = rs.getString("BLDG_TIN_NO");
			            
			            
				}
			}
			
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display setting details of building "+ Main_Window.dashboard +" : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
		}

	public void SettingAdd (ActionEvent event) throws SQLException {
			
		if ( !txtBldgBilling.getText().isEmpty() && !txtBldgReceipt.getText().isEmpty() 
				&& !txtBldgDueDate.getText().isEmpty() )
		{

	        String check = "SELECT * FROM BUILDING_SETTINGS WHERE REFERENCE_ID IS NULL AND BLDG_ID <> "+ Main_Window.dashboard +" ";
		        
		       ps = dbcon.connect.prepareStatement(check);
		       rs = ps.executeQuery();
		        
		       if (rs.next()) {
		            lblBldgMessage.setVisible(true);
		            lblBldgMessage.setText("Record already exist.");
		            
		       } else {
		            
		           String query = "INSERT INTO BUILDING_SETTINGS VALUES ( 0, "
		           		+ " "+ Main_Window.dashboard +" , ?, ?, '"+ txtBldgTIN.getText().replaceAll("'", "`") +"', "
						+ " '"+ txtBldgBilling.getText().replaceAll("'", "`") +"', "
						+ " '"+ txtBldgReceipt.getText().replaceAll("'", "`") +"', "
						+ " '"+ txtBldgDueDate.getText().replaceAll("'", "`") +"', "
						+ " now(), "+ Globals.G_Employee_ID +", null, null, null )";
		            
		            try {
		            	
		            	 if (imgBldgLogoSetting.getImage() != null && imgBldgWatermark.getImage() != null ) {
			                	
			                    ps = dbcon.connect.prepareStatement(query);
			                    ps.setBinaryStream (1, (InputStream)getLogo, (int) fileLogo.length() );
			                    ps.setBinaryStream(2, (InputStream)getWatermark, (int) fileWatermark.length());
			                    ps.executeUpdate();
			                    
			                }
		            	 else if (imgBldgLogoSetting.getImage() != null && imgBldgWatermark.getImage() == null ) {
		            		 
		            		 ps = dbcon.connect.prepareStatement(query);
			                    ps.setBinaryStream (1, (InputStream)getLogo, (int) fileLogo.length() );
			                    ps.setBinaryStream(2, null);
			                    ps.executeUpdate();
		            	 }
		            	 else if (imgBldgLogoSetting.getImage() == null && imgBldgWatermark.getImage() != null ) {
		            		 
		            		 ps = dbcon.connect.prepareStatement(query);
			                    ps.setBinaryStream (1, null );
			                    ps.setBinaryStream(2, (InputStream)getWatermark, (int) fileWatermark.length());
			                    ps.executeUpdate();
		            	 }
			             else {
			                    ps = dbcon.connect.prepareStatement(query);
			                    ps.setString(1, null);
			                    ps.setString(2, null);
			                    ps.executeUpdate();
			                   
			            }
		               
		                
		                Alert alert = new Alert(AlertType.INFORMATION);
		    			alert.setTitle("Information");
		    			alert.setHeaderText(null);
		    			alert.setContentText("You have successfully added a record.");
		    			alert.showAndWait();
		    			
		                refresh();
		                getLogo = null;
		                getWatermark = null;
		                settingClear();
		
		            } catch (Exception e) {
		                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert bldg_setting : " + e.getMessage());
		            } finally {
		                ps.close();
		            }
		        } 
		        }
		     else {
		        	
		       settingCheck();
		        	
		    }
	        
	}
		
	public void SettingEdit (ActionEvent event) {
			
		settingEnable();
			
		btnBldgUpdateSetting.setVisible(true);
			
	}
		
	public void upd() throws SQLException {

		modular.updaterecord(" BUILDING_SETTINGS ", " BLDG_ID, BLDG_LOGO, BLDG_WATERMARK, BLDG_TIN_NO, BLDG_BILLING_STATEMENT_NO, "
				+ " BLDG_RECEIPT_NO, DUE_DATE, DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, REFERENCE_ID ", 
				
				" BLDG_ID, BLDG_LOGO, BLDG_WATERMARK, BLDG_TIN_NO, BLDG_BILLING_STATEMENT_NO, BLDG_RECEIPT_NO, "
				+ "DATETIME_ENTRY, SYSTEM_ACCOUNT_ID, "+ Main_Window.dashboard +" ",
				"  REFERENCE_ID IS NULL AND BLDG_ID = "+ Main_Window.dashboard +" ");
				
	}
		
	public void SettingUpdate (ActionEvent event) throws SQLException {

		if ( !txtBldgBilling.getText().isEmpty() && !txtBldgReceipt.getText().isEmpty() &&
				!txtBldgDueDate.getText().isEmpty()  )
		{

			upd();
	        	
			String check = "SELECT * FROM BUILDING_SETTINGS WHERE REFERENCE_ID IS NULL AND BLDG_ID <> "+ Main_Window.dashboard +" ";

	        ps = dbcon.connect.prepareStatement(check);
	        rs = ps.executeQuery();
	        
	        if (rs.next()) {
	            lblBldgMessage.setVisible(true);
	            lblBldgMessage.setText("Record already exist.");
	            
	        } else {
	        	
	            String query = "UPDATE BUILDING_SETTINGS SET BLDG_LOGO = ?, BLDG_WATERMARK = ?, "
	            		+ " BLDG_TIN_NO = '"+ txtBldgTIN.getText().replaceAll("'", "`") +"', "
						+ " BLDG_BILLING_STATEMENT_NO = '"+ txtBldgBilling.getText().replaceAll("'", "`")+"', "
						+ " BLDG_RECEIPT_NO = '"+ txtBldgReceipt.getText().replaceAll("'", "`") +"', "
						+ " DUE_DATE = '"+ txtBldgDueDate.getText().replaceAll("'", "`") +"', "
						+ " DATETIME_ENTRY = now(), SYSTEM_ACCOUNT_ID = "+ Globals.G_Employee_ID +" WHERE "
						+ "REFERENCE_ID IS NULL AND BLDG_ID = "+ Main_Window.dashboard +" ";

	            try {

	            	 if (imgBldgLogoSetting.getImage() != null && imgBldgWatermark.getImage() != null ) {
	                     
	                     if (getLogo == null && getWatermark == null ) {
	                    	 
	                         fileLogo = new File("src/picture/BldgLogo.jpg") ;
	                         getLogo = new FileInputStream (fileLogo);
	                         
	                         fileWatermark = new File("src/picture/BldgWatermark.jpg");
	                         getWatermark = new FileInputStream (fileWatermark);
	                         
		                     ps = dbcon.connect.prepareStatement(query);
		                     ps.setBinaryStream (1, (InputStream)getLogo, (int) fileLogo.length() );
		                     ps.setBinaryStream(2, (InputStream)getWatermark, (int) fileWatermark.length() ); 
		                     ps.executeUpdate();
		                     
	                     }
	                     
	                     else if (getLogo == null && getWatermark != null) {
	                    	 
	                    	 fileLogo = new File("src/picture/BldgLogo.jpg");
	                    	 getLogo = new FileInputStream(fileLogo);
	                    	 
	                    	 ps = dbcon.connect.prepareStatement(query);
		                     ps.setBinaryStream (1, (InputStream)getLogo, (int) fileLogo.length() );
		                     ps.setBinaryStream(2, (InputStream)getWatermark, (int) fileWatermark.length() ); 
		                     ps.executeUpdate();
	                     }
	                     
	                     else if (getLogo != null && getWatermark == null ) {
	                    	 
	                    	 fileWatermark = new File("src/picture/BldgWatermark.jpg");
	                    	 getWatermark = new FileInputStream (fileWatermark);
	                    	 
	                    	 ps = dbcon.connect.prepareStatement(query);
		                     ps.setBinaryStream (1, (InputStream)getLogo, (int) fileLogo.length() );
		                     ps.setBinaryStream(2, (InputStream)getWatermark, (int) fileWatermark.length() ); 
		                     ps.executeUpdate();
	                     }
	                     else {
	                    	 
	                    	 ps = dbcon.connect.prepareStatement(query);
		                     ps.setBinaryStream (1, (InputStream)getLogo, (int) fileLogo.length() );
		                     ps.setBinaryStream(2, (InputStream)getWatermark, (int) fileWatermark.length() ); 
		                     ps.executeUpdate();
	                     }
	                     
	                 }
	            	 else if (imgBldgLogoSetting.getImage() != null && imgBldgWatermark.getImage() == null ) {
	            		 
	            		 if (getLogo == null) {

	                    	 fileLogo = new File("src/picture/BldgLogo.jpg");
	                    	 getLogo = new FileInputStream(fileLogo);
	                    	 
	            		 }
	            		 
	                     ps = dbcon.connect.prepareStatement(query);
		                 ps.setBinaryStream (1, (InputStream)getLogo, (int) fileLogo.length() );
		                 ps.setString(2, null ); 
		                 ps.executeUpdate();
	            		 
	            	 }
	            	 else if (imgBldgLogoSetting.getImage() == null && imgBldgWatermark.getImage() != null ) {
	            		 
	            		 if (getWatermark == null ) {
	            			 
	            			 fileWatermark = new File("src/picture/BldgWatermark.jpg");
	            			 getWatermark = new FileInputStream(fileWatermark);
	            		 }
	            		 
	            		 ps = dbcon.connect.prepareStatement(query);
	                     ps.setString (1, null );
	                     ps.setBinaryStream(2, (InputStream)getWatermark, (int) fileWatermark.length() ); 
	                     ps.executeUpdate();
	            	 }
	            	 
	                 else {
	                 	
	                     ps = dbcon.connect.prepareStatement(query);
	                     ps.setString(1, null);
	                     ps.setString(2, null);
	                     ps.executeUpdate();
	                 }
	            	

	                Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information");
					alert.setHeaderText(null);
					alert.setContentText("You have successfully updated a record.");
					alert.showAndWait();
					
	                refresh();
	                getLogo = null;
	                getWatermark = null;
	                settingClear();
	                
	            } catch (Exception e) {
	                EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err edit company settings : " + e.getMessage());
	            } finally {
	                ps.close();
	            }
	            
	        	}
			}
	        else {
	        	
	        	settingCheck();
	        }
		
		}
		
	
	public void settingCheck() {
			
	   	if (txtBldgBilling.getText().isEmpty()) lblBldgBilling.setVisible(true);
	   	else lblBldgBilling.setVisible(false);
	    	
	   	if (txtBldgReceipt.getText().isEmpty()) lblBldgReceipt.setVisible(true);
	   	else lblBldgReceipt.setVisible(false);
	    	
	   	if (txtBldgDueDate.getText().isEmpty()) lblBldgDueDate.setVisible(true);
	   	else lblBldgDueDate.setVisible(false);
	   	
	    lblBldgMessage.setVisible(true);
	    	
	    	
	}

		   

}
