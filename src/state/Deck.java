package state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class stores the current state of the deck.
 * 
 * @author Aaron Tetens
 */
class Deck {

	// it is very important to remove entries whose value is zero
	private final Map<String, Integer> cards;

	Deck() {
		this.cards = new HashMap<>();
		this.cards.put("T", 14);
		this.cards.put("S", 14);
		this.cards.put("D", 14);
		this.cards.put("2M", 12);
		this.cards.put("3M", 8);
		this.cards.put("1M", 6);
		this.cards.put("2N", 10);
		this.cards.put("3N", 5);
		this.cards.put("1N", 5);
		this.cards.put("P", 10);
		this.cards.put("W", 6);
		this.cards.put("C", 4);
	}

	Deck(final Deck deck) {
		this.cards = new HashMap<>(deck.cards);
	}

	/**
	 * This method removes a card from the deck at random and returns the removed
	 * card.
	 * 
	 * @return The randomly drawn card
	 */
	String drawRandomCard() {
		final List<String> cardList = new ArrayList<>();

		for (final Map.Entry<String, Integer> entry : this.cards.entrySet()) {
			final String card = entry.getKey();
			final Integer count = entry.getValue();

			for (int i = 0; i < count; i++) {
				cardList.add(card);
			}
		}

		final int randomIndex = (int) (Math.random() * cardList.size());
		return cardList.remove(randomIndex);
	}

	/**
	 * @param card
	 *            The card to remove from the deck
	 * @throws IllegalArgumentException
	 *             If the given card is not in the deck
	 */
	void drawCard(final String card) throws IllegalArgumentException {
		if (!this.cards.containsKey(card)) {
			throw new IllegalArgumentException("Tried to draw " + card + " from the deck (not present)");
		}

		this.cards.put(card, this.cards.get(card) - 1);

		if (this.cards.get(card) == 0) {
			this.cards.remove(card);
		}
	}

	@Override
	public String toString() {
		return this.cards.toString();
	}
}
