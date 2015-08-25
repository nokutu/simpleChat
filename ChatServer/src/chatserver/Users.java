package chatserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Users {
	private static ConcurrentHashMap<UUID, InetAddress> ips = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<UUID, Integer> ports = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<UUID, String> usernames = new ConcurrentHashMap<>();

	public static UUID add(InetAddress ip, int port, String username) {
		UUID id = UUID.randomUUID();
		if (usernames.containsValue(username))
			return null;
		ips.put(id, ip);
		ports.put(id, port);
		usernames.put(id, username);
		return id;
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

		for (UUID id : ips.keySet()) {
			DatagramPacket sendPacket = new DatagramPacket(output.toString()
					.getBytes(), output.toString().getBytes().length, ips.get(id),
					ports.get(id));
			serverSocket.send(sendPacket);
		}
	}
}