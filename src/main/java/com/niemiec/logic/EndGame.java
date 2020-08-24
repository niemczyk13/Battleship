package com.niemiec.logic;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EndGame {
	public void endGame(String player) {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/EndGameWindow.fxml"));
	
		VBox vbox = null;
		
		try {
			vbox = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		Stage dialog = new Stage(); 
		dialog.initStyle(StageStyle.UTILITY); 
		Scene scene = new Scene(vbox); 
		dialog.setScene(scene);
		dialog.setTitle("END GAME");
//		Label label = 
	}
}
