package com.kuangkie.huawei;

import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.cae.v1.region.CaeRegion;
import com.huaweicloud.sdk.cae.v1.*;
import com.huaweicloud.sdk.cae.v1.model.*;

/**
 * 	删除组件
 * @author lhb
 */
public class DeleteComponentSolution {

	 private String ak = null;
	 private String sk = null;
	 
	 CaeClient client = null;
	
	public DeleteComponentSolution(String ak,String sk) {
		this.ak = ak;
		this.sk = sk;
	   ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);
	   client = CaeClient.newBuilder()
                .withCredential(auth)
                .withRegion(CaeRegion.valueOf("cn-east-3"))
                .build();
	}
	
    public boolean deleteComponent(String environmentID, String applicationId, String componentId) {
    	 try {
	    	DeleteComponentRequest request = new DeleteComponentRequest();
	        request.withApplicationId(applicationId);
	        request.withComponentId(componentId);
	        request.withXEnvironmentID(environmentID);
            DeleteComponentResponse response = client.deleteComponent(request);
            int httpStatusCode = response.getHttpStatusCode();
            if (httpStatusCode == 200) {
            	return true;
            }
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
		return false;
    }
}

