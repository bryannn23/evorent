package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class PIN_code implements Initializable {

	public DBconnection dbcon = new DBconnection();
	
	@FXML private Label lblMessage;
	@FXML private Label lblPINcode;

	@FXML private TextField txtPINcode;
	String captcha;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		lblMessage.setVisible(false);
		lblPINcode.setVisible(false);

		for (int i = 0; i <1; i++) {
			captcha = createCaptchaValue();
			//lblMessage.setVisible(true);
			//lblMessage.setText(createCaptchaValue());
		}
		
		txtPINcode.setOnKeyPressed( e-> {
			
			if (e.getCode() == KeyCode.ENTER){
				
				if (!txtPINcode.getText().isEmpty()) {
					
					try {
						if (dbcon.pinCode(txtPINcode.getText()) == true )
						{
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
							primaryStage.setTitle("EvoFlux V1.1.0");
							primaryStage.show();
						}
						else {
							lblMessage.setVisible(true);
							lblMessage.setText("Invalid PIN code.");
						}
					} catch (SQLException e1) {
						EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err check pin code : " + e1.getMessage());
					} catch (IOException e1) {
						EvoLOG.writeLogLn(EvoLOG.CRITICAL, "Err check pin code : " + e1.getMessage());
					}
				}
				else {
					lblPINcode.setVisible(true);
					lblMessage.setVisible(true);
					
				}
			}
		});
		
	}

	public String createCaptchaValue () {
		
		Random random = new Random();
		int length = 7 + (Math.abs(random.nextInt()) % 3);
		StringBuffer cb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			
			int base = Math.abs(random.nextInt() % 62);
			int num = 0;
			if (base < 26) {
				num = 65 + base;
			}
			else if (base < 52) {
					num = 97 + (base - 26);
			}
			else {
				num = 48 + (base - 52);
			}
			cb.append((char)num);
			
		}
		return cb.toString();
		
	}
	
	public void pin (ActionEvent event) throws SQLException, IOException {
		
		//if (txtPINcode.getText().equals(lblMessage.getText() )) {
		//	lblMessage.setText("correct");
			
		if (!txtPINcode.getText().isEmpty()) {
			
			if (dbcon.pinCode(txtPINcode.getText()) == true )
			{
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
				primaryStage.setTitle("EvoFlux V1.1.0");
				primaryStage.show();
			}
			else {
				lblMessage.setVisible(true);
				lblMessage.setText("Invalid PIN code.");
			}
		}
		else {
			lblPINcode.setVisible(true);
			lblMessage.setVisible(true);
			
		}
		/*}
		else {
			lblPINcode.setVisible(true);
			lblMessage.setVisible(true);
			lblMessage.setText("WRONG!");
		}*/
	}
	
}
