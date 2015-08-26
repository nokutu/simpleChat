package chatserver;

public class Utils {

	public static String sub(String s, String start, String end) {
		if (s.indexOf(start) == -1 || s.lastIndexOf(end) == -1 || s.indexOf(start) > s.indexOf(end))
			throw new IllegalArgumentException();
		return s.substring(s.indexOf(start) + start.length(), s.indexOf(end)).trim();
	}
}
