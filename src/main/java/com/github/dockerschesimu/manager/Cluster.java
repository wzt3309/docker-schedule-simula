package com.github.dockerschesimu.manager;

import static com.github.dockerschesimu.constant.DeviceConstants.DEVICE_LEVEL_HIGH;
import static com.github.dockerschesimu.constant.DeviceConstants.DEVICE_LEVEL_LOW;
import static com.github.dockerschesimu.constant.DeviceConstants.DEVICE_LEVEL_MID;
import static com.github.dockerschesimu.tools.BaseLogger.*;

import java.util.Arrays;
import java.util.List;

import com.github.dockerschesimu.device.Host;
import com.github.dockerschesimu.device.HostFactory;

/**
 * Docker集群对象
 * 1、从HostFactory中获取一组物理节点形成集群
 * 2、获取任务分配列表，根据列表将任务分配给每个host
 * @author wzt
 *
 */
public class Cluster {

	private List<Host> hosts;
	private int hlow;
	private int hmid;
	private int hhigh;
	private int hostNum;
	private boolean isInit=false;
	
//	public Cluster(){
//		INFO("------------集群 开始 创建------------");
//		INFO("------------集群 结束 创建------------");
//		info();
//	}
	public Cluster(int hostNum){
		INFO("------------------集群 开始 创建------------------");
		this.hostNum=hostNum;
		this.hmid=hostNum;
		hosts=HostFactory.simpleCreate(hostNum);		
		info();
		INFO("------------------集群 结束 创建------------------\n");
	}
	public Cluster(int low,int mid,int high){		
		INFO("------------------集群 开始 创建------------------");
		this.hlow=low;
		this.hmid=mid;
		this.hhigh=high;
		this.hostNum=low+mid+high;
		hosts=HostFactory.randomCreate(hostNum, low,mid,high);	
		info();
		INFO("------------------集群 结束 创建------------------\n");
	}
	public Cluster(Host...hosts){
		INFO("------------------集群 开始 创建------------------");
		this.hosts=Arrays.asList(hosts);
		this.hostNum=hosts.length;
		for(Host host:hosts){
			int lev=host.getLevl();
			if(lev==DEVICE_LEVEL_LOW){
				this.hlow++;
			}else if(lev==DEVICE_LEVEL_MID){
				this.hmid++;
			}else if(lev==DEVICE_LEVEL_HIGH){
				this.hhigh++;
			}
		}
		info();
		INFO("------------------集群 结束 创建------------------\n");
		
	}
	public void init(){
		INFO("------------------集群 开始 初始化-----------------");
		HostFactory.init(hosts);
		isInit=true;
		monitor();
		INFO("------------------集群 结束 初始化-----------------\n");		
	}
	public void init(double[][] params){
		INFO("------------------集群 开始 初始化-----------------");
		HostFactory.init(hosts,params);
		isInit=true;
		monitor();
		INFO("------------------集群 结束 初始化-----------------\n");		
	}
	public boolean doTasks(int[] sche,List<Task> tasks,int type){
		//INFO("------------集群 开始 执行任务------------");
		if(sche.length!=tasks.size()){
			ERROR("任务分配列表长度与任务组长度不一致! "
					+ "sche.len="+sche.length+" tasks.size="+tasks.size());
			return false;
		}
		int len=sche.length;
		int i=0;
		for(;i<len;i++){
			int hi=sche[i];
			Task taski=tasks.get(i);
			int resi=0;
			if((resi=doTask(hi,taski,type))!=0){
				ERROR("------------集群 失败 执行任务------------");
				pDoTaskError(hi,i,resi,taski);
				return false;
			}
		}
		//INFO("------------集群 结束 执行任务------------");
		return true;
	}
	public void recover(){
		//INFO("------------集群 开始 复原------------");
		for(int i=0;i<hosts.size();i++){
			int res=hosts.get(i).recover();
			if(res<0){
				ERROR("------------集群 失败 复原------------");
				pRecoverError(i,res);
			}
		}
		//monitor();
		//INFO("------------集群 结束 复原------------");
	}
	private void pDoTaskError(int hi,int taski,int resi,Task task){
		StringBuffer err=new StringBuffer();
		Host host=hosts.get(hi);
		err.append("do task fail! hosti="+hi+" taski="+taski);		
		switch(resi){
		case 1:
			err.append("task-->cpu "+task.pCpu());
			ERROR(err);
			host.mCpu();
			break;
		case 2:
			err.append("task-->mem "+task.pMem());
			ERROR(err);
			host.mMem();
			break;
		case 3:
			err.append("task-->disk "+task.pDisk());
			ERROR(err);
			host.mDisk();
			break;
		case 4:
			err.append("task-->net "+task.pNet());
			ERROR(err);
			host.mNet();
			break;
		}		
	}
	private void pRecoverError(int hi,int res){
		StringBuffer err=new StringBuffer();
		err.append("host recover fail! hosti="+hi);
		switch(res){
		case -2:
			err.append("cpu recover fail! cpu recoverI="
					+hosts.get(hi).getCpu().getRecoverI());
			break;
		case -3:
			err.append("mem recover fail! mem recoverI="
					+hosts.get(hi).getMem().getRecoverI());
			break;
		case -4:
			err.append("disk recover fail! disk recoverI="
					+hosts.get(hi).getDisk().getRecoverI());
			break;
		case -5:
			err.append("net recover fail! net recoverI="
					+hosts.get(hi).getNet().getRecoverI());
			break;
		}
		ERROR(err);
	}
	private int doTask(int hi,Task taski,int type){
		Host host=hosts.get(hi);
		int res=-1;
		switch(type){
		case 1:res=host.doCpuTask(taski);break;
		case 2:res=host.doMemTask(taski);break;
		case 3:res=host.doDiskTask(taski);break;
		case 4:res=host.doNetTask(taski);break;
		default:res=host.doTask(taski);break;
		}
		return res;
	}
	
