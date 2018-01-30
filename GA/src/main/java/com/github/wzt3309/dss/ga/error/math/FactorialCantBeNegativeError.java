package com.github.wzt3309.dss.ga.error.math;

/**
 * 不能计算负数的阶乘
 * @author wzt
 *
 */
@SuppressWarnings("serial")
public class FactorialCantBeNegativeError extends ValInputIsnRightError {
	
	public FactorialCantBeNegativeError(){
		
	}
	public FactorialCantBeNegativeError(String msg){
		super(msg);
	}
}
