package chatserver;

public class Main {

	private static PortListener t;

	public static void main(String[] args) {
		try {
			new Main().run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			t.interrupt();
			t.serverSocket.close();
		}
	}

	private void run() throws InterruptedException {
		t = new PortListener();
		t.start();
		while (t.isAlive())
			Thread.sleep(100);
	}
}
