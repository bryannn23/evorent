package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DBconnection {

	protected static Connection conn = null;
	
	public static Connection con(){

		try {
			
			Class.forName("com.mysql.jdbc.Driver").newInstance(); 
			 conn = DriverManager.getConnection(Globals.G_IP_connection,"root","dr3NchEd");
			return conn;				
		}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err con : " +e.getMessage());
			return null;
		}		
	}
	
	Connection connect ;
	
	public DBconnection(){		
		connect = con();		
		if (connect == null) System.exit(1);	
	}
	
	public boolean isdbconnected(){
		try {
			return !connect.isClosed();
		} catch (SQLException e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err dbcon : " +e.getMessage());
			return false;
		}
		}
	
	public boolean islogin(String user, String pass) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		String username = "";
		String password = "";

		String query = "SELECT USER_PROFILE.*, USER_ACCESS_RIGHTS.USER_ACCESS_NAME AS ACCESS, "
				+ "CASE USER_PROFILE.USER_ACCESS_ID "
				+ "WHEN '1' THEN EXECUTIVE_DETAILS.EXEC_NAME "
				+ "WHEN '2' THEN ADMIN_DETAILS.ADMIN_NAME "
				+ "WHEN '3' THEN PRIMARY_TENANT_DETAILS.TENANT_NAME "
				+ "WHEN '0' THEN 'PROGRAMMER' "
				+ "ELSE ' ' "
				+ "END AS PROFILE_NAME  "
				+ " FROM USER_PROFILE "
				+ "LEFT JOIN USER_ACCESS_RIGHTS ON USER_ACCESS_RIGHTS.USER_ACCESS_ID = USER_PROFILE.USER_ACCESS_ID "
				+ "LEFT JOIN EXECUTIVE_DETAILS ON EXECUTIVE_DETAILS.EXEC_ID = USER_PROFILE.PROFILE_ID "
				+ "LEFT JOIN ADMIN_DETAILS ON ADMIN_DETAILS.ADMIN_ID = USER_PROFILE.PROFILE_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = USER_PROFILE.PROFILE_ID "
				+ " WHERE USER_PROFILE.USERNAME = '"+user+"' AND USER_PROFILE.PASSWORD = '"+pass+"' AND "
				+ "USER_PROFILE.REFERENCE_ID IS NULL ";
		
		try{
			ps = connect.prepareStatement(query); 
			rs = ps.executeQuery();
			
			if (rs.next()) { 
				
				username = rs.getString("USERNAME");
				password = rs.getString("PASSWORD");
				
				Globals.G_Employee_Name = rs.getString("PROFILE_NAME");
				Globals.G_Employee_ID = rs.getInt("USER_PROFILE_ID");
				Globals.G_Employee_Access_ID = rs.getInt("USER_ACCESS_ID");
				Globals.G_Employee_Status = rs.getInt("USER_STATUS");
				Globals.G_Profile_ID = rs.getInt("PROFILE_ID");
				
				
			}
			if ((user.equals(username)) && (pass.equals(password))) 
			{	
				
				return true; 
				
			}
			else { 
				
				return false; 
				} 
			}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err login : " +e.getMessage());
			return false;
		}
		finally {
			ps.close();
			rs.close();
			
		}
		}
	
	public boolean pinCode (String pin) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		String pincode = "";

		String query = "SELECT * FROM USER_PROFILE WHERE USER_PROFILE_ID = "+Globals.G_Employee_ID+" "
				+ "AND PIN_CODE = '"+pin+"' AND REFERENCE_ID IS NULL";
		
		try{
			ps = connect.prepareStatement(query); 
			rs = ps.executeQuery();
			
			if (rs.next()) { 
				
				pincode = rs.getString("PIN_CODE");
				
			}
			if (pin.equals(pincode))
			{	
				return true; 
				
			}
			else { 
			
				return false; 
				} 
			}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err login admin : "+Globals.G_Employee_ID+" " +e.getMessage());
			return false;
		}
		finally {
			ps.close();
			rs.close();
			
		}
		}

	public void tblUser (TableView<TableUser> tbl, String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT USER_PROFILE.*, USER_ACCESS_RIGHTS.USER_ACCESS_NAME AS ACCESS, "
				+ "CASE USER_PROFILE.USER_ACCESS_ID "
				+ "WHEN '1' THEN EXECUTIVE_DETAILS.EXEC_NAME "
				+ "WHEN '2' THEN ADMIN_DETAILS.ADMIN_NAME "
				+ "WHEN '3' THEN PRIMARY_TENANT_DETAILS.TENANT_NAME "
				+ "WHEN '0' THEN 'PROGRAMMER' "
				+ "ELSE ' ' "
				+ "END AS PROFILE_NAME  "
				+ " FROM USER_PROFILE "
				+ "LEFT JOIN USER_ACCESS_RIGHTS ON USER_ACCESS_RIGHTS.USER_ACCESS_ID = USER_PROFILE.USER_ACCESS_ID "
				+ "LEFT JOIN EXECUTIVE_DETAILS ON EXECUTIVE_DETAILS.EXEC_ID = USER_PROFILE.PROFILE_ID "
				+ "LEFT JOIN ADMIN_DETAILS ON ADMIN_DETAILS.ADMIN_ID = USER_PROFILE.PROFILE_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = USER_PROFILE.PROFILE_ID "
				+ " WHERE "+ condi +" ";
		
		String access, status;
		ObservableList<TableUser> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				access = rs.getString("USER_ACCESS_ID");
				status = rs.getString("USER_STATUS");
				
				if (access.equals("0")) access = "PROGRAMMER";
				else if (access.equals("1")) access = "EXECUTIVE";
				else if (access.equals("2")) access = "ADMIN";
				else if (access.equals("3")) access = "TENANT";
				
				if (status.equals("1")) status = "ACTIVE";
				else if (status.equals("0")) status = "DEACTIVE";

				data.addAll(new TableUser( rs.getString("USER_PROFILE_ID"),
						"\tUSERNAME:  " + rs.getString("USERNAME") + 
						"\n\tPASSWORD:  " + rs.getString("PASSWORD") ,
						rs.getString("PASSWORD"),
						rs.getString("PROFILE_NAME"),
						"NAME:  " + rs.getString("PROFILE_NAME") + 
						"\nPIN CODE:  " + rs.getString("PIN_CODE"),
						"ACCESS RIGHTS:  " + access + 
						"\nSTATUS:  " + status ,
						rs.getString("PIN_CODE"),
						rs.getString("REFERENCE_ID")));
				tbl.setItems(data);	
		}
			}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display userlist in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	public int countBuilding () throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		
		String query = "SELECT COUNT(*) FROM BUILDING_DETAILS WHERE REFERENCE_ID IS NULL";
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				count = rs.getInt(1);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err count if theres a bldg record : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
		
		return count;
	}
	
	public void tblDashboard (TableView<TableDashboard> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT BUILDING_DETAILS.*, "
				+ "(SELECT COUNT(*) FROM UNIT_DETAILS WHERE BLDG_ID = BUILDING_DETAILS.BLDG_ID ) AS UNIT "
				+ " FROM BUILDING_DETAILS WHERE " +condi ;
		
		ObservableList<TableDashboard> data = FXCollections.observableArrayList();
		
		String file, num;
		Image image = null;
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				 file = rs.getString("BLDG_PHOTO1");
		            if (file != null ) {
		                InputStream input = rs.getBinaryStream("BLDG_PHOTO1");
		                OutputStream output = new FileOutputStream(new File ("src/picture/bldg.jpg"));
		                byte[] content = new byte[3000];
		                int size = 0;
		                
		                while ( (size = input.read(content)) != -1) {
		                    output.write(content, 0, size);
		                }
		                
		                output.close();
		                input.close();
		               
		                image = new Image( "file:src/picture/bldg.jpg", 100, 150, true, true);
		                
		            }
		            else {
		                image = null;
		            }
		            
		            num = rs.getString("BLDG_CONTACT_INFO");
		            if (num.equals("0")) num = "";
		            
				data.addAll(new TableDashboard( rs.getString("BLDG_ID"),
						"BUILDING NAME:  " + rs.getString("BLDG_NAME") + "\n\t\t" +
								"ADDRESS:  " + rs.getString("BLDG_ADDRESS") + "\n\t\t" + 
								"CONTACT INFO:  " + num + "\n\t\t" + 
								"DATE ESTABLISHED:  " + rs.getString("BLDG_DATE_EST"),
								
						"\n" + "NO. OF FLOORS:  " + rs.getString("BLDG_FLOORS") +
						"\nNO.OF UNITS:  " + rs.getString("UNIT")  
						+ "\nOTHER FEATURES:  " + rs.getString("BLDG_FEATURES"),
						"",
						new ImageView(image),
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display bldg list in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	public void tblAdminBuilding (TableView<TableDashboard> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT BUILDING_DETAILS.*, "
				+ "(SELECT COUNT(*) FROM UNIT_DETAILS WHERE BLDG_ID = BUILDING_DETAILS.BLDG_ID ) AS UNIT "
				+ " FROM BUILDING_DETAILS "
				+ "LEFT JOIN ADMIN_MANAGEMENT ON ADMIN_MANAGEMENT.BLDG_ID = BUILDING_DETAILS.BLDG_ID "
				+ "LEFT JOIN ADMIN_DETAILS ON ADMIN_DETAILS.ADMIN_ID = ADMIN_MANAGEMENT.ADMIN_ID "
				+ " WHERE " +condi ;
		
		ObservableList<TableDashboard> data = FXCollections.observableArrayList();
		
		String file, num;
		Image image = null;
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				 file = rs.getString("BLDG_PHOTO1");
		            if (file != null ) {
		                InputStream input = rs.getBinaryStream("BLDG_PHOTO1");
		                OutputStream output = new FileOutputStream(new File ("src/picture/bldg.jpg"));
		                byte[] content = new byte[3000];
		                int size = 0;
		                
		                while ( (size = input.read(content)) != -1) {
		                    output.write(content, 0, size);
		                }
		                
		                output.close();
		                input.close();
		                
		                image = new Image( "file:src/picture/bldg.jpg", 100, 150, true, true);
		            }
		            else {
		                image = null;
		            }
		            
		            num = rs.getString("BLDG_CONTACT_INFO");
		            if (num.equals("0")) num = "";
		            
				data.addAll(new TableDashboard( rs.getString("BLDG_ID"),
						"BUILDING NAME:  " + rs.getString("BLDG_NAME") + "\n\t\t\t" +
								"ADDRESS:  " + rs.getString("BLDG_ADDRESS") + "\n\t\t\t" + 
								"CONTACT INFO:  " + num + "\n\t\t\t" + 
								"DATE ESTABLISHED:  " + rs.getString("BLDG_DATE_EST"),
								
						"\nBUILDING FLOORS:  " + rs.getString("BLDG_FLOORS") + 
						"\nNO. OF UNITS:  " + rs.getString("UNIT") + 
 						"\nBUILDING FEATURES:  " + rs.getString("BLDG_FEATURES"),
						rs.getString("BLDG_CONTACT_INFO"),
						new ImageView(image),
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display bldg list in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void tblExecutiveDetails (TableView<TableExecutive> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM EXECUTIVE_DETAILS WHERE " +condi ;
		
		ObservableList<TableExecutive> data = FXCollections.observableArrayList();
		
		String file;
		Image image = null;
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				String acct = rs.getString("EXEC_USER_ACCT");
				 
				if (acct.equals("0")) acct = "NO";
				else if (acct.equals("1")) acct = "YES";
				
				 file = rs.getString("EXEC_PHOTO");
		            if (file != null ) {
		                InputStream input = rs.getBinaryStream("EXEC_PHOTO");
		                OutputStream output = new FileOutputStream(new File ("src/picture/mainexec.jpg"));
		                byte[] content = new byte[3000];
		                int size = 0;
		                
		                while ( (size = input.read(content)) != -1) {
		                    output.write(content, 0, size);
		                }
		                
		                output.close();
		                input.close();
		                
		                image = new Image("file:src/picture/mainexec.jpg", 100, 150, true, true);
		            }
		            else {
		                image = null;
		            }
		           
		        String num = rs.getString("EXEC_MOBILE_NO");
		        if (num == null) 
		        	num = "";
		        else if (num.equals("0") ) num = "";
		        
		        String bday = rs.getString("EXEC_BDAY");
		        if (bday == null) bday = "";
		        
		        String email = rs.getString("EXEC_EMAIL_ADD");
		        if (email == null) email = "";
		        
		        String rem = rs.getString("EXEC_REMARKS");
		        if (rem == null) rem = "";
		        
		            
				data.addAll(new TableExecutive( rs.getString("EXEC_ID"),
						rs.getString("EXEC_NAME"), 
						"NAME:  " + rs.getString("EXEC_NAME") + 
						"\nMOBILE NO:  " + num,
						"BIRTHDAY:  " + bday + 
						"\nEMAIL ADDRESS:  " + email,
						email,
						"ACCOUNT STATUS:  " + acct + 
						"\nREMARKS:  " + rem,
						rem,
						new ImageView(image),
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display exec detail in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void tblAdminDetails (TableView<TableAdminDetails> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM ADMIN_DETAILS WHERE " +condi ;
		
		ObservableList<TableAdminDetails> data = FXCollections.observableArrayList();
		
		String file;
		Image image = null;
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				 file = rs.getString("ADMIN_PHOTO");
		            if (file != null ) {
		                InputStream input = rs.getBinaryStream("ADMIN_PHOTO");
		                OutputStream output = new FileOutputStream(new File ("src/picture/mainadmin.jpg"));
		                byte[] content = new byte[3000];
		                int size = 0;
		                
		                while ( (size = input.read(content)) != -1) {
		                    output.write(content, 0, size);
		                }
		                
		                output.close();
		                input.close();
		                
		                image = new Image( "file:src/picture/mainadmin.jpg", 100, 150, true, true);
		            }
		            else {
		                image = null;
		            }
		         
		         String status = rs.getString("ADMIN_USER_ACCT");
		            
		            if (status.equals("0")) status = "NO";
		            else if (status.equals("1")) status = "YES";
		            
		        String num = rs.getString("ADMIN_MOBILE_NO");
		        if (num.equals("0")) num = "";
		            
				data.addAll(new TableAdminDetails( rs.getString("ADMIN_ID"),
						rs.getString("ADMIN_NAME"),
						"NAME:  " + rs.getString("ADMIN_NAME") + 
						"\nMOBILE NO:  " + num,
						"BIRTHDAY:  " + rs.getString("ADMIN_BDAY") + 
						"\nEMAIL ADDRESS:  " + rs.getString("ADMIN_EMAIL_ADD"),
						rs.getString("ADMIN_EMAIL_ADD"),
						"STATUS:  " + status + 
						"\nREMARKS:  " + rs.getString("ADMIN_REMARKS"),
						rs.getString("ADMIN_REMARKS"),
						new ImageView(image), 
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display admin detail in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void tblAdminManagement (TableView<TableAdminManagement> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT ADMIN_MANAGEMENT.*, ADMIN_DETAILS.ADMIN_NAME AS ADMIN, "
				+ "BUILDING_DETAILS.BLDG_NAME AS BLDG "
				+ " FROM ADMIN_MANAGEMENT "
				+ "LEFT JOIN ADMIN_DETAILS ON ADMIN_DETAILS.ADMIN_ID = ADMIN_MANAGEMENT.ADMIN_ID "
				+ "LEFT JOIN BUILDING_DETAILS ON BUILDING_DETAILS.BLDG_ID = ADMIN_MANAGEMENT.BLDG_ID"
				+ " WHERE " +condi ;
		
		ObservableList<TableAdminManagement> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				String status = rs.getString("MNGMT_STATUS");
				
				if (status.equals("0")) status = "INACTIVE MANAGEMENT ACCOUNT";
				else if (status.equals("1")) status = "ACTIVE MANAGEMENT ACCOUNT";
				
				data.addAll(new TableAdminManagement( rs.getString("MNGMT_ID"),
						"NAME:  " + rs.getString("ADMIN") ,
						"BUILDING:  " + rs.getString("BLDG"),
						"STATUS:  " + status,
						"REMARKS:  " + rs.getString("MNGMT_REMARKS"),
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display admin mngmt in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void tblBuildingDetail (TableView<TableBuildingDetail> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM BUILDING_DETAILS WHERE " +condi ;
		
		ObservableList<TableBuildingDetail> data = FXCollections.observableArrayList();
		
		String file1, file2, file3, file4;
		Image image1 = null, image2 = null, image3 = null, image4 = null;
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				String condition = rs.getString("BLDG_CONDITION");
				
				if (condition == null) condition = "";
				else if (condition.equals("0")) condition = "NEW";
				else if (condition.equals("1")) condition = "WELL MAINTAINED";
				else if (condition.equals("2")) condition = "NEEDS RENOVATION";
				else if (condition.equals("3")) condition = "UNDER RENOVATION";
				else if (condition.equals("4")) condition = "RENOVATED";
				else if (condition.equals("5")) condition = "RETIRED";
				else if (condition.equals("6")) condition = "OTHERS";
				
				file1 = rs.getString("BLDG_PHOTO1");
		            if (file1 != null ) {
		                InputStream input = rs.getBinaryStream("BLDG_PHOTO1");
		                OutputStream output = new FileOutputStream(new File ("src/picture/bldgphoto1.jpg"));
		                byte[] content = new byte[3000];
		                int size = 0;
		                
		                while ( (size = input.read(content)) != -1) {
		                    output.write(content, 0, size);
		                }
		                
		                output.close();
		                input.close();
		                
		                image1 = new Image( "file:src/picture/bldgphoto1.jpg", 100, 150, true, true);
		            }
		            else {
		                image1 = null;
		            }
		            
		        file2 = rs.getString("BLDG_PHOTO2");
		            if (file2 != null ) {
		                InputStream input = rs.getBinaryStream("BLDG_PHOTO2");
		                OutputStream output = new FileOutputStream(new File ("src/picture/bldgphoto2.jpg"));
		                byte[] content = new byte[3000];
		                int size = 0;
		                
		                while ( (size = input.read(content)) != -1) {
		                    output.write(content, 0, size);
		                }
		                
		                output.close();
		                input.close();
		                
		                image2 = new Image( "file:src/picture/bldgphoto2.jpg", 100, 150, true, true);
		            }
		            else {
		                image2 = null;
		            }
		            
			    file3 = rs.getString("BLDG_PHOTO3");
			            if (file3 != null ) {
			                InputStream input = rs.getBinaryStream("BLDG_PHOTO3");
			                OutputStream output = new FileOutputStream(new File ("src/picture/bldgphoto3.jpg"));
			                byte[] content = new byte[3000];
			                int size = 0;
			                
			                while ( (size = input.read(content)) != -1) {
			                    output.write(content, 0, size);
			                }
			                
			                output.close();
			                input.close();
			                
			                image3 = new Image( "file:src/picture/bldgphoto3.jpg", 100, 150, true, true);
			            }
			            else {
			                image3 = null;
			            }

				file4 = rs.getString("BLDG_PHOTO4");
					   if (file4 != null ) {
					        InputStream input = rs.getBinaryStream("BLDG_PHOTO4");
					        OutputStream output = new FileOutputStream(new File ("src/picture/bldgphoto4.jpg"));
					        byte[] content = new byte[3000];
					        int size = 0;
					                
					        while ( (size = input.read(content)) != -1) {
					             output.write(content, 0, size);
					          }
					                
					        output.close();
					        input.close();
					                
					        image4 = new Image( "file:src/picture/bldgphoto4.jpg", 100, 150, true, true);
					       }
					    else {
					          image4 = null;
					       }
					         
				String num = rs.getString("BLDG_CONTACT_INFO");
				if (num.equals("0")) num = "";
					   
				data.addAll(new TableBuildingDetail( rs.getString("BLDG_ID"),
						rs.getString("BLDG_NAME") ,
						rs.getString("BLDG_ADDRESS"),
						num,
						rs.getString("BLDG_DATE_EST"),
						rs.getString("BLDG_FLOORS"),
						rs.getString("BLDG_FEATURES"),
						condition,
						rs.getString("BLDG_REMARKS"),
						new ImageView(image1),
						new ImageView(image2),
						new ImageView(image3),
						new ImageView(image4),
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display bldg details in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void tblBuildingIssue (TableView<TableBuildingIssue> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT BUILDING_ISSUES.*, BUILDING_DETAILS.BLDG_NAME AS BLDG "
				+ " FROM BUILDING_ISSUES "
				+ "LEFT JOIN BUILDING_DETAILS ON BUILDING_DETAILS.BLDG_ID = BUILDING_ISSUES.BLDG_ID "
				+ " WHERE " +condi ;
		
		ObservableList<TableBuildingIssue> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){

				String status = rs.getString("BLDG_ISSUE_STATUS");
				
				if (status == null) status = "";
				else if (status.equals("0")) status = "REPORTED";
				else if (status.equals("1")) status = "FOR SCHEDULING";
				else if (status.equals("2")) status = "ON-GOING MAINTENANCE";
				else if (status.equals("3")) status = "ON-HOLD";
				else if (status.equals("4")) status = "CANCELLED";
				else if (status.equals("5")) status = "COMPLETED";
				else if (status.equals("6")) status = "OTHERS";
				
				data.addAll(new TableBuildingIssue( rs.getString("BLDG_ISSUES_ID"),
						rs.getString("BLDG") ,
						"\t" + rs.getString("BLDG_INSPECTION_DATE"),
						"\t\t" + rs.getString("BLDG_ISSUE_NAME"),
						"\t\t" + rs.getString("BLDG_ISSUE_DESC"),
						"\t\t" + rs.getString("BLDG_ISSUE_ACTPLAN"),
						"\t\t\t" + rs.getString("BLDG_ISSUE_REMARKS"),
						"\t\t\t" + status,
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display bldg issue in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	public void tblBuildingMaintenance (TableView<TableBuildingMaintenance> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT BUILDING_MAINTENANCE.*, BUILDING_DETAILS.BLDG_NAME AS BLDG, "
				+ "CONTRACTOR_DETAILS.CONTR_NAME AS CONTRACTOR "
				+ " FROM BUILDING_MAINTENANCE "
				+ "LEFT JOIN BUILDING_DETAILS ON BUILDING_DETAILS.BLDG_ID = BUILDING_MAINTENANCE.BLDG_ID "
				+ "LEFT JOIN CONTRACTOR_DETAILS ON CONTRACTOR_DETAILS.CONTR_ID = BUILDING_MAINTENANCE.CONTR_ID "
				+ " WHERE " +condi ;
		
		ObservableList<TableBuildingMaintenance> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){

				String activity = rs.getString("BLDG_MX_ACTIVITY");
				
				if (activity == null) activity = "";
				else if (activity.equals("0")) activity = "OTHERS MAINTENANCE";
				else if (activity.equals("1")) activity = "BUILDING INTERIOR INSPECTION";
				else if (activity.equals("2")) activity = "BUILDING EXTERIOR INSPECTION";
				else if (activity.equals("3")) activity = "GENERAL UNIT INSPECTION";
				else if (activity.equals("4")) activity = "SIDINGS (WASH, RE-PAINT, WATERPROOFING)";
				else if (activity.equals("5")) activity = "WINDOWS (WASH, RE-CAULK)";
				else if (activity.equals("6")) activity = "DOORS (WASH, RE-PAINT)";
				else if (activity.equals("7")) activity = "DECKS AND STAIRS (CLEAN, PAINT)";
				else if (activity.equals("8")) activity = "PLUMBING";
				else if (activity.equals("9")) activity = "ELECTRICAL (MAINTENANCE)";
				else if (activity.equals("10")) activity = "PEST CONTROL";

				else if (activity.equals("11")) activity = "OTHERS REPAIRS AND FIXTURES";
				else if (activity.equals("12")) activity = "INTERIOR MAKEOVER";
				else if (activity.equals("13")) activity = "SIGNAGE (CLEAN, REPAIR)";
				else if (activity.equals("14")) activity = "LIGHTING (CLEAN, CHANGE)";
				else if (activity.equals("15")) activity = "ROOF (CLEAN, WATERPROOFING, LEAKS, REPAIR)";
				else if (activity.equals("16")) activity = "FOUNDATION (REPAIR)";
				else if (activity.equals("17")) activity = "GUTTER (CLEAN, REPAIR, RE-PAINT)";
				
			String status = rs.getString("BLDG_MX_STATUS");
			
			if (status == null) status = "";
			else if (status.equals("0")) status = "SCHEDULED FOR MAINTENANCE";
			else if (status.equals("1")) status = "WAITING FOR CONTRACTOR";
			else if (status.equals("2")) status = "ON-GOING MAINTENANCE";
			else if (status.equals("3")) status = "STILL/TERMINATED MAINTENANCE";
			else if (status.equals("4")) status = "MAINTENANCE COMPLETED";
				
				data.addAll(new TableBuildingMaintenance( rs.getString("BLDG_MX_ID"),
						rs.getString("BLDG") ,
						"\t" +rs.getString("BLDG_MX_REF_ID"),
						"\t" + rs.getString("BLDG_MX_DATE"),
						"\t" + activity,
						"\t" + rs.getString("BLDG_MX_DESC"),
						"\t\t" + rs.getString("CONTRACTOR"),
						"\t\t" + status,
						"\t\t" + rs.getString("BLDG_MX_TOTAL_COST"),
						"\t\t" + rs.getString("BLDG_MX_REMARKS"),
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display bldg maintenance in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	public void tblUnitType (TableView<TableUnitType> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT UNIT_TYPE.*, BUILDING_DETAILS.BLDG_NAME AS BLDG "
				+ " FROM UNIT_TYPE "
				+ "LEFT JOIN BUILDING_DETAILS ON BUILDING_DETAILS.BLDG_ID = UNIT_TYPE.BLDG_ID "
				+ " WHERE " +condi ;
		
		ObservableList<TableUnitType> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				data.addAll(new TableUnitType( rs.getString("UNIT_TYPE_ID"),
						"\t" + rs.getString("UNIT_TYPE_NAME") ,
						"\t\t" + rs.getString("BLDG"),
						"\t\t" + rs.getString("UNIT_TYPE_DESC"),
						"\t\t\t" + rs.getString("UNIT_TYPE_AREA"),
						"\t\t\t" + rs.getString("UNIT_TYPE_PRICE"),
						"\t\t\t" + rs.getString("UNIT_TYPE_MAX_CAPACITY"),
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display unit type in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	public void tblUnitDetail (TableView<TableUnitDetail> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT UNIT_DETAILS.*, BUILDING_DETAILS.BLDG_NAME AS BLDG, "
				+ "BUILDING_DETAILS.BLDG_FLOORS AS FLOORS, UNIT_TYPE.UNIT_TYPE_NAME AS TYPE, "
				+ "UNIT_TYPE.UNIT_TYPE_AREA AS AREA, UNIT_TYPE.UNIT_TYPE_PRICE AS PRICE, "
				+ "UNIT_TYPE.UNIT_TYPE_MAX_CAPACITY AS MAX, OCCUPANCY_DETAILS.UNIT_ID AS STATUS "
				+ " FROM UNIT_DETAILS "
				+ "LEFT JOIN BUILDING_DETAILS ON BUILDING_DETAILS.BLDG_ID = UNIT_DETAILS.BLDG_ID "
				+ "LEFT JOIN UNIT_TYPE ON UNIT_TYPE.UNIT_TYPE_ID = UNIT_DETAILS.UNIT_TYPE_ID "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.UNIT_ID = UNIT_DETAILS.UNIT_ID "
				+ " WHERE " +condi + " " ; //GROUP BY UNIT_DETAILS.UNIT_ID 
		
		ObservableList<TableUnitDetail> data = FXCollections.observableArrayList();
		
		String file, status;
		Image image;
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				file = rs.getString("UNIT_PHOTO");
	            if (file != null ) {
	                InputStream input = rs.getBinaryStream("UNIT_PHOTO");
	                OutputStream output = new FileOutputStream(new File ("src/picture/unitphoto.jpg"));
	                byte[] content = new byte[3000];
	                int size = 0;
	                
	                while ( (size = input.read(content)) != -1) {
	                    output.write(content, 0, size);
	                }
	                
	                output.close();
	                input.close();
	                
	                image = new Image( "file:src/picture/unitphoto.jpg", 100, 150, true, true);
	            }
	            else {
	                image = null;
	            }
	            
	            String location = rs.getString("UNIT_LOCATION");
	            
	            if (location == null) location = "";
	            else if (location.equals("0")) location = "NOT APPLICABLE";
	            else if (location.equals("1")) location = "NORTH";
	            else if (location.equals("2")) location = "SOUTH";
	            else if (location.equals("3")) location = "EAST";
	            else if (location.equals("4")) location = "WEST";
	            else if (location.equals("5")) location = "NORTH EAST";
	            else if (location.equals("6")) location = "NORTH WEST";
	            else if (location.equals("7")) location = "SOUTH EAST";
	            else if (location.equals("8")) location = "SOUTH WEST";
	            
	            String features = rs.getString("UNIT_FEATURES");
	            
	            if (features == null) features = "";
	            else if (features.equals("0")) features = "BARE";
	            else if (features.equals("1")) features = "PARTIALLY FURNISHED";
	            else if (features.equals("2")) features = "FURNISHED";
	            else if (features.equals("3")) features = "FULLY FURNISHED";
	            
	            String condition = rs.getString("UNIT_CONDITION");
	            
	            if (condition == null) condition = "";
	            else if (condition.equals("0")) condition = "NEW";
	            else if (condition.equals("1")) condition = "WELL MAINTAINED";
	            else if (condition.equals("2")) condition = "NEEDS RENOVATION";
	            else if (condition.equals("3")) condition = "UNDER RENOVATION";
	            else if (condition.equals("4")) condition = "RENOVATED";
	            else if (condition.equals("5")) condition = "OTHERS";
	         
	            String area = rs.getString("UNIT_AREA");
	            
	            if (area.equals(null) || area.equals("")) area = rs.getString("AREA");
	            else area = rs.getString("UNIT_AREA");
	            
	            String price = rs.getString("UNIT_RENT_AMOUNT");
	            
	            if (!price.equals("0")) price = rs.getString("UNIT_RENT_AMOUNT");
	            else price = rs.getString("PRICE");
	            
	            String max = rs.getString("UNIT_MAX_CAPACITY");
	            
	            if (!max.equals("0")) max = rs.getString("UNIT_MAX_CAPACITY");
	            else max = rs.getString("MAX");
	            
	            status = rs.getString("STATUS");
	            if (status == null) status = "AVAILABLE";
	            else status = "OCCUPIED";
	            
				data.addAll(new TableUnitDetail( rs.getString("UNIT_ID"),
						"UNIT NAME:  " + rs.getString("UNIT_NAME") + "\n\t\tFLOOR:  " + rs.getString("UNIT_FLOOR") 
						+ "\n\t\tTYPE:  " + rs.getString("TYPE") + "\n\t\tAREA:  " + area ,
						rs.getString("BLDG"),
						rs.getString("UNIT_NAME"),
						location,
						rs.getString("TYPE"),
						area,
						"\nBEDROOM:  " + rs.getString("UNIT_BEDROOM") + "\nBATHROOM:  " + rs.getString("UNIT_BATHROOM") +
						"\nFEATURES:  " + features,
						rs.getString("UNIT_OTHER_FEATURE"),
						rs.getString("UNIT_BEDROOM"),
						rs.getString("UNIT_BATHROOM"),
						max,
						"\nCONDITION:  " + condition + "\nMAXIMUM CAPACITY:  " + max + "\nStatus:  " + status,
						price,
						rs.getString("UNIT_ASSOC_AMOUNT"),
						rs.getString("UNIT_REMARKS"),
						new ImageView(image),
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display unit details in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	public void tblUnitIssue (TableView<TableUnitIssue> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT UNIT_ISSUES.*, UNIT_DETAILS.UNIT_NAME AS UNIT "
				+ " FROM UNIT_ISSUES "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = UNIT_ISSUES.UNIT_ID "
				+ " WHERE " +condi ;
		
		ObservableList<TableUnitIssue> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				String status = rs.getString("UNIT_ISSUE_STATUS");
				
				if (status == null) status = " ";
				else if (status.equals("1")) status = "FOR SCHEDULING";
				else if (status.equals("2")) status = "ON-GOING MAINTENANCE";
				else if (status.equals("3")) status = "ON-HOLD";
				else if (status.equals("4")) status = "CANCELLED";
				else if (status.equals("5")) status = "COMPLETED";
				else if (status.equals("6")) status = "OTHERS";
				
				data.addAll(new TableUnitIssue( rs.getString("UNIT_ISSUE_ID"),
						"\t" + rs.getString("UNIT") ,
						"\t" + rs.getString("UNIT_INSPECTION_DATE"),
						"\t\t" + rs.getString("UNIT_ISSUE_NAME"),
						"\t\t" + rs.getString("UNIT_ISSUE_DESC"),
						"\t\t" + rs.getString("UNIT_ISSUE_ACTPLAN"),
						"\t\t" + status,
						"\t\t" + rs.getString("UNIT_ISSUE_REMARKS"),
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display unit issue in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	public void tblUnitMaintenance (TableView<TableUnitMaintenance> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT UNIT_MAINTENANCE.*, UNIT_ISSUES.UNIT_ISSUE_NAME AS ISSUE, "
				+ "UNIT_DETAILS.UNIT_NAME AS UNIT, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT, "
				+ "CONTRACTOR_DETAILS.CONTR_NAME AS CONTRACTOR "
				+ " FROM UNIT_MAINTENANCE "
				+ "LEFT JOIN UNIT_ISSUES ON UNIT_ISSUES.UNIT_ISSUE_ID = UNIT_MAINTENANCE.UNIT_ISSUE_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = UNIT_MAINTENANCE.UNIT_ID "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = UNIT_MAINTENANCE.OCCP_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN CONTRACTOR_DETAILS ON CONTRACTOR_DETAILS.CONTR_ID = UNIT_MAINTENANCE.UNIT_MX_CONTRACTOR "
				+ " WHERE " +condi ;
		
		ObservableList<TableUnitMaintenance> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				String activity = rs.getString("UNIT_MX_ACTIVITY");
				
				if (activity == null) activity = "";
				else if (activity.equals("0")) activity = "OTHERS MAINTENANCE";
				else if (activity.equals("1")) activity = "UNIT INTERIOR INSPECTION";
				else if (activity.equals("2")) activity = "UNIT EXTERIOR INSPECTION";
				else if (activity.equals("3")) activity = "WALLS (WASH, RE-PAINT)";
				else if (activity.equals("4")) activity = "WINDOWS (WASH, RE-CAULK)";
				else if (activity.equals("5")) activity = "DOORS (WASH, RE-PAINT)";
				else if (activity.equals("6")) activity = "PLUMBING (MAINTENANCE)";
				else if (activity.equals("7")) activity = "ELECTRICAL (MAINTENANCE)";
				else if (activity.equals("8")) activity = "PEST CONTROL";
				

				else if (activity.equals("11")) activity = "OTHERS REPAIRS AND FIXTURES";
				else if (activity.equals("12")) activity = "INTERIOR MAKEOVER";
				else if (activity.equals("13")) activity = "LIGHTING (CLEAN, CHANGE)";
				else if (activity.equals("14")) activity = "FOUNDATION (REPAIR)";
				else if (activity.equals("15")) activity = "FURNITURE (REPAIR, CHANGE)";
				else if (activity.equals("16")) activity = "APPLIANCES (REPAID, CHANGE)";
				
			String request = rs.getString("UNIT_MX_REQUEST");
			
			if (request.equals("0")) request = "ADMIN INTIATIVE";
			else if (request.equals("1")) request = "TENANT REQUEST";
			
			String status = rs.getString("UNIT_MX_STATUS");
			
			if (status == null) status = "";
			else if (status.equals("0")) status = "SCHEDLUED FOR MAINTENANCE";
			else if (status.equals("1")) status = "WAITING FOR CONTRACTOR";
			else if (status.equals("2")) status = "UNDERGOING MAINTENANCE";
			else if (status.equals("3")) status = "STILL/TERMINATED MAINTENANCE";
			else if (status.equals("4")) status = "MAINTENANCE COMPLETED";
				
				data.addAll(new TableUnitMaintenance( rs.getString("UNIT_MX_ID"),
						/*"\t" +*/ rs.getString("ISSUE") ,
						"\t" + rs.getString("UNIT"),
						rs.getString("UNIT_MX_REF_ID"),
						/*"\t" +*/ rs.getString("UNIT_MX_DATE"),
						/*"\t" +*/ activity,
						"\t" + rs.getString("UNIT_MX_DESC"),
						/*"\t" +*/ request,
						"\t" + rs.getString("TENANT"),
						"\t" + rs.getString("CONTRACTOR"),
						"\t" + status,
						"\t" + rs.getString("UNIT_MX_TOTAL_COST"),
						"\t" + rs.getString("UNIT_MX_REMARKS"),
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display unit maintenance in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	public void tblTenantDetail (TableView<TableTenantDetail> tbl,  String condi ) throws SQLException {
	
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	String query = "SELECT * FROM PRIMARY_TENANT_DETAILS "
			+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.TENANT_ID = PRIMARY_TENANT_DETAILS.TENANT_ID "
			+ "WHERE " +condi +" AND PRIMARY_TENANT_DETAILS.TENANT_ID NOT IN "
			+ " ( SELECT OCCUPANCY_DETAILS.TENANT_ID FROM OCCUPANCY_DETAILS  "
			+ " LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
			+ " WHERE OCCUPANCY_DETAILS.REFERENCE_ID IS NULL AND OCCUPANCY_DETAILS.OCCP_STATUS <> 3 AND "
			+ " UNIT_DETAILS.BLDG_ID <> "+ Main_Window.dashboard +" ) " ;
	
	ObservableList<TableTenantDetail> data = FXCollections.observableArrayList();
	
	String file, status;
	Image image;
	
	try{
		ps = connect.prepareStatement(query);
		rs = ps.executeQuery();
		while (rs.next()){
			
			file = rs.getString("TENANT_PHOTO");
            if (file != null ) {
                InputStream input = rs.getBinaryStream("TENANT_PHOTO");
                OutputStream output = new FileOutputStream(new File ("src/picture/tenantphoto.jpg"));
                byte[] content = new byte[3000];
                int size = 0;
                
                while ( (size = input.read(content)) != -1) {
                    output.write(content, 0, size);
                }
                
                output.close();
                input.close();
                
                image = new Image( "file:src/picture/tenantphoto.jpg", 100, 150, true, true);
            }
            else {
                image = null;
            }

            String gender = rs.getString("TENANT_GENDER");
            
            if (gender == null) gender = "";
            else if (gender.equals("0")) gender = "MALE";
            else if (gender.equals("1")) gender = "FEMALE";
            
            String account = rs.getString("TENANT_USERACCT");
            
            if (account.equals("0")) account = "NO";
            else if (account.equals("1")) account = "YES";
            
            status = rs.getString("TENANT_STATUS");
            
            if (status == null) status = "";
            else if (status.equals("1")) status = "ACTIVE";
            else if (status.equals("0")) status = "INACTIVE";
            
            String mobile = rs.getString("TENANT_MOBILE_NO");
            if (mobile.equals("0")) mobile = "";
            
            String landline = rs.getString("TENANT_LANDLINE_NO");
            if (landline.equals("0")) landline = "";
            
            data.addAll(new TableTenantDetail( rs.getString("TENANT_ID"),
					"TENANT NAME:  " + rs.getString("TENANT_NAME") +
					"\n\tADDRESS:  " + rs.getString("TENANT_ADDRESS"),
					 rs.getString("TENANT_ADDRESS"),
					"\nMOBILE NO.:  " + mobile,
					"\nLANDLINE NO.:  " + landline,
					rs.getString("TENANT_EMAIL_ADD"),
					rs.getString("TENANT_BDAY"),
					gender,
					account,
					status,
					new ImageView(image),
					rs.getString("REFERENCE_ID")));
			
			tbl.setItems(data);
		}
	}
	
	catch (Exception e){
		EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display tenant detail in table : " +e.getMessage());
	}
	finally {
		ps.close();
		rs.close();
	}
}
	
	public void tblOccupancyDetail (TableView<TableOccupancyDetail> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT OCCUPANCY_DETAILS.*, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT, "
				+ "UNIT_DETAILS.UNIT_NAME AS UNIT "
				+ " FROM OCCUPANCY_DETAILS "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND " +condi ;
		
		byte[] fileBytes;
		String file, fileName; 
		
		String contractName = null;
		boolean doc = false, pdf = false;
		
		ObservableList<TableOccupancyDetail> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){

				String delivery = rs.getString("OCCP_DELIVERY");
				
				if (delivery == null) delivery = "";
				else if (delivery.equals("0")) delivery = "BARE";
				else if (delivery.equals("1")) delivery = "PARTIALLY FURNISHED";
				else if (delivery.equals("2")) delivery = "FURNISHED";
				else if (delivery.equals("3")) delivery = "FULLY FURNISHED";
				
				String status = rs.getString("OCCP_STATUS");
				
				if (status == null) status = "";
				else if (status.equals("0")) status = "NEW TENANT";
				else if (status.equals("1")) status = "UNIT TRANSFER";
				else if (status.equals("2")) status = "BUILDING TRANSFER";
				else if (status.equals("3")) status = "END OF CONTRACT";
				else if (status.equals("4")) status = "RENEWED CONTRACT";
				else if (status.equals("5")) status = "OTHERS";

				fileName = rs.getString("OCCP_CONTRACT_NAME");
				
				if (fileName == null) {
					fileName = "";
				}
				else if (fileName.endsWith(".docx")) {
					
					doc = true;
					
				}
				else if (fileName.endsWith(".pdf")) {
					
					pdf = true;
				}
				
				if (fileName.equals("")) contractName = "src/picture/contractFile.docx";
				
				else if (doc == true) contractName = "src/picture/contractFile.docx";
				
				else if (pdf == true )
					contractName = "src/picture/contractFile.pdf";
				
					
				file = rs.getString("OCCP_CONTRACT_PHOTO");
				
	            if (file != null ) {
	            	
	            	fileBytes = rs.getBytes("OCCP_CONTRACT_PHOTO");
	                OutputStream targetFile=  new FileOutputStream( contractName);
	                file = contractName;

	                targetFile.write(fileBytes);
	                targetFile.close();
	                
	            }
	            else {
	                file = null;
	            }
				

				data.addAll(new TableOccupancyDetail( rs.getString("OCCP_ID"),
						"\t\t" + rs.getString("UNIT") + " - " + rs.getString("TENANT") +
						"\n\t\t\t\tRENT AMOUNT:  " + rs.getString("OCCP_RENT_AMOUNT") + 
						"\n\t\t\t\tBILLING CYCLE:  " + rs.getString("OCCP_BILLING_CYCLE"),
						rs.getString("UNIT"),
						delivery,
						rs.getString("OCCP_START"),
						rs.getString("OCCP_END"),
						rs.getString("OCCP_BILLING_CYCLE"),
						rs.getString("OCCP_RENT_AMOUNT"),
						status,
						"\nDEPOSIT:  " + rs.getString("OCCP_DEPOSIT") + 
						"\nREMAINING DEPOSIT:  " + rs.getString("OCCP_REM_DEPOSIT"),
						rs.getString("OCCP_REM_DEPOSIT"),
						rs.getString("OCCP_NO_OF_TENANT"),
						rs.getString("OCCP_FELLOW_TENANT"),
						rs.getString("OCCP_REMARKS"),
						file,
						rs.getString("REFERENCE_ID"),
						rs.getString("TENANT_ID"),
						rs.getString("UNIT_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display occp detail in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	public String totalBill ( String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String billing = "0" ;
		        
		String query = "SELECT SUM(BILLING_STATEMENT_DETAILS.STATEMENT_AMOUNT) AS BILL, "
				+ "(SELECT SUM(PAYMENT_AMOUNT) FROM RENT_PAYMENT_DETAILS WHERE REFERENCE_ID IS NULL "
				+ "GROUP BY OCCP_ID ) AS PAYMENT, "
				+ "( SUM(BILLING_STATEMENT_DETAILS.STATEMENT_AMOUNT) - ( SELECT SUM(PAYMENT_AMOUNT) FROM "
				+ "RENT_PAYMENT_DETAILS WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID )) AS TOTAL "
				+ " FROM BILLING_STATEMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN RENT_PAYMENT_DETAILS ON RENT_PAYMENT_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE UNIT_DETAILS.BLDG_ID = "+Main_Window.dashboard+" AND "
				+ " BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL "+ condi +" "
				+ " GROUP BY BILLING_STATEMENT_DETAILS.OCCP_ID ";
		
		try{ 
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
					
				billing = rs.getString("TOTAL");
				
			}	
		}
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err total billing statement per occupancy : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
		
		return billing;
		
	}
	
	public void tblBillingStatementFinal(TableView<TableBillingStatement> tbl, String condi) throws SQLException{
		
	    ObservableList<TableBillingStatement> data = FXCollections.observableArrayList();
	    
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		String occp = null, statenum = null, type = null, date = null, month = null, duedate = null, status = null;
		int ic ;
		
		String i = "SELECT COUNT(*) FROM BILLING_STATEMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
				+ "UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
				+ "BILLING_STATEMENT_DETAILS.STATEMENT_ID IN "
				+ "( SELECT MAX(STATEMENT_ID) FROM BILLING_STATEMENT_DETAILS "
				+ "WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID )  ";
		
		try {
		
			preparedStatement = connect.prepareStatement(i);
			rs = preparedStatement.executeQuery();
			
		while (rs.next()){
	
			ic = rs.getInt(1);
		 
		for (int count = 0; count < ic; count++) {
    		
		    String select = "SELECT BILLING_STATEMENT_DETAILS.* FROM BILLING_STATEMENT_DETAILS "
		    		+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
		    		+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
		    		+ " WHERE BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
		    		+ "UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
		    		+ "BILLING_STATEMENT_DETAILS.STATEMENT_ID IN ( SELECT MAX(STATEMENT_ID) FROM BILLING_STATEMENT_DETAILS "
		    		+ "WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID ) LIMIT "+count+", 1 "; 
		    
    		try{
    		
    			preparedStatement2 = connect.prepareStatement(select);
    			rs2 = preparedStatement2.executeQuery();
    			
    			while (rs2.next()){
    			
    				 occp = rs2.getString("OCCP_ID");
    				 statenum = rs2.getString("STATEMENT_NUMBER");
    				 
    				 type = rs2.getString("STATEMENT_TYPE");
     				
     				if (type == null) type = "";
     				else if (type.equals("0")) type = "RENT/LEASE";
     				else if (type.equals("1")) type = "ASSOCIATION";
     				else if (type.equals("2")) type = "ELECTRICITY";
     				else if (type.equals("3")) type = "WATER";
     				else if (type.equals("4")) type = "REPAIRS";
     				else if (type.equals("5")) type = "OTHERS";
     				
     				date = rs2.getString("STATEMENT_DATE");

    				month = rs2.getString("STATEMENT_MONTH");
    				
    				if (month == null) month = "";
    				else if (month.equals("1")) month = "JANUARY";
    				else if (month.equals("2")) month = "FEBUARY";
    				else if (month.equals("3")) month = "MARCH";
    				else if (month.equals("4")) month = "APRIL";
    				else if (month.equals("5")) month = "MAY";
    				else if (month.equals("6")) month = "JUNE";
    				else if (month.equals("7")) month = "JULY";
    				else if (month.equals("8")) month = "AUGUST";
    				else if (month.equals("9")) month = "SEPTEMBER";
    				else if (month.equals("10")) month = "OCTOBER";
    				else if (month.equals("11")) month = "NOVEMBER";
    				else if (month.equals("12")) month = "DECEMBER";
    				
    				status = rs2.getString("STATEMENT_STATUS");
    				
    				if (status == null) status = "";
    				else if (status.equals("0")) status = "FOR PRINTING";
    				else if (status.equals("1")) status = "FOR DISTRIBUTION";
    				else if (status.equals("2")) status = "FOR RE-PRINTING";
    				else if (status.equals("3")) status = "DELIVERED";
    				else if (status.equals("4")) status = "PAID";
    				else if (status.equals("5")) status = "OVERDUE";
    				
    				duedate = rs2.getString("STATEMENT_DUEDATE");
    		
    			}}
    		
    		catch (Exception e){
    			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing details : " +e.getMessage());
    		}
    		finally {
    			preparedStatement2.close();
    			rs2.close();
    		}
    		
    		String  query = " SELECT BILLING_STATEMENT_DETAILS.*, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT, "
    				+ " ( SUM(BILLING_STATEMENT_DETAILS.STATEMENT_AMOUNT) - IFNULL(( SELECT SUM(PAYMENT_AMOUNT) FROM RENT_PAYMENT_DETAILS "
    				+ " WHERE REFERENCE_ID IS NULL AND OCCP_ID = "+ occp +" ), 0) ) AS AMOUNT "
    				+ " FROM BILLING_STATEMENT_DETAILS "
    				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
    				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
    				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
    				+ "WHERE BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
    				+ " AND BILLING_STATEMENT_DETAILS.OCCP_ID = "+ occp +"  ";
    			
    		try{
    			preparedStatement3 = connect.prepareStatement(query);
    			rs3 = preparedStatement3.executeQuery();
    			
    			while (rs3.next()){
    				
    				data.addAll(new TableBillingStatement( rs3.getString("STATEMENT_ID"),
    						"\t" + statenum ,
    						"\t" + rs3.getString("TENANT"),
    						"\t\t" + type,
    						"\t\t" + date,
    						"\t\t" + month,
    						"\t\t" + duedate,
    						"\t\t" + rs3.getString("AMOUNT") ,
    						"\t\t" + status,
    						rs3.getString("REFERENCE_ID"), 
    						rs3.getString("OCCP_ID")));
    					
    		}
    		}
    		catch (Exception e){
    			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err statement billing display : " +e.getMessage());
    		}
    		finally {
    			preparedStatement3.close();
    			rs3.close();
    		}
    		}
			
		}
			tbl.setItems(data);	
		}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err statement details count : " +e.getMessage());
		}
		finally {
			preparedStatement.close();
			rs.close();
		}
		
	}
	
	
	public void tblBillingStatement (TableView<TableBillingStatement> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT BILLING_STATEMENT_DETAILS.*, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT "
				+ " FROM BILLING_STATEMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE " +condi ;
		
		ObservableList<TableBillingStatement> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){

				String type = rs.getString("STATEMENT_TYPE");
				
				if (type == null) type = "";
				else if (type.equals("0")) type = "RENT/LEASE";
				else if (type.equals("1")) type = "ASSOCIATION";
				else if (type.equals("2")) type = "ELECTRICITY";
				else if (type.equals("3")) type = "WATER";
				else if (type.equals("4")) type = "REPAIRS";
				else if (type.equals("5")) type = "OTHERS";
				
				String month = rs.getString("STATEMENT_MONTH");
				
				if (month == null) month = "";
				else if (month.equals("1")) month = "JANUARY";
				else if (month.equals("2")) month = "FEBUARY";
				else if (month.equals("3")) month = "MARCH";
				else if (month.equals("4")) month = "APRIL";
				else if (month.equals("5")) month = "MAY";
				else if (month.equals("6")) month = "JUNE";
				else if (month.equals("7")) month = "JULY";
				else if (month.equals("8")) month = "AUGUST";
				else if (month.equals("9")) month = "SEPTEMBER";
				else if (month.equals("10")) month = "OCTOBER";
				else if (month.equals("11")) month = "NOVEMBER";
				else if (month.equals("12")) month = "DECEMBER";
				
				String status = rs.getString("STATEMENT_STATUS");
				
				if (status == null) status = "";
				else if (status.equals("0")) status = "FOR PRINTING";
				else if (status.equals("1")) status = "FOR DISTRIBUTION";
				else if (status.equals("2")) status = "FOR RE-PRINTING";
				else if (status.equals("3")) status = "DELIVERED";
				else if (status.equals("4")) status = "PAID";
				else if (status.equals("5")) status = "OVERDUE";
				
				data.addAll(new TableBillingStatement( rs.getString("STATEMENT_ID"),
						"\t" + rs.getString("STATEMENT_NUMBER") ,
						"\t" + rs.getString("TENANT"),
						"\t\t" + type,
						"\t\t" + rs.getString("STATEMENT_DATE"),
						"\t\t" + month,
						"\t\t" + rs.getString("STATEMENT_DUEDATE"),
						"\t\t" + rs.getString("STATEMENT_AMOUNT") ,
						"\t\t" + status,
						rs.getString("REFERENCE_ID"), 
						rs.getString("OCCP_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display billing statement in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void tblBillingStatementView (TableView<TableBillingStatement> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT BILLING_STATEMENT_DETAILS.*, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT "
				+ " FROM BILLING_STATEMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ " WHERE " +condi ;
		
		ObservableList<TableBillingStatement> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){

				String type = rs.getString("STATEMENT_TYPE");
				
				if (type == null) type = "";
				else if (type.equals("0")) type = "RENT/LEASE";
				else if (type.equals("1")) type = "ASSOCIATION";
				else if (type.equals("2")) type = "ELECTRICITY";
				else if (type.equals("3")) type = "WATER";
				else if (type.equals("4")) type = "REPAIRS";
				else if (type.equals("5")) type = "OTHERS";
				
				String month = rs.getString("STATEMENT_MONTH");
				
				if (month == null) month = "";
				else if (month.equals("1")) month = "JANUARY";
				else if (month.equals("2")) month = "FEBUARY";
				else if (month.equals("3")) month = "MARCH";
				else if (month.equals("4")) month = "APRIL";
				else if (month.equals("5")) month = "MAY";
				else if (month.equals("6")) month = "JUNE";
				else if (month.equals("7")) month = "JULY";
				else if (month.equals("8")) month = "AUGUST";
				else if (month.equals("9")) month = "SEPTEMBER";
				else if (month.equals("10")) month = "OCTOBER";
				else if (month.equals("11")) month = "NOVEMBER";
				else if (month.equals("12")) month = "DECEMBER";
				
				String status = rs.getString("STATEMENT_STATUS");
				
				if (status == null) status = "";
				else if (status.equals("0")) status = "FOR PRINTING";
				else if (status.equals("1")) status = "FOR DISTRIBUTION";
				else if (status.equals("2")) status = "FOR RE-PRINTING";
				else if (status.equals("3")) status = "DELIVERED";
				else if (status.equals("4")) status = "PAID";
				else if (status.equals("5")) status = "OVERDUE";
				
				data.addAll(new TableBillingStatement( rs.getString("STATEMENT_ID"),
						rs.getString("STATEMENT_NUMBER") ,
						rs.getString("TENANT"),
						type,
						rs.getString("STATEMENT_DATE"),
						month,
						rs.getString("STATEMENT_DUEDATE"),
						rs.getString("STATEMENT_AMOUNT"),
						status,
						rs.getString("REFERENCE_ID"), 
						rs.getString("OCCP_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display billing statement in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	public void tblRentPayment (TableView<TableRentPayment> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT RENT_PAYMENT_DETAILS.*, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT "
				+ " FROM RENT_PAYMENT_DETAILS "
				+ " LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = RENT_PAYMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND " +condi ;
		
		ObservableList<TableRentPayment> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				String via = rs.getString("PAYMENT_VIA");
				
				if (via == null) via = "";
				else if (via.equals("0")) via = "POST DATED CHECK";
				else if (via.equals("1")) via = "CASH";
				else if (via.equals("2")) via = "BANK DEPOSIT";
				else if (via.equals("3")) via = "OTHERS";
				
				String receipt = rs.getString("PAYMENT_RECEIPT");
				
				if (receipt == null) receipt = "";
				else if (receipt.equals("0")) receipt = "FOR PRINTING";
				else if (receipt.equals("1")) receipt = "FOR DELIVERY";
				else if (receipt.equals("2")) receipt = "ISSUED";
				else if (receipt.equals("3")) receipt = "OTHERS";
				
				data.addAll(new TableRentPayment( rs.getString("PAYMENT_ID"),
						rs.getString("PAYMENT_RECEIPT_NO"),
						rs.getString("TENANT") ,
						rs.getString("STATEMENT_NUMBER"),
						rs.getString("PAYMENT_DATE"),
						rs.getString("PAYMENT_AMOUNT"),
						via,
						receipt,
						rs.getString("REFERENCE_ID"), 
						rs.getString("OCCP_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display rent payment in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void tblContractor (TableView<TableContractor> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM CONTRACTOR_DETAILS WHERE " +condi ;
		
		ObservableList<TableContractor> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				String add, conperson, num, rem;
				
				add = rs.getString("CONTR_ADDRESS");
				if (add == null ) add = "";
				
				conperson = rs.getString("CONTR_CONTACTPERSON");
				if (conperson == null) conperson = "";
				
				num = rs.getString("CONTR_CONTACT_NO");
				if (num == null) num = "";
				
				rem = rs.getString("CONTR_REMARKS");
				if (rem == null) rem = "";
				
				data.addAll(new TableContractor( rs.getString("CONTR_ID"),
						rs.getString("CONTR_NAME") ,
						"\t" + add,
						"\t" + conperson ,
						"\t\t" + num ,
						"\t\t" + rem ,
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display contractor details in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	public void createtracking( String values ) throws SQLException {

		PreparedStatement ps = null;
		
		String insert = "INSERT INTO USER_TRACK_LOG VALUES ( " + values + " )";

			try {
				ps = connect.prepareStatement(insert);
				ps.executeUpdate();

			} catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert tracking : " + e.getMessage());
			} finally {
				ps.close();
			}
		}
	
	public void tblUserTrackLogs (TableView<TableUserTrackLogs> tbl ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT USER_TRACK_LOG.*, USER_PROFILE.USERNAME AS NAME "
				+ "FROM USER_TRACK_LOG "
				+ "LEFT JOIN USER_PROFILE ON USER_PROFILE.USER_PROFILE_ID = USER_TRACK_LOG.USER_PROFILE_ID "
				+ "WHERE USER_TRACK_LOG.USER_PROFILE_ID = "+ Globals.G_Employee_ID +" " ;
		
		ObservableList<TableUserTrackLogs> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				data.addAll(new TableUserTrackLogs( rs.getString("USER_TRACK_LOG_ID"),
						rs.getString("NAME") ,
						rs.getString("DATETIME_ENTRY")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display user logs in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void tblBillingFinal (TableView<TableBillingFinalize> tbl ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM BILLING_STATEMENT_DETAILS WHERE "
				+ "REFERENCE_ID IS NULL AND SYSTEM_ACCOUNT_ID = "+ Globals.G_Employee_ID +" "
						+ "AND FINALIZED_RECORD = 'N' " ;
		
		ObservableList<TableBillingFinalize> data = FXCollections.observableArrayList();
		String type, month;
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				type = rs.getString("STATEMENT_TYPE");
				
				if (type == null) type = "";
				else if (type.equals("0")) type = "RENT/LEASE";
				else if (type.equals("1")) type = "ASSOCIATION";
				else if (type.equals("2")) type = "ELECTRICITY";
				else if (type.equals("3")) type = "WATER";
				else if (type.equals("4")) type = "REPAIRS";
				else if (type.equals("5")) type = "OTHERS";
				
				month = rs.getString("STATEMENT_MONTH");
				
				if (month == null) month = "";
				else if (month.equals("1")) month = "JANUARY";
				else if (month.equals("2")) month = "FEBUARY";
				else if (month.equals("3")) month = "MARCH";
				else if (month.equals("4")) month = "APRIL";
				else if (month.equals("5")) month = "MAY";
				else if (month.equals("6")) month = "JUNE";
				else if (month.equals("7")) month = "JULY";
				else if (month.equals("8")) month = "AUGUST";
				else if (month.equals("9")) month = "SEPTEMBER";
				else if (month.equals("10")) month = "OCTOBER";
				else if (month.equals("11")) month = "NOVEMBER";
				else if (month.equals("12")) month = "DECEMBER";
				
				data.addAll(new TableBillingFinalize( rs.getString("STATEMENT_ID"),
						type,
						month,
						rs.getString("STATEMENT_AMOUNT")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display not yet finalize billings : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void tblBillingVoucher (TableView<TableBilling> tbl, String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM BILLING_STATEMENT_DETAILS WHERE " + condi ;
		
		ObservableList<TableBilling> data = FXCollections.observableArrayList();
		String type, month;
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				type = rs.getString("STATEMENT_TYPE");
				
				if (type == null) type = "";
				else if (type.equals("0")) type = "RENT/LEASE";
				else if (type.equals("1")) type = "ASSOCIATION";
				else if (type.equals("2")) type = "ELECTRICITY";
				else if (type.equals("3")) type = "WATER";
				else if (type.equals("4")) type = "REPAIRS";
				else if (type.equals("5")) type = "OTHERS";
				
				month = rs.getString("STATEMENT_MONTH");
				
				if (month == null) month = "";
				else if (month.equals("1")) month = "JANUARY";
				else if (month.equals("2")) month = "FEBUARY";
				else if (month.equals("3")) month = "MARCH";
				else if (month.equals("4")) month = "APRIL";
				else if (month.equals("5")) month = "MAY";
				else if (month.equals("6")) month = "JUNE";
				else if (month.equals("7")) month = "JULY";
				else if (month.equals("8")) month = "AUGUST";
				else if (month.equals("9")) month = "SEPTEMBER";
				else if (month.equals("10")) month = "OCTOBER";
				else if (month.equals("11")) month = "NOVEMBER";
				else if (month.equals("12")) month = "DECEMBER";
				
				data.addAll(new TableBilling( rs.getString("STATEMENT_ID"),
						type + " - " + month ,
						"",
						rs.getString("STATEMENT_AMOUNT")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display not yet finalize billings : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void cmbDisplay(ComboBox<String> cmb, String col, String colName, String tblName, String condi) throws SQLException{
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String username = "";
		String query = "SELECT "+ col +" FROM "+tblName+" WHERE "+condi+" ";
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				username = rs.getString(colName);
				cmb.getItems().addAll(username);
				
		}}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of "+tblName+": " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void setDisplay(ComboBox<String> cmb, String col, String colName, String tblName, String condi) throws SQLException{
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		String query = "SELECT "+ col +" FROM "+tblName+" WHERE "+condi+" ";
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()){
				cmb.setValue(rs.getString(colName));
				
		}}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display list of "+tblName+": " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public String getIDs( String col, String tblName, String column, String condi) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String id = null ;
		
		String query = "SELECT "+col+" FROM "+ tblName+" WHERE "+condi+" ";
		
		try{
			preparedStatement = connect.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			
			while (rs.next()){
				id = rs.getString(column);
				
		}}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err "+ tblName +" get id : " +e.getMessage());
		}
		finally {
			preparedStatement.close();
			rs.close();
		}
		
		return id;
	}
	
	public String getLastID (String col, String tblname) throws SQLException {
		
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String id = null;
		
		String query = "SELECT "+col+" AS ID FROM "+tblname+" ORDER BY "+col+" DESC LIMIT 1 ";
		
		try{
			preparedStatement = connect.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			while (rs.next()){
				id = rs.getString("ID");	
		}
			}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err get id of last entry of "+tblname+" : " +e.getMessage());
		}
		finally {
			preparedStatement.close();
			rs.close();
		}
		return id;
		}

	public void createContractor(String tblName,  String values ) throws SQLException {

		PreparedStatement ps = null;
		
		String insert = "INSERT INTO "+ tblName +" VALUES ( " + values + " )";

			try {
				ps = connect.prepareStatement(insert);
				ps.executeUpdate();

			} catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert "+ tblName +" : " + e.getMessage());
			} finally {
				ps.close();
			}
			
		}
	
	public void updateRecord (String tblName, String col, String condi) throws SQLException {
		
		PreparedStatement ps = null;
		
		String query = "UPDATE " + tblName + " SET " + col + " WHERE " + condi + " ";

			try {
				ps = connect.prepareStatement(query);
				ps.executeUpdate();

			} catch (Exception e) {
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err edit " + tblName + " : " + e.getMessage());
			} finally {
				ps.close();
			}
		
	}

	public String statementNo () throws SQLException{
		
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String id = null ;
		
		String query = "SELECT * FROM BILLING_STATEMENT_DETAILS WHERE REFERENCE_ID IS NULL "
				+ "ORDER BY STATEMENT_ID DESC LIMIT 1 ";
		
		try{
			preparedStatement = connect.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			
			while (rs.next()){
				id = rs.getString("STATEMENT_NUMBER");
				
		}}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err get last billing statement no : " +e.getMessage());
		}
		finally {
			preparedStatement.close();
			rs.close();
		}
		
		return id;
	}

	public String receiptNo () throws SQLException{
		
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String id = null ;
		
		String query = "SELECT * FROM RENT_PAYMENT_DETAILS WHERE REFERENCE_ID IS NULL "
				+ "ORDER BY PAYMENT_ID DESC LIMIT 1 ";
		
		try{
			preparedStatement = connect.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			
			while (rs.next()){
				id = rs.getString("PAYMENT_RECEIPT_NO");
				
		}}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err get last receipt no : " +e.getMessage());
		}
		finally {
			preparedStatement.close();
			rs.close();
		}
		
		return id;
	}

	public int countSetting (String tblName, String condi) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		
		String query = "SELECT COUNT(*) FROM "+ tblName +" WHERE "+ condi +" ";
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				count = rs.getInt(1);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err count if theres a setting record for "+ tblName +" : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
		
		return count;
	}
	
	public String getSettingID( ) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String id = null ;
		
		String query = "SELECT * FROM COMPANY_SETTINGS WHERE REFERENCE_ID IS NULL ";
		
		try{
			preparedStatement = connect.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			
			while (rs.next()){
				id = rs.getString("SETTING_ID");
				
		}}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err get id of company setting: " +e.getMessage());
		}
		finally {
			preparedStatement.close();
			rs.close();
		}
		
		return id;
	}

	public String totalBilling ( String id) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String billing = "0" ;
		
		String query = "SELECT SUM(BILLING_STATEMENT_DETAILS.STATEMENT_AMOUNT) AS BAL "
				+ " FROM BILLING_STATEMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE UNIT_DETAILS.BLDG_ID = "+Main_Window.dashboard+" AND "
				+ " BILLING_STATEMENT_DETAILS.OCCP_ID = "+ id +" AND BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL ";
		
		try{ 
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
					
				billing = rs.getString("BAL");
				
			}	
		}
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err total billing statement of occupancy "+id+" : " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
		
		return billing;
		
	}

	public String totalPayment ( String id) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String payment = "0" ;
		
		String query = "SELECT SUM(RENT_PAYMENT_DETAILS.PAYMENT_AMOUNT) AS BAL "
				+ " FROM RENT_PAYMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = RENT_PAYMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE UNIT_DETAILS.BLDG_ID = "+Main_Window.dashboard+" AND "
				+ "RENT_PAYMENT_DETAILS.OCCP_ID = "+ id +" AND RENT_PAYMENT_DETAILS.REFERENCE_ID IS NULL ";
		
		try{ 
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
				
			while (rs.next()){	
					
				payment = rs.getString("BAL");

				}
			}	
			catch (Exception e){
				EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err payment for occupancy "+id+": " +e.getMessage());
			}
			finally {
				ps.close();
				rs.close();
			}
		return payment;
		
	}

	public void tblBillingNotification (TableView<TableBillingStatement> tbl ) throws SQLException{
		
	    ObservableList<TableBillingStatement> data = FXCollections.observableArrayList();
	    
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		String occp = null;
		int ic = 0;
		double amount = 0;
		
		String i = "SELECT COUNT(*) FROM BILLING_STATEMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
				+ "UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
				+ "BILLING_STATEMENT_DETAILS.STATEMENT_DUEDATE < NOW() AND "
				+ "BILLING_STATEMENT_DETAILS.STATEMENT_ID IN "
				+ "( SELECT MAX(STATEMENT_ID) FROM BILLING_STATEMENT_DETAILS "
				+ "WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID )  ";
		
		try {
		
			preparedStatement = connect.prepareStatement(i);
			rs = preparedStatement.executeQuery();
			
		while (rs.next()){
	
			ic = rs.getInt(1);
		 
		for (int count = 0; count < ic; count++) {
    		
		    String select = "SELECT BILLING_STATEMENT_DETAILS.* FROM BILLING_STATEMENT_DETAILS "
		    		+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
		    		+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
		    		+ " WHERE BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
		    		+ "UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
		    		+ "BILLING_STATEMENT_DETAILS.STATEMENT_DUEDATE < NOW() AND "
		    		+ "BILLING_STATEMENT_DETAILS.STATEMENT_ID IN ( SELECT MAX(STATEMENT_ID) FROM BILLING_STATEMENT_DETAILS "
		    		+ "WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID ) LIMIT "+count+", 1 "; 
		    
    		try{
    		
    			preparedStatement2 = connect.prepareStatement(select);
    			rs2 = preparedStatement2.executeQuery();
    			
    			while (rs2.next()){
    			
    				 occp = rs2.getString("OCCP_ID");
    		
    			}}
    		
    		catch (Exception e){
    			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing details : " +e.getMessage());
    		}
    		finally {
    			preparedStatement2.close();
    			rs2.close();
    		}
    		
    		String  query = " SELECT BILLING_STATEMENT_DETAILS.*, PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT, "
    				+ " ( SUM(BILLING_STATEMENT_DETAILS.STATEMENT_AMOUNT) - IFNULL(( SELECT SUM(PAYMENT_AMOUNT) FROM RENT_PAYMENT_DETAILS "
    				+ " WHERE REFERENCE_ID IS NULL AND OCCP_ID = "+ occp +" ), 0) ) AS AMOUNT "
    				+ " FROM BILLING_STATEMENT_DETAILS "
    				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
    				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
    				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
    				+ "WHERE BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
    				+ " AND BILLING_STATEMENT_DETAILS.OCCP_ID = "+ occp +"   ";
    			
    		try{
    			preparedStatement3 = connect.prepareStatement(query);
    			rs3 = preparedStatement3.executeQuery();
    			
    			while (rs3.next()){
    				
    				amount = rs3.getDouble("AMOUNT");
    				
    				if (amount > 0) {
    				
    				String type = rs3.getString("STATEMENT_TYPE");
    				
    				if (type == null) type = "";
    				else if (type.equals("0")) type = "RENT/LEASE";
    				else if (type.equals("1")) type = "ASSOCIATION";
    				else if (type.equals("2")) type = "ELECTRICITY";
    				else if (type.equals("3")) type = "WATER";
    				else if (type.equals("4")) type = "REPAIRS";
    				else if (type.equals("5")) type = "OTHERS";
    				
    				String month = rs3.getString("STATEMENT_MONTH");
    				
    				if (month == null) month = "";
    				else if (month.equals("1")) month = "JANUARY";
    				else if (month.equals("2")) month = "FEBUARY";
    				else if (month.equals("3")) month = "MARCH";
    				else if (month.equals("4")) month = "APRIL";
    				else if (month.equals("5")) month = "MAY";
    				else if (month.equals("6")) month = "JUNE";
    				else if (month.equals("7")) month = "JULY";
    				else if (month.equals("8")) month = "AUGUST";
    				else if (month.equals("9")) month = "SEPTEMBER";
    				else if (month.equals("10")) month = "OCTOBER";
    				else if (month.equals("11")) month = "NOVEMBER";
    				else if (month.equals("12")) month = "DECEMBER";
    				
    				String status = rs3.getString("STATEMENT_STATUS");
    				
    				if (status == null) status = "";
    				else if (status.equals("0")) status = "FOR PRINTING";
    				else if (status.equals("1")) status = "FOR DISTRIBUTION";
    				else if (status.equals("2")) status = "FOR RE-PRINTING";
    				else if (status.equals("3")) status = "DELIVERED";
    				else if (status.equals("4")) status = "PAID";
    				else if (status.equals("5")) status = "OVERDUE";
    				
    				data.addAll(new TableBillingStatement( rs3.getString("STATEMENT_ID"),
    						"\t" + rs3.getString("STATEMENT_NUMBER") ,
    						"\t" + rs3.getString("TENANT"),
    						"\t\t" + type,
    						"\t\t" + rs3.getString("STATEMENT_DATE"),
    						"\t\t" + month,
    						"\t\t" + rs3.getString("STATEMENT_DUEDATE"),
    						"\t\t" + rs3.getString("AMOUNT") ,
    						"\t\t" + status,
    						rs3.getString("REFERENCE_ID"), 
    						rs3.getString("OCCP_ID")));
    					
    				}
    				else {
    					
    				}
    		}
    		}
    		catch (Exception e){
    			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err statement billing display : " +e.getMessage());
    		}
    		finally {
    			preparedStatement3.close();
    			rs3.close();
    		}
    		}
			
		}
			tbl.setItems(data);	
		}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err statement details count : " +e.getMessage());
		}
		finally {
			preparedStatement.close();
			rs.close();
		}
		
	}
	
	public int totalBillDue ( ) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		String occp = null;
		int ic = 0;
		int counter = 0;
		double amount = 0;
		
		String i = "SELECT COUNT(*) FROM BILLING_STATEMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
				+ "UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
				+ "BILLING_STATEMENT_DETAILS.STATEMENT_DUEDATE < NOW() AND "
				+ "BILLING_STATEMENT_DETAILS.STATEMENT_ID IN "
				+ "( SELECT MAX(STATEMENT_ID) FROM BILLING_STATEMENT_DETAILS "
				+ "WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID )  ";
		
		try {
		
			preparedStatement = connect.prepareStatement(i);
			rs = preparedStatement.executeQuery();
			
		while (rs.next()){
	
			ic = rs.getInt(1);
		 
		for (int count = 0; count < ic; count++) {
    		
		    String select = "SELECT BILLING_STATEMENT_DETAILS.* FROM BILLING_STATEMENT_DETAILS "
		    		+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
		    		+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
		    		+ " WHERE BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
		    		+ "UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
		    		+ "BILLING_STATEMENT_DETAILS.STATEMENT_DUEDATE < NOW() AND "
		    		+ "BILLING_STATEMENT_DETAILS.STATEMENT_ID IN ( SELECT MAX(STATEMENT_ID) FROM BILLING_STATEMENT_DETAILS "
		    		+ "WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID ) LIMIT "+count+", 1 "; 
		    
    		try{
    		
    			preparedStatement2 = connect.prepareStatement(select);
    			rs2 = preparedStatement2.executeQuery();
    			
    			while (rs2.next()){
    			
    				 occp = rs2.getString("OCCP_ID");
    		
    			}}
    		
    		catch (Exception e){
    			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err billing details : " +e.getMessage());
    		}
    		finally {
    			preparedStatement2.close();
    			rs2.close();
    		}
    		
    		String  query = " SELECT ( SUM(BILLING_STATEMENT_DETAILS.STATEMENT_AMOUNT) - IFNULL(( SELECT SUM(PAYMENT_AMOUNT) FROM RENT_PAYMENT_DETAILS "
    				+ " WHERE REFERENCE_ID IS NULL AND OCCP_ID = "+ occp +" ), 0) ) AS AMOUNT "
    				+ " FROM BILLING_STATEMENT_DETAILS "
    				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
    				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
    				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
    				+ "WHERE BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" "
    				+ " AND BILLING_STATEMENT_DETAILS.OCCP_ID = "+ occp +"   ";
    			
    		try{
    			preparedStatement3 = connect.prepareStatement(query);
    			rs3 = preparedStatement3.executeQuery();
    			
    			while (rs3.next()){
    				
    				amount = rs3.getDouble("AMOUNT")	;
    		}
    			if ( amount > 0) {
    				counter = counter + 1;
    			}
    		}
    		catch (Exception e){
    			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err statement billing display : " +e.getMessage());
    		}
    		finally {
    			preparedStatement3.close();
    			rs3.close();
    		}
    		}
			
		}
		}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err statement details count : " +e.getMessage());
		}
		finally {
			preparedStatement.close();
			rs.close();
		}
		
		return counter;
		
	}
	
	
	/*public int tblBillingNotificationCount ( ) throws SQLException{
		
	    
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int ic = 0;
		
		String i = "SELECT COUNT(*) FROM BILLING_STATEMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND "
				+ "UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "
				+ "BILLING_STATEMENT_DETAILS.STATEMENT_DUEDATE < NOW() AND "
				+ "BILLING_STATEMENT_DETAILS.STATEMENT_ID IN "
				+ "( SELECT MAX(STATEMENT_ID) FROM BILLING_STATEMENT_DETAILS "
				+ "WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID )  ";
		
		try {
		
			preparedStatement = connect.prepareStatement(i);
			rs = preparedStatement.executeQuery();
			
		while (rs.next()){
	
			ic = rs.getInt(1);
		 
			}
		}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err count billing duedates : " +e.getMessage());
		}
		finally {
			preparedStatement.close();
			rs.close();
		}
		
		return ic;
		
	}*/
	
	public void RevenueChart (BarChart<String, Double> chart, String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		XYChart.Series<String, Double> series = new XYChart.Series<>();
		
		String query = "SELECT DATE_FORMAT(RENT_PAYMENT_DETAILS.PAYMENT_DATE, '%M') AS DATE, "
				+ " SUM( RENT_PAYMENT_DETAILS.PAYMENT_AMOUNT ) AS PAYMENT "
				+ " FROM RENT_PAYMENT_DETAILS "
				+ " LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = RENT_PAYMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND " +condi + " "
				+ " GROUP BY MONTH(RENT_PAYMENT_DETAILS.PAYMENT_DATE) " ;
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(2)));
				
			}
			chart.getData().add(series);
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err rent payment chart : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void tblRevenueSummary (TableView<TableSummary> tbl, String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		ObservableList<TableSummary> data = FXCollections.observableArrayList();
				
		String query = "SELECT DATE_FORMAT(RENT_PAYMENT_DETAILS.PAYMENT_DATE, '%M') AS DATE, "
				+ " FORMAT( SUM( RENT_PAYMENT_DETAILS.PAYMENT_AMOUNT ), 2) AS PAYMENT "
				+ " FROM RENT_PAYMENT_DETAILS "
				+ " LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = RENT_PAYMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND " +condi + " "
				+ " GROUP BY MONTH(RENT_PAYMENT_DETAILs.PAYMENT_DATE) " ;
		
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				data.addAll(new TableSummary( rs.getString("DATE"),
						  rs.getString("PAYMENT") ));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err payment summary: " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	public void totalRevenue (TextField txt, String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT FORMAT(SUM( RENT_PAYMENT_DETAILS.PAYMENT_AMOUNT) , 2) AS PAYMENT "
				+ " FROM RENT_PAYMENT_DETAILS "
				+ " LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = RENT_PAYMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " WHERE UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND " +condi  ;
		
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				txt.setText(rs.getString("PAYMENT"));
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err total revenue : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}
	
	/*public void ExpenseChart (BarChart<String, Double> chart, String condi1, String condi2 ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		XYChart.Series<String, Double> series = new XYChart.Series<>();
		
		String query = "SELECT DATE_FORMAT(BLDG_MX_DATE, '%M') AS DATE, BLDG_MX_TOTAL_COST AS COST "
				+ " FROM BUILDING_MAINTENANCE"
				+ " WHERE BLDG_ID = "+ Main_Window.dashboard +" AND " +condi1 + " "
				+ " GROUP BY MONTH(BLDG_MX_DATE) "
				
				+ " UNION ALL "
				+ " SELECT DATE_FORMAT(UNIT_MAINTENANCE.UNIT_MX_DATE, '%M') AS DATE, UNIT_MAINTENANCE.UNIT_MX_TOTAL_COST AS COST "
				+ "FROM UNIT_MAINTENANCE "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = UNIT_MAINTENANCE.UNIT_ID "
				+ "WHERE UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "+ condi2 +" "
				+ "GROUP BY MONTH(UNIT_MX_DATE)  " ;
		
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(2)));
			}
			chart.getData().add(series);
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err expenses from maintenance : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}*/

	public void ExpenseChart (BarChart<String, Double> chart, String condi1 ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		XYChart.Series<String, Double> series = new XYChart.Series<>();
		
		String query = "SELECT DATE_FORMAT(EXPENSE_TXN_DATE, '%M') AS DATE, SUM(EXPENSE_AMOUNT) AS COST "
				+ " FROM EXPENSE_RECORD "
				+ " WHERE BLDG_ID = "+ Main_Window.dashboard +" AND " +condi1 + " "
				+ " GROUP BY MONTH(EXPENSE_TXN_DATE) " ;
		
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				series.getData().add(new XYChart.Data<>(rs.getString(1), rs.getDouble(2)));
			}
			chart.getData().add(series);
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err expenses from EXPENSE RECORD : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void tblExpenseSummary (TableView<TableSummary> tbl, String condi1 ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		ObservableList<TableSummary> data = FXCollections.observableArrayList();
			
		String query = "SELECT DATE_FORMAT(EXPENSE_TXN_DATE, '%M') AS DATE, FORMAT(SUM(EXPENSE_AMOUNT), 2) AS COST "
				+ " FROM EXPENSE_RECORD "
				+ " WHERE BLDG_ID = "+ Main_Window.dashboard +" AND " +condi1 + " "
				+ " GROUP BY MONTH(EXPENSE_TXN_DATE) " ;
		
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){

				data.addAll(new TableSummary( rs.getString("DATE"),
						  rs.getString("COST") ));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err expenses from EXPENSE RECORD : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void totalExpense (TextField txt, String condi  ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String exp = null;
		
		String query = "SELECT FORMAT(SUM(EXPENSE_AMOUNT),2) AS EXPENSE "
				+ " FROM EXPENSE_RECORD "
				+ " WHERE BLDG_ID = "+ Main_Window.dashboard +" AND " +condi  ;
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				exp = rs.getString("EXPENSE");
				
			}
		}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err total expenses from expense record : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}

		if (exp == null) {
			txt.setText("");
		}
		
		else {

			txt.setText(exp);

		}
		
		
	}
	
	/*public void totalExpense (TextField txt, String condi1, String condi2 ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		
		String bldg = "", unit = "";
		
		String query = "SELECT SUM(BLDG_MX_TOTAL_COST) AS BLDGCOST  "
				+ " FROM BUILDING_MAINTENANCE"
				+ " WHERE BLDG_ID = "+ Main_Window.dashboard +" AND " +condi1  ;
		
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				bldg = rs.getString("BLDGCOST");
				
			}
		}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err total expenses from bldg maintenance : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}

		String query1 = "SELECT SUM(UNIT_MAINTENANCE.UNIT_MX_TOTAL_COST) AS UNITCOST "
				+ "FROM UNIT_MAINTENANCE "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = UNIT_MAINTENANCE.UNIT_ID "
				+ "WHERE UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND "+ condi2 +"  " ;
		
		
		try{
			ps1 = connect.prepareStatement(query1);
			rs1 = ps1.executeQuery();
			while (rs1.next()){
				
				unit = rs1.getString("UNITCOST");
				
			}
		}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err total expenses from unit maintenance : " +e.getMessage());
		}
		finally {
			ps1.close();
			rs1.close();
		}
		
		if (bldg == null) {
			bldg = "0";
		}
		if (unit == null) {
			unit = "0";
		}
		
		Double total = Double.parseDouble(bldg) + Double.parseDouble(unit);
		
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		
		if (total == .00) 
		{
			txt.setText("");
		}
		else {
			txt.setText(formatter.format(total));
		}
			
	}*/
	
	public void tblExpense (TableView<TableExpense> tbl,  String condi ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT EXPENSE_RECORD.*, CONTRACTOR_DETAILS.CONTR_NAME AS CONTR "
				+ " FROM EXPENSE_RECORD "
				+ "LEFT JOIN CONTRACTOR_DETAILS ON CONTRACTOR_DETAILS.CONTR_ID = EXPENSE_RECORD.CONTRACTOR_ID "
				+ " WHERE EXPENSE_RECORD.BLDG_ID = "+ Main_Window.dashboard +" AND " +condi ;
		
		ObservableList<TableExpense> data = FXCollections.observableArrayList();
		
		try{
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()){
				
				String coa = rs.getString("EXPENSE_COA");
				
				if (coa == null) coa = " ";
				else if (coa.equals("0")) coa = "OTHER COST";
				else if (coa.equals("1")) coa = "MANAGEMENT FEES";
				else if (coa.equals("2")) coa = "AGENT RENT COLLECTION FEES";
				else if (coa.equals("3")) coa = "INSURANCE";
				else if (coa.equals("4")) coa = "COST OF ADVERTISING";
				else if (coa.equals("5")) coa = "COST OF UTILITIES";
				else if (coa.equals("6")) coa = "PROVISION FOR DEPRECIATION";
				else if (coa.equals("7")) coa = "COST OF MAINTENANCE";
				else if (coa.equals("8")) coa = "COST OF REPAIRS AND REPLACEMENT";
				
				String date = rs.getString("EXPENSE_TXN_DATE");
				
				if (date == null) date = "";
				
				data.addAll(new TableExpense( rs.getString("EXPENSE_ID"),
						"\t" + rs.getString("EXPENSE_REF") ,
						"\t" + rs.getString("CONTR"),
						"\t\t" + date ,
						"\t" + coa,
						"\t\t" + rs.getString("EXPENSE_DESCRIPTION"),
						"\t\t" + rs.getString("EXPENSE_AMOUNT"),
						rs.getString("REFERENCE_ID")));
				
				tbl.setItems(data);
			}
		}
		
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err display EXPENSE in table : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
	}

	public void unitPrice (ComboBox<String> cmb, TextField txtPrice, TextField txtArea, TextField txtMax ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String query = "SELECT *, FORMAT(UNIT_TYPE_PRICE, 2) AS PRICE FROM UNIT_TYPE "
				+ "WHERE REFERENCE_ID IS NULL AND BLDG_ID = "+ Main_Window.dashboard+" "
						+ "AND UNIT_TYPE_NAME = '"+ cmb.getValue() +"' ";
		
		try {
		
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			
		while (rs.next()){
	
			txtPrice.setText(rs.getString("PRICE"));
			txtArea.setText(rs.getString("UNIT_TYPE_AREA"));
			txtMax.setText(rs.getString("UNIT_TYPE_MAX_CAPACITY"));
		 
			}
		}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err get price of "+ cmb.getValue()+" : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
		
		
	}

	public void occpPrice (ComboBox<String> cmb, TextField txtPrice, ComboBox<String> cmbDelivery ) throws SQLException {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		String delivery;
		
		String query = "SELECT *, FORMAT(UNIT_RENT_AMOUNT, 2) AS PRICE FROM UNIT_DETAILS "
				+ "WHERE REFERENCE_ID IS NULL AND BLDG_ID = "+ Main_Window.dashboard+" "
						+ "AND UNIT_NAME = '"+ cmb.getValue() +"' ";
		
		try {
		
			ps = connect.prepareStatement(query);
			rs = ps.executeQuery();
			
		while (rs.next()){
	
			delivery = rs.getString("UNIT_FEATURES");
			
			if (delivery == null) delivery = "";
			else if (delivery.equals("0")) delivery = "BARE";
			else if (delivery.equals("1")) delivery = "PARTIALLY FURNISHED";
			else if (delivery.equals("2")) delivery = "FURNISHED";
			else if (delivery.equals("3")) delivery = "FULLY FURNISHED";
			
			txtPrice.setText(rs.getString("PRICE"));
			cmbDelivery.setValue(delivery);
		 
			}
		}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err get price of "+ cmb.getValue()+" : " +e.getMessage());
		}
		finally {
			ps.close();
			rs.close();
		}
		
	}

	public String getOccupant( String occp ) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String tenant = null;
		
		String query = "SELECT BILLING_STATEMENT_DETAILS.*, "
				+ "PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT "
				+ " FROM BILLING_STATEMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ " WHERE BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND BILLING_STATEMENT_DETAILS.OCCP_ID = "+ occp +" "
				+ " AND BILLING_STATEMENT_DETAILS.STATEMENT_ID IN ( SELECT MAX(STATEMENT_ID) FROM BILLING_STATEMENT_DETAILS "
				+ " WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID ) ";
		
		try{
			preparedStatement = connect.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			
			while (rs.next()){
				tenant = rs.getString("TENANT");
				
		}}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err get id of company setting: " +e.getMessage());
		}
		finally {
			preparedStatement.close();
			rs.close();
		}
		
		return tenant;
	}

	public String getStatement( String occp ) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		String statement = null;
		
		String query = "SELECT BILLING_STATEMENT_DETAILS.*, "
				+ "PRIMARY_TENANT_DETAILS.TENANT_NAME AS TENANT "
				+ " FROM BILLING_STATEMENT_DETAILS "
				+ "LEFT JOIN OCCUPANCY_DETAILS ON OCCUPANCY_DETAILS.OCCP_ID = BILLING_STATEMENT_DETAILS.OCCP_ID "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ " WHERE BILLING_STATEMENT_DETAILS.REFERENCE_ID IS NULL AND BILLING_STATEMENT_DETAILS.OCCP_ID = "+ occp +" "
				+ " AND BILLING_STATEMENT_DETAILS.STATEMENT_ID IN ( SELECT MAX(STATEMENT_ID) FROM BILLING_STATEMENT_DETAILS "
				+ " WHERE REFERENCE_ID IS NULL GROUP BY OCCP_ID ) ";
		
		try{
			preparedStatement = connect.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			
			while (rs.next()){
				statement = rs.getString("STATEMENT_NUMBER");
				
		}}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err get id of company setting: " +e.getMessage());
		}
		finally {
			preparedStatement.close();
			rs.close();
		}
		
		return statement;
	}

	public void updateOccupancy( ) throws SQLException {

		PreparedStatement ps = null;
		
		String query = "UPDATE OCCUPANCY_DETAILS "
				+ "LEFT JOIN PRIMARY_TENANT_DETAILS ON PRIMARY_TENANT_DETAILS.TENANT_ID = OCCUPANCY_DETAILS.TENANT_ID "
				+ "LEFT JOIN UNIT_DETAILS ON UNIT_DETAILS.UNIT_ID = OCCUPANCY_DETAILS.UNIT_ID "
				+ " SET OCCP_STATUS = 3 "
				+ "WHERE UNIT_DETAILS.BLDG_ID = "+ Main_Window.dashboard +" AND OCCUPANCY_DETAILS.REFERENCE_ID IS NULL "
				+ "AND OCCUPANCY_DETAILS.OCCP_END <= NOW() ";
		
		try {
			ps = connect.prepareStatement(query);
			ps.executeUpdate();

		} catch (Exception e) {
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err update occupancy status to end of contract : " + e.getMessage());
		} 
		finally {
			ps.close();
		}
	}

	public int occpDue ( ) throws SQLException{
		
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		int counter = 0;
		
		String i = "SELECT COUNT(*) FROM OCCUPANCY_DETAILS WHERE REFERENCE_ID IS NULL AND "
				+ "OCCP_END < NOW() + INTERVAL 60 DAY  ";
		
		try {
		
			preparedStatement = connect.prepareStatement(i);
			rs = preparedStatement.executeQuery();
			
			while (rs.next()){
	
				counter = rs.getInt(1);
		 
			}
		}
		catch (Exception e){
			EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err count occupancy near to end : " +e.getMessage());
		}
		finally {
			preparedStatement.close();
			rs.close();
		}
		
		return counter;
		
	}

	
}
