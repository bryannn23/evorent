	package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Log_In implements Initializable{
	
	public DBconnection dbcon = new DBconnection();
	//public modular module = new modular();
	
	@FXML private Label lblStatus;
	@FXML private Label lblUn;
	@FXML private Label lblPw;
	@FXML private TextField txtUsername;
	@FXML private TextField txtPassword;
	@FXML private Button btnlogin;
	
	int max_attempts = 10;
	IntegerProperty attempts = new SimpleIntegerProperty();

	private NumberFormat amountFormat;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		
		lblUn.setVisible(false);
		lblPw.setVisible(false);
		
		attempts.addListener((ob, ov, nv) -> {
			if (max_attempts == nv.intValue()){
				txtUsername.setDisable(true);
				txtPassword.setDisable(true);
				btnlogin.setDisable(true);
				lblStatus.setText("Account has been deactivated!");
			}
		});
	
		txtPassword.setOnKeyPressed(e -> {
			
			if (e.getCode() == KeyCode.ENTER){
				try {	
					
					 if ((txtUsername.getText().isEmpty()) || (txtPassword.getText().isEmpty()))
					{
						
						 if (txtUsername.getText().isEmpty()) lblUn.setVisible(true);
						 else lblUn.setVisible(false);
						
						 if (txtPassword.getText().isEmpty()) lblPw.setVisible(true); 
						 else lblPw.setVisible(false);
						
						 lblStatus.setText("Field required!");
					}	
					 else if (dbcon.islogin(txtUsername.getText() , txtPassword.getText()))
					{
						if (Globals.G_Employee_Status == 0) {
							lblStatus.setText("Account is DEACTIVATED!");
							
						}
						else if (Globals.G_Employee_Status == 2) { 
							  lblStatus.setText("Account is FREEZE!");
							 
						  }
						else if (Globals.G_Employee_Status == 1) {
							
							if (Globals.G_Employee_Access_ID == Globals.G_MEMBER_RIGHTS)
							{
								try {
									dbcon.createtracking( " 0, "+ Globals.G_Employee_ID +" , 'SUCCESSFUL LOG IN', now(), "+Globals.G_Employee_ID+" " );
								} catch (SQLException e1) {
									EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert tracking : " + e1.getMessage());
								}
								
								((Node)e.getSource()).getScene().getWindow().hide();
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
							
							else {
								
								try {
									dbcon.createtracking( " 0, "+ Globals.G_Employee_ID +" , 'SUCCESSFUL LOG IN', now(), "+Globals.G_Employee_ID+" " );
								} catch (SQLException e1) {
									EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert tracking : " + e1.getMessage());
								}
								
								((Node)e.getSource()).getScene().getWindow().hide();
								Stage primaryStage = new Stage();
								
								Parent root = FXMLLoader.load(getClass().getResource("/application/PINCode.fxml"));
								Scene scene = new Scene(root);
								
								scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
								primaryStage.setScene(scene);
								primaryStage.setResizable(false);
								
								primaryStage.setTitle("EvoRent V1.1.0");
								
								primaryStage.show();
							}
						}
					}
					else {
						
						lblStatus.setText("Invalid username/password.");
						}
					attempts.set(attempts.add(1).get());
					}
				catch (SQLException ex) {
					EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err Login sql : " +ex.getMessage());
				} 
				catch (IOException ex) {
					EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Error login io : " +ex.getMessage());
					System.out.println(ex.getMessage());
				}	
			}
		});
	}

	
	
	public void loginc(ActionEvent event) throws SQLException, IOException{
		
		if ((txtUsername.getText().isEmpty()) || (txtPassword.getText().isEmpty()))
		{
			
			 if (txtUsername.getText().isEmpty()) lblUn.setVisible(true);
			 else lblUn.setVisible(false);
			
			 if (txtPassword.getText().isEmpty()) lblPw.setVisible(true); 
			 else lblPw.setVisible(false);
			
			 lblStatus.setText("Field required!");
		}
	 
		else if (dbcon.islogin(txtUsername.getText() , txtPassword.getText()))
		{

			  if (Globals.G_Employee_Status == 0) {
				  lblStatus.setText("Account is DEACTIVATED!");
				
			  }
			  if (Globals.G_Employee_Status == 2) {
				  lblStatus.setText("Account is FREEZE!");
				  
			  }
			  if (Globals.G_Employee_Status == 1) {
				  
					if (Globals.G_Employee_Access_ID == Globals.G_MEMBER_RIGHTS)
					{
						try {
							dbcon.createtracking( " 0, "+ Globals.G_Employee_ID +" , 'SUCCESSFUL LOG IN', now(), "+Globals.G_Employee_ID+" " );
						} catch (SQLException e1) {
							EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert tracking : " + e1.getMessage());
						}
						
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
					
					else {
						
						try {
							dbcon.createtracking( " 0, "+ Globals.G_Employee_ID +" , 'SUCCESSFUL LOG IN', now(), "+Globals.G_Employee_ID+" " );
						} catch (SQLException e1) {
							EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err insert tracking : " + e1.getMessage());
						}
						
						((Node)event.getSource()).getScene().getWindow().hide();
						Stage primaryStage = new Stage();
						
						Parent root = FXMLLoader.load(getClass().getResource("/application/PINCode.fxml"));
						Scene scene = new Scene(root);
						scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
						primaryStage.setScene(scene);
						primaryStage.setResizable(false);
						
						primaryStage.setTitle("EvoRent V1.1.0");
						
						primaryStage.show();
					}
			  }
			}
		
		else {
			
			
			lblStatus.setText("Invalid username/password.");
			}
		attempts.set(attempts.add(1).get());
			}
}

