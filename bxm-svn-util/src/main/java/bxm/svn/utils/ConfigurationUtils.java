package bxm.svn.utils;

import java.util.Properties;

import bxm.svn.exception.SvnException;

public class ConfigurationUtils
{

	private static Properties props = new Properties();

	static
	{
		PropertiesConfigurationResolverUtils resolver = new PropertiesConfigurationResolverUtils();
		resolver.loadProperties(props);
	}

	public static String getProperties(String key)
	{
		String value = props.getProperty(key);
		if (!StringUtils.hasText(value)) throw new SvnException(String.format("Configuration key[%s] is required.", key));
		return value;
	}

	public static String getProperties(String key, String defaultValue)
	{
		return props.getProperty(key, defaultValue);
	}

	public static String L1_APP_ENG_ACRONYM_REPLACEMENT_AT_PACKAGE = "bxm.tool.design.l1.app.eng.acronym.replace.at.pkg";

	/** 배포대상 시스템 리스트 (CSV형태 */
	public static final String SYSTEM_CODE_LIST = "system.code.list";

	public static final String EIMS_ENABLE_IMAGE_LOG = "eims.enable.image.log";


}
