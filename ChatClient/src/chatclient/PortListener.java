package chatclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

public class PortListener extends Thread {

	InetAddress ip;
	int port;
	String message;

	public ArrayBlockingQueue<Main.LOGIN_RESULT> loginResults;

	public PortListener() {
		this.loginResults = new ArrayBlockingQueue<>(5);
	}

	@Override
	public void run() {
		while (true) {
			try {
				byte[] receiveData = new byte[1024];
				while (true) {
					DatagramPacket receivePacket = new DatagramPacket(receiveData,
							receiveData.length);
					Main.main.serverSocket.receive(receivePacket);
					message = new String(receivePacket.getData()).trim();
					System.out.println("Client: " + message);
					ip = receivePacket.getAddress();
					port = receivePacket.getPort();
					readMessage();
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void readMessage() throws InterruptedException,
			IOException {

		message = Utils.substringWithDelete(message, "[START]", "[END]");

		// Check package content.
		if (message.indexOf("[LOGIN_ANSWER]") == 0)
			loginAnswer();
		else if (message.indexOf("[MESSAGE]") == 0)
			message();
		else
			throw new IllegalStateException("Invalid action");
	}

	public void loginAnswer() throws InterruptedException {
		message = Utils.substringWithDelete(message, "[LOGIN_ANSWER]",
				"[/LOGIN_ANSWER]");
		if (message.equals("USED"))
			loginResults.put(Main.LOGIN_RESULT.USERNAME_USED);
		else {
			Main.main.id = UUID.fromString(message);
			loginResults.put(Main.LOGIN_RESULT.SUCCESSFUL);
		}
	}

	public void message() {
		message = Utils.substringWithDelete(message, "[MESSAGE]", "[/MESSAGE]");
		Main.main.gui.messages.add(0, Utils.substringWithDelete(message, "[TEXT]", "[/TEXT]"));
		Main.main.gui.refresh();
	}
}
