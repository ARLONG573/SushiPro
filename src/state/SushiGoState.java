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

	private int lastPlayer;
	private int currentPlayer;
	private int currentRound;

	public SushiGoState(final int numPlayers, final Scanner in) {
		this.players = new Player[numPlayers];
		this.deck = new Deck();

		this.lastPlayer = -1;
		this.currentPlayer = 0;
		this.currentRound = 1;

		this.deal(in);
	}

	/**
	 * This method makes the current player put down the specified card
	 * 
	 * @param cardPlayed
	 *            The card to play
	 * @param in
	 *            The Scanner that this method should use to obtain which cards the
	 *            AI drew in the event of a deal occurring after the end of the
	 *            round
	 * @throws IllegalArgumentException
	 *             If the provided card does not match any of the valid cards
	 */
	public void makeMove(final String cardPlayed, final Scanner in) throws IllegalArgumentException {

	}

	/**
	 * This method deals cards from the deck to each player. The number of cards
	 * that each player receives depends on the number of players in the game.
	 * 
	 * @param in
	 *            The Scanner that this method should use to obtain which cards the
	 *            AI drew
	 */
	private void deal(final Scanner in) {

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
