package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Installation_Code implements Initializable {

	@FXML private TextField txtCode;
	@FXML private Label lblMessage;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		lblMessage.setVisible(false);
		
	}

	public void enter (ActionEvent event) {
		if (txtCode.getText().equals(Main.code)) {
			lblMessage.setVisible(true);
			lblMessage.setText("correct");
		}
		else {
			lblMessage.setVisible(true);
			lblMessage.setText("error");
		}
	}
	
}
