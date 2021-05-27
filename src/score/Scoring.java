package score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
	}

	public static void updateScores(final Player[] players, final int currentRound) {
		// score non-contest cards for each player
		for (int i = 0; i < players.length; i++) {
			int numWasabi = 0;
			int numSashimi = 0;
			int numTempura = 0;
			int numDumplings = 0;
			int score = 0;

			for (final String card : players[i].getField()) {
				if (card.equals("W")) {
					numWasabi++;
				} else if (Pattern.matches("[123]N", card)) {
					final int nigiriValue = Integer.parseInt(card.substring(0, 1));

					if (numWasabi > 0) {
						score += nigiriValue * 3;
						numWasabi--;
					} else {
						score += nigiriValue;
					}
				} else if (card.equals("S")) {
					numSashimi++;
				} else if (card.equals("T")) {
					numTempura++;
				} else if (card.equals("D")) {
					numDumplings++;
				}
			}

			score += 10 * (numSashimi / 3);
			score += 5 * (numTempura / 2);
			score += 15 * (numDumplings / 5) + DUMPLING_SCORES.get(numDumplings % 5);

			players[i].addScore(score);
		}

		// score maki
		int mostMaki = 1;
		int secondMostMaki = 1;
		final List<Player> firstMakiWinners = new ArrayList<>();
		final List<Player> secondMakiWinners = new ArrayList<>();

		for (final Player player : players) {
			final int numMaki = player.getNumMaki();

			if (numMaki > mostMaki) {
				mostMaki = numMaki;

				secondMakiWinners.clear();
				secondMakiWinners.addAll(firstMakiWinners);
				firstMakiWinners.clear();
				firstMakiWinners.add(player);
			} else if (numMaki == mostMaki) {
				firstMakiWinners.add(player);
			} else if (numMaki > secondMostMaki) {
				secondMostMaki = numMaki;

				secondMakiWinners.clear();
				secondMakiWinners.add(player);
			} else if (numMaki == secondMostMaki) {
				secondMakiWinners.add(player);
			}
		}

		if (firstMakiWinners.size() == 1) {
			firstMakiWinners.get(0).addScore(6);

			if (!secondMakiWinners.isEmpty()) {
				for (final Player secondMakiWinner : secondMakiWinners) {
					secondMakiWinner.addScore(3 / secondMakiWinners.size());
				}
			}
		} else if (firstMakiWinners.size() > 1) {
			for (final Player firstMakiWinner : firstMakiWinners) {
				firstMakiWinner.addScore(6 / firstMakiWinners.size());
			}
		}

		// if this is the last round, score pudding
		if (currentRound == 3) {
			int mostPuddings = 0;
			int leastPuddings = Integer.MAX_VALUE;
			final List<Player> mostPuddingsPlayers = new ArrayList<>();
			final List<Player> leastPuddingsPlayers = new ArrayList<>();

			for (final Player player : players) {
				final int numPuddings = player.getNumPuddings();

				if (numPuddings > mostPuddings) {
					mostPuddings = numPuddings;

					mostPuddingsPlayers.clear();
					mostPuddingsPlayers.add(player);
				} else if (numPuddings == mostPuddings) {
					mostPuddingsPlayers.add(player);
				}

				if (numPuddings < leastPuddings) {
					leastPuddings = numPuddings;

					leastPuddingsPlayers.clear();
					leastPuddingsPlayers.add(player);
				} else if (numPuddings == leastPuddings) {
					leastPuddingsPlayers.add(player);
				}
			}

			for (final Player player : mostPuddingsPlayers) {
				player.addScore(6 / mostPuddingsPlayers.size());
			}

			for (final Player player : leastPuddingsPlayers) {
				player.addScore(-6 / leastPuddingsPlayers.size());
			}
		}
	}
}
