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
public class ObtainCityNameBs implements AiwcConsts {
	@BsAnnotation(bsCvoBodyClass=WxCityCode.class,bsRvoBodyClass=WxCitySelect.class)
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		WxCityCode  wxCityCode = bsCvo.getBody();
		final String code = wxCityCode.getCode();
		String[] array = {"130000","140000","150000","210000","220000","230000","320000","330000","340000","350000"
				,"360000","370000","410000","420000","430000","440000","450000","460000","510000","520000","530000","540000"
				,"610000","620000","630000","640000","650000"};
		List<String> tempList = Arrays.asList(array);
//		/////如果城市为省代码 则转为该省会城市代码
		final String cityCode;
		if(tempList.contains(code)){
			cityCode = code.substring(0,2)+"0100";
		}else{
			cityCode = code;
		}
		
		DbRvo resp = ContainerFactory.getSession().query(new DbCvo<CityCode>(CityCode.class){
		private static final long serialVersionUID = -2291709702597738740L;

		@Override
		public void initRules(CityCode v) {
			addRule(v.getCode(), WhereRuleOper.cn,cityCode);
			}
		});
		List<CityCode> cityCodes = resp.obtainBeans();
		/////直辖市处理
		ReflectUtil.copyProperties(wxCityCode, cityCodes.get(0));
		
		
		
//		List<WxCityCode>  wxCityCodes = new ArrayList<WxCityCode>();
//		WxCitySelect wxCitySelect = new WxCitySelect();
//		String[] array = {"110000","120000","310000","500000","820000","810000"};
//		List<String> tempList = Arrays.asList(array);
//		/////直辖市处理
//		if(tempList.contains(code)){
//			DbRvo mdResp = ContainerFactory.getSession().query(new DbCvo<CityCode>(CityCode.class){
//				private static final long serialVersionUID = -2291709702597738740L;
//				
//				@Override
//				public void initRules(CityCode v) {
//					addRule(v.getCityLevel(), WhereRuleOper.cn,"01");
//					addRule(v.getCode(), WhereRuleOper.cn,code);
//				}
//			});
//			List<CityCode> mdCityCodes = mdResp.obtainBeans();
//			WxCityCode mdCityCode = new WxCityCode();
//			ReflectUtil.copyProperties(mdCityCode, mdCityCodes.get(0));
//			wxCityCodes.add(mdCityCode);
//			wxCitySelect.setWxCityCodes(wxCityCodes);
//		}else{
//			for(int i=0;i<cityCodes.size();i++){
//				WxCityCode wxCityCode1 = new WxCityCode();
//				ReflectUtil.copyProperties(wxCityCode1, cityCodes.get(i));
//				wxCityCodes.add(wxCityCode1);
//			}
//		}
		
		
		bsRvo.setBody(wxCityCode);
		bsRvo.setExtend(new ResultBean(true, "操作成功"));
		
		return bsRvo;
	}
}
	
	