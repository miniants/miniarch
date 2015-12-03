/*
 * 文 件 名 : Arith.java
 * CopyRright (c) since 2012:
 * 文件编号：
 * 创 建 人：Liu Hengyang Email:yangyang8599@163.com QQ:119316891
 * 日    期： 2012-11-22
 * 修 改 人：
 * 日   期：
 * 描   述：
 * 版 本 号： 1.0
 */

package cn.remex.core.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Hengyang Liu  yangyang8599@163.com
 * @since 2012-11-22
 *
 */
/**
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精 确的浮点数运算，包括加减乘除和四舍五入。
 */
/**
 * @author LIU
 *
 */
public final class Arith {
	static public void  main(String...strings ){
		
//		double[] source={112,322,324,424,511,222,322,321,2343,554,758,654,444,647,641,418,651,189,199,189,839,809,760,199,299,3333,449,595,696,33,46};
		double[] source={5,2,4,6,7,9,11,3,12,22};
		List<Combination<Numeric>> a = findCombination(source , 10, 5, -0.8, 0);
		System.out.println(a.size());
		for(Combination<Numeric> n:a){
			System.out.println(n);
		}
	}
	
	/**
	 * @see Arith#findCombination(List, double, double, double, double)
	 * @param source
	 * @param targetValue
	 * @param positiveDeviation
	 * @param negativeDeviation
	 * @param permittedDeviation
	 * @return
	 */
	public static List<Combination<Numeric>> findCombination(double[] source,final double targetValue,final double positiveDeviation,final double negativeDeviation,final double permittedDeviation){
	
		List<Numeric> sourceList = new ArrayList<Numeric>();
		for(double n:source){
			sourceList.add(new Number(n));
		}
		
		return findCombination(sourceList, targetValue, positiveDeviation, negativeDeviation, permittedDeviation);
	}
	
