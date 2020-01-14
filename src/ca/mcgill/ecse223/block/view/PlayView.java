package ca.mcgill.ecse223.block.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOCurrentBlock;
import ca.mcgill.ecse223.block.controller.TOCurrentlyPlayedGame;
import ca.mcgill.ecse223.block.controller.TOHallOfFame;
import ca.mcgill.ecse223.block.controller.TOHallOfFameEntry;
import ca.mcgill.ecse223.block.model.HallOfFameEntry;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class PlayView extends JFrame implements Block223PlayModeInterface {

	private static final int GRID_SIZE = 390;
	private static final int BLOCK_SIZE = 20;
	private static final int LEFT_VIEW_PADDING = 5; // Custom defined, space between edge of window and play area
	private static final int TOP_VIEW_PADDING = 50; // Custom defined, space between edge of window and play area
	private static final int HOF_WIDTH = 206;
	private static final int BALL_RADIUS = 5;

	private JPanel contentPane;
	private TOCurrentlyPlayedGame curGame;
	private PlayKeyListener bp;
	private JTextArea gameArea;
	private JLabel lblLevel;
	private JLabel lblLives;
	private JLabel lblScore;
	private JLabel gameNameHOF;
	private JLabel lblHOF;
	private JButton btnPrev;
	private JButton btnNext;
	private int HOFPage = 0;
	private JButton btnRecent;
	private JLabel lblError;
	private JButton btnExit;
	private boolean isInitialTest = true;
	private boolean wasPlaying = false;
	private boolean gameEnded = false;
	public int lastPoints = 0;
	public int lastBlockPoints = 0;
	private int entries = 0;

	/**
	 * Create the frame.
	 */
	public PlayView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 625, 525);

		lblError = new JLabel("Error");
		lblError.setForeground(Color.RED);
		lblError.setBounds(99, 454, 461, 16);
		lblError.setVisible(false);

		try {
			curGame = Block223Controller.getCurrentPlayableGame();
		} catch (InvalidInputException e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
		}
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponents(g);

				// Draws outer square in actual game
				g.setColor(Color.BLACK);
				g.drawLine(LEFT_VIEW_PADDING, TOP_VIEW_PADDING, LEFT_VIEW_PADDING, TOP_VIEW_PADDING + GRID_SIZE); // Left
																													// wall
				g.drawLine(LEFT_VIEW_PADDING, TOP_VIEW_PADDING + GRID_SIZE, LEFT_VIEW_PADDING + GRID_SIZE,
						TOP_VIEW_PADDING + GRID_SIZE); // Bottom wall
				g.drawLine(LEFT_VIEW_PADDING + GRID_SIZE, TOP_VIEW_PADDING, LEFT_VIEW_PADDING + GRID_SIZE,
						TOP_VIEW_PADDING + GRID_SIZE); // Right wall
				g.drawLine(LEFT_VIEW_PADDING, TOP_VIEW_PADDING, LEFT_VIEW_PADDING + GRID_SIZE, TOP_VIEW_PADDING); // Top
																													// wall
				if (curGame != null && !gameEnded) {
					// Draws all blocks on grid
					if (curGame.getBlocks() != null) {
						for (TOCurrentBlock b : curGame.getBlocks()) {
							Color color = new Color(b.getRed(), b.getGreen(), b.getBlue());
							g.setColor(color);
							g.fillRect(LEFT_VIEW_PADDING + b.getX(), TOP_VIEW_PADDING + b.getY(), BLOCK_SIZE,
									BLOCK_SIZE);
						}
						if(curGame.getBlocks().size() == 1) {
							lastBlockPoints = curGame.getBlock(0).getPoints();
						}
					}

					// Draw ball
					g.setColor(Color.red);
					g.fillOval((int) Math.round(LEFT_VIEW_PADDING + curGame.getCurrentBallX()) - BALL_RADIUS,
							TOP_VIEW_PADDING + (int) Math.round(curGame.getCurrentBallY()) - BALL_RADIUS,
							BALL_RADIUS * 2, BALL_RADIUS * 2);

					// Draw paddle
					g.setColor(Color.black);
					g.fillRect(LEFT_VIEW_PADDING + (int) Math.round(curGame.getCurrentPaddleX()),
							TOP_VIEW_PADDING + GRID_SIZE - 35, (int) Math.round(curGame.getCurrentPaddleLength()), 5);
				}
			

				// Draw HOF Boundaries
				g.drawLine(LEFT_VIEW_PADDING * 2 + GRID_SIZE, TOP_VIEW_PADDING, LEFT_VIEW_PADDING * 2 + GRID_SIZE,
						TOP_VIEW_PADDING + GRID_SIZE); // Left wall
				g.drawLine(LEFT_VIEW_PADDING * 2 + GRID_SIZE, TOP_VIEW_PADDING + GRID_SIZE,
						LEFT_VIEW_PADDING * 2 + GRID_SIZE + HOF_WIDTH, TOP_VIEW_PADDING + GRID_SIZE); // Bottom wall
				g.drawLine(LEFT_VIEW_PADDING * 2 + GRID_SIZE + HOF_WIDTH, TOP_VIEW_PADDING,
						LEFT_VIEW_PADDING * 2 + GRID_SIZE + HOF_WIDTH, TOP_VIEW_PADDING + GRID_SIZE); // Right wall
				g.drawLine(LEFT_VIEW_PADDING * 2 + GRID_SIZE, TOP_VIEW_PADDING,
						LEFT_VIEW_PADDING * 2 + GRID_SIZE + HOF_WIDTH, TOP_VIEW_PADDING); // Top wall
				g.drawLine(LEFT_VIEW_PADDING * 2 + GRID_SIZE, TOP_VIEW_PADDING + 40,
						LEFT_VIEW_PADDING * 2 + GRID_SIZE + HOF_WIDTH, TOP_VIEW_PADDING + 40);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		contentPane.add(lblError);

		gameArea = new JTextArea();
		gameArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(gameArea);
		scrollPane.setPreferredSize(new Dimension(375, 125));

		getContentPane().add(scrollPane, BorderLayout.CENTER);

		final JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPlay.setVisible(false);
				lblError.setVisible(false);
				// initiating a thread to start listening to keyboard inputs
				bp = new PlayKeyListener();
				Runnable r1 = new Runnable() {
					@Override
					public void run() {
						// in the actual game, add keyListener to the game window
						gameArea.addKeyListener(bp);
					}
				};
				Thread t1 = new Thread(r1);
				t1.start();
				// to be on the safe side use join to start executing thread t1 before executing
				// the next thread
				try {
					t1.join();
				} catch (InterruptedException e1) {
				}

				// initiating a thread to start the game loop
				Runnable r2 = new Runnable() {
					@Override
					public void run() {
						try {
							if (isAdmin() && isInitialTest) {
								Block223Controller.testGame(PlayView.this);
								isInitialTest = false;
							} else {
								Block223Controller.startGame(PlayView.this);
							}
							wasPlaying = true;
							if (!gameEnded) {
								lblError.setVisible(false);
								btnPlay.setVisible(true);
							}
						} catch (InvalidInputException e) {
							lblError.setVisible(true);
							lblError.setText(e.getMessage());
						}
					}
				};
				Thread t2 = new Thread(r2);
				t2.start();

			}
		});
		btnPlay.setBounds(147, 147, 100, 30);
		contentPane.add(btnPlay);

		JLabel lblBlock = new JLabel("BLOCK 223");
		lblBlock.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlock.setFont(new Font("Helvetica", Font.BOLD, 46));
		lblBlock.setBounds(6, 6, 395, 45);
		contentPane.add(lblBlock);

		lblLevel = new JLabel("Level");
		lblLevel.setBounds(413, 6, 61, 16);
		contentPane.add(lblLevel);

		lblLives = new JLabel("Lives");
		lblLives.setBounds(486, 6, 61, 16);
		contentPane.add(lblLives);

		lblScore = new JLabel("Score");
		lblScore.setBounds(413, 28, 90, 16);
		contentPane.add(lblScore);

		gameNameHOF = new JLabel("Hall of Fame");
		gameNameHOF.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		gameNameHOF.setHorizontalAlignment(SwingConstants.CENTER);
		gameNameHOF.setBounds(413, 60, 180, 16);
		contentPane.add(gameNameHOF);

		btnPrev = new JButton("<");
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// TODO - decrease page count by 1
				// Two options if hit 0
				// #1 - stay at 0 --> chose that one
				// #2 - wrap around to last page
				if (HOFPage > 0) {
					HOFPage--;
				}
				refreshHOF(false);
			}
		});
		btnPrev.setBounds(413, 400, 61, 29);
		contentPane.add(btnPrev);

		lblHOF = new JLabel("");
		lblHOF.setVerticalAlignment(SwingConstants.TOP);
		lblHOF.setBounds(422, 101, 122, 310);
		contentPane.add(lblHOF);

		btnNext = new JButton(">");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// TODO - increase page count by 1
				// Two options if hit max
				// #1 - stay at max
				// #2 - wrap around to 0 --> went for this one
				if (entries < 10) {
					HOFPage = 0;
				} else {
					HOFPage++;
				}
				refreshHOF(false);
			}
		});
		btnNext.setBounds(532, 400, 61, 29);
		contentPane.add(btnNext);

		btnRecent = new JButton("Recent");
		btnRecent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshHOF(true);
			}
		});
		btnRecent.setBounds(469, 400, 69, 29);
		contentPane.add(btnRecent);

		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Block223Controller.pauseCurrentGame();
				} catch (InvalidInputException e3) {
					lblError.setVisible(true);
					lblError.setText(e3.getMessage());
				}

				if (isAdmin()) {
					MainMenu menu = new MainMenu(); // If admin, go back to admin menu
					menu.setVisible(true);
					dispose();
				} else {
					PlayerMainMenu menu = new PlayerMainMenu(); // If player, go back to player menu
					menu.setVisible(true);
					dispose();
				}
			}
		});
		btnExit.setBounds(12, 449, 75, 29);
		contentPane.add(btnExit);

		refresh();
		refreshHOF(false);
	}

	// Checks to see if admin
	// Done by checking exception, due to lack of role in TO
	private boolean isAdmin() {
		try {
			Block223Controller.getHallOfFameWithMostRecentEntry(0);
			return false;
		} catch (InvalidInputException e2) {
			if (e2.getMessage().equals("Player privileges are required to access a game’s hall of fame.")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	@Override
	public String takeInputs() {
		if (bp == null) {
			return "";
		}
		String result = bp.takeInputs();
		return result;
	}

	@Override
	public void refresh() {
		try {
			curGame = Block223Controller.getCurrentPlayableGame();
			lblLives.setText("Lives: " + curGame.getLives());
			lblScore.setText("Score: " + curGame.getScore());
			lastPoints = curGame.getScore();
			lblLevel.setText("Level: " + curGame.getCurrentLevel());
			gameNameHOF.setText(curGame.getGamename() + " Hall of Fame");
		} catch (InvalidInputException e) {
			if (isAdmin() && !wasPlaying) {
				lblError.setVisible(true);
				lblError.setText("Click play to test game.");
			} else {
				lblError.setVisible(true);
				lblError.setText(e.getMessage());
			}

		}
		contentPane.repaint();
	}

	private void refreshHOF(boolean mostRecentEntryCalled) {
		if (isAdmin()) {
			lblHOF.setText("<html><i>Testing Game</i> <br>Hall of Fame Unavailable </html>");
			return;
		}
		entries = 0;
		if (mostRecentEntryCalled == true) {
			// TODO - call most recent entry, update label
			try {
				TOHallOfFame hof = Block223Controller.getHallOfFameWithMostRecentEntry(10);
				List<TOHallOfFameEntry> hofes = hof.getEntries();
				String ranking = "<html>";
				for (int i = 0; i < hofes.size(); i++) {
					TOHallOfFameEntry hofe = hofes.get(i);
					ranking = ranking
							.concat(hofe.getPosition() + ". " + hofe.getPlayername() + " " + hofe.getScore() + "<br>");
					entries++;
				}
				ranking = ranking.concat("</html>");
				lblHOF.setText(ranking);
				HOFPage = 0;
			} catch (InvalidInputException e) {
				lblError.setVisible(true);
				lblError.setText(e.getMessage());
			}
		} else {
			// TODO - Use int HOFPage to determine which entries to get
			try {
				TOHallOfFame hof = Block223Controller.getHallOfFame(HOFPage * 10 + 1, (HOFPage + 1) * 10);
				List<TOHallOfFameEntry> hofes = hof.getEntries();
				String ranking = "<html>";
				for (int i = 0; i < hofes.size(); i++) {
					TOHallOfFameEntry hofe = hofes.get(i);
					ranking = ranking
							.concat(hofe.getPosition() + ". " + hofe.getPlayername() + " " + hofe.getScore() + "<br>");
					entries++;
				}
				ranking = ranking.concat("</html>");
				lblHOF.setText(ranking);
			} catch (InvalidInputException e) {
				lblError.setVisible(true);
				lblError.setText(e.getMessage());
			}
		}
	}

	@Override
	public void endGame(int lives, TOHallOfFameEntry hof) {
		lblError.setVisible(true);
		if (isAdmin()) {
			if (lives > 0) {
				lblError.setText("Won test game.");
				contentPane.repaint();
			} else {
				lblError.setText("Game over.");
			}
		} else {
			if (lives > 0) {
				lblError.setText("Won game. Score of " + (lastPoints + lastBlockPoints) + " added to Hall of Fame.");
				contentPane.repaint();
			} else {
				lblError.setText("Game over. Score of " + lastPoints + " added to Hall of Fame.");
			}
		}
		gameEnded = true;
	}

}
