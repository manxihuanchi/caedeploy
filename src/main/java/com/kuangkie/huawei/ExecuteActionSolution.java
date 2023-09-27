package com.kuangkie.huawei;
import com.huaweicloud.sdk.cae.v1.CaeClient;
import com.huaweicloud.sdk.cae.v1.model.ActionKindObj;
import com.huaweicloud.sdk.cae.v1.model.ApiVersionObj;
import com.huaweicloud.sdk.cae.v1.model.ExecuteActionRequest;
import com.huaweicloud.sdk.cae.v1.model.ExecuteActionRequestBody;
import com.huaweicloud.sdk.cae.v1.model.ExecuteActionRequestBodyMetadata;
import com.huaweicloud.sdk.cae.v1.model.ExecuteActionResponse;
import com.huaweicloud.sdk.cae.v1.region.CaeRegion;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
//操作组件
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;

/**
 * 	部署配置
 * @author lhb
 *
 */
public class ExecuteActionSolution {
	
	private String ak = null;
	private String sk = null;
	
	private  CaeClient client = null;
	
	public ExecuteActionSolution( String ak, String sk) {
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
	
	/**
	 * 	部署组件
	 * @param environmentID
	 * @param applicationId
	 * @param componentId
	 * @return
	 */
    public boolean deploy(String environmentID, String applicationId, String componentId) {
       
        ExecuteActionRequest request = new ExecuteActionRequest();
        request.withApplicationId(applicationId);
        request.withComponentId(componentId);
        request.withXEnvironmentID(environmentID);
        ExecuteActionRequestBody body = new ExecuteActionRequestBody();
        ExecuteActionRequestBodyMetadata metadatabody = new ExecuteActionRequestBodyMetadata();
        // 部署组件
        metadatabody.withName(ExecuteActionRequestBodyMetadata.NameEnum.fromValue("deploy"));
        body.withMetadata(metadatabody);
		body.withKind(ActionKindObj.ACTION);
		body.withApiVersion(ApiVersionObj.V1);
        request.withBody(body);
        try {
            ExecuteActionResponse response = client.executeAction(request);
            int httpStatusCode = response.getHttpStatusCode();
            if (httpStatusCode == 200) {
            	return true;
            }
            
            System.out.println(response.toString());
            
            return false;
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
    
    /**
     * 	启动组件
     * @param environmentID
     * @param applicationId
     * @param componentId
     * @return
     */
    public boolean start(String environmentID, String applicationId, String componentId) {
        
        ExecuteActionRequest request = new ExecuteActionRequest();
        request.withApplicationId(applicationId);
        request.withComponentId(componentId);
        request.withXEnvironmentID(environmentID);
        ExecuteActionRequestBody body = new ExecuteActionRequestBody();
        ExecuteActionRequestBodyMetadata metadatabody = new ExecuteActionRequestBodyMetadata();
        // 部署组件
        metadatabody.withName(ExecuteActionRequestBodyMetadata.NameEnum.fromValue("start"));
        body.withMetadata(metadatabody);
		body.withKind(ActionKindObj.ACTION);
		body.withApiVersion(ApiVersionObj.V1);
        request.withBody(body);
        try {
            ExecuteActionResponse response = client.executeAction(request);
            int httpStatusCode = response.getHttpStatusCode();
            if (httpStatusCode == 200) {
            	return true;
            }
            
            System.out.println(response.toString());
            
            return false;
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
    
    /**
     * 	停止组件
     * @param environmentID
     * @param applicationId
     * @param componentId
     * @return
     */
    public boolean stop(String environmentID, String applicationId, String componentId) {
        
        ExecuteActionRequest request = new ExecuteActionRequest();
        request.withApplicationId(applicationId);
        request.withComponentId(componentId);
        request.withXEnvironmentID(environmentID);
        ExecuteActionRequestBody body = new ExecuteActionRequestBody();
        ExecuteActionRequestBodyMetadata metadatabody = new ExecuteActionRequestBodyMetadata();
        // 部署组件
        metadatabody.withName(ExecuteActionRequestBodyMetadata.NameEnum.fromValue("stop"));
        body.withMetadata(metadatabody);
		body.withKind(ActionKindObj.ACTION);
		body.withApiVersion(ApiVersionObj.V1);
        request.withBody(body);
        try {
            ExecuteActionResponse response = client.executeAction(request);
            int httpStatusCode = response.getHttpStatusCode();
            if (httpStatusCode == 200) {
            	return true;
            }
            System.out.println(response.toString());
            return false;
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
