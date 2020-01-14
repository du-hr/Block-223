package ca.mcgill.ecse223.block.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ca.mcgill.ecse223.block.controller.Block223Controller;
import ca.mcgill.ecse223.block.controller.InvalidInputException;
import ca.mcgill.ecse223.block.controller.TOUserMode;

import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JPasswordField;

public class LoginView extends JFrame {

	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton btnLogin;
	private JButton btnRegister;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblErrorMain;
	private JPanel contentPane;

	/**
	 * Create the application.
	 */
	public LoginView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.contentPane = new JPanel();
		setBounds(100, 100, 500, 350);
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new MigLayout("", "[450px,grow]", "[150][50][]"));
		this.contentPane.add(getLblUsername(), "flowy,cell 0 0,alignx center");
		this.contentPane.add(getUsernameField(), "cell 0 0,alignx center");
		this.contentPane.add(getLblErrorMain(), "flowy,cell 0 1,alignx center");
		this.contentPane.add(getBtnLogin(), "cell 0 1,alignx center");
		this.contentPane.add(getLblPassword(), "cell 0 0,alignx center");
		this.contentPane.add(getPasswordField(), "cell 0 0 1 2,alignx center");
		this.contentPane.add(getBtnRegister(), "cell 0 2,alignx center");
		lblErrorMain.setVisible(false);
	}

	private JTextField getUsernameField() {
		if (usernameField == null) {
			usernameField = new JTextField();
			usernameField.setColumns(10);
		}
		return usernameField;
	}

	private JPasswordField getPasswordField() {
		if (passwordField == null) {
			passwordField = new JPasswordField();
			passwordField.setColumns(10);
		}
		return passwordField;
	}

	private JButton getBtnLogin() {
		if (btnLogin == null) {
			btnLogin = new JButton("Login");
			btnLogin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String username = usernameField.getText();
					String password = String.valueOf(passwordField.getPassword());
					try {
						Block223Controller.login(username, password);
						TOUserMode mode = Block223Controller.getUserMode();
						if (mode.getMode() == TOUserMode.Mode.Design) { //Only logins to design main menu if admin
							MainMenu main = new MainMenu();
							main.setVisible(true);
							dispose();
						} else { 
							PlayerMainMenu main = new PlayerMainMenu();
							main.setVisible(true);
							dispose();
						}
					} catch (InvalidInputException m) {
						lblErrorMain.setText(m.getMessage());
						lblErrorMain.setVisible(true);
					}
				}
			});
		}
		return btnLogin;
	}

	private JButton getBtnRegister() {
		if (btnRegister == null) {
			btnRegister = new JButton("Register");
			btnRegister.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					RegisterView register = new RegisterView();
					register.setVisible(true);
					dispose();
				}
			});
		}
		return btnRegister;
	}

	private JLabel getLblUsername() {
		if (lblUsername == null) {
			lblUsername = new JLabel("Username");
		}
		return lblUsername;
	}

	private JLabel getLblPassword() {
		if (lblPassword == null) {
			lblPassword = new JLabel("Password");
		}
		return lblPassword;
	}

	private JLabel getLblErrorMain() {
		if (lblErrorMain == null) {
			lblErrorMain = new JLabel("error");
			lblErrorMain.setForeground(Color.RED);
			lblErrorMain.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		}
		return lblErrorMain;
	}
}
