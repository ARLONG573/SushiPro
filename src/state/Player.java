package state;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class stores all of the information regarding a single player (hand,
 * field, and score). A question mark is used to denote an unknown card during
 * gameplay (these question marks get replaced with random cards from the deck
 * during simulation).
 * 
 * @author Aaron Tetens
 */
public class Player {

	private final List<String> hand;

	// the field is kept in the order the cards are played to keep track of
	// wasabi/nigiri combos
	private final List<String> field;

	private int score;

	Player() {
		this.hand = new ArrayList<>();
		this.field = new ArrayList<>();

		this.score = 0;
	}

	/**
	 * @param score
	 *            The score to add to the current score
	 */
	public void addScore(final int score) {
		this.score += score;
	}

	/**
	 * Remove all cards from the field that are not puddings
	 */
	void clearField() {
		final int numPuddings = this.getNumPuddings();

		this.field.clear();

		for (int i = 0; i < numPuddings; i++) {
			this.field.add("P");
		}
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
	 * Plays the given cards from the player's hand to their field.
	 * 
	 * @param cards
	 *            The cards to play
	 * @throws IllegalArgumentException
	 *             If the given set of cards is invalid
	 */
	void playCards(final String[] cards) throws IllegalArgumentException {
		if (cards.length < 1 || cards.length > 2) {
			throw new IllegalArgumentException("Tried to play " + cards.length + " cards at once (1-2 required)");
		}

		// if one card, just play it
		if (cards.length == 1) {
			try {
				this.playSingleCard(cards[0]);
			} catch (final IllegalArgumentException e) {
				throw e;
			}
		} else {
			// if two cards, it is required that we have chopsticks in our field
			if (!this.field.contains("C")) {
				throw new IllegalArgumentException("Tried to play 2 cards without chopsticks in the field");
			}

			// this is needed to revert the first card placement in case the second one
			// throws an exception
			boolean wasFirstCardKnown = this.hand.contains(cards[0]);

			try {
				this.playSingleCard(cards[0]);
			} catch (final IllegalArgumentException e) {
				throw e;
			}

			try {
				this.playSingleCard(cards[1]);
			} catch (final IllegalArgumentException e) {
				// revert the first card played
				this.field.remove(cards[0]);
				this.hand.add(wasFirstCardKnown ? cards[0] : "?");

				throw e;
			}

			// put chopsticks back in hand
			this.field.remove("C");
			this.hand.add("C");
		}
	}

	/**
	 * @param cardToPlay
	 *            The single card to play
	 * @throws IllegalArgumentException
	 *             If the provided card is not validO
	 */
	private void playSingleCard(final String cardToPlay) throws IllegalArgumentException {
		// first check to see if the given card is known to be in our hand
		for (final String card : this.hand) {
			if (card.equals(cardToPlay)) {
				this.hand.remove(cardToPlay);
				this.field.add(cardToPlay);
				return;
			}
		}

		// next check to see if the given card may be any of the unknown cards
		// TODO it is possible for the user to put in a card that is not theoretically
		// possible to play due to all of them being seen already...it is okay for now
		// as long as the user provides correct inputs
		for (final String card : this.hand) {
			if (card.equals("?")) {
				this.hand.remove(card);
				this.field.add(cardToPlay);
				return;
			}
		}

		// if both of the above failed, the input was no good
		throw new IllegalArgumentException("Tried to play " + cardToPlay + " (not a card)");
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
	public int getNumPuddings() {
		int numPuddings = 0;

		for (final String card : this.field) {
			if (card.equals("P")) {
				numPuddings++;
			}
		}

		return numPuddings;
	}

	/**
	 * @return The amount of maki in the field
	 */
	public int getNumMaki() {
		int numMaki = 0;
		for (final String card : this.field) {
			if (Pattern.matches("{123}M", card)) {
				numMaki += Integer.parseInt(card.substring(0, 1));
			}
		}

		return numMaki;
	}

	/**
	 * @return This player's field
	 */
	public List<String> getField() {
		return this.field;
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
