package cn.remex.db.model.view;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.Column;

public class JqgColModel extends ModelableImpl implements
Comparable<JqgColModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3828577709878191227L;

	@Column(length = 10, columnDefinition = " ")
	private String align = "left";
	@Column(length = 300, columnDefinition = " ")
	private String beanClassName;
	@Column(length = 60, columnDefinition = " ")
	private String beanName;

	@Column(length = 50, columnDefinition = " ")
	private String cellattr;
	@Column(length = 50, columnDefinition = " ")
	private String classes;
	@Column(length = 50, columnDefinition = " ")
	private String colModel;

	private int colModelIndex = 100;
	@Column(length = 50, columnDefinition = " ")
	private String colNames;
	@Column(length = 200, columnDefinition = " ")
	private String datefmt;
	@Column(length = 200, columnDefinition = " ")
	private String defval;
	private boolean editable;
	@Column(length = 600, columnDefinition = " ")
	private String editoptions; // array
	@Column(length = 20, columnDefinition = " ")
	private String editrules;// array
	@Column(length = 20, columnDefinition = " ")
	private String edittype;
	@Column(length = 300, columnDefinition = " ")
	private String fieldName;
	@Column(length = 50, columnDefinition = " ")
	private String firstsortorder;
	private boolean fixed;
	@Column(length = 50, columnDefinition = " ")
	private String formatoptions;// array
	@Column(length = 50, columnDefinition = " ")
	private String formatter;// mixed
	@Column(length = 50, columnDefinition = " ")
	private String formoptions;// array
	private boolean hidden;
	private boolean hidedlg;
	@Column(length = 50, columnDefinition = " ")
	private String idx;
	private boolean iskey;
	@Column(length = 200, columnDefinition = " ")
	private String jsonmap;
	@Column(length = 30, columnDefinition = " ")
	private String label;
	@Column(length = 50, columnDefinition = " ")
	private String name;

	private boolean resizable = true;
	private boolean search = true;
	@Column(length = 600, columnDefinition = " ")
	private String searchoptions = "{sopt:['cn','eq','ne','lt','le','gt','ge','bw','bn','ew','en','nc'] }";// array
	private boolean sortable = true;
	@Column(length = 30, columnDefinition = " ")
	private String sorttype;// mixed
	@Column(length = 30, columnDefinition = " ")
	private String stype;
	private String surl;
	private String template;// object
	private boolean title;
	private String unformat;// function
	private boolean viewable = true;
	private int width;
	private String xmlmap;

	// String align
	// function cellattr
	// String classes
	// String datefmt
	// String defval
	// boolean editable
	// array editoptions
	// array editrules
	// String edittype
	// String firstsortorder
	// boolean fixed
	// array formoptions
	// array formatoptions
	// mixed formatter
	// boolean hidedlg
	// boolean hidden
	// String index
	// String jsonmap
	// boolean key
	// String label
	// String name
	// boolean resizable
	// boolean search
	// array searchoptions
	// boolean sortable
	// mixed sorttype
	// String stype
	// String surl
	// object template
	// boolean title
	// number width
	// String xmlmap
	// function unformat
	// boolean viewable

	/***********************
	 * In this example we demonstrate the abilty to have a summary information at each group footer 
The appropriate options is groupSummary which enable/disable footer summary
In order to work this we added two additional properties in colModel - summaryType which descripes the summary function
and summaryTpl which is template on how data will be displayed into the footer column.
Currently we support the following functions - sum, count, min, max. In the final release we will add a possibility to
define a custom function.
	 */
	private String summaryType;
	private String summaryTpl ;
	
	public String getSummaryType() {
		return summaryType;
	}

	public void setSummaryType(String summaryType) {
		this.summaryType = summaryType;
	}

	public String getSummaryTpl() {
		return summaryTpl;
	}

	public void setSummaryTpl(String summaryTpl) {
		this.summaryTpl = summaryTpl;
	}

	@Override
	public int compareTo(final JqgColModel o) {
		// TODO Auto-generated method stub
		if( this.colModelIndex - o.colModelIndex == 0) {
			return this.name.compareTo(o.name);
		} else {
			return  this.colModelIndex - o.colModelIndex;
		}
	}

	public String getAlign() {
		return this.align;
	}

	public String getBeanClassName() {
		return this.beanClassName;
	}

	public String getBeanName() {
		return this.beanName;
	}

	public String getCellattr() {
		return this.cellattr;
	}

	public String getClasses() {
		return this.classes;
	}

	public String getColModel() {
		return this.colModel;
	}

	public int getColModelIndex() {
		return this.colModelIndex;
	}

	public String getColNames() {
		return this.colNames;
	}

	public String getDatefmt() {
		return this.datefmt;
	}

	public String getDefval() {
		return this.defval;
	}

	public String getEditoptions() {
		return this.editoptions;
	}

	public String getEditrules() {
		return this.editrules;
	}

	public String getEdittype() {
		return this.edittype;
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public String getFirstsortorder() {
		return this.firstsortorder;
	}

	public String getFormatoptions() {
		return this.formatoptions;
	}

	public String getFormatter() {
		return this.formatter;
	}

	public String getFormoptions() {
		return this.formoptions;
	}

	public String getIdx() {
		return this.idx;
	}

	public String getJsonmap() {
		return this.jsonmap;
	}

	public String getLabel() {
		return this.label;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public String getSearchoptions() {
		return this.searchoptions;
	}

	public String getSorttype() {
		return this.sorttype;
	}

	public String getStype() {
		return this.stype;
	}

	public String getSurl() {
		return this.surl;
	}

	public String getTemplate() {
		return this.template;
	}

	public String getUnformat() {
		return this.unformat;
	}

	public int getWidth() {
		return this.width;
	}

	public String getXmlmap() {
		return this.xmlmap;
	}

	public boolean isEditable() {
		return this.editable;
	}

	public boolean isFixed() {
		return this.fixed;
	}

	public boolean isHidden() {
		return this.hidden;
	}

	public boolean isHidedlg() {
		return this.hidedlg;
	}

	public boolean isIskey() {
		return this.iskey;
	}

	public boolean isResizable() {
		return this.resizable;
	}

	public boolean isSearch() {
		return this.search;
	}

	public boolean isSortable() {
		return this.sortable;
	}

	public boolean isTitle() {
		return this.title;
	}

	public boolean isViewable() {
		return this.viewable;
	}

	public void setAlign(final String align) {
		this.align = align;
	}

	public void setBeanClassName(final String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public void setBeanName(final String beanName) {
		this.beanName = beanName;
	}

	public void setCellattr(final String cellattr) {
		this.cellattr = cellattr;
	}

	public void setClasses(final String classes) {
		this.classes = classes;
	}

	public void setColModel(final String colModel) {
		this.colModel = colModel;
	}

	public void setColModelIndex(final int colModelIndex) {
		this.colModelIndex = colModelIndex;
	}

	public void setColNames(final String colNames) {
		this.colNames = colNames;
	}

	public void setDatefmt(final String datefmt) {
		this.datefmt = datefmt;
	}

	public void setDefval(final String defval) {
		this.defval = defval;
	}

	public void setEditable(final boolean editable) {
		this.editable = editable;
	}

	public void setEditoptions(final String editoptions) {

		this.editoptions = formate(editoptions);
	}

	public void setEditrules(final String editrules) {
		this.editrules = formate(editrules);
	}

	public void setEdittype(final String edittype) {
		this.edittype = edittype;
	}

	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

	public void setFirstsortorder(final String firstsortorder) {
		this.firstsortorder = firstsortorder;
	}

	public void setFixed(final boolean fixed) {
		this.fixed = fixed;
	}

	public void setFormatoptions(final String formatoptions) {
		this.formatoptions = formatoptions;
	}

	public void setFormatter(final String formatter) {
		this.formatter = formatter;
	}

	public void setFormoptions(final String formoptions) {
		this.formoptions = formate(formoptions);
	}

	public void setHidden(final boolean hidden) {
		this.hidden = hidden;
	}

	public void setHidedlg(final boolean hidedlg) {
		this.hidedlg = hidedlg;
	}

	public void setIdx(final String index) {
		this.idx = index;
	}

	public void setIskey(final boolean key) {
		this.iskey = key;
	}

	public void setJsonmap(final String jsonmap) {
		this.jsonmap = jsonmap;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	public void setResizable(final boolean resizable) {
		this.resizable = resizable;
	}

	public void setSearch(final boolean search) {
		this.search = search;
	}

	public void setSearchoptions(final String searchoptions) {

		this.searchoptions = formate(searchoptions);
	}

	public void setSortable(final boolean sortable) {
		this.sortable = sortable;
	}

	public void setSorttype(final String sorttype) {
		this.sorttype = sorttype;
	}

	public void setStype(final String stype) {
		this.stype = stype;
	}

	public void setSurl(final String surl) {
		this.surl = surl;
	}

	public void setTemplate(final String template) {
		this.template = template;
	}

	public void setTitle(final boolean title) {
		this.title = title;
	}

	public void setUnformat(final String unformat) {
		this.unformat = unformat;
	}

	public void setViewable(final boolean viewable) {
		this.viewable = viewable;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public void setXmlmap(final String xmlmap) {
		this.xmlmap = xmlmap;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		if (this.name != null && this.name.length() > 0) {
			builder.append("name:'");
			builder.append(this.name);
			builder.append("',");
		}
		if (this.align != null && this.align.length() > 0) {
			builder.append("align:'");
			builder.append(this.align);
			builder.append("',");
		}
		if (this.cellattr != null && this.cellattr.length() > 0) {
			builder.append("cellattr:'");
			builder.append(this.cellattr);
			builder.append("',");
		}
		if (this.classes != null && this.classes.length() > 0) {
			builder.append("classes:'");
			builder.append(this.classes);
			builder.append("',");
		}
		if (this.datefmt != null && this.datefmt.length() > 0) {
			builder.append("datefmt:'");
			builder.append(this.datefmt);
			builder.append("',");
		}
		if (this.defval != null && this.defval.length() > 0) {
			builder.append("defval:'");
			builder.append(this.defval);
			builder.append("',");
		}
		if (this.editable) {
			builder.append("editable:");
			builder.append(this.editable);
			builder.append(",");
		}
		if (this.editoptions != null && this.editoptions.length() > 0) {
			builder.append("editoptions:");
			builder.append(this.editoptions);
			builder.append(",");
		}
		if (this.editrules != null && this.editrules.length() > 0) {
			builder.append("editrules:");
			builder.append(this.editrules);
			builder.append(",");
		}
		if (this.edittype != null && this.edittype.length() > 0) {
			builder.append("edittype:'");
			builder.append(this.edittype);
			builder.append("',");
		}
		if (this.firstsortorder != null && this.firstsortorder.length() > 0) {
			builder.append("firstsortorder:'");
			builder.append(this.firstsortorder);
			builder.append("',");
		}
		if (this.fixed) {
			builder.append("fixed:'");
			builder.append(this.fixed);
			builder.append("',");
		}
		if (this.formatoptions != null && this.formatoptions.length() > 0) {
			builder.append("formatoptions:");
			builder.append(this.formatoptions);
			builder.append(",");
		}
		if (this.formatter != null && this.formatter.length() > 0) {
			builder.append("formatter:'");
			builder.append(this.formatter);
			builder.append("',");
		}
		if (this.formoptions != null && this.formoptions.length() > 0) {
			builder.append("formoptions:");
			builder.append(this.formoptions);
			builder.append(",");
		}
		if (this.hidden) {
			builder.append("hidden:");
			builder.append(this.hidden);
			builder.append(",");
		}
		if (this.hidedlg) {
			builder.append("hidedlg:");
			builder.append(this.hidedlg);
			builder.append(",");
		}
		if (this.idx != null && this.idx.length() > 0) {
			builder.append("index:'");
			builder.append(this.idx);
			builder.append("',");
		}
		if (this.jsonmap != null && this.jsonmap.length() > 0) {
			builder.append("jsonmap:'");
			builder.append(this.jsonmap);
			builder.append("',");
		}
		if (this.iskey) {
			builder.append("key:");
			builder.append(this.iskey);
			builder.append(",");
		}
		if (this.label != null && this.label.length() > 0) {
			builder.append("label:'");
			builder.append(this.label);
			builder.append("',");
		}

		if (this.resizable) {
			builder.append("resizable:");
			builder.append(this.resizable);
			builder.append(",");
		}
		if (this.search) {
			builder.append("search:");
			builder.append(this.search);
			builder.append(",");
			builder.append("searchoptions:");
			builder.append(this.searchoptions);
			builder.append(",");
		}
		if (this.sortable) {
			builder.append("sortable:");
			builder.append(this.sortable);
			builder.append(",");
		}
		if (this.sorttype != null && this.sorttype.length() > 0) {
			builder.append("sorttype:");
			builder.append(this.sorttype);
			builder.append(",");
		}
		if (this.stype != null && this.stype.length() > 0) {
			builder.append("stype:'");
			builder.append(this.stype);
			builder.append("',");
		}
		if (this.surl != null && this.surl.length() > 0) {
			builder.append("surl:'");
			builder.append(this.surl);
			builder.append("',");
		}
		if (this.template != null && this.template.length() > 0) {
			builder.append("template:");
			builder.append(this.template);
			builder.append(",");
		}
		if (this.title) {
			builder.append("title:'");
			builder.append(this.title);
			builder.append("',");
		}
		if (this.unformat != null && this.unformat.length() > 0) {
			builder.append("unformat:");
			builder.append(this.unformat);
			builder.append(",");
		}

		builder.append("width:");
		builder.append(this.width);
		builder.append(",");
		if (this.xmlmap != null && this.xmlmap.length() > 0) {
			builder.append("xmlmap:'");
			builder.append(this.xmlmap);
			builder.append("',");
		}

		// 以这个没有逗号的结尾，避免删除操作

		builder.append("viewable:");
		builder.append(this.viewable);

		builder.append("}");
		return builder.toString();
	}

	public String toString1() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		if (this.name != null && this.name.length() > 0) {
			builder.append("name:'");
			builder.append(this.name);
			builder.append("',");
		}
		if (this.align != null && this.align.length() > 0) {
			builder.append("align:'");
			builder.append(this.align);
			builder.append("',");
		}
		if (this.cellattr != null && this.cellattr.length() > 0) {
			builder.append("cellattr:'");
			builder.append(this.cellattr);
			builder.append("',");
		}
		if (this.classes != null && this.classes.length() > 0) {
			builder.append("classes:'");
			builder.append(this.classes);
			builder.append("',");
		}
		if (this.datefmt != null && this.datefmt.length() > 0) {
			builder.append("datefmt:'");
			builder.append(this.datefmt);
			builder.append("',");
		}
		if (this.defval != null && this.defval.length() > 0) {
			builder.append("defval:'");
			builder.append(this.defval);
			builder.append("',");
		}
		if (this.editable) {
			builder.append("editable:");
			builder.append(this.editable);
			builder.append(",");
		}
		if (this.editoptions != null && this.editoptions.length() > 0) {
			builder.append("editoptions:");
			builder.append(this.editoptions);
			builder.append(",");
		}
		if (this.editrules != null && this.editrules.length() > 0) {
			builder.append("editrules:");
			builder.append(this.editrules);
			builder.append(",");
		}
		if (this.edittype != null && this.edittype.length() > 0) {
			builder.append("edittype:'");
			builder.append(this.edittype);
			builder.append("',");
		}
		if (this.firstsortorder != null && this.firstsortorder.length() > 0) {
			builder.append("firstsortorder:'");
			builder.append(this.firstsortorder);
			builder.append("',");
		}
		if (this.fixed) {
			builder.append("fixed:'");
			builder.append(this.fixed);
			builder.append("',");
		}
		if (this.formatoptions != null && this.formatoptions.length() > 0) {
			builder.append("formatoptions:");
			builder.append(this.formatoptions);
			builder.append(",");
		}
		if (this.formatter != null && this.formatter.length() > 0) {
			builder.append("formatter:'");
			builder.append(this.formatter);
			builder.append("',");
		}
		if (this.formoptions != null && this.formoptions.length() > 0) {
			builder.append("formoptions:");
			builder.append(this.formoptions);
			builder.append(",");
		}
		if (this.hidden) {
			builder.append("hidden:");
			builder.append(this.hidden);
			builder.append(",");
		}
		if (this.hidedlg) {
			builder.append("hidedlg:");
			builder.append(this.hidedlg);
			builder.append(",");
		}
		if (this.idx != null && this.idx.length() > 0) {
			builder.append("index:'");
			builder.append(this.idx);
			builder.append("',");
		}
		if (this.jsonmap != null && this.jsonmap.length() > 0) {
			builder.append("jsonmap:'");
			builder.append(this.jsonmap);
			builder.append("',");
		}
		if (this.iskey) {
			builder.append("key:");
			builder.append(this.iskey);
			builder.append(",");
		}
		if (this.label != null && this.label.length() > 0) {
			builder.append("label:'");
			builder.append(this.label);
			builder.append("',");
		}

		if (this.resizable) {
			builder.append("resizable:");
			builder.append(this.resizable);
			builder.append(",");
		}
		if (this.search) {
			builder.append("search:");
			builder.append(this.search);
			builder.append(",");
			builder.append("searchoptions:");
			builder.append(this.searchoptions);
			builder.append(",");
		}
		if (this.sortable) {
			builder.append("sortable:");
			builder.append(this.sortable);
			builder.append(",");
		}
		if (this.sorttype != null && this.sorttype.length() > 0) {
			builder.append("sorttype:");
			builder.append(this.sorttype);
			builder.append(",");
		}
		if (this.stype != null && this.stype.length() > 0) {
			builder.append("stype:'");
			builder.append(this.stype);
			builder.append("',");
		}
		if (this.surl != null && this.surl.length() > 0) {
			builder.append("surl:'");
			builder.append(this.surl);
			builder.append("',");
		}
		if (this.template != null && this.template.length() > 0) {
			builder.append("template:");
			builder.append(this.template);
			builder.append(",");
		}
		if (this.title) {
			builder.append("title:'");
			builder.append(this.title);
			builder.append("',");
		}
		if (this.unformat != null && this.unformat.length() > 0) {
			builder.append("unformat:");
			builder.append(this.unformat);
			builder.append(",");
		}

		builder.append("width:");
		builder.append(this.width);
		builder.append(",");
		if (this.xmlmap != null && this.xmlmap.length() > 0) {
			builder.append("xmlmap:'");
			builder.append(this.xmlmap);
			builder.append("',");
		}

		// 以这个没有逗号的结尾，避免删除操作

		builder.append("viewable:");
		builder.append(this.viewable);

		builder.append("}");
		return builder.toString();
	}

	private String formate(String quoteJsonString) {
		if (null == quoteJsonString) {
			return null;
		}
		if (quoteJsonString.startsWith("\"")) {
			quoteJsonString = quoteJsonString.substring(1);
		}
		if (quoteJsonString.endsWith("\"")) {
			quoteJsonString = quoteJsonString.substring(0, quoteJsonString
					.length() - 1);
		}
		return quoteJsonString;
	}

}
