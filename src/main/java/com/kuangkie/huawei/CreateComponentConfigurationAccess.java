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
public class CreateComponentConfigurationAccess {
	private String ak;
	private String sk;
	
	public CreateComponentConfigurationAccess(String ak, String sk) {
		this.ak = ak;
		this.sk = sk;
	}
	
	/**
	 * 配置访问方式
	 * @param port  访问端口
	 * @param targetPort  监听端口
	 * @return
	 */
    public boolean createAccess(int accessPort, int listenerPort, 
    		String environmentID, String applicationId, String componentId) {
    	
        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);
        
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
        port.setPort(accessPort);
        port.setTarget_port(listenerPort);
        port.setProtocol("TCP");
        ArrayList<Ports> ports = new ArrayList<Ports>();
        ports.add(port);
        item.setPorts(ports);
        
        items.add(item);
        spec.setItems(items);
        model.setSpec(spec);
        
        CreateComponentConfigurationRequest request = new CreateComponentConfigurationRequest();
        request.withApplicationId(applicationId);
        request.withComponentId(componentId);
        request.withXEnvironmentID(environmentID);
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
            	return true;
            } else {
            	System.out.println("失败");
            	return false;
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