package com.kuangkie.huawei;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.image.v2.ImageClient;
import com.huaweicloud.sdk.image.v2.model.ImageMediaTaggingItemBody;
import com.huaweicloud.sdk.image.v2.model.ImageMediaTaggingReq;
import com.huaweicloud.sdk.image.v2.model.ImageMediaTaggingResponseResult;
import com.huaweicloud.sdk.image.v2.model.RunImageMediaTaggingRequest;
import com.huaweicloud.sdk.image.v2.model.RunImageMediaTaggingResponse;
import com.huaweicloud.sdk.image.v2.region.ImageRegion;

/**
 * 	把图片中的内容标签识别出来
 * @author lhb
 *
 */
public class ImageService {
	
	 String ak = null;
     String sk = null;
	
	public ImageService(String ak, String sk) {
		this.ak = ak;
		this.sk = sk;
	}
	
    public ImageMediaTaggingItemBody imgTag(byte[] fileByte) {
        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);
        ImageClient client = ImageClient.newBuilder()
                .withCredential(auth)
                .withRegion(ImageRegion.valueOf("cn-north-4"))
                .build();
        RunImageMediaTaggingRequest request = new RunImageMediaTaggingRequest();
        ImageMediaTaggingReq body = new ImageMediaTaggingReq();

        String image = Base64.getEncoder().encodeToString(fileByte);
		body.withImage(image);
        request.withBody(body);
        try {
            RunImageMediaTaggingResponse response = client.runImageMediaTagging(request);
            ImageMediaTaggingResponseResult result = response.getResult();
            List<ImageMediaTaggingItemBody> tags = result.getTags();
            if (tags != null) {
            	ImageMediaTaggingItemBody imageMediaTaggingItemBody = tags.get(0);
            	return imageMediaTaggingItemBody;
            }
            return null;
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
		return null;
    }
}
