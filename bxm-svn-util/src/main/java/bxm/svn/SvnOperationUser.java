package bxm.svn;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLock;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import bxm.svn.exception.SvnException;

public class SvnOperationUser implements ISvnOperation
{

	private final SVNRepository repository;
	private final List<SvnInfo> allOperationInfos = new ArrayList<SvnInfo>();

	// logger
	final private Logger logger = LoggerFactory.getLogger(SvnOperationUser.class);

	// Constructor
	public SvnOperationUser(String url, String userName, String userPassword)
	{
		try
		{
			SVNRepositoryFactoryImpl.setup();
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
			ISVNAuthenticationManager authManager =
					SVNWCUtil.createDefaultAuthenticationManager(userName, userPassword);
			repository.setAuthenticationManager(authManager);
		}
		catch (Throwable th)
		{
			logger.error(th.getMessage(), th);
			throw new SvnException(th.getMessage(), th);
		}
	}

	@Override
	public byte [] getFileContents(String filePath)
	{
		final SVNProperties fileProperties = new SVNProperties();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try
		{
			repository.getFile(filePath, LATEST_REVISION, fileProperties, baos);
		}
		catch (SVNException e)
		{
			throw new SvnException(e.getMessage(), e);
		}
		return baos.toByteArray();
	}

	@Override
	public void deleteDir(String folderPath)
	{
		SvnInfo info = SvnInfo.createDirInfo(Code.DELETE, folderPath);
		// 동일한 code와 path를 가지는 EimsSvnInfo가 존재하면 다시 추가하지 않고 종료한다.
		if (existSvnInfo(info))
			return;
		allOperationInfos.add(info);
	}

