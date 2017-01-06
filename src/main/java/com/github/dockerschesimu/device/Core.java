package com.github.dockerschesimu.device;

import com.github.dockerschesimu.constant.DeviceConstants;
import com.github.dockerschesimu.error.device.SetCoreUseError;
import com.github.dockerschesimu.error.device.SetCoreUseNegtiveError;
import com.github.dockerschesimu.error.device.SetCoreUseOverMaxError;
import com.github.dockerschesimu.manager.Task;
import com.github.dockerschesimu.tools.BaseUUID;
/**
 * 模拟cpu核心
 * @author wzt
 *
 */
public class Core implements Device{

	private final long uuid;			//核心的设备uuid
	private final long ref_cpu_uuid;	//核心所关联cpu的设备uuid
	private final int id;				//核心编号，表示在cpu中是第几个核
	private final float frequency;		//核心主频
	private double use;					//核心利用率 百分数
	
	//---------------------------------------------构造区-----------------------------------//
	{
		this.uuid=BaseUUID.uuid(DeviceConstants.IDENTIFI_CORE);
	}
	public Core(int id,long ref_cpu_uuid){
		this(id,ref_cpu_uuid,DeviceConstants.DEFAULT_CORE_FREQUENCY);
	}
	public Core(int id,long ref_cpu_uuid,float frequency){
		this.id=id;
		this.ref_cpu_uuid=ref_cpu_uuid;
		this.frequency=frequency;
	}
	@Override
	public void init(double use) {
		try {
			setUse(use);
		} catch (SetCoreUseError e) {
			System.out.format("cpu[%18s] 第%d核心初始化出错\n 输入use:%7.2f有误"
					, String.valueOf(ref_cpu_uuid),id,use);
		}
	}
	@Override
	public boolean doTask(Task task) {
		float multi=frequency/task.getTestFrequence();
		use+=multi*task.getTestCoreUse();
		use=use>100?100:use;
		return true;			
	}
	@Override
	public double getLoad() {
		return 0;
	}
	@Override
	public void recover(CheckPoint point) {
	}
	@Override
	public void monitor() {
	}
	@Override
	public void info() {
	}	
	
	//---------------------------------------------对象方法区-----------------------------------//
	/**
	 * 返回描述 [id,uuid,frequency,use,ref_cpu_uuid]
	 */
	public String toString(){
		return String.format("[%-2d %-18s %7.2fGHz %7.2f%% %18s]", id,String.valueOf(uuid)
				,frequency,use,String.valueOf(ref_cpu_uuid));
	}
	/**
	 * 打印描述
	 */
	public void show(){
		System.out.println(this);
	}
	
	//---------------------------------------------getter/setter区-----------------------------------//
	public float getFrequency() {
		return frequency;
	}
	public double getUse() {
		return use;
	}
	public void setUse(double use) throws SetCoreUseError{
		if(use>100)
			throw new SetCoreUseOverMaxError();
		else if(use<0)
			throw new SetCoreUseNegtiveError();
		this.use = use;
	}
	public int getId() {
		return id;
	}
	public long getUuid() {
		return uuid;
	}
	public long getRef_cpu_uuid() {
		return ref_cpu_uuid;
	}
	
}
