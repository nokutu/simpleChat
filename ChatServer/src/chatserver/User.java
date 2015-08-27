package chatserver;

import java.net.InetAddress;
import java.util.UUID;

public class User {

	private UUID id;
	private InetAddress ip;
	private int port;
	private String username;

	public User(UUID id, InetAddress ip, int port, String username) {
		this.id = id;
		this.ip = ip;
		this.port = port;
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public UUID getId() {
		return this.id;
	}

	public InetAddress getIp() {
		return this.ip;
	}
	public int getPort() {
		return this.port;
	}

}
