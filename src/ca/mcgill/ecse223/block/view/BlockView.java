package ca.mcgill.ecse223.block.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOBlock;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;

public class BlockView extends JFrame {

	private JPanel contentPane;
	private JTextField txtRed;
	private JTextField txtGreen;
	private JTextField txtBlue;
	private JTextField txtPoints;
	private JButton btnAddBlock;
	private JLabel lblRed;
	private JLabel lblGreen;
	private JLabel lblBlue;
	private JLabel lblPoints;
	private JComboBox<String> comboBox;
	private JButton btnEditBlock;
	private JButton btnBack;
	private JButton saveBtn;
	private JButton btnNext;
	private JLabel lblError;
	private List<TOBlock> listBlocks;
	private TOBlock currentBlock;
	private final int MARGIN = 20;
	private final int BLOCK_SIZE = 40;
	private JLabel lblBlockId;
	private JButton btnRefreshColor;
	private JButton btnDelete;

	/**
	 * Create the frame.
	 */
	public BlockView() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 300);
		this.contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				try {
					int red = Integer.parseInt(txtRed.getText());
					int green = Integer.parseInt(txtGreen.getText());
					int blue = Integer.parseInt(txtBlue.getText());
					Color color = new Color(red, green, blue);
					g.setColor(color);
					g.fillRect(MARGIN, MARGIN, BLOCK_SIZE, BLOCK_SIZE);
				} catch (Exception e) {
					lblError.setText("Colors must be integers (0-255).");
				}
			}
		};
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[100px][grow][80px]", "[][][][][][100px]"));
		this.contentPane.add(getLblRed(), "cell 0 0,alignx trailing");
		this.contentPane.add(getTxtRed(), "cell 1 0,growx");
		this.contentPane.add(getLblGreen(), "cell 0 1,alignx trailing");
		this.contentPane.add(getTxtGreen(), "cell 1 1,growx");
		this.contentPane.add(getBtnRefreshColor(), "flowx,cell 0 2");
		this.contentPane.add(getLblBlue(), "cell 0 2,alignx trailing");
		this.contentPane.add(getTxtBlue(), "cell 1 2,growx");
		this.contentPane.add(getLblPoints(), "cell 0 3,alignx trailing");
		this.contentPane.add(getTxtPoints(), "cell 1 3,growx");
		this.contentPane.add(getLblBlockId(), "cell 2 3,alignx center");
		this.contentPane.add(getBtnAddBlock(), "flowx,cell 1 4");
		this.contentPane.add(getBtnEditBlock(), "cell 1 4");
		this.contentPane.add(getComboBox(), "cell 2 4,growx");
		this.contentPane.add(getBtnBack(), "cell 0 5,aligny bottom");
		this.contentPane.add(getLblError(), "flowy,cell 1 5,aligny bottom");
		this.contentPane.add(getSaveBtn(), "cell 1 5,aligny bottom");
		this.contentPane.add(getBtnNext(), "flowx,cell 2 5,alignx right,aligny bottom");
		this.contentPane.add(getBtnDelete(), "cell 1 4,alignx left,aligny top");
		refreshComboBox();

	}

	private void refreshGraphics() {
		this.contentPane.repaint();
	}

	private void refreshComboBox() {
		try {
			listBlocks = Block223Controller.getBlocksOfCurrentDesignableGame();
			lblError.setVisible(false);
			comboBox.removeAllItems();
			for (TOBlock t : listBlocks) {
				comboBox.addItem(String.valueOf(t.getId()));
			}
		} catch (InvalidInputException e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
		}
	}

	private JTextField getTxtRed() {
		if (txtRed == null) {
			txtRed = new JTextField();
			txtRed.setColumns(10);
		}
		return txtRed;
	}

	private JTextField getTxtGreen() {
		if (txtGreen == null) {
			txtGreen = new JTextField();
			txtGreen.setColumns(10);
		}
		return txtGreen;
	}

	private JTextField getTxtBlue() {
		if (txtBlue == null) {
			txtBlue = new JTextField();
			txtBlue.setColumns(10);
		}
		return txtBlue;
	}

	private JTextField getTxtPoints() {
		if (txtPoints == null) {
			txtPoints = new JTextField();
			txtPoints.setColumns(10);
		}
		return txtPoints;
	}

	private JButton getBtnAddBlock() {
		if (btnAddBlock == null) {
			btnAddBlock = new JButton("Add");
			btnAddBlock.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Block223Controller.addBlock(Integer.parseInt(txtRed.getText()),
								Integer.parseInt(txtGreen.getText()), Integer.parseInt(txtBlue.getText()),
								Integer.parseInt(txtPoints.getText()));
						lblError.setVisible(false);
						refreshComboBox();
					} catch (InvalidInputException e1) {
						lblError.setText(e1.getMessage());
						lblError.setVisible(true);
					} catch (NumberFormatException e2) {
						lblError.setText("All values must be integers.");
						lblError.setVisible(true);
					}
				}
			});
		}
		return btnAddBlock;
	}

	private JLabel getLblRed() {
		if (lblRed == null) {
			lblRed = new JLabel("Red");
		}
		return lblRed;
	}

	private JLabel getLblGreen() {
		if (lblGreen == null) {
			lblGreen = new JLabel("Green");
		}
		return lblGreen;
	}

	private JLabel getLblBlue() {
		if (lblBlue == null) {
			lblBlue = new JLabel("Blue");
		}
		return lblBlue;
	}

	private JLabel getLblPoints() {
		if (lblPoints == null) {
			lblPoints = new JLabel("Points");
		}
		return lblPoints;
	}

	private JComboBox<String> getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox<String>();
		}
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (comboBox.getItemCount() != 0) {
						if (comboBox.getSelectedItem().toString() != null
								&& !comboBox.getSelectedItem().toString().equals("")) {
							currentBlock = Block223Controller.getBlockOfCurrentDesignableGame(
									Integer.parseInt(comboBox.getSelectedItem().toString()));
							txtRed.setText(String.valueOf(currentBlock.getRed()));
							txtBlue.setText(String.valueOf(currentBlock.getBlue()));
							txtGreen.setText(String.valueOf(currentBlock.getGreen()));
							txtPoints.setText(String.valueOf(currentBlock.getPoints()));
							refreshGraphics();
							lblError.setVisible(false);
						}
					}
				} catch (InvalidInputException e2) {
					lblError.setVisible(true);
					lblError.setText(e2.getMessage());
				}
			}
		});
		return comboBox;
	}

	private JButton getBtnEditBlock() {
		if (btnEditBlock == null) {
			btnEditBlock = new JButton("Edit");
			btnEditBlock.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Block223Controller.updateBlock(currentBlock.getId(), Integer.parseInt(txtRed.getText()),
								Integer.parseInt(txtGreen.getText()), Integer.parseInt(txtBlue.getText()),
								Integer.parseInt(txtPoints.getText()));
						lblError.setVisible(false);
					} catch (InvalidInputException e1) {
						lblError.setText(e1.getMessage());
						lblError.setVisible(true);
					} catch (NumberFormatException e2) {
						lblError.setText("All values must be integers.");
						lblError.setVisible(true);
					} catch (NullPointerException e3) {
						lblError.setText("Must create block to edit.");
						lblError.setVisible(true);
					}
				}
			});
		}
		return btnEditBlock;
	}

	private JButton getBtnBack() {
		if (btnBack == null) {
			btnBack = new JButton("Back");
			btnBack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DefineGameSettingsView defGameSettings = new DefineGameSettingsView();
					defGameSettings.setVisible(true);
					dispose();
				}
			});
		}
		return btnBack;
	}

	private JButton getSaveBtn() {
		if (saveBtn == null) {
			saveBtn = new JButton("Save");
			saveBtn.addActionListener(new ActionListener() {
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
		}
		return saveBtn;
	}

	private JButton getBtnNext() {
		if (btnNext == null) {
			btnNext = new JButton("Next");
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (Block223Controller.getBlocksOfCurrentDesignableGame().size() > 0) {
							BlockAssignmentView blockView = new BlockAssignmentView();
							blockView.setVisible(true);
							dispose();
						} else {
							lblError.setText("Must create block before proceeding.");
							lblError.setVisible(true); 
						}
					} catch (InvalidInputException e1) {
						lblError.setText("Must create block before proceeding.");
						lblError.setVisible(true); 
					}
				}
			});
		}
		return btnNext;

	}

	private JLabel getLblError() {
		if (lblError == null) {
			lblError = new JLabel("Error");
			lblError.setForeground(Color.RED);
			lblError.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		}
		return lblError;
	}

	private JLabel getLblBlockId() {
		if (lblBlockId == null) {
			lblBlockId = new JLabel("Block Id");
		}
		return lblBlockId;
	}

	private JButton getBtnRefreshColor() {
		if (btnRefreshColor == null) {
			btnRefreshColor = new JButton("Refresh ");
			btnRefreshColor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refreshGraphics();
				}
			});
		}
		return btnRefreshColor;
	}

	private JButton getBtnDelete() {
		if (btnDelete == null) {
			btnDelete = new JButton("Delete");
			btnDelete.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						Block223Controller.deleteBlock(currentBlock.getId());
						lblError.setVisible(false);
						refreshComboBox();
					} catch (InvalidInputException e1) {
						lblError.setText(e1.getMessage());
						lblError.setVisible(true);
					} catch (NullPointerException e3) {
						lblError.setText("Must create block to delete it.");
						lblError.setVisible(true);
					}
				}
			});
		}
		return btnDelete;
	}
}
