package chatclient;

import java.net.UnknownHostException;

public class PeriodicConnectionCheck extends Thread {

	public long lastCheck;

	@Override
	public void run() {
		while (true) {
			if (lastCheck != 0) {
				if (System.currentTimeMillis() - lastCheck > 5000L)
					try {
						Actions.disconnect();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
			}
			synchronized (this) {
				try {
					this.wait(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
