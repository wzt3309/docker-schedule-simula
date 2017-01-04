package com.github.dockerschesimu.device;

import com.github.dockerschesimu.constant.Device;
import com.github.dockerschesimu.error.device.MemoryOverFlowError;
import com.github.dockerschesimu.tools.BaseUUID;
import com.github.dockerschesimu.tools.BaseValidate;

/**
 * 模拟内存
 * @author wzt
 *
 */
public class Memory {

	private final long uuid;//内存设备uuid
	private int total;	//内存总容量 单位kb
	private int used;	//已被使用的内存 单位kb
	private int free;	//空闲的内存 单位kb
	private float use;	//内存使用率
	
	//---------------------------------------------构造区---------------------------------------//
	{
		uuid=BaseUUID.uuid(Device.IDENTIFI_MEM);
	}
	public Memory(){
		this(Device.DEFAULT_MEM_TOTAL);
	}	
	public Memory(int total){
		this.total=total;
		
	}
	//-------------------------------------------对象方法区-------------------------------------//
	/**
	 * 分配内存
	 * @param used
	 * @throws MemoryOverFlowError
	 */
	public void malloc(int used) throws MemoryOverFlowError{
		if(!BaseValidate.noMoreThan(free, used))
			throw new MemoryOverFlowError();
		this.used+=used;
		this.free=this.total-this.used;
		this.use=(this.used+1.0F)/(this.total+1.0F)*100;
	}
	/**
	 * 任务开始前，设定内存使用情况
	 * @param used
	 * @throws MemoryOverFlowError
	 */
	public void initMEM(int used) throws MemoryOverFlowError{
		if(!BaseValidate.noMoreThan(total, used))
			throw new MemoryOverFlowError();
		this.used=used;
		this.free=this.total-this.used;
		this.use=(this.used+1.0F)/(this.total+1.0F)*100;
	}
	public int getTotal(){
		return this.total;
	}
	public int getUsed(){
		return this.used;
	}
	public int getFree(){
		return this.free;
	}
	public float getUse(){
		return this.use;
	}
	public String toString(){
		return String.format("[%-18s %10d %10d %10d %7.2f%%]"
				, String.valueOf(uuid),total,used,free,use);
	}
	/**
	 * 显示内存描述信息 单位KB
	 */
	public void show(){
		System.out.format("%19s %10s %10s %10s %8s\n"
				, "uuid","total(k)","used(k)","free(k)","use%");
		System.out.println(this);
	}
	/**
	 * 显示内存描述信息 单位GB
	 */
	public void showGB(){
		System.out.format("%19s %10s %10s %10s %8s\n"
				, "uuid","total(G)","used(G)","free(G)","use%");
		
		System.out.format("[%-18s %10.2f %10.2f %10.2f %7.2f%%]\n"
				, String.valueOf(uuid)
				,total/1000000f,used/1000000f,free/1000000f,use);
	}
	
	
	
	
	
	
}
