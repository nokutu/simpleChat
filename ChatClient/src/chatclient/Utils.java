package chatclient;

public class Utils {

	public static String sub(String s, String start, String end) {
		if (s.indexOf(start) == -1 || s.lastIndexOf(end) == -1 || s.indexOf(start) > s.indexOf(end))
			throw new IllegalArgumentException();
		return s.substring(s.indexOf(start) + start.length(), s.indexOf(end)).trim();
	}

	public static boolean validIP (String ip) {
    try {
        if ( ip == null || ip.isEmpty() ) {
            return false;
        }

        String[] parts = ip.split( "\\." );
        if ( parts.length != 4 ) {
            return false;
        }

        for ( String s : parts ) {
            int i = Integer.parseInt( s );
            if ( (i < 0) || (i > 255) ) {
                return false;
            }
        }
        if ( ip.endsWith(".") ) {
            return false;
        }

        return true;
    } catch (NumberFormatException nfe) {
        return false;
    }
}
}
