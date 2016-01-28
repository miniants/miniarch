package cn.remex.db.rsql.sqlutil;

import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.*;
import cn.remex.db.DbRvo;
import cn.remex.db.sql.FieldType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static cn.remex.db.sql.FieldType.TBase;
import static cn.remex.db.sql.FieldType.TCollection;
import static cn.remex.db.sql.FieldType.TObject;

/**
 *
 * Created by yangy on 2016/1/16 0016.
 */
public class Node<T> {

    /**
     * 字符串添加属性列.字符串表达式支持链式访问包括 . [] :
     */
    public static final String regx = "([A-Za-z_][A-Za-z\\d_]*)$|(([A-Za-z_][A-Za-z\\d_]*)\\.)|(([A-Za-z_][A-Za-z\\d_]*)\\[\\]\\.)";

    private Node<?> rootNode;
    private Node<?> supNode;
    private FieldType type;
    private Class<T> nodeModel;
    private Class nodeType;
    private String nodeName;
    private String nodeExpr;
    private List<Node<?>> subNodes;
    private Map<String,Node<?>> paths;

    //数据层面
    private Map<String,T> listItemMap;
    private List<T> list;
    private T nodeBean;
    private Map<String,?> rowData;
    private DbRvo dbRvo;
    private int dbRvoIndex;
    private Object dbRvoInstanceFactory;
    private Method dbRvoInstanceMethod;

    /**
     * 用于创建根节点的。
     * @param clazz
     */
    public Node(Class<T> clazz) {
        this.type = TObject;
        this.rootNode = this;
        this.nodeModel = clazz;
        this.paths = new HashMap<>();
    }

    /**
     * 用于创建子节点的
     * @param supNode
     * @param nodeName
     * @param <PT>
     */
    public <PT> Node(Node<PT> supNode, FieldType type, String nodeName) {
        this.type = type;
        this.rootNode = supNode.rootNode;
        this.supNode = supNode;
        this.nodeName = nodeName;
        if(TCollection.equals(type)) {
            this.nodeModel = (Class<T>) ReflectUtil.getListActualType(ReflectUtil.getGetter(supNode.getNodeModel(), nodeName).getGenericReturnType());
        }else {
            this.nodeModel = (Class<T>) ReflectUtil.getGetter(supNode.getNodeModel(), nodeName).getGenericReturnType();
        }
    }

    public <ST> Node<ST> addSubNode(FieldType type, String baseField, String nodeExpr) {
        Assert.isTrue(this.type.equals(FieldType.TCollection) || this.type.equals(FieldType.TObject), "只有Model节点或Collection节点能添加子节点！");
        Node<ST> subNode = new Node<>(this, type, baseField);
        if (subNodes == null) subNodes = new ArrayList<>();
        subNode.nodeExpr = nodeExpr;
        subNodes.add(subNode);
        return subNode;
    }

    public void addIndex(String expr) {
        Param<Node> param = new Param(this);
        StringHelper.forEachMatch(expr, regx, (m, end) -> {
            String baseField = m.group(1), objField = m.group(3), listField = m.group(5);
            if (baseField != null) {
                Assert.isTrue(end, "索引到此处必须结束，结束标识为baseField");
                paths.put(expr, param.param.addSubNode(TBase, baseField, baseField)); // 建立快速索引的路径
            } else if (objField != null) {
                Assert.isTrue(!end, "索引不能以未完成的Object引用结束");
                param.param = param.param.addSubNode(TObject, objField, m.group(2));
            } else if (listField != null) {
                Assert.isTrue(!end, "索引不能以未完成的List引用结束");
                param.param = param.param.addSubNode(TCollection, listField, m.group(4));
            }
        });
    }

    public Node<T> start(T nodeBean,Map<String,Object> rowData) {
        this.nodeBean = nodeBean;
        this.rowData = rowData;
        return this;
    }
    public Node<T> start(T nodeBean,DbRvo dbRvo,int rowIndex, Object instanceFactory, Method instanceMethod) {
        this.nodeBean = nodeBean;
        this.dbRvo = dbRvo;
        this.dbRvoIndex = rowIndex;
        this.dbRvoInstanceFactory = instanceFactory;
        this.dbRvoInstanceMethod = instanceMethod;
        return this;
    }

