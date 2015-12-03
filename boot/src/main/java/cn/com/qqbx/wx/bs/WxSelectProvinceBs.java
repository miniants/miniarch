package cn.com.qqbx.wx.bs;

import java.util.ArrayList;
import java.util.List;

import anbox.aibc.model.system.CityCode;
import cn.com.qqbx.wx.AiwcConsts;
import cn.com.qqbx.wx.appbean.ResultBean;
import cn.com.qqbx.wx.appbeans.city.WxCityCode;
import cn.com.qqbx.wx.appbeans.city.WxCitySelect;
import cn.com.qqbx.wx.appbeans.vehicle.VehicleQueryExtend;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import cn.remex.reflect.ReflectUtil;
@BsAnnotation()
public class WxSelectProvinceBs implements AiwcConsts {
	@BsAnnotation(bsCvoBodyClass=WxCityCode.class,bsRvoBodyClass=WxCitySelect.class,
			bsCvoExtendClass=VehicleQueryExtend.class,bsRvoExtendClass=VehicleQueryExtend.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		DbRvo resp = ContainerFactory.getSession().query(new DbCvo<CityCode>(CityCode.class){
		private static final long serialVersionUID = -2291709702597738740L;

		@Override
		public void initRules(CityCode v) {
			addRule(v.getCityLevel(), WhereRuleOper.cn,"01");
		}
	});
		List<CityCode> cityCodes = resp.obtainBeans();
		List<WxCityCode>  wxCityCodes = new ArrayList<WxCityCode>();
		WxCitySelect wxCitySelect = new WxCitySelect();
		for(int i=0;i<cityCodes.size();i++){
			WxCityCode wxCityCode = new WxCityCode();
			ReflectUtil.copyProperties(wxCityCode, cityCodes.get(i));
			wxCityCodes.add(wxCityCode);
		}
		wxCitySelect.setWxCityCodes(wxCityCodes);
		bsRvo.setBody(wxCitySelect);
		bsRvo.setExtend(new ResultBean(true, "操作成功"));
		
		return bsRvo;
	}
}
	
	