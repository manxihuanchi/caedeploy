package com.kuangkie.huawei;

import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.cae.v1.region.CaeRegion;
import com.huaweicloud.sdk.cae.v1.*;
import com.huaweicloud.sdk.cae.v1.model.*;

import java.util.Map;
import java.util.HashMap;

/**
 * 	创建组件
 * @author lhb
 */
public class CreateComponentSolution {
	
	private String ak;
	private String sk;
	
	public CreateComponentSolution(String ak, String sk) {
		this.ak = ak;
		this.sk = sk;
	}
	
	/**
	 * 创建组件
	 * @param componentName  组件名称
	 * @param environmentID   环境id
	 * @param withApplicationId  应用id
	 * @param url  jar obs 路径
	 */
	public String createComponent(String componentName, String environmentID,
			String withApplicationId, String url) {
        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);

        CaeClient client = CaeClient.newBuilder()
                .withCredential(auth)
                .withRegion(CaeRegion.valueOf("cn-east-3"))
                .build();
        CreateComponentRequest request = new CreateComponentRequest();
        request.withApplicationId(withApplicationId);
        request.withXEnvironmentID(environmentID);
        CreateComponentRequestBody body = new CreateComponentRequestBody();
        ResourceLimit resourceLimitSpec = new ResourceLimit();
        resourceLimitSpec.withCpuLimit(ResourceLimit.CpuLimitEnum.fromValue("500m"))
            .withMemoryLimit(ResourceLimit.MemoryLimitEnum.fromValue("1Gi"));
        Source sourceSpec = new Source();
        sourceSpec.withType(Source.TypeEnum.fromValue("softwarePackage"))
            .withSubType(Source.SubTypeEnum.fromValue("BinObs"))
            .withUrl(url);
        Map<String, String> listBuildParameters = new HashMap<>();
        listBuildParameters.put("base_image", "swr.cn-east-3.myhuaweicloud.com/op_svc_cse/openjdk-{arch}:8-1.1.7");
        Archive archiveBuild = new Archive();
        archiveBuild.withArtifactNamespace("kuangkie_org");
        Build buildSpec = new Build();
        buildSpec.withArchive(archiveBuild)
            .withParameters(listBuildParameters);
        CreateComponentRequestBodySpec specbody = new CreateComponentRequestBodySpec();
        specbody.withRuntime(CreateComponentRequestBodySpec.RuntimeEnum.fromValue("Java8"))
            .withReplica(1)
            .withBuild(buildSpec)
            .withSource(sourceSpec)
            .withResourceLimit(resourceLimitSpec);
        Map<String, String> listMetadataAnnotations = new HashMap<>();
        listMetadataAnnotations.put("version", "1.0.0");
        CreateComponentRequestBodyMetadata metadatabody = new CreateComponentRequestBodyMetadata();
        metadatabody.withName(componentName)
            .withAnnotations(listMetadataAnnotations);
        body.withSpec(specbody);
        body.withMetadata(metadatabody);
		body.withKind(ComponentKindObj.COMPONENT);
		body.withApiVersion(ApiVersionObj.V1);
//        body.withKind(CreateComponentRequestBody.KindEnum.fromValue("Component"));
//        body.withApiVersion(CreateComponentRequestBody.ApiVersionEnum.fromValue("v1"));
        request.withBody(body);
        try {
            CreateComponentResponse response = client.createComponent(request);
            MetadataResponse metadata = response.getMetadata();
            if (metadata == null) {
            	return null;
            }
            String id = metadata.getId();
            return id;
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