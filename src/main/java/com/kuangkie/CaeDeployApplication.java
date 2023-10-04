package com.kuangkie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


/**
 *  参加华为项目
 * @author lhb
 */
@SpringBootApplication(exclude = { RedisAutoConfiguration.class,
        MongoAutoConfiguration.class,
        DataSourceAutoConfiguration.class}  )
@ComponentScan("com.kuangkie")
public class CaeDeployApplication {
	static Logger logger = LoggerFactory.getLogger(CaeDeployApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(CaeDeployApplication.class, args);
		String line = null;
		InputStream in2 = null;
		
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		String osName = System.getProperty("os.name");
		if (osName.contains("Linux")) {
			Process process;
			try {
				Runtime runtime = Runtime.getRuntime();
				process = runtime.exec("curl -o /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-vault-8.5.2111.repo");
				int waitFor = process.waitFor();
				int exitValue = process.exitValue();
				if (exitValue == 0) {
					
				} else {
					
				}
				
				process = runtime.exec("yum -y install  java-1.8.0-openjdk-devel");
				waitFor = process.waitFor();
			    exitValue = process.exitValue();
				if (exitValue == 0) {
					
				} else {
					
				}
			
				process = runtime.exec("pwd");
				waitFor = process.waitFor();
				exitValue = process.exitValue();
//				
				in2 = process.getInputStream();
				if (exitValue == 0) {
					logger.info("获取jar路径成功");
				} else {
					logger.info("获取jar路径失败");
				}
				
				inputStreamReader = new InputStreamReader(in2);
				bufferedReader = new BufferedReader(inputStreamReader);
				while ((line = bufferedReader.readLine()) != null) {
					line += line;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

