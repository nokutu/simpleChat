package chatserver;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Main {

	private static PortListener t;
	public static PeriodicConnectionCheck periodicConnectionCheck;
	public static DatagramSocket serverSocket = null;

	public static void main(String[] args) {
		try {
			Main.serverSocket = new DatagramSocket(9885);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		try {
			new Main().run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			t.interrupt();
			serverSocket.close();
		}
	}

	private void run() throws InterruptedException {
		t = new PortListener();
		t.start();
		periodicConnectionCheck = new PeriodicConnectionCheck();
		periodicConnectionCheck.start();
		while (t.isAlive())
			Thread.sleep(100);
	}
}
