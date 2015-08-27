package chatclient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.UUID;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class Main {

	public static enum LOGIN_RESULT {
		SUCCESSFUL, USERNAME_USED, INVALID_IP
	}

	static PortListener portListener;

	public UUID id;
	public String username;
	public InetAddress serverIP;
	public int serverPort = 9885;
	public DatagramSocket serverSocket;
	public JFrame parent;
	public GUI gui;
	public static Main main;

	public static void main(String[] args) {
		main = new Main();
		try {
			main.run();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} finally {
			portListener.interrupt();
			if (Main.main.serverSocket != null)
				Main.main.serverSocket.close();
		}
	}

	private void run() throws UnknownHostException {
		while (true) {
			try {
				serverSocket = new DatagramSocket(new Random().nextInt(1000) + 8000);
				break;
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		portListener = new PortListener();
		portListener.start();
		parent = new JFrame("Chat");
		parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		parent.setMinimumSize(new Dimension(300, 400));
		parent.setLocationRelativeTo(null);
		this.gui = new GUI();
		parent.getContentPane().add(this.gui, BorderLayout.CENTER);
		parent.pack();
		parent.setVisible(true);

		showLoginDialog();

		while (parent.isVisible()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void showLoginDialog() throws UnknownHostException {
		JOptionPane pane = new JOptionPane();
		LoginDialog dialog = new LoginDialog();
		pane.setMessage(dialog);

		JButton ok = new JButton("Ok");
		ok.addActionListener(new OkAction(pane, dialog));

		ok.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),				"ok");
		ok.getActionMap().put("ok", new OkAction(pane, dialog));

		pane.setOptions(new JButton[] { ok });

		JDialog dlg = pane.createDialog(parent, "Connect");
		dlg.setVisible(true);
	}

	private class LoginDialog extends JPanel {

		private static final long serialVersionUID = -3841892360225803982L;

		private JTextField serverIp;
		private JTextField username;
		private JLabel usernameLabel;
		private JLabel serverIpLabel;

		private LoginDialog() {
			this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
			serverIp = new JTextField();
			username = new JTextField();

			this.serverIpLabel = new JLabel("Server IP");
			this.add(this.serverIpLabel);
			this.add(this.serverIp);
			this.usernameLabel = new JLabel("Username");
			this.add(this.usernameLabel);
			this.add(this.username);
		}
	}

	private class OkAction extends AbstractAction {

		private static final long serialVersionUID = 7231965987886531076L;

		private JOptionPane pane;
		private LoginDialog dialog;

		public OkAction(JOptionPane pane, LoginDialog dialog) {
			this.pane = pane;
			this.dialog = dialog;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!dialog.username.getText().isEmpty()
					&& Utils.validIP(dialog.serverIp.getText())) {
				try {
					serverIP = InetAddress.getByName(dialog.serverIp.getText());
					username = dialog.username.getText();
					LOGIN_RESULT result = Actions.login(serverIP, serverPort);
					if (result == LOGIN_RESULT.SUCCESSFUL)
						pane.setValue(JOptionPane.OK_OPTION);
					else if (result == LOGIN_RESULT.USERNAME_USED) {
						dialog.usernameLabel.setText("Username already in use.");
						dialog.username
								.setBorder(BorderFactory.createLineBorder(Color.RED));
					} else if (result == LOGIN_RESULT.INVALID_IP || result == null) {
						dialog.serverIpLabel.setText("Server IP is invalid.");
						dialog.serverIp
								.setBorder(BorderFactory.createLineBorder(Color.RED));
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				if (dialog.username.getText().isEmpty())
					dialog.username.setBorder(BorderFactory.createLineBorder(Color.RED));
				if (!Utils.validIP(dialog.serverIp.getText()))
					dialog.serverIp.setBorder(BorderFactory.createLineBorder(Color.RED));
			}
		}
	}
}
