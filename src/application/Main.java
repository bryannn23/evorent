package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static String code = "try";
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		try {
		Globals.setDB_Location("evo-config.txt");
		primaryStage.setResizable(false);
		

		Parent root = FXMLLoader.load(getClass().getResource("/application/LogIn.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("PopUpStyle_A.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		
		primaryStage.setTitle("EvoRent V1.1.0");
		
		primaryStage.show();
		
	} catch(Exception e) {
		e.printStackTrace();
	}
	}
	
	public static void main(String[] args) {
		EvoLOG.initializeLog();
		launch(args);
	}

}
