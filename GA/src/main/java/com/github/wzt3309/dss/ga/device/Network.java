package com.github.wzt3309.dss.ga.device;

import java.util.ArrayList;
import java.util.List;

import com.github.wzt3309.dss.ga.constant.DeviceConstants;
import com.github.wzt3309.dss.ga.manager.Task;
import static com.github.wzt3309.dss.ga.tools.BaseLogger.*;

public class Network implements Device {
	
	private final int total;	//总带宽
	private float used;			//被使用的带宽
	private float free;			//空闲带宽
	
	private List<Float> recoverUsed=new ArrayList<>();		//记录检查点
	private int recoverI=0;									//已记录几个检查点

	public Network(){
		this(DeviceConstants.DEFAULT_NET_TOTAL);
	}
	public Network(int total){
		this.total=total;
	}
	/**
	 * 使用的带宽 百分比
	 * @param used
	 */
	public void init(double used){
		if(used<=100)	{
			this.used=(int)(this.total*used/100);
			this.free=this.total-this.used;
			checkPoint();
		}		
	}
	@Override
	public boolean doTask(Task task) {
		float needNet=task.getNeedNet();
		if(needNet>free){
			return false;
		}else{
			malloc(needNet);
			return true;
		}
		
	}

	@Override
	public double getLoad() {
		double x=(this.used*1.0)/(this.total*1.0)*100;
		return x;
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
		INFO(String.format("%11s %10s %10s %8s", 
				"total(Mbps)","used(Mbps)","free(Mbps)","use%"));
		INFO(this);
	}

	@Override
	public void info() {
		INFO(this);
	}
	public String toString(){
		String s= String.format("[%10d %10f %10f %7.2f%%]"
				, total,used,free,getLoad());
		return s;
	}
	public int getTotal(){
		return total;
	}
	private void malloc(float used) {		
		this.used+=used;
		this.free=this.total-this.used;
	}

}
