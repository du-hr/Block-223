package ca.mcgill.ecse223.block.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOBlock;
import ca.mcgill.ecse223.block.controller.TOGame;
import ca.mcgill.ecse223.block.controller.TOGridCell;

import java.awt.Color;

import javax.swing.JComboBox;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JTextField;

public class BlockAssignmentView extends JFrame {

	enum Action {
		ADD, MOVE, DELETE, NOTHING
	};

	private JPanel contentPanebav;
	private JComboBox<String> comboBoxBlock;
	private final JLabel lblXval = new JLabel("X: ");
	private final JLabel lblYval = new JLabel("Y: ");
	private static final int GRID_SIZE = 390;
	private static final int BLOCK_SIZE = 20;
	private static final int VIEW_PADDING = 5;
	private static final int WALL_PADDING = 10;
	private static final int COLUMNS_PADDING = 5;
	private static final int ROW_PADDING = 2;
	private static final int MAX_Y_BLOCKS = 15; //(GRID_SIZE - 2 * WALL_PADDING - BLOCK_SIZE) / (BLOCK_SIZE + ROW_PADDING) + 1;
	private static final int MAX_X_BLOCKS = (GRID_SIZE - 2 * WALL_PADDING - BLOCK_SIZE) / (BLOCK_SIZE + COLUMNS_PADDING)
			+ 1;
	private final JRadioButton rdbtnPosition = new JRadioButton("Position");
	private final JRadioButton rdbtnDelete = new JRadioButton("Remove");
	private final JRadioButton rdbtnMove = new JRadioButton("Move");
	private final JButton btnBack = new JButton("Back");
	private final JButton btnFinish = new JButton("Finish");
	private final JButton btnSave = new JButton("Save");
	private final JLabel lblError = new JLabel("error");
	private final JLabel lblBlockId = new JLabel("Block Id");
	private List<TOGridCell> blocklvlList;
	private List<TOBlock> blockList;
	private TOGame game;
	private int level = 1;
	private int curId;
	private Action currentAction;
	private final JLabel lblLevel = new JLabel("Level");
	private final JComboBox<String> levelComboBox = new JComboBox<String>();
	private boolean isFirstMove = true;
	private int firstMoveX = 0;
	private int firstMoveY = 0;
	private final JLabel lblPoints = new JLabel("Points: ");
	private Color blockPreviewColor;

	/**
	 * Create the frame.
	 */
	public BlockAssignmentView() {
		currentAction = Action.NOTHING;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 625, 525);
		lblError.setVisible(false);

		try {
			blocklvlList = Block223Controller.getBlocksAtLevelOfCurrentDesignableGame(level);
		} catch (InvalidInputException e2) {
			lblError.setVisible(true);
			lblError.setText(e2.getMessage());
		}

		this.contentPanebav = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponents(g);

				// Draws outer square (thickness is 10 for wall_padding) in actual game
				g.setColor(Color.BLACK);
				g.fillRect(VIEW_PADDING, VIEW_PADDING, WALL_PADDING, GRID_SIZE);
				g.fillRect(VIEW_PADDING, VIEW_PADDING, GRID_SIZE, WALL_PADDING);
				g.fillRect(VIEW_PADDING, GRID_SIZE + VIEW_PADDING - WALL_PADDING, GRID_SIZE, WALL_PADDING);
				g.fillRect(GRID_SIZE + VIEW_PADDING - WALL_PADDING, VIEW_PADDING, WALL_PADDING, GRID_SIZE);

				// Draw vertical rectangles with padding for vertical lines
				for (int i = 1; i < (GRID_SIZE - 2 * WALL_PADDING - BLOCK_SIZE) / (BLOCK_SIZE + COLUMNS_PADDING)
						+ 1; i++) {
					g.fillRect((i) * (BLOCK_SIZE + COLUMNS_PADDING) + WALL_PADDING + VIEW_PADDING - COLUMNS_PADDING,
							VIEW_PADDING, COLUMNS_PADDING, GRID_SIZE);
				}

				// Draw horizontal rectangles with padding for horizontal lines
				for (int i = 1; i < (GRID_SIZE - 2 * WALL_PADDING - BLOCK_SIZE) / (BLOCK_SIZE + ROW_PADDING) + 2; i++) {
					g.fillRect(VIEW_PADDING,
							(i) * (BLOCK_SIZE + ROW_PADDING) + WALL_PADDING + VIEW_PADDING - ROW_PADDING, GRID_SIZE,
							ROW_PADDING);
				}

