/* 
* 文 件 名 : SysVariables.java
* CopyRright (c) since 2013: 
* 文件编号： 
* 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
* 日    期： 2013-6-23
* 修 改 人： 
* 日   期： 
* 描   述： 
* 版 本 号： 1.0
*/

package cn.remex.db.model;

import cn.remex.db.rsql.RsqlConstants;
import cn.remex.db.rsql.model.ModelableImpl;

import java.util.Random;
import java.util.UUID;

/**
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2013-6-23
 * <p>
 * <p>
 * 此表是用来保存主键的。为了避免主键冲突而设计
 */
public class SysSerialNumber extends ModelableImpl {
    public static final String createSerialNumber_oracle = "CREATE OR REPLACE FUNCTION\r\n" +
            "       \"createSerialNumber\" \r\n" +
            "          (beanName in \"SysSerialNumber\".\"beanName\"%type,\r\n" +
            "           fieldName in \"SysSerialNumber\".\"fieldName\"%type)\r\n" +
            "RETURN INTEGER\r\n" +
            "IS\r\n" +
            "       currentValue integer := 0;  --定义返回变量\r\n" +
            " \r\n" +
            "PRAGMA AUTONOMOUS_TRANSACTION;\r\n" +

            "BEGIN\r\n" +

            "    --最大数加1\r\n" +
            "    UPDATE \"SysSerialNumber\" SET \"currentValue\" = \"currentValue\"+1 where \"beanName\" = beanName and \"fieldName\" = fieldName\r\n" +
            "    Returning \"currentValue\" Into currentValue; --取出最大数\r\n" +

            "    If(SQL%NOTFOUND) THEN  --第一次向数据库中插入最大数为 1 的记录\r\n" +
            "       INSERT INTO \"SysSerialNumber\"(\"id\",\"beanName\",\"fieldName\",\"currentValue\") values('SSN'|| to_char(sysdate,'yyyyMMddhhmmss')||ABS(MOD(DBMS_RANDOM.RANDOM,10000)),beanName,fieldName,1) ;\r\n" +
            "       currentValue := 1;\r\n" +
            "    End If ;\r\n" +
            "     commit;\r\n" +
            "  return(currentValue); --返回结果\r\n" +
            "end \"createSerialNumber\";";

    public static final String querySerialNumber_oracle = "SELECT \"createSerialNumber\"(:beanName,:fieldName) from dual";

    public static Object createSerialNumber(final Class<?> beanClass) {
        return createSerialNumber(beanClass, RsqlConstants.SYS_id);
    }

    /*
     *
     *
"CREATE OR REPLACE FUNCTION\r\n"+
"       \"createSerialNumber\" \r\n"+
"          (beanName in \"SysSerialNumber\".\"beanName\"%type,\r\n"+
"           fieldName in \"SysSerialNumber\".\"fieldName\"%type)\r\n"+
"RETURN INTEGER\r\n"+
"IS\r\n"+
"       currentValue integer := 0;  --定义返回变量\r\n"+
" \r\n"+
"PRAGMA AUTONOMOUS_TRANSACTION;\r\n"+

"BEGIN\r\n"+

"    --最大数加1\r\n"+
"    UPDATE \"SysSerialNumber\" SET \"currentValue\" = \"currentValue\"+1 where \"beanName\" = beanName and \"fieldName\" = fieldName\r\n"+
"    Returning \"currentValue\" Into currentValue; --取出最大数\r\n"+

"    If(SQL%NOTFOUND) THEN  --第一次向数据库中插入最大数为 1 的记录\r\n"+
"       INSERT INTO \"SysSerialNumber\"(\"id\",\"beanName\",\"fieldName\",\"currentValue\") values('SSN'|| to_char(sysdate,'yyyyMMddhhmmss')||ABS(MOD(DBMS_RANDOM.RANDOM,10000)),beanName,fieldName,1) ;\r\n"+
"       currentValue := 1;\r\n"+
"    End If ;\r\n"+
"     commit;\r\n"+
"  return(currentValue); --返回结果\r\n"+
"end \"createSerialNumber\";"
    ==================================================================
CREATE OR REPLACE FUNCTION
       "createSerialNumber"
          (beanName in "SysSerialNumber"."beanName"%type,
           fieldName in "SysSerialNumber"."fieldName"%type)
RETURN INTEGER
IS
       currentValue integer := 0;  --定义返回变量

PRAGMA AUTONOMOUS_TRANSACTION;

BEGIN

    --最大数加1
    UPDATE "SysSerialNumber" SET "currentValue" = "currentValue"+1 where "beanName" = beanName and "fieldName" = fieldName
    Returning "currentValue" Into currentValue; --取出最大数

    If(SQL%NOTFOUND) THEN  --第一次向数据库中插入最大数为 1 的记录
       INSERT INTO "SysSerialNumber"("id","beanName","fieldName","currentValue") values('SSN'|| to_char(sysdate,'yyyyMMddhhmmss')||ABS(MOD(DBMS_RANDOM.RANDOM,10000)),beanName,fieldName,1) ;
       currentValue := 1;
    End If ;
     commit;
  return(currentValue); --返回结果
end "createSerialNumber";
     *
     *
     *
     */
    private static Random radomSeed = new Random();

