package com.kuangkie.huawei;

import java.util.List;

import com.huaweicloud.sdk.cae.v1.CaeClient;
import com.huaweicloud.sdk.cae.v1.model.ComponentItem;
import com.huaweicloud.sdk.cae.v1.model.ListComponentsRequest;
import com.huaweicloud.sdk.cae.v1.model.ListComponentsResponse;
import com.huaweicloud.sdk.cae.v1.region.CaeRegion;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;

/**
 * 获取组件列表
 * @author lhb
 *
 */
public class ListComponentsSolution {

    public static void main(String[] args) {

        ICredential auth = new BasicCredentials()
                .withAk(Ak.ak)
                .withSk(Ak.sk);

        CaeClient client = CaeClient.newBuilder()
                .withCredential(auth)
                .withRegion(CaeRegion.valueOf("cn-east-3"))
                .build();
        ListComponentsRequest request = new ListComponentsRequest();
        request.withApplicationId("6159e50e-2cb7-4eb1-a2b5-4111ddd17d27");
        request.withXEnvironmentID("e9e0acfd-479f-4db5-8053-91f2f3e87d88");
        try {
            ListComponentsResponse response = client.listComponents(request);
            List<ComponentItem> items = response.getItems();
           for (ComponentItem item : items) {
			
        	   System.out.println("组件id:" + item.getId());
        	   System.out.println("组件name:" + item.getName());
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