package state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import api.GameState;
import score.Scoring;

/**
 * This class stores information about the current game state in a way that is
 * compatible with MCTS.
 * 
 * @author Aaron Tetens
 */
public class SushiGoState implements GameState {

	private static final int AI_INDEX = 0;

	// maps number of players to number of cards dealt to each player
	private static final Map<Integer, Integer> NUM_CARDS_PER_PLAYER = new HashMap<>();
	static {
		NUM_CARDS_PER_PLAYER.put(2, 10);
		NUM_CARDS_PER_PLAYER.put(3, 9);
		NUM_CARDS_PER_PLAYER.put(4, 8);
		NUM_CARDS_PER_PLAYER.put(5, 7);
	}

	// index 0 represents the ai player
	private final Player[] players;

	private final Deck deck;

	private int currentRound;

	/**
	 * @param numPlayers
	 *            The number of players, should be 2-5
	 * @param in
	 *            The scanner the game should use to read in the cards that the AI
	 *            receives
	 * @throws IllegalArgumentException
	 *             If numPlayers or aiPlayer is not in the correct range
	 */
	public SushiGoState(final int numPlayers, final Scanner in) throws IllegalArgumentException {
		if (numPlayers < 2 || numPlayers > 5) {
			throw new IllegalArgumentException("Tried to start a game with " + numPlayers + " players (2-5 required)");
		}

		this.players = new Player[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			this.players[i] = new Player();
		}

		this.deck = new Deck();

		this.currentRound = 1;

		this.dealFromInput(in);
	}

	private SushiGoState(final SushiGoState state) {
		this.players = new Player[state.players.length];
		for (int i = 0; i < this.players.length; i++) {
			this.players[i] = new Player(state.players[i]);
		}

		this.deck = new Deck(state.deck);

		this.currentRound = state.currentRound;
	}

	/**
	 * This method reads and performs the moves that the humans make against the AI.
	 * 
	 * @param in
	 *            The Scanner that this method uses to read the human moves
	 */
	public void getHumanPlayersMoves(final Scanner in) {
		for (int i = 1; i < this.players.length; i++) {
			String cards = null;
			boolean tryAgain = true;

			while (tryAgain) {
				tryAgain = false;

				System.out.print("What did player " + i + " play? ");
				cards = in.nextLine().toUpperCase();

				try {
					this.makeMove(cards, i, false, in);
				} catch (final IllegalArgumentException e) {
					tryAgain = true;
				}
			}

		}
	}

	/**
	 * @param in
	 *            The Scanner that this method should use to read what cards we
	 *            received if any were previously unknown
	 */
	private void rotateHands(final Scanner in) {
		for (int i = 1; i < this.players.length; i++) {
			final List<String> temp = new ArrayList<>(this.players[AI_INDEX].getHand());
			this.players[AI_INDEX].replaceHand(this.players[i].getHand());
			this.players[i].replaceHand(temp);
		}

		while (this.players[AI_INDEX].getHand().contains("?")) {
			System.out.print("Unknown card: ");
			final String newCard = in.nextLine().toUpperCase();
			this.players[AI_INDEX].replaceUnknownCard(newCard);
		}
	}

