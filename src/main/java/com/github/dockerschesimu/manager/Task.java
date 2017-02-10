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
	
	/**
	 * 存储需求
	 */
	private int needMemory;			//需要的内存
	
	/**
	 * io需求
	 */
	private int needWaitt;			//磁盘等待时间 ms
	private int needChunk;			//单次读写块的大小 kb
	
	/**
	 * 网络相关
	 */
	private float needNet;			//需要的网络带宽
	
	private String lev;
	
	public Task(){
		
	}
	/**
	 * 只设定内存
	 * @param needMemory
	 */
	public Task(int needMemory){
		super();
		this.needMemory=needMemory;
	}
	/**
	 * 只设定磁盘IO
	 * @param needWaitt
	 * @param needChunk
	 */
	public Task(int needWaitt,int needChunk){
		super();
		this.needWaitt=needWaitt;
		this.needChunk=needChunk;
	}
	/**
	 * 只设定cpu
	 * @param needThreadNum
	 * @param testFrequence
	 * @param testCoreUse
	 */
	public Task(int needThreadNum, float testFrequence, double testCoreUse) {
		super();
		this.needThreadNum = needThreadNum;
		this.testFrequence = testFrequence;
		this.testCoreUse = testCoreUse;
	}	
	/**
	 * 设定所有选项
	 * @param needThreadNum
	 * @param testFrequence
	 * @param testCoreUse
	 * @param needMemory
	 * @param needWaitt
	 * @param needChunk
	 * @param needNet
	 */
	public Task(int needThreadNum, float testFrequence, double testCoreUse, int needMemory, int needWaitt,
			int needChunk, float needNet,String lev) {
		super();
		this.needThreadNum = needThreadNum;
		this.testFrequence = testFrequence;
		this.testCoreUse = testCoreUse;
		this.needMemory = needMemory;
		this.needWaitt = needWaitt;
		this.needChunk = needChunk;
		this.needNet = needNet;
		this.lev=lev;
	}
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
	public int getNeedMemory() {
		return needMemory;
	}
	public void setNeedMemory(int needMemory) {
		this.needMemory = needMemory;
	}
	public int getNeedWaitt() {
		return needWaitt;
	}
	public void setNeedWaitt(int needWaitt) {
		this.needWaitt = needWaitt;
	}
	public int getNeedChunk() {
		return needChunk;
	}
	public void setNeedChunk(int needChunk) {
		this.needChunk = needChunk;
	}
	public float getNeedNet() {
		return needNet;
	}
	public void setNeedNet(float needNet) {
		this.needNet = needNet;
	}	
	public String getLev() {
		return lev;
	}
	public void setLev(String lev) {
		this.lev = lev;
	}
	public void show(){
		System.out.println(this);
	}
	@Override
	public String toString() {
		return "Task lev:"+lev+"[needThreadNum=" + needThreadNum + ", testFrequence=" + testFrequence + ", testCoreUse="
				+ testCoreUse + ", needMemory=" + needMemory + ", needWaitt=" + needWaitt + ", needChunk=" + needChunk
				+ ", needNet=" + needNet + "]";
	}
	public String pCpu(){
		return "Task lev:"+lev+" tnum=" + needThreadNum + " freq=" + testFrequence + " use="
				+ testCoreUse;
	}
	public String pMem(){
		return "Task lev:"+lev+" mem=" + needMemory;
	}
	public String pDisk(){
		return "Task lev:"+lev+" wait=" + needWaitt+" chunk"+needChunk;
	}
	public String pNet(){
		return "Task lev:"+lev+" net=" + needNet;
	}
}
