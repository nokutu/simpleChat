package chatclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class NetworkManager {

	public static DatagramPacket[] history = new DatagramPacket[128];
	private static int pos = 0;

	public synchronized static void send(String message, InetAddress ip, int port)
			throws IOException {
		System.out.println("Client out: " +  message);

		byte[] content = new byte[message.getBytes().length + 2];
		content[0] = ((Integer) message.length()).byteValue();
		content[1] = ((Integer) pos).byteValue();
		for (int i = 0; i < message.getBytes().length; i++)
			content[i + 2] = message.getBytes()[i];
		DatagramPacket sendPacket = new DatagramPacket(content, content.length, ip,
				port);
		history[pos] = sendPacket;
		pos = (pos + 1) % 128;
		Main.main.serverSocket.send(sendPacket);
	}

	public static Packet getPacket(DatagramPacket receivePacket)
			throws InvalidPackageException, IOException {
		Main.main.serverSocket.receive(receivePacket);
		byte[] content = receivePacket.getData();
		Byte length = content[0];
		Byte packageNumber = content[1];
		byte[] messageBytes = new byte[1024];
		for (int i = 0; i < 1022; i++)
			messageBytes[i] = content[i + 2];
		String message = new String(messageBytes).trim().substring(0, length);
		System.out.println("Client in: " + message);
		return new Packet(message, packageNumber, receivePacket.getAddress(),
				receivePacket.getPort());
	}

	public static class InvalidPackageException extends Exception {

		private static final long serialVersionUID = 2372508495514901297L;

		private int packageNumber;
		private InetAddress ip;
		private int port;

		public InvalidPackageException(int packageNumber, InetAddress ip, int port) {
			this.packageNumber = packageNumber;
			this.ip = ip;
			this.port = port;
		}

		public int getPackageNumber() {
			return packageNumber;
		}

		public InetAddress getIp() {
			return this.ip;
		}

		public int getPort() {
			return this.port;
		}
	}
}
