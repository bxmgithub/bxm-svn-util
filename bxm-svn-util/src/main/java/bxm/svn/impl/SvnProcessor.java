package bxm.svn.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bxm.svn.ISvnOperation;
import bxm.svn.RepositoryProcessor;
import bxm.svn.SvnConstants;
import bxm.svn.SvnOperationFactory;
import bxm.svn.exception.SvnException;
import bxm.svn.model.RepositoryFileInfo;
import bxm.svn.utils.ConfigurationUtils;
import bxm.svn.utils.EncryptionUtils;
import bxm.svn.utils.StringUtils;

public class SvnProcessor implements RepositoryProcessor
{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private ISvnOperation operation;

	private ArrayList<RepositoryFileInfo> lockFilePaths;

	@Override
	public void init()
	{
		String url = ConfigurationUtils.getProperties("eims.repository.url");
		String userName = ConfigurationUtils.getProperties("eims.repository.user");
		String encryptUserPassword = ConfigurationUtils.getProperties("eims.repository.password");

		if (!StringUtils.hasText(url))
			throw new SvnException("eims.repository.url is required!");
		if (!StringUtils.hasText(userName))
			throw new SvnException("eims.repository.user is required!");
		if (!StringUtils.hasText(encryptUserPassword))
			throw new SvnException("eims.repository.password is required!");

		String userPassword = "";
		userPassword = EncryptionUtils.decrypt(encryptUserPassword);
		// logger.debug("userPassword : {}, decryptPassword : {}", encryptUserPassword,
		// userPassword);
		this.operation = SvnOperationFactory.create(url, userName, userPassword);

		logger.info("Svn Operation created : {} ", new Object [] { this.operation });

		this.lockFilePaths = new ArrayList<RepositoryFileInfo>();

	}

	@Override
	public void
			updateFileAtRepository(String repositoryPath, byte [] dtoFileBytes, String deployerId, String commitMessage)
					throws Exception
	{
		logger.debug("repositoryPath : {}", repositoryPath);
		String [] pathArray = null;
		pathArray = repositoryPath.split("/");

		String projectName = pathArray[0];

		if (operation.checkPath(projectName) != ISvnOperation.Kind.DIR) { throw new SvnException("Project is not creadted : "
				+ projectName); }

		// String fordPath = "";
		StringBuffer fordPath = new StringBuffer();
		for (int i = 1; i < pathArray.length - 1; i++)
		{
			fordPath.append("/");
			fordPath.append(pathArray[i]);
			String dirPath = projectName + fordPath.toString();
			if (operation.checkPath(dirPath) != ISvnOperation.Kind.DIR)
			{
				logger.debug("{} is not exist at repository. now created directory.", dirPath);
				operation.addDir(dirPath);
			}
		}

		RepositoryFileInfo info = null;
		switch (operation.checkPath(repositoryPath))
		{
			case NONE :
				operation.addFile(repositoryPath, dtoFileBytes);
				info = new RepositoryFileInfo();
				info.setRepositoryPath(repositoryPath);
				this.lockFilePaths.add(info);
				break;

			case FILE :
				operation.unlock(repositoryPath);
				operation.modifyFile(repositoryPath, dtoFileBytes);
				info = new RepositoryFileInfo();
				info.setRepositoryPath(repositoryPath);
				this.lockFilePaths.add(info);
				break;

			default :
				throw new SvnException(String.format("The %s file can't create ", repositoryPath));
		}

	}

	@Override
	public void deleteFileAtRepository(String repositoryPath, String deployerId, String commitMessage) throws Exception
	{
		switch (operation.checkPath(repositoryPath))
		{
			case NONE :
				logger.debug("Deleting file({}) is not exist at repository.", repositoryPath);
				break;

			case FILE :
				operation.unlock(repositoryPath);
				operation.deleteFile(repositoryPath);
				break;

			default :
				throw new SvnException(String.format("The %s file can't delete ", repositoryPath));
		}

	}

	@Override
	public void commit(String commitMessage)
	{
		this.operation.commit(commitMessage);
	}

	@Override
	public void lock(String repositoryPath)
	{
		this.operation.lock(repositoryPath);
	}

	@Override
	public void lock()
	{

		for (RepositoryFileInfo info : this.lockFilePaths)
		{
			this.operation.lock(info.getRepositoryPath());
		}
	}

	public void unlock()
	{
		for (RepositoryFileInfo info : this.lockFilePaths)
		{
			this.operation.unlock(info.getRepositoryPath());
		}
	}

