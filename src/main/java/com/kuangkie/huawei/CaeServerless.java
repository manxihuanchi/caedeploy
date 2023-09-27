package com.kuangkie.huawei;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kuangkie.carbon.common.IntegrationMsg;
import com.kuangkie.carbon.fg.ImproveResultFactory;
import com.kuangkie.carbon.fg.ops.ProRecordOpsBuilder;
import com.kuangkie.carbon.panel.CarbonPanel;
import com.kuangkie.carbon.panel.RecordIntegration;
import com.kuangkie.carbon.record.FGRecord;
import com.kuangkie.carbon.record.FGRecordBuilder;
import com.kuangkie.carbon.record.ProRecord;
import com.kuangkie.dev.fg.BaseConstant;
import com.kuangkie.dev.fg.EnumKeyValue;
import com.kuangkie.dev.fg.item.DeployConfigHGVBE6828Item;
import com.kuangkie.dev.fg.item.TmplMetadataHGVBE6805Item;
import com.kuangkie.hydrogenv2.algorithm.Serverless;
import com.obs.services.ObsClient;

/**
 * 	华为CAE
 * @author lhb
 */
@Component
public class CaeServerless implements Serverless{
	 Logger logger = LoggerFactory.getLogger(CaeServerless.class);
	 
	@Override
	public void publish(ImproveResultFactory improveResultFactory,	
			String jarName, String jarPath, FGRecord deployConfig) {
		ProRecordOpsBuilder currentProRecordOpsBuilder = improveResultFactory.getCurrentProRecordOpsBuilder();
		FileInputStream fis = null;
		try {
				// 上传文件到cae
			 String endPoint = "https://obs.cn-east-3.myhuaweicloud.com";
			 String ak = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_ak);
			 if (StringUtils.isBlank(ak)) {
				 improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请填写ak！");
				 return;
			 }
			 ak = ak.trim();
			 String sk = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_sk);
			 if (StringUtils.isBlank(sk)) {
				 improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请填写sk！");
				 return;
			 }
			 sk = sk.trim();
			// 创建ObsClient实例
			ObsClient obsClient = new ObsClient(ak, sk, endPoint);
			// 待上传的本地文件路径，需要指定到具体的文件名
			fis = new FileInputStream(new File(jarPath));  
			com.obs.services.model.PutObjectResult putObject = obsClient.putObject("kuangkie", jarName, fis);
			String url = putObject.getObjectUrl();
			if (url == null) {
				improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "上传失败！");
				return;
			} else {
				// 上传成功， 组装oss 文件 url
				currentProRecordOpsBuilder.putAttribute(TmplMetadataHGVBE6805Item.基本属性组_状态, EnumKeyValue.版本状态_已发布_yfb);
				currentProRecordOpsBuilder.putAttribute(TmplMetadataHGVBE6805Item.基本属性组_ossurl, url);
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "文件发布到obs 失败！");
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	@Override
	public void onlineDeploy(ProRecord proRecord, FGRecord deployConfig, ImproveResultFactory improveResultFactory) {
		
		ProRecordOpsBuilder currentProRecordOpsBuilder = improveResultFactory.getCurrentProRecordOpsBuilder();
		
		String recordCode = proRecord.getRecordCode();
		String version = proRecord.getString(TmplMetadataHGVBE6805Item.基本属性组_版本);
		String port = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_上线端口);
		
		// jar 包地址
		String url =  proRecord.getString(TmplMetadataHGVBE6805Item.基本属性组_ossurl);
		
		if (port == null || StringUtils.isBlank(port)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写上线端口！");
			return;
		}
		String shangxianVersionOnlyCode = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_上线版本编码);
		if (shangxianVersionOnlyCode == null || StringUtils.isBlank(shangxianVersionOnlyCode)) {
			
		} else {
			// 如果存在正在运行的应用， 需要先删除应用
			stopApp(deployConfig, improveResultFactory, shangxianVersionOnlyCode, true);
		}
		
		String ak = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_ak);
		if (ak == null || StringUtils.isBlank(ak)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写ak！");
			return;
		}

		String sk = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_sk);
		if (sk == null || StringUtils.isBlank(sk)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写sk！");
			return;
		}
		
		String environmentID = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_环境id);
		if (environmentID == null || StringUtils.isBlank(environmentID)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写环境id！");
			return;
		}
		
		String applicationId = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_应用id);
		if (applicationId == null || StringUtils.isBlank(applicationId)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写应用id！");
			return;
		}
		
		CreateComponentSolution createComponentSolution = new CreateComponentSolution(ak, sk);
		String componentName = "k" +recordCode;
		
		String componentId = createComponentSolution.createComponent(componentName, environmentID, applicationId, url);
		if (componentId == null || StringUtils.isBlank(componentId)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "发布组件失败！");
			return;
		}
		
		// 绑定公网
		CreateComponentConfigurationAccess createComponentAccess = new CreateComponentConfigurationAccess(ak, sk);
		boolean createAccess = createComponentAccess.createAccess(Integer.valueOf(port), 8080, environmentID, applicationId, componentId);
		if (!createAccess) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "绑定公网ip失败！");
			return;
		}
		
		// 部署应用
		ExecuteActionSolution executeActionSolution = new ExecuteActionSolution(ak, sk);
		boolean deploy = executeActionSolution.deploy(environmentID, applicationId, componentId);
		if(!deploy) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "部署应用失败！");
			return;
		}

		currentProRecordOpsBuilder.putAttribute(TmplMetadataHGVBE6805Item.基本属性组_状态,EnumKeyValue.版本状态_运行中_yxz);

		// 更新状态
		FGRecordBuilder deployConfigBuilder = CarbonPanel.getFGRecordBuilder(BaseConstant.TYPE_部署配置, deployConfig.getRecordCode());
		
		deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.cae配置_上线组件id, componentId);
		deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线版本编码, recordCode);
		deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线访问地址, "http://124.70.130.169:" + port);
		
		 RecordIntegration integration = CarbonPanel.getRecordIntegration(BaseConstant.TYPE_部署配置);
		 IntegrationMsg msg = integration.integrate(deployConfigBuilder.getRecord()); 
		 boolean success = msg.success();
		 if (success) {
			 
		 } else {
			 
		 }
	}

	@Override
	public void testDeploy(ProRecord proRecord, ImproveResultFactory improveResultFactory, String recordCode,
				String version, FGRecord deployConfig) {
		ProRecordOpsBuilder currentProRecordOpsBuilder = improveResultFactory.getCurrentProRecordOpsBuilder();
		
		String port = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_测试端口);
		
		// jar 包地址
		String url =  proRecord.getString(TmplMetadataHGVBE6805Item.基本属性组_ossurl);
		
		if (port == null || StringUtils.isBlank(port)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写测试端口！");
			return;
		}
		String ceshiVersionOnlyCode = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_测试版本编码);
		if (ceshiVersionOnlyCode == null || StringUtils.isBlank(ceshiVersionOnlyCode)) {
			
		} else {
			// 如果存在正在运行的应用， 需要先删除应用
			stopApp(deployConfig, improveResultFactory, ceshiVersionOnlyCode, true);
		}
		
		String ak = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_ak);
		if (ak == null || StringUtils.isBlank(ak)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写ak！");
			return;
		}

		String sk = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_sk);
		if (sk == null || StringUtils.isBlank(sk)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写sk！");
			return;
		}
		
		String environmentID = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_环境id);
		if (environmentID == null || StringUtils.isBlank(environmentID)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写环境id！");
			return;
		}
		
		String applicationId = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_应用id);
		if (applicationId == null || StringUtils.isBlank(applicationId)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写应用id！");
			return;
		}
		
		CreateComponentSolution createComponentSolution = new CreateComponentSolution(ak, sk);
		String componentName = "k" +recordCode;
		String componentId = createComponentSolution.createComponent(componentName, environmentID, applicationId, url);
		if (componentId == null || StringUtils.isBlank(componentId)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "发布组件失败！");
			return;
		}
		
		// 绑定公网
		CreateComponentConfigurationAccess createComponentAccess = new CreateComponentConfigurationAccess(ak, sk);
		boolean createAccess = createComponentAccess.createAccess(Integer.valueOf(port), 8080, environmentID, applicationId, componentId);
		if (!createAccess) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "绑定公网ip失败！");
			return;
		}
		
		// 部署应用
		ExecuteActionSolution executeActionSolution = new ExecuteActionSolution(ak, sk);
		boolean deploy = executeActionSolution.deploy(environmentID, applicationId, componentId);
		if(!deploy) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "部署应用失败！");
			return;
		}

		currentProRecordOpsBuilder.putAttribute(TmplMetadataHGVBE6805Item.基本属性组_状态,EnumKeyValue.版本状态_运行中_yxz);

		// 更新状态
		FGRecordBuilder deployConfigBuilder = CarbonPanel.getFGRecordBuilder(BaseConstant.TYPE_部署配置, deployConfig.getRecordCode());
		
		deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.cae配置_测试组件id, componentId);
		deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试版本编码, recordCode);
		deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试访问地址, "http://124.70.130.169:" + port);
		
		 RecordIntegration integration = CarbonPanel.getRecordIntegration(BaseConstant.TYPE_部署配置);
		 IntegrationMsg msg = integration.integrate(deployConfigBuilder.getRecord()); 
		 boolean success = msg.success();
		 if (success) {
			 
		 } else {
			 
		 }
	}

	/**
	 * 	删除或停止正在运行的应用
	 * @param improveResultFactory
	 * @param updateStatus 是否更新版本的状态
	 * @param version
	 */
	@Override
	public boolean stopApp(FGRecord deployConfig, ImproveResultFactory improveResultFactory, String versionOnlyCode, boolean updateStatus) {
		String ak = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_ak);
		if (ak == null || StringUtils.isBlank(ak)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写ak！");
			return false;
		}

		String sk = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_sk);
		if (sk == null || StringUtils.isBlank(sk)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写sk！");
			return false;
		}
		
		String environmentID = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_环境id);
		if (environmentID == null || StringUtils.isBlank(environmentID)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写环境id！");
			return false;
		}
		
		String applicationId = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_应用id);
		if (applicationId == null || StringUtils.isBlank(applicationId)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "请在【部署配置】填写应用id！");
			return false;
		}
		
		String shangxianComponentId = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_上线组件id);
		if (shangxianComponentId == null || StringUtils.isBlank(shangxianComponentId)) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "没有找到上线组件id！");
			return false;
		}
		
		//上线版本编码
		String shangxianVersionOnlyCode = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_上线版本编码);
		//测试版本编码
		String testVersionOnlyCode = deployConfig.getString(DeployConfigHGVBE6828Item.基本属性组_测试版本编码);
		String componentId = null;
		FGRecordBuilder deployConfigBuilder = CarbonPanel.getFGRecordBuilder(BaseConstant.TYPE_部署配置, deployConfig.getRecordCode());
		
		if (versionOnlyCode.equals(shangxianVersionOnlyCode)) {
			componentId = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_上线组件id);
			// 清空配置信息
			deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线版本号, null);
			deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线版本编码, null);
			deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线appid, null);
			deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_上线访问地址, null);
		} else if (versionOnlyCode.equals(testVersionOnlyCode)) {
			componentId = deployConfig.getString(DeployConfigHGVBE6828Item.cae配置_测试组件id);
			// 清空配置信息
			deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试版本号, null);
			deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试版本编码, null);
			deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试appid, null);
			deployConfigBuilder.putAttribute(DeployConfigHGVBE6828Item.基本属性组_测试访问地址, null);
		}
		
		DeleteComponentSolution deleteComponentSolution = new DeleteComponentSolution(ak, sk);
		boolean deleteComponent = deleteComponentSolution.deleteComponent(environmentID, applicationId, componentId);
		if(!deleteComponent) {
			improveResultFactory.addRefuseMessage(BaseConstant.TYPE_模板元数据, "删除组件失败！");
			return false;
		}
			
		RecordIntegration integration = CarbonPanel.getRecordIntegration(BaseConstant.TYPE_部署配置);
		 IntegrationMsg msg = integration.integrate(deployConfigBuilder.getRecord()); 
		 boolean success = msg.success();
		 if (success) {
			 
		 } else {
			 
		 }
		 if (updateStatus) {
			 FGRecordBuilder metaBuilder = CarbonPanel.getFGRecordBuilder(BaseConstant.TYPE_模板元数据, versionOnlyCode);
			metaBuilder.putAttribute(TmplMetadataHGVBE6805Item.基本属性组_状态, EnumKeyValue.版本状态_已发布_yfb);
			RecordIntegration metaIntegration = CarbonPanel.getRecordIntegration(BaseConstant.TYPE_模板元数据);
			 IntegrationMsg metaMsg = metaIntegration.integrate(metaBuilder.getRecord()); 
			 boolean metaSuccess = metaMsg.success();
			 if (metaSuccess) {
			 } else {
				 
			 }
		 }
		return true;
	}
	
	

}