    public void evalEnd() {
        forEvery(n -> {
            n.nodeBean = null;
            n.rowData = null;
            n.dbRvo = null;
            n.dbRvoIndex = 0;
            n.list = null;
            n.listItemMap = null;
        });
    }

    public Node<T> eval(String expr, Object value) {
        Node targetNode = paths.get(expr);
        Object curBean = targetNode.supNode.obtainBean();

        //这里这一步一定是基本类型，复杂类型再obtainBean已经完成
        ReflectUtil.invokeSetter(targetNode.nodeName, curBean,
                ReflectUtil.caseObject(targetNode.nodeModel, value/*, instanceFactory, instanceMethod, fieldClass*/));
        return this;
    }

    private StringBuilder obtainPath() {
        if (Judgment.nullOrBlank(nodeExpr)) {
            return new StringBuilder();
        }else {
            return supNode.obtainPath().append(nodeExpr);
        }
    }

    private T obtainBean() {
        if (null == nodeBean) {
            if (TCollection.equals(type)) {
                if (listItemMap == null) {
                    Object bean = ReflectUtil.invokeGetter(nodeName, supNode.obtainBean());
                    if (null == bean) {
                        bean = new ArrayList<>();
                        ReflectUtil.invokeSetter(nodeName, supNode.nodeBean, bean);
                    }
                    listItemMap = CollectionUtils.listToMap((List) bean, "id");
                    list = (List<T>) bean;
                }

                Object id = (rootNode.dbRvo != null) ?
                        rootNode.dbRvo.getCell(rootNode.dbRvoIndex, obtainPath().append("id").toString())
                        : rootNode.rowData.get(obtainPath().append("id").toString());
                Assert.notNull(id, "检索匹配的对象的id为空，无法赋值！");
                nodeBean = listItemMap.get(id);

                if (null == nodeBean) {
                    nodeBean = rootNode.dbRvoInstanceFactory==null?ReflectUtil.invokeNewInstance(nodeModel):
                            (T) ReflectUtil.invokeMethod(rootNode.dbRvoInstanceMethod,rootNode.dbRvoInstanceFactory,nodeModel);
                    ReflectUtil.invokeSetter("id", nodeBean, String.valueOf(id));
                    (list = null == list ? (List<T>) ReflectUtil.invokeGetter(nodeName, supNode.nodeBean) : list).add(nodeBean);
                }

            } else if (TObject.equals(type)) {
                Object bean = ReflectUtil.invokeGetter(nodeName, supNode.obtainBean());
                nodeBean = (T) bean;
                if (null == nodeBean) {
                    nodeBean = dbRvoInstanceFactory==null?ReflectUtil.invokeNewInstance(nodeModel):
                            (T) ReflectUtil.invokeMethod(dbRvoInstanceMethod,dbRvoInstanceFactory,nodeModel);
                    ReflectUtil.invokeSetter(nodeName,supNode.nodeBean,nodeBean);
                }
            }
        }

        return nodeBean;
    }

    /**
     * 遍历每个节点，含子节点
     * @param c
     * @return
     */
    public Node<?> forEvery(Consumer<Node<?>> c) {
        c.accept(this);
        if(null!=subNodes)subNodes.forEach(cc->cc.forEvery(c));
        return this;
    }


    //getter and setter


    public Map<String, T> getListItemMap() {
        return listItemMap;
    }

    public void setListItemMap(Map<String, T> listItemMap) {
        this.listItemMap = listItemMap;
    }

    public List<Node<?>> getSubNodes() {
        return subNodes;
    }

    public void setSubNodes(List<Node<?>> subNodes) {
        this.subNodes = subNodes;
    }

    public Class<T> getNodeModel() {
        return nodeModel;
    }

    public void setNodeModel(Class<T> nodeModel) {
        this.nodeModel = nodeModel;
    }

    public Class getNodeType() {
        return nodeType;
    }

    public void setNodeType(Class nodeType) {
        this.nodeType = nodeType;
    }

    public Node<?> getSupNode() {
        return supNode;
    }

    public void setSupNode(Node<?> supNode) {
        this.supNode = supNode;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public Map<String, Node<?>> getPaths() {
        return paths;
    }

    public void setPaths(Map<String, Node<?>> paths) {
        this.paths = paths;
    }

}
