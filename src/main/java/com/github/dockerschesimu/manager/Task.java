package com.github.dockerschesimu.manager;

/**
 * 单个任务对象
 * @author wzt
 *
 */
public class Task {
	
	/**
	 * 任务CPU需求
	 */
	private int needThreadNum;		//需要的线程数
	private float testFrequence;	//任务测试主频
	private double testCoreUse;		//任务测试主频下的单核cpu利用率
	
	public int getNeedThreadNum() {
		return needThreadNum;
	}
	public void setNeedThreadNum(int needThreadNum) {
		this.needThreadNum = needThreadNum;
	}
	public float getTestFrequence() {
		return testFrequence;
	}
	public void setTestFrequence(float testFrequence) {
		this.testFrequence = testFrequence;
	}
	public double getTestCoreUse() {
		return testCoreUse;
	}
	public void setTestCoreUse(double testCoreUse) {
		this.testCoreUse = testCoreUse;
	}
	
	
}
