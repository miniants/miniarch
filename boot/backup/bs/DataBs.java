package cn.remex.db.bs;

import org.apache.log4j.Logger;

import cn.remex.web.service.BsCvo;
import cn.remex.web.service.BsRvo;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.RsqlConstants.SqlOper;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.rsql.transactional.RsqlTransaction;
import cn.remex.db.sql.SqlBeanWhere;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.reflect.ReflectUtil.SPFeature;
import cn.remex.core.util.Assert;
import cn.remex.core.util.Judgment;

/**
 * @author zhangaiguo
 * @rmx.summary 本类用于数据库操作逻辑分发处理时将请求的数据进行提取转换
 */
public class DataBs implements Bs {
	
	static Logger logger = RsqlConstants.logger;

	/**
	 * @rmx.summary 根据传入的条件查询记录，根据查询结果进行删除
	 * @param bsCvo
	 * @param bsRvo
	 * @return BsRvo
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RsqlTransaction
	public BsRvo deleteByFilters(BsCvo bsCvo,BsRvo bsRvo){           //删除符合条件的记录（级联同上）？？？？？
		DataCmd dataCmd = bsCvo.getExtend(DataCmd.class);
		DataResult  dataResult =  new DataResult();
		String beanName = dataCmd.getBeanName();		//获取传入的表名
		Assert.notNull(beanName, "未提供查询的beanName");
		logger.info(new StringBuilder("---------------------------进入根据条件删除数据的服务：表名为【").append(beanName).append("】------------------------\r\n").toString());
		SqlBeanWhere filter = dataCmd.getFilters();
		if(filter != null){
			Class<Modelable> beanClass = (Class<Modelable>) ContainerFactory.getSession().obtainModelClass(beanName);		//根据beanName获取该表对应的类
			Assert.notNull(beanClass,"根据提供的表没有查找到对应的类，请确认beanName是否正确或是否正确配置数据库表类！");
			DbCvo cvo = new DbCvo<Modelable>(beanClass, SqlOper.list);
			cvo.setSqlBeanWhere(filter);			//设置过滤条件
			DbRvo dbRvo = ContainerFactory.getSession().query(cvo);
			String[] idStr = new String[dbRvo.getRecordCount()];		//获取查询结果的id，以逗号分隔
			int i = 0;
			for(Modelable m : dbRvo.obtainObjects(beanClass)){
				idStr[i] = m.getId();
				i++;
			}
			dataResult = delModels(beanClass, idStr);	//调用删除的方法循环并获取响应扩展信息
		}else{
			dataResult.setMsg("没有过滤条件，不能删除数据！");
		}
		bsRvo.setExtend(dataResult);
		logger.info(new StringBuilder(dataResult.getMsg()).append("\r\n影响行数【").append(dataResult.getEffectRowCounts()).append("】，删除结果为【")
				.append(dataResult.getErrorCode()).append("】\r\n---------------------------根据条件删除表【").append(beanName).append("】的数据结束!----------------------------"));
		return bsRvo;
	}

	/**
	 * @rmx.summary 根据id记录
	 * <li>根据id删除记录（级联删除根据model属性的cascade的决定）</li>
	 * @param bsCvo
	 * @param bsRvo
	 * @return BsRvo
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RsqlTransaction
	public BsRvo deleteById(BsCvo bsCvo,BsRvo bsRvo){
		DataCmd dataCmd = bsCvo.getExtend(DataCmd.class);
		String id = dataCmd.getId();
		String beanName = dataCmd.getBeanName();		//获取传入的表名
		Assert.notNull(beanName, "未提供查询的beanName");
		logger.info(new StringBuilder("---------------------------进入根据id删除数据的服务：表名为【").append(beanName).append("】，id为【").append(id).append("】------------------------\r\n").toString());
		DataResult dataResult = new DataResult();
		if (null != id){ 
			Class<Modelable> beanClass = (Class<Modelable>) ContainerFactory.getSession().obtainModelClass(beanName);		//根据beanName获取该表对应的类
			Assert.notNull(beanClass,"根据提供的表没有查找到对应的类，请确认beanName是否正确或是否正确配置数据库表类！");
			DbRvo dbRvo = ContainerFactory.getSession().deleteById(beanClass, id);
			int effectRowCount = dbRvo.getEffectRowCount();
			dataResult.setEffectRowCounts(effectRowCount); 	//返回影响行数
			dataResult.setErrorCode(DataBsConst.BsStatus.Success.toString());	//设置响应状态
			dataResult.setMsg(1 == effectRowCount ? "成功删除表"+beanName+"中id为"+id+"的数据记录" :"在表‘"+beanName+"’中未找到id为’"+id+"‘的记录!"); // TODO 多行返回的信息提示会有误，建议性缺陷
		}else {
			dataResult.setMsg("id为空，请提供id进行删除!");
		}
		bsRvo.setExtend(dataResult);
		logger.info(new StringBuilder(dataResult.getMsg()).append("\r\n影响行数【").append(dataResult.getEffectRowCounts()).append("】，删除结果为【")
				.append(dataResult.getErrorCode()).append("】\r\n---------------------------根据条件删除表【").append(beanName).append("】中id为【").append(id).append("】的数据结束!----------------------------"));
		return bsRvo;
	}
	

	/**
	 * @rmx.summary 根据ids删除记录
	 * <li>根据ids删除记录（级联删除根据model属性的cascade的决定）</li>
	 * @param bsCvo
	 * @param bsRvo
	 * @return BsRvo
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RsqlTransaction
	public BsRvo deleteByIds(BsCvo bsCvo,BsRvo bsRvo){
		DataCmd dataCmd = bsCvo.getExtend(DataCmd.class);
		String ids = null == dataCmd.getIds() ? dataCmd.getId() : dataCmd.getIds();
		String beanName = dataCmd.getBeanName();		//获取传入的表名
		Assert.notNull(beanName, "未提供查询的beanName");
		logger.info(new StringBuilder("---------------------------进入根据ids删除数据的服务：表名为【").append(beanName).append("】，ids为【").append(ids).append("】------------------------\r\n").toString());
		DataResult dataResult = new DataResult();
		if(ids != null){
			Class<Modelable> beanClass = (Class<Modelable>) ContainerFactory.getSession().obtainModelClass(beanName);		//根据beanName获取该表对应的类
			Assert.notNull(beanClass,"根据提供的表没有查找到对应的类，请确认beanName是否正确或是否正确配置数据库表类！");
			String[] idArr = ids.split(",");
			dataResult = delModels(beanClass, idArr);	//调用删除的方法循环删除数据并获取响应扩展信息
		}else{
			dataResult.setMsg("id为空，请提供id进行删除!");      
		}
		bsRvo.setExtend(dataResult);
		logger.info(new StringBuilder(dataResult.getMsg()).append("\r\n影响行数【").append(dataResult.getEffectRowCounts()).append("】，删除结果为【")
				.append(dataResult.getErrorCode()).append("】\r\n---------------------------根据条件删除表【").append(beanName).append("】中ids为【").append(ids).append("】的数据结束!----------------------------"));
		return bsRvo;
	}
	
	/**
	 * @rmx.summary 用于方法deleteByFilters 和deleteByFilters的遍历删除
	 * @see  deleteByFilters和deleteByFilters
	 * @param beanClass 类
	 * @param idArr	id数组
	 * @return DataResult
	 */
	@RsqlTransaction
	private DataResult delModels(Class<Modelable> beanClass, String[] idArr){
		DataResult dataResult = new DataResult();
		if(0 == idArr.length){
			dataResult.setMsg("传入的id序列为空，请确定传入的id序列！");
			return dataResult;
		}
		int effectRowCount = 0;
		int statusSuccCount = 0;
		String toDelIds = "";
		String succDelIds = "";
		String failDelIds = "";
		for(String id : idArr){
			DbRvo dbRvo = ContainerFactory.getSession().deleteById(beanClass, id);
			boolean status = dbRvo.getStatus();		//根据删除结果设置响应状态，分全部删除、部分删除和全部没有删除
			toDelIds += id+",";
			if(status && 1 == dbRvo.getEffectRowCount()){
				succDelIds += id+",";
				statusSuccCount += 1;
			}else{
				failDelIds += id+",";
			}
			effectRowCount += dbRvo.getEffectRowCount();
		}
		String resStatus = "";
		StringBuilder msg = new StringBuilder();
		if(statusSuccCount == idArr.length){	//返回全部删除的id号
			resStatus = DataBsConst.BsStatus.Success.toString();
			msg = msg.append("要删除的记录id为 ").append(toDelIds).append(",成功删除符合条件的数据记录id为").append(succDelIds).append(",执行删除成功！！！");
		}else if(0 < statusSuccCount &&statusSuccCount < idArr.length){
			resStatus = DataBsConst.BsStatus.PartSuccess.toString();
			//返回删除的id号,没有删除的id号
			msg = msg.append("要删除的记录id为 ").append(toDelIds).append(",要删除符合条件的数据记录id为").append(succDelIds)
					.append("不能成功删除的记录id为").append(failDelIds).append("，请确定").append(failDelIds).append("在库中是否存在；由于部分记录删除失败，故执行删除失败！！！");
		}else{
			resStatus = DataBsConst.BsStatus.Fail.toString();
			msg = msg.append("要删除的记录id为 ").append(toDelIds).append(",记录均不能删除，删除失败！");
		}
		dataResult.setErrorCode(resStatus);	//设置响应状态
		dataResult.setEffectRowCounts(effectRowCount); 	//返回影响行数
		dataResult.setMsg(msg.toString());
		return dataResult;
	}
	
