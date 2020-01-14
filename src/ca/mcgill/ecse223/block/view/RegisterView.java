package ca.mcgill.ecse223.block.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class RegisterView extends JFrame {

	private JPanel contentPane;
	private JTextField usernameField;
	private JPasswordField playerPasswordField;
	private JLabel lblAdminPasswordoptional;
	private JPasswordField adminPasswordField;
	private JButton btnRegister;
	private JLabel errorMessage;
	private JButton button;

	/**
	 * Create the frame.
	 */
	public RegisterView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 350);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[548.00px,grow]", "[134.00][49.00]"));
		
		button = new JButton("Back");
		button.setFont(new Font("Tahoma", Font.PLAIN, 11));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LoginView login = new LoginView();
				login.setVisible(true);
				dispose();
			}
		});
		contentPane.add(button, "flowy,cell 0 0");
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblUsername, "cell 0 0,alignx center");
		
		usernameField = new JTextField();
		contentPane.add(usernameField, "cell 0 0,alignx center");
		usernameField.setColumns(10);
		
		JLabel lblPlayerPassword = new JLabel("Player Password (required)");
		lblPlayerPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblPlayerPassword, "cell 0 0,alignx center");
		
		playerPasswordField = new JPasswordField();
		contentPane.add(playerPasswordField, "cell 0 0,alignx center");
		playerPasswordField.setColumns(10);
		
		lblAdminPasswordoptional = new JLabel("Admin Password (optional)");
		lblAdminPasswordoptional.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblAdminPasswordoptional, "cell 0 0,alignx center");
		
		adminPasswordField = new JPasswordField();
		contentPane.add(adminPasswordField, "cell 0 0,alignx center");
		adminPasswordField.setColumns(10);
		
		errorMessage = new JLabel("error");
		errorMessage.setFont(new Font("Dialog", Font.BOLD, 13));
		errorMessage.setForeground(Color.RED);
		errorMessage.setVisible(false);
		contentPane.add(errorMessage, "flowy,cell 0 1,alignx center");
		
		btnRegister = new JButton("Register");
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				char[] securedPasswordAsPlayer = playerPasswordField.getPassword();
				char[] securedPasswordAsAdmin = adminPasswordField.getPassword();
				String passwordAsPlayer = String.valueOf(securedPasswordAsPlayer);
				String passwordAsAdmin = String.valueOf(securedPasswordAsAdmin);
				try {
					Block223Controller.register(username, passwordAsPlayer, passwordAsAdmin);
					LoginView login = new LoginView();
					login.setVisible(true);
					dispose();
				} catch (InvalidInputException e1) {
					errorMessage.setText(e1.getMessage());
					errorMessage.setVisible(true);
				}
			}
		});
		btnRegister.setFont(new Font("Tahoma", Font.BOLD, 16));
		contentPane.add(btnRegister, "cell 0 1,alignx center");
	}
}
