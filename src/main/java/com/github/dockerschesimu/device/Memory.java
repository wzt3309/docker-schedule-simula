package com.github.dockerschesimu.device;

import java.util.ArrayList;
import java.util.List;

import com.github.dockerschesimu.constant.DeviceConstants;
import com.github.dockerschesimu.error.device.MemoryOverFlowError;
import com.github.dockerschesimu.manager.Task;
import com.github.dockerschesimu.tools.BaseUUID;

/**
 * 模拟内存
 * @author wzt
 *
 */
public class Memory implements Device{

	private final long uuid;	//内存设备uuid
	private final int total;	//内存总容量 单位kb
	private int used;			//已被使用的内存 单位kb
	private int free;			//空闲的内存 单位kb
	
	private List<Integer> recoverUsed=new ArrayList<>();	//检查点使用的内存量
	private int recoverI=0;									//当前是第几个保存点
	
	//---------------------------------------------构造区---------------------------------------//
	{
		uuid=BaseUUID.uuid(DeviceConstants.IDENTIFI_MEM);
	}
	public Memory(){
		this(DeviceConstants.DEFAULT_MEM_TOTAL);
	}	
	public Memory(int total){
		this.total=total;
		
	}
	/**
	 * 任务开始前，设定内存使用情况 Mb
	 * @param used
	 * @throws MemoryOverFlowError
	 */
	public void init(int mem){		
		if(mem<=total)	{
			this.used=mem;
			this.free=this.total-this.used;
			checkPoint();
		}		
	}
	@Override
	public boolean doTask(Task task) {
		int needMem=task.getNeedMemory();
		if(needMem>free){
			return false;
		}			
		else{
			malloc(needMem);
		}
		return true;
	}
	@Override
	public double getLoad() {
		
		return (this.used+1.0F)/(this.total+1.0F)*100;
	}
	@Override
	public void checkPoint() {
		recoverUsed.add(used);
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
			this.used=recoverUsed.get(i);
			int last=recoverI-i-1;
			for(int j=0;j<last;j++){
				recoverUsed.remove(recoverUsed.size()-1);
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
		showGB();
	}
	@Override
	public void info() {
		System.out.println(this);
	}
	//-------------------------------------------对象方法区-------------------------------------//
	/**
	 * 分配内存
	 * @param used
	 * @throws MemoryOverFlowError
	 */
	private void malloc(int used) {		
		this.used+=used;
		this.free=this.total-this.used;
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
	public String toString(){
		return String.format("[%-18s %10d %10d %10d %7.2f%%]"
				, String.valueOf(uuid),total,used,free,getLoad());
	}
	/**
	 * 显示内存描述信息 单位KB
	 */
	private void show(){
		System.out.format("%19s %10s %10s %10s %8s\n"
				, "uuid","total(k)","used(k)","free(k)","use%");
		System.out.println(this);
	}
	/**
	 * 显示内存描述信息 单位GB
	 */
	private void showGB(){
		System.out.format("%19s %10s %10s %10s %8s\n"
				, "uuid","total(G)","used(G)","free(G)","use%");
		
		System.out.format("[%-18s %10.2f %10.2f %10.2f %7.2f%%]\n"
				, String.valueOf(uuid)
				,total/1000000f,used/1000000f,free/1000000f,getLoad());
	}
	
	
	
	
	
	
	
}