	/**
	 * This method makes the current player put down the specified card
	 * 
	 * @param cardsPlayed
	 *            The cards to play
	 * @param player
	 *            The index of the player playing the card
	 * @param dealRandomly
	 *            Whether or not the deck should be dealt randomly in the event of a
	 *            deal occurring after the end of the round
	 * @param in
	 *            The Scanner that this method should use to obtain which cards the
	 *            AI drew in the event of a deal occurring after the end of the
	 *            round (this value doesn't matter if dealRandomly is true), or if
	 *            the hands get rotated and we receive cards that were previously
	 *            unknown
	 * @throws IllegalArgumentException
	 *             If the provided card does not match any of the valid cards
	 */
	private void makeMove(final String cardsPlayed, final int playerIndex, final boolean dealRandomly, final Scanner in)
			throws IllegalArgumentException {
		final String[] cards = cardsPlayed.split(" ");

		try {
			this.players[playerIndex].playCards(cards);
		} catch (final IllegalArgumentException e) {
			throw e;
		}

		// if everyone has played a card, rotate the hands
		boolean rotateHands = true;

		final int numCardsInHand = this.players[AI_INDEX].getNumCardsInHand();
		for (int i = 1; i < this.players.length; i++) {
			if (this.players[i].getNumCardsInHand() > numCardsInHand) {
				rotateHands = false;
				break;
			}
		}

		if (rotateHands) {
			this.rotateHands(in);
		}

		// if the round is not over, then we are done
		if (!this.isRoundOver()) {
			return;
		}

		// if the round is over, update the score and prepare the
		// next round if there is one
		Scoring.updateScores(this.players, this.currentRound);

		if (this.currentRound < 3) {
			this.currentRound++;

			for (final Player player : this.players) {
				player.clearField();
			}

			if (dealRandomly) {
				this.dealRandomly();
			} else {
				this.dealFromInput(in);
			}
		}
	}

	/**
	 * @return Whether or not the current round is over
	 */
	private boolean isRoundOver() {
		for (final Player player : this.players) {
			if (!player.isHandEmpty()) {
				return false;
			}
		}

		return true;
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
		final int numCardsPerPlayer = NUM_CARDS_PER_PLAYER.get(this.players.length);

		// give the AI its cards first
		for (int i = 1; i <= numCardsPerPlayer; i++) {
			String card = null;
			boolean tryAgain = true;

			while (tryAgain) {
				tryAgain = false;

				System.out.print("Card " + i + ": ");
				card = in.nextLine().toUpperCase();

				try {
					this.deck.drawCard(card);
				} catch (final IllegalArgumentException e) {
					System.out.println(e.getMessage());
					tryAgain = true;
					continue;
				}

				this.players[AI_INDEX].addCardToHand(card);
			}
		}

		// deal unknown cards to the rest of the players
		for (int i = 1; i < this.players.length; i++) {
			this.players[i].addUnknownCardsToHand(numCardsPerPlayer);
		}
	}

	/**
	 * This method deals the cards to the players randomly (used for simulation).
	 */
	private void dealRandomly() {
		for (final Player player : this.players) {
			while (player.getNumCardsInHand() < NUM_CARDS_PER_PLAYER.get(this.players.length)) {
				player.addCardToHand(this.deck.drawRandomCard());
			}
		}
	}

	@Override
	public int getLastPlayer() {
		// every turn occurs simultaneously, so we can tell MCTS that it is always the
		// AI's turn (this is as true as saying that it is always any other player's
		// turn)
		return AI_INDEX;
	}

	@Override
	public List<GameState> getNextStates() {
		final List<GameState> nextStates = new ArrayList<>();

		// we cannot simulate further if we have already played a card since we do not
		// know what our opponents have played
		if (this.players[AI_INDEX].getNumCardsInHand() < this.players[AI_INDEX + 1].getNumCardsInHand()) {
			return nextStates;
		}

		// generate a state for each possible play

		// one card plays
		for (final String card : this.players[AI_INDEX].getHand()) {
			final SushiGoState nextState = new SushiGoState(this);

			try {
				// the round can't end in this method, so the values of dealRandomly and in
				// don't matter
				nextState.makeMove(card, AI_INDEX, false, null);
			} catch (final IllegalArgumentException e) {
				System.out.println("Error during next state generation");
				System.out.println(e.getMessage());
				System.exit(1);
			}

			nextStates.add(nextState);
		}

		// two card plays (if we have chopsticks)
		// the ordering of i and j matter because of wasabi/nigiri orderings
		if (this.players[AI_INDEX].getField().contains("C")) {
			for (int i = 0; i < this.players[AI_INDEX].getNumCardsInHand(); i++) {
				for (int j = 0; j < this.players[AI_INDEX].getNumCardsInHand(); j++) {
					if (i != j) {
						final String card1 = this.players[AI_INDEX].getHand().get(i);
						final String card2 = this.players[AI_INDEX].getHand().get(j);

						final SushiGoState nextState = new SushiGoState(this);

						try {
							// the round can't end in this method, so the values of dealRandomly and in
							// don't matter
							nextState.makeMove(card1 + " " + card2, AI_INDEX, true, null);
						} catch (final IllegalArgumentException e) {
							System.out.println("Error during next state generation");
							System.out.println(e.getMessage());
							System.exit(1);
						}

						nextStates.add(nextState);
					}
				}
			}
		}

		return nextStates;
	}

