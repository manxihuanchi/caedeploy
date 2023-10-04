package com.kuangkie;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Hw {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("hello");
		Runtime runtime = Runtime.getRuntime();
		String pathAfter = "/root/kuangkiesae/324117291074363393/";
		String jarName = "jwoe.jar";
//		Process process = runtime.exec("cd " + pathAfter + " ; jar -cfM0 " + jarName + " BOOT-INF META-INF org");
		Process process = runtime.exec("jar -cfM0 " + jarName + " -C " + pathAfter + " BOOT-INF META-INF org");
		int waitFor = process.waitFor();
		int exitValue = process.exitValue();
		if (exitValue == 0) {
			System.out.println("88888888");
		} else {
			System.out.println("99999999");
		}
		
		process = runtime.exec("pwd");
		waitFor = process.waitFor();
		exitValue = process.exitValue();
		
		InputStream inputStream = process.getInputStream();
		
		if (exitValue == 0) {
			System.out.println("88888888");
		} else {
			System.out.println("99999999");
		}
		
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String line = null;
		
		while ((line = bufferedReader.readLine()) != null) {
			System.out.println(line);
		}
		
//		String string = IOUtils.toString(inputStream);
//		
//		System.out.println(string);
		System.out.println("hello2222...");
		
	}
}
