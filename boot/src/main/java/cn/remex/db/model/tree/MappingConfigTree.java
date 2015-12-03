package cn.remex.db.model.tree;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.OneToMany;
import java.util.List;

public class MappingConfigTree extends ModelableImpl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3304085829518117179L;
	
	
	private MappingConfigTree parent;
	
	@OneToMany(mappedBy="parent")
	private List<MappingConfigTree> children;

	public MappingConfigTree getParent() {
		return parent;
	}

	public void setParent(MappingConfigTree parent) {
		this.parent = parent;
	}

	public List<MappingConfigTree> getChildren() {
		return children;
	}

	public void setChildren(List<MappingConfigTree> children) {
		this.children = children;
	}
	
	
}
