package ca.mcgill.ecse223.block.application;

import ca.mcgill.ecse223.block.model.Block223;
import ca.mcgill.ecse223.block.model.Game;
import ca.mcgill.ecse223.block.model.PlayedGame;
import ca.mcgill.ecse223.block.model.UserRole;
import ca.mcgill.ecse223.block.persistence.Block223Persistence;
import ca.mcgill.ecse223.block.view.LoginView;

public class Block223Application {

	private static UserRole currentUserRole = null;
	private static Game currentGame = null;
	private static Block223 block223 = null;
	private static PlayedGame playedGame = null;

	public static void main(String[] args) {
		LoginView window = new LoginView();
		window.setVisible(true);
	}

	public static UserRole getCurrentUserRole() {
		return currentUserRole;
	}

	public static void setCurrentUserRole(UserRole aUserRole) {
		currentUserRole = aUserRole;
	}

	public static Game getCurrentGame() {
		return currentGame;
	}

	public static void setCurrentGame(Game aGame) {
		currentGame = aGame;
	}

	public static Block223 getBlock223() {
		if (block223 == null) {
			// If finds a file, loads it, or else, creates new Block223
			block223 = Block223Persistence.load();
		}
		return block223;
	}

	// ONLY USE FOR UNIT TESTING
	public static void setBlock223(Block223 aBlock223) {
		block223 = aBlock223;
	}

	public static Block223 resetBlock223() {
		if (block223 != null) {
			block223.delete();
		}
		setCurrentGame(null);
		setCurrentPlayableGame(null);
		block223 = Block223Persistence.load();
		return block223;
	}
	
	public static void setCurrentPlayableGame(PlayedGame aGame) {
		playedGame = aGame;
	}
	
	public static PlayedGame getCurrentPlayableGame() {
		return playedGame;
	}
}
