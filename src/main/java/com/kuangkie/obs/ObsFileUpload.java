package com.kuangkie.obs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;

/**
 * 	上传文件到obs
 * @author lhb
 *
 */
public class ObsFileUpload {

	
	public static void main(String[] args) throws FileNotFoundException {
		// Endpoint以北京四为例，其他地区请按实际情况填写。
		
		String endPoint = "https://obs.cn-east-3.myhuaweicloud.com";
		 String ak = "O6ZWAM2BZUSYNZJHENAB";
         String sk = "e6fhIkg3IfeEbQlLiy9Och24lbbOANcLTBWDB1rQ";
		// 创建ObsClient实例
		ObsClient obsClient = new ObsClient(ak, sk, endPoint);

		// 待上传的本地文件路径，需要指定到具体的文件名
		FileInputStream fis = new FileInputStream(new File("C:\\Users\\chuyin\\Desktop\\12.jpg"));  
		PutObjectResult putObject = obsClient.putObject("kuangkie", "12.jpg", fis);
		
		String objectUrl = putObject.getObjectUrl();
		System.out.println(objectUrl);
		
		// 待上传的本地文件路径，需要指定到具体的文件名
//		FileInputStream fis2 = new FileInputStream(new File("localfile2"));
//		PutObjectRequest request = new PutObjectRequest();
//		request.setBucketName("bucketname");
//		request.setObjectKey("objectname2");
//		request.setInput(fis2);
//		obsClient.putObject(request);

	}
	
}
