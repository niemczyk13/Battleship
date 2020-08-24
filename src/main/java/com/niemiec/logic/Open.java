package com.niemiec.logic;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Open {
	
	public static GameLogic downloadDataFromSaveFile() {
		GameLogic gameLogic = null;
		try (ObjectInputStream obj = new ObjectInputStream(new FileInputStream("serial"))) {
			gameLogic = (GameLogic) obj.readObject();
		} catch (Exception e) {
			
		}
		
		return gameLogic;
	}
}
