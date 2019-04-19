package bxm.svn;

import java.util.List;
import java.util.Map;

public interface RepositoryProcessor
{
	public void executeCommand(Map<String, byte []> ommMapm, Map<Object, Object> context);

	public void init();

	public void updateFileAtRepository(String repositoryPath, byte [] dtoFileBytes, String deployerId, String commitMessage)
			throws Exception;

	public void deleteFileAtRepository(String repositoryPath, String deployerId, String commitMessage) throws Exception;

	public void executeDeleteCommand(List<String> list, Map<Object, Object> context);

	public void commit(String commitMessage);

	public void lock(String repositoryPath);

	public void lock();

	public void unlock();

}
