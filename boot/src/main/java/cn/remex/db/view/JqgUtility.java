package cn.remex.db.view;

import cn.remex.RemexConstants;
import cn.remex.core.util.JsonHelper;
import cn.remex.db.Container;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbCvo;
import cn.remex.db.model.view.JqgColModel;
import cn.remex.db.rsql.RsqlAssert;
import cn.remex.db.rsql.RsqlRvo;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.sql.SqlType;
import cn.remex.db.sql.FieldType;
import cn.remex.db.sql.WhereRuleOper;

import java.lang.reflect.Type;
import java.util.*;

public class JqgUtility implements RemexConstants {

	public final static String[] JSonJqgColModels = {
			"{name:'id',			editable:true,hidden:true,label:'ID',width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",

			"{name:'beanName',		editable:true,label:'Bean名称',width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'fieldName',		editable:true,label:'Bean属性',width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'name',			editable:true,label:'名称',width:60,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'idx',			editable:true,label:'index',width:60,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'label',			editable:true,label:'标题',width:60,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'colModelIndex',	editable:true,label:'序号',width:30,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'width',			editable:true,label:'宽度',width:30,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'hidden',		editable:true,label:'隐藏',width:30,search:true,edittype:'checkbox',editoptions:\"{value:'true:false'}\",searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'editable',		editable:true,label:'编辑',width:30,search:true,edittype:'checkbox',editoptions:\"{value:'true:false'}\",searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'sortable',		editable:true,label:'排序',width:30,search:true,edittype:'checkbox',editoptions:\"{value:'true:false'}\",searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'search',		editable:true,label:'搜索',width:30,search:true,edittype:'checkbox',editoptions:\"{value:'true:false'}\",searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'align',			editable:true,label:'对齐',width:40,search:true,edittype:'select',editoptions:\"{value:{'left':'left','center':'center','right':'right'}}\",searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'resizable',		editable:true,label:'可变大小',width:30,search:true,edittype:'checkbox',editoptions:\"{value:'true:false'}\",searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'searchoptions',	editable:true,label:'搜索选项',width:150,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'editoptions',	editable:true,label:'编辑选项',width:150,search:true,edittype:'custom',"
					+ "editoptions:\"{custom_element: RJQ.RefEle, custom_value:RJQ.RefVal,custom_type:'jqgEditOptions'}\",searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'editrules',		editable:true,label:'编辑规则',width:150,search:true,edittype:'text',"
					+ "searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'edittype',		editable:true,label:'编辑类型',width:60,search:true,edittype:'select',"
					+ "editoptions:\"{value:{'text':'text','textarea':'textarea','checkbox':'checkbox','select':'select','password':'password','button':'button','password':'password','image':'image','file':'file','custom':'custom'}}\",searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",

			"{name:'summaryType',	editable:true,width:80,search:true,edittype:'select',editoptions:\"{value:{'sum':'sum','count':'count','min':'min','max':'max'}}\",searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'summaryTpl',	editable:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			
			
			
			"{name:'title',			editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'colNames',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",

			"{name:'formatter',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'colModel',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'datefmt',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'jsonmap',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'iskey',			editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'viewable',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'cellattr',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'classes',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'defval',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'firstsortorder',editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'fixed',			editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'formoptions',	editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'formatoptions',	editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'hidedlg',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'sorttype',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'stype',			editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'surl',			editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'template',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'xmlmap',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'unformat',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'version',		editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}",
			"{name:'dataStatus',	editable:true,hidden:true,width:80,search:true,searchoptions:\"{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }\",viewable:true}" };
	static private Map<String, List<JqgColModel>> JqgColModelCache = new HashMap<String, List<JqgColModel>>();

	static private Map<String, String> JqgColModelStrings = new HashMap<String, String>();

	public static void clearCache() {
		JqgColModelStrings.clear();
		JqgColModelCache.clear();
	}

