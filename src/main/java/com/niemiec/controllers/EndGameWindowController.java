package com.niemiec.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EndGameWindowController {
	@FXML
	private Label info;
	
	@FXML
	void exit(ActionEvent event) {
		System.exit(0);
	}

	public Label getInfo() {
		return info;
	}

	public void setInfo(String text) {
		info.setText(text);
	}
	
}
