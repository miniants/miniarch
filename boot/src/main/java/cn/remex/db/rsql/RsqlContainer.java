package cn.remex.db.rsql;

import cn.remex.core.CoreSvo;
import cn.remex.core.RemexApplication;
import cn.remex.core.RemexContext;
import cn.remex.core.aop.AOPCaller;
import cn.remex.core.aop.AOPFactory;
import cn.remex.core.exception.ServiceCode;
import cn.remex.core.reflect.ReflectUtil;
import cn.remex.core.util.Assert;
import cn.remex.core.util.DateHelper;
import cn.remex.core.util.Judgment;
import cn.remex.core.util.Param;
import cn.remex.core.util.StringHelper;
import cn.remex.db.Container;
import cn.remex.db.DbCvo;
import cn.remex.db.DbRvo;
import cn.remex.db.exception.RsqlException;
import cn.remex.db.exception.RsqlExecuteException;
import cn.remex.db.lambdaapi.ColumnPredicate;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.rsql.connection.RDBManager;
import cn.remex.db.rsql.model.Modelable;
import cn.remex.db.rsql.transactional.RsqlTransaction;
import cn.remex.db.sql.FieldType;
import cn.remex.db.sql.SqlType;
import cn.remex.db.sql.WhereRuleOper;
import net.sf.cglib.proxy.MethodProxy;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本类对外交互的参数只有两个cvo、rvo。 其中cvo封装了读取json的基本参数，并且 在cvo中的parameter变量中必须含有
 * sqlCvoName变量的索引
 */
public class RsqlContainer implements Container, RsqlConstants {
    @SuppressWarnings("rawtypes")
    private DbCvo innerDbCvo;
    private String spaceName = RDBManager.DEFAULT_SPACE;

    /**
     * spring 的bean工厂<br>
     *
     */
    private static AOPFactory DBBeanFactory = new AOPFactory(new AOPCaller(null) {
        private static final long serialVersionUID = 865574373191977031L;
        @SuppressWarnings("unchecked")
        @Override
        public Object intercept(final Object obj, final Method method, final Object[] args, final MethodProxy proxy)
                throws Throwable {
            // 查找
            // getIaop().beforeMethod();
            Object result = proxy.invokeSuper(obj, args); // 方法在下面
            String methodName = method.getName();
            Class<?> returnType = method.getReturnType();
//            boolean isGetMethod = args.length == 0 && (methodName.startsWith("get") || (methodName.startsWith("is") && (boolean.class.equals(returnType) || Boolean.class.equals(returnType))) );
            boolean isSetMethod = args.length == 1 && methodName.startsWith("set");
            @SuppressWarnings("unused")
            boolean isRetCollection=false,isRetObject=false;
//            boolean fetchAndStoreAutoFlag;
            /*if (false//关闭延时加载功能 @TODO
                    &&isGetMethod && (fetchAndStoreAutoFlag=(CoreSvo.valLocal(FecthObjectFlied)==null?true:(Boolean)CoreSvo.valLocal(FecthObjectFlied)) )){
                if (SYS_Method_getDataStatus.equals(methodName) || SYS_Method_getId.equals(methodName)){
                    // 此部分是无需任何处理
                } else if (null == result
                        && (isRetCollection=Collection.class.isAssignableFrom(returnType))
                        && !Judgment.nullOrBlank(((Modelable) obj).getId())
                        ) {
                    Class<?> beanClass = Class.forName(StringHelper.getClassName(obj.getClass()));
                    String getterName = method.getName().substring(3);
                    String fieldName = StringHelper.lowerFirstLetter(getterName);
                    Type fieldType = SqlType.getFields(beanClass, FieldType.TCollection).get(fieldName);

                    DbRvo sqlDbRvo = RsqlUtils.queryCollectionBeans(spaceName, beanClass, fieldName,
                            ReflectUtil.invokeMethod(beanClass.getMethod(SYS_Method_getId), obj));

//						result = sqlDbRvo.obtainObjects(ReflectUtil.getListActualType(fieldType));LHY 20141105 返回的集合对象不是代理对象的bug
                    result = sqlDbRvo.obtainBeans((Class<? extends Modelable>) ReflectUtil.getListActualType(fieldType));
                    // ReflectUtil.invokeMethod(beanClass.getMethod("set"+getterName,
                    // List.class), obj, result);
                    ReflectUtil.invokeSetter(fieldName, obj, result);
//LHY 2015-2-17 将自动获取对象的触发事件后置到obj的某个属性为空或为0时，dataStatus状态为DS_part+DS_beanNew时去数据库中查询，提供效率。这样通过oed查询的数据列进行操作时不会读取数据库，效率有明显的提高。
                } else if (fetchAndStoreAutoFlag && null != result && Modelable.class.isAssignableFrom(returnType)
                        && (DS_part+DS_beanNew).contains(((Modelable) result)._getDataStatus()) // 新建的bean 或者 是再数据部分读取数据的bean
                    //&& (null==((Modelable) result)._getModifyFileds() || ((Modelable) result)._getModifyFileds().contains()) 当时部分获取的时候值为空的时候，是不是每次都要读取一次数据库？
                        ) {

                    Object subId = ((Modelable) result).getId();
                    if (null != subId) {
                        String fieldName = StringHelper.lowerFirstLetter(methodName.substring(3));
                        Class<Modelable> bc = (Class<Modelable>) method.getReturnType();
                        result = ContainerFactory.getSession().queryBeanById(bc , subId); // 用数据中的bean替换掉
                        if(null!=result)
                            ReflectUtil.invokeSetter(fieldName, obj, result);
                    }
                }
//代码可用，但是否合适尚未考虑清楚，还用上面老的
//				} else if(fetchAndStoreAutoFlag && (null == result || result.equals(0)) && (!isRetCollection && !isRetObject)//基本数据列为null或等于0
//						&& (DS_part+DS_beanNew).contains(((Modelable) obj)._getDataStatus())
//						){ //普通base字段，没有指名读取的，且datastatus为part的，需要去数据库中读取。
//
//					Object id = ((Modelable) obj).getId();
//					if (null != id) {
//						Class<Modelable> beanClass = (Class<Modelable>) obj.getClass();
//						Modelable _obj = (Modelable) obj;
//						ContainerFactory.getSession().queryByFields(beanClass,null,SYS_id,id).assignRow(_obj ); // 用数据中的bean替换掉
//						if(null!=_obj){
//							_obj._setDataStatus(DS_managed);
//							result=ReflectUtil.invokeMethod(methodName, _obj);
//						}
//					}
//				}
//end for LHY 2015-2-17 将自动获取对象的触发事件后置到obj的某个属性为空或为0时，dataStatus状态为DS_part+DS_beanNew时去数据库中查询，提供效率。这样通过oed查询的数据列进行操作时不会读取数据库，效率有明显的提高。
            }else */if(isSetMethod
                    && (CoreSvo.valLocal(StoreObjectFlied)==null?true:(Boolean)CoreSvo.valLocal(StoreObjectFlied))) {
                if ((SYS_Method_setDataStatus+SYS_Method_setId).contains(methodName)){
                    //;
                }else{
                    Modelable m = (Modelable)obj;
                    m._setDataStatus(DS_needSave);
                    //将调用了set方法的属性保存起来，在store的时候使用，节约资源
                    if(!Collection.class.isAssignableFrom(returnType)
                            && !Modelable.class.isAssignableFrom(returnType))
                        m._addModifyFileds(StringHelper.lowerFirstLetter(methodName.substring(3)));
                }
            }
            // getIaop().afterMethod();
            return result;
        }
    });
    /**
     * 本线程是否自动获取object类型属性标示
     * false代表自动获取，true代表不自动获取，此处关键是设计给JSON序列化时避免无限循环的
     */
    private static final String FecthObjectFlied = "cn.remex.db.rsql.RsqlContainer.FecthObjectFlied";
    private static final String StoreObjectFlied = "cn.remex.db.rsql.RsqlContainer.StoreObjectFlied";
    @Override
    public boolean isLocalAutoFetchObjectFiled(){
        Object b = CoreSvo.valLocal(FecthObjectFlied);
        return (null==b?true:(Boolean)b);
    }
    @Override
    public boolean isLocalAutoStoreObjectFiled(){
        Object b = CoreSvo.valLocal(StoreObjectFlied);
        return (null==b?true:(Boolean)b);
    }
    /**
     * 本线程是否自动获取object类型属性标识
     * @param b true代表自动获取，false代表不自动获取，此处关键是设计给JSON序列化时避免无限循环的
     */
    @Override
    public void setLocalAutoFecthObjectFiled(boolean b){
        CoreSvo.putLocal(FecthObjectFlied, b);
    }
    /**
     * 本线程是否自动存储object类型属性标识
     * @param b true代表自动获取，false代表不自动获取，此处关键是设计给JSON序列化时避免无限循环的
     */
    @Override
    public void setLocalAutoStoreObjectFlied(boolean b){
        CoreSvo.putLocal(StoreObjectFlied, b);
    }
    /**
     * 直接获取非缓存dbbean
     * @return T
     */
    @Override
    public <T> T createDBBean(final Class<T> clazz) {
        RsqlAssert.isOrmClass(spaceName, clazz);
        T t;
        // t = (T) clazz.newInstance();
        t = DBBeanFactory.getBean(clazz);
        ((Modelable) t)._setAopModelBean();
        Modelable m = (Modelable) t;
        m._setDataStatus(DS_beanNew);
        return t;
    }


