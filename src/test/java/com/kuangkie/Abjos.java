package com.kuangkie;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Abjos {

	
	public static void main(String[] args) throws InterruptedException, IOException {
		System.out.println(	);
		// 打成新的jar
			Runtime runtime = Runtime.getRuntime();
			String pathAfter = "C:\\Users\\chuyin\\kuangkiesae\\319120496057425921";
			String jarName = "FWEO.jar";
			Process process = runtime.exec("cmd /c cd " + pathAfter + " && jar -cfM0 " + jarName + " BOOT-INF META-INF org");
			
			int waitFor = process.waitFor();
			int exitValue = process.exitValue();
			if (exitValue == 0) {
			} else {
			}
	}
}
