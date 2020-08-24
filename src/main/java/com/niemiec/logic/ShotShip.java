package com.niemiec.logic;

import java.io.Serializable;
import java.util.Random;

import com.niemiec.data.check.CheckData;
import com.niemiec.data.create.automatically.CreatorAutomaticallyShotData;
import com.niemiec.objects.Board;
import com.niemiec.objects.Coordinates;
import com.niemiec.objects.Player;
import com.niemiec.objects.PlayerImpl;
import com.niemiec.objects.Ship;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;

@SuppressWarnings("serial")
public class ShotShip implements Serializable  {
	private CreatorAutomaticallyShotData creatorAutomaticallyShotData;
	private boolean gameEnd;
	private int winner;
	private PlayerImpl[] players;

	public void setInitialData(PlayerImpl realPlayer, PlayerImpl virtualPlayer) {
		this.players = new PlayerImpl[2];
		players[Player.REAL_PLAYER] = realPlayer;
		players[Player.VIRTUAL_PLAYER] = virtualPlayer;
		this.gameEnd = false;
		this.winner = -1;
		this.creatorAutomaticallyShotData = new CreatorAutomaticallyShotData(players);
	}

	public ShotShip() {
	}

	public boolean shot(ActionEvent event) {
		if (!shotRealPlayer(event) && !gameEnd) {
			shotVirtualPlayer();
		}
		return gameEnd;
	}

	private boolean shotRealPlayer(ActionEvent event) {
		Coordinates coordinates = CheckData.getCoordinatesFromButton((Button) event.getSource());
		return shotMast(coordinates, Player.REAL_PLAYER);
	}

	private void shotVirtualPlayer() {
		while (true) {
			Coordinates coordinates = creatorAutomaticallyShotData.downloadShotFromVirtualPlayer(Player.VIRTUAL_PLAYER);
			if (!shotMast(coordinates, Player.VIRTUAL_PLAYER) || gameEnd)
				break;
		}
	}

	private boolean shotMast(Coordinates coordinates, int activePlayer) {

		if (checkWithTheOpponentBoardIfWasHit(coordinates, activePlayer)) {
			BorderManagement.drawOpponentBoardInOpponentBorder(players[Player.REAL_PLAYER]);
			BorderManagement.drawBoardInMyBorder(players[Player.REAL_PLAYER]);
			return true;
		}
		BorderManagement.drawOpponentBoardInOpponentBorder(players[Player.REAL_PLAYER]);
		BorderManagement.drawBoardInMyBorder(players[Player.REAL_PLAYER]);

		return false;
	}

	private boolean checkWithTheOpponentBoardIfWasHit(Coordinates coordinates, int activePlayer) {
		int opponentPlayer = getIndexOpponentPlayer(activePlayer);
		int box = getBoxFromPlayerBoard(coordinates, opponentPlayer);

		switch (box) {
		case Board.BOX_EMPTY:
			setBoxInToOpponentBoard(coordinates, Board.BOX_NOT_HIT, activePlayer);
			setBoxInToPlayerBoard(coordinates, Board.BOX_NOT_HIT, opponentPlayer);
			return false;
		case Board.BOX_SHIP:
			actionAfterHit(coordinates, activePlayer);
			return true;
		}
		return true;
	}

	private void actionAfterHit(Coordinates coordinates, int activePlayer) {
		int opponentPlayer = getIndexOpponentPlayer(activePlayer);
		shipWasHit(opponentPlayer, coordinates);
		if (shipWasSunk(opponentPlayer, coordinates)) {
			change3To5AndInsert1Around(activePlayer, coordinates);
			ifActivePlayerIsVirtualResetTheHitData(activePlayer);
			players[opponentPlayer].increaseSunkenShips();
			checkIfWeHaveTheEndOfTheGame(activePlayer);
		} else {
			setBoxInToOpponentBoard(coordinates, Board.BOX_HIT, activePlayer);
			setBoxInToPlayerBoard(coordinates, Board.BOX_HIT, opponentPlayer);
			ifActivePlayerIsVirtualSetHitData(coordinates, activePlayer);
		}

	}

	private void checkIfWeHaveTheEndOfTheGame(int activePlayer) {
		int opponentPlayer = getIndexOpponentPlayer(activePlayer);
		if (players[opponentPlayer].getSunkenShips() == 10) {
			gameEnd = true;
			winner = activePlayer;
		}
	}

