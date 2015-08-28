package chatclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;

import chatclient.NetworkManager.InvalidPackageException;

public class PortListener extends Thread {

	ArrayBlockingQueue<Main.LOGIN_RESULT> loginResults;

	public PortListener() {
		this.loginResults = new ArrayBlockingQueue<>(5);
	}

	@Override
	public void run() {
		while (true) {
			try {
				byte[] receiveData = new byte[1024];
				Packet packet = null;
				while (true) {
					DatagramPacket receivePacket = new DatagramPacket(receiveData,
							receiveData.length);
					try {
					packet = NetworkManager.getPacket(receivePacket);
					readMessage(packet);}
					catch (InvalidPackageException e) {
						Actions.askResend(e.getPackageNumber(), e.getIp(), e.getPort());
					}
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

	private synchronized void readMessage(Packet packet) throws InterruptedException,
			IOException, InvalidPackageException {

		String message = Utils.sub(packet.getMessage(), "[START]", "[END]");
		// Check package content.
		if (message.indexOf("[LOGIN_ANSWER]") == 0)
			Actions.loginAnswer(message, loginResults);
		else if (message.indexOf("[MESSAGE]") == 0)
			Actions.messageReceived(message);
		else if (message.indexOf("[RESEND]") == 0)
			Actions.resend(message);
		else if (message.indexOf("[IS_ALIVE]") == 0)
			Actions.receiveCheck();
		else
			throw new IllegalStateException("Invalid action");
	}
}
