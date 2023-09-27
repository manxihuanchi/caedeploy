package com.kuangkie.img.fg;

import org.springframework.stereotype.Repository;

import com.huaweicloud.sdk.image.v2.model.ImageMediaTaggingItemBody;
import com.kuangkie.carbon.fg.FuncGroupContext;
import com.kuangkie.carbon.fg.ImproveResultFactory;
import com.kuangkie.carbon.fg.SecondRoundImproveFuncGroup;
import com.kuangkie.carbon.fg.ops.ProRecordOpsBuilder;
import com.kuangkie.carbon.panel.CarbonPanel;
import com.kuangkie.carbon.record.FGAttribute;
import com.kuangkie.carbon.record.FGRecordComplexus;
import com.kuangkie.carbon.record.ProRecord;
import com.kuangkie.huawei.ImageService;
import com.kuangkie.img.CarbonBaseConstant;
import com.kuangkie.img.CarbonEnumKeyValue;
import com.kuangkie.img.CarbonRelationType;
import com.kuangkie.img.item.ImageMangerHCAEE10697Item;

@Repository(value = "hcaee10697323531148297543686")
public class ImageMangerHCAEE10697BNB implements SecondRoundImproveFuncGroup{
	
	//模型Code
	private CarbonBaseConstant carbonBaseConstant = new CarbonBaseConstant();
	//枚举字典信息
	private CarbonEnumKeyValue carbonEnumKeyValue = new CarbonEnumKeyValue();
	//关系类型Code
	private CarbonRelationType carbonRelationType = new CarbonRelationType();
	// 模型属性Code
	ImageMangerHCAEE10697Item item = new ImageMangerHCAEE10697Item();
	
	@Override
	public void secondImprove(FuncGroupContext context, ProRecord proRecord, FGRecordComplexus recordComplexus,
			ImproveResultFactory improveResultFactory) {
		
		byte[] fileBytes = proRecord.getFileBody(ImageMangerHCAEE10697Item.基本属性组_照片);
//		byte[] fileBytes = CarbonPanel.getBytesInfoVODiscover().discoverBytesInfo(CarbonBaseConstant.相册管理_xcgl, img);
			
		if (fileBytes != null) {
			 String ak = "O6ZWAM2BZUSYNZJHENAB";
			 String sk = "e6fhIkg3IfeEbQlLiy9Och24lbbOANcLTBWDB1rQ";
			 
			ImageService imageService = new ImageService(ak, sk);
			
			ImageMediaTaggingItemBody imgTag = imageService.imgTag(fileBytes);
			
			String type = imgTag.getType();
			String tag = imgTag.getTag();
			
			ProRecordOpsBuilder currentProRecordOpsBuilder = improveResultFactory.getCurrentProRecordOpsBuilder();
			currentProRecordOpsBuilder.putAttribute(ImageMangerHCAEE10697Item.基本属性组_类型, type);
			currentProRecordOpsBuilder.putAttribute(ImageMangerHCAEE10697Item.基本属性组_标签, tag);
		}
		
	}
	
}
