package com.kuangkie.huawei;

import java.util.ArrayList;
import java.util.List;

import com.huaweicloud.sdk.cae.v1.CaeClient;
import com.huaweicloud.sdk.cae.v1.model.ApiVersionObj;
import com.huaweicloud.sdk.cae.v1.model.ComponentConfigurationKindObj;
import com.huaweicloud.sdk.cae.v1.model.ConfigurationItem;
import com.huaweicloud.sdk.cae.v1.model.CreateComponentConfigurationRequest;
import com.huaweicloud.sdk.cae.v1.model.CreateComponentConfigurationRequestBody;
import com.huaweicloud.sdk.cae.v1.model.CreateComponentConfigurationResponse;
import com.huaweicloud.sdk.cae.v1.region.CaeRegion;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.kuangkie.huawei.pojo.AccessControl;
import com.kuangkie.huawei.pojo.Items;
import com.kuangkie.huawei.pojo.Model;
import com.kuangkie.huawei.pojo.Ports;
import com.kuangkie.huawei.pojo.Spec;
/**
 * 创建访问方式： 外网
 * @author lhb
 * 生效配置， 在操作组件里面： configure
 */
public class CreateComponentConfigurationSolution2Model {
	
    public static void main(String[] args) {
//    	String str = "{\"accessControl\":{},\"spec\":{\"items\":[{\"type\":\"LoadBalancer\",\"ports\":[{\"target_port\":8080,\"protocol\":\"TCP\",\"port\":8080}]}]}}";
//    	System.out.println(str);
//    	
//    	String str2 = "{\"spec\":{\"items\":[{\"type\":\"LoadBalancer\",\"ports\":[{\"target_port\":8080,\"protocol\":\"TCP\",\"port\":8080}]}]}}";
//    	System.out.println(str2);
    	
        ICredential auth = new BasicCredentials()
                .withAk(Ak.ak)
                .withSk(Ak.sk);

        CaeClient client = CaeClient.newBuilder()
                .withCredential(auth)
                .withRegion(CaeRegion.valueOf("cn-east-3"))
                .build();
        
        Model model = new Model();
        
        model.setAccessControl(new AccessControl());
        
        Spec spec = new Spec();
        List<Items> items = new ArrayList<Items>();
        
        Items item = new Items();
        item.setType("LoadBalancer");
        Ports port= new Ports();
        port.setPort(90);
        port.setTarget_port(95);
        port.setProtocol("TCP");
        ArrayList<Ports> ports = new ArrayList<Ports>();
        ports.add(port);
        item.setPorts(ports);
        
        items.add(item);
        spec.setItems(items);
        model.setSpec(spec);
        
        CreateComponentConfigurationRequest request = new CreateComponentConfigurationRequest();
        request.withApplicationId("6159e50e-2cb7-4eb1-a2b5-4111ddd17d27");
        request.withComponentId("230f4514-5ec8-40cb-bf7d-1c822836e588");
        request.withXEnvironmentID("e9e0acfd-479f-4db5-8053-91f2f3e87d88");
        CreateComponentConfigurationRequestBody body = new CreateComponentConfigurationRequestBody();
        List<ConfigurationItem> listbodyItems = new ArrayList<>();
		listbodyItems.add(
            new ConfigurationItem()
                .withType(ConfigurationItem.TypeEnum.fromValue("access"))
                .withData(model)
        		);
		
        body.withItems(listbodyItems);
		body.withKind(ComponentConfigurationKindObj.COMPONENTCONFIGURATION);
		body.withApiVersion(ApiVersionObj.V1);
        request.withBody(body);
        try {
            CreateComponentConfigurationResponse response = client.createComponentConfiguration(request);
          
            int httpStatusCode = response.getHttpStatusCode();
            if (httpStatusCode == 200) {
            	System.out.println("成功");
            } else {
            	System.out.println("失败");
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
    
    
    
//    {
//  	  "metadata": {
//  	    "annotations": {
//  	      "deployBackend": "k8s"
//  	    },
//  	    "created_at": "0001-01-01T00:00:00Z",
//  	    "updated_at": "0001-01-01T00:00:00Z"
//  	  },
//  	  "spec": {
//  	    "ip": "124.70.130.169",
//  	    "items": [
//  	      {
//  	        "type": "LoadBalancer",
//  	        "access_control": null,
//  	        "ports": [
//  	          {
//  	            "ip": "",
//  	            "name": "",
//  	            "target_port": 8080,
//  	            "port": 8080,
//  	            "protocol": "TCP",
//  	            "default_certificate": ""
//  	          }
//  	        ]
//  	      }
//  	    ]
//  	  },
//  	  "ref": {
//  	    "id": "4a933b06-a70a-445a-b619-fc2701d08b38"
//  	  }
//  	}
}