	@Override
	public void executeCommand(Map<String, byte []> ommMap, Map<Object, Object> context)
	{
		String deployStatus = (String) context.get(SvnConstants.DEPLOY_STATUS_KEY_NAME);
		
		switch (deployStatus)
		{
			case "DEPLOY" :
				executeDeployCommand(ommMap, context);
				break;
			case "ROLLBACK" :
				executeRollbackCommand(ommMap, context);
				break;
			case "DELETE" :
				// executeDeleteCommand(ommMap);
				break;
		}
	}

	private void executeDeployCommand(Map<String, byte []> ommMap, Map<Object, Object> context)
	{
		boolean isExecuteCommand = false;
		String deployId = (String) context.get(SvnConstants.DEPLOY_ID_KEY_NAME);
		String commitMessage = deployId;
		try
		{
			for (String repositoryPath : ommMap.keySet())
			{
				logger.debug("Deploy command start... resposiotryPath : [{}]", repositoryPath);
				updateFileAtRepository(repositoryPath, ommMap.get(repositoryPath), deployId, commitMessage);
				isExecuteCommand = true;
			}
		}
		catch (Throwable th)
		{
			logger.error(th.getMessage(), th);
			throw new RuntimeException(th);
		}

		if (isExecuteCommand)
		{
			commit(commitMessage);
			for (String repositoryPath : ommMap.keySet())
			{
				lock(repositoryPath);
			}
		}
	}

	private void executeRollbackCommand(Map<String, byte []> ommMap, Map<Object, Object> context)
	{
		boolean isExecuteCommand = false;
		String deployId = (String) context.get(SvnConstants.DEPLOY_ID_KEY_NAME);
		String commitMessage = deployId;
		try
		{
			for (String repositoryPath : ommMap.keySet())
			{
				logger.debug("Rollback command start... resposiotryPath : [{}]", repositoryPath);
				updateFileAtRepository(repositoryPath, ommMap.get(repositoryPath), deployId, commitMessage);
				isExecuteCommand = true;
			}
		}
		catch (Throwable th)
		{
			logger.error(th.getMessage(), th);
			throw new RuntimeException(th);
		}

		if (isExecuteCommand)
		{
			commit(commitMessage);
			for (String repositoryPath : ommMap.keySet())
			{
				lock(repositoryPath);
			}
		}
	}

	private void executeDeleteCommand(Map<String, byte []> ommMap, Map<Object, Object> context)
	{
		boolean isExecuteCommand = false;
		String deployId = (String) context.get(SvnConstants.DEPLOY_ID_KEY_NAME);
		String commitMessage = deployId;
		try
		{
			for (String repositoryPath : ommMap.keySet())
			{
				logger.debug("Delete command start... resposiotryPath : [{}]", repositoryPath);

				String srcJavaPath = StringUtils.replace(repositoryPath, "/src/", "/src-gen/");
				srcJavaPath = StringUtils.stripFilenameExtension(srcJavaPath) + ".java";

				deleteFileAtRepository(repositoryPath, deployId, commitMessage);
				deleteFileAtRepository(srcJavaPath, deployId, commitMessage);
				isExecuteCommand = true;
			}
		}
		catch (Throwable th)
		{
			logger.error(th.getMessage(), th);
			throw new RuntimeException(th);
		}

		if (isExecuteCommand)
		{
			commit(commitMessage);
		}
	}

	public void executeDeleteCommand(List<String> list, Map<Object, Object> context)
	{
		boolean isExecuteCommand = false;

		String deployId = (String) context.get(SvnConstants.DEPLOY_ID_KEY_NAME);
		String commitMessage = deployId;
		try
		{
			for (String repositoryPath : list)
			{
				logger.debug("Delete command start... resposiotryPath : [{}]", repositoryPath);

				String srcJavaPath = StringUtils.replace(repositoryPath, "/src/", "/src-gen/");
				srcJavaPath = StringUtils.stripFilenameExtension(srcJavaPath) + ".java";

				deleteFileAtRepository(repositoryPath, deployId, commitMessage);
				deleteFileAtRepository(srcJavaPath, deployId, commitMessage);
				isExecuteCommand = true;
			}
		}
		catch (Throwable th)
		{
			logger.error(th.getMessage(), th);
			throw new RuntimeException(th);
		}

		if (isExecuteCommand)
		{
			commit(commitMessage);
		}
	}
}
