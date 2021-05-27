package score;

import java.util.HashMap;
import java.util.Map;

import state.Player;

/**
 * This class contains a static util method to help update the scores at the end
 * of each round.
 * 
 * @author Aaron Tetens
 */
public class Scoring {

	private final static Map<Integer, Integer> DUMPLING_SCORES = new HashMap<>();
	static {
		DUMPLING_SCORES.put(0, 0);
		DUMPLING_SCORES.put(1, 1);
		DUMPLING_SCORES.put(2, 3);
		DUMPLING_SCORES.put(3, 6);
		DUMPLING_SCORES.put(4, 10);
		DUMPLING_SCORES.put(5, 15);
	}

	public static void updateScores(final Player[] players, final int currentRound) {

	}
}
