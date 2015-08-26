package chatserver;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

public class Actions {

	public static void messageReceived(String message) throws IOException {
		message = Utils.sub(message, "[MESSAGE]", "[/MESSAGE]");

		StringBuilder output = new StringBuilder();
		output.append("[START]");
		output.append("[MESSAGE]");
		output.append("[TIME]");
		output.append(System.currentTimeMillis());
		output.append("[/TIME]");
		output.append("[TEXT]");
		output.append(message);
		output.append("[/TEXT]");
		output.append("[/MESSAGE]");
		output.append("[END]");

		for (User user : Users.getUsers()) {
			NetworkManager.send(output.toString(), user.getIp(), user.getPort());
		}
	}

	public static void loginRequested(String message, InetAddress ip, int port)
			throws IOException {
		message = Utils.sub(message, "[LOGIN]", "[/LOGIN]");
		UUID id = Users.add(ip, port, message);

		StringBuilder output = new StringBuilder();
		output.append("[START]");
		output.append("[LOGIN_ANSWER]");
		if (id != null)
			output.append(id.toString());
		else
			output.append("USED");
		output.append("[/LOGIN_ANSWER]");
		output.append("[END]");

		NetworkManager.send(output.toString(), ip, port);
	}

	public static void resend(String message) throws IOException {
		Main.serverSocket.send(NetworkManager.history[Integer.parseInt(Utils.sub(
				message, "[RESEND]", "[/RESEND]"))]);
	}

	public static void askResend(int pos, InetAddress ip, int port) throws IOException {
		StringBuilder output = new StringBuilder();
		output.append("[START]");
		output.append("[RESEND]");
		output.append(pos);
		output.append("[/RESEND]");
		output.append("[END]");

		NetworkManager.send(output.toString(), ip, port);
	}
}
