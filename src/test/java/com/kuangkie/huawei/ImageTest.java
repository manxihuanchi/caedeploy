package com.kuangkie.huawei;

import com.huaweicloud.sdk.core.auth.ICredential;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.image.v2.region.ImageRegion;
import com.huaweicloud.sdk.image.v2.*;
import com.huaweicloud.sdk.image.v2.model.*;

/**
 * 	把图片中的内容标签识别出来
 * @author lhb
 *
 */
public class ImageTest {

    public static void main(String[] args) throws IOException {
        String ak = Ak.ak;
        String sk =  Ak.sk;

        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);

        ImageClient client = ImageClient.newBuilder()
                .withCredential(auth)
                .withRegion(ImageRegion.valueOf("cn-north-4"))
                .build();
        RunImageMediaTaggingRequest request = new RunImageMediaTaggingRequest();
        ImageMediaTaggingReq body = new ImageMediaTaggingReq();

        
        File file = new File("C:\\Users\\chuyin\\Desktop\\人.jpg");
        byte[] fileByte = FileUtils.readFileToByteArray(file);
        
        String image = Base64.getEncoder().encodeToString(fileByte);
		body.withImage(image);
        
//        body.withUrl("https://img0.baidu.com/it/u=2686955487,3501942157&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=750");
        request.withBody(body);
        try {
            RunImageMediaTaggingResponse response = client.runImageMediaTagging(request);
           
            ImageMediaTaggingResponseResult result = response.getResult();
            
            List<ImageMediaTaggingItemBody> tags = result.getTags();
            for (ImageMediaTaggingItemBody imageMediaTaggingItemBody : tags) {
            	String tag = imageMediaTaggingItemBody.getTag();
            	System.out.println(tag);
			}
            
            
            System.out.println(response.toString());
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (RequestTimeoutException e) {
            e.printStackTrace();
        } catch (ServiceResponseException e) {
            e.printStackTrace();
            System.out.println(e.getHttpStatusCode());
            System.out.println(e.getRequestId());
            System.out.println(e.getErrorCode());
            System.out.println(e.getErrorMsg());
        }
    }
}
