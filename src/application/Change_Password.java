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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextFormatter;

public class Change_Password implements Initializable {

	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	public Modular_Class modular = new Modular_Class();

	@FXML PasswordField txtOldPassword;
	@FXML PasswordField txtNewPassword;
	@FXML PasswordField txtConfirmPassword;

	@FXML Label lblOldPassword;
	@FXML Label lblNewPassword;
	@FXML Label lblConfirmPassword;
	@FXML Label lblMessage;

	@FXML Button btnChangePassword;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		lblOldPassword.setVisible(false);
		lblNewPassword.setVisible(false);
		lblConfirmPassword.setVisible(false);
		lblMessage.setVisible(false);
		
		TextFormatter<String> tfnew = modular.getTextFlexiFormatter(15, 'a'); 
		txtNewPassword.setTextFormatter(tfnew);
		TextFormatter<String> tfcnew = modular.getTextFlexiFormatter(15, 'a'); 
		txtConfirmPassword.setTextFormatter(tfcnew);
		
	}

	public void changepass(ActionEvent event) throws SQLException {

		String query = "SELECT * FROM USER_PROFILE WHERE PASSWORD = '" + txtOldPassword.getText()
				+ "' AND USER_PROFILE_ID = " + Globals.G_Employee_ID + " AND REFERENCE_ID IS NULL ";

		ps = dbcon.connect.prepareStatement(query);
		rs = ps.executeQuery();

		if (rs.next()) {
			
			if (!txtNewPassword.getText().isEmpty() && !txtConfirmPassword.getText().isEmpty()) {
				
				if ((txtNewPassword.getText().length() >= 5) && (txtConfirmPassword.getText().length() >= 5)) {
					
					if (txtNewPassword.getText().equals(txtConfirmPassword.getText())) {
						
						String update = "UPDATE USER_PROFILE SET PASSWORD = '"+txtNewPassword.getText().replaceAll("'", "`")+"' WHERE USER_PROFILE_ID = "
								+ Globals.G_Employee_ID + " AND REFERENCE_ID IS NULL ";

						try {
							ps = dbcon.connect.prepareStatement(update);
							ps.executeUpdate();

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Information");
							alert.setHeaderText(null);
							alert.setContentText("You have successfully changed your password.");
							alert.showAndWait();
							((Node) event.getSource()).getScene().getWindow().hide();

						} catch (Exception e) {
							EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err USER PASS UPDATE : " + e.getMessage());
						} finally {
							ps.close();
						}
						
					} 
					else {
						lblNewPassword.setVisible(true);
						lblConfirmPassword.setVisible(true);
						lblMessage.setVisible(true);
						lblMessage.setText("Password and confirm password do not match!");
						
					}
				} 
				else {
					
					if (txtNewPassword.getText().length() < 5)
						lblNewPassword.setVisible(true);
					else
						lblNewPassword.setVisible(false);
					
					if (txtConfirmPassword.getText().length() < 5)
						lblConfirmPassword.setVisible(true);
					else
						lblConfirmPassword.setVisible(false);
					
					lblMessage.setVisible(true);
					lblMessage.setText("Username or password must have at least 5 or more characters.");
					
				}
			} 
			else {
				
				if (txtNewPassword.getText().isEmpty())
					lblNewPassword.setVisible(true);
				else
					lblNewPassword.setVisible(false);
				
				if (txtConfirmPassword.getText().isEmpty())
					lblConfirmPassword.setVisible(true);
				else
					lblConfirmPassword.setVisible(false);
				
				lblMessage.setVisible(true);
				lblMessage.setText("Field required!");
					
			}
		}
		else {
		
			lblOldPassword.setVisible(true);
			lblMessage.setVisible(true);
			lblMessage.setText("Password is incorrect. ");
			
		}
	}

}
