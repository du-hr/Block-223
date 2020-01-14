package block.ca.mcgill.ecse223.block.controller;

import ca.mcgill.ecse223.block.application.Block223Application;
import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOBlock;
import ca.mcgill.ecse223.block.controller.TOGame;
import ca.mcgill.ecse223.block.persistence.Block223Persistence;
import ca.mcgill.ecse223.block.model.Admin;
import ca.mcgill.ecse223.block.model.Block;
import ca.mcgill.ecse223.block.model.Block223;
import ca.mcgill.ecse223.block.model.BlockAssignment;
import ca.mcgill.ecse223.block.model.Game;
import ca.mcgill.ecse223.block.model.Level;
import ca.mcgill.ecse223.block.model.Player;
import ca.mcgill.ecse223.block.model.User;
import java.util.List;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class Block223ControllerTest {

	private static final String GAME_NAME = "Gummy Worm";
    private static final String GAME_NAME_2 = "Marshmallow";
    private static final String GAME_NAME_3 = "Raviolli";
    private static final String GAME_NAME_4 = "Tortellini";
    private static final String GAME_NAME_5 = "Penne";
	private static final String INVALID_GAME_NAME = "Gummy Bear";
	private static final String USERNAME = "Allouks123";
	private static final String INVALID_USERNAME = "Cisso456";
	private static final String PASSWORD = "Sachertorte";
	private static final String PASSWORD_2 = "Socrates";
	private static final String INVALID_PASSWORD = "StringSSituation";
	private static final int BLOCKS_PER_LEVEL = 10;
	private static final int BLOCKS_PER_LEVEL_2 = 20;
	private static final int MIN_BALL_SPEED_X = 4;
	private static final int MIN_BALL_SPEED_X_2 = 14;
	private static final int MIN_BALL_SPEED_Y = 5;
	private static final int MIN_BALL_SPEED_Y_2 = 15;
	private static final double BALL_SPEED_INCREASE_FACTOR = 1.2;
	private static final double BALL_SPEED_INCREASE_FACTOR_2 = 10.2;
	private static final int MAX_LENGTH_PADDLE = 11;
	private static final int MAX_LENGTH_PADDLE_2 = 21;
	private static final int MIN_LENGTH_PADDLE = 2;
	private static final int MIN_LENGTH_PADDLE_2 = 12;
	private static final int NR_LEVELS = 13;
	private static final int NR_LEVELS_2 = 23;
    private static final int RED = 100;
    private static final int RED2 = 220;
    private static final int GREEN = 96;
    private static final int GREEN2 = 143;
    private static final int BLUE = 180;
    private static final int BLUE2 = 27;
    private static final int POINTS = 578;
    private static final int POINTS2 = 878;
    private static final int GRID_X = 4;
    private static final int GRID_X2 = 5;
    private static final int GRID_Y = 7;
    private static final int GRID_Y2 = 8;
	private Block223 block;



	// Select Game Unit Tests
	@Test
	public void testSelectGameAdminPrivilegeException() {
		block = new Block223();
		try {
			Block223Controller.selectGame(INVALID_GAME_NAME);
			fail();
		} catch (InvalidInputException e) {
			assertEquals("Admin privileges are required to select a game.", e.getMessage());
		}
		try {
			Player player = new Player(PASSWORD, block);
			Block223Application.setCurrentUserRole(player);
			Block223Controller.selectGame(INVALID_GAME_NAME);
			fail();
		} catch (InvalidInputException e) {
			assertEquals("Admin privileges are required to select a game.", e.getMessage());
		}
	}

	@Test
	public void testSelectGameDoesNotExist() {
		block = new Block223();
		Block223Application.setBlock223(block);

		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);
		try {
			Block223Controller.selectGame(INVALID_GAME_NAME);
			fail();
		} catch (InvalidInputException e) {
			assertEquals("A game with name " + INVALID_GAME_NAME + " does not exist.", e.getMessage());
		}
	}

	@Test
	public void testSelectGameAdminDoesNotMatch() {
		block = new Block223();
		Block223Application.setBlock223(block);
		Admin admin = new Admin(PASSWORD, block);

		// Game created by admin
		Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		Admin admin2 = new Admin(PASSWORD_2, block);

		// Set current role to admin2 to check if admin does not match
		Block223Application.setCurrentUserRole(admin2);
		try {
			Block223Controller.selectGame(GAME_NAME);
			fail();
		} catch (InvalidInputException e) {
			assertEquals("Only the admin who created the game can select the game.", e.getMessage());
		}
	}

	@Test
	public void testSelectGame() {
		block = new Block223();
		Block223Application.setBlock223(block);
		Admin admin = new Admin(PASSWORD, block);

		// Game created by admin
		Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		Block223Application.setCurrentUserRole(admin);

		try {
			Block223Controller.selectGame(GAME_NAME);
			assertEquals(game, Block223Application.getCurrentGame());
		} catch (InvalidInputException e) {
			fail();
		}
	}

	// Get Current Designable Game Unit Tests
	@Test
	public void testGetCurrentDesignableGameAdminPrivilegeException() {
		block = new Block223();
		try {
			Block223Controller.getCurrentDesignableGame();
			fail();
		} catch (InvalidInputException e) {
			assertEquals("Admin privileges are required to access game information.", e.getMessage());
		}

		try {
			Player player = new Player(PASSWORD, block);
			Block223Application.setCurrentUserRole(player);
			Block223Controller.getCurrentDesignableGame();
			fail();
		} catch (InvalidInputException e) {
			assertEquals("Admin privileges are required to access game information.", e.getMessage());
		}
	}

	@Test
	public void testGetCurrentDesignableGameCurrentGameNotSetException() {
		block = new Block223();
		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		try {
			Block223Controller.getCurrentDesignableGame();
			fail();
		} catch (InvalidInputException e) {
			assertEquals("A game must be selected to access its information.", e.getMessage());
		}
	}

	@Test
	public void testGetCurrentDesignableGameAdminDidNotCreateGameException() {
		block = new Block223();

		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		// Game created by admin
		Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		Block223Application.setCurrentGame(game);
		Admin admin2 = new Admin(PASSWORD_2, block);

		// Set current role to admin2 to check if admin does not match
		Block223Application.setCurrentUserRole(admin2);

		try {
			Block223Controller.getCurrentDesignableGame();
			fail();
		} catch (InvalidInputException e) {
			assertEquals("Only the admin who created the game can access its information.", e.getMessage());
		}
	}

	@Test
	public void testGetCurrentDesignableGame() {
		block = new Block223();
		Block223Application.setBlock223(block);
		Admin admin = new Admin(PASSWORD, block);

		// Game created by admin
		Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		Block223Application.setCurrentGame(game);
		Block223Application.setCurrentUserRole(admin);

		try {
			TOGame to = Block223Controller.getCurrentDesignableGame();
			assertEquals(game.getName(), to.getName());
			assertEquals(game.getLevels().size(), to.getNrLevels());
			assertEquals(game.getNrBlocksPerLevel(), to.getNrBlocksPerLevel());
			assertEquals(game.getBall().getMinBallSpeedX(), to.getMinBallSpeedX());
			assertEquals(game.getBall().getMinBallSpeedY(), to.getMinBallSpeedY());
			assertEquals(game.getBall().getBallSpeedIncreaseFactor(), to.getBallSpeedIncreaseFactor(), .001);
			assertEquals(game.getPaddle().getMaxPaddleLength(), to.getMaxPaddleLength());
			assertEquals(game.getPaddle().getMinPaddleLength(), to.getMinPaddleLength());
		} catch (InvalidInputException e) {
			fail();
		}
	}

	// Update Game Unit Tests
	@Test
	public void testUpdateGameAdminPrivilegeException() {
		block = new Block223();
		try {
			Block223Controller.updateGame(GAME_NAME_2, NR_LEVELS_2, BLOCKS_PER_LEVEL_2, MIN_BALL_SPEED_X_2, MIN_BALL_SPEED_Y_2, BALL_SPEED_INCREASE_FACTOR_2, MAX_LENGTH_PADDLE_2, MIN_LENGTH_PADDLE_2);
			fail();
		} catch (InvalidInputException e) {
			assertEquals("Admin privileges are required to define game settings.", e.getMessage());
		}

		try {
			Player player = new Player(PASSWORD, block);
			Block223Application.setCurrentUserRole(player);
			Block223Controller.updateGame(GAME_NAME_2, NR_LEVELS_2, BLOCKS_PER_LEVEL_2, MIN_BALL_SPEED_X_2, MIN_BALL_SPEED_Y_2, BALL_SPEED_INCREASE_FACTOR_2, MAX_LENGTH_PADDLE_2, MIN_LENGTH_PADDLE_2);
			fail();
		} catch (InvalidInputException e) {
			assertEquals("Admin privileges are required to define game settings.", e.getMessage());
		}
	}

	@Test
	public void testUpdateGameCurrentGameNotSetException() {
		block = new Block223();
		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		try {
			Block223Controller.updateGame(GAME_NAME_2, NR_LEVELS_2, BLOCKS_PER_LEVEL_2, MIN_BALL_SPEED_X_2, MIN_BALL_SPEED_Y_2, BALL_SPEED_INCREASE_FACTOR_2, MAX_LENGTH_PADDLE_2, MIN_LENGTH_PADDLE_2);
			fail();
		} catch (InvalidInputException e) {
			assertEquals("A game must be selected to define game settings.", e.getMessage());
		}
	}

	@Test
	public void testUpdateGameAdminDidNotCreateGameException() {
		block = new Block223();

		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		// Game created by admin
		Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		Block223Application.setCurrentGame(game);
		Admin admin2 = new Admin(PASSWORD_2, block);

		// Set current role to admin2 to check if admin does not match
		Block223Application.setCurrentUserRole(admin2);

		try {
			Block223Controller.updateGame(GAME_NAME_2, NR_LEVELS_2, BLOCKS_PER_LEVEL_2, MIN_BALL_SPEED_X_2, MIN_BALL_SPEED_Y_2, BALL_SPEED_INCREASE_FACTOR_2, MAX_LENGTH_PADDLE_2, MIN_LENGTH_PADDLE_2);
			fail();
		} catch (InvalidInputException e) {
			assertEquals("Only the admin who created the game can define its game settings.", e.getMessage());
		}
	}

	@Test
	public void testUpdateGameNameNotUniqueException() {
		block = new Block223();
		Block223Application.setBlock223(block);

		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		// Game created by admin
		Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		Game game2 = new Game(GAME_NAME_2, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		Block223Application.setCurrentGame(game2);
		Block223Application.setCurrentUserRole(admin);

		try {
			Block223Controller.updateGame(GAME_NAME, NR_LEVELS_2, BLOCKS_PER_LEVEL_2, MIN_BALL_SPEED_X_2, MIN_BALL_SPEED_Y_2, BALL_SPEED_INCREASE_FACTOR_2, MAX_LENGTH_PADDLE_2, MIN_LENGTH_PADDLE_2);
			fail(Block223Application.getCurrentGame().getName());
		} catch (InvalidInputException e) {
			assertEquals("The name of a game must be unique.", e.getMessage());
		}
	}


	@Test
	public void testUpdateGameNameEmptyStringOrNullException() {
		block = new Block223();

		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		// Game created by admin
		Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		Block223Application.setCurrentGame(game);
		Block223Application.setCurrentUserRole(admin);

		try {
			Block223Controller.updateGame("", NR_LEVELS_2, BLOCKS_PER_LEVEL_2, MIN_BALL_SPEED_X_2, MIN_BALL_SPEED_Y_2, BALL_SPEED_INCREASE_FACTOR_2, MAX_LENGTH_PADDLE_2, MIN_LENGTH_PADDLE_2);
			fail();
		} catch (InvalidInputException e) {
			assertEquals("The name of a game must be specified.", e.getMessage());
		}

		try {
			Block223Controller.updateGame(null, NR_LEVELS_2, BLOCKS_PER_LEVEL_2, MIN_BALL_SPEED_X_2, MIN_BALL_SPEED_Y_2, BALL_SPEED_INCREASE_FACTOR_2, MAX_LENGTH_PADDLE_2, MIN_LENGTH_PADDLE_2);
			fail();
		} catch (InvalidInputException e) {
			assertEquals("The name of a game must be specified.", e.getMessage());
		}
	}


	@Test
	public void testUpdateGame() {
		block = new Block223();
		Block223Application.setBlock223(block);
		Admin admin = new Admin(PASSWORD, block);

		// Game created by admin
		Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		Block223Application.setCurrentGame(game);
		Block223Application.setCurrentUserRole(admin);

		try {
			Block223Controller.updateGame(GAME_NAME_2, NR_LEVELS_2, BLOCKS_PER_LEVEL_2, MIN_BALL_SPEED_X_2, MIN_BALL_SPEED_Y_2, BALL_SPEED_INCREASE_FACTOR_2, MAX_LENGTH_PADDLE_2, MIN_LENGTH_PADDLE_2);
			assertEquals(game.getName(), GAME_NAME_2);
			assertEquals(game.getLevels().size(), NR_LEVELS_2);
			assertEquals(game.getNrBlocksPerLevel(), BLOCKS_PER_LEVEL_2);
			assertEquals(game.getBall().getMinBallSpeedX(), MIN_BALL_SPEED_X_2);
			assertEquals(game.getBall().getMinBallSpeedY(), MIN_BALL_SPEED_Y_2);
			assertEquals(game.getBall().getBallSpeedIncreaseFactor(), BALL_SPEED_INCREASE_FACTOR_2, .001);
			assertEquals(game.getPaddle().getMaxPaddleLength(), MAX_LENGTH_PADDLE_2);
			assertEquals(game.getPaddle().getMinPaddleLength(), MIN_LENGTH_PADDLE_2);
		} catch (InvalidInputException e) {
			fail(e.getMessage());
		}
	}


	// Persistence Unit Tests
	@Test
	public void testSaveGameAdminPrivilegeException() {
		block = new Block223();
		try {
			Block223Controller.saveGame();
			fail();
		} catch (InvalidInputException e) {
			assertEquals("Admin privileges are required to save a game.", e.getMessage());
		}

		try {
			Player player = new Player(PASSWORD, block);
			Block223Application.setCurrentUserRole(player);
			Block223Controller.saveGame();
			fail();
		} catch (InvalidInputException e) {
			assertEquals("Admin privileges are required to save a game.", e.getMessage());
		}
	}

	@Test
	public void testSaveGameCurrentGameNotSetException() {
		block = new Block223();
		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		try {
			Block223Controller.saveGame();
			fail();
		} catch (InvalidInputException e) {
			assertEquals("A game must be selected to save it.", e.getMessage());
		}
	}

	@Test
	public void testSaveGameAdminDidNotCreateGameException() {
		block = new Block223();

		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		// Game created by admin
		Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y,
				BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		Block223Application.setCurrentGame(game);
		Admin admin2 = new Admin(PASSWORD_2, block);

		// Set current role to admin2 to check if admin does not match
		Block223Application.setCurrentUserRole(admin2);

		try {
			Block223Controller.saveGame();
			fail();
		} catch (InvalidInputException e) {
			assertEquals("Only the admin who created the game can save it.", e.getMessage());
		}
	}

	@Test
	public void findGame() {
		block = new Block223();

		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		Game game2 = new Game(GAME_NAME_2, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);

		assertEquals(block.findGame(GAME_NAME), game);
		assertEquals(block.findGame(GAME_NAME_2), game2);

	}

	@Test
	public void testAddGameSuccess() {
		block = new Block223();
		Block223Application.setBlock223(block);
		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		try {
			Block223Controller.createGame(GAME_NAME);
		} catch (InvalidInputException e) {
			fail();
		}
		//Better messages (but I had to have same argument in different checks since the constructor initialize them this way)
		assertEquals(1, block.getGames().size());
		assertEquals(GAME_NAME, block.getGame(0).getName());
		assertEquals(1, block.getGame(0).getNrBlocksPerLevel());
		assertEquals(admin, block.getGame(0).getAdmin());
		assertEquals("X speed",1, block.getGame(0).getBall().getMinBallSpeedX());
		assertEquals("Y speed",1, block.getGame(0).getBall().getMinBallSpeedY());
		assertEquals(1, block.getGame(0).getBall().getBallSpeedIncreaseFactor(), 0);
		assertEquals("Max paddle length",10, block.getGame(0).getPaddle().getMaxPaddleLength());
		assertEquals("Min paddle length",10, block.getGame(0).getPaddle().getMinPaddleLength());

	}

	@Test
	public void testAddGameNull() {
		block = new Block223();
		Block223Application.setBlock223(block);
		String name = null;
		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		try {
			Block223Controller.createGame(name);
		} catch(InvalidInputException e) {
			assertEquals("The name of a game must be specified.", e.getMessage());
		}

		//Should I check no change in memory?

	}

	@Test
	public void testAddGameEmpty() {
		block = new Block223();
		Block223Application.setBlock223(block);
		String name = "";
		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		try {
			Block223Controller.createGame(name);
		} catch(InvalidInputException e) {
			assertEquals("The name of a game must be specified.", e.getMessage());
		}

		//Should I check no change in memory?

	}


	@Test
	public void testAddGameNotUnique() {
		block = new Block223();
		Block223Application.setBlock223(block);
		Admin admin = new Admin(PASSWORD, block);
		Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		//		block.addGame(game); This is redundant
		Block223Application.setCurrentUserRole(admin);

		try {
			Block223Controller.createGame(GAME_NAME);
			fail();
		} catch(InvalidInputException e) {
			assertEquals("The name of a game must be unique.", e.getMessage());

		}

		//Should I check no change in memory?
	}

	//TODO One more case to test when we want to verify that current user is an admin

	@Test
	public void testDefineGameSettingsSuccess() {
		block = new Block223();
		Block223Application.setBlock223(block);
		Admin admin = new Admin(PASSWORD, block);
		Game game = new Game(GAME_NAME, 1, admin, 1, 1, 1, 1, 1, block);
		Block223Application.setCurrentGame(game);
		Block223Application.setCurrentUserRole(admin);

		try {
			Block223Controller.setGameDetails(NR_LEVELS, BLOCKS_PER_LEVEL, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE);
		} catch (InvalidInputException e) {
			fail();
		}

		assertEquals(NR_LEVELS, game.getLevels().size());
		assertEquals(BLOCKS_PER_LEVEL, game.getNrBlocksPerLevel());
		assertEquals(MIN_BALL_SPEED_X, game.getBall().getMinBallSpeedX());
		assertEquals(MIN_BALL_SPEED_Y, game.getBall().getMinBallSpeedY());
		assertEquals(BALL_SPEED_INCREASE_FACTOR, game.getBall().getBallSpeedIncreaseFactor(), 0);
		assertEquals(MAX_LENGTH_PADDLE, game.getPaddle().getMaxPaddleLength());
		assertEquals(MIN_LENGTH_PADDLE, game.getPaddle().getMinPaddleLength());

	}

	@Test
	public void testDefineGameSettingsNull() {
		block = new Block223();
		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentGame(null);
		Block223Application.setCurrentUserRole(admin);
		

		try {
			Block223Controller.setGameDetails(NR_LEVELS, BLOCKS_PER_LEVEL, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE);
		} catch (InvalidInputException e) {
			assertEquals("A game must be selected to define game settings.", e.getMessage());
		}

	}

	//TODO decide whether or not to implement other test for all different validations

	@Test
	public void testDeleteGameSucess() {
		block = new Block223();
		Block223Application.setBlock223(block);
		Admin admin = new Admin(PASSWORD, block);
		Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
		Block223Application.setCurrentUserRole(admin);
		Block223Application.setCurrentGame(game);
		
		try {
			Block223Controller.deleteGame(GAME_NAME);
		} catch (InvalidInputException e) {
			System.out.println(e.getMessage()); //for debugging purposes TODO delete
			fail();
		}

		assertEquals(0, block.getGames().size());
	}

	//Not the best test
	@Test 
	public void testRegisterWithAdminSuccess() {
		block = new Block223();
		Block223Application.setBlock223(block);

		try {
			Block223Controller.register(USERNAME, PASSWORD, PASSWORD_2);
		} catch (InvalidInputException e) {
			fail();
		}

		assertEquals(USERNAME, block.getUser(0).getUsername());
		assertEquals(2, block.getUser(0).getRoles().size());

	}

	@Test 
	public void testRegisterWithoutAdminSuccess() {
		block = new Block223();
		Block223Application.setBlock223(block);

		try {
			Block223Controller.register(USERNAME, PASSWORD, "");
		} catch (InvalidInputException e) {
			fail();
		}

		assertEquals(USERNAME, block.getUser(0).getUsername());
		assertEquals(1, block.getUser(0).getRoles().size());

	}

	@Test
	public void testLoginInvalidUsername() {
		block = new Block223();
		Block223Application.setBlock223(block);
		Player player = new Player(PASSWORD, block);
		Admin admin = new Admin(PASSWORD_2, block);
		User user = new User(USERNAME, block, player, admin);


		try {
			Block223Controller.login(INVALID_USERNAME, PASSWORD);
		} catch (InvalidInputException e) {
			assertEquals("The username and password do not match.", e.getMessage());
		}

	}	

	@Test
	public void testLoginInvalidPassword() {
		block = new Block223();
		Block223Application.setBlock223(block);
		Player player = new Player(PASSWORD, block);
		Admin admin = new Admin(PASSWORD_2, block);
		User user = new User(USERNAME, block, player, admin);


		try {
			Block223Controller.login(USERNAME, INVALID_PASSWORD);
		} catch (InvalidInputException e) {
			assertEquals("The username and password do not match.", e.getMessage());
		}
		
	}


	@Test
	public void testLogoutSuccess() {
		block = new Block223();
		Block223Application.setBlock223(block);
		Admin admin = new Admin(PASSWORD, block);
		Block223Application.setCurrentUserRole(admin);

		Block223Controller.logout();

		assertNull(Block223Application.getCurrentUserRole());
    }
    
    @Test
    public void testGetDesignableGames() {
        block = new Block223();
        Block223Application.setBlock223(block);
        Admin admin = new Admin(PASSWORD, block);
        Block223Application.setCurrentUserRole(admin);
        Admin admin2 = new Admin(PASSWORD_2, block);
        Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        Game game1 = new Game(GAME_NAME_2, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        Game game2 = new Game(GAME_NAME_3, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        Game game3 = new Game(GAME_NAME_4, BLOCKS_PER_LEVEL, admin2, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        Game game4 = new Game(GAME_NAME_5, BLOCKS_PER_LEVEL, admin2, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        
        List<TOGame> games = new ArrayList<>();
        try {
             games = Block223Controller.getDesignableGames();
        } catch (InvalidInputException e) {
            fail(e.getMessage());
        }
        for (TOGame g : games) {
            if (g.getName().equals(GAME_NAME) || g.getName().equals(GAME_NAME_2) || g.getName().equals(GAME_NAME_3)) {
                assertEquals(3, games.size());
            } else {
                fail();
            }
        }

    }

    @Test
    public void testAddBlock() {
        block = new Block223();
        Block223Application.setBlock223(block);
        Admin admin = new Admin(PASSWORD, block);

        // Game created by admin
        Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, 
        		BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        Block223Application.setCurrentGame(game);
        Block223Application.setCurrentUserRole(admin);
        try {
        	Block223Controller.addBlock(RED, GREEN, BLUE, POINTS);
        } catch (InvalidInputException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testUpdateBlock() {
        block = new Block223();
        Block223Application.setBlock223(block);
        Admin admin = new Admin(PASSWORD, block);

        // Game created by admin
        Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, 
        		BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        Block223Application.setCurrentGame(game);
        Block223Application.setCurrentUserRole(admin);
        

        
        try {
	        Block223Controller.addBlock(RED, GREEN, BLUE, POINTS);
	        Block b = game.getBlock(0);
        	Block223Controller.updateBlock(b.getId(), RED2, GREEN2, BLUE2, POINTS2);
        	assertEquals(b.getRed(), RED2);
        	assertEquals(b.getGreen(), GREEN2);
        	assertEquals(b.getBlue(), BLUE2);
        	assertEquals(b.getPoints(), POINTS2);
        } catch (InvalidInputException e) {
            fail(e.getMessage());
        }
        
    }
    
    @Test
    public void testDeleteBlock() {
        block = new Block223();
        Block223Application.setBlock223(block);
        Admin admin = new Admin(PASSWORD, block);

        // Game created by admin
        Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, 
        		BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        Block223Application.setCurrentGame(game);
        Block223Application.setCurrentUserRole(admin);
        

        
        try {
	        Block223Controller.addBlock(RED, GREEN, BLUE, POINTS);
        	Block223Controller.deleteBlock(game.getBlock(0).getId());
        	assertEquals(game.getBlocks().size(), 0);
        }catch(InvalidInputException e) {
        	fail(e.getMessage());
        }
    }
    @Test
    public void testPositionBlock() {
    	block = new Block223();
        Block223Application.setBlock223(block);
        Admin admin = new Admin(PASSWORD, block);

        // Game created by admin
        Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, 
        		BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        Block223Application.setCurrentGame(game);
        Block223Application.setCurrentUserRole(admin);

        
        try {
	        Block223Controller.addBlock(RED, GREEN, BLUE, POINTS);
	        Block b = game.getBlock(0);
	        Level level = new Level(game);
        	Block223Controller.positionBlock(b.getId(), 1, GRID_X, GRID_Y);
        }catch(InvalidInputException e) {
        	fail(e.getMessage());
        }

    }
    
    @Test
    public void testMoveBlock() {
    	block = new Block223();
        Block223Application.setBlock223(block);
        Admin admin = new Admin(PASSWORD, block);

        // Game created by admin
        Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, 
        		BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        Block223Application.setCurrentGame(game);
        Block223Application.setCurrentUserRole(admin);

    	
    	try {
	        Block223Controller.addBlock(RED, GREEN, BLUE, POINTS);
	        Block b = game.getBlock(0);
	        Level level = new Level(game);
	    	Block223Controller.positionBlock(b.getId(), 1, GRID_X, GRID_Y);
	    	BlockAssignment ass = b.getBlockAssignment(0);
    		Block223Controller.moveBlock(1, GRID_X, GRID_Y, GRID_X2, GRID_Y2);
    		assertEquals(ass.getGridHorizontalPosition(), GRID_X2);
    		assertEquals(ass.getGridVerticalPosition(), GRID_Y2);
    	} catch(InvalidInputException e) {
    		fail(e.getMessage());
    	}
        
    	
    }
    
    @Test
    public void testRemoveBlockFromLevel() {
    	block = new Block223();
        Block223Application.setBlock223(block);
        Admin admin = new Admin(PASSWORD, block);

        // Game created by admin
        Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, 
        		BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        Block223Application.setCurrentGame(game);
        Block223Application.setCurrentUserRole(admin);


       	try {
	        Block223Controller.addBlock(RED, GREEN, BLUE, POINTS);
	        Block b = game.getBlock(0);
	        Level level = new Level(game);
	    	Block223Controller.positionBlock(b.getId(), 1, GRID_X, GRID_Y);
	    	BlockAssignment ass = b.getBlockAssignment(0);
    		Block223Controller.removeBlock(1, GRID_X, GRID_Y);
    		assertEquals(b.getBlockAssignments().size(),0);
    		assertEquals(level.getBlockAssignments().size(), 0);
    		assertEquals(game.getBlockAssignments().size(), 0);
    		
    	} catch(InvalidInputException e) {
    		fail(e.getMessage());
    	}
    	
    }
    
    public void testGetBlocksOfCurrentDesignableGames() {
        block = new Block223();
        Block223Application.setBlock223(block);
        Admin admin = new Admin(PASSWORD, block);

        // Game created by admin
        Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, 
        		BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        Block223Application.setCurrentGame(game);
        Block223Application.setCurrentUserRole(admin);
        


        try {
	        Block223Controller.addBlock(RED, GREEN, BLUE, POINTS);
	        Block223Controller.addBlock(RED2, GREEN2, BLUE2, POINTS2);
	        Block b = game.getBlock(0);
	        Block b2 = game.getBlock(1);
        	ArrayList<TOBlock> tobs= (ArrayList<TOBlock>) Block223Controller.getBlocksOfCurrentDesignableGame();
        	assertEquals(tobs.get(0).getId(), b.getId());
        	assertEquals(tobs.get(0).getRed(), b.getRed());
        	assertEquals(tobs.get(0).getGreen(), b.getGreen());
        	assertEquals(tobs.get(0).getBlue(), b.getBlue());
        	assertEquals(tobs.get(0).getPoints(), b.getPoints());
        	
        	assertEquals(tobs.get(1).getId(), b2.getId());
        	assertEquals(tobs.get(1).getRed(), b2.getRed());
        	assertEquals(tobs.get(1).getGreen(), b2.getGreen());
        	assertEquals(tobs.get(1).getBlue(), b2.getBlue());
        	assertEquals(tobs.get(1).getPoints(), b2.getPoints());

        } catch(InvalidInputException e) {
        	fail(e.getMessage());
        }
    }
    
    public void testGetBlockOfCurrentDesignableGames() {
        block = new Block223();
        Block223Application.setBlock223(block);
        Admin admin = new Admin(PASSWORD, block);

        // Game created by admin
        Game game = new Game(GAME_NAME, BLOCKS_PER_LEVEL, admin, MIN_BALL_SPEED_X, MIN_BALL_SPEED_Y, 
        		BALL_SPEED_INCREASE_FACTOR, MAX_LENGTH_PADDLE, MIN_LENGTH_PADDLE, block);
        Block223Application.setCurrentGame(game);
        Block223Application.setCurrentUserRole(admin);

        
        try {
	        
	        Block223Controller.addBlock(RED, GREEN, BLUE, POINTS);
	        Block223Controller.addBlock(RED2, GREEN2, BLUE2, POINTS2);
	        Block b = game.getBlock(0);
	        Block b2 = game.getBlock(1);
        	TOBlock tob = Block223Controller.getBlockOfCurrentDesignableGame(b.getId());
        	assertEquals(tob.getId(), b.getId());
        	assertEquals(tob.getRed(), b.getRed());
        	assertEquals(tob.getGreen(), b.getGreen());
        	assertEquals(tob.getBlue(), b.getBlue());
        	assertEquals(tob.getPoints(), b.getPoints());
        	
        	TOBlock tob2 = Block223Controller.getBlockOfCurrentDesignableGame(b2.getId());
        	assertEquals(tob2.getId(), b2.getId());
        	assertEquals(tob2.getRed(), b2.getRed());
        	assertEquals(tob2.getGreen(), b2.getGreen());
        	assertEquals(tob2.getBlue(), b2.getBlue());
        	assertEquals(tob2.getPoints(), b2.getPoints());
        	
        }catch(InvalidInputException e) {
        	fail(e.getMessage());
        }
        
    }


	//Missing -- getCurrentUser with transfer object

	// Perform after each test
	@After
	public void teardown() {
		Block223Application.setCurrentGame(null);
		Block223Application.setCurrentUserRole(null);
		Block223Application.setBlock223(null);
		block.delete();
		try {
			Runtime.getRuntime().exec("rm output.txt");
			Runtime.getRuntime().exec("rm data.block223");

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}