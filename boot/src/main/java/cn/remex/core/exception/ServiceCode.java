package cn.remex.core.exception;

/**
 * 名称：
 * 缩写：
 * 用途：
 * Created by yangy on 2016/4/23 0023.
 */
public interface ServiceCode {
    String ERROR = "ERROR";
    String FAIL = "FAIL";
    String SUCCESS = "SUCCESS";

    //bs
    String BS_ERROR = "BS_ERROR";

    //core
    String DATE_INVALID = "DATE_INVALID";
    String XML_STRING_INVALID = "XML_STRING_INVALID";


    //db
    String RSQL_INIT_ERROR = "RSQL_INIT_ERROR";
    String RSQL_CONNECTION_ERROR = "RSQL_CONNECTION_ERROR";
    String RSQL_BEANSTATUS_ERROR = "RSQL_BEANSTATUS_ERROR";
    String RSQL_BEANCLASS_ERROR = "RSQL_BEANCLASS_ERROR";
    String RSQL_SQL_ERROR = "RSQL_SQL_ERROR";
    String RSQL_EXECUTE_ERROR = "RSQL_SQL_ERROR";
    String RSQL_QUERY_ERROR = "RSQL_QUERY_ERROR";
    String RSQL_UPDATE_ERROR = "RSQL_EXECUTE_ERROR";
    String RSQL_CREATECALL_ERROR = "RSQL_CREATECALL_ERROR";
    String RSQL_DIALECT_ERROR = "RSQL_DIALECT_ERROR";
    String RSQL_DATA_ERROR = "RSQL_DATA_ERROR";
    String RSQL_FAIL = "RSQL_FAIL";
    String ALIAS_NAME_INVALID = "ALIAS_NAME_INVALID";

    //UserService
    String ACCOUNT_ = "ACCOUNT_";
    String ACCOUNT_ERROR = ACCOUNT_+"ERROR";
    String ACCOUNT_NOT_AUTH = ACCOUNT_+"NOT_AUTH";//凡是以此变量都会发送 HTTP 401
    String ACCOUNT_NOT_PERMIT = ACCOUNT_+"NOT_PERMIT";//凡是以此变量都会发送 HTTP 401
    String ACCOUNT_USERNAME_INVALID = ACCOUNT_+"USERNAME_INVALID";
    String ACCOUNT_PASSWORD_INVALID = ACCOUNT_+"PASSWORD_INVALID";
    String ACCOUNT_PASSWORD_NOTMATCH = ACCOUNT_+"PASSWORD_NOTMATCH";

    //WeixinService
    String OPENID_INVALID = "OPENID_INVALID";
    String WEIXIN_ARGS_INVALID = "WEIXIN_ARGS_INVALID";
    String WEIXIN_NOT_BIND = "WEIXIN_NOT_BIND";
    String WEIXIN_HAS_BIND = "WEIXIN_HAS_BIND";
}