	static public synchronized List<JqgColModel> createColModel(final String beanName, final String beanClassName) {
		List<JqgColModel> colModels = new ArrayList<JqgColModel>();

		Class<?> clazz = RDBManager.getLocalSpaceConfig().getOrmBeanClass(beanName);
		// 分两类custom custom_element: $.remex.jgq.jRemexRefEle,
		// custom_value:$.remex.jgq.jRemexRefVal,custom_type:'jqgEditType'
		// 如果是JqgColModel本书，则在程序中定义配置
		if (clazz == JqgColModel.class) {
			for (int i = 0; i < JSonJqgColModels.length; i++) {
//				JSONObject jcm = JSONObject.fromObject(JSonJqgColModels[i]);
//				JqgColModel colModel = (JqgColModel) JSONObject.toBean(jcm, JqgColModel.class);
				JqgColModel colModel = JsonHelper.toJavaObject(JSonJqgColModels[i], JqgColModel.class);
				colModel.setBeanName(beanName);
				colModel.setFieldName(colModel.getName());
				colModel.setBeanClassName(clazz.getName());
				colModel.setIdx(colModel.getName());
				colModel.setColModelIndex(i);

				colModels.add(colModel);
			}

		} else {
			// 否则根据class的数据属性创建模型
			Map<String, Type> baseFields = SqlType.getFields(clazz, FieldType.TBase);

			for (String fieldName : baseFields.keySet()) {
				JqgColModel colModel = new JqgColModel();
				colModel.setBeanName(beanName);

				ViewUtil.buildCommConfig(clazz, fieldName, colModel);
				ViewUtil.buildEditConfig(clazz, fieldName, baseFields.get(fieldName), colModel);

				colModels.add(colModel);
			}
			Map<String, Type> objectFields = SqlType.getFields(clazz, FieldType.TObject);
			for (String fieldName : objectFields.keySet()) {
				JqgColModel colModel = new JqgColModel();
				colModel.setBeanName(beanName);

				ViewUtil.buildCommConfig(clazz, fieldName, colModel);
				ViewUtil.buildEditConfig(clazz, fieldName, objectFields.get(fieldName), colModel);
				colModels.add(colModel);
				colModel.setHidden(true);// 参照列隐藏起来。

				// 创建显示列
				if (!ViewUtil.NoRefColumn.contains(fieldName)) {
					JqgColModel colModel_ref = new JqgColModel();
					colModel_ref.setColModelIndex(colModel.getColModelIndex());
					colModel_ref.setWidth(colModel.getWidth());
					colModel_ref.setBeanName(beanName);
					ViewUtil.buildRefDisplayColumn(fieldName, objectFields.get(fieldName), colModel_ref);
					colModels.add(colModel_ref);
				}
			}
		}

		// 排序
		Collections.sort(colModels);

		return colModels;

	}

	static public List<JqgColModel> obtainColumnModels(final String beanName) {
		List<JqgColModel> colModels = JqgColModelCache.get(beanName);
		// 调试模式下 或者 缓存中没有都需要去创建。
		if (logger.isDebugEnabled() || null == colModels) {
			// 先尝试从数据库中查询获取
			RsqlRvo rvo = (RsqlRvo) ContainerFactory.createDbCvo(JqgColModel.class).filterBy(JqgColModel::getBeanName,WhereRuleOper.eq,beanName).ready().query();
			if (rvo.getRowCount() > 0) {
				colModels = rvo.obtainBeans();
				Collections.sort(colModels);
			} else {
				Class<?> clazz = RDBManager.getLocalSpaceConfig().getOrmBeanClass(beanName);
			
				// 如果数据中没有则根据bean来自动创建并保存到数据库中
				colModels = createColModel(beanName, clazz.getName());
				// 保存到数据库
				for (JqgColModel jcm : colModels) {
					ContainerFactory.getSession().store(jcm);
				}
			}
			// 存入缓存
			JqgColModelCache.put(beanName, colModels);
		}
		return colModels;
	}

	static public String obtainColumnModelString(final String beanName, final String beanClassName) throws Exception {
		String colModels = JqgColModelStrings.get(beanName);
		if (null != colModels) {
			return colModels;
		}

		// 自动创建
		StringBuilder builder = new StringBuilder();
		builder.append("[\r\n");
		List<JqgColModel> colModel = obtainColumnModels(beanName);
		for (JqgColModel cm : colModel) {
			builder.append(cm.toString());
			builder.append(",\r\n");
		}
		builder.delete(builder.length() - 3, builder.length());
		builder.append("]");

		JqgColModelStrings.put(beanName, builder.toString());
		return builder.toString();
	}

}
