package com.github.wzt3309.dss.ga.device;

import com.github.wzt3309.dss.ga.manager.Task;
import static com.github.wzt3309.dss.ga.tools.BaseLogger.*;

public class Host {

	private static int count=0;
	private int id=count++;
	private Cpu cpu;
	private Memory mem;
	private Disk disk;
	private Network net;
	private int levl;
	
	private int recoverI=0;
	public Host(){
		
	}

	public Host(Cpu cpu, Memory mem, Disk disk, Network net,int levl) {
		super();
		this.cpu = cpu;
		this.mem = mem;
		this.disk = disk;
		this.net = net;
		this.levl=levl;
	}
	/**
	 * 初始化 主机各项资源，都是百分比
	 * @param icpu
	 * @param imem
	 * @param idisk
	 * @param inet
	 */
	public void init(double icpu,double imem,double idisk,double inet){
		cpu.init(icpu);
		mem.init(imem);
		disk.init(idisk);
		net.init(inet);
		//checkPoint();  init时自动保存记录点,此处只需要增加recoverI
		recoverI++;		
	}
	/**
	 * 
	 * @param task
	 * @return 没有成功的资源序号
	 */
	public int doTask(Task task){
		if(!cpu.doTask(task)){
			return 1;
		}
		if(!mem.doTask(task)){
			return 2;
		}
		if(!disk.doTask(task)){
			return 3;
		}
		if(!net.doTask(task)){
			return 4;
		}
		return 0;
		
	}
	public int doCpuTask(Task task){
		if(!cpu.doTask(task)){
			return 1;
		}
		return 0;
	}
	public int doMemTask(Task task){
		if(!mem.doTask(task)){
			return 1;
		}
		return 0;
	}
	public int doDiskTask(Task task){
		if(!disk.doTask(task)){
			return 1;
		}
		return 0;
	}
	public int doNetTask(Task task){
		if(!net.doTask(task)){
			return 1;
		}
		return 0;
	}
	/**
	 * 
	 * @return 哪项资源没恢复成功返回 -(1+n) 返回-1说明本host没有init
	 */
	public int recover(){
		int k=-1;
		if(recoverI>0){
			if(cpu.recover()==-1){
				return -2;
			}
			if(mem.recover()==-1){
				return -3;
			}
			if(disk.recover()==-1){
				return -4;
			}
			if(net.recover()==-1){
				return -5;
			}			
			k=recoverI-1;
		}		
		return k;
	}
	public void recover(int k){
		if(k<recoverI&&k>-1){
			cpu.recover(k);
			mem.recover(k);
			disk.recover(k);
			net.recover(k);
			recoverI=k+1;
		}		
	}
	
	public void checkPoint() {
		cpu.checkPoint();
		mem.checkPoint();
		disk.checkPoint();
		net.checkPoint();
		recoverI++;
	}
	
	public void mCpu(){
		INFO(">>>>>>>>>>STAT<<<<<<<<<<");
		INFO("Host id="+id+" levl="+levl);
//		INFO("CPU MONITOR:");
		cpu.monitor();
		INFO(">>>>>>>>>>END<<<<<<<<<<");
	}
	public void mMem(){
		INFO(">>>>>>>>>>STAT<<<<<<<<<<");
		INFO("Host id="+id+" levl="+levl);
//		INFO("MEM MONITOR:");
		mem.monitor();
		INFO(">>>>>>>>>>END<<<<<<<<<<");
	}
	public void mDisk(){
		INFO(">>>>>>>>>>STAT<<<<<<<<<<");
		INFO("Host id="+id+" levl="+levl);
//		INFO("DISK MONITOR:");
		disk.monitor();
		INFO(">>>>>>>>>>END<<<<<<<<<<");
	}
	public void mNet(){
		INFO(">>>>>>>>>>STAT<<<<<<<<<<");
		INFO("Host id="+id+" levl="+levl);
//		INFO("NET MONITOR:");
		net.monitor();
		INFO(">>>>>>>>>>END<<<<<<<<<<");
	}
	public void monitor(){
		INFO(">>>>>>>>>>STAT<<<<<<<<<<");
		INFO("Host id="+id+" levl="+levl);
//		INFO("CPU MONITOR:");
		cpu.info();
//		INFO("MEM MONITOR:");
		mem.monitor();
//		INFO("DISK MONITOR:");
		disk.monitor();
//		INFO("NET MONITOR:");
		net.monitor();
		INFO(">>>>>>>>>>END<<<<<<<<<<");
	}
	public void info(){
		INFO(this);
	}
	public Cpu getCpu() {
		return cpu;
	}

	public void setCpu(Cpu cpu) {
		this.cpu = cpu;
	}

	public Memory getMem() {
		return mem;
	}

	public void setMem(Memory mem) {
		this.mem = mem;
	}

	public Disk getDisk() {
		return disk;
	}

	public void setDisk(Disk disk) {
		this.disk = disk;
	}

	public Network getNet() {
		return net;
	}

	public void setNet(Network net) {
		this.net = net;
	}
	
	public int getLevl() {
		return levl;
	}

	public void setLevl(int levl) {
		this.levl = levl;
	}

	public String toString(){
		StringBuffer buf=new StringBuffer();
		buf.append("<host id="+id);
		switch(levl){
		case 1:buf.append("levl low");break;
		case 2:buf.append("levl mid");break;
		case 3:buf.append("levl high");break;
		default:;
		}
		if(cpu!=null){
			buf.append("\n");
			buf.append("cpu{ "+cpu.getName()+" "+cpu.getCoreNum()+"核 "
					+String.format("%.2f",cpu.getFrequency())+"GHz }");
		}
		if(mem!=null){
			buf.append(";\n");
			buf.append("mem{ "+String.format("%.2f", mem.getTotal()/1000000f)+"G }");
		}
		if(disk!=null){
			buf.append(";\n");
			buf.append("disk{ "+disk.getRpm()+"r/min "
					+disk.getMtr()+"Mb/s "+String.format("%.2f",disk.getAat())+" ms }");
		}
		if(net!=null){
			buf.append(";\n");
			buf.append("net{ "+net.getTotal()+"Mbps }");
		}
		buf.append("\n>");
		return buf.toString();
	}
	
}




