	@Override
	public void deleteFile(String filePath)
	{
		SvnInfo info = SvnInfo.createFileInfo(Code.DELETE, filePath, null);
		// 동일한 code와 path를 가지는 EimsSvnInfo가 존재하면 다시 추가하지 않고 종료한다.
		if (existSvnInfo(info))
			return;
		allOperationInfos.add(info);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void deleteSubEntry(String path)
	{
		Collection entries;
		try
		{
			entries = repository.getDir(path, LATEST_REVISION, null, (Collection) null);
		}
		catch (SVNException e)
		{
			throw new SvnException(e.getMessage(), e);
		}
		Iterator iterator = entries.iterator();
		while (iterator.hasNext())
		{
			SVNDirEntry entry = (SVNDirEntry) iterator.next();

			String deletePath = ( path.equals("") ) ? entry.getName() : path + "/" + entry.getName();
			if (entry.getKind() == SVNNodeKind.DIR)
			{
				deleteDir(deletePath);
			}
			else
			{
				deleteFile(deletePath);
			}
		}
	}

	@Override
	public void addDir(String folderPath)
	{
		SvnInfo info = SvnInfo.createDirInfo(Code.ADD, folderPath);
		// 동일한 code와 path를 가지는 EimsSvnInfo가 존재하면 다시 추가하지 않고 종료한다.
		if (existSvnInfo(info))
			return;
		allOperationInfos.add(info);
	}

	@Override
	public void addFile(String path, byte [] contents)
	{
		SvnInfo info = SvnInfo.createFileInfo(Code.ADD, path, contents);
		// 동일한 code와 path를 가지는 EimsSvnInfo가 존재하면 다시 추가하지 않고 종료한다.
		if (existSvnInfo(info))
			return;
		allOperationInfos.add(info);
	}

	@Override
	public void modifyFile(String path, byte [] contents)
	{
		SvnInfo info = SvnInfo.createFileInfo(Code.MODIFY, path, contents);
		// 동일한 code와 path를 가지는 EimsSvnInfo가 존재하면 다시 추가하지 않고 종료한다.
		if (existSvnInfo(info))
			return;

		info.oldContents = getFileContents(path);
		allOperationInfos.add(info);
	}

	@Override
	public void clear()
	{
		allOperationInfos.clear();
	}

	@Override
	public void commit(String logMessage)
	{
		try
		{
			SvnCommitOperation.commit(repository, logMessage, allOperationInfos);
		}
		catch (SVNException e)
		{
			throw new SvnException(e.getMessage(), e);
		}
	}

	@Override
	public Kind checkPath(String path)
	{

		SVNNodeKind kind;
		try
		{
			kind = repository.checkPath(path, LATEST_REVISION);
		}
		catch (SVNException e)
		{
			throw new SvnException(e.getMessage(), e);
		}
		if (kind == SVNNodeKind.NONE)
		{
			return Kind.NONE;
		}
		else if (kind == SVNNodeKind.FILE)
		{
			return Kind.FILE;
		}
		else if (kind == SVNNodeKind.DIR)
		{
			return Kind.DIR;
		}
		else
		{
			return null;
		}
	}

	public void getLock(String filePath)
	{
		SVNLock lock;
		try
		{
			lock = repository.getLock(filePath);
		}
		catch (SVNException e)
		{
			throw new SvnException(e.getMessage(), e);
		}
		logger.debug("lock = {}", lock);
	}

	public void getLocks(String dirPath)
	{
		SVNLock [] locks;
		try
		{
			locks = repository.getLocks(dirPath);
		}
		catch (SVNException e)
		{
			throw new SvnException(e.getMessage(), e);
		}
		if (logger.isDebugEnabled())
		{
			logger.debug("locks = {}", locks.toString());
		}
	}

	@Override
	public void lock(String filePath)
	{
		Map<String, Long> pathsToRevisions = new HashMap<String, Long>();

		long revision;
		try
		{
			revision = repository.getFile(filePath, LATEST_REVISION, null, null);
			pathsToRevisions.put(filePath, revision);
			logger.debug("lock : {}, {}", filePath, revision);
			repository.lock(pathsToRevisions, "EIMS lock", true, null);
		}
		catch (SVNException e)
		{
			throw new SvnException(e.getMessage(), e);
		}
	}

	@Override
	public void unlock(String filePath)
	{
		SVNLock lock = null;
		try
		{
			lock = repository.getLock(filePath);

			if (lock == null) { return; }

			Map<String, Long> pathsToRevisions = new HashMap<String, Long>();
			pathsToRevisions.put(lock.getPath(), null);
			logger.debug("unlock : {}", filePath);
			repository.unlock(pathsToRevisions, true, null);
		}
		catch (SVNException e)
		{
			throw new SvnException(e.getMessage(), e);
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void locks(String dirPath)
	{
		Map<String, Long> pathsToRevisions = new HashMap<String, Long>();

		try
		{
			Collection entries = repository.getDir(dirPath, LATEST_REVISION, null, (Collection) null);
			Iterator iterator = entries.iterator();
			while (iterator.hasNext())
			{
				SVNDirEntry entry = (SVNDirEntry) iterator.next();
				String path = ( dirPath.equals("") ) ? entry.getName() : dirPath + "/" + entry.getName();
				logger.debug("lock : {}", path);
				pathsToRevisions.put(path, entry.getRevision());
			}

			repository.lock(pathsToRevisions, "EIMS lock", true, null);
		}
		catch (SVNException e)
		{
			throw new SvnException(e.getMessage(), e);
		}

	}

	@Override
	public void unlocks(String dirPath)
	{
		try
		{
			SVNLock [] locks = repository.getLocks(dirPath);
			if (locks == null) { return; }

			Map<String, Long> pathsToRevisions = new HashMap<String, Long>();
			for (SVNLock lock : locks)
			{
				logger.debug("unlock : {}", lock.getPath());
				pathsToRevisions.put(lock.getPath(), null);
			}
			repository.unlock(pathsToRevisions, true, null);
		}
		catch (SVNException e)
		{
			throw new SvnException(e.getMessage(), e);
		}

	}

	private boolean existSvnInfo(SvnInfo info)
	{
		for (Iterator<SvnInfo> infoIterator = allOperationInfos.iterator(); infoIterator.hasNext();)
		{
			SvnInfo tmpInfo = infoIterator.next();
			if (info.code.equals(tmpInfo.code) && info.path.equalsIgnoreCase(tmpInfo.path)) { return true; }
		}

		return false;

	}

}
