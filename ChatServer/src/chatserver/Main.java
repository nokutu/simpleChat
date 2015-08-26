package chatserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Main {

	private static PortListener t;

	public static void main(String[] args) {
		try {
			new Main().run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			t.interrupt();
			t.serverSocket.close();
		}
	}

	private void run() throws InterruptedException {
		t = new PortListener();
		t.start();
		while (t.isAlive())
			Thread.sleep(100);
	}
	public static void message(String message, DatagramSocket serverSocket)
			throws IOException {
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
			DatagramPacket sendPacket = new DatagramPacket(output.toString()
					.getBytes(), output.toString().getBytes().length, user.getIp(),
					user.getPort());
			serverSocket.send(sendPacket);
		}
	}

}
