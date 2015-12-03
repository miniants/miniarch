package cn.remex.db.rsql;

import cn.remex.db.exception.RsqlBeanStatusException;
import org.apache.log4j.Logger;

public interface RsqlConstants {

	/**
	 * @author Hengyang Liu
	 * @since 2012-4-30
	 */
	public enum DataStatus{
		needSave,saving,beanNew,removed,managed,part;
		public boolean equalsString(String status){
			boolean b = false;
			try{
				DataStatus enumStatus = DataStatus.valueOf(status);
				b  = this==enumStatus;
			}catch (Exception e) {
				throw new RsqlBeanStatusException("数据库持久化状态异常！输入状态为:"+status,e);
			}
			return b;
		}
	}

	/**
	 * 分组多条件查询的组合逻辑关系
	 */
	public enum WhereGroupOp{
		AND,
		OR
	}

	public enum SqlOper{
		/**增加*/
		add,
		/**删除*/
		del,
		/**编辑*/
		edit,
		/**执行*/
		execute,
		/** 查看数据 **/
		list,
		/**数据存储*/
		store,
		/**数据查看*/
		view,
		/**复制*/
		copy,
		/***/
		sql
	}
//	public static final String DO_add = "add";
//	/**
//	 * 数据库操作指令:
//	 * 1.add 2.del 3.edit 4.list 5.view以及自定义操作6.execute
//	 */
//	public static final String DO_del = "del";
//	public static final String DO_edit = "edit";
//	public static final String DO_execute = "execute";
//	public static final String DO_list = "list";
//	public static final String DO_store = "store";
//	public static final String DO_view = "view";
//	public static final String DO_copy = "copy";
//	public static final String DO_sql = "sql";

//	public static final String DO_ALL=DO_add+DO_del+DO_edit+DO_list+DO_view+DO_execute+DO_store+DO_copy;

	//	public static final String SYS_Method_getId="";
	//	public static final String SYS_Method_getId="";
	/**
	 * bean的数据状态
	 **/
	/**
	 * 数据状态dataStatus
	 */
	//public static final String DS_beanOnlyId="beanOnlyId"; 已经没有了。
	public static final String DS_beanNew="beanNew";
	public static final String DS_managed="managed";
	public static final String DS_part="part";
	public static final String DS_needSave="needSave";
	
//	public static final String DS_normal="normal";
	public static final String DS_removed="removed";

//	public static final String DS_saved="saved"; //用managed替代
	public static final String DS_saving = "saving";
	/**
	 * 储存数据的基本类型
	 */
	public static final String DT_base = "bd";
	/**
	 * 一对多外键数据，列表型 collection,set,vector,List
	 */
	public static final String DT_collection = "cd";

	/**
	 * 一对多，可能含数据的map外键型
	 */
	public static final String DT_map = "md";
	/**
	 * 一对一外键数据
	 */
	public static final String DT_object = "od";
	public static final String DT_objectExt = "oed";
	public static final String DT_present = "pd";
	public static final String DT_realDelete = "rd";
	public static final String DT_selfDefination = "sd";
	public static final String DT_unrealDelete = "ud";
	/**
	 * 完全数据模型
	 */
	public static final String DT_whole = DT_base+DT_object+DT_objectExt+DT_collection+DT_map;
	static Logger logger=Logger.getLogger(RsqlConstants.class);
	static boolean isDebug = logger.isDebugEnabled();
	public static final String PN_bn = "bn";//beanName的缩写
	public static final String PN_model = "model";//beanName的缩写


	public static final String PN_bs = "service";
	public static final String PN_bsCmd = "bsCmd";


	/** 需要使用的数据列  */
	public static final String PN_dc = "dc";//dataColumns的缩写
	/**
	 *  需要使用的数据类型
	 *  dt可选的值为bd,od,oed。
	 */
	public static final String PN_dt = "dt";//dataType的缩写
	/** ExtColumn的缩写 */
	public static final String PN_ec = "ec";
	/** foreignBean的缩写。及外联的一对一关系表*/
	public static final String PN_fb = "fb";
	public static final String PN_filter = "filters";
	public static final String PN_filterBean = "filterBean";
	public static final String PN_id = "id";

	public static final String PN_JAVASqlBean = "jasb";
	public static final String PN_jqg_pagination = "page";
	//需转换的参数
	public static final String PN_jqg_rowCount = "rows";
	public static final String PN_JSONSqlBean = "jssb";
	/**
	 * request、cvo调用传递参数名
	 */
	public static final String PN_sqlString = "sqlString";
	public static final String PN_oper ="oper";

	public static final String PN_pagination = "pagination";
	public static final String PN_doPaging = "doPaging";
	//public static final String PN_whCdnObj = "filtersObject";//暂时没用
	public static final String PN_doCount = "doCount";
	public static final String PN_rowCount = "rowCount";
	public static final String PN_recordCount = "recordCount";

	/**
	 * 返回类型，指struts2返回页面的数据类型。json、jsp等选项
	 */
	public static final String PN_rt = "rt";

	public static final String PN_search = "_search";
	public static final String PN_searchField = "searchField";
	public static final String PN_searchOper = "searchOper";
	public static final String PN_searchString = "searchString";
	public static final String PN_sidx = "sidx";
	public static final String PN_sord = "sord";
	/**
	 * 数据库查询模型(SqlBeanType)
	 */
	public static final String PN_SqlBeanType = "SqlBeanType";
	public static final String PN_subList="subList";
	public static final String PN_listFied="listField";//指定listField的属性，可以链式引用比如，AuthUser表中
	public static final String PN_parentIds="parentIds";//与listField组合使用，用来查询指定的listField的

	public static final String PN_whOptById = "_searchById";

	public static final String RT_map = "map";
	/*
	 * json返回的数据类型
	 */
	public static final String RT_obj = "obj";
	public static final String SBTP_JAVA = "JAVA";
	public static final String SBTP_JSON = "JSON";
	public static final String SBTP_LIST = "LIST";
	/*
	 * 预定义sys key
	 */
	public static final String SYS_createOperator = "createOperator";
	public static final String SYS_createOperator_name = "createOperator_name";
	public static final String SYS_createTime = "createTime";
	public static final String SYS_dataStatus = "dataStatus";
	public static final String SYS_version = "version";
	public static final String SYS_id = "id";
	public static final String SYS_ids = "ids";
	public static final String SYS_key = "SYSKEY";
	public static final String SYS_key_idPreffix = "ID_";
	public static final String SYS_Method_getDataStatus="getDataStatus";
	public static final String SYS_Method_getId="getId";
	public static final String SYS_Method_setDataStatus="setDataStatus";
	public static final String SYS_Method_setId="setId";
	public static final String SYS_modifyOperator = "modifyOperator";
	public static final String SYS_modifyOperator_name = "modifyOperator_name";
	public static final String SYS_modifyTime = "modifyTime";
	public static final String SYS_name = "name";//此列一般在对象中都有，用于默认检索一对一外键是显示用的
	public static final String SYS_ownership = "ownership";
	public static final String SYS_ownership_name = "ownership_name";
	



	public static final String SYS_tableName = "SYSTEMKEY";
	public static final String SYS_value = "SYSVALUE";


	public static final String SQL_IDENTITY = "IDENTITY (2000000000, 1)";
	/**
	 * 定义自动增量的id，为了和以前的db40区别开来，起始值修改为2
	 */
	public static final String SQL_Insert_Identity = "SELECT  @@IDENTITY  AS " + SYS_id;
	public static final String PN_spaceName = "defautl";
}
