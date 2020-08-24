package com.niemiec.controllers;

import com.niemiec.view.ExitWindowView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class ExitWindowController {

	@FXML
	private static Stage stage;

	private static ExitWindowView exitClass;

	public static void setStage(Stage stage) {
		ExitWindowController.stage = stage;
	}

	public static void setExitClass(ExitWindowView exit2) {
		ExitWindowController.exitClass = exit2;
	}

	@FXML
	void exit(ActionEvent event) {
		exitClass.dontSave();
		System.exit(0);
	}

	@FXML
	void noExit(ActionEvent event) {
		ExitWindowController.stage.close();
	}

	@FXML
	void saveAndExit(ActionEvent event) {
		exitClass.saveGame();
		System.exit(0);
	}

}
