package bxm.svn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bxm.svn.utils.ConfigurationUtils;

public class RepositoryProcessorFactory 
{
	protected static Logger logger= LoggerFactory.getLogger(RepositoryProcessorFactory.class);

	private static Class<?> processorClazz = null;

	static {
		String processorClassName = ConfigurationUtils.getProperties("eims.repository.processor.class.name");
		logger.debug("RepositoryProcessor is now loading. class name : {}", processorClassName);
		try {
			processorClazz = Thread.currentThread().getContextClassLoader().loadClass(processorClassName);
		} catch (Exception e) {
			logger.error("RepositoryProcessor initializing is failed. class name : {}", processorClassName);
		}
	}

	public static RepositoryProcessor newRepositoryProcessor() {

		RepositoryProcessor processor = null;

		try {
			processor = (RepositoryProcessor) processorClazz.newInstance();
		} catch (Exception e) {
			logger.error("RepositoryProcessor initializing is failed. class name : {}", processorClazz.getCanonicalName());
		}

		processor.init();

		return processor;

	}
}
