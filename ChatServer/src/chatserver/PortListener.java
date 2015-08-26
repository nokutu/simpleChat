package chatserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

import chatserver.NetworkManager.InvalidPackageException;

public class PortListener extends Thread {

	@Override
	public void run() {
		while (true) {
			try {
				byte[] receiveData = new byte[1024];
				while (true) {
					DatagramPacket receivePacket = new DatagramPacket(receiveData,
							receiveData.length);
					Packet packet = null;
					try {
						packet = NetworkManager.getPacket(receivePacket);
						readMessage(packet);
					} catch (InvalidPackageException e) {
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

	private synchronized void readMessage(Packet packet)
			throws InterruptedException, IOException, InvalidPackageException {

		String message = Utils.sub(packet.getMessage(), "[START]", "[END]");

		// Check package content.
		if (message.indexOf("[LOGIN]") == 0)
			Actions.loginRequested(message, packet.getIp(), packet.getPort());
		else if (message.indexOf("[MESSAGE]") == 0)
			Actions.messageReceived(message);
		else if (message.indexOf("[RESEND]") == 0)
			Actions.resend(message);
		else
			throw new InvalidPackageException(packet.getPosition(), packet.getIp(), packet.getPort());
	}
}