	/**
	 * 本函数是用于从给定组合数字中查找符合以下条件的任意组合：<br>
	 * 1.组合值的加和在目标值targetValue正负偏差度为positiveDeviation-negativeDaviation之间。
	 * 2.如果许可偏差permittedDevation不为零，则系统将在查找到第一个符合许可偏差的组合后返回结果
	 * @param <T>
	 * 
	 * @param source 原始对象集合
	 * @param targetValue 目标值
	 * @param positiveDeviation 正向偏差
	 * @param negativeDeviation 负向偏差
	 * @param permittedDeviation 许可偏差，即符合最优条件的最差偏离度，只要符合这个偏差即可终止计算。由于优化的，默认为0，此时系统将不对计算进行优化。
	 * @return
	 */
	public static <T extends Numeric> List<Combination<T>> findCombination(List<T>source,final double targetValue,final double positiveDeviation,final double negativeDeviation,final double permittedDeviation){
		if(source == null || source.size() == 0) return null;
		//默认的数字排序比较器
		Comparator<Numeric> comparator = new Comparator<Numeric>() {
			@Override
			public int compare(Numeric o1, Numeric o2) {
				double diff = o1.getValue()-o2.getValue();
				return diff>=0?1:-1;
			}
		};
		
		//返回的结果保存的treemap，将根据偏离度的值进行升序排序
		List<Combination<T>> resultList = new ArrayList<Combination<T>>();
		//排序是为了在深搜时可以进行剪枝
		Collections.sort(source,comparator);
		
		//目标组合的所有元素的数量
		final int length = source.size();
		//统计总和有多少个组合符合条件
		int count = 0;
		
		//组合计算中保存下标的数组
		//本函数选取的组合没有数量限制，返回的结果中可能有一个元素就能符合条件的，也可能是全部元素之和符合条件
		//下标为了方便是从1开始计算的，取值时需要注意
		int []subscriptArray = new int[length];
		//将所有的下标都置为0，表示初始组合中没有元素
		for(int i=0;i<length;i++) {
			subscriptArray[i] = 0;
		}
		//从1个元素开始计算
		subscriptArray[0] = 1;
		
		//标识resultArray中有多少个位置不为0
		//也就当下参与计算比较的组合中有多少个元素
		//个数是从1开始的，数组是从0开始的，故其值刚好是下一个元素的数组index
		int arrayLocationIndex = 1;
		int tempNumber = 1;//标志当前已经到了来源数组的哪个下标
		
		/**
		 * 循环比较。
		 * 退出条件为，当使用最后一个元素进行比较结束后，循环退出
		 * 
		 * 循环的逻辑为：
		 * 从小到大比较。将数列中的数字依次选定当做基数，依次加0，1，2，3.....个元素至最后一个，凡是前面参与过的数字不在参与下面的组合计算。全部算完后，再更换基数到下一个
		 * 
		 */
		while(subscriptArray[0] != length+1) {
			//当前组合的值
			double curCombinationValue = 0;
			//for循环计算当前结果
			for(int i=0;i<subscriptArray.length;i++) {
				if(subscriptArray[i] != 0) {
					curCombinationValue += source.get(subscriptArray[i]-1).getValue();
				}
			}
			
			//计算偏离度
			double diff = (curCombinationValue -targetValue)/targetValue;
			//判断偏离度是否在目标区间
			boolean ok = diff>=negativeDeviation && diff<=positiveDeviation;
			if(ok ) {//得到了一个符合条件的结果
				count++;
				//保存当前组合
				List<T> combinationItems = new ArrayList<T>();
				for(int i=0;i<subscriptArray.length;i++) {
					if(subscriptArray[i] != 0) {
						T bj = source.get(subscriptArray[i]-1);
						combinationItems.add(bj);
					}
				}
				//将组合放到结果中
				resultList.add(new Combination<T>(diff, combinationItems));
				
				//如果找到一个符合许可偏差度的
				if(permittedDeviation!=0 && Math.abs(diff)<permittedDeviation)
					return resultList;
				
				if(count>1000000)
					throw new RuntimeException("组合的可能性太多，超过100W种组合均符合条件，终止计算！");
			}
			
			//应为算法经过排序，在一个指定基数的组合上只要出现一次超出上限边界的组合，该数量的组合的后续计算的组合值都会大于目标值的。所以此处已上限为出发条件的边界
			if(diff==positiveDeviation){
				if(arrayLocationIndex == 1) {
					//一个对象就刚刚好满足上限的条件，那么任意再加一个数就一定会大于目标值，所以直接换个基数再算
					//这种情况下数组中只有一个元素，那么把它加1(a中的下标)
					subscriptArray[0]++;
				} else  {
					//此处是所有组合起来刚符合上限条件的组合
					if(tempNumber<length){
						//当前组合符合条件，而且数组没有到最后，这个时候需要将最后加进来的数字换换下一个，因为下一个数字有可能与之相同，则不能错过另外一个组合。
						//把resultArray中最后一个不为0的置为0，同时把倒数第二个元素＋1
						subscriptArray[arrayLocationIndex-1]++;
						tempNumber = subscriptArray[arrayLocationIndex-1];
					}else{
						//本组合中最后一个刚好符合条件，需要退回计算另外一个组合
						//到a数组的最后了，回退
						subscriptArray[arrayLocationIndex-1]=0;
						subscriptArray[arrayLocationIndex-2]++;
						tempNumber = subscriptArray[arrayLocationIndex-2];
						arrayLocationIndex--;
					}
				}
				//diff==positiveDeviation end
			}else if(diff<positiveDeviation) {
				if(tempNumber < length) {
					//继续添加下一个
					tempNumber++;
					subscriptArray[arrayLocationIndex] = tempNumber;//arrayLocationIndex的数量刚好是下一个的游标的数组的index
					arrayLocationIndex++;
				}else {//tempNumber==length,tempNumber不会大于length的；
					//算到最后一个数字的情况
					if(arrayLocationIndex==1){
						//1如果当前组合的最后一个数字是原始数组的最后一个，说明计算的值已经是最大的了，后续的所有组合数组一定比这个小，所以就不用计算，退出循环
						//2最后一个数字也太小的时候
						break;
					}else{
						//当前组合太小加到最后都太小，需要退回上一层次的组合中切换到下一个
						subscriptArray[arrayLocationIndex-1]=0;	//arrayLocationIndex-1是当前组合中最后一个数组的游标位置
						subscriptArray[arrayLocationIndex-2]++; //arrayLocationIndex-2是当前组合中倒数第二个数组的游标位置，到最后都是小于目标值，只能换下一个大数在计算了
						tempNumber = subscriptArray[arrayLocationIndex-2];
						arrayLocationIndex--;
					}
				}
				//diff<positiveDeviation end
			}else if(diff>positiveDeviation) {
				if(tempNumber == 1 || arrayLocationIndex==1){
					//第一个数都比目标值大，就不用组合了。所有组合都一定比他大。
					break;
				}else{
					//到a数组的最后了，回退
					subscriptArray[arrayLocationIndex-1]=0;
					subscriptArray[arrayLocationIndex-2]++;
					tempNumber = subscriptArray[arrayLocationIndex-2];
					arrayLocationIndex--;
				}
				//diff>positiveDeviation end
			}
		}
		
		Collections.sort(resultList,new Comparator<Combination<T>>() {
			@Override
			public int compare(Combination<T> o1, Combination<T> o2) {
				double diff = Math.abs(o1.getDeviation()) - Math.abs(o2.getDeviation());
				return diff>0?1:diff<0?-1:o1.getDeviation()<0?1:o1.getDeviation()>0?-1:0;
			}
		});
		
		return resultList;
	}
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 10;

	public final static BigDecimal one = new BigDecimal("1");

	/**
	 * 提供精确的加法运算。
	 * v1+v2
	 * 
	 * @param v1
	 *            被加数
	 * @param v2
	 *            加数
	 * @return 两个参数的和
	 */
	public static double add(final Object v1, final Object v2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(v1));
		BigDecimal b2 = new BigDecimal(String.valueOf(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
	 * v1/v2
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @return 两个参数的商
	 */
	public static double div(final Object v1, final Object v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * v1/v2
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。最大不超过double的限制。
	 * @return 两个参数的商
	 */
	public static double div(final Object v1, final Object v2, final int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(String.valueOf(v1));
		BigDecimal b2 = new BigDecimal(String.valueOf(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 提供精确的乘法运算。
	 * v1*v2
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static double mul(final Object v1, final Object v2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(v1));
		BigDecimal b2 = new BigDecimal(String.valueOf(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(final Object v, final int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(String.valueOf(v));
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static String round(final String numbericString, final int scale) {
		BigDecimal b = new BigDecimal(numbericString);
		if(0==scale){
			return String.valueOf(b.divide(Arith.one, scale, BigDecimal.ROUND_HALF_UP).intValue());
		}else{
			return String.valueOf(b.divide(Arith.one, scale, BigDecimal.ROUND_HALF_UP).doubleValue());
		}
	}

	/**
	 * 提供精确的减法运算。
	 * v1-v2
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(final Object v1, final Object v2) {
		BigDecimal b1 = new BigDecimal(String.valueOf(v1));
		BigDecimal b2 = new BigDecimal(String.valueOf(v2));
		return b1.subtract(b2).doubleValue();
	}
	// 这个类不能实例化
	private Arith() {
	}
}





