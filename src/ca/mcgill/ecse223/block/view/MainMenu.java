package ca.mcgill.ecse223.block.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOGame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import java.awt.Color;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MainMenu extends JFrame {

	private JPanel contentPane;
	private JButton btnSignOut;
	private JButton createGame;
	private JComboBox<String> comboBox;
	private JButton editButton;
	private JButton deleteButton;
	private JLabel lblError;
	List<TOGame> games;
	private JTextField newGameName;
	private JButton testButton;
	private JButton publishButton;


	/**
	 * Create the frame.
	 */
	public MainMenu() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[450px,grow]", "[][100px][][50]"));
		this.contentPane.add(getBtnSignOut(), "flowx,cell 0 0");
		this.contentPane.add(getNewGameName(), "flowx,cell 0 1,aligny center");
		this.contentPane.add(getCreateGame(), "cell 0 1,alignx leading,aligny center");
		this.contentPane.add(getComboBox(), "flowx,cell 0 2,growx");
		this.contentPane.add(getEditButton(), "flowx,cell 1 2");
		contentPane.add(getTestButton(), "flowx,cell 1 3");
		this.contentPane.add(getLblError(), "cell 0 4,alignx center,aligny bottom");
		this.contentPane.add(getDeleteButton(), "cell 1 2");
		contentPane.add(getPublishButton(), "cell 1 3");
		lblError.setVisible(false);
		refreshComboBox();
	}

	private void refreshComboBox() {
		try {
			games = Block223Controller.getDesignableGames();
			comboBox.removeAllItems();
			for (TOGame g : games) {
				comboBox.addItem(g.getName());
			}
		} catch (InvalidInputException e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
		}
	}

	private JButton getBtnSignOut() {
		if (btnSignOut == null) {
			btnSignOut = new JButton("Sign Out");
			btnSignOut.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Block223Controller.logout();
					LoginView login = new LoginView();
					login.setVisible(true);
					dispose();
				}
			});
		}
		return btnSignOut;
	}

	private JButton getCreateGame() {
		if (createGame == null) {
			createGame = new JButton("Create Game");
			createGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Block223Controller.createGame(newGameName.getText());
						lblError.setVisible(false);
						newGameName.setText("");
						refreshComboBox();
					} catch (InvalidInputException e3) {
						lblError.setVisible(true);
						lblError.setText(e3.getMessage());
					}
				}
			});
		}
		return createGame;
	}

	private JComboBox<String> getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox<String>();
			comboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (comboBox.getItemCount() != 0) {
							if (comboBox.getSelectedItem().toString() != null
									&& !comboBox.getSelectedItem().toString().equals("")) {
								Block223Controller.selectGame(comboBox.getSelectedItem().toString());
							}
						}
					} catch (InvalidInputException e2) {
						lblError.setVisible(true);
						lblError.setText(e2.getMessage());
					}
				}
			});
		}
		return comboBox;
	}

	private JButton getEditButton() {
		if (editButton == null) {
			editButton = new JButton("Edit");
			editButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (Block223Controller.getCurrentDesignableGame() != null) {
							DefineGameSettingsView defGameSettings = new DefineGameSettingsView();
							defGameSettings.setVisible(true);
							dispose();
						} else {
							lblError.setText("Must create game to edit.");
							lblError.setVisible(true);
						}
					} catch (InvalidInputException e1) {
						lblError.setText(e1.getMessage());
						lblError.setVisible(true);
					}
				}
			});
		}
		return editButton;
	}

	private JButton getDeleteButton() {
		if (deleteButton == null) {
			deleteButton = new JButton("Delete");
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (comboBox.getSelectedItem().toString() != null
								&& !comboBox.getSelectedItem().toString().equals("")) {
							Block223Controller.deleteGame(comboBox.getSelectedItem().toString());
							lblError.setVisible(false);
							refreshComboBox();
						}
					} catch (InvalidInputException e2) {
						lblError.setVisible(true);
						lblError.setText(e2.getMessage());
					} catch (NullPointerException e3) {
						lblError.setText("Must create game to delete.");
						lblError.setVisible(true);
					}
				}
			});
		}
		return deleteButton;
	}

	private JLabel getLblError() {
		if (lblError == null) {
			lblError = new JLabel("error");
			lblError.setForeground(Color.RED);
		}
		return lblError;
	}

	private JTextField getNewGameName() {
		if (newGameName == null) {
			newGameName = new JTextField();
			newGameName.setColumns(10);
		}
		return newGameName;
	}

	private JButton getTestButton() {
		if (testButton == null) {
			testButton = new JButton("Test");
			testButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PlayView test = new PlayView();
					test.setVisible(true);
					dispose();
				}
			});
		}
		return testButton;
	}
	private JButton getPublishButton() {
		if (publishButton == null) {
			publishButton = new JButton("Publish");
			publishButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Block223Controller.publishGame();
						lblError.setVisible(false);
					} catch (InvalidInputException e1) {
						lblError.setVisible(true);
						lblError.setText(e1.getMessage());
					}
					refreshComboBox();
				}
			});
		}
		return publishButton;
	}
}
