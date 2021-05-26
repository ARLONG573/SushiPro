package state;

import java.util.List;
import java.util.Scanner;

import api.GameState;

/**
 * This class stores information about the current game state in a way that is
 * compatible with MCTS.
 * 
 * @author Aaron Tetens
 */
public class SushiGoState implements GameState {

	private final Player[] players;
	private final Deck deck;
	private final int aiPlayer;

	private int lastPlayer;
	private int currentPlayer;
	private int currentRound;

	/**
	 * @param numPlayers
	 *            The number of players, should be 2-5
	 * @param aiPlayer
	 *            Which player is the AI, should be 0-[numPlayers-1]
	 * @param in
	 *            The scanner the game should use to read in the cards that the AI
	 *            receives
	 * @throws IllegalArgumentException
	 *             If numPlayers or aiPlayer is not in the correct range
	 */
	public SushiGoState(final int numPlayers, final int aiPlayer, final Scanner in) throws IllegalArgumentException {
		if (numPlayers < 2 || numPlayers > 5) {
			throw new IllegalArgumentException("Tried to start a game with " + numPlayers + " players (2-5 required)");
		}

		if (aiPlayer < 0 || aiPlayer >= numPlayers) {
			throw new IllegalArgumentException("Tried to make the AI player " + aiPlayer + " in a " + numPlayers
					+ "-player game (0-" + (numPlayers - 1) + " required)");
		}

		this.players = new Player[numPlayers];
		this.deck = new Deck();
		this.aiPlayer = aiPlayer;

		this.lastPlayer = -1;
		this.currentPlayer = 0;
		this.currentRound = 1;

		this.dealFromInput(in);
	}

	/**
	 * This method makes the current player put down the specified card
	 * 
	 * @param cardPlayed
	 *            The card to play
	 * @param dealRandomly
	 *            Whether or not the deck should be dealt randomly in the event of a
	 *            deal occurring after the end of the round
	 * @param in
	 *            The Scanner that this method should use to obtain which cards the
	 *            AI drew in the event of a deal occurring after the end of the
	 *            round (this value doesn't matter if dealRandomly is true
	 * @throws IllegalArgumentException
	 *             If the provided card does not match any of the valid cards
	 */
	public void makeMove(final String cardPlayed, final boolean dealRandomly, final Scanner in)
			throws IllegalArgumentException {

	}

	/**
	 * This method deals cards from the deck to each player. The number of cards
	 * that each player receives depends on the number of players in the game.
	 * 
	 * @param in
	 *            The Scanner that this method should use to obtain which cards the
	 *            AI drew
	 */
	private void dealFromInput(final Scanner in) {

	}

	/**
	 * This method deals the cards to the players randomly (used for simulation).
	 */
	private void dealRandomly() {

	}

	@Override
	public int getLastPlayer() {
		return this.lastPlayer;
	}

	@Override
	public List<GameState> getNextStates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameState getRandomNextState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getWinningPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

}
