package com.niemiec.logic;

import java.io.Serializable;

import com.niemiec.objects.Player;
import com.niemiec.objects.PlayerImpl;

import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;

//przechowuje cały stan o aktualej grze
//gdy zaczynamy grę na nowo to MainScreenController tworzy nowy obiekt

@SuppressWarnings("serial")
public class GameLogic implements Serializable {
	private boolean saveGame;
	private boolean automaticallySpacingOfShips;
	private boolean theGameWasStarted;
	private PlayerImpl realPlayer;
	private PlayerImpl virtualPlayer;
	private AddShips addShips;
	private ShotShip shotShip;

	public GameLogic(VBox myBorder, VBox opponentBorder) {
		this.automaticallySpacingOfShips = false;
		this.saveGame = false;
	}
	
	public void startGameAfterSave() {
		if (automaticallySpacingOfShips) {
			BorderManagement.setBordersToStartShot();
		} else if (!addShips.areShipsAdded()) {
			BorderManagement.startNewGameWithVirtualPlayer();
		}
		BorderManagement.drawBoardInMyBorder(realPlayer);
		BorderManagement.drawOpponentBoardInOpponentBorder(realPlayer);
	}

	public void startNewGameWithVirtualPlayer() {
		this.theGameWasStarted = false;
		this.addShips = new AddShips();
		this.shotShip = new ShotShip();
		createPlayers();
		addShips.addPlayers(realPlayer, virtualPlayer);
		BorderManagement.startNewGameWithVirtualPlayer();
		shotShip.setInitialData(realPlayer, virtualPlayer);
		addShips.addShipsAutomatically(Player.VIRTUAL_PLAYER);
		if (automaticallySpacingOfShips) {
			this.theGameWasStarted = true;
			addShips.addShipsAutomatically(Player.REAL_PLAYER);
			BorderManagement.setBordersToStartShot();
			BorderManagement.drawBoardInMyBorder(realPlayer);
			shotShip.firstShotInTheGame();
		}
	}

	private void createPlayers() {
		realPlayer = new PlayerImpl(Player.REAL_PLAYER);
		virtualPlayer = new PlayerImpl(Player.VIRTUAL_PLAYER);
	}

	public void addShips(ActionEvent event) {
		this.theGameWasStarted = true;
		if (addShips.addShipsManually(Player.REAL_PLAYER, event)) {
			BorderManagement.setBordersToStartShot();
			shotShip.firstShotInTheGame();
		}
		BorderManagement.drawBoardInMyBorder(realPlayer);
	}

	public void shot(ActionEvent event) {
		if (shotShip.shot(event)) {
			String winner = shotShip.getWinnerName();
			// okienko kto wygrywa i pytanie czy zaczynamy od nowa
			System.out.println("Wygrywa gracz: " + winner);
			BorderManagement.setBordersToEndGame();
		}
	}

	public void setAutomaticallySpacingOfShips(boolean b) {
		this.automaticallySpacingOfShips = b;
	}

	public boolean getAutomaticallySpacingOfShips() {
		return automaticallySpacingOfShips;
	}
	
	public boolean getSaveGame() {
		return this.saveGame;
	}
	
	public void setSaveGame(boolean saveGame) {
		this.saveGame = saveGame;
	}
	
	public boolean getTheGameWasStarted() {
		return this.theGameWasStarted;
	}
}
