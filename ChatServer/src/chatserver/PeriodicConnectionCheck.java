package chatserver;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PeriodicConnectionCheck extends Thread {

	private ConcurrentHashMap<UUID, Long> record;

	public PeriodicConnectionCheck() {
		record = new ConcurrentHashMap<>();
	}

	@Override
	public void run() {
		while (true) {
			for (User user : Users.getUsers()) {
				if (System.currentTimeMillis() - record.get(user.getId()) > 15000) {
					Users.remove(user.getId());
				}
				try {
					Actions.sendCheck(user.getIp(), user.getPort());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			synchronized (this) {
				try {
					this.wait(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void received(UUID id) {
		record.put(id, System.currentTimeMillis());
	}

	public void remove(UUID id) {
		record.remove(id);
	}
}
