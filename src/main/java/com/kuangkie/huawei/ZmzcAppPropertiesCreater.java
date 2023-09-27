package com.kuangkie.huawei;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;

/**
 * 	加载配置文件内容
 * @author lhb
 *
 */
public class ZmzcAppPropertiesCreater {

	public ZmzcAppPropertiesCreater() {

	}

	public String create(Long programCode) throws IOException {
		StringBuilder sb = new StringBuilder();

		
		InputStream resourceAsStream = ZmzcAppPropertiesCreater.class.getResourceAsStream("/zmzc/application-tmpl.properties");
		String readFileToString = IOUtils.toString(resourceAsStream);
		String appPropertiesPattern ="(\\$\\{([programCode\\}]+)\\})";

		String replaceFirst = readFileToString.replaceFirst(appPropertiesPattern, programCode + "");
		sb.append(replaceFirst);
		return sb.toString();
	}

}