	private void shipWasHit(int opponentPlayer, Coordinates coordinates) {
		players[opponentPlayer].getCollectionShips().shipWasHit(coordinates);

	}

	private boolean shipWasSunk(int opponentPlayer, Coordinates coordinates) {
		return players[opponentPlayer].getCollectionShips().checkIfShipIsSunk(coordinates);
	}

	private void change3To5AndInsert1Around(int activePlayer, Coordinates coordinates) {
		insetr1AroundHitShip(activePlayer, coordinates);
		check3To5InBoard(activePlayer, coordinates);		
	}

	private void insetr1AroundHitShip(int activePlayer, Coordinates coordinates) {
		int opponentPlayer = getIndexOpponentPlayer(activePlayer);
		Ship ship = players[opponentPlayer].getCollectionShips().getShip(coordinates);
		for (int i = 1; i <= ship.getNumberOfMasts(); i++) {
			Coordinates c = ship.getCoordinates(i);
			insert1AroundMast(c, activePlayer, opponentPlayer);
		}
	}

	private void insert1AroundMast(Coordinates c, int activePlayer, int opponentPlayer) {
		for (int changeX = -1; changeX < 2; changeX++) {
			for (int changeY = -1; changeY < 2; changeY++) {
				if (CheckData.checkIfWithinThePlayingField(c, changeX, changeY)) {
					Coordinates cWithChangeXY = new Coordinates(c, changeX, changeY);
					insertOne(cWithChangeXY, activePlayer, opponentPlayer);
				}
			}
		}
	}

	private void insertOne(Coordinates cWithChangeXY, int activePlayer, int opponentPlayer) {
		int box = getBoxFromPlayerBoard(cWithChangeXY, opponentPlayer);
		if (box != Board.BOX_NOT_HIT && box != Board.BOX_HIT) {
			setBoxInToPlayerBoard(cWithChangeXY, Board.BOX_NOT_HIT, opponentPlayer);
			setBoxInToOpponentBoard(cWithChangeXY, Board.BOX_NOT_HIT, activePlayer);
		}
	}

	private void check3To5InBoard(int activePlayer, Coordinates coordinates) {
		int opponentPlayer = getIndexOpponentPlayer(activePlayer);
		Ship ship = players[opponentPlayer].getCollectionShips().getShip(coordinates);
		for (int i = 1; i <= ship.getNumberOfMasts(); i++) {
			Coordinates c = ship.getCoordinates(i);
			setBoxInToOpponentBoard(c, Board.BOX_SUNK, activePlayer);
			setBoxInToPlayerBoard(c, Board.BOX_SUNK, opponentPlayer);
		}
	}

	private void ifActivePlayerIsVirtualResetTheHitData(int activePlayer) {
		if (players[activePlayer].getInformationInThePlayerIsVirtual()) {
			PlayerImpl player = (PlayerImpl) players[activePlayer];
			player.resetHitData();
		}
	}

	private void ifActivePlayerIsVirtualSetHitData(Coordinates coordinates, int activePlayer) {
		if (players[activePlayer].getInformationInThePlayerIsVirtual()) {
			PlayerImpl player = (PlayerImpl) players[activePlayer];
			player.setHitData(coordinates);
		}
	}

	private int getIndexOpponentPlayer(int activePlayer) {
		return (activePlayer == Player.REAL_PLAYER) ? Player.VIRTUAL_PLAYER : Player.REAL_PLAYER;
	}

	private void setBoxInToPlayerBoard(Coordinates coordinates, int box, int typeOfPlayer) {
		players[typeOfPlayer].getBoard().setBox(coordinates, box);
	}

	private void setBoxInToOpponentBoard(Coordinates coordinates, int box, int typeOfPlayer) {
		players[typeOfPlayer].getOpponentBoard().setBox(coordinates, box);
	}

	private int getBoxFromPlayerBoard(Coordinates coordinates, int typeOfPlayer) {
		return players[typeOfPlayer].getBoard().getBox(coordinates);
	}

	public void firstShotInTheGame() {
		int r = randomStart();

		if (r == Player.VIRTUAL_PLAYER) {
			shotVirtualPlayer();
		}
	}

	private int randomStart() {
		Random random = new Random();
		return random.nextInt(2);
	}

	public String getWinnerName() {
		if (winner == Player.REAL_PLAYER)
			return "RealPlayer";
		else
			return "VirtualPlayer";
	}
}
