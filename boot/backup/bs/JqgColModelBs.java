package cn.remex.db.bs;

import java.util.List;

import cn.remex.web.service.BsCvo;
import cn.remex.web.service.BsRvo;
import cn.remex.db.model.view.JqgColModel;
import cn.remex.db.view.JqgUtility;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Judgment;

public class JqgColModelBs implements Bs {

	@Override
	public BsRvo execute(BsCvo cvo, BsRvo rvo){
		// TODO Auto-generated method stub
		JqgColModelBsCvoExtend cvoExtend=cvo.getExtend(JqgColModelBsCvoExtend.class);
		String viewName = Judgment.nullOrBlank(cvoExtend.getViewName())?cvoExtend.getBeanName():cvoExtend.getViewName();
		Assert.notNull(viewName, "视图名不能为空！");
		List<JqgColModel> objects = JqgUtility.obtainColumnModels(viewName );
		DataResult dataResult = new DataResult(true,"ok");
		DataBsRvoBody  dataBsRvoBody = new DataBsRvoBody();
		dataBsRvoBody.setBeans(objects);
		rvo.setBody(dataBsRvoBody );
		rvo.setExtend(dataResult);
		return rvo;
	}

}
