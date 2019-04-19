package bxm.svn;

public class SvnInfo
{
	public boolean isFile;

	public ISvnOperation.Code code;
	public String path;

	public byte [] oldContents;
	public byte [] newContents;

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + code.hashCode();
		result = prime * result + ( ( path == null ) ? 0 : path.hashCode() );
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SvnInfo other = (SvnInfo) obj;
		if (code != other.code)
			return false;
		if (path == null)
		{
			if (other.path != null)
				return false;
		}
		else if (!path.equals(other.path))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "EimsSvnInfo [code=" + code + ", path=" + path + "]";
	}

	public static SvnInfo createFileInfo(ISvnOperation.Code code, String filePath, byte [] contents)
	{

		if (filePath == null) { return null; }

		final SvnInfo info = new SvnInfo();

		info.isFile = true;
		info.code = code;
		info.path = filePath;
		info.newContents = contents;

		return info;
	}

	public static SvnInfo createDirInfo(ISvnOperation.Code code, String folderPath)
	{

		if (folderPath == null) { return null; }

		final SvnInfo info = new SvnInfo();

		info.isFile = false;
		info.code = code;
		info.path = folderPath;

		return info;
	}
}
