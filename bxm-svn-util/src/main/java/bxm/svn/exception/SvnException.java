package bxm.svn.exception;

public class SvnException extends RuntimeException
{
	private static final long serialVersionUID = 3290771880622995547L;

	public SvnException()
	{
		super();
	}

	public SvnException(String message)
	{
		super(message);
	}

	public SvnException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SvnException(Throwable cause)
	{
		super(cause);
	}

}
