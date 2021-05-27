package state;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all of the information regarding a single player (hand,
 * field, and score). A question mark is used to denote an unknown card during
 * gameplay (these question marks get replaced with random cards from the deck
 * during simulation).
 * 
 * @author Aaron Tetens
 */
class Player {

	private final List<String> hand;
	private final List<String> field;

	private int score;
	private int numPuddings;

	Player() {
		this.hand = new ArrayList<>();
		this.field = new ArrayList<>();

		this.score = 0;
		this.numPuddings = 0;
	}

	/**
	 * @param numUnknownCards
	 *            The number of unknown cards to add to this player's hand
	 */
	void addUnknownCardsToHand(final int numUnknownCards) {
		for (int i = 0; i < numUnknownCards; i++) {
			this.addCardToHand("?");
		}
	}

	/**
	 * This method assumes that the given card is valid.
	 * 
	 * @param card
	 *            The card to add to this player's hand
	 */
	void addCardToHand(final String card) {
		this.hand.add(card);
	}

	/**
	 * @return Whether or not this player has no cards in their hand
	 */
	boolean isHandEmpty() {
		return this.hand.isEmpty();
	}

	/**
	 * @return This player's current score
	 */
	int getScore() {
		return this.score;
	}

	/**
	 * @return The number of puddings this player has
	 */
	int getNumPuddings() {
		return this.numPuddings;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();

		sb.append("Score = " + this.score);
		sb.append("\nHand = " + this.hand);
		sb.append("\nField = " + this.field);

		return sb.toString();
	}
}
