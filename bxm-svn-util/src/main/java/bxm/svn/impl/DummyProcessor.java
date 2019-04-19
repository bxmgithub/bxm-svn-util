package bxm.svn.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bxm.svn.ISvnOperation;
import bxm.svn.RepositoryProcessor;

public class DummyProcessor implements RepositoryProcessor
{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ISvnOperation operation;

	@Override
	public void init()
	{
		logger.info("DummyProcessor is created . ");

	}

	@Override
	public void updateFileAtRepository(String repositoryPath, byte [] dtoFileBytes, String deployerId, String commitMessage)
			throws Exception
	{
		logger.debug("updateFileAtRepository - repositoryPath : {}", repositoryPath);
	}

	@Override
	public void deleteFileAtRepository(String repositoryPath, String deployerId, String commitMessage) throws Exception
	{
		logger.debug("deleteFileAtRepository - repositoryPath : {}", repositoryPath);

	}

	@Override
	public void commit(String commitMessage)
	{
		logger.debug("commit - commitMessage : {}", commitMessage);
	}

	@Override
	public void lock(String repositoryPath)
	{

		logger.debug("lock - repositoryPath : {}", repositoryPath);

	}

	@Override
	public void lock()
	{

		logger.debug("lock is called.");

	}

	@Override
	public void unlock()
	{
		logger.debug("unlock is called.");
	}

	@Override
	public void executeCommand(Map<String, byte []> ommMapm, Map<Object, Object> context)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeDeleteCommand(List<String> list, Map<Object, Object> context)
	{
		// TODO Auto-generated method stub
		
	}
}
