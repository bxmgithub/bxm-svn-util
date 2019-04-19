package bxm.svn.model;

public class RepositoryFileInfo
{
	/**
	 * repostory 상의 path
	 */
	String repositoryPath;

	public String getRepositoryPath()
	{
		return repositoryPath;
	}

	public void setRepositoryPath(String repositoryPath)
	{
		this.repositoryPath = repositoryPath;
	}

	@Override
	public String toString()
	{
		return "RepositoryFileInfo [repositoryPath=" + repositoryPath + "]";
	}

}
