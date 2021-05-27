import java.util.Scanner;

import state.SushiGoState;

/**
 * This class is the driver for the SushiPro program. It takes in user input to
 * continue the game play loop.
 * 
 * @author Aaron Tetens
 */
public class SushiProMain {

	public static void main(final String[] args) {
		final Scanner in = new Scanner(System.in);

		int numPlayers = 0;
		SushiGoState state = null;
		boolean tryAgain = true;

		// game setup
		while (tryAgain) {
			tryAgain = false;
			System.out.print("How many players? ");

			try {
				numPlayers = Integer.parseInt(in.nextLine());
			} catch (final NumberFormatException e) {
				System.out.println("Please enter a number from 2-5");
				tryAgain = true;
				continue;
			}

			try {
				state = new SushiGoState(numPlayers, in);
			} catch (final IllegalArgumentException e) {
				System.out.println(e.getMessage());
				tryAgain = true;
			}
		}

		System.out.println(state);

		// gameplay loop
		while (state.getWinningPlayers().isEmpty()) {
			System.out.println("AI is thinking...");
			state = (SushiGoState) MCTS.search(state, 10, 1);
			System.out.println(state);
			state.getHumanPlayersMoves(in);
		}

		in.close();

		System.out.println("Winning players = " + state.getWinningPlayers());
	}
}