    //===lambda控制方式下数据库控制接口===============//
    @Override
    public <T extends Modelable> DbCvo<T> createDbCvo(Class<T> beanClass) {
        innerDbCvo = new DbCvo<>(spaceName, beanClass);
        innerDbCvo.setContainer(this);
        return (DbCvo<T>) innerDbCvo;
    }

    @Override
    public <T extends Modelable> DbCvo<T> createDbCvo(String sqlString, Map<String, Object> params) {
        innerDbCvo = new DbCvo(spaceName, obtainManualSql(sqlString), params);
        innerDbCvo.setContainer(this);
        return (DbCvo<T>) innerDbCvo;
    }

    @Override
    public DbRvo execute() {
        return null;
    }

    @Override
    public DbRvo update() {
        innerDbCvo._initForRsqlDao();
        return RsqlDao.getDefaultRemexDao().executeUpdate(innerDbCvo);
    }

    @Override
    public DbRvo query() {
        // LHY 2015-2-17
        // dbCvo._initForRsqlDao();
        // return RsqlDao.getDefaultRemexDao().executeQuery(dbCvo);
        return query(innerDbCvo);
    }

    @Override
    public <T> T queryBean() {
        List<T> beans = (List<T>) query(innerDbCvo).obtainBeans();
        return beans.size() > 0 ? beans.get(0) : null;
    }

