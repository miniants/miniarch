package cn.remex.web.service;

import cn.remex.core.reflect.ReflectUtil;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

import javax.validation.Valid;

/**
 * 业务服务模型数据获取
 * 获取前台 form json xml 中 head body extend 节点
 * head 包含系统字段  body为数据字段 extend 为扩展字段
 * @rmx.call {@cn.remex.web.form.FormCvo}
 *@rmx.call{@cn.remex.web.json.JsonCvo}
 */
public abstract class BsCvo implements Serializable{
    private static final long serialVersionUID = 3155133328004613812L;
    private String bs;                  //必选,调用的Bs名称或映射值
    private String bsCmd = "execute";   //可选,调用的Bs的方法名
    private String rt;					//可选,返回的类型，
    private String rv;					//可选,返回结构的值，
    private String pk;                  //可选, LHY 2015-6-21 RESTFul模式设计中的resource/pk主键 部分。 http://host:prot/app/services/resources services:service-bsCmdresources:beans/num/subBeans/num 循环beans/num
    private String sid;                 //可选, 会话id
    private String flowNo;              //可选, 流程流水号
    private String transNo;             //可选, 交易流水号
    private String cpt;                 //可选,调用端组件标识
    private String mid;					//可选,随机码
    private String ef;			        //可选,是否加密 encryprFlag
    private String et;			        //可选,加密类型 encryptType
    private String tkn;					//可选,本次交易的token值，避免重复提交。
    private String stk;                 //可选,身份验证security token

    private String insuCom;					//厂商
    private String mangCom;					//管理机构
    private String execCom;					//执行机构
    private String execProtocolCode;		//执行机构协议编号
    private String agntCom;				    //行销机构
    private String agntProtocolCode;		//行销机构协议编号
    private String cityCode;			    //城市编码
    private String channelOper;				//系统当前的操作员
    private String channelUser;				//用户名
    private String channelPwd;				//密码
    private String traderIP;                //商户IP
    private String traderMac;			    //商户机器码
    private String transChannel;            //交易渠道编码
    private String transDate;				//交易日期
	private String transType;                 //交易流水号

	protected Object body;
	private Map params;
	private String contextPath;

	//functions

	/**
	 * 返回bsCvo可以找到的值,先从属性中找,找不到再从params中找
	 * @param key
	 * @return
	 */
	public Object $V(Object key){
		if(null != ReflectUtil.getGetter(this.getClass(),key.toString())){
			return ReflectUtil.invokeGetter(key.toString(),this);
		}
		Object o = this.params.get(key);
		if(o instanceof String[]) {
			return ((String[])o)[0].toString().trim();
		} else if(o instanceof File[]) {
			return ((File[])o)[0];
		} else {
			return o;
		}
	}

	//getters and setters
	public String getAgntCom() {
		return agntCom;
	}
	public String getAgntProtocolCode() {
		return agntProtocolCode;
	}
	public String getBs() {
		return bs;
	}
	public String getBsCmd() {
		return bsCmd;
	}
	public String getChannelOper() {
		return channelOper;
	}
	public String getChannelPwd() {
		return channelPwd;
	}
	public String getChannelUser() {
		return channelUser;
	}
	public String getCityCode() {
		return cityCode;
	}
	public String getCpt() {
		return cpt;
	}
	public String getEf() {
		return ef;
	}
	public String getEt() {
		return et;
	}
	public String getExecCom() {
		return execCom;
	}
	public String getExecProtocolCode() {
		return execProtocolCode;
	}
	public String getInsuCom() {
		return insuCom;
	}
	public String getMangCom() {
		return mangCom;
	}
	public String getMid() {
		return mid;
	}
	public String getRt() {
		return rt;
	}
	public String getRv() {
		return rv;
	}
	public String getTkn() {
		return tkn;
	}
	public String getTraderIP() {
		return traderIP;
	}
	public String getTraderMac() {
		return traderMac;
	}
	public String getTransChannel() {
		return transChannel;
	}
	public String getTransDate() {
		return transDate;
	}
	public String getTransNo() {
		return transNo;
	}
	public String getTransType() {
		return transType;
	}
	public void setAgntCom(String agntCom) {
		this.agntCom = agntCom;
	}
	public void setAgntProtocolCode(String agntProtocolCode) {
		this.agntProtocolCode = agntProtocolCode;
	}
	public void setBs(String bs) {
		this.bs = bs;
	}
	public void setBsCmd(String bsCmd) {
		this.bsCmd = bsCmd;
	}
	public void setChannelOper(String channelOper) {
		this.channelOper = channelOper;
	}
	public void setChannelPwd(String channelPwd) {
		this.channelPwd = channelPwd;
	}
	public void setChannelUser(String channelUser) {
		this.channelUser = channelUser;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public void setCpt(String cpt) {
		this.cpt = cpt;
	}
	public void setEf(String ef) {
		this.ef = ef;
	}
	public void setEt(String et) {
		this.et = et;
	}
	public void setExecCom(String execCom) {
		this.execCom = execCom;
	}
	public void setExecProtocolCode(String execProtocolCode) {
		this.execProtocolCode = execProtocolCode;
	}
	public void setInsuCom(String insuCom) {
		this.insuCom = insuCom;
	}
	public void setMangCom(String mangCom) {
		this.mangCom = mangCom;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public void setRt(String rt) {
		this.rt = rt;
	}
	public void setRv(String rv) {
		this.rv = rv;
	}
	public void setTkn(String tkn) {
		this.tkn = tkn;
	}
	public void setTraderIP(String traderIP) {
		this.traderIP = traderIP;
	}
	public void setTraderMac(String traderMac) {
		this.traderMac = traderMac;
	}
	public void setTransChannel(String transChannel) {
		this.transChannel = transChannel;
	}
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}
	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getFlowNo() {
		return flowNo;
	}
	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
    public String getStk() {
        return stk;
    }
    public void setStk(String stk) {
        this.stk = stk;
    }

	public void setParams(Map params) {
		this.params = params;
	}

	public Map getParams() {
		return params;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getContextPath() {
		return contextPath;
	}
}
