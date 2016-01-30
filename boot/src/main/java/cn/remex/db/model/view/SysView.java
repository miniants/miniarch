package cn.remex.db.model.view;

import cn.remex.db.rsql.model.ModelableImpl;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by LIU on 16/1/5.
 */
public class SysView extends ModelableImpl {
    private String viewName;
    private String beanName;
    @OneToMany(targetEntity = SysElement.class,cascade = CascadeType.PERSIST,mappedBy = "view")
    private List<SysElement> elements;

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public List<SysElement> getElements() {
        return elements;
    }

    public void setElements(List<SysElement> elements) {
        this.elements = elements;
    }
}
