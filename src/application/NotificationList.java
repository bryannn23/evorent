package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class NotificationList implements Initializable  {

	public DBconnection dbcon = new DBconnection();
	public TableColumns tblCol = new TableColumns();
	
	
	@FXML private TableView<TableBillingStatement> tblBilling;
	@FXML private TableView<TableOccupancyDetail> tblOccupancy;
	@FXML private TableView<TableOccupancyDetail> tblOccupancyTerminator;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		tblCol.TableBillingStatement(tblBilling);
		tblCol.TableOccupancyDetail(tblOccupancy);
		tblCol.TableOccupancyDetail(tblOccupancyTerminator);
		
		tblBilling.setStyle("-fx-table-cell-border-color: transparent; ");
		tblOccupancy.setStyle("-fx-table-cell-border-color: transparent; ");
		tblOccupancyTerminator.setStyle("-fx-table-cell-border-color: transparent; ");
		
		try {
			
			dbcon.tblBillingNotification(tblBilling);
			
			dbcon.tblOccupancyDetail(tblOccupancy, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND "
					+ "OCCUPANCY_DETAILS.OCCP_STATUS <> 3 AND OCCUPANCY_DETAILS.OCCP_END < NOW() + INTERVAL 60 DAY "
					+ "AND OCCUPANCY_DETAILS.OCCP_END > NOW() + INTERVAL 30 DAY ");
			
			dbcon.tblOccupancyDetail(tblOccupancyTerminator, " OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND "
					+ "OCCUPANCY_DETAILS.OCCP_STATUS <> 3 AND OCCUPANCY_DETAILS.OCCP_END < NOW() + INTERVAL 30 DAY  " );
			
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing statement duedate : " +e.getMessage());
		}
		
	}

}
