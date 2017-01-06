package com.github.dockerschesimu.device;

import com.github.dockerschesimu.constant.DeviceConstants;
import com.github.dockerschesimu.error.ValError;
import com.github.dockerschesimu.tools.BaseUUID;
import com.github.dockerschesimu.tools.BaseValidate;

/**
 * 模拟机械硬盘
 * @author wzt
 *
 */
public class SATADisk {

	private final long uuid;		//硬盘设备uuid
	private final int rpm;			//硬盘转速
	private final int mtr;			//硬盘最大持续传输速率
	private final float aat;		//硬盘平均寻址时间
	private float use;				//硬盘当前负载 百分数
	{
		uuid=BaseUUID.uuid(DeviceConstants.IDENTIFI_DISK);
	}
	public SATADisk(){
		this(DeviceConstants.DEFAULT_SATA_RPM);
	}
	public SATADisk(int rpm){
		this(rpm,DeviceConstants.DEFAULT_SATA_MTR);
	}
	public SATADisk(int rpm,int mtr){
		this(rpm,mtr,DeviceConstants.DEFAULT_SATA_AAT);
	}
	public SATADisk(int rpm,int mtr,float aat){
		this.rpm=Math.abs(rpm);
		this.mtr=Math.abs(mtr);
		this.aat=Math.abs(aat);
	}
	/**
	 * task方法
	 * 给磁盘分配io任务
	 * @param waitt 磁盘等待时间 ms
	 * @param chunk 单次读写块的大小 kb
	 * @return
	 */
	public void taskIO(int waitt,int chunk){
		this.use+=iops(waitt,chunk)/maxiops(chunk)*100;
		if(this.use>100)
			this.use=100F;		
	
	}
	/**
	 * 任务开始前，设定硬盘当前的负载
	 * @param use
	 * @throws ValError 
	 */
	public void initIO(int use) throws ValError{
		if(!BaseValidate.noMoreThan(100, use))
			throw new ValError();
		this.use=use;
	}
	/**
	 * 有磁盘等待时间情况下，读取chunk块 iops
	 * @param waitt 磁盘等待时间 ms
	 * @param chunk 单次io读取块大小 kb
	 * @return
	 * @throws ValError 
	 */
	public float iops(int waitt,int chunk){
		float rest=aat+60F/(rpm*2F)+waitt+chunk/(mtr*1000F);
		return 1000F/rest;
	}
	/**
	 * 持续读写 chunk 最大iops
	 * @param chunk
	 * @return
	 */
	public float maxiops(int chunk){
		float rest=aat+60F/(rpm*2F)+chunk/(mtr*1000F);
		return 1000F/rest;
	}	
	public float getUse(){
		return this.use;
	}
	public long getUuid() {
		return uuid;
	}
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
