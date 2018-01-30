package com.github.wzt3309.dss.ga.error.math;

/**
 * 排列组合中，组合计算输入值不合理
 * @author wzt
 *
 */
@SuppressWarnings("serial")
public class CombinValInputError extends ValInputIsnRightError {
	
	public CombinValInputError(){
		
	}
	
	public CombinValInputError(String msg){
		super(msg);
	}
}