	@Override
	public GameState getRandomNextState() {
		final SushiGoState copy = new SushiGoState(this);

		// if this is the start of the simulation, randomly replace unknown cards with
		// cards from the deck and make each human player do a random play
		if (copy.players[AI_INDEX].getNumCardsInHand() < copy.players[AI_INDEX + 1].getNumCardsInHand()) {
			for (final Player player : copy.players) {
				while (player.getHand().contains("?")) {
					player.replaceUnknownCard(copy.deck.drawRandomCard());
				}
			}

			for (int i = 1; i < copy.players.length; i++) {
				copy.doRandomPlay(i);
			}

			return copy;
		}

		// if this is not the start of the simulation, make a random move for all
		// players
		for (int i = 0; i < copy.players.length; i++) {
			copy.doRandomPlay(i);
		}

		return copy;
	}

	/**
	 * @param player
	 *            The player to perform the random move
	 */
	private void doRandomPlay(final int player) {
		final List<String> plays = new ArrayList<>();

		// one card plays
		plays.addAll(this.players[player].getHand());

		// two card plays (only available if player has chopsticks in their field)
		// the ordering of i and j matter because of wasabi/nigiri orderings
		if (this.players[player].getField().contains("C")) {
			for (int i = 0; i < this.players[player].getNumCardsInHand(); i++) {
				for (int j = 0; j < this.players[player].getNumCardsInHand(); j++) {
					if (i != j) {
						final String card1 = this.players[player].getHand().get(i);
						final String card2 = this.players[player].getHand().get(j);

						plays.add(card1 + " " + card2);
					}
				}
			}
		}

		final int randomIndex = (int) (Math.random() * plays.size());

		try {
			this.makeMove(plays.get(randomIndex), player, true, null);
		} catch (final IllegalArgumentException e) {
			System.out.println("Error during simulation");
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}

	@Override
	public List<Integer> getWinningPlayers() {
		final List<Integer> winningPlayers = new ArrayList<>();

		if (this.currentRound < 3 || !this.players[AI_INDEX].isHandEmpty()) {
			return winningPlayers;
		}

		int scoreOfBest = -1;
		int numPuddingsOfBest = -1;

		for (int i = 0; i < this.players.length; i++) {
			final int score = this.players[i].getScore();
			final int numPuddings = this.players[i].getNumPuddings();

			if (score > scoreOfBest) {
				winningPlayers.clear();
				winningPlayers.add(i);

				scoreOfBest = score;
				numPuddingsOfBest = numPuddings;
			} else if (score == scoreOfBest) {
				if (numPuddings > numPuddingsOfBest) {
					winningPlayers.clear();
					winningPlayers.add(i);

					numPuddingsOfBest = numPuddings;
				} else if (numPuddings == numPuddingsOfBest) {
					winningPlayers.add(i);
				}
			}
		}

		return winningPlayers;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("Current round = " + this.currentRound);
		sb.append("\nDeck = " + this.deck);

		for (int i = 0; i < this.players.length; i++) {
			sb.append("\n===============================================");
			sb.append("\nPlayer " + i + ":\n" + this.players[i]);
		}

		return sb.toString();
	}
}
