package com.niemiec.view;

import java.io.IOException;

import com.niemiec.controllers.EndGameWindowController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class EndGameWindowView {
	private EndGameWindowController controller;
	private String winner;

	public EndGameWindowView(String winner) {
		this.winner = winner;
	}

	public void view() {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/EndGameWindow.fxml"));

		VBox vbox = null;

		try {
			vbox = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Stage dialog = new Stage();
		dialog.initStyle(StageStyle.UTILITY);
		Scene scene = new Scene(vbox);
		dialog.setScene(scene);
		dialog.setTitle("Koniec gry");
		
		controller = loader.getController();
		controller.setInfo(winner);
		dialog.show();
	}
}
