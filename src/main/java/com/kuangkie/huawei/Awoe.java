package com.kuangkie.huawei;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

import com.kuangkie.dev.fg.BaseConstant;

public class Awoe {

	
	public static void main(String[] args) {
		// 把当前开发的项目code放入配置文件中
		try {
			// 获取本项目的  项目根路径
			File file = new File(ResourceUtils.getURL("classpath:").getPath());
			String programRootPath = file.getParentFile().getParent();
			
			String appFilePath = programRootPath + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "zmzc";
			Long devProgramCode = 9375923L;
			ZmzcAppPropertiesCreater zmzcAppPropertiesCreater = new ZmzcAppPropertiesCreater();
			String appFileStr = zmzcAppPropertiesCreater.create(devProgramCode);
			File resourcesFile = new File(appFilePath, "application.properties");
			FileUtils.writeStringToFile(resourcesFile, appFileStr);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} 
	}
}