				// Draw null zone at bottom
				g.setColor(Color.red);
				g.fillRect(VIEW_PADDING + WALL_PADDING,
						(MAX_Y_BLOCKS) * (BLOCK_SIZE + ROW_PADDING) + VIEW_PADDING + WALL_PADDING,
						GRID_SIZE - 2 * WALL_PADDING, (2*BLOCK_SIZE+ROW_PADDING));
				
				//Draws all blocks on grid
				if (blocklvlList != null) {
					for (TOGridCell b : blocklvlList) {
						Color color = new Color(b.getRed(), b.getGreen(), b.getBlue());
						g.setColor(color);
						int xPos = VIEW_PADDING + WALL_PADDING
								+ (b.getGridHorizontalPosition() - 1) * (BLOCK_SIZE + COLUMNS_PADDING);
						int yPos = VIEW_PADDING + WALL_PADDING
								+ (b.getGridVerticalPosition() - 1) * (BLOCK_SIZE + ROW_PADDING);
						g.fillRect(xPos, yPos, BLOCK_SIZE, BLOCK_SIZE);
					}
				}
				
				//Draws blocks on preview
				g.setColor(blockPreviewColor);
				g.fillRect(2*VIEW_PADDING+2*WALL_PADDING+GRID_SIZE, VIEW_PADDING, 3*BLOCK_SIZE, 3*BLOCK_SIZE);

			}
		};
		this.contentPanebav.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPanebav);
		this.contentPanebav.setLayout(new MigLayout("", "[390px][100px,grow]", "[390px][]"));

		this.contentPanebav.add(this.lblPoints, "flowy,cell 1 0");

		this.contentPanebav.add(this.lblLevel, "cell 1 0");
		refreshLevelComboBox();
		this.levelComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (levelComboBox.getItemCount() != 0) {
						level = levelComboBox.getSelectedIndex() + 1;
						lblError.setVisible(false);
						blocklvlList = Block223Controller.getBlocksAtLevelOfCurrentDesignableGame(level);
						contentPanebav.repaint();
					}
				} catch (InvalidInputException e2) {
					lblError.setVisible(true);
					lblError.setText(e2.getMessage());
				}
			}

		});

		this.contentPanebav.add(this.levelComboBox, "cell 1 0,growx");
		this.contentPanebav.add(this.lblBlockId, "cell 1 0");
		this.contentPanebav.add(getComboBoxBlock(), "cell 1 0,growx");
		this.contentPanebav.add(getLblXval(), "cell 1 0");
		this.contentPanebav.add(getLblYval(), "cell 1 0");
		this.rdbtnPosition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnPosition.setSelected(true);
				rdbtnMove.setSelected(false);
				rdbtnDelete.setSelected(false);
				currentAction = Action.ADD;
				lblError.setText("Click grid slot to add block.");
			}
		});

		this.contentPanebav.add(this.rdbtnPosition, "cell 1 0");
		this.rdbtnMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnPosition.setSelected(false);
				rdbtnMove.setSelected(true);
				rdbtnDelete.setSelected(false);
				lblError.setVisible(true);
				isFirstMove = true;
				currentAction = Action.MOVE;
				lblError.setText("Select block to move.");
			}
		});

		this.contentPanebav.add(this.rdbtnMove, "cell 1 0");
		this.rdbtnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdbtnPosition.setSelected(false);
				rdbtnMove.setSelected(false);
				rdbtnDelete.setSelected(true);
				lblError.setVisible(true);
				currentAction = Action.DELETE;
				lblError.setText("Click block to remove it.");
			}
		});

		this.contentPanebav.add(this.rdbtnDelete, "cell 1 0");
		this.btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BlockView blockView = new BlockView();
				blockView.setVisible(true);
				dispose();
			}
		});

		this.contentPanebav.add(this.btnBack, "flowx,cell 0 1");
		this.btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainMenu mainmenu = new MainMenu();
				mainmenu.setVisible(true);
				dispose();
			}
		});

		this.contentPanebav.add(this.btnFinish, "cell 1 1");
		this.btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Block223Controller.saveGame();
					lblError.setVisible(true);
					lblError.setText("Game saved.");
				} catch (InvalidInputException e1) {
					lblError.setText(e1.getMessage());
					lblError.setVisible(true);
				}
			}
		});

		this.contentPanebav.add(this.btnSave, "cell 0 1,alignx right");
		this.lblError.setForeground(Color.RED);

		this.contentPanebav.add(this.lblError, "cell 0 1");
		contentPanebav.addMouseListener(new MouseListen());
		updateBlockPreview();
	}

	private void refreshBlockComboBox() {
		try {
			blockList = Block223Controller.getBlocksOfCurrentDesignableGame();
			lblError.setVisible(false);
			comboBoxBlock.removeAllItems();
			for (TOBlock b : blockList) {
				comboBoxBlock.addItem(String.valueOf(b.getId()));
			}
		} catch (InvalidInputException e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
		}
	}

	private void refreshLevelComboBox() {
		try {
			game = Block223Controller.getCurrentDesignableGame();
			int level = game.getNrLevels();
			lblError.setVisible(false);
			levelComboBox.removeAllItems();
			for (int i = 1; i <= level; i++) {
				levelComboBox.addItem("Level " + String.valueOf(i));
			}
		} catch (InvalidInputException e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
		}
	}

	private JComboBox<String> getComboBoxBlock() {
		if (comboBoxBlock == null) {
			comboBoxBlock = new JComboBox<String>();
			refreshBlockComboBox();
			curId = Integer.parseInt(comboBoxBlock.getSelectedItem().toString());
			comboBoxBlock.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updateBlockPreview();
				}
			});
		}
		return comboBoxBlock;
	}

	private void updateBlockPreview() {
		curId = Integer.parseInt(comboBoxBlock.getSelectedItem().toString());
		try {
			TOBlock block = Block223Controller.getBlockOfCurrentDesignableGame(curId);
			lblPoints.setText("Points: " + block.getPoints());
			blockPreviewColor = new Color(block.getRed(), block.getGreen(), block.getBlue());
		} catch (InvalidInputException e1) {
			lblError.setVisible(true);
			lblError.setText(e1.getMessage());
		}
		contentPanebav.repaint();
	}
	
	private JLabel getLblXval() {
		return lblXval;
	}

	private JLabel getLblYval() {
		return lblYval;
	}

	class MouseListen extends MouseAdapter {
		public void mouseReleased(MouseEvent e) {
			int x = (e.getX() - VIEW_PADDING + WALL_PADDING) / (BLOCK_SIZE + COLUMNS_PADDING);
			int y = (e.getY() - VIEW_PADDING + WALL_PADDING) / (BLOCK_SIZE + ROW_PADDING);
			if (x > 0 && x <= MAX_X_BLOCKS && y > 0 && y <= MAX_Y_BLOCKS) {
				lblXval.setText("X: " + x);
				lblYval.setText("Y: " + y);
				switch (currentAction) {
				case ADD:
					try {
						Block223Controller.positionBlock(curId, level, x, y);
						blocklvlList = Block223Controller.getBlocksAtLevelOfCurrentDesignableGame(level);

					} catch (InvalidInputException e2) {
						lblError.setVisible(true);
						lblError.setText(e2.getMessage());
					}
					contentPanebav.repaint();
					break;
				case DELETE:
					try {
						Block223Controller.removeBlock(level, x, y);
						blocklvlList = Block223Controller.getBlocksAtLevelOfCurrentDesignableGame(level);
					} catch (InvalidInputException e1) {
						lblError.setVisible(true);
						lblError.setText(e1.getMessage());
					}
					contentPanebav.repaint();
					break;

				case MOVE:
					if (isFirstMove) {
						firstMoveX = x;
						firstMoveY = y;
						lblError.setText("Select new position for block.");
						isFirstMove = false;
					} else {
						try {
							Block223Controller.moveBlock(level, firstMoveX, firstMoveY, x, y);
							blocklvlList = Block223Controller.getBlocksAtLevelOfCurrentDesignableGame(level);
							lblError.setText("Select block to move.");
							isFirstMove = true;
						} catch (InvalidInputException e1) {
							lblError.setVisible(true);
							lblError.setText(e1.getMessage());
							isFirstMove = true;
						}
					}
					contentPanebav.repaint();
					break;
				case NOTHING:
					break;
				}
			}
		}
	}
}
