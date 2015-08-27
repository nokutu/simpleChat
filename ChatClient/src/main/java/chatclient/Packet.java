package chatclient;

import java.net.InetAddress;

public class Packet {

	private String message;
	private int pos;
	private InetAddress ip;
	private int port;

	public Packet (String message, int pos, InetAddress ip, int port) {
		this.message = message;
		this.pos = pos;
		this.ip = ip;
		this.port = port;
	}

	public int getPosition() {
		return this.pos;
	}

	public String getMessage() {
		return this.message;
	}

	public InetAddress getIp() {
		return this.ip;
	}

	public int getPort() {
		return this.port;
	}
}
