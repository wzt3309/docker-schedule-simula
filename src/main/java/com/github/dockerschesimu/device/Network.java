package com.github.dockerschesimu.device;

import java.util.ArrayList;
import java.util.List;

import com.github.dockerschesimu.constant.DeviceConstants;
import com.github.dockerschesimu.manager.Task;

public class Network implements Device {
	
	private final int total;	//总带宽
	private int	used;			//被使用的带宽
	private int free;			//空闲带宽
	
	private List<Integer> recoverUsed=new ArrayList<>();	//记录检查点
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
		int needNet=task.getNeedNet();
		if(needNet>free){
			return false;
		}else{
			malloc(needNet);
			return true;
		}
		
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
		System.out.format("%10s %10s %10s %8s\n", 
				"total(Mbps)","used(Mbps)","free(Mbps)","use%");
		System.out.println(this);
	}

	@Override
	public void info() {
		System.out.println(this);
	}
	public String toString(){
		return String.format("[%10d %10d %10d %7.2f%%]"
				, total,used,free,getLoad());
	}
	public int getTotal(){
		return total;
	}
	private void malloc(int used) {		
		this.used+=used;
		this.free=this.total-this.used;
	}

}