    @Override
    public <T extends Modelable> T queryBeanById(final Class<T> clazz, final Object idObject) {
        Assert.isNull(innerDbCvo, ServiceCode.FAIL,  "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
        return createDbCvo(clazz).filterBy(Modelable::getId, WhereRuleOper.eq, idObject.toString()).ready().queryBean();
//        DbRvo dbRvo = queryById(clazz, idObject);
//        List<T> beans = dbRvo.obtainBeans();
//        if (beans.size() > 0)
//            return beans.get(0);
//        else
//            return null;
    }
    @Override
    public <T> List<T> queryBeans() {
        return (List<T>) query(innerDbCvo).obtainBeans();
    }

    @Override
    public <T extends Modelable> List queryObjectsByCollectionField(Class<T> clazz, ColumnPredicate<T> cp, Object foreignKey) {
        return queryByCollectionField(clazz, cp, foreignKey).obtainObjects();
    }

    @Override
    public <T extends Modelable> DbRvo queryByCollectionField(Class<T> clazz, ColumnPredicate<T> cp, Object foreignKey) {
        Param<String> fieldNameParam = new Param<>(null);
        ReflectUtil.eachFieldWhenGet(clazz, bean -> cp.init((T) bean), fieldName -> fieldNameParam.param = fieldName);
        return RsqlUtils.queryCollectionBeans(spaceName, clazz, fieldNameParam.param, foreignKey);
    }





    //===核心数据库控制接口===============//
    @Override
    @RsqlTransaction
    public <T extends Modelable> DbRvo delete(final T o) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) o.getClass();
        DbCvo<T> dbCvo;
        if (null == innerDbCvo)
            dbCvo = new DbCvo<>(spaceName, clazz, SqlOper.del);
        else
            dbCvo = innerDbCvo;
        dbCvo.setOper(SqlOper.del);
        o._setDataStatus(DataStatus.removed.toString());// ReflectUtil.invokeSetter(SYS_dataStatus,
        // o,
        // DataStatus.removed.toString());
        // LHY 2015-2-17
        dbCvo.setDataType(DT_whole);
        dbCvo._setSpaceName(spaceName);
        dbCvo.setId(o.getId());// dbCvo.setId((String)
        // ReflectUtil.invokeGetter(SYS_id, o)); LHY
        // 2015-2-17
        // dbCvo._initForRsqlDao(); LHY 2015-2-17 在store中已经处理
        // RsqlRvo dbRvo = RsqlDao.getDefaultRemexDao().executeUpdate(dbCvo);
        DbRvo dbRvo = store(o, dbCvo);
        return dbRvo;
    }

    @Override
    @RsqlTransaction
    // 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
    public <T extends Modelable> DbRvo store(final T obj) {
        DbCvo dbCvo;
        if (null == innerDbCvo)
            dbCvo = new DbCvo<>(spaceName, (Class<Modelable>) obj.getClass(), SqlOper.store, DT_whole);
        else {
            innerDbCvo.setOper(SqlOper.store);
            innerDbCvo.setDataType((DT_base + DT_object).equals(innerDbCvo.getDataType()) ? DT_whole : innerDbCvo.getDataType());
            dbCvo = innerDbCvo;
        }
        return store(obj, dbCvo);//如果修改了默认值，则使用当前的
    }

    @Override
    public <T extends Modelable> DbRvo query(final DbCvo<T> dbCvo) {
        dbCvo._initForRsqlDao();// LHY 2015-1-17 挪到此处更加恰当，Dao切面捕获异常时需要用到sqlbean，必须提前初始化。
        return RsqlDao.getDefaultRemexDao().executeQuery(dbCvo);
    }


    //===辅助方法，不能用于lambda控制方式，建议逐步使用lambda注入属性名及值得方式替代========//
    @Override
    @RsqlTransaction
    // 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
    public <T extends Modelable> DbRvo copy(DbCvo<T> cvo) {
        Assert.isNull(innerDbCvo, ServiceCode.FAIL,  "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
        DbRvo dbRvo = null;
        String beanName = cvo.getBeanName();
        cvo._setSpaceName(this.spaceName);
        @SuppressWarnings("unchecked")
        Class<T> beanClass = (Class<T>) RDBManager.getLocalSpaceConfig(spaceName).getOrmBeanClass(beanName);
        if (null == beanClass)
            throw new RsqlExecuteException(ServiceCode.RSQL_BEANCLASS_ERROR, "指定名为" + beanName + "的ormBean并不在数据库建模的包中，请联系程序管理员！");
        Object ids = cvo.$V(SYS_ids) == null ? cvo.$V(SYS_id) : cvo.$V(SYS_ids);
        if (!Judgment.nullOrBlank(ids)) {
            String[] uniqueFields = cvo.$V("uniqueFields") != null ? cvo.$V("uniqueFields").toString().split(";") : null;
            for (String id : ids.toString().split("[,;]")) {
                Modelable sourceBean = (Modelable) queryBeanById(beanClass, id);
                Modelable newBean = ReflectUtil.invokeNewInstance(beanClass);
                ReflectUtil.copyProperties(newBean, sourceBean);// 浅层复制，不用复制id和status
                if (null != uniqueFields)
                    for (String field : uniqueFields) {
                        if (!Judgment.nullOrBlank(field)) {
                            Method setter = ReflectUtil.getSetter(newBean.getClass(), field);
                            Assert.notNull(setter,  ServiceCode.FAIL, "没有如此属性:" + field);
                            ReflectUtil.invokeMethod(setter, newBean, ReflectUtil.caseObject(setter.getParameterTypes()[0], Math.random()));
                        }
                    }
                newBean.setId(null);// 清除原来的id
                newBean._setDataStatus(DS_beanNew);// 清除原来的状态
                dbRvo = store(newBean);
            }
        } else
            throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, "无有效的id，无法复制！");
        return dbRvo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Modelable> T queryBeanByJpk(Class<T> clazz, String resultFields, String jpkFields, Object... valuesandLastErrorMsg) {
        Assert.isNull(innerDbCvo, ServiceCode.FAIL,  "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
        if (null == valuesandLastErrorMsg || null == jpkFields || valuesandLastErrorMsg.length < 1)
            throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, "进行queryByFields操作室，参数constraintFields、values不能为空,valuesandLastErrorMsg长度不能小于1！");

        String[] fields = jpkFields.split(";");
        boolean b = fields.length == valuesandLastErrorMsg.length;
        String errorMsg = b ? "进行queryBeanByJpk时从数据库中查询到多条数据，此为数据库数据保存不合理导致逻辑上的主键约束存在异常！" : valuesandLastErrorMsg[valuesandLastErrorMsg.length - 1].toString();
        Object[] values = valuesandLastErrorMsg;
        if (!b) {
            values = new Object[valuesandLastErrorMsg.length - 1];
            System.arraycopy(valuesandLastErrorMsg, 0, values, 0, valuesandLastErrorMsg.length - 1);
        }
        DbRvo dbRvo = queryByFields(clazz, resultFields, jpkFields, values);
        if (dbRvo.getRecordCount() > 1) {
            throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, errorMsg);
        }

        return (T) (dbRvo.getRecordCount() == 1 ? dbRvo.obtainBeans().get(0) : null);
    }

    @Override
    public <T extends Modelable> DbRvo queryByFields(Class<T> clazz, String resultFields, String constraintFields, Object... values) {
        Assert.isNull(innerDbCvo, ServiceCode.FAIL,  "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
        DbCvo<T> dbCvo = new DbCvo<>(spaceName, clazz);
        if (null == values || null == constraintFields)
            throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, "进行queryByFields操作时，参数constraintFields、values不能为空！");
        String[] fields = constraintFields.split(";");
        if (fields.length != values.length)
            throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, "进行queryByFields操作时，参数constraintFields的属性个数、与values的个数不相等！");

        for (int i = 0; i < fields.length; i++) {
            Method getter = ReflectUtil.getGetter(clazz, fields[i]);
            if (null == getter)
                throw new RsqlExecuteException(ServiceCode.RSQL_BEANCLASS_ERROR, "数据库保存时(queryByFields)，所依据的fields在模型中不存在！");
            dbCvo.addRule(fields[i], WhereRuleOper.eq, String.valueOf(values[i]));
        }
        dbCvo.setDataColumns(resultFields);
        return query(dbCvo);
    }

//    @Override
//    public <T extends Modelable> DbRvo query(final Class<T> clazz) {
//        Assert.isNull(innerDbCvo, "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
//        DbCvo<T> dbCvo = new DbCvo<>(clazz);
//        // LHY 2015-2-17
//        // dbCvo._initForRsqlDao();
//        // return RsqlDao.getDefaultRemexDao().executeQuery(dbCvo);
//        return query(dbCvo);
//    }


