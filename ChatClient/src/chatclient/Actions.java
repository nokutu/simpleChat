package chatclient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import chatclient.Main.LOGIN_RESULT;

public class Actions {

	public static void messageReceived(String message) throws IOException {
		message = Utils.sub(message, "[MESSAGE]", "[/MESSAGE]");

		Main.main.gui.messages.add(0, Utils.sub(message, "[TEXT]", "[/TEXT]"));
		Main.main.gui.refresh();
	}

	public static void resend(String message) throws IOException {
		Main.main.serverSocket.send(NetworkManager.history[Integer.parseInt(Utils
				.sub(message, "[RESEND]", "[/RESEND]"))]);
	}

	public static void loginAnswer(String message,
			ArrayBlockingQueue<Main.LOGIN_RESULT> loginResults)
			throws InterruptedException {
		message = Utils.sub(message, "[LOGIN_ANSWER]", "[/LOGIN_ANSWER]");
		if (message.equals("USED"))
			loginResults.put(Main.LOGIN_RESULT.USERNAME_USED);
		else {
			Main.main.id = UUID.fromString(message);
			loginResults.put(Main.LOGIN_RESULT.SUCCESSFUL);
		}
	}

	public static void askResend(int pos, InetAddress ip, int port)
			throws IOException {
		StringBuilder output = new StringBuilder();
		output.append("[START]");
		output.append("[RESEND]");
		output.append(pos);
		output.append("[/RESEND]");
		output.append("[END]");

		NetworkManager.send(output.toString(), ip, port);
	}

	public synchronized static void send(String message, InetAddress ip, int port)
			throws IOException {
		message = "[START][MESSAGE]" + Main.main.username + ": " + message
				+ "[/MESSAGE][END]";
		NetworkManager.send(message, ip, port);
	}

	public static LOGIN_RESULT login(InetAddress ip, int port)
			throws IOException, InterruptedException {
		String message = "[START][LOGIN]" + Main.main.username + "[/LOGIN][END]";
		NetworkManager.send(message, ip, port);

		return Main.portListener.loginResults.poll(5, TimeUnit.SECONDS);
	}

	public static void receiveCheck() throws IOException {
		StringBuilder output = new StringBuilder();
		output.append("[START]");
		output.append("[IS_ALIVE]");
		output.append(Main.main.id);
		output.append("[/IS_ALIVE]");
		output.append("[END]");

		Main.main.periodicConnectionCheck.lastCheck = System.currentTimeMillis();
		NetworkManager.send(output.toString(), Main.main.serverIP, Main.main.serverPort);
	}

	public static void disconnect() throws UnknownHostException {
		Main.main.serverIP = null;
		Main.main.serverPort = 0;
		Main.main.periodicConnectionCheck.lastCheck = 0L;
		Main.main.gui.messages.clear();
		Main.main.showLoginDialog();
	}
}
