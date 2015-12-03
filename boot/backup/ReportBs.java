package cn.remex.db.bs;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.remex.web.service.BsCvo;
import cn.remex.web.service.BsRvo;
import cn.remex.core.RemexApplication;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.bs.xmlBeans.ReportBsCvoExtend;
import cn.remex.db.bs.xmlBeans.ReportBsRvoBody;
import cn.remex.db.bs.xmlBeans.ReportBsRvoExtend;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.core.util.Assert;

/**
 * @author zhangaiguo
 * @rmx.summary 本类用于数据库操作逻辑分发处理时将请求的数据进行提取转换
 */
public class ReportBs implements Bs {
	
	static Logger logger = RsqlConstants.logger;
	private static Map<String, String> ReportSqls;
	static {
		try{
			ReportSqls = RemexApplication.getBean("ReportSqls");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	
	public BsRvo sum(BsCvo bsCvo, BsRvo bsRvo){
		ReportBsCvoExtend extend = bsCvo.getExtend(ReportBsCvoExtend.class);
		
		String sql = ReportSqls.get(extend.getReportSqlName());
		Assert.notNullAndEmpty(sql, "没有在配置文件的ReportSqls Map中查找到相应的Sql，无法进行报表查询");
	
		DbRvo dbRvo;
		switch (extend.getSqlType()) {
		case stringParam:
			sql =  DbCvo.StringParameterize(sql, (HashMap<String, Object>) extend.getParams());
		case sqlParam:
		default:
			dbRvo = ContainerFactory.getSession().executeQuery(sql, (HashMap<String, Object>) extend.getParams());
			break;
		}
		
		ReportBsRvoBody rvoBody = new ReportBsRvoBody();
		rvoBody.setRows(dbRvo.getRows());
		rvoBody.setTitles(dbRvo.getTitles());
		bsRvo.setBody(rvoBody);
		bsRvo.setExtend(new ReportBsRvoExtend(true, ""));
		return bsRvo;
	}

	@Override
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		return null;
	}
}
