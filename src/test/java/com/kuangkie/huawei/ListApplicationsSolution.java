package com.kuangkie.huawei;

import java.util.List;

import com.huaweicloud.sdk.cae.v1.CaeClient;
import com.huaweicloud.sdk.cae.v1.model.ApplicationItem;
import com.huaweicloud.sdk.cae.v1.model.ListApplicationsRequest;
import com.huaweicloud.sdk.cae.v1.model.ListApplicationsResponse;
import com.huaweicloud.sdk.cae.v1.region.CaeRegion;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;


public class ListApplicationsSolution {

    public static void main(String[] args) {
    	 String ak = "O6ZWAM2BZUSYNZJHENAB";
         String sk = "e6fhIkg3IfeEbQlLiy9Och24lbbOANcLTBWDB1rQ";

        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);

        CaeClient client = CaeClient.newBuilder()
                .withCredential(auth)
                	// 项目id， 即地域
                .withRegion(CaeRegion.valueOf("cn-east-3"))
                .build();
        ListApplicationsRequest request = new ListApplicationsRequest();
        	// 设置环境id
        request.withXEnvironmentID("e9e0acfd-479f-4db5-8053-91f2f3e87d88");
        try {
            ListApplicationsResponse response = client.listApplications(request);
            List<ApplicationItem> items = response.getItems();
            for (ApplicationItem applicationItem : items) {
            	System.out.println("应用id：" + applicationItem.getId());
            	System.out.println("应用名称：" + applicationItem.getName());
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
