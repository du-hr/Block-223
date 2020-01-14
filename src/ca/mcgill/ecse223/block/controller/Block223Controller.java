package ca.mcgill.ecse223.block.controller;

import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.controller.TOUserMode.Mode;
import ca.mcgill.ecse223.block.model.*;
import ca.mcgill.ecse223.block.model.PlayedGame.PlayStatus;
import ca.mcgill.ecse223.block.persistence.*;
import ca.mcgill.ecse223.block.view.Block223PlayModeInterface;

public class Block223Controller {

	// ****************************
	// Modifier methods
	// ****************************
	public static void createGame(String name) throws InvalidInputException {
		Block223 block223 = Block223Application.getBlock223();

		// Input validation: verify that current user is an admin
		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to create a game.");
		}
		try {
			new Game(name, 1, (Admin) Block223Application.getCurrentUserRole(), 1, 1, 1, 10, 10, block223);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public static void setGameDetails(int nrLevels, int nrBlocksPerLevel, int minBallSpeedX, int minBallSpeedY,
			Double ballSpeedIncreaseFactor, int maxPaddleLength, int minPaddleLength) throws InvalidInputException {

		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to define game settings.");
		}

		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to define game settings.");
		}
		Game game = Block223Application.getCurrentGame();

		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can define its game settings.");
		}

		if (nrLevels < 1 || nrLevels > 99) {
			throw new InvalidInputException("The number of levels must be between 1 and 99.");
		}

		if (minBallSpeedX == 0 && minBallSpeedY == 0) {
			throw new InvalidInputException("The minimum speed of the ball must be greater than zero.");
		}

		try {
			game.setNrBlocksPerLevel(nrBlocksPerLevel);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

		Ball ball = game.getBall();

		try {
			ball.setMinBallSpeedX(minBallSpeedX);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

		try {
			ball.setMinBallSpeedY(minBallSpeedY);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

		try {
			ball.setBallSpeedIncreaseFactor(ballSpeedIncreaseFactor);
		} catch (RuntimeException e) {
			throw new InvalidInputException("The speed increase factor of the ball must be greater than zero.");
		}

		Paddle paddle = game.getPaddle();

		try {
			paddle.setMaxPaddleLength(maxPaddleLength);
		} catch (RuntimeException e) {
			throw new InvalidInputException(
					"The maximum length of the paddle must be greater than zero and less than or equal to 390.");
		}

		try {
			paddle.setMinPaddleLength(minPaddleLength);
		} catch (RuntimeException e) {
			throw new InvalidInputException("The minimum length of the paddle must be greater than zero.");
		}

		if (maxPaddleLength < minPaddleLength) {
			throw new InvalidInputException(
					"The minimum length of the paddle must be smaller than the maximum length.");
		}

		List<Level> levels = game.getLevels();
		int size = levels.size();
		while (nrLevels > size) {
			game.addLevel();
			size = levels.size();
		}

		while (nrLevels < size) {
			Level level = game.getLevel(size - 1);
			level.delete();
			size = levels.size();
		}

	}

	public static void deleteGame(String name) throws InvalidInputException {
		// Input validation: verify that current user is an admin

		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to delete a game.");
		}

		Block223 block223 = Block223Application.getBlock223();

		Game gameToDelete = block223.findGame(name);

		if (gameToDelete != null) {
			if (Block223Application.getCurrentUserRole() != gameToDelete.getAdmin()) {
				throw new InvalidInputException("Only the admin who created the game can delete the game.");
			}
			if (gameToDelete.isPublished()) { // TODO can replace it with CurrentGame
				throw new InvalidInputException("A published game cannot be deleted.");
			}
			gameToDelete.delete();
			Block223Persistence.save(block223);
			;
		}
		return;
	}

	public static void selectGame(String name) throws InvalidInputException {

		Block223 block223 = Block223Application.getBlock223();

		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to select a game.");
		}
		Game game = block223.findGame(name);

		if (game == null) {
			throw new InvalidInputException("A game with name " + name + " does not exist.");
		}

		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can select the game.");
		}

		if (game.isPublished()) { // What is getPublished
			throw new InvalidInputException("A published game cannot be changed.");
		}
		Block223Application.setCurrentGame(game);
	}

	public static void updateGame(String name, int nrLevels, int nrBlocksPerLevel, int minBallSpeedX, int minBallSpeedY,
			Double ballSpeedIncreaseFactor, int maxPaddleLength, int minPaddleLength) throws InvalidInputException {
		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to define game settings.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to define game settings.");
		}

		Game game = Block223Application.getCurrentGame();
		Block223 block223 = Block223Application.getBlock223();

		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can define its game settings.");
		}

		String currentName = game.getName();

		// Case added since getWithName broken in umple
		if (!currentName.equals(name)) {
			if (block223.findGame(name) != null) {
				throw new InvalidInputException("The name of a game must be unique.");
			}
		}

		try {
			if (!currentName.equals(name)) {
				if (game.setName(name) == false) {
					throw new InvalidInputException("The name of a game must be unique.");
				}
			}
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
		try {
			setGameDetails(nrLevels, nrBlocksPerLevel, minBallSpeedX, minBallSpeedY, ballSpeedIncreaseFactor,
					maxPaddleLength, minPaddleLength);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public static void addBlock(int red, int green, int blue, int points) throws InvalidInputException {

		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to add a block.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to add a block.");
		}
		Game game = Block223Application.getCurrentGame();

		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can add a block.");
		}
		for (Block b : game.getBlocks()) {
			if (b.getRed() == red && b.getGreen() == green && b.getBlue() == blue) {
				throw new InvalidInputException("A block with the same color already exists for the game.");
			}

		}

		try {
			Block block = new Block(red, green, blue, points, game);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public static void deleteBlock(int id) throws InvalidInputException {

		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to delete a block.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to delete a block.");
		}
		Game game = Block223Application.getCurrentGame();

		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can delete a block.");
		}

		if (game.findBlock(id) != null) {
			game.findBlock(id).delete();
		}
	}

	public static void updateBlock(int id, int red, int green, int blue, int points) throws InvalidInputException {
		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to update a block.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to update a block.");
		}
		Game game = Block223Application.getCurrentGame();

		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can update a block.");
		}

		for (Block b : game.getBlocks()) {
			if (b.getId() != id) {
				if (b.getRed() == red && b.getGreen() == green && b.getBlue() == blue) {
					throw new InvalidInputException("A block with the same color already exists for the game.");
				}
			}
		}

		if (game.findBlock(id) == null) {
			throw new InvalidInputException("The block does not exist.");
		}

		Block block = game.findBlock(id);

		try {
			block.setRed(red);
			block.setGreen(green);
			block.setBlue(blue);
			block.setPoints(points);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public static void positionBlock(int id, int level, int gridHorizontalPosition, int gridVerticalPosition)
			throws InvalidInputException {

		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to position a block.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to position a block.");
		}
		Game game = Block223Application.getCurrentGame();

		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can position a block.");
		}
		Level aLevel;

		try {
			aLevel = game.getLevel(level - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException("Level " + level + " does not exist for the game.");
		}

		if (aLevel.numberOfBlockAssignments() >= game.getNrBlocksPerLevel()) {
			throw new InvalidInputException("The number of blocks has reached the maximum number ("
					+ game.getNrBlocksPerLevel() + ") allowed for this game.");
		}

		for (BlockAssignment block : aLevel.getBlockAssignments()) {
			if (block.getGridHorizontalPosition() == gridHorizontalPosition
					&& block.getGridVerticalPosition() == gridVerticalPosition) {
				throw new InvalidInputException("A block already exists at location " + gridHorizontalPosition + "/"
						+ gridVerticalPosition + ".");
			}
		}

		if (game.findBlock(id) == null) {
			throw new InvalidInputException("The block does not exist.");
		}

		Block block = game.findBlock(id);

		try {
			BlockAssignment assignment = new BlockAssignment(gridHorizontalPosition, gridVerticalPosition, aLevel,
					block, game);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

	}

	public static void moveBlock(int level, int oldGridHorizontalPosition, int oldGridVerticalPosition,
			int newGridHorizontalPosition, int newGridVerticalPosition) throws InvalidInputException {

		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to move a block.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to move a block.");
		}
		Game game = Block223Application.getCurrentGame();

		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can move a block.");
		}

		Level aLevel;
		try {
			aLevel = game.getLevel(level - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException("Level " + level + " does not exist for the game.");
		}

		if (aLevel.findBlockAssignment(oldGridHorizontalPosition, oldGridVerticalPosition) == null) {
			throw new InvalidInputException("A block does not exist at location " + oldGridHorizontalPosition + "/"
					+ oldGridVerticalPosition + ".");
		}

		BlockAssignment assignment = aLevel.findBlockAssignment(oldGridHorizontalPosition, oldGridVerticalPosition);

		for (BlockAssignment block : aLevel.getBlockAssignments()) {
			if (block.getGridHorizontalPosition() == newGridHorizontalPosition
					&& block.getGridVerticalPosition() == newGridVerticalPosition) {
				throw new InvalidInputException("A block already exists at location " + newGridHorizontalPosition + "/"
						+ newGridVerticalPosition + ".");
			}
		}

		try {
			assignment.setGridHorizontalPosition(newGridHorizontalPosition);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}

		try {
			assignment.setGridVerticalPosition(newGridVerticalPosition);
		} catch (RuntimeException e) {
			// Reset grid horizontal position if vertical position fails
			assignment.setGridHorizontalPosition(oldGridHorizontalPosition);
			throw new InvalidInputException(e.getMessage());
		}

	}

	public static void removeBlock(int level, int gridHorizontalPosition, int gridVerticalPosition)
			throws InvalidInputException {
		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to remove a block.");
		}

		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to remove a block.");
		}

		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can remove a block.");
		}

		Game game = Block223Application.getCurrentGame();
		Level aLevel = game.getLevel(level - 1);
		if (aLevel.findBlockAssignment(gridHorizontalPosition, gridVerticalPosition) != null) {
			aLevel.findBlockAssignment(gridHorizontalPosition, gridVerticalPosition).delete();
		}
	}

	public static void saveGame() throws InvalidInputException {

		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to save a game.");
		}

		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to save it.");
		}

		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can save it.");
		}

		Block223 block223 = Block223Application.getBlock223();

		// Catch and RuntimeException and rethrow as InvalidInputException
		try {
			Block223Persistence.save(block223);
		} catch (RuntimeException e) {
			throw new InvalidInputException(e.getMessage());
		}
	}

	public static void register(String username, String playerPassword, String adminPassword)
			throws InvalidInputException {

		if (Block223Application.getCurrentUserRole() != null) {
			throw new InvalidInputException("Cannot register a new user while a user is logged in.");
		}

		if (playerPassword != null && adminPassword != null && playerPassword.equals(adminPassword)) {
			throw new InvalidInputException("The passwords have to be different.");
		}

		Block223 block223 = Block223Application.getBlock223();

		Player player;
		try {
			player = new Player(playerPassword, block223);
		} catch (RuntimeException e) {
			throw new InvalidInputException("The player password needs to be specified.");
		}

		User user;
		try {
			user = new User(username, block223, player);
		} catch (RuntimeException e) {
			player.delete();

			if (e.getMessage().equals("Cannot create due to duplicate username")) {
				throw new InvalidInputException("The username has already been taken.");
			} else {
				throw new InvalidInputException(e.getMessage());
			}

		}
		if (adminPassword != null && !adminPassword.equals("")) {
			Admin admin = new Admin(adminPassword, block223);
			user.addRole(admin);
		}

		Block223Persistence.save(block223);

	}

	public static void login(String username, String password) throws InvalidInputException {

		if (Block223Application.getCurrentUserRole() != null) {
			throw new InvalidInputException("Cannot login a user while a user is already logged in.");
		}

		Block223Application.resetBlock223();

		if (User.getWithUsername(username) == null) {
			throw new InvalidInputException("The username and password do not match.");
		}

		User user = User.getWithUsername(username); // Put this after to avoid nullptr exception

		List<UserRole> roles = user.getRoles();
		for (UserRole role : roles) {
			String rolePassword = role.getPassword();
			if (rolePassword.equals(password)) {
				Block223Application.setCurrentUserRole(role);
			}
		}

		if (Block223Application.getCurrentUserRole() == null) {
			throw new InvalidInputException("The username and password do not match.");
		}
	}

	public static void logout() {
		Block223Application.setCurrentUserRole(null);
	}

	// ****************************
	// Iteration 5 Modifier Methods
	// ****************************
	// play mode

	public static void selectPlayableGame(String name, int id) throws InvalidInputException {
		UserRole player = Block223Application.getCurrentUserRole();
		if (!(player instanceof Player)) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}

		Block223 block223 = Block223Application.getBlock223();
		Game game = block223.findGame(name);

		PlayedGame pgame;
		if (game != null) {
			String username = block223.findUsername((Player) player);
			if (username == null) { // Case should not occur
				throw new InvalidInputException("System error.");
			}

			pgame = new PlayedGame(username, game, block223);
			pgame.setPlayer((Player) player);
		} else {
			pgame = block223.findPlayableGame(id);
			if (pgame == null) {
				throw new InvalidInputException("The game does not exist.");
			}
			if (pgame.getPlayer() != Block223Application.getCurrentUserRole()) {
				throw new InvalidInputException("Only the player that started a game can continue the game.");
			}
		}
		Block223Application.setCurrentPlayableGame(pgame);
	}

	public static void startGame(Block223PlayModeInterface ui) throws InvalidInputException {

		if (Block223Application.getCurrentUserRole() == null) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}

		PlayedGame game = Block223Application.getCurrentPlayableGame();

		if (game == null) {
			throw new InvalidInputException("A game must be selected to play it.");
		}

		if (game.getPlayer() != null && (Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}

		if (Block223Application.getCurrentUserRole() instanceof Admin
				&& game.getGame().getAdmin() != Block223Application.getCurrentUserRole()) {
			throw new InvalidInputException("Only the admin of a game can test the game.");
		}

		if (Block223Application.getCurrentUserRole() instanceof Player && game.getPlayer() == null) {
			throw new InvalidInputException("Admin privileges are required to test a game.");
		}

		game.play();
		ui.takeInputs();

		while (game.getPlayStatus() == PlayStatus.Moving) {
			String userInput = ui.takeInputs();
			updatePaddlePosition(userInput);

			game.move();

			if (userInput.contains(" ")) {
				game.pause();
			}

			try {
				Thread.sleep((long) game.getWaitTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (game.getPlayStatus() != PlayStatus.GameOver) {
				ui.refresh();
			}
		}

		if (game.getPlayStatus() == PlayStatus.GameOver) {
			ui.endGame(game.getLives(), null);
			Block223 block223 = Block223Application.getBlock223();
			Block223Persistence.save(block223);
			Block223Application.setCurrentPlayableGame(null);
		} 
		if (game.getPlayer() != null) {
			Block223 block223 = Block223Application.getBlock223();
			Block223Persistence.save(block223);
		}
	}

	public static boolean isGameFinished() throws InvalidInputException {
		if (Block223Application.getCurrentPlayableGame() == null) {
			throw new InvalidInputException("A game must be selected to view its state.");
		}
		
		return Block223Application.getCurrentPlayableGame().getPlayStatus() == PlayStatus.GameOver;
	}

	// Should be used if exiting game through exit button
	public static void pauseCurrentGame() throws InvalidInputException {
		PlayedGame game = Block223Application.getCurrentPlayableGame();
		if (game == null) {
			throw new InvalidInputException("Must set current playable game to pause game.");
		}
		game.pause();
	}

	private static void updatePaddlePosition(String userInputs) throws InvalidInputException {
		PlayedGame game = Block223Application.getCurrentPlayableGame();
		if (game == null) {
			throw new InvalidInputException("Must set current playable game to move paddle.");
		}

		for (int i = 0; i < userInputs.length(); i++) {
			switch (userInputs.charAt(i)) {
			case 'l':
				game.setCurrentPaddleX(game.getCurrentPaddleX() + game.PADDLE_MOVE_LEFT); // PADDLE_MOVE_LEFT is
																							// negative
				if (game.getCurrentPaddleX() < 0) {
					game.setCurrentPaddleX(0);
				}
				break;
			case 'r':
				game.setCurrentPaddleX(game.getCurrentPaddleX() + game.PADDLE_MOVE_RIGHT);
				if (game.getCurrentPaddleX() + game.getCurrentPaddleLength() > game.getGame().PLAY_AREA_SIDE) {
					game.setCurrentPaddleX(game.getGame().PLAY_AREA_SIDE - game.getCurrentPaddleLength());
				}
				break;
			case ' ':
				return;
			}
		}
		return;
	}

	public static void testGame(Block223PlayModeInterface ui) throws InvalidInputException {

		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to test it.");
		}

		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to test a game.");
		}

		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can test it.");
		}

		if (Block223Application.getCurrentGame().getLevels().size() < 1) {
			throw new InvalidInputException("At least one level must be defined for a game to be tested.");
		}

		if (Block223Application.getCurrentGame().getBlocks().size() < 1) {
			throw new InvalidInputException("At least one block must be defined for a game to be tested.");
		}

		Game game = Block223Application.getCurrentGame();
		UserRole admin = Block223Application.getCurrentUserRole();
		Block223 block223 = Block223Application.getBlock223();
		String username = block223.findUsername(admin);
		PlayedGame pGame = new PlayedGame(username, game, block223);
		pGame.setPlayer(null);
		Block223Application.setCurrentPlayableGame(pGame);
		startGame(ui);
	}

	public static void publishGame() throws InvalidInputException {

		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to publish it.");
		}

		if (!(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to publish a game.");
		}

		if (Block223Application.getCurrentUserRole() != Block223Application.getCurrentGame().getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can publish it.");
		}

		if (Block223Application.getCurrentGame().getBlocks().size() < 1) {
			throw new InvalidInputException("At least one block must be defined for a game to be published.");
		}

		if (Block223Application.getCurrentGame().getLevels().size() < 1) {
			throw new InvalidInputException("At least one level must be defined for a game to be published.");
		}

		Game game = Block223Application.getCurrentGame();
		game.setPublished(true);
		saveGame();
	}

	// ****************************
	// Query methods
	// ****************************
	public static List<TOGame> getDesignableGames() throws InvalidInputException {
		// Input validation: verify that current user is an admin
		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		Block223 block223 = Block223Application.getBlock223();

		Admin admin = (Admin) Block223Application.getCurrentUserRole();
		List<TOGame> result = new ArrayList<TOGame>();

		List<Game> games = block223.getGames();
		for (Game g : games) {
			if (g.getAdmin() == (admin) && !(g.isPublished())) {
				result.add(new TOGame(g.getName(), g.getLevels().size(), g.getNrBlocksPerLevel(),
						g.getBall().getMinBallSpeedX(), g.getBall().getMinBallSpeedY(),
						g.getBall().getBallSpeedIncreaseFactor(), g.getPaddle().getMaxPaddleLength(),
						g.getPaddle().getMinPaddleLength()));
			}
		}

		return result;
	}

	public static TOGame getCurrentDesignableGame() throws InvalidInputException {

		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}

		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}

		Game game = Block223Application.getCurrentGame();

		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}
		TOGame to = new TOGame(game.getName(), game.getLevels().size(), game.getNrBlocksPerLevel(),
				game.getBall().getMinBallSpeedX(), game.getBall().getMinBallSpeedY(),
				game.getBall().getBallSpeedIncreaseFactor(), game.getPaddle().getMaxPaddleLength(),
				game.getPaddle().getMinPaddleLength());
		return to;
	}

	public static List<TOBlock> getBlocksOfCurrentDesignableGame() throws InvalidInputException {

		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}

		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}

		Game game = Block223Application.getCurrentGame();

		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}

		List<TOBlock> result = new ArrayList<TOBlock>();

		List<Block> blocks = game.getBlocks();
		for (Block block : blocks) {
			result.add(
					new TOBlock(block.getId(), block.getRed(), block.getGreen(), block.getBlue(), block.getPoints()));
		}
		return result;
	}

	public static TOBlock getBlockOfCurrentDesignableGame(int id) throws InvalidInputException {

		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}

		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}

		Game game = Block223Application.getCurrentGame();

		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}

		if (game.findBlock(id) == null) {
			throw new InvalidInputException("The block does not exist.");
		}

		Block block = game.findBlock(id);

		TOBlock tob = new TOBlock(block.getId(), block.getRed(), block.getGreen(), block.getBlue(), block.getPoints());

		return tob;
	}

	public static List<TOGridCell> getBlocksAtLevelOfCurrentDesignableGame(int level) throws InvalidInputException {
		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Admin)) {
			throw new InvalidInputException("Admin privileges are required to access game information.");
		}
		if (Block223Application.getCurrentGame() == null) {
			throw new InvalidInputException("A game must be selected to access its information.");
		}

		Game game = Block223Application.getCurrentGame();

		if (Block223Application.getCurrentUserRole() != game.getAdmin()) {
			throw new InvalidInputException("Only the admin who created the game can access its information.");
		}

		Level aLevel;
		try {
			aLevel = game.getLevel(level - 1);
		} catch (IndexOutOfBoundsException e) {
			throw new InvalidInputException("Level " + level + " does not exist for the game.");
		}

		ArrayList<TOGridCell> result = new ArrayList<>();

		for (BlockAssignment currentAssignment : aLevel.getBlockAssignments()) {
			TOGridCell to = new TOGridCell(currentAssignment.getGridHorizontalPosition(),
					currentAssignment.getGridVerticalPosition(), currentAssignment.getBlock().getId(),
					currentAssignment.getBlock().getRed(), currentAssignment.getBlock().getGreen(),
					currentAssignment.getBlock().getBlue(), currentAssignment.getBlock().getPoints());
			result.add(to);
		}
		return result;
	}

	public static TOUserMode getUserMode() {
		UserRole userRole = Block223Application.getCurrentUserRole();

		if (userRole == null) {
			TOUserMode to = new TOUserMode(Mode.None);
			return to;
		}

		if (userRole instanceof Player) {
			TOUserMode to = new TOUserMode(Mode.Play);
			return to;
		}

		if (userRole instanceof Admin) {
			TOUserMode to = new TOUserMode(Mode.Design);
			return to;
		}

		return null;
	}

	// ****************************
	// Iteration 5 Query methods
	// ****************************
	// play mode

	public static List<TOPlayableGame> getPlayableGames() throws InvalidInputException {
		Block223 block223 = Block223Application.getBlock223();

		UserRole player = Block223Application.getCurrentUserRole();

		// Null check not needed since encompassed in instanceof check
		// Java Language Specification 15.2.2
		if (!(player instanceof Player)) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}

		List<TOPlayableGame> result = new ArrayList<TOPlayableGame>();

		List<Game> games = block223.getGames();
		for (Game g : games) {
			if (g.isPublished()) {
				result.add(new TOPlayableGame(g.getName(), -1, 0));
			}
		}

		List<PlayedGame> playedGames = ((Player) player).getPlayedGames();
		for (PlayedGame g : playedGames) {
			result.add(new TOPlayableGame(g.getGame().getName(), g.getId(), g.getCurrentLevel()));
		}
		return result;
	}

	public static TOCurrentlyPlayedGame getCurrentPlayableGame() throws InvalidInputException {

		if (Block223Application.getCurrentUserRole() == null) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}
		if (Block223Application.getCurrentPlayableGame() == null) {
			throw new InvalidInputException("A game must be selected to play it.");
		}

		if (Block223Application.getCurrentUserRole() instanceof Admin
				&& Block223Application.getCurrentPlayableGame().getPlayer() != null) {
			throw new InvalidInputException("Player privileges are required to play a game.");
		}
		if (Block223Application.getCurrentUserRole() instanceof Admin && Block223Application
				.getCurrentUserRole() != Block223Application.getCurrentPlayableGame().getGame().getAdmin()) {
			throw new InvalidInputException("Only the admin of a game can test the game.");
		}
		if (Block223Application.getCurrentUserRole() instanceof Player
				&& Block223Application.getCurrentPlayableGame().getPlayer() == null) {
			throw new InvalidInputException("Admin privileges are required to test a game.");
		}

		PlayedGame pgame = Block223Application.getCurrentPlayableGame();
		boolean paused = pgame.getPlayStatus() == PlayStatus.Ready || pgame.getPlayStatus() == PlayStatus.Paused;

		TOCurrentlyPlayedGame result = new TOCurrentlyPlayedGame(pgame.getGame().getName(), paused, pgame.getScore(),
				pgame.getLives(), pgame.getCurrentLevel(), pgame.getPlayername(), pgame.getCurrentBallX(),
				pgame.getCurrentBallY(), pgame.getCurrentPaddleLength(), pgame.getCurrentPaddleX());

		List<PlayedBlockAssignment> blocks = pgame.getBlocks();
		for (PlayedBlockAssignment pblock : blocks) {
			new TOCurrentBlock(pblock.getBlock().getRed(), pblock.getBlock().getGreen(), pblock.getBlock().getBlue(),
					pblock.getBlock().getPoints(), pblock.getX(), pblock.getY(), result);
		}
		return result;

	}

	public static TOHallOfFame getHallOfFame(int start, int end) throws InvalidInputException {
		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Player)) {
			throw new InvalidInputException("Player privileges are required to access a game’s hall of fame.");
		}

		if (Block223Application.getCurrentPlayableGame() == null) {
			throw new InvalidInputException("A game must be selected to view its hall of fame. ");
		}
		PlayedGame pg = Block223Application.getCurrentPlayableGame();
		Game game = pg.getGame();
		TOHallOfFame result = new TOHallOfFame(game.getName());
		if (start < 1) {
			start = 1;
		}
		if (end > game.numberOfHallOfFameEntries()) {
			end = game.numberOfHallOfFameEntries();
		}
		start = game.numberOfHallOfFameEntries() - start;
		end = game.numberOfHallOfFameEntries() - end;

		for (int i = start; i >= end; i--) {
			TOHallOfFameEntry tohofe = new TOHallOfFameEntry(game.numberOfHallOfFameEntries()-i, game.getHallOfFameEntry(i).getPlayername(),
					game.getHallOfFameEntry(i).getScore(), result);
		}

		return result;

	}

	public static TOHallOfFame getHallOfFameWithMostRecentEntry(int numberOfEntries) throws InvalidInputException {
		if ((Block223Application.getCurrentUserRole() == null)
				|| !(Block223Application.getCurrentUserRole() instanceof Player)) {
			throw new InvalidInputException("Player privileges are required to access a game’s hall of fame.");
		}

		if (Block223Application.getCurrentPlayableGame() == null) {
			throw new InvalidInputException("A game must be selected to view its hall of fame. ");
		}
		PlayedGame pg = Block223Application.getCurrentPlayableGame();
		Game game = pg.getGame();
		TOHallOfFame result = new TOHallOfFame(game.getName());
		if (numberOfEntries <= 0 || game.getMostRecentEntry() == null) {
			return result;
		}
		HallOfFameEntry mostrecent = game.getMostRecentEntry();
		int index = game.indexOfHallOfFameEntry(mostrecent);
		int start = index + numberOfEntries / 2;
		if (start > game.numberOfHallOfFameEntries() - 1) {
			start = game.numberOfHallOfFameEntries() - 1;
		}
		int end = start - numberOfEntries + 1;
		if (end < 0) {
			end = 0;
		}
		
		for (int i = start; i >= end; i--) {
			TOHallOfFameEntry tohofe = new TOHallOfFameEntry(game.numberOfHallOfFameEntries()-index-numberOfEntries/2+start-i, 
			    game.getHallOfFameEntry(i).getPlayername(), game.getHallOfFameEntry(i).getScore(), result);
		}

		return result;

	}
}
