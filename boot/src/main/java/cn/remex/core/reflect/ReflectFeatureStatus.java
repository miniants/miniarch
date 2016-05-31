package cn.remex.core.reflect;

import cn.remex.RemexConstants;
import cn.remex.core.reflect.ReflectUtil.SPFeature;


/**
 * 为了提高复制和置值的效率，设计此中转类<br>使状态值集中存储 节省计算和迭代的开销
 * @author HengYang Liu
 */
public class ReflectFeatureStatus{
	boolean isSPFeatureOn =false;
	boolean ConvertNull2EmptyString=false;
	boolean ConvertZero2EmptyString=false;
	boolean IgnoreEmptyStringValue=false;
	boolean EnableNullValue=false;
	boolean enableDeeplyCopy=false;
	boolean CopyIdAndDataStatus=false;
	boolean CopyId=false;
	boolean CopyDataStatus=false;
	boolean IgnoreCase=false;
	boolean JustUseNamePreffix=false;
	boolean RemoveTargetGatherItemWhenSourceExclude = false;
	
	boolean enableMethodMap=false;
	
	boolean isDebugEnabled = RemexConstants.logger.isDebugEnabled();
	SPFeature[] features;
	public boolean enableCodeMap = false;
	

	/**
	 * @rmx.summary 对特性功能的是否启用的布尔值进行遍历赋值<br>
	 * @param features 入参为8中特性功能的枚举数组
	 * @rmx.call ReflectUtil copyProperties
	 * @rmx.call ReflectUtil setProperties
	 * @rmx.call ReflectUtil invokeSetterIgnoreNoSetter
	 * @rmx.call MapHelper toObjcet
	 * @see ReflectUtil.SPFeature
	 */
	public ReflectFeatureStatus(SPFeature[] features) {
		this.features = features;
		isSPFeatureOn = null != features && features.length > 0;
		// 特性功能
		if (isSPFeatureOn) {
			for (final SPFeature feature : features) {
				switch (feature) {
				case ConvertNull2EmptyString:// 将null转化为空字符串
					ConvertNull2EmptyString=true;
					break;
				case ConvertZero2EmptyString:// 将0/0.0转化为空字符串
					ConvertZero2EmptyString=true;
					break;
				case IgnoreEmptyStringValue:// 赋值时忽略空字符串,“”
					IgnoreEmptyStringValue = true;
					break;
				case EnableNullValue: // 赋值时将对null使用。比如从源对象A中通过getName获得值null，将赋值给B.setName(null)。
										// 此时原来B无论Name是否为null将被覆盖。
					EnableNullValue = true;
					break;
				case DeeplyCopy:
					enableDeeplyCopy = true;
					break;
				case CopyIdAndDataStatus:
					CopyIdAndDataStatus=true;
					CopyDataStatus=true;
					CopyId=true;
					break;
				case IgnoreCase:
					IgnoreCase=true;
					break;
				case JustUseNamePreffix:
					JustUseNamePreffix=true;
					break;
				case RemoveTargetGatherItemWhenSourceExclude:
					RemoveTargetGatherItemWhenSourceExclude=true;
					break;
				case CopyDataStatus:
					CopyDataStatus=true;
					CopyIdAndDataStatus = CopyId&&CopyDataStatus;
					break;
				case CopyId:
					CopyId=true;
					CopyIdAndDataStatus = CopyId&&CopyDataStatus;
					break;
				default:
					break;
				}
			}
			
			
		}
	}
	
}