//    @Override
//    public <T extends Modelable> DbRvo queryById(final Class<T> clazz, final Object idObject) {
//        Assert.isNull(innerDbCvo, "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
//        DbCvo<T> dbCvo = new DbCvo<T>(clazz);
//        dbCvo.setId(idObject.toString());
//        dbCvo.setDataType(DT_base + DT_object);
//        dbCvo.addRule(SYS_id, WhereRuleOper.eq, idObject.toString());
//        return this.query(dbCvo);
//    }

    @Override
    @RsqlTransaction
    // 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
    public <T extends Modelable> DbRvo storeByJpk(T bean, String saveFields, String jpkFields, String moreOneErrorMsg) {
        Assert.isNull(innerDbCvo, ServiceCode.FAIL, "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
        DbRvo dbRvo = updateByJpk(bean, saveFields, jpkFields, moreOneErrorMsg);
        if (dbRvo.getEffectRowCount() == 0) {// 如果没有更新，则次数新建。
            dbRvo = store(bean, saveFields);
            bean.setId(dbRvo.getId());
        } else {
            // donot need arrive here
        }
        return dbRvo;
    }

    @Override
    @RsqlTransaction
    public <T extends Modelable> DbRvo updateByJpk(T bean, String saveFields, String jpkFields, String moreOneErrorMsg) {
        Assert.isNull(innerDbCvo, ServiceCode.FAIL,  "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
        DbRvo dbRvo = updateByFields(bean, saveFields, jpkFields);
        if (dbRvo.getEffectRowCount() > 1) {
            throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, moreOneErrorMsg);
        }

        if (dbRvo.getEffectRowCount() == 1) {
            String[] jpkFieldsArr = jpkFields.split(";");
            Object[] values = new Object[jpkFieldsArr.length];
            for (int i = 0, s = jpkFieldsArr.length; i < s; i++) {
                values[i] = ReflectUtil.invokeGetter(jpkFieldsArr[i], bean);
            }
            // 因为是通过update where语句实现的，id无法返回，需要查询一次。
            Object id = queryByFields(bean.getClass(), SYS_id, jpkFields, values).getCell(0, SYS_id);
            bean.setId(id.toString());
        }

        return dbRvo;
    }

    @Override
    @RsqlTransaction
    // 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
    public <T extends Modelable> DbRvo updateByFields(T bean, String saveFields, String constraintFields) {
        Assert.isNull(innerDbCvo, ServiceCode.FAIL,  "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
        DbCvo<T> dbCvo = new DbCvo<>(spaceName, (Class<T>) bean.getClass());
        dbCvo.setDataColumns(saveFields);
        if (exists(bean, false)) {
            return store(bean, dbCvo);
        } else {
            for (String field : constraintFields.split(";")) {
                Method getter = ReflectUtil.getGetter(dbCvo.getBeanClass(), field);
                if (null == getter)
                    throw new RsqlExecuteException(ServiceCode.RSQL_BEANCLASS_ERROR, "数据库保存时(storeByField)，所依据的fields在模型中不存在！");
                dbCvo.addRule(field, WhereRuleOper.eq, String.valueOf(ReflectUtil.invokeMethod(getter, bean)));
            }
            dbCvo.setUpdateByWhere(true);

            DbRvo dbRvo = store(bean, dbCvo);
            //LHY update方法不进行新增操作。
        /*	if (dbRvo.getEffectRowCount() == 0)// 没有更新则保存。
				return store(bean);
			else*/
            return dbRvo;
        }
    }

    @Override
    @RsqlTransaction
    // 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
    public <T extends Modelable> DbRvo updateByFields(T bean, String constraintFields) {
        Assert.isNull(innerDbCvo,  ServiceCode.FAIL, "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
        return updateByFields(bean, null, constraintFields);
    }

    @Override
    // 保存基本列可以不加上事务，次数保险起见，加上。
    @RsqlTransaction
    // 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
    public <T extends Modelable> DbRvo storeBase(T bean) {
        Assert.isNull(innerDbCvo, ServiceCode.FAIL,  "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
        DbCvo<T> dbCvo = new DbCvo<>(spaceName, (Class<T>) bean.getClass());
        dbCvo.setDataType(DT_base);
        return store(bean, dbCvo);
    }

    @SuppressWarnings("unchecked")
    @Override
    @RsqlTransaction
    // 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
    public <T extends Modelable> DbRvo store(T bean, String fields) {
        Assert.isNull(innerDbCvo, ServiceCode.FAIL,  "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
        DbCvo<T> dbCvo = new DbCvo<>(spaceName, (Class<T>) bean.getClass());

        dbCvo.setDataColumns(fields);
        return store(bean, dbCvo);
    }

    @Override
    @RsqlTransaction
    public <T extends Modelable> DbRvo deleteById(Class<T> clazz, final String id) {
        Assert.isNull(innerDbCvo, ServiceCode.FAIL,  "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
        T bean = ReflectUtil.invokeNewInstance(clazz);
        bean.setId(id);
        return delete(bean);
    }

    @Override
    @RsqlTransaction
    public <T extends Modelable> DbRvo deleteByIds(Class<T> clazz, final String ids) {
        Assert.isNull(innerDbCvo, ServiceCode.FAIL,  "该方法禁止dbCvo方法，只能通过ContainerFactory.getSession()直接调用！", RsqlException.class);
        if (Judgment.nullOrBlank(ids)) {
            throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, "批量根据主键id删除对象时，ids参数不能为空!");
        }
        DbRvo dbRvo = null;
        int ec = 0;
        for (String id : ids.split("[,;]")) {
            dbRvo = deleteById(clazz, id);
            ec += dbRvo.getEffectRowCount();
        }
        ((RsqlRvo) dbRvo).setEffectRowCount(ec);
        return dbRvo;
    }


    //===核心的数据操作方法-直接到DAO层=====================//
    @Override
    @RsqlTransaction
    // 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
    public DbRvo execute(final String sql, final HashMap<String, Object> params) {
        Assert.isTrue(null == innerDbCvo, ServiceCode.FAIL,  "此处execute(final String sql, final HashMap<String, Object> params)不支持链式模式的数据库访问！", RsqlException.class);
        @SuppressWarnings({"unchecked", "rawtypes"})
        DbCvo<?> dbCvo = new DbCvo<>(spaceName, obtainManualSql(sql), params);
        dbCvo.setContainer(this);
        dbCvo._setSpaceName(spaceName);
        dbCvo.setRowCount(0); // TODO 现在是自动全查
        dbCvo._initForRsqlDao();// LHY 2015-1-17 挪到此处更加恰当，Dao切面捕获异常时需要用到sqlbean，必须提前初始化。
        return RsqlDao.getDefaultRemexDao().execute(dbCvo);
    }

    @Override
    public DbRvo executeQuery(final String sql, final Map<String, Object> params) {
        Assert.isTrue(null == innerDbCvo, ServiceCode.FAIL,  "此处executeQuery(final String sql, final HashMap<String, Object> params)不支持链式模式的数据库访问！", RsqlException.class);
        @SuppressWarnings({"unchecked", "rawtypes"})
        DbCvo<?> dbCvo = new DbCvo<>(spaceName, obtainManualSql(sql), params);
        dbCvo.setContainer(this);
        dbCvo._setSpaceName(spaceName);
        dbCvo.setRowCount(0); // TODO 现在是自动全查
        dbCvo._initForRsqlDao();// LHY 2015-1-17 挪到此处更加恰当，Dao切面捕获异常时需要用到sqlbean，必须提前初始化。
        if (null != params && null != params.get("RMX_beanClass"))
            dbCvo.setBeanClass((Class) params.get("RMX_beanClass"));
        return RsqlDao.getDefaultRemexDao().executeQuery(dbCvo);
    }

    @Override
    @RsqlTransaction
    // 此方法被外部调用时需要配置该声明。该声明在本模块内部调用时无法触发事务切面的。
    public RsqlRvo executeUpdate(final String sql, final Map<String, Object> params) {
        Assert.isTrue(null == innerDbCvo, ServiceCode.FAIL,  "此处executeUpdate(final String sql, final HashMap<String, Object> params)不支持链式模式的数据库访问！", RsqlException.class);
        @SuppressWarnings({"unchecked", "rawtypes"})
        DbCvo<?> dbCvo = new DbCvo<>(spaceName, obtainManualSql(sql), params);
        dbCvo.setContainer(this);
        dbCvo._setSpaceName(spaceName);
        dbCvo._initForRsqlDao();// LHY 2015-1-17 挪到此处更加恰当，Dao切面捕获异常时需要用到sqlbean，必须提前初始化。
        return RsqlDao.getDefaultRemexDao().executeUpdate(dbCvo);
    }

    @Override
    public DbRvo createCall(final String callSql) {
        return RsqlDao.getDefaultRemexDao().createCall(callSql, spaceName);
    }


    //====核心的方法 ==========================//

    /**
     * RsqlContainer C增加U更新D删除的核心方法。
     * 根据传入的对象和cvo中的相关参数存储数据
     *
     * @param obj 存储的对象
     * @param cvo
     * @return DbRvo
     * @rmx.call {@link RsqlContainer#delete(Modelable)}
     * @rmx.call {@link RsqlContainer#store(Modelable)}
     */
    @RsqlTransaction
    private <T extends Modelable> DbRvo store(final T obj, final DbCvo<T> cvo) {
        // 如果对象为空 则不保存，空对象不保存
        RsqlAssert.notNullBean(obj);
        RsqlAssert.isOrmBean(obj);

        DbRvo dbRvo;
        String beanName = StringHelper.getClassSimpleName(obj.getClass());
        Class<?> beanClass = RDBManager.getLocalSpaceConfig(spaceName).getOrmBeanClass(beanName);// 必须考虑的cgil加强,后面用这个原始beanClass替换了
        // 没有在配置文件中配置的bean不保存

        // 进入对象保存，
        // 为了避免无限递归必须检查数据状态，如需保存还需设置saving
        Object dataStatus = getDS(obj);
        if (DS_saving.equals(dataStatus))
            return null;
        else
            updateDS(obj, DS_saving);
        String obj_id = getPK(obj, false);

        /** =================保存base及外键object类型数据============ */
        String dt = String.valueOf(null != cvo && null != cvo.getDataType() ? cvo.getDataType() : DT_whole);
        // 初始化数据条件对象
        SqlOper oper = obtainOper(obj_id, dataStatus, cvo.getOper(), cvo); // del/add/edit
        String dcs = (null != cvo && Judgment.notEmpty(cvo.getDataColumns())) ? cvo.getDataColumns() : (obj._isAopModelBean() ? obj._getModifyFileds() : null);// 通过切面的监控优化
        @SuppressWarnings("unchecked")
        DbCvo<T> dbCvo = new DbCvo<>(spaceName, (Class<T>) obj.getClass(), oper, dt, dcs);// 需要生成本地所需的DBCVO
        dbCvo.setId(obj_id);
        if (!SqlOper.del.equals(oper)) {
            dbCvo.putParameters(maplizeObject(obj, oper, dcs)); // 更新、插入时才需要序列化基本数据列，id，datastatus列必须处理。
        }
        if (SqlOper.edit.equals(oper) && null != cvo.getFilter()) {
            dbCvo.setFilter(cvo.getFilter());
        }
        dbCvo.setBean(obj);
        // 外键对象数据
        Map<OneToOne,Modelable> map = null;
        if (dt.contains(DT_object))
            map = this.storeFKBean(beanClass, obj, dbCvo);// 保存外键对象并将更新的外键对象的Id存入DbCvo,如Person.department更新后将
        // partment.id更新至本obj的DbCvo为partment=id

        // 执行base及object's id 保存.
        dbCvo._initForRsqlDao();// LHY 2015-1-17 挪到此处更加恰当，Dao切面捕获异常时需要用到sqlbean，必须提前初始化。
        dbRvo = RsqlDao.getDefaultRemexDao().executeUpdate(dbCvo);

        // 更新one2one 关系 ，关系只能在保存完当前对象后 更新
        updateOne2One(obj,map);

        // 对象基本数据保存成功后必须更新其id
        if (dbRvo.getStatus() && null != dbRvo.getId()) {
            obj_id = dbRvo.getId();
            this.updatePK(obj, obj_id);
            this.updateDS(obj, !SqlOper.del.equals(oper) ? DS_managed : DS_removed);
        } else {
            this.updateDS(obj, dataStatus);
        }

        /** =================检查并保存collection类型数据============ */
        if (dt.contains(DT_collection))
            this.storeFKList(beanClass, obj, dbCvo, obj_id, beanName, oper);

        // 标记对象已经完成保存。
        // updateDS(obj, DS_managed); LHY 挪动至461
        return dbRvo;
    }


    //===内部控制的方法=========================//
    @Override
    public void setSpaceName(final String spaceName) {
        this.spaceName = spaceName;
    }

    @Override
    public <T extends Modelable> T createBean(Class<T> beanClass) {
        return (T) createDBBean(beanClass);
    }

    @Override
    public Class<?> obtainModelClass(String beanName) {
        return RDBManager.getLocalSpaceConfig(spaceName).getOrmBeanClass(beanName);
    }

    @Override
    public boolean existsModel(String beanName) {
        return null != RDBManager.getLocalSpaceConfig(spaceName).getOrmBeanClass(beanName);
    }

    @Override
    public <T extends Modelable> boolean exists(final T obj, final boolean forceCheckFromDB) {
        // ormBeans一定有getDataStatus方法，已经断言
        Object dataStatus = obj._getDataStatus();
        if (DS_saving.equals(dataStatus) || null != getPK(obj, forceCheckFromDB))
            return true;
        else
            return false;
    }

    @Override
    public String getSpaceName() {
        return spaceName;
    }

    /**
     * 保存当前对象的one2one对应外键对象属性为当前对象的id
     * @param obj
     * @param map
     * @param <T>
     */
    private <T> void updateOne2One(T obj, Map<OneToOne, Modelable> map) {
        map.forEach((oto, foreignBean) -> {
            if (oto != null) {
                String tgtFieldName = oto.mappedBy();
                Assert.isTrue(tgtFieldName != null,  ServiceCode.FAIL, "OneToOne的mappedBy对方属性的值不能为空！");
                if(!DS_saving.equals(foreignBean._getDataStatus())){
                    //当双方都维护oneToOne关系时，需要保存双方各自关系，导致循环保存，在此判断上一个bean的保存状态来退出循环
                    ReflectUtil.invokeSetter(tgtFieldName, foreignBean, obj);
                    this.store(foreignBean,tgtFieldName);
                }
            }
        });
    }

    /**
     * 此方法用于根据id确定是否需要保存。 如果为空表示需要保存。
     *
     * @param obj
     * @return boolean
     */
    private <T extends Modelable> boolean needStore(final T obj) {
        // ormBeans一定有getDataStatus方法，已经断言
        String id = getPK(obj, false);
        String ds = getDS(obj);
        if (null == id || DataStatus.needSave.equals(ds))
            return true;
        return false;
    }

    /**
     * 如果string以"SQL_"开头则从spring的配置文件Rsql_SQL中获取真实的SQL。
     *
     * @param str
     * @return
     */
    private String obtainManualSql(String str) {
        return str.startsWith("SQL_") ? ((Map<String, String>) RemexApplication.getBean("Rsql_SQL")).get(str) : str;
    }

    /**
     * 根据obj的id及dataStatus的属性确定操作类型
     *
     * @param <T>
     * @param dataStatus
     * @param sqlOper
     * @return String
     */
    private <T extends Modelable> SqlOper obtainOper(final String p_id, final Object dataStatus, final SqlOper sqlOper, final DbCvo<T> cvo) {
        boolean idForNew = Judgment.nullOrBlank(p_id) || "-1".equals(p_id); // ""，null，-1代表新生状态的id
        boolean isUpdateByWhere = cvo.isUpdateByWhere();
        if (idForNew && (!isUpdateByWhere) && (DS_beanNew.equals(dataStatus) || DS_needSave.equals(dataStatus) || SqlOper.store.equals(sqlOper)))
            return SqlOper.add;
        else if (!idForNew && (!isUpdateByWhere) && (DS_removed.equals(dataStatus) || SqlOper.del.equals(sqlOper)))
            return SqlOper.del;
        else if (isUpdateByWhere || (!idForNew && (DS_beanNew.equals(dataStatus) || DS_managed.equals(dataStatus) || DS_needSave.equals(dataStatus) || DS_part.equals(dataStatus) || SqlOper.edit.equals(sqlOper))))
            return SqlOper.edit;
        else
            throw new RsqlExecuteException(ServiceCode.RSQL_SQL_ERROR,"数据状态id及dataStatus错误！(add：id为空或者-1 && beanNew或者needSave)；del:id不能为空并且removed；edit:isUpdateByWhere=true/id不能为空 && managed或者needSave");
    }

    /**
     * 序列化对象为Map 类型为TBase
     *
     * @param o
     * @param oper
     * @param dcs
     * @return Map
     * @throws Exception
     */
    private Map<String, Object> maplizeObject(final Modelable o, SqlOper oper, String dcs) {
        Map<String, Object> mapFromObject = new HashMap<String, Object>();

        Class<?> clazz = o.getClass();

        Map<String, Method> baseGetters = SqlType.getGetters(clazz, FieldType.TBase);
        if (Judgment.nullOrBlank(dcs)) {// 通过dataColumns进行优化
            for (String fieldName : baseGetters.keySet()) {
                Method getter = baseGetters.get(fieldName);
                Object value = ReflectUtil.invokeMethod(getter, o);
                mapFromObject.put(fieldName, value);
            }
        } else {
            for (String fieldName : dcs.split(";")) {
                if (Judgment.nullOrBlank(fieldName))
                    continue;
                Method getter = baseGetters.get(fieldName);
                if (null != getter) {
                    Object value = ReflectUtil.invokeMethod(getter, o);
                    mapFromObject.put(fieldName, value);
                } else {
                    if (isDebug)
                        logger.debug("dataColumns配置值" + dcs + "包含非base字段");
                    continue;
                }
            }
        }
        mapFromObject.put(RsqlConstants.SYS_id, o.getId());
        mapFromObject.put(RsqlConstants.SYS_dataStatus, o._getDataStatus());

        // 只有在添加的时候修改者三个字段
        String now = DateHelper.getNow();
        AuthUser au = (AuthUser) RemexContext.getContext().getBean();
        String un = au == null || Judgment.nullOrBlank(au.getUsername()) ? "TEMP" : au.getUsername();
        if (oper == SqlOper.add) {
            mapFromObject.put(RsqlConstants.SYS_createTime, now);
            mapFromObject.put(RsqlConstants.SYS_createOperator, un);
            mapFromObject.put(RsqlConstants.SYS_createOperator_name, un);
            mapFromObject.put(RsqlConstants.SYS_ownership, un);
            mapFromObject.put(RsqlConstants.SYS_ownership_name, un);
        }
        // 编辑的字段和添加都需要添加这三个字段
        // if(oper == SqlOper.edit){
        // }
        mapFromObject.put(RsqlConstants.SYS_modifyTime, now);
        mapFromObject.put(RsqlConstants.SYS_modifyOperator, un);
        mapFromObject.put(RsqlConstants.SYS_modifyOperator_name, un);

        return mapFromObject;
    }

    /**
     * 保存数据库表中的外键数据，在java里面这个关系以为这一个外键的java bean，指FKBean
     *
     * @param beanClass
     * @param obj
     * @param cvo
     */
    private <T extends Modelable> Map storeFKBean(final Class<?> beanClass, final T obj, final DbCvo<T> cvo) {
        /***** 外键对象数据类型保存 ********************/
        Map<String, Method> objectGetters = SqlType.getGetters(beanClass, FieldType.TObject);
        String F_id = null;// 默认外键连接id为空
        Method getter;
        T value;
        String dcs = cvo.getDataColumns();
        boolean hasDcs = !Judgment.nullOrBlank(dcs);// dcs已经在Store(T,DbCvo)处理了。
        Map<OneToOne, Object> map = new HashMap<>(); // 保存o2o 的信息 供保存完当前对象后更新关系。
        if (hasDcs) {
            dcs = cvo.getDataColumns() + ";";
        }
        setLocalAutoFecthObjectFiled(false);
        for (String fieldName : objectGetters.keySet()) {
            if (hasDcs && obj._isAopModelBean() && dcs.indexOf(fieldName + ";") < 0) {
                continue;
            }
            // OneToOne otm = ReflectUtil.getAnnotation(beanClass, fieldName,
            // OneToOne.class);// 要么一对多，要么多对多

            getter = objectGetters.get(fieldName);

            // 获取外键对象
            value = (T) ReflectUtil.invokeMethod(getter, obj);

            // 如果数据库中不含有本对象则按照参数option规则递归保存外联对象
            if (null == value)
                continue;

            OneToOne oto = ReflectUtil.getAnnotation(beanClass, fieldName, OneToOne.class);// 一对一
            if (DataStatus.removed.equalsString(obj._getDataStatus())) {
                ManyToOne mto = ReflectUtil.getAnnotation(beanClass, fieldName, ManyToOne.class);// 多对一
                boolean hasCascade = (null != mto && null != mto.cascade()) || (null != oto && null != oto.cascade());
                List<CascadeType> cascade = hasCascade ? Arrays.asList(null != mto.cascade() ? mto.cascade() : oto.cascade()) : null;//ManyToOne的优先级高于OneToOne
                // 如果有级联删除的标志位，则删除
                if (hasCascade && cascade.contains(CascadeType.REMOVE)) {
                    delete(obj);
                }
            } else if (needStore(value)) {
                ManyToOne mto = ReflectUtil.getAnnotation(beanClass, fieldName, ManyToOne.class);// 多对一
                boolean hasCascade = (null != mto && null != mto.cascade()) || (null != oto && null != oto.cascade());
                List<CascadeType> cascade = hasCascade ? Arrays.asList(null != mto.cascade() ? mto.cascade() : oto.cascade()) : null;//ManyToOne的优先级高于OneToOne
                // 如果有级联保存的标志位，则保存
                if (hasCascade && cascade.contains(CascadeType.PERSIST)) {
                    if (oto != null) {// 保存o2o 的信息 供保存完当前对象后更新关系。
                        map.put(oto, value);
                    }
                    F_id = this.store(value).getId();
                }

                if (null == F_id) {
                    throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, "在非级联状态下，一对一中的外键对象没有保存！没有保存的属性为：" + fieldName);
                }
            } else { //更新关系即可
                F_id = this.getPK(value, false);
                if (oto != null) {// 保存o2o 的信息 供保存完当前对象后更新关系。
                    map.put(oto, value);
                }
            }
            // 外键对象处理完毕，无论保存与否都保存id，如果没有则id为null
            cvo.$S(fieldName, F_id);
        }// end for ObjectFields
        setLocalAutoFecthObjectFiled(true);
        return map;
    }

    /**
     * 保存数据库表中的外键list。在关系型数据库中对应为一个一对多的数据关系。
     *
     * @param beanClass
     * @param obj
     * @param obj_Id
     * @param beanName
     * @param oper
     * @rmx.summary 保存有两种方式<li>主表的id存于字表的字段中，从字表的方向看为外键对象</li> <li>
     * 新建一个关系表，专门用于保存一对多关系。在Rsql中一第一种方式为准。</li>
     */
    private <T extends Modelable> void storeFKList(final Class<?> beanClass, final T obj, final DbCvo<T> cvo, final Object obj_Id, final String beanName, SqlOper oper) {
        // 取得集合映射
        Map<String, Method> cGetters = SqlType.getGetters(beanClass, FieldType.TCollection);
        Map<String, Type> cFields = SqlType.getFields(beanClass, FieldType.TCollection);
        String dcs = cvo.getDataColumns();
        boolean hasDcs = !Judgment.nullOrBlank(dcs);// dcs已经在Store(T,DbCvo)处理了。
        if (hasDcs) {
            dcs = cvo.getDataColumns() + ";";
        }
        for (String fieldName : cFields.keySet()) {
            Type fieldType = cFields.get(fieldName);
            Class<?> subBeanClass = ReflectUtil.getListActualType(fieldType);
            // 读取当前bean中collection连接对象
            @SuppressWarnings("unchecked")
            Collection<Modelable> obj_fieldColl = (Collection<Modelable>) ReflectUtil.invokeMethod(cGetters.get(fieldName), obj);// 此行是调用的非代理方法，读取的时bean中的原始数据，没有去数据库读取。
            if (SqlOper.add.equals(oper) && (null == obj_fieldColl || 0 == obj_fieldColl.size()))
                continue;// 如果是父对象是新增，且列表为空，则不用进行下面的操作！ LHY 2014-11-1 优化
            if (SqlOper.edit.equals(oper) && hasDcs && dcs.indexOf(fieldName + ";") < 0 && obj._isAopModelBean()) {
                continue;// 编辑时，如果是代理对象，且dcs里没有则不用保存
            }
            boolean notDelOper = !(SqlOper.del.equals(oper) || DataStatus.removed.equalsString(((Modelable) obj)._getDataStatus())); // 不是删除操作的标志位，如果oper为删除或者数据的状态为删除都代表删除。
            if (notDelOper && null == obj_fieldColl)// 定义:在非删除情况下， Collection属性为null，即前台该属性为null或者undefined时，多对多关系不变。
                continue;
            // 读取数据库的collection数据id，
            @SuppressWarnings("unchecked")
            Map<String, Modelable> db_fieldMap = (Map<String, Modelable>) ((RsqlRvo) RsqlUtils.queryCollectionBeans(spaceName, beanClass, fieldName, obj_Id)).obtainObjectsMap(SYS_id, subBeanClass);
            // 如果当前bean的collection和数据的collection都为空则不用保存
            if ((null != obj_fieldColl && 0 == obj_fieldColl.size()) && (null == db_fieldMap || 0 == db_fieldMap.size())) // 前端该属性为0的数组，且数据库中的集合也为0时，不用进行多对多关系维护。
                continue;

            OneToMany otm = ReflectUtil.getAnnotation(beanClass, fieldName, OneToMany.class);// 要么一对多，要么多对多
            if (null != otm) {
                /*************** 一对多,多方用一个属性来保存这个关系的，只需更新多方外键即可 ********/

                String mappedBy = otm.mappedBy();
                boolean hasCascade = null != otm.cascade();
                List<CascadeType> cascade = hasCascade ? Arrays.asList(otm.cascade()) : null;
                if (null == mappedBy)
                    continue;

                // 不为空并且 父对象不是标示为删除 则逐一保存
                if (null != obj_fieldColl && notDelOper) { // if L2
                    boolean isPersist = hasCascade && cascade.contains(CascadeType.PERSIST);// 级联更新及保存
                    for (Modelable co : obj_fieldColl) {
                        // 一对多的多方为空则不用保存
                        if (null == co)
                            continue;

                        Object coField_id = ReflectUtil.invokeGetter(SYS_id, co);

                        if (db_fieldMap.containsKey(coField_id)) {// 数据库中已经存在
                            if (isPersist) {// 因为已经存在于数据库中，故为级联更新
                                ReflectUtil.invokeSetter(mappedBy, co, obj);// 检查并确保外键连接
                                store(co);
                            } else if (DataStatus.needSave.equalsString(co._getDataStatus())) {
                                throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, "在非级联状态下，一对多中的外键对象没有保存更新！");
                            } else { // 不是级联需要保存关系 2016-1-13 LHY
                                ReflectUtil.invokeSetter(mappedBy, co, obj);// 检查并确保外键连接
                                store(co, mappedBy);
                            }

                            db_fieldMap.remove(coField_id);
                        } else {// 数据库中不存在
                            if (isPersist) {// 级联保存
                                ReflectUtil.invokeSetter(mappedBy, co, obj);// 检查并确保外键连接
                                coField_id = store(co).getId();
                            } else { // 不是级联需要保存关系 2016-1-13 LHY
                                ReflectUtil.invokeSetter(mappedBy, co, obj);// 检查并确保外键连接
                                store(co, mappedBy);
                            }

                            if (null == coField_id) {
                                throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, "在非级联状态下，一对多中的外键对象没有保存！");
                            }
                        }

                        db_fieldMap.remove(coField_id);
                    }// end for collections
                }// if L2 end
                // 如果列表中还有对象，说明是前端删除的
                if (hasCascade && cascade.contains(CascadeType.REMOVE))// 删除数据
                    for (Modelable co : db_fieldMap.values())
                        delete(co);
                else
                    // 删除外键
                    for (Modelable co : db_fieldMap.values()) {
                        ReflectUtil.invokeSetter(mappedBy, co, null);
                        store(co);
                    }

            } else {
//                // 清空，让list自动获取 LHY 2016-5-8 取消get方法自动获取功能后，这行就没用了
//                ReflectUtil.invokeSetter(fieldName, obj, null);
                /******* 多对多 */
                ManyToMany mtm = ReflectUtil.getAnnotation(beanClass, fieldName, ManyToMany.class);// 要么一对多，要么多对多
                boolean hasCascade = null != mtm && null != mtm.cascade();
                List<CascadeType> cascade = hasCascade ? Arrays.asList(mtm.cascade()) : null;
                boolean meIsPrimaryTable = mtm == null || !"void".equals(mtm.targetEntity().toString());

                // 不为空并且 父对象不是标示为删除 则逐一保存
                if (null != obj_fieldColl && notDelOper) { // if
                    // 2
                    boolean isPersist = hasCascade && cascade.contains(CascadeType.PERSIST);// 级联更新及保存
                    for (Modelable co : obj_fieldColl) {
                        // 多对多的多方为空则不用保存
                        if (null == co)
                            continue;

                        Object coField_id = ReflectUtil.invokeGetter(SYS_id, co);

                        if (db_fieldMap.containsKey(coField_id)) {// 数据库中已经存在
                            if (isPersist) {// 因为已经存在于数据库中，故为级联更新
                                store(co);
                            } else if (DataStatus.needSave.equalsString(co._getDataStatus())) {
                                throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, "在非级联状态下，多对多中的外键对象没有保存更新！");
                            }

                            db_fieldMap.remove(coField_id);
                        } else {// 数据库中不存在
                            if (isPersist) {// 级联保存
                                coField_id = store(co).getId();
                            }

                            if (null != coField_id) {
                                RsqlUtils.doManyToMany_insert(spaceName, beanClass, beanName, fieldName, obj_Id, coField_id, meIsPrimaryTable);
                            } else {
                                throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, "在非级联状态下，多对多中的外键对象没有保存！");
                            }
                        }
                    }
                }// if 2 end
                // 如果列表中还有对象，说明是前端删除的
                if (hasCascade && cascade.contains(CascadeType.REMOVE))// 级联删除
                    for (Modelable co : db_fieldMap.values())
                        delete(co);
                else
                    // 默认删除外键关系
                    for (Object F_id : db_fieldMap.keySet())
                        RsqlUtils.doManyToMany_delete(cvo._getSpaceName(),beanClass, beanName, fieldName, obj_Id, F_id, meIsPrimaryTable);
            }
            // TODO
            // 由于struts调用的list对象的原始构造方法，采用ongl对list进行赋值。
            // 故list中的对象绕过了AOPFactory，不能列入数据库缓存池中，所以在此把他清除
            // 清除后，数据库的事务会发现list 为空，自动进行数据库查询，补充真实的bean进来
            // ReflectUtil.invokeSetter(fieldName, obj, null);
        }
    }

    /**
     * 更新obj的dataStatus，标明数据的状态
     *
     * @param obj
     */
    private void updateDS(final Object obj, final Object dataStatus) {
        Method setter = ReflectUtil.getMethod(obj.getClass(), SYS_Method_setDataStatus);
        if (null != setter)
            ReflectUtil.invokeMethod(setter, obj, ReflectUtil.caseObject(setter.getGenericParameterTypes()[0], dataStatus));
        else
            throw new RsqlExecuteException(ServiceCode.RSQL_BEANSTATUS_ERROR, obj.getClass().toGenericString()+"保存了一个没有dataStatus的Bean，数据库数据会出现错误！");
    }

    /**
     * 更新obj的pk，因为每个数据保存的bean都应该明确了ormBean关系。其必有id属性{@link Modelable}<br>
     * ,这个函数是通过reflect更新其id
     *
     * @param obj
     * @param P_Id
     */
    private void updatePK(final Object obj, final Object P_Id) {

        Method setter = ReflectUtil.getAllSetters(obj.getClass()).get(RsqlConstants.SYS_id);
        if (null != setter)
            ReflectUtil.invokeMethod(setter, obj, ReflectUtil.caseObject(setter.getGenericParameterTypes()[0], P_Id));
        else
            throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, obj.getClass().toString()+"保存了一个没有id的Bean，数据库数据会出现错误！");

    }

    /**
     * 判断数据状态，本数据库模型不支持没有状态的对象数据
     *
     * @param obj
     * @return Object
     * @rmx.call {@link RsqlContainer#needStore(Modelable)}
     * @rmx.call {@link RsqlContainer#store(Modelable, DbCvo)}
     */
    private <T extends Modelable> String getDS(final T obj) {
        // // 不为空则检查id来判断是否保存
        // // 判断是否有getId方法
        // Method getter =
        // ReflectUtil.getAllGetters(obj.getClass()).get(SYS_dataStatus);
        //
        // // 没有getDataStatus方法的对象不能通过本数据库模型保存,库中没有，直接返回null
        // if (null == getter)
        // return null;
        //
        // // 有getgetDataStatus方法则调研该方法
        // String DS = (String) ReflectUtil.invokeMethod(getter, obj);
        // 2015-2-17
        return obj._getDataStatus();
    }

    /**
     * 判断目前数据库中是否包含参数对象<br>
     *
     * @param obj 需要检索的对象
     * @return String 对象存在主键id的值，<br>
     * 如果需要检查其在数据中是否仅唯一存在主键，请指定{@link RsqlCore#checkPKFromDataBase}
     * 为true，否则将直接检查bean的id来验证 以下情况将返回null<br>
     * <li>obj == null <li>obj 没有getId() 和 setId() <li>obj 的属性id为空 <li>
     * obj 的属性id有值，但在数据库中不存在.此功能必须指定{@link RsqlCore#checkPKFromDataBase}
     * 为true，否则将直接检查bean的id来验证
     * @throws Exception <li>如果数据库查询有错误<br> <li>或者本对象所对应的类在数据中持久化的表中的数据有重复项将会跑出异常<br>
     *                   如果数据中同样id的数据存在多条则数据完整性及结构存在问题<br>
     *                   一旦出现问题说明有人在程序外修改了数据结构。<br>
     *                   <p>
     *                   调用本方法通过判断返回值是否为空来确定是否在数据库中存在该对象。
     * 的值，并通过query来检查是否在<br>
     * 本数据库中存在该对象。
     * @see RsqlCore#setCheckPKFromDatabase(boolean)
     */
    private <T extends Modelable> String getPK(final T obj, final boolean forceCheckFromDB) {
        String id;
        // 如果id为空则为未保存对象
        // id为空则表示是新对象，库中没有，返回fasle
        if (null == obj)
            return null;
        // 有getId方法则调研该方法
        Object idObj = obj.getId();// ReflectUtil.invokeGetter(SYS_id, obj); LHY
        // 2015-02-16
        // Object dataStatus = getDS(obj);

        id = (String) ReflectUtil.caseObject(String.class, idObj);
		/* 可以通过配置RDBConfignation.setCheckPKFULL来控制是否去数据库检查数据正确与否 */
        if ((forceCheckFromDB || RsqlCore.checkPKFromDataBase) && null != idObj) {

            // 如果id不为空，可能是数据库中的对象（标准状态id必须是从数据中获取的）
            // 通过id来检索该对象是否存在于本数据库
            DbRvo qr = createDbCvo(obj.getClass()).filterBy(Modelable::getId,WhereRuleOper.eq,idObj.toString()).ready().query();

            // 如果不在数据中
            if (qr.getRowCount() == 0)
                return null;
            else if (qr.getRecordCount() > 1)
                throw new RsqlExecuteException(ServiceCode.RSQL_DATA_ERROR, obj.getClass()+idObj.toString()+"主键ID重复！");
                // return null;// never arrived here
                // 如果在数据中唯一存在
            else
                // (qr.getRecordCount() == 1)数据库中有唯一项，则保存其id作为外键
                return (String) ReflectUtil.caseObject(String.class, qr.getCell(0, SYS_id));

        }
        return id;
    }

}
