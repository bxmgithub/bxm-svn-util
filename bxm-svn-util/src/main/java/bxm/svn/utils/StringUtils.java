package bxm.svn.utils;

public class StringUtils
{
	private static final String FOLDER_SEPARATOR= "/";
	private static final char EXTENSION_SEPARATOR= '.';
	
	public static boolean hasLength(CharSequence str)
	{
		return ( str != null && str.length() > 0 );
	}

	public static boolean hasText(CharSequence str)
	{
		if (!hasLength(str))
			return false;
		int len = str.length();
		for (int i = 0; i < len; i++)
		{
			if (!Character.isWhitespace(str.charAt(i)))
				return true;
		}
		return false;
	}

	public static boolean hasText(String str)
	{
		return hasText((CharSequence) str);
	}

	public static String replace(String in, String old, String to)
	{
		if (!hasLength(in) || !hasLength(old) || to == null)
			return in;
		StringBuilder sb = new StringBuilder();
		int pos = 0;
		int index = in.indexOf(old);
		int patLen = old.length();
		while (index >= 0)
		{
			sb.append(in.substring(pos, index));
			sb.append(to);
			pos = index + patLen;
			index = in.indexOf(old, pos);
		}
		sb.append(in.substring(pos));
		return sb.toString();
	}

	public static String stripFilenameExtension(String path)
	{
		if (path == null)
			return null;
		int extIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (extIndex == -1)
			return path;
		int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (folderIndex > extIndex)
			return path;
		return path.substring(0, extIndex);
	}
}
