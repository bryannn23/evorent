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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextFormatter;

public class Change_PIN_Code implements Initializable {

	public DBconnection dbcon = new DBconnection();
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	public Modular_Class modular = new Modular_Class();

	@FXML PasswordField txtOldPIN;
	@FXML PasswordField txtNewPIN;
	@FXML PasswordField txtConfirmPIN;

	@FXML Label lblOldPIN;
	@FXML Label lblNewPIN;
	@FXML Label lblConfirmPIN;
	@FXML Label lblMessage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		lblOldPIN.setVisible(false);
		lblNewPIN.setVisible(false);
		lblConfirmPIN.setVisible(false);
		lblMessage.setVisible(false);
		
		TextFormatter<String> tfpin = modular.getTextFlexiFormatter(7, 'a');
		txtNewPIN.setTextFormatter(tfpin);
		TextFormatter<String> tfcpin = modular.getTextFlexiFormatter(7, 'a');
		txtConfirmPIN.setTextFormatter(tfcpin);
		
	}

	public void changePIN(ActionEvent event) throws SQLException {

		String query = "SELECT * FROM USER_PROFILE WHERE PIN_CODE = '" + txtOldPIN.getText()
				+ "' AND USER_PROFILE_ID = " + Globals.G_Employee_ID + " AND REFERENCE_ID IS NULL ";

		ps = dbcon.connect.prepareStatement(query);
		rs = ps.executeQuery();

		if (rs.next()) {
			
			if (txtNewPIN.getText().equals(txtConfirmPIN.getText())) {
						
				String update = "UPDATE USER_PROFILE SET PIN_CODE = '"+txtNewPIN.getText().replaceAll("'", "`")+"' WHERE USER_PROFILE_ID = "
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
						lblNewPIN.setVisible(true);
						lblConfirmPIN.setVisible(true);
						lblMessage.setVisible(true);
						lblMessage.setText("Password and confirm password do not match!");
						
					}
				} 
				
		
		else {
		
			lblOldPIN.setVisible(true);
			lblMessage.setVisible(true);
			lblMessage.setText("Password is incorrect. ");
			
		}
	}

}
