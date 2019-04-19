
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;

import bxm.svn.ISvnOperation;
import bxm.svn.SvnOperationFactory;

public class TestSvn
{

	private static final String url = "svn://localhost:3690/test/";
	private static final String userName = "";
	private static final String userPassword = "";
	private static final String UUID = "1111";
	
	final Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String [] args) throws SVNException
	{
		new TestSvn().run();
	}

	void run() throws SVNException
	{
		String dirpath = "C:/eclipse-oxygen/workspaces_bxmv4/bxm-svn-util/src/test/resources/SvnTestIO.omm";

		logger.debug("start");

		String [] [] lists = { { dirpath, "test content!!!!!!!!!!!!!!!!!!!!!" } };

		ISvnOperation operation = SvnOperationFactory.create(url, userName, userPassword);

		for (String [] fileInfo : lists)
		{
			String file = fileInfo[0];
			byte [] contents = fileInfo[1].getBytes();

			switch (operation.checkPath(file))
			{
				case NONE :
					operation.addFile(file, contents);
					break;

				case FILE :
					logger.info("modifyFile...");
					operation.unlock(file);
					operation.modifyFile(file, contents);
					break;

				default :
					throw new SVNException(SVNErrorMessage.create(	SVNErrorCode.BAD_FILENAME,
																	"The {0} file can't create ",
																	file));
			}
		}

		operation.commit("Deploy " + UUID);

		// lock
		for (String [] fileInfo : lists)
		{
			String file = fileInfo[0];
			operation.lock(file);
		}

		logger.debug("end");
	}

	void deleteAllAndCreate() throws SVNException
	{

		String dirpath = "src/dco/infid3";

		logger.debug("start");

		ISvnOperation operation = SvnOperationFactory.create(url, userName, userPassword);

		if (operation.checkPath("src") != ISvnOperation.Kind.DIR) { throw new RuntimeException(); }

		if (operation.checkPath("src/dco") == ISvnOperation.Kind.NONE)
		{
			operation.addDir("src/dco");
		}

		operation.unlocks(dirpath);
		if (operation.checkPath(dirpath) != ISvnOperation.Kind.NONE)
		{
			operation.deleteDir(dirpath);
		}

		operation.addDir(dirpath);
		operation.addFile(dirpath + "/infid_S_in.omm", "new file".getBytes());
		operation.addFile(dirpath + "/infid_S_out.omm", "new file".getBytes());
		operation.addFile(dirpath + "/infid_R_in.omm", "new file".getBytes());
		operation.addFile(dirpath + "/infid_R_out.omm", "new file".getBytes());

		operation.addFile(dirpath + "/infid_S.map", "new file".getBytes());
		operation.addFile(dirpath + "/infid_R.map", "new file".getBytes());

		operation.commit("EIMS Deploy " + UUID);

		operation.locks(dirpath);

		logger.debug("end");
	}
}
