package ca.mcgill.ecse223.block.view;

import java.util.ArrayList;
import java.util.List;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOPlayableGame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

@SuppressWarnings("serial")
public class PlayerMainMenu extends JFrame {

	private JPanel contentPane;
	private JLabel lblError;
	private List<TOPlayableGame> newGames;
	private List<TOPlayableGame> currentGames;

	private JComboBox<String> currentGameCombo;
	private JComboBox<String> newGameCombo;

	/**
	 * Create the frame.
	 */
	public PlayerMainMenu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[100px][grow][fill]", "[][][][][][][][][]"));

		JButton btnSignOut = new JButton("Sign Out");
		btnSignOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Block223Controller.logout();
				LoginView login = new LoginView();
				login.setVisible(true);
				dispose();
			}
		});
		contentPane.add(btnSignOut, "cell 0 0");

		JLabel lblPlayNewGame = new JLabel("Play New Game");
		contentPane.add(lblPlayNewGame, "flowx,cell 0 4");

		newGameCombo = new JComboBox<String>();
		newGameCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(newGameCombo, "cell 1 4,growx");

		JButton btnPlay = new JButton("Play Game");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectNewGame();
				
				PlayView playView = new PlayView();
				playView.setVisible(true);
				dispose();
			}
		});
		contentPane.add(btnPlay, "cell 2 4");

		JLabel lblResumeGame = new JLabel("Resume");
		contentPane.add(lblResumeGame, "cell 0 6,alignx trailing");

		currentGameCombo = new JComboBox<String>();
		currentGameCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(currentGameCombo, "cell 1 6,growx");

		JButton btnResume = new JButton("Resume Game");
		btnResume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectCurrentGame();
				
				PlayView playView = new PlayView();
				playView.setVisible(true);
				dispose();
			}
		});
		contentPane.add(btnResume, "cell 2 6");

		lblError = new JLabel("error");
		lblError.setForeground(Color.RED);
		contentPane.add(lblError, "cell 1 8");
		lblError.setVisible(false);
		
		newGames = new ArrayList<TOPlayableGame>();
		currentGames = new ArrayList<TOPlayableGame>();

		refreshComboBox();

	}

	private void refreshComboBox() {
		try {
			List<TOPlayableGame> currentPlayableGames = Block223Controller.getPlayableGames();
			lblError.setVisible(false);
			currentGameCombo.removeAllItems();
			newGameCombo.removeAllItems();
			newGames.clear();
			currentGames.clear();


			for (TOPlayableGame g : currentPlayableGames) {
				if(g.getNumber() == -1) { // New Game
					newGames.add(g); // Order matters - when add item, will perform action immediately
					newGameCombo.addItem(g.getName());

				} else {
					currentGames.add(g); // Order matters
					currentGameCombo.addItem(String.valueOf(g.getNumber()) + "-" + g.getName() + ": Level-" + String.valueOf(g.getCurrentLevel()));

				}
			}
		} catch (InvalidInputException e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
		}
	}
	
	private void selectNewGame() {
		try {
			if (newGameCombo.getItemCount() != 0) {

				if (newGameCombo.getSelectedItem().toString() != null
						&& !newGameCombo.getSelectedItem().toString().equals("")) {
					int index = newGameCombo.getSelectedIndex();
					Block223Controller.selectPlayableGame(newGames.get(index).getName(), -1);
					lblError.setVisible(false);
				}
			}
		} catch (InvalidInputException e2) {
			lblError.setVisible(true);
			lblError.setText(e2.getMessage());
		}
	}
	
	private void selectCurrentGame() {
		try {
			if (currentGameCombo.getItemCount() != 0) {
				if (currentGameCombo.getSelectedItem().toString() != null
						&& !currentGameCombo.getSelectedItem().toString().equals("")) {
					int index = currentGameCombo.getSelectedIndex();
					Block223Controller.selectPlayableGame(null, currentGames.get(index).getNumber());
					lblError.setVisible(false);
				}
			}
		} catch (InvalidInputException e2) {
			lblError.setVisible(true);
			lblError.setText(e2.getMessage());
		}
	}
}