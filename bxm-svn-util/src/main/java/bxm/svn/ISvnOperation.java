package bxm.svn;

public interface ISvnOperation
{
	/* Constants */
	int LATEST_REVISION = -1; // negative revision number

	public static enum Code
	{
		DELETE, ADD, MODIFY, ADD_MODIFY
	}

	public static enum Kind
	{
		NONE, FILE, DIR, UNKNOWN
	}

	/* file contents */
	public byte [] getFileContents(String filePath);

	public Kind checkPath(String path);

	/* create user commit list */
	public void deleteDir(String path);

	public void deleteFile(String path);

	public void deleteSubEntry(String path);

	public void addDir(String path);

	public void addFile(String path, byte [] contents);

	public void modifyFile(String path, byte [] contents);

	public void clear();

	/* commit operation */
	public void commit(String logMessage);

	// locks
	public void lock(String filePath);

	public void unlock(String filePath);

	public void locks(String dirPath);

	public void unlocks(String dirPath);
}
