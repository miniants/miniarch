package cn.remex.core.validator;

import javax.validation.ConstraintViolation;
import java.util.Set;

/** 
 * @author kangsj
 * @date 2014-12
 * @version 版本号码
 * 本类用于封装校验结果
 */
public class RemexValidateContext <T>{
	private Set<ConstraintViolation<T>> constraints;
	
	RemexValidateContext(Set<ConstraintViolation<T>> constraints) {
		this.constraints = constraints;
	}
	
	/**
	 * 判断校验的对象中存在违反约束。
	 * @return
	 * 只要属性约束（注解式、xml配置方式）的数量大于1则返回true，否则false。
	 */
	public boolean hasConstraintViolation() {
		return constraints.size()>0;
	}
	
	/**
	 * 获得该bean对应的所有约束
	 * @return
	 */
	public Set<ConstraintViolation<T>> getConstraints() {
		return constraints;
	}

	/**
	 * 默认推荐的、没有校验通过的那个结果集（通过next()获取，违反约束的结果对象）
	 * @return
	 */
	public ConstraintViolation<T> getDefaultConstraintViolation() {
		return constraints.iterator().next();
	}

	/**
	 * 默认推荐的、没有校验通过的那个rootBean
	 * @return
	 */
	public T getDefaultRootBean() {
		return  constraints.iterator().next().getRootBean();
	}

	/**
	 * 首个违反约束的结果集信息
	 * @return
	 */
	public String getDefaultConstraintMsg() {
		return constraints.iterator().next().getMessage();
	}
}
