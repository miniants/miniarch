/**
 * 
 */
package cn.remex.db.view;

import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Judgment;
import cn.remex.db.model.view.JqgColModel;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author Hengyang Liu
 * @since 2012-4-30
 *
 */
public class ViewUtil {
	public static final Element DefaultElement = ReflectUtil.getAnnotation(ElementDefault.class, "defaultElement", Element.class);
	public static final String DefaultLabel= "未设置";

	public static final ArrayList<String> NoRefColumn = new ArrayList<String>();
	static {
		NoRefColumn.add("createOperator");
		NoRefColumn.add("modifyOperator");
		NoRefColumn.add("ownership");
	}



	public static void buildCommConfig(final Class<?> ormBeanClass,final String fieldName,final JqgColModel colModel){
		Element e = ReflectUtil.getAnnotation(ormBeanClass, fieldName, Element.class);
		if(e == null) {
			e=DefaultElement;
		}

		colModel.setName(fieldName);
		colModel.setFieldName(fieldName);
		colModel.setBeanClassName(fieldName);
		colModel.setIdx(fieldName);

		colModel.setWidth(e.width());
		colModel.setHidden(e.hidden());
		colModel.setLabel(e.label().equals(DefaultLabel)?fieldName:e.label());
		colModel.setColModelIndex(e.colModelIndex());
	}
	/**
	 * 显示层的设置往往由EditOptions、EditRules,EditType,Editable四部分组成。此方法根据ormBeanClass及所指field来生成目标视图格式
	 * @param ormBeanClass
	 * @param fieldType
	 * @param element
	 * @param field
	 * @return
	 */
	public static void buildEditConfig(final Class<?> ormBeanClass,final String fieldName,final Type fieldType, final JqgColModel colModel){
		Element e = ReflectUtil.getAnnotation(ormBeanClass, fieldName, Element.class);
		if(e == null) {
			e=DefaultElement;
		}

		colModel.setEditable(e.editable());
		EditType et = e.edittype();
		
		if(boolean.class == fieldType
				|| Boolean.class == fieldType) {
			et=EditType.checkbox;
		}/*else if(fieldType instanceof Class && Enum.class.isAssignableFrom((Class)fieldType)){
			et=EditType.EnumRef;
		}*/ //TODO 将来让枚举也能在前端页面匹配上
		switch (et ) {
		case text:
			colModel.setEdittype(EditType.text.toString());
			break;
		case CodeRef:
			colModel.setEdittype(EditType.CodeRef.toString());
			String codeType = Judgment.nullOrBlank(e.CodeRefCodeType())?fieldName:e.CodeRefCodeType();
			colModel.setEditoptions("{beanName:'"+e.CodeRefBean().getSimpleName()
					+"',typeColumn:'"+e.CodeRefTypeColumn()+"',codeColumn:'"+e.CodeRefCodeColumn()+"',descColumn:'"+e.CodeRefDescColumn()+"',codeType:'"+codeType +"'}");
			break;
		case EnumRef:
			colModel.setEdittype(EditType.EnumRef.toString());
			colModel.setEditoptions("{beanName:'"+((Class)fieldType).getName()+"'}");
			break;
		case custom:
			colModel.setEdittype(EditType.custom.toString());
			colModel.setEditoptions("{custom_element: RJQ.RefEle, custom_value:RJQ.RefVal,autoSearch:true," +
					"custom_type:'" +e.refType()+
					"',beanName:'"+e.refClass().getSimpleName()+"'}");
			break;
		case checkbox:
			colModel.setEdittype("checkbox");
			colModel.setEditoptions("{value:'true:false'}");
			break;
		case select:
			colModel.setEdittype("select");
			colModel.setEditoptions(e.editoptions());
			break;

		default:
			break;
		}

		return;
	}
	/**
	 * @param fieldName
	 * @param fieldType
	 * @param colModels
	 */
	public static void buildRefDisplayColumn(final String fieldName, final Type fieldType,final JqgColModel colModel) {

		colModel.setFieldName(fieldName);
		colModel.setLabel(fieldName);
		colModel.setName(fieldName+".name"); //显示名称
		/**
		 * 2013-4-30
		 * 数据库映射名称由于是map数据映射，并没有通过javabean转化，所以此处参照名称应该直接对应数据库中的外键列。
		 */
		colModel.setIdx(fieldName);
		colModel.setEditable(true);
		colModel.setWidth(80);
		colModel.setEdittype(EditType.custom.toString());
		colModel.setEditoptions("{custom_element: RJQ.RefEle, custom_value:RJQ.RefVal,autoSearch:true," +
				"custom_type:'dbList"+
				"',beanName:'"+((Class<?>)fieldType).getSimpleName()+"'}");
		colModel.setColModelIndex(colModel.getColModelIndex());

	}
}
