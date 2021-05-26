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

	Player() {
		this.hand = new ArrayList<>();
		this.field = new ArrayList<>();

		this.score = 0;
	}
}