    public static Object createSerialNumber(final Class<?> beanClass, String fieldName) {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		String beanName = StringHelper.getClassSimpleName(beanClass);
//		params.put("beanName", beanName);
//		params.put("fieldName", fieldName);

//		Container session = ContainerFactory.getSession();
//		try {

//			return session.executeQuery(RDBManager.getLocalSpaceConfig().getDialect().obtainQuerySerialNumberFunctionSQL(), params).getCell(0, 0);
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(uuid.lastIndexOf("-") + 1, uuid.length());
//		} catch (RsqlException e) {
//			if (StringHelper.match(e.getCause().getMessage(), "(ORA-00904)|(ORA-06575)", null)!=null
//					||
//					(e.getCause() instanceof MySQLSyntaxErrorException && ((MySQLSyntaxErrorException)e.getCause()).getSQLState().equals("42000"))
//					) {
//				ContainerFactory.getSession().createCall(RDBManager.getLocalSpaceConfig().getDialect().obtainCreateSerialNumberFunctionSQL());
//				return session.executeQuery(querySerialNumber_oracle, params).getCell(0, 0);
//			}else{
//				throw e;
//			}
//		}

//		
//		String where1 = " WHERE "	+d.quoteKey("beanName") + " = :beanName1"
//				+(null==fieldName?"":" AND "+d.quoteKey("fieldName")+" = :fieldName1");
//		String where2 = " WHERE "	+d.quoteKey("beanName") + " = :beanName2"
//				+(null==fieldName?"":" AND "+d.quoteKey("fieldName")+" = :fieldName2");
//		
//		String updateSql = "UPDATE "+K_SysPrimaryKey
//				+" SET "+K_currentValue+" = ((select "+K_currentValue+" from "+K_SysPrimaryKey+where1+")+1)"
//				+where2;
//		if(0==session.executeUpdate(updateSql,params).getUpdateResult().getEffectRowCount()){
//			SysSerialNumber ssn = new SysSerialNumber();
//			ssn.setBeanName(beanName);
//			ssn.setFieldName(fieldName);
//			ssn.setCurrentValue("1");
//			ContainerFactory.getSession().store(ssn);
////			String insertSql = "INSERT INTO "+SysPrimaryKey+" ("+d.quoteKey("id")+","
////																+d.quoteKey("beanName")+","
////																+d.quoteKey("fieldName")+","
////																+d.quoteKey("currentValue")+")VALUES("
////																+d.quoteAsString((new SysSerialNumber()).generateId())+","
////																+d.quoteAsString(beanName)+","
////																+d.quoteAsString(null==fieldName?"":fieldName)+","
////																+"1)";
////			RsqlDao.executeUpdate(insertSql);
//		}
    }

    /**
     *
     */
    private static final long serialVersionUID = 3209654992912075385L;
    private String beanName;
    private String fieldName;
    private String currentValue;

    @Override
    public String generateId() {
        return String.format("%1$s%2$tY%2$tm%2$td%2$TH%2$TM%2$TS%2$TS%2$TL", obtainAbbreviation(), System.currentTimeMillis());
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }
}
