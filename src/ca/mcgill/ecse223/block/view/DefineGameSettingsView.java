package ca.mcgill.ecse223.block.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOGame;
import net.miginfocom.swing.MigLayout;

public class DefineGameSettingsView extends JFrame {
	private JTextField gameNameField;
	private JTextField nrBlocksField;
	private JTextField minSpeedXField;
	private JTextField minSpeedYField;
	private JTextField speedIncreaseFactorField;
	private JTextField minLengthPaddleField;
	private JTextField maxLengthPaddleField;
	private JLabel errorMessageLabel;
	private TOGame toGame;
	private JTextField nrLevelsField;
	private JLabel lblNumberOfLevels;
	private JLabel lblName;
	private JLabel lblNumberOfBlocks;
	private JLabel lblMinimumXSpeed;
	private JLabel lblNewLabel;
	private JLabel lblSpeedIncreaseFactor;
	private JLabel lblMinimumPaddleLength;
	private JLabel lblMaximumPaddleLength;

	private JPanel contentPane;


	/**
	 * Create the frame.
	 */
	public DefineGameSettingsView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);
		
		try {
			toGame = Block223Controller.getCurrentDesignableGame();	
		} catch (InvalidInputException e1) {
			MainMenu menu = new MainMenu();
			menu.setVisible(true);
			dispose();
		}
		
		getContentPane().setLayout(new MigLayout("", "[270.00px][270.00px]", "[34.00px][211.00][161.00][62.00]"));

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainMenu menu = new MainMenu();
				menu.setVisible(true);
				dispose();
			}
		});
		getContentPane().add(btnBack, "cell 0 0");

		JLabel lblGameSettings_1 = new JLabel("GAME SETTINGS");
		lblGameSettings_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		getContentPane().add(lblGameSettings_1, "cell 1 0,alignx center");

		JLabel lblGameSettings = new JLabel("Game Settings");
		lblGameSettings.setFont(new Font("Tahoma", Font.BOLD, 16));
		getContentPane().add(lblGameSettings, "flowy,cell 0 1,alignx center,aligny center");
		
		lblNumberOfLevels = new JLabel("Number of Levels");
		getContentPane().add(lblNumberOfLevels, "cell 0 1,alignx center");
		
		nrLevelsField = new JTextField();
		getContentPane().add(nrLevelsField, "cell 0 1,alignx center");
		nrLevelsField.setColumns(10);
		
		lblName = new JLabel("Name");
		getContentPane().add(lblName, "cell 0 1,alignx center");

		gameNameField = new JTextField();
		getContentPane().add(gameNameField, "cell 0 1,alignx center,aligny bottom");
		gameNameField.setColumns(10);
		
		lblNumberOfBlocks = new JLabel("Number of Blocks per Level");
		getContentPane().add(lblNumberOfBlocks, "cell 0 1,alignx center");

		nrBlocksField = new JTextField();
		getContentPane().add(nrBlocksField, "cell 0 1,alignx center");
		nrBlocksField.setColumns(10);

		JLabel lblBallSettings = new JLabel("Ball Settings");
		lblBallSettings.setFont(new Font("Tahoma", Font.BOLD, 16));
		getContentPane().add(lblBallSettings, "flowy,cell 0 2,alignx center,aligny top");
		
		lblMinimumXSpeed = new JLabel("Minimum X Speed");
		getContentPane().add(lblMinimumXSpeed, "cell 0 2,alignx center");

		minSpeedXField = new JTextField();
		getContentPane().add(minSpeedXField, "cell 0 2,alignx center");
		minSpeedXField.setColumns(10);
		
		lblNewLabel = new JLabel("Minimum Y Speed");
		getContentPane().add(lblNewLabel, "cell 0 2,alignx center");

		minSpeedYField = new JTextField();
		getContentPane().add(minSpeedYField, "cell 0 2,alignx center");
		minSpeedYField.setColumns(10);
		
		lblSpeedIncreaseFactor = new JLabel("Speed Increase Factor");
		getContentPane().add(lblSpeedIncreaseFactor, "cell 0 2,alignx center");

		speedIncreaseFactorField = new JTextField();
		getContentPane().add(speedIncreaseFactorField, "cell 0 2,alignx center");
		speedIncreaseFactorField.setColumns(10);

		JLabel lblPaddleSettings = new JLabel("Paddle Settings");
		lblPaddleSettings.setFont(new Font("Tahoma", Font.BOLD, 16));
		getContentPane().add(lblPaddleSettings, "flowy,cell 1 2,alignx center,aligny top");
		
		lblMinimumPaddleLength = new JLabel("Minimum Paddle Length");
		getContentPane().add(lblMinimumPaddleLength, "cell 1 2,alignx center");

		minLengthPaddleField = new JTextField();
		getContentPane().add(minLengthPaddleField, "cell 1 2,alignx center");
		minLengthPaddleField.setColumns(10);
		
		lblMaximumPaddleLength = new JLabel("Maximum Paddle Length");
		getContentPane().add(lblMaximumPaddleLength, "cell 1 2,alignx center");

		maxLengthPaddleField = new JTextField();
		getContentPane().add(maxLengthPaddleField, "cell 1 2,alignx center,aligny center");
		maxLengthPaddleField.setColumns(10);
		
		refreshData(toGame, nrLevelsField, gameNameField, nrBlocksField, minSpeedXField, minSpeedYField, speedIncreaseFactorField, minLengthPaddleField, maxLengthPaddleField);

		JButton btnSave = new JButton("Save");
		 
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				errorMessageLabel.setVisible(false);
				String gameName = gameNameField.getText();
				int nrLevels = 1, nrBlocksPerLevel = 1, ballMinSpeed = 1, ballMaxSpeed = 10, minLengthPaddle = 1 , maxLengthPaddle = 10;
				double speedIncreaseFactor = 1;
				try {
					nrLevels = Integer.parseInt(nrLevelsField.getText());	
				} catch(NumberFormatException e) {
					errorMessageLabel.setText("Incorrect number of level");
					errorMessageLabel.setVisible(true);
				}
				try {
					nrBlocksPerLevel = Integer.parseInt(nrBlocksField.getText());	
				} catch(NumberFormatException e) {
					errorMessageLabel.setText("Incorrect number of blocks per level");
					errorMessageLabel.setVisible(true);
				}

				try {
					ballMinSpeed = Integer.parseInt(minSpeedXField.getText());	
				} catch(NumberFormatException e) {
					errorMessageLabel.setText("Incorrect minimum X speed");
					errorMessageLabel.setVisible(true);
				}

				try {
					ballMaxSpeed = Integer.parseInt(minSpeedYField.getText());	
				} catch(NumberFormatException e) {
					errorMessageLabel.setText("Incorrect minimum Y speed");
					errorMessageLabel.setVisible(true);
				}

				try {
					speedIncreaseFactor = Double.parseDouble(speedIncreaseFactorField.getText());	
				} catch(NumberFormatException e) {
					errorMessageLabel.setText("Incorrect speed increase factor");
					errorMessageLabel.setVisible(true);
				}

				try {
					minLengthPaddle = Integer.parseInt(minLengthPaddleField.getText());	
				} catch(NumberFormatException e) {
					errorMessageLabel.setText("Incorrect minimum paddle length");
					errorMessageLabel.setVisible(true);
				}

				try {
					maxLengthPaddle = Integer.parseInt(maxLengthPaddleField.getText());	
				} catch(NumberFormatException e) {
					errorMessageLabel.setText("Incorrect maximum paddle length");
					errorMessageLabel.setVisible(true);
				}

				try {
					Block223Controller.updateGame(gameName, nrLevels, nrBlocksPerLevel, ballMinSpeed, ballMaxSpeed, speedIncreaseFactor, maxLengthPaddle, minLengthPaddle);
					Block223Controller.saveGame();
					errorMessageLabel.setText("Game Saved.");
					errorMessageLabel.setVisible(true);
				} catch (InvalidInputException e) {
					errorMessageLabel.setVisible(false);
					errorMessageLabel.setText(e.getMessage());
					errorMessageLabel.setVisible(true);
				}

			}
		});
		
		errorMessageLabel = new JLabel("error");
		errorMessageLabel.setVisible(false);
		errorMessageLabel.setForeground(Color.RED);
		getContentPane().add(errorMessageLabel, "cell 0 3,alignx center");
		
		
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 14));
		getContentPane().add(btnSave, "flowx,cell 1 3,alignx right");
		JButton btnNextPage = new JButton("Next");
		btnNextPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BlockView blockview = new BlockView();
				blockview.setVisible(true);
				dispose();
			}
		});
		btnNextPage.setFont(new Font("Tahoma", Font.PLAIN, 14));
		getContentPane().add(btnNextPage, "cell 1 3,alignx right");
	
		

	}
	
	private static void refreshData(TOGame toGame, JTextField nrLevelsField, JTextField gameNameField, JTextField nrBlocksField, JTextField minSpeedXField, JTextField minSpeedYField,JTextField speedIncreaseFactorField,JTextField minLengthPaddleField,JTextField maxLengthPaddleField) {
		nrLevelsField.setText(Integer.toString(toGame.getNrLevels()));
		gameNameField.setText(toGame.getName());
		nrBlocksField.setText(Integer.toString(toGame.getNrBlocksPerLevel()));
		minSpeedXField.setText(Integer.toString(toGame.getMinBallSpeedX()));
		minSpeedYField.setText(Integer.toString(toGame.getMinBallSpeedY()));
		speedIncreaseFactorField.setText(Double.toString(toGame.getBallSpeedIncreaseFactor()));
		minLengthPaddleField.setText(Integer.toString(toGame.getMinPaddleLength()));
		maxLengthPaddleField.setText(Integer.toString(toGame.getMaxPaddleLength()));

	}

}
