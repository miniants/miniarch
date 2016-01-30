/**
 * 
 */
package cn.remex.db.view;

import cn.remex.db.model.DataDic;
import cn.remex.db.rsql.model.ModelableImpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Hengyang Liu
 * @since 2012-4-28
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD,ElementType.FIELD})
public @interface Element {
	public int colModelIndex() default 20;

	public boolean editable() default true;

	public String editoptions() default "{value:{1:'One',2:'Two'}}";
	/**
	 * @see EditType
	 * @return
	 */
	public EditType edittype() default EditType.text;
	public boolean hidden() default false;

	/**
	 * 显示层显示的名称
	 * 
	 * @return
	 */
	public String label() default ViewUtil.DefaultLabel;

	public Class<?> refClass() default ModelableImpl.class;

	public String refName() default "name";
	public Class<?> CodeRefBean() default DataDic.class;
	/**要显示的值*/
	public String CodeRefCodeColumn() default "dataCode";
	/**要显示的字段*/
	public String CodeRefDescColumn() default "dataDesc";
	/**默认数据字典表中去搜索匹配的字段*/
	public String CodeRefTypeColumn() default "dataType";
	/**默认数据字典表中 dataType=的值*/
	public String CodeRefCodeType() default "";
	/**搜索的filter字符串，有filters是，dataType约束失效
	 * filter="{rules:[{field:'col1',op:'eq',data:'val1'},{field:'col2',op:'eq',data:'val2'}]}"
	 * */
	public String CodeRefFilters() default "";
	public RefType refType() default RefType.dbList;
	

	/**
	 * 显示层控件的宽度width。符合css标准，单位为px;整形
	 * 
	 * @return
	 */
	public int width() default 80;

}
