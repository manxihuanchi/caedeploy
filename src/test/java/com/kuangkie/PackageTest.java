package com.kuangkie;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

public class PackageTest {

	public static Logger logger = LoggerFactory.getLogger(PackageTest.class);
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		boolean mvnPackage = mvnPackage();
		if (mvnPackage) {
			logger.info("打成jar成功！");
			// 获取本项目的  项目根路径
			File file = new File(ResourceUtils.getURL("classpath:").getPath());
			String programRootPath = file.getParentFile().getParent();
			String jarPath = programRootPath + File.separator + "target" + File.separator +  "caedeploy.jar";
			
	        File jarFile = new File(jarPath);
	        if (jarFile == null) {
	        	logger.warn("没有找到jar文件！");
			}
	        logger.warn("已经找到jar文件！");
	        
	        // 把此文件拷贝到 zmzc/sae 下， 进行第二次打包
//	        C:\soft\eclipsework\kuangkie\caedeploy\src\main\resources\zmzc\sae
	        String targetPath = programRootPath + File.separator  + "src"+File.separator +"main"+File.separator +"resources"+File.separator +"zmzc"+File.separator +"sae" + File.separator +"basejar.zip";
	        File targetFile = new File(targetPath);
			FileUtils.copyFile(jarFile, targetFile);
			
			// 重新打包
			boolean mvnPackage2 = mvnPackage();
			if (mvnPackage2) {
				logger.info("第二次打包成功！");
				// 打包成功后， 找到此jar， 上传到文件服务器上
				File jarFile2 = new File(jarPath);
		        if (jarFile2 == null) {
		        	logger.warn("没有找到jar文件！");
				}
		        logger.warn("已经找到jar文件！");
				
				// 开始上传文件到obs
		        
		        // 上线此项目
			        
				// 启动应用
				
			} else {
				logger.info("第二次打包失败！");
			}
		} else {
			logger.error("打成jar失败！");
		}
		
		
		
	}
	
	
	
	public static boolean mvnPackage() {
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		
		BufferedReader br = null;
		try {
			// 打成新的jar
			Runtime runtime = Runtime.getRuntime();
			String pathAfter = "";
			Process process = runtime.exec("cmd /c cd " + pathAfter + " && mvn clean compile package -Pzmzc -Dmaven.test.skip");
			inputStream = process.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			br = new BufferedReader(inputStreamReader);
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				Thread.sleep(100);
			}
			
			int waitFor = process.waitFor();
			int exitValue = process.exitValue();
			if (exitValue == 0) {
				logger.info("打成jar成功！");
				return true;
		        // 开始上传文件到文件服务器
			} else {
				logger.error("打成jar失败！");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	
	
}
