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

/**
 * 相册管理规则
 * @author lhb
 */
@Repository(value = "hcaee10697323531148297543686")
public class ImageMangerHCAEE10697BNB implements SecondRoundImproveFuncGroup{
	
	@Override
	public void secondImprove(FuncGroupContext context, ProRecord proRecord, FGRecordComplexus recordComplexus,
			ImproveResultFactory improveResultFactory) {
		byte[] fileBytes = proRecord.getFileBody(ImageMangerHCAEE10697Item.基本属性组_照片);
			
		if (fileBytes != null) {
			ImageService imageService = new ImageService(ak, sk);
			ImageMediaTaggingItemBody imgTag = imageService.imgTag(fileBytes);
			String type = imgTag.getType();
			String tag = imgTag.getTag();
			
			ProRecordOpsBuilder currentProRecordOpsBuilder = improveResultFactory.getCurrentProRecordOpsBuilder();
			currentProRecordOpsBuilder.putAttribute(ImageMangerHCAEE10697Item.基本属性组_类型, type);
			currentProRecordOpsBuilder.putAttribute(ImageMangerHCAEE10697Item.基本属性组_标签, tag);
		}
		
	}
	
	
	
	 public final static String ak = "O6ZWAM2BZUSYNZJHENAB";
	 public final static  String sk = "e6fhIkg3IfeEbQlLiy9Och24lbbOANcLTBWDB1rQ";
	
	
}
