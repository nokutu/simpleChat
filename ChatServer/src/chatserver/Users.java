package chatserver;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.UUID;

public class Users {

	private static ArrayList<User> users = new ArrayList<>();

	public static UUID add(InetAddress ip, int port, String username) {
		UUID id = UUID.randomUUID();
		if (isUsernameInUse(username))
			return null;
		users.add(new User(id, ip, port, username));
		return id;
	}

	public static boolean isUsernameInUse(String username) {
		for (User user : users)
			if (username.equals(user.getUsername()))
				return true;
		return false;
	}

	public static ArrayList<User> getUsers() {
		return users;
	}

	public static void remove(User user) {
		users.remove(user);
	}

	public static void remove(UUID id) {
		for (User user : users) {
			if (user.getId() == id) {
				users.remove(user);
				return;
			}
		}
	}
}