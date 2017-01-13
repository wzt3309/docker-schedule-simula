package com.github.dockerschesimu.device;

import java.util.ArrayList;
import java.util.List;

import com.github.dockerschesimu.constant.DeviceConstants;
import com.github.dockerschesimu.error.device.SetCoreUseError;
import com.github.dockerschesimu.error.device.SetCoreUseNegtiveError;
import com.github.dockerschesimu.error.device.SetCoreUseOverMaxError;
import com.github.dockerschesimu.manager.Task;
import com.github.dockerschesimu.tools.BaseUUID;

import static com.github.dockerschesimu.tools.BaseLogger.*;
/**
 * 模拟cpu核心
 * @author wzt
 *
 */
public class Core implements Device{

	//private final long uuid;			//核心的设备uuid
	//private final long ref_cpu_uuid;	//核心所关联cpu的设备uuid
	private final int id;				//核心编号，表示在cpu中是第几个核
	private final float frequency;		//核心主频
	private double use;					//核心利用率 百分数
	
	private List<Double> recoverUse=new ArrayList<>();	//记录点保存核心利用率
	private int recoverI=0;								//当前是第几个保存点
	
	//---------------------------------------------构造区-----------------------------------//
	{
		//this.uuid=BaseUUID.uuid(DeviceConstants.IDENTIFI_CORE);
	}
	public Core(int id){
		this(id,DeviceConstants.DEFAULT_CORE_FREQUENCY);
	}
	public Core(int id,float frequency){
		this.id=id;
		//this.ref_cpu_uuid=ref_cpu_uuid;
		this.frequency=frequency;
	}
	public void init(double use) {
		try {
			if(use>100)
				throw new SetCoreUseOverMaxError();
			else if(use<0)
				throw new SetCoreUseNegtiveError();
			this.use = use;		
		} catch (SetCoreUseError e) {
			ERROR(String.format("cpu 第%d核心初始化出错\n 输入use:%7.2f有误"
					, //String.valueOf(ref_cpu_uuid),
					id,use));
		}
		checkPoint();
	}
	@Override
	public boolean doTask(Task task) {
		float multi=task.getTestFrequence()/frequency;
		use+=multi*task.getTestCoreUse();
		use=use>100?100:use;
		return true;			
	}
	@Override
	public double getLoad() {
		return use;
	}
	@Override
	public int recover() {
		int i=-1;
		if(recoverI>0){
			i=recoverI-1;
			recover(i);				
		}
		return i;	
	}
	@Override
	public void recover(int i) {
		if(i<recoverUse.size()&&i>-1){
			this.use=recoverUse.get(i);
			int last=recoverI-i-1;
			for(int j=0;j<last;j++){
				recoverUse.remove(recoverUse.size()-1);
			}
			recoverI=i+1;
		}
	}
	@Override
	public int getRecoverI(){
		return recoverI;
	}
	@Override
	public void checkPoint() {
		recoverUse.add(use);
		recoverI++;
	}
	@Override
	public void monitor() {
		INFO(this);
	}
	@Override
	public void info() {
		INFO(toInfo());
	}	
	//---------------------------------------------对象方法区-----------------------------------//
	/**
	 * 返回描述 [id,uuid,frequency,use,ref_cpu_uuid]
	 */
	public String toString(){
		return String.format("[%-2d %7.2fGHz %7.2f%%]", id
				,frequency,use);
	}
	public String toInfo(){
		return String.format("[%-2d %7.2fGHz]", id
				,frequency);
	}
	//---------------------------------------------getter/setter区-----------------------------------//
	public float getFrequency() {
		return frequency;
	}		
	public int getId() {
		return id;
	}
//	public long getUuid() {
//		return uuid;
//	}
//	public long getRef_cpu_uuid() {
//		return ref_cpu_uuid;
//	}
	
	
}
