package bxm.svn;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;

public class SvnCommitOperation
{
	private final ISVNEditor editor;
	private final List<SvnInfo> allOperationInfos;

	final Logger logger = LoggerFactory.getLogger(getClass());

	private SvnCommitOperation(SVNRepository repository, String logMessage, List<SvnInfo> allOperationInfos)
			throws SVNException
	{
		this.editor = repository.getCommitEditor(logMessage, null);
		this.allOperationInfos = allOperationInfos;
	}

	public static void commit(SVNRepository repository, String logMessage, List<SvnInfo> allOperationInfos)
			throws SVNException
	{
		new SvnCommitOperation(repository, logMessage, allOperationInfos).commit();
	}

	protected void commit() throws SVNException
	{
		/**
		 * RootPath 잘 입력하기. Case 1. svn://url:4400/bcc/Develop 에 배포하는거면 repository.url :
		 * svn://url:4400/bcc eims.repository.root.dir = Develop
		 *
		 * Case 2. svn://url:5500/bxm 이면, repository.url : svn://url:5500/bxm
		 * eims.repository.root.dir : .
		 */
		editor.openRoot(-1);
		svnOperations();
		editor.closeDir();

		SVNCommitInfo commitInfo = editor.closeEdit();
		logger.debug("End of Commit Operatioin : {} ", commitInfo);
	}

	protected void svnOperations() throws SVNException
	{

		for (SvnInfo info : allOperationInfos)
		{

			logger.debug("Operation = {}", info);

			switch (info.code)
			{
				case DELETE :
					logger.debug("DELETE = {}", info.path);
					editor.deleteEntry(info.path, -1);
					break;

				case ADD :
					if (info.isFile)
					{
						logger.debug("ADD File = {}", info.path);
						createFile(info);
					}
					else
					{
						logger.debug("ADD Dir = {}", info.path);
						editor.addDir(info.path, null, -1);
						editor.closeDir();
					}
					break;

				case MODIFY :
					if (info.isFile)
					{
						logger.debug("Modify File = {}", info.path);
						modifyFile(info);
					}
					else
					{
						throwUnknownException("MODIFY Operation used Only File Type {0}, {1}", info.path, info.isFile);
					}
					break;

				default :
					throwUnknownException("Operation Code is Unknown {0}", info.code);
			}
		}
	}

	protected void createFile(SvnInfo info) throws SVNException
	{

		editor.addFile(info.path, null, ISvnOperation.LATEST_REVISION);
		editor.applyTextDelta(info.path, null);

		final SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
		String checksum = deltaGenerator.sendDelta(info.path, new ByteArrayInputStream(info.newContents), editor, true);

		editor.closeFile(info.path, checksum);
	}

	protected void modifyFile(SvnInfo info) throws SVNException
	{

		editor.openFile(info.path, ISvnOperation.LATEST_REVISION);
		editor.applyTextDelta(info.path, null);

		final SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
		String checksum = deltaGenerator.sendDelta(	info.path,
													new ByteArrayInputStream(info.oldContents),
													0,
													new ByteArrayInputStream(info.newContents),
													editor,
													true);

		editor.closeFile(info.path, checksum);
	}

	protected void throwUnknownException(String format, Object... objs) throws SVNException
	{
		throw new SVNException(SVNErrorMessage.create(SVNErrorCode.UNKNOWN, format, objs));
	}
}
