package com.niemiec.view;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.niemiec.controllers.ExitWindowController;
import com.niemiec.logic.GameLogic;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ExitWindowView {
	private GameLogic gameLogic;

	public ExitWindowView(GameLogic gameLogic) {
		this.gameLogic = gameLogic;
	}

	public void orClose() {
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ExitWindow.fxml"));

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
		dialog.setTitle("Wyjście");
		ExitWindowController.setStage(dialog);
		ExitWindowController.setExitClass(this);

		dialog.show();
	}

	public void saveGame() {
		gameLogic.setSaveGame(true);
		save();
	}

	public void dontSave() {
		gameLogic.setSaveGame(false);
		save();
	}

	private void save() {
		try (ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream("serial"))) {
			obj.writeObject(gameLogic);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
