package com.kuangkie.huawei;

import java.util.List;

import com.huaweicloud.sdk.cae.v1.CaeClient;
import com.huaweicloud.sdk.cae.v1.model.EnvironmentItem;
import com.huaweicloud.sdk.cae.v1.model.ListEnvironmentsRequest;
import com.huaweicloud.sdk.cae.v1.model.ListEnvironmentsResponse;
import com.huaweicloud.sdk.cae.v1.region.CaeRegion;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;

/**
 * 	获取环境列表
 * @author lhb
 *
 *	直接在应用下创建组件
 *
 */
public class ListEnvironmentsSolution {

	// 项目code： f9e12bb6c6a1464ca7158f164863804d
	
    public static void main(String[] args) {
        String ak = "O6ZWAM2BZUSYNZJHENAB";
        String sk = "e6fhIkg3IfeEbQlLiy9Och24lbbOANcLTBWDB1rQ";

        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);

        CaeClient client = CaeClient.newBuilder()
                .withCredential(auth)
                .withRegion(CaeRegion.valueOf("cn-east-3"))
                .build();
        ListEnvironmentsRequest request = new ListEnvironmentsRequest();
        try {
            ListEnvironmentsResponse response = client.listEnvironments(request);
            
            List<EnvironmentItem> items = response.getItems();
            for (EnvironmentItem environmentItem : items) {
            	String name = environmentItem.getName();
            	System.out.println("环境id：" + environmentItem.getId());
            	System.out.println("环境名称：" + name);
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