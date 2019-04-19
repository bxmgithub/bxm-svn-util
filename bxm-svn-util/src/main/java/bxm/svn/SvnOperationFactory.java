package bxm.svn;

public class SvnOperationFactory
{
	private SvnOperationFactory()
	{
	}

	public static ISvnOperation create(String url, String userName, String userPassword)
	{
		return new SvnOperationUser(url, userName, userPassword);
	}
}
