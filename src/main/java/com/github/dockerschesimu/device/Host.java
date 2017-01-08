package com.github.dockerschesimu.device;

import com.github.dockerschesimu.manager.Task;
public class Host {

	private static int count=0;
	private final int id=count++;
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

	public void init(double icpu,int imem,double idisk,int inet){
		cpu.init(icpu);
		mem.init(imem);
		disk.init(idisk);
		net.init(inet);
		//checkPoint();  init时自动保存记录点,此处只需要增加recoverI
		recoverI++;		
	}
	public boolean doTask(Task task){
		return cpu.doTask(task)
				&&mem.doTask(task)
				&&disk.doTask(task)
				&&net.doTask(task);
		
	}
	public int recover(){
		int k=-1;
		if(recoverI>0){
			if(cpu.recover()==-1
					||mem.recover()==-1
					||disk.recover()==-1
					||net.recover()==-1)
			{
				return -1;
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
	
	public void monitor(){
		System.out.println("Host id="+id+" levl="+levl);
		System.out.println("cpu monitor:");
		cpu.monitor();
//		System.out.println("mem monitor:");
//		mem.monitor();
//		System.out.println("disk monitor:");
//		disk.monitor();
//		System.out.println("net monitor:");
//		net.monitor();
	}
	public void info(){
		System.out.println(this);
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




