	public double[][] getAllLoad(){
		double[][] allLoads=new double[4][hosts.size()];
		allLoads[0]=getCpuLoad();
		allLoads[1]=getMemLoad();
		allLoads[2]=getDiskLoad();
		allLoads[3]=getNetLoad();
		return allLoads;
	}
	public double[] getCpuLoad(){
		double[] loads=new double[hosts.size()];
		for(int i=0;i<loads.length;i++){
			loads[i]=hosts.get(i).getCpu().getLoad();
		}
		return loads;
	}
	public double[] getMemLoad(){
		double[] loads=new double[hosts.size()];
		for(int i=0;i<loads.length;i++){
			loads[i]=hosts.get(i).getMem().getLoad();
		}
		return loads;
	}
	public double[] getDiskLoad(){
		double[] loads=new double[hosts.size()];
		for(int i=0;i<loads.length;i++){
			loads[i]=hosts.get(i).getDisk().getLoad();
		}
		return loads;
	}
	public double[] getNetLoad(){
		double[] loads=new double[hosts.size()];
		for(int i=0;i<loads.length;i++){
			loads[i]=hosts.get(i).getNet().getLoad();
		}
		return loads;
	}
	public void info(){
		INFO(this);
	}
	public void mCpu(){
		INFO("Cluster CPU monitor:");
		for(Host host:hosts){
			host.mCpu();
		}
	}
	public void mMem(){
		INFO("Cluster MEM monitor:");
		for(Host host:hosts){
			host.mMem();
		}
	}
	public void mDisk(){
		INFO("Cluster DISK monitor:");
		for(Host host:hosts){
			host.mDisk();
		}
	}
	public void mNet(){
		INFO("Cluster NET monitor:");
		for(Host host:hosts){
			host.mNet();
		}
	}
	public void monitor(){
		INFO("Cluster All monitor:");
		for(Host host:hosts){			
			host.monitor();
		}
	}
	public String toString(){
		StringBuffer bf=new StringBuffer();
		bf.append("Cluster hostNum="+hostNum);
		bf.append(String.format(" [low=%d,mid=%d,high=%d]\n", hlow,hmid,hhigh));
		for(Host host:hosts){
			bf.append(host.toString());
			bf.append("\n");
		}
		return bf.toString();
	}
	public List<Host> getHosts() {
		return hosts;
	}
	public void setHosts(List<Host> hosts) {
		this.hosts = hosts;
	}
	public int getHlow() {
		return hlow;
	}
	public void setHlow(int hlow) {
		this.hlow = hlow;
	}
	public int getHmid() {
		return hmid;
	}
	public void setHmid(int hmid) {
		this.hmid = hmid;
	}
	public int getHhigh() {
		return hhigh;
	}
	public void setHhigh(int hhigh) {
		this.hhigh = hhigh;
	}
	public int getHostNum() {
		return hostNum;
	}
	public void setHostNum(int hostNum) {
		this.hostNum = hostNum;
	}
	public boolean isInit() {
		return isInit;
	}
	
}
