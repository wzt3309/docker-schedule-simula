package com.github.dockerschesimu.device;

import com.github.dockerschesimu.constant.Device;
import com.github.dockerschesimu.error.ValError;
import com.github.dockerschesimu.tools.BaseUUID;

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
	private float maxiops;			//硬盘最大iops 无法到达（不读写任何内容，只计算选址和寻址时间）
	private float use;				//硬盘当前负载 百分数
	{
		uuid=BaseUUID.uuid(Device.IDENTIFI_DISK);
	}
	public SATADisk(){
		this(Device.DEFAULT_SATA_RPM);
	}
	public SATADisk(int rpm){
		this(rpm,Device.DEFAULT_SATA_MTR);
	}
	public SATADisk(int rpm,int mtr){
		this(rpm,mtr,Device.DEFAULT_SATA_AAT);
	}
	public SATADisk(int rpm,int mtr,float aat){
		this.rpm=Math.abs(rpm);
		this.mtr=Math.abs(mtr);
		this.aat=Math.abs(aat);
		this.maxiops=iops(0);
		
	}
	/**
	 * 
	 * @param rpm 硬盘转速
	 * @param mtr 硬盘最大持续传输率
	 * @param aat 平均选址延迟
	 * @param chunk 单次io读取块大小 kb
	 * @return
	 * @throws ValError 
	 */
	public int iops(int chunk){
		float rest=aat+60F/(rpm*2F)+chunk/(mtr*1000F);
		return Math.round(1000F/rest);
	}
	/**
	 * 获取磁盘当前io负载 百分数
	 * 当前读取 chunk大小的块 需要的iops与最大磁盘iops数之比
	 * @param chunk
	 * @return
	 */
	public float getUse(int chunk){
		chunk=Math.abs(chunk);
		if(chunk==0)
			chunk=Device.DEFAULT_CHUNK_SIZE;		
		this.use=(iops(chunk)+1F)/this.maxiops;
		return this.use;
	
	}	
	public float getMaxiops() {
		return maxiops;
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