	@Override
	public BsRvo execute(BsCvo bsCvo, BsRvo bsRvo) {
		try {
			throw new java.lang.Exception("Datas的execute()五福为开发，请更换请求的bsCmd服务!");
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
		return bsRvo;
	}
	/**
	 * 查询指定id的model数据
	 * @rmx.summary
	 * <li>传入表名所对应的类全名，查询数据库，返回该表的所有记录</li>
	 * @param bsCvo
	 * @param bsRvo
	 * @return BsRvo
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public BsRvo view(BsCvo bsCvo,BsRvo bsRvo){
		DataCmd dataCmd = bsCvo.getExtend(DataCmd.class);
		String beanName = dataCmd.getBeanName();		//获取传入的表名
		Assert.notNull(beanName, "未提供查询的beanName");
		logger.info(new StringBuilder("-----------------------调用DataBs.view服务开始,查询表名为【").append(beanName).append("】-----------------------"));
		Class<Modelable> beanClass = (Class<Modelable>) ContainerFactory.getSession().obtainModelClass(beanName);		//根据beanName获取该表对应的类

		DbRvo dbRvo = ContainerFactory.getSession().queryById(beanClass, dataCmd.getId());

		DataResult dataResult = new DataResult();
		DataBsRvoBody  dataBsRvoBody = new DataBsRvoBody();
		bsRvo.setBody(dataBsRvoBody);
		bsRvo.setExtend(dataResult);
		dataResult.setDoPaging(dataCmd.isDoPaging());
		dataResult.setDoCount(dataCmd.isDoCount());
		dataResult.setRowCount(dbRvo.getRowCount());
		dataResult.setPagination(dbRvo.getPagination());
		dataResult.setRecordCount(dbRvo.getRecordCount());
		dataResult.setErrorCode(dbRvo.getStatus()? DataBsConst.BsStatus.Success.toString() : DataBsConst.BsStatus.Fail.toString());	//设置响应状态
		dataResult.setMsg("查询记录数："+dataResult.getRecordCount());
		dataBsRvoBody.setBeans(dbRvo.obtainObjects(beanClass));
		logger.info(new StringBuilder().append("查询【").append(beanName).append("】表，返回第【").append(dataResult.getPagination()).append("】页，本页记录数【").append(dataResult.getRowCount())
				.append("】，总记录数【").append(dbRvo.getRecords()).append("】")
				.append("\r\n-----------------------调用DataBs.view服务结束！------------------------"));
		return bsRvo;
	}
	/**
	 * 根据类名查询返回list
	 * @rmx.summary
	 * <li>传入表名所对应的类全名，查询数据库，返回该表的所有记录</li>
	 * @param bsCvo
	 * @param bsRvo
	 * @return BsRvo
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BsRvo list(BsCvo bsCvo,BsRvo bsRvo){
		DataCmd dataCmd = bsCvo.getExtend(DataCmd.class);
		String beanName = dataCmd.getBeanName();		//获取传入的表名
		Assert.notNull(beanName, "未提供查询的beanName");
		logger.info(new StringBuilder("-----------------------调用list服务开始,查询表名为【").append(beanName).append("】-----------------------"));
		Class<Modelable> beanClass = (Class<Modelable>) ContainerFactory.getSession().obtainModelClass(beanName);		//根据beanName获取该表对应的类
		Assert.notNull(beanClass,"根据提供的表没有查找到对应的类，请确认beanName是否正确或是否正确配置数据库表类！");
		DataResult dataResult = new DataResult();
		DbCvo cvo = new DbCvo<Modelable>((beanClass), SqlOper.list);
		//分页查询，设置每页显示的行数，要查询多少页
		// LHY 2014-9-28 默认情况必须分页，此时考虑到：数据安全级服务器资源控制。
		// 在DataCmd中设置默认值进行控制，此处后续要校验最大查询行数
		cvo.setRowCount(dataCmd.getRowCount());
		cvo.setPagination(dataCmd.getPagination());
		cvo.setDoPaging(dataCmd.isDoPaging());//dataCmd中默认为true
		cvo.setDoCount(dataCmd.isDoCount()); // dataCmd中默认为false
		if(!Judgment.nullOrBlank(dataCmd.getSidx())){
			cvo.setSortable(true);
			cvo.addOrder(true, dataCmd.getSidx(), dataCmd.getSord());
		}
		// LHY 2014-9-28       c  
		if(null != dataCmd.getOrders()){
			cvo.setOrders(dataCmd.getOrders());
		}
		if(null == dataCmd.getDataType()) cvo.setDataType(cvo.getDataType());
		else cvo.setDataType(dataCmd.getDataType());
		if(null != dataCmd.getDataColumns()){
			cvo.setDataColumns(dataCmd.getDataColumns());
		}
		SqlBeanWhere sbw = dataCmd.getFilters();
		if(null != sbw){
			cvo.setSqlBeanWhere(sbw);			//设置过滤条件
		}
		if(!Judgment.nullOrBlank(dataCmd.getSearchField()) && !Judgment.nullOrBlank(dataCmd.getSearchString())){
			cvo.addRule(dataCmd.getSearchField(), cn.remex.db.WhereRuleOper.valueOf(dataCmd.getSearchOper()), dataCmd.getSearchString());
		}
		
		DbRvo dbRvo = ContainerFactory.getSession().query(cvo);
		DataBsRvoBody  dataBsRvoBody = new DataBsRvoBody();
		bsRvo.setBody(dataBsRvoBody);
		bsRvo.setExtend(dataResult);
		dataResult.setDoPaging(dataCmd.isDoPaging());
		dataResult.setDoCount(dataCmd.isDoCount());
		dataResult.setRowCount(dbRvo.getRowCount());
		dataResult.setPagination(dbRvo.getPagination());
		dataResult.setPageCount((dbRvo.getRowCount()>0?dbRvo.getRecordCount()/cvo.getRowCount()+(dbRvo.getRecordCount()%cvo.getRowCount()>0?1:0):1));
		dataResult.setRecordCount(dbRvo.getRecordCount());
		dataResult.setErrorCode(dbRvo.getStatus()? DataBsConst.BsStatus.Success.toString() : DataBsConst.BsStatus.Fail.toString());	//设置响应状态
		dataResult.setMsg("查询记录数："+dataResult.getRecordCount());
		dataBsRvoBody.setBeans(dbRvo.obtainObjects(beanClass));
		logger.info(new StringBuilder().append("查询【").append(beanName).append("】表，返回第【").append(dataResult.getPagination()).append("】页，本页记录数【").append(dataResult.getRowCount())
				.append("】，总记录数【").append(dbRvo.getRecords()).append("】")
				.append("\r\n-----------------------调用list服务结束！------------------------"));
		return bsRvo;
	}
	
	/**
	 * @rmx.summary 根据传入的表名和指定返回的数据列dataColumns返回数据
	 * @param bsCvo
	 * @param bsRvo
	 * @return
	 */
	public BsRvo queryWithOrders(BsCvo bsCvo,BsRvo bsRvo){
		bsRvo = list( bsCvo, bsRvo);
		return bsRvo;
	}
	
	/**
	 * @rmx.summary 根据传入的表名和指定返回的数据列dataColumns返回数据
	 * @param bsCvo
	 * @param bsRvo
	 * @return
	 */
	public BsRvo queryByDataColumns(BsCvo bsCvo,BsRvo bsRvo){
		bsRvo = list( bsCvo, bsRvo);
		return bsRvo;
	}
	
	/**
	 * 根据id查询返回符合条件的结果
	 * @rmx.summary
	 * @param bsCvo
	 * @param bsRvo
	 * @return BsRvo
	 */     
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BsRvo queryByIds(BsCvo bsCvo,BsRvo bsRvo){
		DataCmd dataCmd = bsCvo.getExtend(DataCmd.class);
		String beanName = dataCmd.getBeanName();		//获取传入的表名
		Assert.notNull(beanName, "未提供查询的beanName");
		logger.info(new StringBuilder("-----------------------调用queryByIds服务开始，查询表名为【").append(beanName).append("】------------------------"));
		Class<Modelable> beanClass = (Class<Modelable>) ContainerFactory.getSession().obtainModelClass(beanName);		//根据beanName获取该表对应的类
		Assert.notNull(beanClass,"根据提供的表没有查找到对应的类，请确认beanName是否正确或是否正确配置数据库表类！");
		DbCvo cvo = new DbCvo<Modelable>(beanClass);
		String ids =  dataCmd.getIds();
		logger.info(new StringBuilder("查询ids为【").append(ids).append("】"));
		DataResult dataResult = new DataResult();
		//分页查询
		if(dataCmd.isDoPaging()){
			cvo.setDoPaging(true);
			//添加默认的分页显示行数=15
			dataCmd.setRowCount(0==dataCmd.getRowCount() ? 15 : dataCmd.getRowCount());
			dataResult.setDoPaging(true);
			int rowCount = dataCmd.getRowCount();
			cvo.setRowCount(rowCount);
			cvo.setPagination(dataCmd.getPagination());
			dataResult.setRowCount(rowCount);
			dataResult.setPagination(dataCmd.getPagination());
		}
		if (null != ids){
			cvo.addRule(RsqlConstants.PN_id, cn.remex.db.WhereRuleOper.in, ids);
			DbRvo dbRvo =ContainerFactory.getSession().query(cvo);
			DataBsRvoBody  dataBsRvoBody = new DataBsRvoBody();
			bsRvo.setBody(dataBsRvoBody);
			bsRvo.setExtend(dataResult);
			dataResult.setRecordCount(dbRvo.getRecordCount());
			dataBsRvoBody.setBeans(dbRvo.obtainObjects(beanClass));
			dataResult.setErrorCode(dbRvo.getStatus() ? DataBsConst.BsStatus.Success.toString() : DataBsConst.BsStatus.Fail.toString());	//设置响应状态
			dataResult.setMsg("查询记录数："+dataResult.getRecordCount());
		}else{
			dataResult.setMsg("请提供id以进行查询!");
		}
		logger.info(new StringBuilder("查询【").append(beanName).append("】表，返回记录数【").append(dataResult.getRecordCount())
				.append("】，").append(dataResult.isDoPaging() ? "每页显示行数："+dataResult.getRowCount() : "")
				.append("\r\n-----------------------调用queryByIds服务结束！------------------------"));
		return bsRvo;
	}
	
	
///**
// /* @rmx.summary 根据传入的Sql语句进行查询，只支持select查询，需要提供表名已返回数据，暂时不用
// * @param bsCvo
// * @param bsRvo
// * @return BsRvo
// */
//public BsRvo queryBySqlString(BsCvo bsCvo,BsRvo bsRvo){                    //根据传入的sql语句查询,需要提供beanName以返回数据
//		@SuppressWarnings("rawtypes")
//		DataCmd dataCmd = bsCvo.getExtend(DataCmd.class);
//		String beanName = dataCmd.getBeanName();
//		Assert.notNull(beanName,"若要返回查询结果，需要提供查询的beanName");
//		Class<?> beanClass = ContainerFactory.getSession().obtainModelClass(beanName);
//		String sql= dataCmd.getSqlString();
//		DataResult dataResult = new DataResult();
//		if (null != sql &&sql.toLowerCase().startsWith("select")){
//			DbRvo dbRvo = ContainerFactory.getSession().executeQuery(sql, null);
//			DataBsRvoBody dataBsRvoBody = new DataBsRvoBody();
//			dataBsRvoBody.setBeans(dbRvo.obtainObjects(beanClass));		//TODO
//			dataResult.setRecordCount(dbRvo.getRecordCount());
//			bsRvo.setBody(dataBsRvoBody);
//		}else {
//			dataResult.setMsg("sql语句为空或不以‘select’单词开始!");
//		}
//		bsRvo.setExtend(dataResult);
//		return bsRvo;
//	}
	
	
	/**
	 * @rmx.summary 增加记录基本数据列、级联保存的一对一、一对多的数据
	 * @param bsCvo
	 * @param bsRvo
	 * @return BsRvo
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RsqlTransaction
	public BsRvo add(BsCvo bsCvo,BsRvo bsRvo){         	//插入记录
		DataCmd dataCmd = bsCvo.getExtend(DataCmd.class);
		String beanName = dataCmd.getBeanName();		//获取传入的表名
		Assert.notNull(beanName, "未提供查询的beanName");
		logger.info(new StringBuilder("------------------------调用add服务开始，表名为【").append(beanName).append("】---------------------------------------------------"));
		Class<Modelable> beanClass = (Class<Modelable>) ContainerFactory.getSession().obtainModelClass(beanName);		//根据beanName获取该表对应的类
		Assert.notNull(beanClass,"根据提供的表没有查找到对应的类，请确认beanName是否正确或是否正确配置数据库表类！");
		Modelable m =bsCvo.getBody(beanClass);
		//插入的数据id为空或id=-1，否则无法插入数据
		Assert.isTrue("-1".equals(m.getId()) || Judgment.nullOrBlank(m.getId()),"要增加的记录中的id属性必须为空或-1，请确认id的值!");
		m.setDataStatus(RsqlConstants.DS_beanNew);		//请求者提供还是后端提供
		DbCvo cvo = new DbCvo<Modelable>((beanClass), SqlOper.add );
		cvo.setDataType(null == dataCmd.getDataType() ? RsqlConstants.DT_base
				+RsqlConstants.DT_object+RsqlConstants.DT_collection : dataCmd.getDataType());
		cvo.setDataColumns(dataCmd.getDataColumns());
		DbRvo dbRvo = ContainerFactory.getSession().store(m, cvo);
		DataResult dataResult = new DataResult();
		m.setId(dbRvo.getId());
		bsRvo.setBody(m);
		bsRvo.setExtend(dataResult);
		dataResult.setEffectRowCounts(dbRvo.getEffectRowCount()); 	//返回影响行数
		dataResult.setErrorCode(dbRvo.getStatus() ? DataBsConst.BsStatus.Success.toString() : DataBsConst.BsStatus.Fail.toString());	//设置响应状态
		dataResult.setMsg(dbRvo.getStatus() ?"添加"+dataResult.getEffectRowCounts()+"条数据至表《"+beanName+"》中！" :"表《"+beanName+"》中添加失败！");
		logger.info(new StringBuilder(dataResult.getMsg()).append("\r\n------------------------调用add服务结束!---------------------------------------------------"));
		Assert.notNull(bsRvo.getExtend(), "服务响应Extend节点为空");
		return bsRvo;
	}
	
	/**
	 * @rmx.summary 增加一条记录，只允许添加基本数据列
	 * @param bsCvo
	 * @param bsRvo
	 * @return BsRvo
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RsqlTransaction
	public BsRvo addBase(BsCvo bsCvo,BsRvo bsRvo){
		DataCmd dataCmd = bsCvo.getExtend(DataCmd.class);
		String beanName = dataCmd.getBeanName();		//获取传入的表名
		Assert.notNull(beanName, "未提供查询的beanName");
		logger.info(new StringBuilder("------------------------调用addBase服务开始，表名为【").append(beanName).append("】---------------------------------------------------"));
		Class<Modelable> beanClass = (Class<Modelable>) ContainerFactory.getSession().obtainModelClass(beanName);		//根据beanName获取该表对应的类
		Assert.notNull(beanClass,"根据提供的表没有查找到对应的类，请确认beanName是否正确或是否正确配置数据库表类！");
		Modelable m =bsCvo.getBody(beanClass);
		//插入的数据id为空或id=-1，否则无法插入数据
		Assert.isTrue("-1".equals(m.getId()) || null == m.getId(),"要增加的记录中的id属性必须为空或-1，请确认id的值!");
		m.setDataStatus(RsqlConstants.DS_beanNew);		//请求者提供还是后端提供
		Assert.isTrue(null == dataCmd.getDataType() || RsqlConstants.DT_base.equals(dataCmd.getDataType()), "数据类型必须为null或bd");
		DbCvo cvo = new DbCvo<Modelable>((beanClass), SqlOper.add, RsqlConstants.DT_base);		//TODO 是否需要自己制定dateType
		cvo.setDataColumns(null == dataCmd.getDataColumns() ? cvo.getDataColumns() : dataCmd.getDataColumns());
		DbRvo dbRvo = ContainerFactory.getSession().store(m, cvo);    //TODO       对数据库的操作
		DataResult dataResult = new DataResult();
		bsRvo.setExtend(dataResult);
		dataResult.setEffectRowCounts(dbRvo.getEffectRowCount()); 	//返回影响行数
		dataResult.setErrorCode(dbRvo.getStatus() ? DataBsConst.BsStatus.Success.toString() : DataBsConst.BsStatus.Fail.toString());
		dataResult.setMsg(dbRvo.getStatus() ?"添加"+dataResult.getEffectRowCounts()+"条数据至表《"+beanName+"》中！" :"表《"+beanName+"》中添加失败！");
		logger.info(new StringBuilder(dataResult.getMsg())
				.append("\r\n------------------------调用addBase服务结束!---------------------------------------------------"));
		return bsRvo;
	}

	/**
	 * @rmx.summary 根据条件修改记录 ，每次值允许修改一条记录
	 * <li>修改记录中指定的基本数据列的数据</li>
	 * <li>修改记录的指定外键数据列（考虑级联，考虑一对多的多方维护）</li>
	 * <li>修改记录的一对多、多对多关系（考虑级联）</li>
	 * @param bsCvo
	 * @param bsRvo
	 * @return BsRvo
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RsqlTransaction
	public BsRvo update(BsCvo bsCvo,BsRvo bsRvo){
		DataCmd dataCmd = bsCvo.getExtend(DataCmd.class);
		String beanName = dataCmd.getBeanName();		//获取传入的表名
		Assert.notNull(beanName, "未提供查询的beanName");
		logger.info(new StringBuilder("------------------------调用update服务开始，表名为【").append(beanName).append("】---------------------------------------------------"));
		String dataType = dataCmd.getDataType();
		Class<Modelable> beanClass = (Class<Modelable>) ContainerFactory.getSession().obtainModelClass(beanName);		//根据beanName获取该表对应的类
		Assert.notNull(beanClass,"根据提供的表没有查找到对应的类，请确认beanName是否正确或是否正确配置数据库表类！");
		Modelable m =bsCvo.getBody(beanClass);
		//更新的数据id不能为空
		Assert.isTrue(null != m.getId() &&!("-1".equals(m.getId())), "要更新的记录id不允许为空！");
		//更新的数据的数据状态必须为managed或needSave // LHY 因为是显示调用update，故需要制定dataStatus为needSave
		m.setDataStatus(RsqlConstants.DS_needSave);
//		Assert.isTrue(RsqlConstants.DS_managed.equals(m.getDataStatus()) || RsqlConstants.DS_needSave.equals(m.getDataStatus()), 
//				"更新数据的数据状态不是managed或needSave");
		DbCvo cvo = null;
		//判断数据类型，根据数据类型修改数据
		String cvoDataType="";
		boolean dataTypeFlag = false;
		if(null == dataType || dataType.contains(RsqlConstants.DT_base)){
			cvoDataType += RsqlConstants.DT_base;
		}
		if(dataType.contains(RsqlConstants.DT_object)){
			dataTypeFlag = true;
			cvoDataType += RsqlConstants.DT_object;
		}
		if(dataType.contains(RsqlConstants.DT_collection)){
			dataTypeFlag = true;
			cvoDataType += RsqlConstants.DT_collection;
		}
		cvo = new DbCvo<Modelable>((beanClass), SqlOper.edit,cvoDataType);
		String dc = dataCmd.getDataColumns();
		if(null != dc){
			cvo.setDataColumns(dc);
		}
		//修订：新增本行和下一行，本方法为update方法，若不获取对象，仅仅通过id去更新，会将m的信息覆盖原信息，有时只想更新几个字段，却导致没有涉及的字段更新后为null，添加则后不会
		Modelable oldModel = ContainerFactory.getSession().queryBeanById(beanClass, m.getId());
		Assert.notNull(oldModel,"数据库中没有该数据，可能已经被删除！");
		if(dataTypeFlag){
			ReflectUtil.copyProperties(oldModel, m, SPFeature.DeeplyCopy,SPFeature.CopyIdAndDataStatus);		//TODO 比较危险，待后续调整
		}else{
			ReflectUtil.copyProperties(oldModel, m);
			
		}
		DbRvo dbRvo =ContainerFactory.getSession().store(oldModel,cvo);
		DataResult dataResult = new DataResult();
		dataResult.setEffectRowCounts(dbRvo.getEffectRowCount()); 	//返回影响行数
		dataResult.setErrorCode(dbRvo.getStatus() ? DataBsConst.BsStatus.Success.toString() : DataBsConst.BsStatus.Fail.toString());	//设置响应状态
		bsRvo.setExtend(dataResult);
		dataResult.setMsg("修改表《"+beanName+"》中的"+dataResult.getEffectRowCounts()+"条数据！");
		logger.info(dataResult.getMsg());
		logger.info("------------------------调用update服务结束!---------------------------------------------------");
		return bsRvo;
	}
     
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BsRvo copy(BsCvo bsCvo,BsRvo bsRvo){
		DataCmd dataCmd = bsCvo.getExtend(DataCmd.class);
		String beanName = dataCmd.getBeanName();		//获取传入的表名
		Assert.notNull(beanName, "未提供查询的beanName");
		logger.info(new StringBuilder("------------------------调用copy服务开始，表名为【").append(beanName).append("】---------------------------------------------------"));
		Class<Modelable> beanClass = (Class<Modelable>) ContainerFactory.getSession().obtainModelClass(beanName);		//根据beanName获取该表对应的类
		Assert.notNull(beanClass,"根据提供的表没有查找到对应的类，请确认beanName是否正确或是否正确配置数据库表类！");
		//更新的数据id不能为空
		Assert.isTrue(null != dataCmd.getId() && null != dataCmd.getIds(),"id和ids为空，无法复制!");
		DbCvo cvo = new DbCvo<Modelable>((beanClass));
		cvo.setBeanName(beanName);
		cvo.$S("uniqueFields", dataCmd.getUniqueFields());
		cvo.$S(RsqlConstants.SYS_ids, dataCmd.getIds());
		cvo.$S(RsqlConstants.SYS_id, dataCmd.getIds());
		//判断数据类型，根据数据类型修改数据
		//修订：新增本行和下一行，本方法为update方法，若不获取对象，仅仅通过id去更新，会将m的信息覆盖原信息，有时只想更新几个字段，却导致没有涉及的字段更新后为null，添加则后不会
		DbRvo dbRvo =ContainerFactory.getSession().copy(cvo);
		DataResult dataResult = new DataResult();
//		dataResult.setEffectRowCounts(dbRvo.getEffectRowCount()); 	//返回影响行数
		dataResult.setErrorCode(dbRvo.getStatus() ? DataBsConst.BsStatus.Success.toString() : DataBsConst.BsStatus.Fail.toString());	//设置响应状态
		dataResult.setMsg("修改表《"+beanName+"》中的"+dataResult.getEffectRowCounts()+"条数据！");
		bsRvo.setExtend(dataResult);
		logger.info(dataResult.getMsg());
		logger.info("------------------------调用copy服务结束!---------------------------------------------------");
		return bsRvo;
		
	}

	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public BsRvo updateByDataColumns(BsCvo bsCvo,BsRvo bsRvo){
//		DataCmd dataCmd = bsCvo.getExtend(DataCmd.class);
//		String beanName = dataCmd.getBeanName();
//		Assert.notNull(beanName, "未提供查询的beanName");
//		Class<Modelable> beanClass = (Class<Modelable>) ContainerFactory.getSession().obtainModelClass(beanName);
//		Modelable m =bsCvo.getBody(beanClass);
//		Assert.isTrue(null != m.getId() &&!("-1".equals(m.getId())), "要更新的记录簿允许为空！");
//		DbCvo cvo = new DbCvo<Modelable>((beanClass), SqlOper.edit,RsqlConstants.DT_base);
//		cvo.setDataColumns(dataCmd.getDataColumns());
//		DbRvo dbRvo =ContainerFactory.getSession().store(m,cvo);
//		DataResult dataResult = new DataResult();
//		dataResult.setEffectRowCounts(dbRvo.getEffectRowCount());
//		dataResult.setStatus(dbRvo.getStatus() ? DataBsConst.BsStatus.Success.toString() : DataBsConst.BsStatus.Fail.toString());
//		bsRvo.setExtend(dataResult);
//		return bsRvo;
//	}
	

//	/**
//	 * 保存特定场景数据的方法
//	 * @rmx.summary 主要针对一些有明显边界的相关场景的添加数据的需求。
//	 * @param bsCvo
//	 * @param bsRvo
//	 * @return BsRvo
//	 */
//	public BsRvo add(BsCvo bsCvo,BsRvo bsRvo){
//		return null;
//	}
//	
//	/**
//	 * 删除特殊场景的数据	TODO
//	 * @rmx.summary 主要针对一些有明显边界的相关场景的删除数据的需求，以增加数据的安全性。
//	 * 如某些场景下删除数据时的一些有公共处理部分的情况，以及有公共条件限制的情况
//	 * @param bsCvo
//	 * @param bsRvo
//	 * @return
//	 */
//	public BsRvo del(BsCvo bsCvo,BsRvo bsRvo){
//		return null;
//	}
//	/**
//	 * 修改特殊场景的数据 TODO
//	 * @param bsCvo
//	 * @param bsRvo
//	 * @return
//	 */
//	public BsRvo edit(BsCvo bsCvo,BsRvo bsRvo){
//		return null;
//	}
//	/**
//	 * 查询数据-针对特殊场景的查询数据，增加数据的安全性
//	 * @param bsCvo
//	 * @param bsRvo
//	 * @return
//	 */
//	public BsRvo list(BsCvo bsCvo,BsRvo bsRvo){
//		
//		DbCvo dbCvo = convertCvo(bsCvo);
//		dbCvo.$S("oper", "list");
//		DbRvo dbRvo = ContainerFactory.getSession((String) dbCvo.$V("poolName")).execute(dbCvo);
//		
//		bsRvo.installHead(dbRvo,null);
//		
//		String rt = dbCvo.$V(RsqlConstants.PN_rt);
//		if(RsqlConstants.RT_obj.equals(rt))
//			bsRvo.installBody(dbRvo.obtainObjects(beanClass),null);
//		else
//			bsRvo.installBody(dbRvo.getGridData(),null);
//			
//		bsRvo.installExtend(dbRvo,null);
//		
//		return bsRvo;
//	}
//	public BsRvo view(BsCvo bsCvo,BsRvo bsRvo){
//		return null;
//	}
//	/**
//	 * 指定查询的字段查询数据
//	 * @param bsCvo
//	 * @param bsRvo
//	 * @return
//	 */
//	public BsRvo listFields(BsCvo bsCvo,BsRvo bsRvo){
//		return null;
//	}
}
