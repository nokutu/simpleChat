package chatclient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

public class GUI extends JPanel {

	private static final long serialVersionUID = 4568153954118269950L;

	JTextArea inputBox;
	JTextArea chatBox;
	ArrayList<String> messages;

	JScrollPane scroll;

	public GUI() {
		messages = new ArrayList<>();

		this.setLayout(new BorderLayout());

		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BorderLayout());
		inputBox = new JTextArea();
		inputBox.setLineWrap(true);
		inputBox.setRows(2);
		inputBox.setBorder(BorderFactory.createLineBorder(Color.black));
		lowerPanel.add(inputBox, BorderLayout.CENTER);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendAction());
		lowerPanel.add(sendButton, BorderLayout.EAST);
		this.add(lowerPanel, BorderLayout.SOUTH);

		chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setLineWrap(true);
		scroll = new JScrollPane(chatBox);
		scroll
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		DefaultCaret caret = (DefaultCaret) chatBox.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		this.add(scroll, BorderLayout.CENTER);

		inputBox.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				"send");
		inputBox.getActionMap().put("send", new SendAction());
	}

	public void refresh() {
		chatBox.setText("");
		String newlines = "";
		for (int i = 0; i < (chatBox.getParent().getHeight() / 16) - messages.size(); i++) {
			newlines += "\n";
		}
		for (String message : messages) {
			chatBox.setText(message + "\n" + chatBox.getText());
		}
		chatBox.setText(newlines + chatBox.getText());
		System.out.println(chatBox.getHeight());
	}

	private class SendAction extends AbstractAction {

		private static final long serialVersionUID = -247856673225601687L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!inputBox.getText().isEmpty())
				try {
					Actions.send(inputBox.getText(), Main.main.serverIP,
							Main.main.serverPort);
					inputBox.setText("");
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
