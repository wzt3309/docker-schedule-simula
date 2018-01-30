package com.github.wzt3309.dss.ga.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.wzt3309.dss.ga.manager.Cluster;
import com.github.wzt3309.dss.ga.manager.Task;
import com.github.wzt3309.dss.ga.tools.BaseMath;
import com.github.wzt3309.dss.ga.tools.BaseLogger;

public class DockerCluFunc implements FitnessFunction {
	
	private Cluster cluster;
	private List<Task> tasks;
	private int type;	//指示适应函数是测量cpu1  mem2 disk3 net4 还是全部0
	private List<int[]> failSches=new ArrayList<>();	//记录失败的分配计划
	
	public DockerCluFunc(Cluster cluster,List<Task> tasks,int type){
		if(!cluster.isInit()){
			BaseLogger.WARN("cluster 未初始化，执行默认初始化");
			cluster.init();			
		}
		this.cluster=cluster;
		this.tasks=tasks;
		this.type=type;
	}
	/**
	 * 打印当前最佳任务分配列表的结果
	 * @param sche
	 */
	public void pfitness(int[] sche){
		cluster.doTasks(sche,tasks,type);
		BaseLogger.INFO("the best list monitor:");
		switch(type){
		case 0:
			cluster.monitor();
			fitAll();
			break;
		case 1:
			cluster.mCpu();
			fitCpu();
			break;
		case 2:
			cluster.mMem();
			fitMem();
			break;
		case 3:
			cluster.mDisk();
			fitDisk();
			break;
		case 4:
			cluster.mNet();
			fitNet();
			break;
		}
		cluster.recover();
	}
	@Override
	public double fitness(int[] sche) {
		boolean doTaskRes=cluster.doTasks(sche,tasks,type);
		double fit=0;
		if(doTaskRes){
			switch(type){
			case 0:
				//cluster.monitor();
				fit=fitAll();
				break;
			case 1:
				//cluster.mCpu();
				fit=fitCpu();
				break;
			case 2:
				//cluster.mMem();
				fit=fitMem();
				break;
			case 3:
				//cluster.mDisk();
				fit=fitDisk();
				break;
			case 4:
				//cluster.mNet();
				fit=fitNet();
				break;
			}
		}else{
			failSches.add(sche);//记录失败的任务分配计划
			fit=Double.MAX_VALUE;
		}	
		cluster.recover();//一次任务分配完，不论成功与否，都要恢复集群
		return fit;
	}
	private double fitCpu(){
		double[] load=cluster.getCpuLoad();
		double fit= BaseMath.stander(load);
		return fit;
	}
	private double fitMem(){
		double[] load=cluster.getMemLoad();
		double fit=BaseMath.stander(load);
		return fit;
	}
	private double fitDisk(){
		double[] load=cluster.getDiskLoad();
		double fit=BaseMath.stander(load);
		return fit;
	}
	private double fitNet(){
		double[] load=cluster.getNetLoad();
		double fit=BaseMath.stander(load);
		return fit;
	}
	private double fitAll(){
		double cpuf,memf,diskf,netf;
		cpuf=fitCpu();
		memf=fitMem();
		diskf=fitDisk();
		netf=fitNet();
		
		return (cpuf+memf+diskf+netf)/4;
	}
	@Override
	public boolean canBeParent(int[] sche) {
		for(int[] fail:failSches){
			if(Arrays.equals(fail, sche)){
				return false;
			}
		}
		return true;
	}
}
