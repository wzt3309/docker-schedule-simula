package com.github.dockerschesimu.device;

import java.util.ArrayList;
import java.util.List;

import com.github.dockerschesimu.constant.DeviceConstants;
import com.github.dockerschesimu.error.ValError;
import com.github.dockerschesimu.manager.Task;
import com.github.dockerschesimu.tools.BaseUUID;
import static com.github.dockerschesimu.tools.BaseLogger.*;
/**
 * 模拟机械硬盘
 * @author wzt
 *
 */
public class Disk implements Device{

	//private final long uuid;		//硬盘设备uuid
	private final int rpm;			//硬盘转速
	private final int mtr;			//硬盘最大持续传输速率
	private final float aat;		//硬盘平均寻址时间
	private double use;				//硬盘当前负载 百分数
	
	private List<Double> recoverUse=new ArrayList<>();	//记录检查点
	private int recoverI=0;								//当前是第几个保存点
	
	{
		//uuid=BaseUUID.uuid(DeviceConstants.IDENTIFI_DISK);
	}
	public Disk(){
		this(DeviceConstants.DEFAULT_SATA_RPM);
	}
	public Disk(int rpm){
		this(rpm,DeviceConstants.DEFAULT_SATA_MTR);
	}
	public Disk(int rpm,int mtr){
		this(rpm,mtr,DeviceConstants.DEFAULT_SATA_AAT);
	}
	public Disk(int rpm,int mtr,float aat){
		this.rpm=Math.abs(rpm);
		this.mtr=Math.abs(mtr);
		this.aat=Math.abs(aat);
	}
	
	/**
	 * 任务开始前，设定硬盘当前的负载
	 * @param use
	 */
	public void init(double use){
		if(use<=100){
			this.use=use;
			checkPoint();
		}
			
	}
	@Override
	public boolean doTask(Task task) {
		int waitt=task.getNeedWaitt();
		int chunk=task.getNeedChunk();
		if(waitt>=0&&chunk>=0){
			taskIO(waitt, chunk);
			return true;
		}
		return false;
		
	}
	@Override
	public double getLoad() {		
		return use;
	}
	@Override
	public void checkPoint() {
		recoverUse.add(use);
		recoverI++;
	}
	@Override
	public int recover() {
		int k=-1;
		if(recoverI>0){
			k=recoverI-1;
			recover(k);
		}
		return k;
	}
	@Override
	public void recover(int i) {
		if(i<recoverI&&i>-1){
			this.use=recoverUse.get(i);
			int last=recoverI-i-1;
			for(int j=0;j<last;j++){
				recoverUse.remove(recoverUse.size()-1);
			}
			recoverI=i+1;
		}
	}
	@Override
	public int getRecoverI() {
		
		return recoverI;
	}
	@Override
	public void monitor() {
		INFO(String.format("%12s %10s %10s %8s"
				,"rpm(r/min)","mtr(mb/s)","aat(ms)","use%"));
		INFO(this);
	}
	@Override
	public void info() {
		INFO(this);
	}
	public String toString(){
		return String.format("[%10d %10d %10.2f %7.2f%%]",
				rpm,mtr,aat,use);
	}
	/**
	 * task方法
	 * 给磁盘分配io任务
	 * @param waitt 磁盘等待时间 ms
	 * @param chunk 单次读写块的大小 kb
	 * @return
	 */
	private void taskIO(int waitt,int chunk){
		if(waitt==0&&chunk==0){
			this.use+=0;
		}else{
			this.use+=iops(waitt,chunk)/maxiops(chunk)*100;
		}		
		if(this.use>100)
			this.use=100F;		
	
	}	
	/**
	 * 有磁盘等待时间情况下，读取chunk块 iops
	 * @param waitt 磁盘等待时间 ms
	 * @param chunk 单次io读取块大小 kb
	 * @return
	 * @throws ValError 
	 */
	private float iops(int waitt,int chunk){
		float rest=aat+60F/(rpm*2F)+waitt+chunk/(mtr*1000F);
		return 1000F/rest;
	}
	/**
	 * 持续读写 chunk 最大iops
	 * @param chunk
	 * @return
	 */
	private float maxiops(int chunk){
		float rest=aat+60F/(rpm*2F)+chunk/(mtr*1000F);
		return 1000F/rest;
	}	
	
	
//	public long getUuid() {
//		return uuid;
//	}
	public int getRpm() {
		return rpm;
	}
	public int getMtr() {
		return mtr;
	}
	public float getAat() {
		return aat;
	}
	
	
}
