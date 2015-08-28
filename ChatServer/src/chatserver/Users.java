package chatserver;

import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Users {

	private static CopyOnWriteArrayList<User> users = new CopyOnWriteArrayList<>();

	public static UUID add(InetAddress ip, int port, String username) {
		UUID id = UUID.randomUUID();
		if (isUsernameInUse(username))
			return null;
		users.add(new User(id, ip, port, username));
		System.out.println("New connection: " + username);
		return id;
	}

	public static boolean isUsernameInUse(String username) {
		for (User user : users)
			if (username.equals(user.getUsername()))
				return true;
		return false;
	}

	public static CopyOnWriteArrayList<User> getUsers() {
		return users;
	}

	public static void remove(User user) {
		users.remove(user);
	}

	public static void remove(UUID id) {
		for (User user : users) {
			if (user.getId() == id) {
				System.out.println("Disconnected: " + user.getUsername());
				users.remove(user);
				Main.periodicConnectionCheck.remove(id);
				return;
			}
		}
	}
}