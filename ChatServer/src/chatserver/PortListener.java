package chatserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.UUID;

public class PortListener extends Thread {

	DatagramSocket serverSocket = null;
	InetAddress ip;
	int port;
	String message;

	@Override
	public void run() {
		try {
			serverSocket = new DatagramSocket(9885);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		while (true) {
			try {
				byte[] receiveData = new byte[1024];
				while (true) {
					DatagramPacket receivePacket = new DatagramPacket(receiveData,
							receiveData.length);
					serverSocket.receive(receivePacket);
					message = new String(receivePacket.getData()).trim();
					System.out.println("Server: " + message);
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
		if (message.indexOf("[LOGIN]") == 0)
			login();
		else if (message.indexOf("[MESSAGE]") == 0)
			message();
		else
			throw new IllegalStateException("Invalid action");
	}

	private synchronized void login() throws IOException {
		message = Utils.substringWithDelete(message, "[LOGIN]", "[/LOGIN]");
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

		DatagramPacket sendPacket = new DatagramPacket(
				output.toString().getBytes(), output.toString().getBytes().length, ip,
				port);
		serverSocket.send(sendPacket);
	}

	private synchronized void message() throws IOException {
		message = Utils.substringWithDelete(message, "[MESSAGE]", "[/MESSAGE]");
		Users.message(message, serverSocket);
	}
}