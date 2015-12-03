package cn.com.qqbx.wx.bs;

import java.util.ArrayList;
import java.util.List;

import anbox.aibc.model.system.CityCode;
import cn.com.qqbx.wx.AiwcConsts;
import cn.com.qqbx.wx.appbean.ResultBean;
import cn.com.qqbx.wx.appbeans.city.WxCityCode;
import cn.com.qqbx.wx.appbeans.city.WxCitySelect;
import cn.remex.bs.BsCvo;
import cn.remex.bs.BsRvo;
import cn.remex.bs.anno.BsAnnotation;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants.WhereRuleOper;
import cn.remex.reflect.ReflectUtil;
import edu.emory.mathcs.backport.java.util.Arrays;
@BsAnnotation()
public class WxSelectCityBs implements AiwcConsts {
	@BsAnnotation(bsCvoBodyClass=WxCityCode.class,bsRvoBodyClass=WxCitySelect.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		WxCityCode  wxCityCode = bsCvo.getBody();
		final String code = wxCityCode.getCode();
		DbRvo resp = ContainerFactory.getSession().query(new DbCvo<CityCode>(CityCode.class){
		private static final long serialVersionUID = -2291709702597738740L;

		@Override
		public void initRules(CityCode v) {
			addRule(v.getCityLevel(), WhereRuleOper.cn,"02");
			addRule(v.getSupCityCode(), WhereRuleOper.cn,code);
			}
		});
		List<CityCode> cityCodes = resp.obtainBeans();
		/////直辖市处理
		
		
		
		
		List<WxCityCode>  wxCityCodes = new ArrayList<WxCityCode>();
		WxCitySelect wxCitySelect = new WxCitySelect();
		String[] array = {"110000","120000","310000","500000","820000","810000"};
		List<String> tempList = Arrays.asList(array);
		/////直辖市处理
		if(tempList.contains(code)){
			DbRvo mdResp = ContainerFactory.getSession().query(new DbCvo<CityCode>(CityCode.class){
				private static final long serialVersionUID = -2291709702597738740L;
				
				@Override
				public void initRules(CityCode v) {
					addRule(v.getCityLevel(), WhereRuleOper.cn,"01");
					addRule(v.getCode(), WhereRuleOper.cn,code);
				}
			});
			List<CityCode> mdCityCodes = mdResp.obtainBeans();
			WxCityCode mdCityCode = new WxCityCode();
			ReflectUtil.copyProperties(mdCityCode, mdCityCodes.get(0));
			wxCityCodes.add(mdCityCode);
			wxCitySelect.setWxCityCodes(wxCityCodes);
		}else{
			for(int i=0;i<cityCodes.size();i++){
				WxCityCode wxCityCode1 = new WxCityCode();
				ReflectUtil.copyProperties(wxCityCode1, cityCodes.get(i));
				wxCityCodes.add(wxCityCode1);
			}
		}
		
		
		wxCitySelect.setWxCityCodes(wxCityCodes);
		bsRvo.setBody(wxCitySelect);
		bsRvo.setExtend(new ResultBean(true, "操作成功"));
		
		return bsRvo;
	}
}
	
	