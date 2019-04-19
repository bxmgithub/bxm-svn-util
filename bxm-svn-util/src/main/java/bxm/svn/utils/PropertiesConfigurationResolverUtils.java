package bxm.svn.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesConfigurationResolverUtils
{
	protected Logger logger= LoggerFactory.getLogger(PropertiesConfigurationResolverUtils.class);

	public static String CONFIG_FILE_NAME = "eimsConfiguration.properties";

	public void loadProperties(Properties props)
	{
		String configFileName = CONFIG_FILE_NAME;
		try
		{
			//File configFile = ResourceUtils.getFile("classpatch:test.properties");
			//FileInputStream fis = new FileInputStream(configFile);
			//FileInputStream fis = new FileInputStream(configUrl);
			//props.load(fis);

			InputStream configInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( configFileName);
			props.load(configInputStream);
		}
		catch  (IOException e)
		{
			logger.warn("properties loading is failed. config name : {}", configFileName);
		}

	}
}
