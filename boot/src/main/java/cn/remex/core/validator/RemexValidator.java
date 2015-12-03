package cn.remex.core.validator;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 使用javax jsr303校验标准进行bean值校验。<br>
 * 一般使用注解方式。
 * 
 * @author kangsj
 * @date 2014-12
 * @version 版本号码
 * @TODO 描述
 */
public class RemexValidator {
	private static ValidatorFactory validatorFactory;
	private static Validator validator;

	static {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	private RemexValidator() {
	}

	/**
	 * 校验bean对象中所有的属性
	 * 
	 * Parameters: object object to validate groups group or list of groups
	 * targeted for validation (default to javax.validation.groups.Default)
	 * Returns: constraint violations or an empty Set if none Throws:
	 * IllegalArgumentException - if object is null or if null is passed to the
	 * varargs groups ValidationException - if a non recoverable error happens
	 * during the validation process
	 * 
	 * @param bean
	 * @param groups
	 * @return
	 */
	public static <T> RemexValidateContext<T> validate(T bean, Class<?>... groups) {
		RemexValidateContext<T> validateContext = new RemexValidateContext<T>(validator.validate(bean, groups));
		return validateContext;
	}

	/**
	 * 校验bean对象中propertyName属性的合法性.仅校验此属性。
	 * 
	 * Parameters: object object to validate propertyName property to validate (ie
	 * field and getter constraints) groups group or list of groups targeted for
	 * validation (default to javax.validation.groups.Default) Returns: constraint
	 * violations or an empty Set if none Throws: IllegalArgumentException - if
	 * object is null, if propertyName null, empty or not a valid object property
	 * or if null is passed to the varargs groups ValidationException - if a non
	 * recoverable error happens during the validation process
	 * 
	 * @param bean
	 * @param propertyName
	 * @param groups
	 * @return {@link RemexValidateContext}是进过封装的校验上下文结果。
	 */
	public static <T> RemexValidateContext<T> validate(T bean, String propertyName, Class<?>... groups) {
		RemexValidateContext<T> validateContext = new RemexValidateContext<T>(validator.validateProperty(bean, propertyName, groups));
		return validateContext;
	}

	/**
	 * 获得原生的校验工厂
	 * @return
	 */
	public static ValidatorFactory getValidatorFactory() {
		return validatorFactory;
	}

	/**
	 * 获得原生的校验器
	 * @return
	 */
	public static Validator getValidator() {
		return validator;
	}
}
